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
public class EmploymentRegimesDto {
    private boolean onSite;
    private boolean hybrid;
    private boolean remote;
}
