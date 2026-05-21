package com.luckybusiness.luckyapp.unit;

import com.luckybusiness.luckyapp.validations.RequireAtLeastOneTrueValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequireAtLeastOneTrueValidatorTest {

    private static class Dummy {
        boolean a;
        Boolean b;
        Dummy(boolean a, Boolean b) {
            this.a = a;
            this.b = b;
        }
    }

    @Test
    void returnsTrueWhenAtLeastOneTrue() {
        RequireAtLeastOneTrueValidator validator = new RequireAtLeastOneTrueValidator();
        assertTrue(validator.isValid(new Dummy(false, true), null));
    }

    @Test
    void returnsFalseWhenAllFalse() {
        RequireAtLeastOneTrueValidator validator = new RequireAtLeastOneTrueValidator();
        assertFalse(validator.isValid(new Dummy(false, false), null));
    }

    @Test
    void returnsTrueWhenObjectIsNull() {
        RequireAtLeastOneTrueValidator validator = new RequireAtLeastOneTrueValidator();
        assertTrue(validator.isValid(null, null));
    }
}
