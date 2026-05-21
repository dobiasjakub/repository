package com.luckybusiness.luckyapp.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validační anotace, která ověřuje, zda alespoň jedno boolean pole
 * v anotovaném objektu je nastaveno na true.
 */
@Documented
@Constraint(validatedBy = RequireAtLeastOneTrueValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAtLeastOneTrue {
    String message() default "Je nutné vybrat alespoň jednu možnost.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
