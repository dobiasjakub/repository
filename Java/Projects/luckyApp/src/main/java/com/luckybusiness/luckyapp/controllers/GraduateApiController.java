package com.luckybusiness.luckyapp.controllers;

import com.luckybusiness.luckyapp.filters.GraduateFilter;
import com.luckybusiness.luckyapp.mapper.GraduateMapper;
import com.luckybusiness.luckyapp.model.dtos.GraduateDto;
import com.luckybusiness.luckyapp.model.dtos.GraduatePreviewDto;
import com.luckybusiness.luckyapp.services.GraduateService;
import com.luckybusiness.luckyapp.validations.OnCreate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/graduates")
@Tag(name = "Absolventi", description = "API pro správu profilů absolventů včetně filtrování, získávání detailů a úprav profilu.")
public class GraduateApiController {

    private final GraduateService graduateService;
    private final GraduateMapper graduateMapper;

    protected GraduateApiController(GraduateService graduateService, GraduateMapper graduateMapper) {
        this.graduateService = graduateService;
        this.graduateMapper = graduateMapper;
    }

    @PostMapping("list")
    @Operation(summary = "Získání seznamu absolventů dle filtru",
            description = "Vrací seznam absolventů (pouze preview basic data), kteří odpovídají zadaným kritériím filtrování a mají viditelný profil a souhlas GDPR. Lze filtrovat podle kraje, typu úvazku, preferované role a pracovního režimu.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Seznam nalezených absolventů",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = GraduatePreviewDto.class))))
    })
    public ResponseEntity<List<GraduatePreviewDto>> getGraduates(
            @RequestBody(description = "Filtr pro vyhledávání absolventů. Pokud jsou všechny hodnoty false, vrátí všechny viditelné absolventy.", required = true,
                    content = @Content(schema = @Schema(implementation = GraduateFilter.class)))
            @org.springframework.web.bind.annotation.RequestBody(required = false) GraduateFilter filter
    ) {
        if (filter == null)
            filter = new GraduateFilter();

        var graduates = graduateService.getGraduates(filter);

        var result = graduates.stream()
                .map(graduateMapper::entityToPreviewDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("")
    @Operation(summary = "Získání absolventa podle unique code (pro editaci)",
            description = "Vrací detail absolventa včetně uniqueCode na základě unikátního identifikátoru `uniqueCode`. " +
                    "Pokud kód existuje a zatím není přiřazený k žádnému absolventovi, vrátí se prázdná DTO struktura s vyplněným `uniqueCode` pro vytvoření nového profilu. " +
                    "Tento endpoint slouží primárně pro absolventa k načtení jeho profilu pro vytvoření a úpravu.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Úspěšně nalezený absolvent (existující nebo nový placeholder)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GraduateDto.class))),
            @ApiResponse(responseCode = "400", description = "Neplatný formát nebo neexistující unique code", content = @Content)
    })
    public ResponseEntity<GraduateDto> getNewOrExistingGraduateByUniqueCode(
            @Parameter(description = "Unikátní kód absolventa pro identifikaci a autorizaci.", required = true, example = "uuid-abc-123")
            @RequestParam String uniqueCode) {
        var graduate = graduateService.getGraduateByUniqueCode(uniqueCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid unique code"));

        return ResponseEntity.ok(graduateMapper.entityToDtoWithUniqueCode(graduate));
    }

    @GetMapping("{id}")
    @Operation(summary = "Získání veřejného profilu absolventa podle ID",
            description = "Vrací veřejný detail absolventa bez binárních dat CV v poli `cvData.data`) dle databázového ID. Slouží pro zobrazení profilu veřejnosti (např. firmám). Vrací pouze profily, které jsou viditelné a mají souhlas GDPR.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Úspěšně nalezený viditelný absolvent",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GraduateDto.class))),
            @ApiResponse(responseCode = "404", description = "Absolvent nenalezen nebo není viditelný", content = @Content)
    })
    public ResponseEntity<GraduateDto> getGraduateById(
            @Parameter(description = "Databázové ID absolventa.", required = true, example = "123")
            @PathVariable(name = "id") Long id) {
        var graduate = graduateService.getGraduateById(id);

        return ResponseEntity.ok(graduateMapper.entityToDto(graduate));
    }

    @PostMapping("")
    @Operation(summary = "Vytvořit nový profil absolventa",
            description = """
                    Vytvoří nový profil absolventa na základě zaslaných dat v `GraduateDto`.
                    **DŮLEŽITÉ:** Pro vytvoření profilu je **povinné** v zaslaném JSONu (`GraduateDto`) vyplnit pole `cvData`.
                    ID v `GraduateDto` musí být `null`.
                    
                    Objekt v poli `cvData` musí obsahovat:
                    1.  `fileName`: Název souboru (např. "zivotopis.pdf").
                    2.  `contentType`: MIME typ souboru (povolen pouze "application/pdf").
                    3.  `data`: Samotný obsah CV jako **Base64 enkódovaný string**.

                    Služba na backendu tento Base64 string dekóduje a uloží jako binární data (`byte[]`) do databáze.
                    Pro samotné vytvoření je dále nutné, aby JSON obsahoval povinné údaje, a platný, dosud nepřiřazený `uniqueCode`."""
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profil úspěšně vytvořen, vrací ID nového absolventa",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "integer", format = "int64", description = "ID nově vytvořeného absolventa", example = "124"))),
            @ApiResponse(responseCode = "400", description = "Neplatná vstupní data (chybějící/neplatný uniqueCode, ID není null, chybějící nebo nevalidní cvData včetně chybějícího Base64 řetězce v poli 'data', validační chyby DTO)", content = @Content)
    })
    public ResponseEntity<Long> createGraduate(
            @RequestBody(description = "Data nového absolventa. Musí obsahovat platný `uniqueCode` a `id` musí být null. " +
                    "Pole `cvData` **musí** obsahovat objekt s vyplněným `fileName`, `contentType` a Base64 stringem v poli `data`.", required = true,
                    content = @Content(schema = @Schema(implementation = GraduateDto.class)))
            @Validated(OnCreate.class) @Valid @org.springframework.web.bind.annotation.RequestBody GraduateDto graduateDto
    ) {
        var id = graduateService.createGraduate(graduateDto);
        return ResponseEntity.status(201).body(id);
    }


    @PutMapping("{id}")
    @Operation(summary = "Aktualizovat existující profil absolventa",
            description = """
                    Aktualizuje existující profil absolventa na základě zaslaných dat v `GraduateDto`. Pro autorizaci je nutné, aby JSON obsahoval platný `uniqueCode` odpovídající danému `id` absolventa. `id` v JSON těle musí odpovídat `id` v URL.

                    **Aktualizace CV:**
                    * Pokud chcete nahrát **nové CV**, vložte do pole `cvData` v `GraduateDto` objekt obsahující `fileName`, `contentType` a nový obsah CV jako **Base64 enkódovaný string** v poli `data`. Backend dekóduje string a přepíše stávající CV v databázi.
                    * Pokud **nechcete měnit CV**, v cvData nechte původní objekt s `fileName` a `contentType` a polem `data` nastavým na `null`. Backend v tomto případě stávající CV v databázi **nezmění**.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profil úspěšně aktualizován, vrací ID aktualizovaného absolventa",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(type = "integer", format = "int64", description = "ID aktualizovaného absolventa", example = "123"))),
            @ApiResponse(responseCode = "400", description = "Neplatná vstupní data (nesoulad ID, chybějící/neplatný uniqueCode, validační chyby DTO, nevalidní Base64 data v cvData.data pokud je poskytnuto)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Absolvent s daným ID nebyl nalezen", content = @Content)
    })
    public ResponseEntity<Long> updateGraduate(
            @Parameter(description = "ID absolventa, kterého chceme aktualizovat.", required = true, example = "123")
            @PathVariable Long id,
            @RequestBody(description = "Aktualizovaná data absolventa. Musí obsahovat správný `uniqueCode` a `id` shodné s ID v URL. " +
                    "Pokud má být aktualizováno i CV, pole `cvData` musí obsahovat objekt `CvDataDto` s `fileName`, `contentType` a Base64 stringem v poli `data`. Pokud CV nemá být změněno, pole `cvData.data` musí být `null` nebo celý objekt `cvData` může být `null`.", required = true,
                    content = @Content(schema = @Schema(implementation = GraduateDto.class)))
            @Valid @org.springframework.web.bind.annotation.RequestBody GraduateDto graduateDto
    ) {
        var updatedEntityId = graduateService.updateGraduate(graduateDto, id);
        return ResponseEntity.ok(updatedEntityId);
    }


    @DeleteMapping("{id}")
    @Operation(summary = "Smazat profil absolventa",
            description = "Smaže profil absolventa podle ID. Operace vyžaduje ověření pomocí `uniqueCode`.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Absolvent úspěšně smazán", content = @Content),
            @ApiResponse(responseCode = "404", description = "Absolvent nenalezen pro dané ID a uniqueCode", content = @Content)
    })
    public ResponseEntity<Void> deleteGraduate(
            @Parameter(description = "ID absolventa ke smazání.", required = true, example = "123")
            @PathVariable Long id,
            @Parameter(description = "Unikátní kód pro ověření oprávnění ke smazání.", required = true, example = "uuid-abc-123")
            @RequestParam String uniqueCode) {
        graduateService.deleteGraduate(id, uniqueCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("cv/{id}")
    @Operation(summary = "Stáhnout životopis (CV) absolventa",
            description = "Stáhne životopis (CV) absolventa podle ID. Vrací soubor k přímému stažení (binární data).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CV úspěšně staženo",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "404", description = "CV nenalezeno (absolvent neexistuje nebo nemá nahrané CV)", content = @Content)
    })
    public ResponseEntity<Resource> downloadCv(
            @Parameter(description = "ID absolventa, jehož CV se má stáhnout.", required = true, example = "123")
            @PathVariable Long id) {

        var cvData = graduateService.getCv(id);

        ByteArrayResource resource = new ByteArrayResource(cvData.getData());

        MediaType contentType = MediaType.parseMediaType(cvData.getContentType() != null ? cvData.getContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE);
        String filename = cvData.getFileName() != null && !cvData.getFileName().isBlank()
                ? cvData.getFileName()
                : "absolvent_" + id + "_cv.pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(contentType)
                .contentLength(cvData.getData().length)
                .body(resource);
    }

    @PutMapping("visibility/{id}/{visibility}")
    @Operation(summary = "Nastavit viditelnost profilu absolventa",
            description = "Nastaví viditelnost profilu absolventa (true = viditelný, false = skrytý). Operace vyžaduje ověření pomocí `uniqueCode`.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Viditelnost profilu úspěšně aktualizována", content = @Content),
            @ApiResponse(responseCode = "404", description = "Absolvent nenalezen pro dané ID a uniqueCode", content = @Content)
    })
    public ResponseEntity<Void> setGraduateVisibility(
            @Parameter(description = "ID absolventa, jehož viditelnost se mění.", required = true, example = "123")
            @PathVariable Long id,
            @Parameter(description = "Požadovaná viditelnost profilu (true nebo false).", required = true, example = "true")
            @PathVariable boolean visibility,
            @Parameter(description = "Unikátní kód pro autorizaci změny.", required = true, example = "uuid-abc-123")
            @RequestParam String uniqueCode
    ) {

        graduateService.setGraduateVisibility(id, uniqueCode, visibility);
        return ResponseEntity.ok().build();
    }
}