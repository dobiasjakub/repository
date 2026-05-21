package com.luckybusiness.luckyapp.validations;

import com.luckybusiness.luckyapp.model.dtos.EmploymentTypesDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.logging.log4j.util.Strings;

public class PartTimeHoursValidator implements ConstraintValidator<ValidPartTimeHours, EmploymentTypesDto> {

    @Override
    public boolean isValid(EmploymentTypesDto dto, ConstraintValidatorContext context) {
        if (dto.isPartTime() && Strings.isBlank(dto.getPartTimeHours())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Počet hodin musí být vyplněn při volbě částečného úvazku")
                    .addPropertyNode("partTimeHours")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
