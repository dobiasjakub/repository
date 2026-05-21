package com.luckybusiness.luckyapp.unit;

import com.luckybusiness.luckyapp.model.dtos.EmploymentTypesDto;
import com.luckybusiness.luckyapp.validations.PartTimeHoursValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PartTimeHoursValidatorTest {

    private ConstraintValidatorContext mockContext() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext node = mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(context.buildConstraintViolationWithTemplate(any())).thenReturn(builder);
        when(builder.addPropertyNode(any())).thenReturn(node);
        when(node.addConstraintViolation()).thenReturn(context);
        return context;
    }

    @Test
    void whenPartTimeTrueAndHoursNull_isInvalid() {
        PartTimeHoursValidator validator = new PartTimeHoursValidator();
        EmploymentTypesDto dto = EmploymentTypesDto.builder()
                .partTime(true)
                .partTimeHours(null)
                .build();

        assertFalse(validator.isValid(dto, mockContext()));
    }

    @Test
    void whenPartTimeTrueAndHoursProvided_isValid() {
        PartTimeHoursValidator validator = new PartTimeHoursValidator();
        EmploymentTypesDto dto = EmploymentTypesDto.builder()
                .partTime(true)
                .partTimeHours("20")
                .build();

        assertTrue(validator.isValid(dto, mockContext()));
    }

    @Test
    void whenPartTimeFalseHoursNull_isValid() {
        PartTimeHoursValidator validator = new PartTimeHoursValidator();
        EmploymentTypesDto dto = EmploymentTypesDto.builder()
                .partTime(false)
                .partTimeHours(null)
                .build();

        assertTrue(validator.isValid(dto, mockContext()));
    }
}
