package com.luckybusiness.luckyapp.mapper;

import com.luckybusiness.luckyapp.model.dtos.*;
import com.luckybusiness.luckyapp.model.entities.*;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface GraduateMapper {

    @Mapping(target = "uniqueCode", ignore = true)
    @Mapping(target = "cvData.data", ignore = true)
    GraduateDto entityToDto(Graduate graduate);

    GraduatePreviewDto entityToPreviewDto(Graduate graduate);

    @Mapping(source = "uniqueCode", target = "uniqueCode")
    @Mapping(target = "cvData.data", ignore = true)
    GraduateDto entityToDtoWithUniqueCode(Graduate graduate);

    UniqueCodeDto uniqueCodeEntityToDto(UniqueCode entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uniqueCode", ignore = true)
    @Mapping(target = "cvData", ignore = true)
    @Mapping(source = "contractTypes", target = "contractTypes")
    @Mapping(source = "employmentTypes", target = "employmentTypes")
    @Mapping(source = "employmentRegimes", target = "employmentRegimes")
    @Mapping(source = "preferredRoles", target = "preferredRoles")
    @Mapping(source = "workRegions", target = "workRegions")
    Graduate mapForCreation(GraduateDto dto);

    @Mapping(target = "id", ignore = true)
    ContractTypes contractTypesDtoToEntity(ContractTypesDto dto);
    @Mapping(target = "id", ignore = true)
    EmploymentTypes employmentTypesDtoToEntity(EmploymentTypesDto dto);
    @Mapping(target = "id", ignore = true)
    EmploymentRegimes employmentRegimesDtoToEntity(EmploymentRegimesDto dto);
    @Mapping(target = "id", ignore = true)
    PreferredRoles preferredRolesDtoToEntity(PreferredRolesDto dto);
    @Mapping(target = "id", ignore = true)
    WorkRegions workRegionsDtoToEntity(WorkRegionsDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uniqueCode", ignore = true)
    @Mapping(target = "gdprConsent", ignore = true)
    @Mapping(target = "cvData", ignore = true)
    @Mapping(source = "contractTypes", target = "contractTypes")
    @Mapping(source = "employmentTypes", target = "employmentTypes")
    @Mapping(source = "employmentRegimes", target = "employmentRegimes")
    @Mapping(source = "preferredRoles", target = "preferredRoles")
    @Mapping(source = "workRegions", target = "workRegions")
    void updateEntityFromDto(GraduateDto dto, @MappingTarget Graduate entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "graduate", ignore = true)
    @Mapping(source = "hpp", target = "hpp")
    @Mapping(source = "dppDpc", target = "dppDpc")
    @Mapping(source = "ico", target = "ico")
    void updateContractTypesFromDto(ContractTypesDto dto, @MappingTarget ContractTypes entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "graduate", ignore = true)
    @Mapping(source = "fullTime", target = "fullTime")
    @Mapping(source = "halfTime", target = "halfTime")
    @Mapping(source = "partTime", target = "partTime")
    @Mapping(source = "partTimeHours", target = "partTimeHours")
    void updateEmploymentTypesFromDto(EmploymentTypesDto dto, @MappingTarget EmploymentTypes entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "graduate", ignore = true)
    @Mapping(source = "onSite", target = "onSite")
    @Mapping(source = "hybrid", target = "hybrid")
    @Mapping(source = "remote", target = "remote")
    void updateEmploymentRegimesFromDto(EmploymentRegimesDto dto, @MappingTarget EmploymentRegimes entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "graduate", ignore = true)
    @Mapping(source = "tester", target = "tester")
    @Mapping(source = "itSupport", target = "itSupport")
    @Mapping(source = "automationTester", target = "automationTester")
    @Mapping(source = "testAnalyst", target = "testAnalyst")
    @Mapping(source = "testManager", target = "testManager")
    @Mapping(source = "frontendDeveloper", target = "frontendDeveloper")
    @Mapping(source = "backendDeveloper", target = "backendDeveloper")
    @Mapping(source = "dataAnalyst", target = "dataAnalyst")
    @Mapping(source = "scrumMaster", target = "scrumMaster")
    @Mapping(source = "productOwner", target = "productOwner")
    void updatePreferredRolesFromDto(PreferredRolesDto dto, @MappingTarget PreferredRoles entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "graduate", ignore = true)
    @Mapping(source = "praha", target = "praha")
    @Mapping(source = "stredoceskyKraj", target = "stredoceskyKraj")
    @Mapping(source = "jihoceskyKraj", target = "jihoceskyKraj")
    @Mapping(source = "plzenskyKraj", target = "plzenskyKraj")
    @Mapping(source = "karlovarskyKraj", target = "karlovarskyKraj")
    @Mapping(source = "usteckyKraj", target = "usteckyKraj")
    @Mapping(source = "libereckyKraj", target = "libereckyKraj")
    @Mapping(source = "kralovehradeckyKraj", target = "kralovehradeckyKraj")
    @Mapping(source = "pardubickyKraj", target = "pardubickyKraj")
    @Mapping(source = "krajVysocina", target = "krajVysocina")
    @Mapping(source = "jihomoravskyKraj", target = "jihomoravskyKraj")
    @Mapping(source = "zlinskyKraj", target = "zlinskyKraj")
    @Mapping(source = "olomouckyKraj", target = "olomouckyKraj")
    @Mapping(source = "moravskoslezskyKraj", target = "moravskoslezskyKraj")
    void updateWorkRegionsFromDto(WorkRegionsDto dto, @MappingTarget WorkRegions entity);
}