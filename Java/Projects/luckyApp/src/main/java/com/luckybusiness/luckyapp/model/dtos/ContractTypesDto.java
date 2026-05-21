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
public class ContractTypesDto {
    private boolean hpp;
    private boolean dppDpc;
    private boolean ico;
}
