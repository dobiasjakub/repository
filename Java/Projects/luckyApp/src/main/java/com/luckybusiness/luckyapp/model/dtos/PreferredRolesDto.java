package com.luckybusiness.luckyapp.model.dtos;

import com.luckybusiness.luckyapp.validations.RequireAtLeastOneTrue;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RequireAtLeastOneTrue(message = "Toto je povinné pole, bez jeho vyplnění není možné vytvořit profil.")
public class PreferredRolesDto {
    private boolean tester;
    private boolean itSupport;
    private boolean automationTester;
    private boolean testAnalyst;
    private boolean testManager;
    private boolean frontendDeveloper;
    private boolean backendDeveloper;
    private boolean dataAnalyst;
    private boolean scrumMaster;
    private boolean productOwner;
}
