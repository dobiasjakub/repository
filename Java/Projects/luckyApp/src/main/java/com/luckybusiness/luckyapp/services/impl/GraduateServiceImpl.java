package com.luckybusiness.luckyapp.services.impl;

import com.luckybusiness.luckyapp.filters.GraduateFilter;
import com.luckybusiness.luckyapp.filters.specifications.GraduateSpecification;
import com.luckybusiness.luckyapp.mapper.GraduateMapper;
import com.luckybusiness.luckyapp.model.dtos.CvDataDto;
import com.luckybusiness.luckyapp.model.dtos.GraduateDto;
import com.luckybusiness.luckyapp.model.entities.*;
import com.luckybusiness.luckyapp.model.enums.Role;
import com.luckybusiness.luckyapp.repositories.CvDataRepository;
import com.luckybusiness.luckyapp.repositories.GraduateRepository;
import com.luckybusiness.luckyapp.repositories.UniqueCodeRepository;
import com.luckybusiness.luckyapp.services.GraduateService;
import org.apache.logging.log4j.util.Strings;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
public class GraduateServiceImpl implements GraduateService {
    private final GraduateRepository graduateRepository;
    private final UniqueCodeRepository uniqueCodeRepository;
    private final GraduateMapper graduateMapper;
    private final CvDataRepository cvDataRepository;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    private static final String[] ALLOWED_MIME_TYPES = {
            "application/pdf",
    };

