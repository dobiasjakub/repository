package com.luckybusiness.luckyapp.model.entities;

import com.luckybusiness.luckyapp.model.enums.Availability;
import com.luckybusiness.luckyapp.model.enums.LuckyCourse;
import com.luckybusiness.luckyapp.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@SequenceGenerator(name = "graduate_seq", sequenceName = "graduate_seq", allocationSize = 1)
public class Graduate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "graduate_seq")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "unique_code_id", unique = true)
    private UniqueCode uniqueCode;

    private LocalDateTime created;

    private LocalDateTime modified;

    @Enumerated(EnumType.STRING)
    private Role role;

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
    @Enumerated(EnumType.STRING)
    private Availability availability;

    @Enumerated(EnumType.STRING)
    private LuckyCourse luckyCourse;

    @Size(min = 4, max = 1000, message = "Maximální počet znaků pro toto pole je 1000 znaků.")
    private String motivationLetter;

    @NotNull(message = "Toto je povinné pole, bez jeho vyplnění není možné vytvořit profil.")
    private boolean gdprConsent;

    @Size(min = 4, max = 1000, message = "Minimální délka 4 znaky, maximální 1000.")
    private String otherCourses;

    private boolean profileVisible;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "contract_types_id")
    @NotNull
    private ContractTypes contractTypes;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "employment_types_id")
    @NotNull
    private EmploymentTypes employmentTypes;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "employment_regimes_id")
    @NotNull
    private EmploymentRegimes employmentRegimes;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "preferred_roles_id")
    @NotNull
    private PreferredRoles preferredRoles;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "work_regions_id")
    @NotNull
    private WorkRegions workRegions;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cv_data_id")
    @ToString.Exclude
    @NotNull(message = "Toto je povinné pole, bez jeho vyplnění není možné vytvořit profil.")
    private CvData cvData;

}
