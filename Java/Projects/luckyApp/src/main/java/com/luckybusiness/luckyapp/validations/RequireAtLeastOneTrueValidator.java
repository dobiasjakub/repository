package com.luckybusiness.luckyapp.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class RequireAtLeastOneTrueValidator implements ConstraintValidator<RequireAtLeastOneTrue, Object> {

    @Override
    public boolean isValid(Object objectToValidate, ConstraintValidatorContext context) {
        if (objectToValidate == null) return true;

        Class<?> clazz = objectToValidate.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            Class<?> type = field.getType();
            if (type == boolean.class || type == Boolean.class) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(objectToValidate);
                    if (Boolean.TRUE.equals(value)) {
                        return true;
                    }
                } catch (IllegalAccessException e) {
                    System.err.println("Chyba při validaci pole " + field.getName() + " ve třídě " + clazz.getSimpleName() + ": " + e.getMessage());
                    return false;
                } finally {
                    field.setAccessible(false);
                }
            }
        }

        return false;
    }
}