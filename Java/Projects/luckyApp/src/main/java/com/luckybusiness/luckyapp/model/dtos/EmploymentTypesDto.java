package com.luckybusiness.luckyapp.model.dtos;

import com.luckybusiness.luckyapp.validations.RequireAtLeastOneTrue;
import com.luckybusiness.luckyapp.validations.ValidPartTimeHours;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RequireAtLeastOneTrue(message = "Toto je povinné pole, bez jeho vyplnění není možné vytvořit profil. Zadej, prosím, tvé kapacitní možnosti.")
@ValidPartTimeHours
public class EmploymentTypesDto {
    private boolean fullTime;
    private boolean halfTime;
    private boolean partTime;
    private String partTimeHours;
}
