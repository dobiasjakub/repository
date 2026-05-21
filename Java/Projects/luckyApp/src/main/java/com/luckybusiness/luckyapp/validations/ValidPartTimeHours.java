package com.luckybusiness.luckyapp.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PartTimeHoursValidator.class)
public @interface ValidPartTimeHours {
    String message() default "Počet hodin musí být vyplněn při volbě částečného úvazku";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
