package com.luckybusiness.luckyapp.unit;

import com.luckybusiness.luckyapp.mapper.GraduateMapper;
import com.luckybusiness.luckyapp.model.dtos.*;
import com.luckybusiness.luckyapp.model.entities.CvData;
import com.luckybusiness.luckyapp.model.entities.Graduate;
import com.luckybusiness.luckyapp.model.entities.UniqueCode;
import com.luckybusiness.luckyapp.model.enums.Availability;
import com.luckybusiness.luckyapp.model.enums.Role;
import com.luckybusiness.luckyapp.repositories.CvDataRepository;
import com.luckybusiness.luckyapp.repositories.GraduateRepository;
import com.luckybusiness.luckyapp.repositories.UniqueCodeRepository;
import com.luckybusiness.luckyapp.services.impl.GraduateServiceImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class GraduateServiceImplTest {

    @Mock
    private GraduateRepository graduateRepository;

    @Mock
    private UniqueCodeRepository uniqueCodeRepository;

    @Mock
    private GraduateMapper graduateMapper;

    @Mock
    private CvDataRepository cvDataRepository;

    @InjectMocks
    private GraduateServiceImpl graduateService;

    private Graduate graduate1;
    private UniqueCode uniqueCode1;
    private GraduateDto graduateDto1;
    private CvData cvData1;
    private final String cvDataString =
            """
                    %PDF-1.4
                    1 0 obj
                    << /Type /Catalog /Pages 2 0 R >>
                    endobj
                    2 0 obj
                    << /Type /Pages /Kids [3 0 R] /Count 1 >>
                    endobj
                    3 0 obj
                    << /Type /Page /Parent 2 0 R /MediaBox [0 0 300 144] /Contents 4 0 R >>
                    endobj
                    4 0 obj
                    << /Length 44 >>
                    stream
                    BT /F1 24 Tf 100 100 Td (Hello PDF) Tj ET
                    endstream
                    endobj
                    xref
                    0 5
                    0000000000 65535 f\s
                    0000000010 00000 n\s
                    0000000061 00000 n\s
                    0000000116 00000 n\s
                    0000000215 00000 n\s
                    trailer
                    << /Root 1 0 R /Size 5 >>
                    startxref
                    295
                    %%EOF""";
    private final byte[] cvDataBytes = cvDataString.getBytes(StandardCharsets.US_ASCII);


    @BeforeEach
    void setUp() {
        uniqueCode1 = UniqueCode.builder().id(1L).code("testcode123").build();
        cvData1 = CvData.builder()
                .id(1L)
                .data(cvDataBytes)
                .fileName("cv.pdf")
                .contentType("application/pdf")
                .build();
        graduate1 = Graduate.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .uniqueCode(uniqueCode1)
                .gdprConsent(true)
                .profileVisible(true)
                .created(LocalDateTime.now().minusDays(1))
                .modified(LocalDateTime.now())
                .cvData(cvData1)
                .build();
        uniqueCode1.setGraduate(graduate1);
        cvData1.setGraduate(graduate1);

        UniqueCodeDto uniqueCodeDto1 = UniqueCodeDto.builder().code("testcode123").build();
        CvDataDto cvDataDto1 = CvDataDto.builder()
                .data(cvDataString)
                .fileName("cv.pdf")
                .contentType("application/pdf")
                .build();
        graduateDto1 = GraduateDto.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .uniqueCode(uniqueCodeDto1)
                .gdprConsent(true)
                .profileVisible(true)
                .cvData(cvDataDto1)
                .build();
    }

    @Test
    void getGraduateById_WhenFound_ShouldReturnGraduate() {
        when(graduateRepository.findGraduateByIdAndProfileVisibleAndGdprConsent(1L, true, true))
                .thenReturn(Optional.of(graduate1));

        Graduate found = graduateService.getGraduateById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(graduateRepository).findGraduateByIdAndProfileVisibleAndGdprConsent(1L, true, true);
    }

    @Test
    void getGraduateById_WhenNotFound_ShouldThrowNotFound() {
        when(graduateRepository.findGraduateByIdAndProfileVisibleAndGdprConsent(anyLong(), eq(true), eq(true)))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> graduateService.getGraduateById(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Absolvent nenalezen"));
    }

    @Test
    void getGraduateByUniqueCode_WhenCodeExistsAndGraduateAssigned_ShouldReturnGraduate() {
        when(uniqueCodeRepository.findByCode("testcode123")).thenReturn(Optional.of(uniqueCode1));
        when(graduateRepository.findById(1L)).thenReturn(Optional.of(graduate1));

        Optional<Graduate> result = graduateService.getGraduateByUniqueCode("testcode123");

        assertTrue(result.isPresent());
        assertEquals(graduate1.getId(), result.get().getId());
        verify(uniqueCodeRepository).findByCode("testcode123");
        verify(graduateRepository).findById(1L);
    }

    @Test
    void getGraduateByUniqueCode_WhenCodeExistsAndGraduateNotAssigned_ShouldReturnNewGraduateWithCode() {
        UniqueCode unassignedCode = UniqueCode.builder().id(3L).code("newcode789").graduate(null).build();
        when(uniqueCodeRepository.findByCode("newcode789")).thenReturn(Optional.of(unassignedCode));

        Optional<Graduate> result = graduateService.getGraduateByUniqueCode("newcode789");

        assertTrue(result.isPresent());
        assertNull(result.get().getId());
        assertNotNull(result.get().getUniqueCode());
        assertEquals("newcode789", result.get().getUniqueCode().getCode());
        verify(uniqueCodeRepository).findByCode("newcode789");
        verify(graduateRepository, never()).findById(anyLong());
    }

    @Test
    void getGraduateByUniqueCode_WhenCodeInvalid_ShouldThrowBadRequest() {
        when(uniqueCodeRepository.findByCode("invalidcode")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> graduateService.getGraduateByUniqueCode("invalidcode"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Neplatný uniqueCode"));
    }


    @Test
    void deleteGraduate_WhenCodeValid_ShouldDelete() {
        when(uniqueCodeRepository.findByGraduateIdAndCode(1L, "testcode123")).thenReturn(Optional.of(new UniqueCode()));

        Graduate mockGraduate = Graduate.builder().id(1L).uniqueCode(new UniqueCode()).build(); // Jednoduchý mock absolventa
        when(graduateRepository.findById(1L)).thenReturn(Optional.of(mockGraduate));

        graduateService.deleteGraduate(1L, "testcode123");

        verify(uniqueCodeRepository).findByGraduateIdAndCode(1L, "testcode123");        verify(graduateRepository).deleteById(1L);
    }

    @Test
    void deleteGraduate_WhenCodeInvalidOrNotFound_ShouldThrowNotFound() {
        when(uniqueCodeRepository.findByGraduateIdAndCode(1L, "wrongcode")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> graduateService.deleteGraduate(1L, "wrongcode"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Neplatná kombinace id a uniqueCode"));
        verify(graduateRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateGraduate_WhenValid_ShouldCallUpdateMapperAndSave() {
        graduateDto1.setId(1L);
        graduateDto1.setFirstName("UpdatedFirstName");
        byte[] updatedCvBytes = cvDataString.getBytes(StandardCharsets.US_ASCII);
        String updatedCvString = Base64.getEncoder().encodeToString(updatedCvBytes);

        CvDataDto updatedCvDataDto = CvDataDto.builder()
                .data(updatedCvString) // Use Base64 string
                .fileName("updated_cv.pdf")
                .contentType("application/pdf")
                .build();
        graduateDto1.setCvData(updatedCvDataDto);

        when(graduateRepository.findById(1L)).thenReturn(Optional.of(graduate1));
        doNothing().when(graduateMapper).updateEntityFromDto(any(GraduateDto.class), any(Graduate.class));
        when(graduateRepository.save(any(Graduate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        long resultId = graduateService.updateGraduate(graduateDto1, 1L);

        assertEquals(1L, resultId);

        verify(graduateRepository).findById(1L);
        verify(graduateMapper).updateEntityFromDto(eq(graduateDto1), eq(graduate1));

        ArgumentCaptor<Graduate> graduateCaptor = ArgumentCaptor.forClass(Graduate.class);
        verify(graduateRepository).save(graduateCaptor.capture());
        Graduate savedGraduate = graduateCaptor.getValue();

        assertNotNull(savedGraduate.getModified());
        assertNotNull(savedGraduate.getCvData());
        assertArrayEquals(updatedCvBytes, savedGraduate.getCvData().getData()); // Verify decoded bytes in entity
        assertEquals("updated_cv.pdf", savedGraduate.getCvData().getFileName());

        verify(graduateMapper, never()).mapForCreation(any(GraduateDto.class));
    }

    @Test
    void createGraduate_WhenValid_ShouldMapAndSaveWithCvData() {
        String newCvString = Base64.getEncoder().encodeToString(cvDataBytes);
        CvDataDto newCvDataDto = CvDataDto.builder()
                .data(newCvString) // Use Base64 string
                .fileName("new_cv.pdf")
                .contentType("application/pdf")
                .build();
        GraduateDto newGraduateDto = GraduateDto.builder()
                .firstName("New")
                .lastName("Graduate")
                .uniqueCode(UniqueCodeDto.builder().code("newcode789").build())
                .cvData(newCvDataDto)
                .build();
        newGraduateDto.setId(null);

        UniqueCode unassignedCode = UniqueCode.builder().id(3L).code("newcode789").build();
        Graduate mappedGraduate = Graduate.builder()
                .firstName("New")
                .lastName("Graduate")
                .build();

        Graduate savedGraduateEntity = Graduate.builder()
                .id(5L)
                .firstName("New")
                .lastName("Graduate")
                .uniqueCode(unassignedCode)
                .created(LocalDateTime.now())
                .role(Role.GRADUATE)
                .cvData(CvData.builder().id(10L).data(cvDataBytes).fileName("new_cv.pdf").contentType("application/pdf").build())
                .build();

        when(uniqueCodeRepository.findByCode("newcode789")).thenReturn(Optional.of(unassignedCode));
        when(graduateMapper.mapForCreation(newGraduateDto)).thenReturn(mappedGraduate);
        when(graduateRepository.save(any(Graduate.class))).thenReturn(savedGraduateEntity);

        long savedId = graduateService.createGraduate(newGraduateDto);

        ArgumentCaptor<Graduate> graduateCaptor = ArgumentCaptor.forClass(Graduate.class);
        verify(graduateRepository).save(graduateCaptor.capture());
        Graduate entityToSave = graduateCaptor.getValue();

        assertEquals(5L, savedId);
        assertEquals("New", entityToSave.getFirstName());
        assertNotNull(entityToSave.getCreated());
        assertNotNull(entityToSave.getRole());
        assertEquals(unassignedCode, entityToSave.getUniqueCode());
        assertNotNull(entityToSave.getCvData());
        assertArrayEquals(cvDataBytes, entityToSave.getCvData().getData()); // Verify decoded bytes
        assertEquals("new_cv.pdf", entityToSave.getCvData().getFileName());
        assertEquals("application/pdf", entityToSave.getCvData().getContentType());
        assertEquals(entityToSave, entityToSave.getCvData().getGraduate()); // Check relation is set

        verify(graduateMapper).mapForCreation(eq(newGraduateDto));
        verify(graduateMapper, never()).updateEntityFromDto(any(), any());
        verify(graduateRepository, never()).findById(anyLong());
    }

    @Test
    void createGraduate_WhenDtoOrCodeMissing_ShouldThrowBadRequest() {
        ResponseStatusException exception1 = assertThrows(ResponseStatusException.class, () -> graduateService.createGraduate(null));
        assertEquals(HttpStatus.BAD_REQUEST, exception1.getStatusCode());
        assertTrue(exception1.getReason().contains("GraduateDto nesmí být null"));

        GraduateDto dtoWithoutCode = GraduateDto.builder().id(null).uniqueCode(null).build();
        ResponseStatusException exception2 = assertThrows(ResponseStatusException.class, () -> graduateService.createGraduate(dtoWithoutCode));
        assertEquals(HttpStatus.BAD_REQUEST, exception2.getStatusCode());
        assertTrue(exception2.getReason().contains("Neplatný uniqueCode"));

        GraduateDto dtoWithCodeValueNull = GraduateDto.builder().id(null).uniqueCode(UniqueCodeDto.builder().code(null).build()).build();
        ResponseStatusException exception3 = assertThrows(ResponseStatusException.class, () -> graduateService.createGraduate(dtoWithCodeValueNull));
        assertEquals(HttpStatus.BAD_REQUEST, exception3.getStatusCode());
        assertTrue(exception3.getReason().contains("Neplatný uniqueCode"));
    }

    @Test
    void createGraduate_WhenIdNotNull_ShouldThrowBadRequest() {
        GraduateDto dtoWithId = GraduateDto.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Fail")
                .uniqueCode(UniqueCodeDto.builder().code("somecode").build())
                .cvData(CvDataDto.builder().data("dGVzdA==").fileName("a.pdf").contentType("a/b").build()) // Add required CV data
                .build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> graduateService.createGraduate(dtoWithId));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Id musí být null"));
    }

    @Test
    void createGraduate_WhenCodeAssigned_ShouldThrowBadRequest() {
        ContractTypesDto dummyContracts = ContractTypesDto.builder().hpp(true).build();
        EmploymentTypesDto dummyEmpTypes = EmploymentTypesDto.builder().fullTime(true).build();
        EmploymentRegimesDto dummyRegimes = EmploymentRegimesDto.builder().onSite(true).build();
        PreferredRolesDto dummyRoles = PreferredRolesDto.builder().tester(true).build();
        WorkRegionsDto dummyRegions = WorkRegionsDto.builder().praha(true).build();
        CvDataDto dummyCvData = CvDataDto.builder()
                .fileName("dummy.pdf")
                .contentType("application/pdf")
                .data(Base64.getEncoder().encodeToString(new byte[]{1}))
                .build();

        GraduateDto dtoWithExistingCode = GraduateDto.builder()
                .id(null)
                .firstName("Test")
                .lastName("Fail")
                .uniqueCode(UniqueCodeDto.builder().code("assignedCode").build())
                .contractTypes(dummyContracts)
                .employmentTypes(dummyEmpTypes)
                .employmentRegimes(dummyRegimes)
                .preferredRoles(dummyRoles)
                .workRegions(dummyRegions)
                .cvData(dummyCvData)
                .languages("Czech")
                .availability(Availability.IMMEDIATELY)
                .gdprConsent(true)
                .build();

        UniqueCode existingCode = UniqueCode.builder().id(99L).code("assignedCode").graduate(new Graduate()).build();

        when(uniqueCodeRepository.findByCode("assignedCode")).thenReturn(Optional.of(existingCode));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> graduateService.createGraduate(dtoWithExistingCode));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Neplatný uniqueCode"));
    }

    @Test
    void createGraduate_WhenCvDataDtoMissing_ShouldThrowBadRequest() {
        GraduateDto dtoWithoutCv = GraduateDto.builder()
                .id(null)
                .firstName("Test")
                .lastName("Fail")
                .uniqueCode(UniqueCodeDto.builder().code("unassignedCode").build())
                .cvData(null)
                .build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> graduateService.createGraduate(dtoWithoutCv));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void createGraduate_WhenCvDataDtoDataIsNull_ShouldThrowBadRequest() {
        GraduateDto dtoWithNullCvData = GraduateDto.builder()
                .id(null)
                .firstName("Test")
                .lastName("Fail")
                .uniqueCode(UniqueCodeDto.builder().code("unassignedCode").build())
                .cvData(CvDataDto.builder().data(null).fileName("a.pdf").contentType("a/b").build()) // Null data
                .build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> graduateService.createGraduate(dtoWithNullCvData));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        System.out.println(exception.getReason());
        assertTrue(exception.getReason().contains("Nahrání CV je povinné."));
    }

    @Test
    void updateGraduate_WhenCvDataDtoIsNull_ShouldNotUpdateCv() {
        graduateDto1.setId(1L);
        graduateDto1.setFirstName("UpdatedFirstName");
        graduateDto1.setCvData(null); // No CV data in update DTO

        when(graduateRepository.findById(1L)).thenReturn(Optional.of(graduate1));
        doNothing().when(graduateMapper).updateEntityFromDto(any(GraduateDto.class), any(Graduate.class));
        when(graduateRepository.save(any(Graduate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        graduateService.updateGraduate(graduateDto1, 1L);

        ArgumentCaptor<Graduate> graduateCaptor = ArgumentCaptor.forClass(Graduate.class);
        verify(graduateRepository).save(graduateCaptor.capture());
        Graduate savedGraduate = graduateCaptor.getValue();

        assertNotNull(savedGraduate.getCvData());
        assertArrayEquals(cvDataBytes, savedGraduate.getCvData().getData()); // Original CV bytes should remain
        assertEquals("cv.pdf", savedGraduate.getCvData().getFileName());
    }

    @Test
    void getCv_WhenCvExists_ShouldReturnCvData() {
        when(cvDataRepository.findByGraduateId(1L)).thenReturn(Optional.of(cvData1));

        CvData foundCvData = graduateService.getCv(1L);

        assertNotNull(foundCvData);
        assertArrayEquals(cvData1.getData(), foundCvData.getData());
        assertEquals(cvData1.getFileName(), foundCvData.getFileName());
        assertEquals(cvData1.getContentType(), foundCvData.getContentType());
        verify(cvDataRepository).findByGraduateId(1L);
    }


    @Test
    void getCv_WhenCvNotExists_ShouldThrowNotFound() {
        when(cvDataRepository.findByGraduateId(2L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> graduateService.getCv(2L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("CV nenalezeno"));
    }

    @Test
    void setGraduateVisibility_WhenGraduateExists_ShouldSetVisibility() {
        when(uniqueCodeRepository.findByGraduateIdAndCode(1L, "testcode123")).thenReturn(Optional.of(new UniqueCode()));
        when(graduateRepository.findById(1L)).thenReturn(Optional.of(graduate1));
        when(graduateRepository.save(any(Graduate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        graduateService.setGraduateVisibility(1L, "testcode123", false);

        ArgumentCaptor<Graduate> graduateCaptor = ArgumentCaptor.forClass(Graduate.class);
        verify(graduateRepository).save(graduateCaptor.capture());
        Graduate savedGraduate = graduateCaptor.getValue();

        assertFalse(savedGraduate.isProfileVisible());
        assertNotNull(savedGraduate.getModified());

        reset(graduateRepository);

        when(uniqueCodeRepository.findByGraduateIdAndCode(1L, "testcode123")).thenReturn(Optional.of(new UniqueCode()));
        when(graduateRepository.findById(1L)).thenReturn(Optional.of(savedGraduate));
        when(graduateRepository.save(any(Graduate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        graduateService.setGraduateVisibility(1L, "testcode123", true);

        verify(graduateRepository).save(graduateCaptor.capture());
        savedGraduate = graduateCaptor.getValue();

        assertTrue(savedGraduate.isProfileVisible());
        assertNotNull(savedGraduate.getModified());
    }

    @Test
    void setGraduateVisibility_WhenGraduateNotFound_ShouldThrowNotFound() {
        when(uniqueCodeRepository.findByGraduateIdAndCode(99L, "testcode123")).thenReturn(Optional.of(new UniqueCode()));
        when(graduateRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> graduateService.setGraduateVisibility(99L, "testcode123", true));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Absolvent nenalezen"));
        verify(graduateRepository, never()).save(any());
    }

    @Test
    void createGraduate_WithValidPdf_ShouldPassValidation() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PDDocument doc = new PDDocument()) {
            doc.addPage(new PDPage());
            doc.save(baos);
        }
        byte[] validPdfBytes = baos.toByteArray();
        String validPdfBase64 = Base64.getEncoder().encodeToString(validPdfBytes);

        CvDataDto validCvData = CvDataDto.builder()
                .fileName("valid.pdf")
                .contentType("application/pdf")
                .data(validPdfBase64)
                .build();
        GraduateDto newGraduate = GraduateDto.builder()
                .firstName("Valid")
                .lastName("Pdf")
                .uniqueCode(UniqueCodeDto.builder().code("newValidCode").build())
                .cvData(validCvData)
                .build();

        UniqueCode unassignedCode = UniqueCode.builder().id(7L).code("newValidCode").build();
        when(uniqueCodeRepository.findByCode("newValidCode")).thenReturn(Optional.of(unassignedCode));
        when(graduateMapper.mapForCreation(any(GraduateDto.class))).thenReturn(new Graduate());
        when(graduateRepository.save(any(Graduate.class))).thenReturn(Graduate.builder().id(99L).build());

        long id = graduateService.createGraduate(newGraduate);

        assertEquals(99L, id);
        verify(graduateRepository).save(any());
    }

    @Test
    void createGraduate_WithInvalidPdf_ShouldThrowBadRequest() {
        byte[] invalidPdfBytes = "not a pdf".getBytes();
        String invalidPdfBase64 = Base64.getEncoder().encodeToString(invalidPdfBytes);

        CvDataDto invalidCvData = CvDataDto.builder()
                .fileName("invalid.pdf")
                .contentType("application/pdf")
                .data(invalidPdfBase64)
                .build();
        GraduateDto newGraduate = GraduateDto.builder()
                .firstName("Invalid")
                .lastName("Pdf")
                .uniqueCode(UniqueCodeDto.builder().code("newInvalidCode").build())
                .cvData(invalidCvData)
                .build();

        UniqueCode unassignedCode = UniqueCode.builder().id(8L).code("newInvalidCode").build();
        when(uniqueCodeRepository.findByCode("newInvalidCode")).thenReturn(Optional.of(unassignedCode));

        // ✔️ Doplnit mock mapForCreation, aby nevrátil null
        when(graduateMapper.mapForCreation(any())).thenReturn(new Graduate());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> graduateService.createGraduate(newGraduate));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Obsah souboru není validní"));
    }

}