    public GraduateServiceImpl(GraduateRepository graduateRepository, UniqueCodeRepository uniqueCodeRepository, GraduateMapper graduateMapper, CvDataRepository cvDataRepository) {
        this.graduateRepository = graduateRepository;
        this.uniqueCodeRepository = uniqueCodeRepository;
        this.graduateMapper = graduateMapper;
        this.cvDataRepository = cvDataRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Graduate> getGraduates(GraduateFilter filter) {
        Specification<Graduate> spec = GraduateSpecification.filterBy(filter)
                .and((root, query, cb) -> cb.isTrue(root.get("profileVisible")))
                .and((root, query, cb) -> cb.isTrue(root.get("gdprConsent")));

        Sort sort = Sort.by(Sort.Direction.ASC, "created");

        return graduateRepository.findAll(spec, sort);
    }

    @Override
    @Transactional(readOnly = true)
    public Graduate getGraduateById(Long id) {
        return graduateRepository.findGraduateByIdAndProfileVisibleAndGdprConsent(id, true, true)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Absolvent nenalezen"));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Graduate> getGraduateByUniqueCode(String uniqueCode) {
        var dbUniqueCode = uniqueCodeRepository.findByCode(uniqueCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Neplatný uniqueCode"));

        if (dbUniqueCode.getGraduate() != null && dbUniqueCode.getGraduate().getId() != null) {
            return graduateRepository.findById(dbUniqueCode.getGraduate().getId());
        } else {
            var graduate = new Graduate();
            graduate.setUniqueCode(dbUniqueCode);
            graduate.setContractTypes(new ContractTypes());
            graduate.setEmploymentTypes(new EmploymentTypes());
            graduate.setPreferredRoles(new PreferredRoles());
            graduate.setEmploymentRegimes(new EmploymentRegimes());
            graduate.setCvData(new CvData());
            graduate.setWorkRegions(new WorkRegions());
            return Optional.of(graduate);
        }
    }


    @Override
    @Transactional
    public void deleteGraduate(Long id, String uniqueCode) {
        if (uniqueCodeRepository.findByGraduateIdAndCode(id, uniqueCode).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Neplatná kombinace id a uniqueCode");
        }

        Graduate graduateToDelete = graduateRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Absolvent s id %s nenalezen", id)));

        UniqueCode codeToKeep = graduateToDelete.getUniqueCode();

        if (codeToKeep != null) {
            codeToKeep.setGraduate(null);
        }

        graduateRepository.deleteById(id);
    }

    @Override
    @Transactional
    public long updateGraduate(GraduateDto dto, Long id) {
        validateGraduateDto(dto, false);

        var dbGraduate = graduateRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Absolvent nenalezen"));

        if (!dbGraduate.getId().equals(dto.getId()) || dbGraduate.getUniqueCode().getCode() == null || !dbGraduate.getUniqueCode().getCode().equals(dto.getUniqueCode().getCode()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Neplatný uniqueCode");

        graduateMapper.updateEntityFromDto(dto, dbGraduate);
        dbGraduate.setModified(LocalDateTime.now());

        if (dto.getCvData() != null && !isBlank(dto.getCvData().getData())) {
            CvData file = processCvDataDto(dto.getCvData());

            validateFile(file);
            CvData existingCvData = dbGraduate.getCvData();

            existingCvData.setData(file.getData());
            existingCvData.setFileName(file.getFileName());
            existingCvData.setContentType(file.getContentType());
        }

        Graduate saved = graduateRepository.save(dbGraduate);
        return saved.getId();
    }

    @Override
    @Transactional
    public long createGraduate(GraduateDto dto) {
        validateGraduateDto(dto, true);
        var dbUniqueCode = uniqueCodeRepository.findByCode(dto.getUniqueCode().getCode()).orElse(null);

        if (dbUniqueCode == null || dbUniqueCode.getGraduate() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Neplatný uniqueCode");

        Graduate dbGraduate = graduateMapper.mapForCreation(dto);

        dbGraduate.setCreated(LocalDateTime.now());
        dbGraduate.setRole(Role.GRADUATE);
        dbGraduate.setUniqueCode(dbUniqueCode);

        dbUniqueCode.setGraduate(dbGraduate);

        CvData cvDataEntity = processCvDataDto(dto.getCvData());
        validateFile(cvDataEntity);
        cvDataEntity.setGraduate(dbGraduate);
        dbGraduate.setCvData(cvDataEntity);

        Graduate saved = graduateRepository.save(dbGraduate);
        return saved.getId();
    }

    private void validateGraduateDto(GraduateDto dto, boolean onCreated) {
        if (dto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GraduateDto nesmí být null");

        if (dto.getUniqueCode() == null || Strings.isBlank(dto.getUniqueCode().getCode()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Neplatný uniqueCode");

        if (onCreated) {
            if (dto.getId() != null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id musí být null");

            if (dto.getCvData() == null || Strings.isBlank(dto.getCvData().getData()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nahrání CV je povinné.");

        } else {
            if (dto.getId() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id nesmí být null");
        }
    }


    @Override
    @Transactional(readOnly = true)
    public CvData getCv(Long id) {

        return cvDataRepository.findByGraduateId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CV nenalezeno"));
    }

    @Override
    public void setGraduateVisibility(Long id, String uniqueCode, boolean visibility) {
        if (uniqueCodeRepository.findByGraduateIdAndCode(id, uniqueCode).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Absolvent nenalezen");

        Graduate graduate = graduateRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Absolvent nenalezen"));

        graduate.setProfileVisible(visibility);
        graduate.setModified(LocalDateTime.now());

        graduateRepository.save(graduate);
    }

    private CvData processCvDataDto(CvDataDto cvDataDto) {
        if (isBlank(cvDataDto.getData()) || isBlank(cvDataDto.getFileName()) || isBlank(cvDataDto.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data pro CV nejsou kompletní.");
        }
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(cvDataDto.getData());
            CvData cvDataEntity = new CvData();
            cvDataEntity.setData(decodedBytes);
            cvDataEntity.setFileName(cvDataDto.getFileName());
            cvDataEntity.setContentType(cvDataDto.getContentType());
            return cvDataEntity;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Poskytnutá data CV nejsou validní Base64 řetězec.", e);
        }
    }

    private void validateFile(CvData file) {
        if (file.getData().length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Soubor nesmí být prázdný");
        }

        if (file.getData().length > MAX_FILE_SIZE) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "Váš soubor je příliš velký, maximální velikost souboru je 10 MB");
        }

        final String fileContentType = file.getContentType();
        if (Arrays.stream(ALLOWED_MIME_TYPES).noneMatch(t -> t.equalsIgnoreCase(fileContentType))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nepodporovaný formát, prosím nahrajte soubor v pdf formátu");
        }

        try (PDDocument doc = PDDocument.load(file.getData())) {
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Obsah souboru není validní nebo je poškozený");
        }
    }
}