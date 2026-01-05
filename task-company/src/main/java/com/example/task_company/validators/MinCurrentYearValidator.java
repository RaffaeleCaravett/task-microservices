package com.example.task_company.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Year;

public class MinCurrentYearValidator implements ConstraintValidator<MinCurrentYear, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) return false;
        int currentYear = Year.now().getValue() % 100;
        return value >= currentYear;
    }
}