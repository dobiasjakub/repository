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
public class WorkRegionsDto {
    private boolean praha;
    private boolean stredoceskyKraj;
    private boolean jihoceskyKraj;
    private boolean plzenskyKraj;
    private boolean karlovarskyKraj;
    private boolean usteckyKraj;
    private boolean libereckyKraj;
    private boolean kralovehradeckyKraj;
    private boolean pardubickyKraj;
    private boolean krajVysocina;
    private boolean jihomoravskyKraj;
    private boolean zlinskyKraj;
    private boolean olomouckyKraj;
    private boolean moravskoslezskyKraj;
}
