package com.luckybusiness.luckyapp.integration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luckybusiness.luckyapp.filters.GraduateFilter;
import com.luckybusiness.luckyapp.model.dtos.*;
import com.luckybusiness.luckyapp.model.entities.CvData;
import com.luckybusiness.luckyapp.model.entities.Graduate;
import com.luckybusiness.luckyapp.model.enums.Availability;
import com.luckybusiness.luckyapp.model.enums.LuckyCourse;
import com.luckybusiness.luckyapp.repositories.CvDataRepository;
import com.luckybusiness.luckyapp.repositories.GraduateRepository;
import com.luckybusiness.luckyapp.repositories.UniqueCodeRepository; // Added import
import org.springframework.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;


import java.util.Arrays;
import java.util.Base64;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/sql/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GraduateApiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GraduateRepository graduateRepository;

    @Autowired
    private UniqueCodeRepository uniqueCodeRepository;

    @Autowired
    private CvDataRepository cvDataRepository;

    private final String BASE_URL = "/api/graduates";
    private final String EXISTING_CODE_1 = "testcode123";
    private final String EXISTING_CODE_2 = "testcode456";
    private final String UNASSIGNED_CODE = "unassigned123";
    private final Long GRADUATE_ID_5 = 5L;
    private final Long GRADUATE_ID_6 = 6L;
    private final Long GRADUATE_ID_7 = 7L;
    private final Long NON_EXISTENT_ID = 999L;
    private final Long GRADUATE_ID_BACKEND_REMOTE = 10L;
    private final Long GRADUATE_ID_TESTER_HYBRID = 11L;
    private final Long CV_DATA_ID_FOR_GRADUATE_5 = 5L;
    private final String minimalPdfString =
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


    @Test
    void getNewOrExistingGraduateByUniqueCode_WhenExisting_ShouldReturnGraduateWithCode() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("uniqueCode", EXISTING_CODE_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(GRADUATE_ID_5.intValue())))
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.lastName", is("UserOne")))
                .andExpect(jsonPath("$.uniqueCode.code", is(EXISTING_CODE_1)))
                .andExpect(jsonPath("$.cvData.data").doesNotExist());
    }

    @Test
    void getNewOrExistingGraduateByUniqueCode_WhenUnassigned_ShouldReturnEmptyGraduateWithCode() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("uniqueCode", UNASSIGNED_CODE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.firstName").isEmpty())
                .andExpect(jsonPath("$.lastName").isEmpty())
                .andExpect(jsonPath("$.uniqueCode.code", is(UNASSIGNED_CODE)))
                .andExpect(jsonPath("$.uniqueCode.id").doesNotExist()) 
                .andExpect(jsonPath("$.contractTypes").exists())
                .andExpect(jsonPath("$.contractTypes.id").doesNotExist())
                .andExpect(jsonPath("$.contractTypes.hpp").isBoolean())
                .andExpect(jsonPath("$.employmentTypes").exists())
                .andExpect(jsonPath("$.employmentTypes.id").doesNotExist())
                .andExpect(jsonPath("$.employmentRegimes").exists())
                .andExpect(jsonPath("$.employmentRegimes.id").doesNotExist())
                .andExpect(jsonPath("$.preferredRoles").exists())
                .andExpect(jsonPath("$.preferredRoles.id").doesNotExist())
                .andExpect(jsonPath("$.workRegions").exists())
                .andExpect(jsonPath("$.workRegions.id").doesNotExist())
                .andExpect(jsonPath("$.cvData").exists()) 
                .andExpect(jsonPath("$.cvData.id").doesNotExist())
                .andExpect(jsonPath("$.cvData.data").doesNotExist())
                .andExpect(jsonPath("$.cvData.fileName").isEmpty()) 
                .andExpect(jsonPath("$.cvData.contentType").isEmpty()); 
    }

    @Test
    void getNewOrExistingGraduateByUniqueCode_WhenInvalidCode_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("uniqueCode", "invalid-code-format"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getGraduateById_WhenVisible_ShouldReturnGraduateWithoutCode() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", GRADUATE_ID_5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(GRADUATE_ID_5.intValue())))
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.lastName", is("UserOne")))
                .andExpect(jsonPath("$.uniqueCode").doesNotExist())
                .andExpect(jsonPath("$.cvData.data").doesNotExist());
    }

    @Test
    void getGraduateById_WhenNotVisibleOrNotConsented_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", GRADUATE_ID_6))
                .andExpect(status().isNotFound());
    }

    @Test
    void getGraduateById_WhenNonExistent_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", NON_EXISTENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void createGraduate_NewFlow_ShouldSucceed() throws Exception {
        byte[] cvContent = minimalPdfString.getBytes();
        String cvContentBase64 = Base64.getEncoder().encodeToString(cvContent);
        String cvFilename = "new_flow_cv.pdf";
        String cvContentType = MediaType.APPLICATION_PDF_VALUE;

        GraduateDto newDto = GraduateDto.builder()
                .firstName("CreateNewFlow")
                .lastName("Graduate")
                .uniqueCode(UniqueCodeDto.builder().code(UNASSIGNED_CODE).build())
                .gdprConsent(true)
                .languages("Czech, German")
                .availability(Availability.ONE_MONTH)
                .contractTypes(ContractTypesDto.builder().dppDpc(true).build())
                .employmentTypes(EmploymentTypesDto.builder().halfTime(true).build())
                .employmentRegimes(EmploymentRegimesDto.builder().hybrid(true).build())
                .preferredRoles(PreferredRolesDto.builder().tester(true).build())
                .workRegions(WorkRegionsDto.builder().praha(true).build())
                .cvData(CvDataDto.builder()
                        .data(cvContentBase64)
                        .fileName(cvFilename)
                        .contentType(cvContentType)
                        .build())
                .build();

        String graduateJson = objectMapper.writeValueAsString(newDto);

        MvcResult createResult = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = createResult.getResponse().getContentAsString();
        assertNotNull(responseContent);
        assertTrue(responseContent.matches("\\d+"), "Response should be the new graduate ID");
        Long createdId = Long.parseLong(responseContent);


        Graduate created = graduateRepository.findById(createdId)
                .orElseThrow(() -> new AssertionError("Graduate with ID " + createdId + " not found after creation"));

        assertEquals("CreateNewFlow", created.getFirstName());
        assertEquals("Graduate", created.getLastName());
        assertNotNull(created.getUniqueCode());
        assertEquals(UNASSIGNED_CODE, created.getUniqueCode().getCode());
        assertEquals(createdId, created.getUniqueCode().getGraduate().getId());
        assertTrue(created.getContractTypes().isDppDpc());
        assertTrue(created.getWorkRegions().isPraha());

        assertNotNull(created.getCvData());
        assertNotNull(created.getCvData().getId());
        assertEquals(cvFilename, created.getCvData().getFileName());
        assertEquals(cvContentType, created.getCvData().getContentType());
        assertEquals(created.getId(), created.getCvData().getGraduate().getId());
    }


    @Test
    void createGraduate_WhenDtoInvalid_ShouldReturnBadRequest() throws Exception {
        GraduateDto invalidDto = GraduateDto.builder()
                .firstName("Invalid DTO")
                .lastName("Fail")
                .uniqueCode(null)
                .gdprConsent(true)
                .languages("Czech")
                .availability(Availability.IMMEDIATELY)
                .contractTypes(ContractTypesDto.builder().hpp(true).build())
                .employmentTypes(EmploymentTypesDto.builder().fullTime(true).build())
                .employmentRegimes(EmploymentRegimesDto.builder().onSite(true).build())
                .preferredRoles(PreferredRolesDto.builder().tester(true).build())
                .workRegions(WorkRegionsDto.builder().praha(true).build())
                .cvData(CvDataDto.builder().data(Base64.getEncoder().encodeToString(new byte[]{1})).fileName("a.pdf").contentType("application/pdf").build())
                .build();
        String invalidGraduateJson = objectMapper.writeValueAsString(invalidDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidGraduateJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGraduate_WhenValid_ShouldUpdateAndReturnId() throws Exception {
        byte[] updatedCvBytes = minimalPdfString.getBytes();
        String updatedCvBase64 = Base64.getEncoder().encodeToString(updatedCvBytes);

        CvDataDto updatedCvData = CvDataDto.builder()
                .data(updatedCvBase64)
                .fileName("updated_cv_via_dto.pdf")
                .contentType(MediaType.APPLICATION_PDF_VALUE)
                .build();

        GraduateDto dto = GraduateDto.builder()
                .id(GRADUATE_ID_5)
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .languages("Updated Languages")
                .availability(Availability.TWO_MONTHS)
                .luckyCourse(LuckyCourse.MANUAL_IT_TESTER_ACADEMY)
                .motivationLetter("Updated motivation.")
                .gdprConsent(true)
                .otherCourses("rgregergeg")
                .profileVisible(true)
                .uniqueCode(UniqueCodeDto.builder().code(EXISTING_CODE_1).build())
                .contractTypes(ContractTypesDto.builder().hpp(false).dppDpc(true).ico(false).build())
                .employmentTypes(EmploymentTypesDto.builder().fullTime(false).halfTime(true).partTime(true).partTimeHours("15").build())
                .employmentRegimes(EmploymentRegimesDto.builder().onSite(false).hybrid(false).remote(true).build())
                .preferredRoles(PreferredRolesDto.builder().tester(false).frontendDeveloper(false).backendDeveloper(true).build())
                .workRegions(WorkRegionsDto.builder().praha(false).jihomoravskyKraj(false).moravskoslezskyKraj(true).build())
                .cvData(updatedCvData)
                .build();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String graduateDtoJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(BASE_URL + "/" + GRADUATE_ID_5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().string(GRADUATE_ID_5.toString()));

        Graduate updatedGraduate = graduateRepository.findById(GRADUATE_ID_5).orElseThrow();
        assertEquals("UpdatedFirstName", updatedGraduate.getFirstName());
        assertEquals(Availability.TWO_MONTHS, updatedGraduate.getAvailability());
        assertNotNull(updatedGraduate.getModified());
        assertNotNull(updatedGraduate.getContractTypes());
        assertFalse(updatedGraduate.getContractTypes().isHpp());
        assertTrue(updatedGraduate.getContractTypes().isDppDpc());
        assertNotNull(updatedGraduate.getEmploymentTypes());
        assertEquals("15", updatedGraduate.getEmploymentTypes().getPartTimeHours());
        assertTrue(updatedGraduate.getEmploymentTypes().isHalfTime());
        assertNotNull(updatedGraduate.getEmploymentRegimes());
        assertTrue(updatedGraduate.getEmploymentRegimes().isRemote());
        assertNotNull(updatedGraduate.getPreferredRoles());
        assertFalse(updatedGraduate.getPreferredRoles().isTester());
        assertTrue(updatedGraduate.getPreferredRoles().isBackendDeveloper());
        assertNotNull(updatedGraduate.getWorkRegions());
        assertFalse(updatedGraduate.getWorkRegions().isPardubickyKraj());
        assertTrue(updatedGraduate.getWorkRegions().isMoravskoslezskyKraj());

        assertNotNull(updatedGraduate.getCvData());
        assertEquals(CV_DATA_ID_FOR_GRADUATE_5, updatedGraduate.getCvData().getId());
        assertArrayEquals(updatedCvBytes, updatedGraduate.getCvData().getData());
        assertEquals(updatedCvData.getFileName(), updatedGraduate.getCvData().getFileName());
        assertEquals(updatedCvData.getContentType(), updatedGraduate.getCvData().getContentType());
    }

    @Test
    void updateGraduate_WhenCvDataNotInDto_ShouldNotUpdateCv() throws Exception {
        CvData originalCvDataEntity = cvDataRepository.findById(CV_DATA_ID_FOR_GRADUATE_5)
                .orElseThrow(() -> new AssertionError("Original CV data not found"));
        byte[] originalCvBytes = originalCvDataEntity.getData();
        String originalFileName = originalCvDataEntity.getFileName();
        String originalContentType = originalCvDataEntity.getContentType();

        CvDataDto cvDataMetadataOnly = CvDataDto.builder()
                .fileName(originalFileName)
                .contentType(originalContentType)
                .data(null)
                .build();

        GraduateDto dtoWithExistingCvMetadata = GraduateDto.builder()
                .id(GRADUATE_ID_5)
                .firstName("UpdatedNameOnly")
                .lastName("UpdatedLastNameOnly")
                .languages("Updated Languages")
                .availability(Availability.TWO_MONTHS)
                .luckyCourse(LuckyCourse.MANUAL_IT_TESTER_ACADEMY)
                .motivationLetter("Updated motivation.")
                .gdprConsent(true)
                .otherCourses("rgregergeg")
                .profileVisible(true)
                .uniqueCode(UniqueCodeDto.builder().code(EXISTING_CODE_1).build())
                .contractTypes(ContractTypesDto.builder().hpp(false).dppDpc(true).ico(false).build())
                .employmentTypes(EmploymentTypesDto.builder().fullTime(false).halfTime(true).partTime(true).partTimeHours("15").build())
                .employmentRegimes(EmploymentRegimesDto.builder().onSite(false).hybrid(false).remote(true).build())
                .preferredRoles(PreferredRolesDto.builder().tester(false).frontendDeveloper(false).backendDeveloper(true).build())
                .workRegions(WorkRegionsDto.builder().praha(false).jihomoravskyKraj(false).moravskoslezskyKraj(true).build())
                .cvData(cvDataMetadataOnly)
                .build();

        String graduateDtoJson = objectMapper.writeValueAsString(dtoWithExistingCvMetadata);

        mockMvc.perform(put(BASE_URL + "/" + GRADUATE_ID_5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateDtoJson))
                .andExpect(status().isOk());

        Graduate updatedGraduate = graduateRepository.findById(GRADUATE_ID_5).orElseThrow();
        assertEquals("UpdatedNameOnly", updatedGraduate.getFirstName());

        CvData currentCvData = updatedGraduate.getCvData();
        assertNotNull(currentCvData, "CV Data should still exist");
        assertEquals(CV_DATA_ID_FOR_GRADUATE_5, currentCvData.getId(), "CV Data ID should match");
        assertArrayEquals(originalCvBytes, currentCvData.getData(), "CV binary data should not change");
        assertEquals(originalFileName, currentCvData.getFileName(), "CV file name should not change");
        assertEquals(originalContentType, currentCvData.getContentType(), "CV content type should not change");
    }

    @Test
    void updateGraduate_WhenIdAndCodeMismatch_ShouldReturnBadRequest() throws Exception {
        GraduateDto updateDto = GraduateDto.builder()
                .id(GRADUATE_ID_5)
                .firstName("Update Fail")
                .lastName("Update Fail")
                .uniqueCode(UniqueCodeDto.builder().code(EXISTING_CODE_2).build())
                .gdprConsent(true)
                .languages("Czech")
                .availability(Availability.IMMEDIATELY)
                .contractTypes(ContractTypesDto.builder().hpp(true).build())
                .employmentTypes(EmploymentTypesDto.builder().fullTime(true).build())
                .employmentRegimes(EmploymentRegimesDto.builder().onSite(true).build())
                .preferredRoles(PreferredRolesDto.builder().tester(true).build())
                .workRegions(WorkRegionsDto.builder().praha(true).build())
                .cvData(CvDataDto.builder().data(Base64.getEncoder().encodeToString(new byte[]{1})).fileName("x.pdf").contentType("application/pdf").build()) // Ensure valid CV content type
                .build();

        mockMvc.perform(put(BASE_URL+ "/" + GRADUATE_ID_5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteGraduate_WhenValid_ShouldDeleteGraduateButKeepUniqueCode() throws Exception {
        Graduate graduateBeforeDelete = graduateRepository.findById(GRADUATE_ID_5).orElseThrow();
        Long uniqueCodeId = graduateBeforeDelete.getUniqueCode().getId();
        String uniqueCodeValue = graduateBeforeDelete.getUniqueCode().getCode();
        assertEquals(EXISTING_CODE_1, uniqueCodeValue);
        assertTrue(graduateRepository.existsById(GRADUATE_ID_5));
        assertTrue(uniqueCodeRepository.existsById(uniqueCodeId));
        assertTrue(cvDataRepository.existsById(CV_DATA_ID_FOR_GRADUATE_5));


        mockMvc.perform(delete(BASE_URL + "/{id}", GRADUATE_ID_5)
                        .param("uniqueCode", EXISTING_CODE_1))
                .andExpect(status().isOk());


        assertFalse(graduateRepository.existsById(GRADUATE_ID_5));
        assertTrue(uniqueCodeRepository.existsById(uniqueCodeId));
        assertTrue(uniqueCodeRepository.findByCode(uniqueCodeValue).isPresent());
        assertFalse(cvDataRepository.existsById(CV_DATA_ID_FOR_GRADUATE_5)); // CV data should be deleted
    }

    @Test
    void deleteGraduate_WhenIdOrCodeInvalid_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", GRADUATE_ID_5)
                        .param("uniqueCode", EXISTING_CODE_2))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete(BASE_URL + "/{id}", NON_EXISTENT_ID)
                        .param("uniqueCode", EXISTING_CODE_1))
                .andExpect(status().isNotFound());
    }

    @Test
    void downloadCv_WhenCvExists_ShouldReturnFile() throws Exception {
        byte[] expectedCvData = new byte[] {(byte)0xde, (byte)0xad, (byte)0xbe, (byte)0xef};
        String expectedFilename = "cv1.pdf";
        String expectedContentType = MediaType.APPLICATION_PDF_VALUE;

        MvcResult result = mockMvc.perform(get(BASE_URL + "/cv/{id}", GRADUATE_ID_5))
                .andExpect(status().isOk())
                .andExpect(content().contentType(expectedContentType))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, containsString("attachment; filename=\"" + expectedFilename + "\"")))
                .andReturn();

        assertArrayEquals(expectedCvData, result.getResponse().getContentAsByteArray());
    }

    @Test
    void downloadCv_WhenCvNotExists_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/cv/{id}", NON_EXISTENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void downloadCv_WhenGraduateNotExists_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/cv/{id}", NON_EXISTENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void setGraduateVisibility_WhenValid_ShouldReturnOkAndUpdateVisibility() throws Exception {
        mockMvc.perform(put(BASE_URL + "/visibility/{id}/{visibility}", GRADUATE_ID_5, false)
                        .param("uniqueCode", EXISTING_CODE_1))
                .andExpect(status().isOk());

        Graduate graduate = graduateRepository.findById(GRADUATE_ID_5).orElseThrow();
        assertFalse(graduate.isProfileVisible());

        mockMvc.perform(put(BASE_URL + "/visibility/{id}/{visibility}", GRADUATE_ID_5, true)
                        .param("uniqueCode", EXISTING_CODE_1))
                .andExpect(status().isOk());

        graduate = graduateRepository.findById(GRADUATE_ID_5).orElseThrow();
        assertTrue(graduate.isProfileVisible());

    }

    @Test
    void setGraduateVisibility_WhenGraduateNotFound_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(put(BASE_URL + "/visibility/{id}/{visibility}", GRADUATE_ID_5, false)
                        .param("uniqueCode", EXISTING_CODE_2))
                .andExpect(status().isNotFound());
    }


    @Test
    void getGraduates_WhenDefaultFilter_ShouldReturnVisibleGraduates() throws Exception {
        GraduateFilter filter = new GraduateFilter();
        mockMvc.perform(post(BASE_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(4)))
                .andExpect(jsonPath("$[0].id", is(GRADUATE_ID_5.intValue())))
                .andExpect(jsonPath("$[1].id", is(GRADUATE_ID_7.intValue())))
                .andExpect(jsonPath("$[2].id", is(GRADUATE_ID_BACKEND_REMOTE.intValue())))
                .andExpect(jsonPath("$[3].id", is(GRADUATE_ID_TESTER_HYBRID.intValue())))
                .andExpect(jsonPath("$[*].cvData.data", everyItem(is(nullValue()))));
    }

    @Test
    void getGraduates_WhenFilteredByPreferredWorkTypeRemote() throws Exception {
        GraduateFilter filter = new GraduateFilter();
        filter.setRemote(true);

        mockMvc.perform(post(BASE_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(
                        GRADUATE_ID_5.intValue(),
                        GRADUATE_ID_7.intValue(),
                        GRADUATE_ID_BACKEND_REMOTE.intValue()
                )));
    }

    @Test
    void filter_WhenRegionJihomoravsky_ShouldReturnTwoGraduates() throws Exception {
        GraduateFilter filter = new GraduateFilter();
        filter.setJihomoravskyKraj(true);

        mockMvc.perform(post(BASE_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(
                        GRADUATE_ID_5.intValue(),
                        GRADUATE_ID_BACKEND_REMOTE.intValue()
                )));
    }

    @Test
    void filter_WhenRoleBackendDeveloper_ShouldReturnTwoGraduates() throws Exception {
        GraduateFilter filter = new GraduateFilter();
        filter.setBackendDeveloper(true);

        mockMvc.perform(post(BASE_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(
                        GRADUATE_ID_5.intValue(),
                        GRADUATE_ID_BACKEND_REMOTE.intValue()
                )));
    }

    @Test
    void filter_WhenRoleTesterOrAutomationTester_ShouldReturnThreeGraduates() throws Exception {
        GraduateFilter filter = new GraduateFilter();
        filter.setTester(true);
        filter.setAutomationTester(true);

        mockMvc.perform(post(BASE_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(
                        GRADUATE_ID_5.intValue(),
                        GRADUATE_ID_TESTER_HYBRID.intValue(),
                        GRADUATE_ID_7.intValue()
                )));
    }

    @Test
    void filter_WhenRegionStredoceskyAndContractHPPAndRegimeHybrid_ShouldReturnTwoGraduates() throws Exception {
        GraduateFilter filter = new GraduateFilter();
        filter.setStredoceskyKraj(true);
        filter.setContractTypeHpp(true);
        filter.setHybrid(true);

        mockMvc.perform(post(BASE_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(
                        GRADUATE_ID_5.intValue(),
                        GRADUATE_ID_TESTER_HYBRID.intValue()
                )));
    }

    @Test
        void filter_WhenRegimeRemote_ShouldReturnThreeGraduates() throws Exception {
            GraduateFilter filter = new GraduateFilter();
            filter.setRemote(true);

            mockMvc.perform(post(BASE_URL + "/list")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(filter)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()", is(3)))
                    .andExpect(jsonPath("$[*].id", containsInAnyOrder(
                            GRADUATE_ID_5.intValue(),
                            GRADUATE_ID_7.intValue(),
                            GRADUATE_ID_BACKEND_REMOTE.intValue()
                    )));
        }

    @Test
    void filter_WhenMatchesOnlyInvisibleOrNoGdpr_ShouldReturnCorrectVisibleSet() throws Exception {
        GraduateFilter filter = new GraduateFilter();
        filter.setMoravskoslezskyKraj(true);
        filter.setFrontendDeveloper(true);

        mockMvc.perform(post(BASE_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(GRADUATE_ID_5.intValue())));


        filter = new GraduateFilter();
        filter.setDataAnalyst(true);

        mockMvc.perform(post(BASE_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(GRADUATE_ID_5.intValue())));
    }

    @Test
    void filter_WhenEmptyFilter_ShouldReturnAllVisibleAndConsentingGraduates() throws Exception {
        GraduateFilter filter = new GraduateFilter();

        mockMvc.perform(post(BASE_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(4)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(
                        GRADUATE_ID_5.intValue(),
                        GRADUATE_ID_7.intValue(),
                        GRADUATE_ID_BACKEND_REMOTE.intValue(),
                        GRADUATE_ID_TESTER_HYBRID.intValue()
                )));
    }

    @Test
    void createGraduate_WhenWorkRegionsNull_ShouldReturnBadRequest() throws Exception {
        GraduateDto newDto = createValidGraduateDtoBase();
        newDto.setWorkRegions(null);
        String graduateJson = objectMapper.writeValueAsString(newDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'workRegions' && @.message == 'Výběr regionu pro hledání práce je povinný.')]").exists());
    }


    @Test
    void createGraduate_WhenWorkRegionsAllFalse_ShouldReturnBadRequest() throws Exception {
        WorkRegionsDto allFalseRegions = new WorkRegionsDto();
        GraduateDto newDto = createValidGraduateDtoBase();
        newDto.setWorkRegions(allFalseRegions);
        String graduateJson = objectMapper.writeValueAsString(newDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'workRegions' && @.message == 'Toto je povinné pole, bez jeho vyplnění není možné vytvořit profil.')]").exists());
    }

    @Test
    void createGraduate_WhenContractTypesAllFalse_ShouldReturnBadRequest() throws Exception {
        ContractTypesDto allFalseContracts = new ContractTypesDto();
        GraduateDto newDto = createValidGraduateDtoBase();
        newDto.setContractTypes(allFalseContracts);
        String graduateJson = objectMapper.writeValueAsString(newDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'contractTypes' && @.message == 'Toto je povinné pole, bez jeho vyplnění není možné vytvořit profil.')]").exists());
    }

    @Test
    void updateGraduate_WhenWorkRegionsAllFalse_ShouldReturnBadRequest() throws Exception {
        WorkRegionsDto allFalseRegions = new WorkRegionsDto();
        GraduateDto invalidUpdateDto = createValidGraduateDtoBase();
        invalidUpdateDto.setId(GRADUATE_ID_5);
        invalidUpdateDto.setUniqueCode(UniqueCodeDto.builder().code(EXISTING_CODE_1).build());
        invalidUpdateDto.setWorkRegions(allFalseRegions);
        String graduateJson = objectMapper.writeValueAsString(invalidUpdateDto);

        mockMvc.perform(put(BASE_URL + "/" + GRADUATE_ID_5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'workRegions' && @.message == 'Toto je povinné pole, bez jeho vyplnění není možné vytvořit profil.')]").exists());
    }

    @Test
    void createGraduate_WhenPartTimeTrueAndHoursProvided_ShouldSucceed() throws Exception {
        EmploymentTypesDto employmentTypes = EmploymentTypesDto.builder()
                .partTime(true)
                .partTimeHours("20")
                .build();

        GraduateDto newDto = createValidGraduateDtoBase();
        newDto.setEmploymentTypes(employmentTypes);
        String graduateJson = objectMapper.writeValueAsString(newDto);


        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isCreated());
    }

    @Test
    void createGraduate_WhenPartTimeTrueAndHoursNull_ShouldReturnBadRequest() throws Exception {
        EmploymentTypesDto employmentTypes = EmploymentTypesDto.builder()
                .partTime(true)
                .partTimeHours(null)
                .build();

        GraduateDto newDto = createValidGraduateDtoBase();
        newDto.setEmploymentTypes(employmentTypes);
        String graduateJson = objectMapper.writeValueAsString(newDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'employmentTypes.partTimeHours')].message")
                        .value("Počet hodin musí být vyplněn při volbě částečného úvazku"));
    }


    @Test
    void createGraduate_WhenPartTimeFalseAndHoursNull_ShouldSucceed() throws Exception {
        EmploymentTypesDto employmentTypes = EmploymentTypesDto.builder()
                .partTime(false)
                .partTimeHours(null)
                .fullTime(true)
                .build();

        GraduateDto newDto = createValidGraduateDtoBase();
        newDto.setEmploymentTypes(employmentTypes);
        String graduateJson = objectMapper.writeValueAsString(newDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isCreated());
    }

    @Test
    void createGraduate_WhenPartTimeFalseAndHoursProvided_ShouldSucceed() throws Exception {
        EmploymentTypesDto employmentTypes = EmploymentTypesDto.builder()
                .partTime(false)
                .partTimeHours("10")
                .fullTime(true)
                .build();

        GraduateDto newDto = createValidGraduateDtoBase();
        newDto.setEmploymentTypes(employmentTypes);
        String graduateJson = objectMapper.writeValueAsString(newDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isCreated());
    }

    @Test
    void createGraduate_WhenCvDataNull_ShouldReturnBadRequestWithCorrectMessage() throws Exception {
        GraduateDto newDto = createValidGraduateDtoBase();
        newDto.setCvData(null);
        String graduateJson = objectMapper.writeValueAsString(newDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'cvData')].message")
                        .value("Nahrání CV je povinné."));
    }

    @Test
    void createGraduate_WhenCvDataDataNull_ShouldReturnBadRequestWithServiceMessage() throws Exception {
        GraduateDto newDto = createValidGraduateDtoBase();
        newDto.getCvData().setData(null);
        String graduateJson = objectMapper.writeValueAsString(newDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertEquals(HttpStatus.BAD_REQUEST, ((MethodArgumentNotValidException)result.getResolvedException()).getStatusCode()))
                .andExpect(jsonPath("$.errors[?(@.field == 'cvData.data')].message")
                        .value("Nahrání CV je povinné."));
    }

    @Test
    void createGraduate_WhenCvContentTypeInvalid_ShouldReturnBadRequestWithCorrectMessage() throws Exception {
        GraduateDto newDto = createValidGraduateDtoBase();
        newDto.getCvData().setContentType("image/jpeg");
        String graduateJson = objectMapper.writeValueAsString(newDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'cvData.contentType')].message")
                        .value("Nepodporovaný formát, prosím nahrajte soubor v pdf formátu")); // Assuming DTO message was updated
    }

    @Test
    void createGraduate_WhenCvDataTooLarge_ShouldReturnBadRequestWithCorrectMessage() throws Exception {
        GraduateDto newDto = createValidGraduateDtoBase();
        int size = 11 * 1024 * 1024;
        byte[] largeData = new byte[size];
        Arrays.fill(largeData, (byte) 'A');
        String largeDataBase64 = Base64.getEncoder().encodeToString(largeData);
        newDto.getCvData().setData(largeDataBase64);

        String graduateJson = objectMapper.writeValueAsString(newDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isPayloadTooLarge())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals(HttpStatus.PAYLOAD_TOO_LARGE, ((ResponseStatusException)result.getResolvedException()).getStatusCode()))
                .andExpect(jsonPath("$.message").value(containsString("Váš soubor je příliš velký")));    }

    @Test
    void createGraduate_WhenCvDataInvalidBase64_ShouldReturnBadRequest() throws Exception {
        GraduateDto newDto = createValidGraduateDtoBase();
        newDto.getCvData().setData("!!notbase64!!");

        String graduateJson = objectMapper.writeValueAsString(newDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals(HttpStatus.BAD_REQUEST, ((ResponseStatusException)result.getResolvedException()).getStatusCode()))
                .andExpect(jsonPath("$.message").value(containsString("Poskytnutá data CV nejsou validní Base64 řetězec.")));
    }

    @Test
    void createGraduate_WhenEmploymentTypesAllFalse_ShouldReturnBadRequest() throws Exception {
        EmploymentTypesDto allFalseTypes = new EmploymentTypesDto();
        GraduateDto newDto = createValidGraduateDtoBase();
        newDto.setEmploymentTypes(allFalseTypes);
        String graduateJson = objectMapper.writeValueAsString(newDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'employmentTypes')].message")
                        .value("Toto je povinné pole, bez jeho vyplnění není možné vytvořit profil. Zadej, prosím, tvé kapacitní možnosti.")); // Testing current implemented message
    }

    private GraduateDto createValidGraduateDtoBase() {
        byte[] cvBytes = minimalPdfString.getBytes();
        String cvBase64 = Base64.getEncoder().encodeToString(cvBytes);
        CvDataDto dummyCv = CvDataDto.builder()
                .data(cvBase64)
                .fileName("dummy.pdf")
                .contentType(MediaType.APPLICATION_PDF_VALUE)
                .build();


        return GraduateDto.builder()
                .firstName("Validation")
                .lastName("Base")
                .uniqueCode(UniqueCodeDto.builder().code(UNASSIGNED_CODE).build())
                .gdprConsent(true)
                .languages("Czech, English")
                .availability(Availability.IMMEDIATELY)
                .contractTypes(ContractTypesDto.builder().hpp(true).build())
                .employmentRegimes(EmploymentRegimesDto.builder().remote(true).build())
                .preferredRoles(PreferredRolesDto.builder().tester(true).build())
                .workRegions(WorkRegionsDto.builder().praha(true).build())
                .cvData(dummyCv)
                .employmentTypes(EmploymentTypesDto.builder().fullTime(true).build())
                .build();
    }

    @Test
    void getGraduateById_ShouldReturnNestedDtosWithoutIdsInResponse() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", GRADUATE_ID_5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(GRADUATE_ID_5.intValue())))
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.contractTypes").exists())
                .andExpect(jsonPath("$.contractTypes.id").doesNotExist())
                .andExpect(jsonPath("$.employmentTypes").exists())
                .andExpect(jsonPath("$.employmentTypes.id").doesNotExist())
                .andExpect(jsonPath("$.employmentRegimes").exists())
                .andExpect(jsonPath("$.employmentRegimes.id").doesNotExist())
                .andExpect(jsonPath("$.preferredRoles").exists())
                .andExpect(jsonPath("$.preferredRoles.id").doesNotExist())
                .andExpect(jsonPath("$.workRegions").exists())
                .andExpect(jsonPath("$.workRegions.id").doesNotExist())
                .andExpect(jsonPath("$.uniqueCode").doesNotExist())
                .andExpect(jsonPath("$.cvData").exists())
                .andExpect(jsonPath("$.cvData.id").doesNotExist())
                .andExpect(jsonPath("$.cvData.data").doesNotExist());
    }

    @Test
    void getNewOrExistingGraduateByUniqueCode_ShouldReturnNestedDtosWithoutIdsInResponse() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("uniqueCode", EXISTING_CODE_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(GRADUATE_ID_5.intValue())))
                .andExpect(jsonPath("$.uniqueCode.code", is(EXISTING_CODE_1)))
                .andExpect(jsonPath("$.uniqueCode.id").doesNotExist())
                .andExpect(jsonPath("$.contractTypes").exists())
                .andExpect(jsonPath("$.contractTypes.id").doesNotExist())
                .andExpect(jsonPath("$.employmentTypes").exists())
                .andExpect(jsonPath("$.employmentTypes.id").doesNotExist())
                .andExpect(jsonPath("$.employmentRegimes").exists())
                .andExpect(jsonPath("$.employmentRegimes.id").doesNotExist())
                .andExpect(jsonPath("$.preferredRoles").exists())
                .andExpect(jsonPath("$.preferredRoles.id").doesNotExist())
                .andExpect(jsonPath("$.workRegions").exists())
                .andExpect(jsonPath("$.workRegions.id").doesNotExist())
                .andExpect(jsonPath("$.cvData").exists())
                .andExpect(jsonPath("$.cvData.id").doesNotExist())
                .andExpect(jsonPath("$.cvData.data").doesNotExist());
    }

    @Test
    void createGraduate_HandlesNestedDtosWithoutIdsInRequestCorrectly() throws Exception {
        byte[] cvContent = minimalPdfString.getBytes();
        String cvContentBase64 = Base64.getEncoder().encodeToString(cvContent);
        String cvFilename = "create_test_cv.pdf";
        String cvContentType = MediaType.APPLICATION_PDF_VALUE;

        ContractTypesDto contractTypesDto = ContractTypesDto.builder().hpp(true).build();
        EmploymentTypesDto employmentTypesDto = EmploymentTypesDto.builder().fullTime(true).build();
        EmploymentRegimesDto employmentRegimesDto = EmploymentRegimesDto.builder().remote(true).build();
        PreferredRolesDto preferredRolesDto = PreferredRolesDto.builder().backendDeveloper(true).build();
        WorkRegionsDto workRegionsDto = WorkRegionsDto.builder().jihomoravskyKraj(true).build();
        CvDataDto cvDataDto = CvDataDto.builder()
                .data(cvContentBase64)
                .fileName(cvFilename)
                .contentType(cvContentType)
                .build(); // CvDataDto také nemá ID

        GraduateDto newDto = GraduateDto.builder()
                .id(null)
                .firstName("CreateNestedTest")
                .lastName("Graduate")
                .uniqueCode(UniqueCodeDto.builder().code(UNASSIGNED_CODE).build())
                .gdprConsent(true)
                .languages("Spanish")
                .availability(Availability.IMMEDIATELY)
                .contractTypes(contractTypesDto)
                .employmentTypes(employmentTypesDto)
                .employmentRegimes(employmentRegimesDto)
                .preferredRoles(preferredRolesDto)
                .workRegions(workRegionsDto)
                .cvData(cvDataDto)
                .build();

        String graduateJson = objectMapper.writeValueAsString(newDto);

        MvcResult createResult = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateJson))
                .andExpect(status().isCreated())
                .andReturn();

        Long createdId = Long.parseLong(createResult.getResponse().getContentAsString());

        Graduate created = graduateRepository.findById(createdId)
                .orElseThrow(() -> new AssertionError("Absolvent nenalezen after creation"));

        assertNotNull(created.getId());
        assertNotNull(created.getContractTypes());
        assertNotNull(created.getContractTypes().getId()); 
        assertNotNull(created.getEmploymentTypes());
        assertNotNull(created.getEmploymentTypes().getId()); 
        assertNotNull(created.getEmploymentRegimes());
        assertNotNull(created.getEmploymentRegimes().getId()); 
        assertNotNull(created.getPreferredRoles());
        assertNotNull(created.getPreferredRoles().getId()); 
        assertNotNull(created.getWorkRegions());
        assertNotNull(created.getWorkRegions().getId()); 
        assertNotNull(created.getUniqueCode());
        assertNotNull(created.getUniqueCode().getId());
        assertNotNull(created.getCvData());
        assertNotNull(created.getCvData().getId());

        // Ověření hodnot
        assertEquals("CreateNestedTest", created.getFirstName());
        assertTrue(created.getContractTypes().isHpp());
        assertTrue(created.getEmploymentTypes().isFullTime());
        assertTrue(created.getEmploymentRegimes().isRemote());
        assertTrue(created.getPreferredRoles().isBackendDeveloper());
        assertTrue(created.getWorkRegions().isJihomoravskyKraj());
        assertArrayEquals(cvContent, created.getCvData().getData());
    }

    @Test
    void updateGraduate_HandlesNestedDtosWithoutIdsInRequestCorrectly() throws Exception {
        Graduate originalGraduate = graduateRepository.findById(GRADUATE_ID_5).orElseThrow();
        Long originalContractTypeId = originalGraduate.getContractTypes().getId();
        Long originalEmploymentTypeId = originalGraduate.getEmploymentTypes().getId();
        Long originalRegimeId = originalGraduate.getEmploymentRegimes().getId();
        Long originalRolesId = originalGraduate.getPreferredRoles().getId();
        Long originalRegionId = originalGraduate.getWorkRegions().getId();
        Long originalCvDataId = originalGraduate.getCvData().getId();

        ContractTypesDto updatedContractTypes = ContractTypesDto.builder().hpp(false).ico(true).build();
        EmploymentTypesDto updatedEmploymentTypes = EmploymentTypesDto.builder().halfTime(true).build();
        EmploymentRegimesDto existingRegimes = EmploymentRegimesDto.builder().onSite(true).hybrid(true).remote(true).build();
        PreferredRolesDto existingRoles = PreferredRolesDto.builder().tester(true).itSupport(true).automationTester(true).testAnalyst(true).testManager(true).frontendDeveloper(true).backendDeveloper(true).dataAnalyst(true).scrumMaster(true).productOwner(true).build();
        WorkRegionsDto existingRegions = WorkRegionsDto.builder().praha(true).stredoceskyKraj(true).jihoceskyKraj(true).plzenskyKraj(true).karlovarskyKraj(true).usteckyKraj(true).libereckyKraj(true).kralovehradeckyKraj(true).pardubickyKraj(true).krajVysocina(true).jihomoravskyKraj(true).zlinskyKraj(true).olomouckyKraj(true).moravskoslezskyKraj(true).build();
        CvDataDto existingCvDataMeta = CvDataDto.builder()
                .fileName(originalGraduate.getCvData().getFileName())
                .contentType(originalGraduate.getCvData().getContentType())
                .data(null) 
                .build();


        GraduateDto updateDto = GraduateDto.builder()
                .id(GRADUATE_ID_5)
                .firstName("UpdateNestedTest")
                .lastName("Graduate")
                .uniqueCode(UniqueCodeDto.builder().code(EXISTING_CODE_1).build())
                .gdprConsent(true)
                .languages("Updated Lang")
                .availability(Availability.TWO_MONTHS)
                .contractTypes(updatedContractTypes)
                .employmentTypes(updatedEmploymentTypes)
                .employmentRegimes(existingRegimes) 
                .preferredRoles(existingRoles) 
                .workRegions(existingRegions) 
                .cvData(existingCvDataMeta)
                .build();

        String graduateUpdateJson = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(put(BASE_URL + "/" + GRADUATE_ID_5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(graduateUpdateJson))
                .andExpect(status().isOk())
                .andExpect(content().string(GRADUATE_ID_5.toString()));

        // Ověření stavu v databázi
        Graduate updatedGraduate = graduateRepository.findById(GRADUATE_ID_5).orElseThrow();

        assertEquals("UpdateNestedTest", updatedGraduate.getFirstName());
        assertEquals(Availability.TWO_MONTHS, updatedGraduate.getAvailability());

        assertNotNull(updatedGraduate.getContractTypes());
        assertEquals(originalContractTypeId, updatedGraduate.getContractTypes().getId());
        assertFalse(updatedGraduate.getContractTypes().isHpp());
        assertTrue(updatedGraduate.getContractTypes().isIco());

        assertNotNull(updatedGraduate.getEmploymentTypes());
        assertEquals(originalEmploymentTypeId, updatedGraduate.getEmploymentTypes().getId());
        assertFalse(updatedGraduate.getEmploymentTypes().isFullTime());
        assertTrue(updatedGraduate.getEmploymentTypes().isHalfTime());

        assertNotNull(updatedGraduate.getEmploymentRegimes());
        assertEquals(originalRegimeId, updatedGraduate.getEmploymentRegimes().getId());
        assertNotNull(updatedGraduate.getPreferredRoles());
        assertEquals(originalRolesId, updatedGraduate.getPreferredRoles().getId());
        assertNotNull(updatedGraduate.getWorkRegions());
        assertEquals(originalRegionId, updatedGraduate.getWorkRegions().getId());
        assertNotNull(updatedGraduate.getCvData());
        assertEquals(originalCvDataId, updatedGraduate.getCvData().getId());
    }

    @Test
    void createGraduate_WhenAllValidationsFailOnCreate_ShouldReturnBadRequestWithAllErrors() throws Exception {

        GraduateDto invalidDto = GraduateDto.builder()
                .id(null)
                .uniqueCode(UniqueCodeDto.builder().code(UNASSIGNED_CODE).build())
                .firstName("   ")
                .lastName("TotoJeExtremneDlouhePrijmeniKterePresahujeStoZnakuLimitxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
                .languages("CZ")
                .availability(null)
                .gdprConsent(false)
                .contractTypes(ContractTypesDto.builder().hpp(false).dppDpc(false).ico(false).build())
                .employmentRegimes(EmploymentRegimesDto.builder().onSite(false).hybrid(false).remote(false).build())
                .preferredRoles(PreferredRolesDto.builder()
                        .tester(false).itSupport(false).automationTester(false).testAnalyst(false).testManager(false)
                        .frontendDeveloper(false).backendDeveloper(false).dataAnalyst(false).scrumMaster(false).productOwner(false)
                        .build())
                .workRegions(WorkRegionsDto.builder()
                        .praha(false).stredoceskyKraj(false).jihoceskyKraj(false).plzenskyKraj(false).karlovarskyKraj(false)
                        .usteckyKraj(false).libereckyKraj(false).kralovehradeckyKraj(false).pardubickyKraj(false)
                        .krajVysocina(false).jihomoravskyKraj(false).zlinskyKraj(false).olomouckyKraj(false).moravskoslezskyKraj(false)
                        .build())
                .employmentTypes(EmploymentTypesDto.builder()
                        .fullTime(false).halfTime(false)
                        .partTime(true)
                        .partTimeHours(null)
                        .build())
                .cvData(CvDataDto.builder()
                        .fileName("test.pdf")
                        .contentType("application/pdf")
                        .data("   ")
                        .build())
                 .preferredRoles(null)
                .build();

        String invalidJson = objectMapper.writeValueAsString(invalidDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(greaterThanOrEqualTo(8)))) // Upravte dle přesného počtu očekávaných chyb
                .andExpect(jsonPath("$.errors[*].field", hasItems(
                        "firstName",
                        "lastName",
                        "languages",
                        "availability",
                        "gdprConsent",
                        "contractTypes",
                        "employmentRegimes",
                        "preferredRoles",
                        "workRegions",
                        "employmentTypes.partTimeHours",
                        "cvData.data"
                )));
    }

    @Test
    void createGraduate_ShouldTrimAndSanitizeStringsCorrectly() throws Exception {
        byte[] cvBytes = minimalPdfString.getBytes();
        String base64 = Base64.getEncoder().encodeToString(cvBytes);

        GraduateDto dto = GraduateDto.builder()
                .firstName("   TrimMe   ")
                .lastName("    jiwfjwoiefj")
                .motivationLetter("   Motivace   ")
                .uniqueCode(UniqueCodeDto.builder().code(UNASSIGNED_CODE).build())
                .gdprConsent(true)
                .languages(" CZ K ")
                .otherCourses("     ")
                .availability(Availability.IMMEDIATELY)
                .contractTypes(ContractTypesDto.builder().hpp(true).build())
                .employmentTypes(EmploymentTypesDto.builder().partTime(true).partTimeHours("  20  ").build())
                .employmentRegimes(EmploymentRegimesDto.builder().remote(true).build())
                .preferredRoles(PreferredRolesDto.builder().tester(true).build())
                .workRegions(WorkRegionsDto.builder().praha(true).build())
                .cvData(CvDataDto.builder().data(base64).fileName("  cv.pdf  ").contentType("application/pdf").build())
                .build();

        String json = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = Long.valueOf(result.getResponse().getContentAsString());
        Graduate saved = graduateRepository.findById(id).orElseThrow();

        assertEquals("TrimMe", saved.getFirstName());
        assertEquals("jiwfjwoiefj", saved.getLastName());
        assertEquals("Motivace", saved.getMotivationLetter());
        assertNull(saved.getOtherCourses());
        assertEquals("CZ K", saved.getLanguages());
        assertEquals("20", saved.getEmploymentTypes().getPartTimeHours());
        assertEquals("cv.pdf", saved.getCvData().getFileName());
        assertEquals("application/pdf", saved.getCvData().getContentType());
    }
}