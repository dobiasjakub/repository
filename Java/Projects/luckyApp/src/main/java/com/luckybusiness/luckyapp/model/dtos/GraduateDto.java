package com.luckybusiness.luckyapp.model.dtos;

import com.luckybusiness.luckyapp.model.enums.Availability;
import com.luckybusiness.luckyapp.model.enums.LuckyCourse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Schema(description = "DTO pro zobrazení a update absolventů.")
public class GraduateDto {
    private Long id;

    @NotBlank(message = "Toto je povinné pole, bez jeho vyplnění není možné vytvořit profil.")
    @Size(max = 50, message = "Maximální počet znaků pro toto pole je 50 znaků.")
    private String firstName;

    @NotBlank(message = "Toto je povinné pole, bez jeho vyplnění není možné vytvořit profil.")
    @Size(max = 100, message = "Maximální počet znaků pro toto pole je 100 znaků.")
    private String lastName;

    @NotBlank(message = "Toto je povinné pole, bez jeho vyplnění není možné vytvořit profil.")
    @Size(min = 4, max = 150, message = "Minimální délka je 4 znaky, maximální délka je omezena na 150 znaků.")
    private String languages;

    @NotNull(message = "Toto je povinné pole, bez jeho vyplnění není možné vytvořit profil.")
    private Availability availability;

    private LuckyCourse luckyCourse;

    @Size(min = 4, max = 1000, message = "Minimální délka je 4 znaky, maximální počet znaků pro toto pole je 1000 znaků.")
    private String motivationLetter;

    @AssertTrue(message = "Toto je povinné pole, bez jeho vyplnění není možné vytvořit profil.")
    private boolean gdprConsent;

    @Size(min = 4, max = 1000, message = "Minimální délka je 4 znaky, maximální počet znaků pro toto pole je 1000 znaků.")
    private String otherCourses;

    private boolean profileVisible;

    @NotNull(message = "Bez unikátního kodu není možné vytvořit ani upravit profil")
    private UniqueCodeDto uniqueCode;

    @NotNull(message = "Výběr typu spolupráce je povinný.")
    @Valid
    private ContractTypesDto contractTypes;

    @NotNull(message = "Výběr typu úvazku je povinný.")
    @Valid
    private EmploymentTypesDto employmentTypes;

    @NotNull(message = "Výběr preferovaného režimu práce je povinný.")
    @Valid
    private EmploymentRegimesDto employmentRegimes;

    @NotNull(message = "Výběr hledané role je povinný.")
    @Valid
    private PreferredRolesDto preferredRoles;

    @NotNull(message = "Výběr regionu pro hledání práce je povinný.")
    @Valid
    private WorkRegionsDto workRegions;

    @NotNull(message = "Nahrání CV je povinné.")
    @Valid
    private CvDataDto cvData;
}
