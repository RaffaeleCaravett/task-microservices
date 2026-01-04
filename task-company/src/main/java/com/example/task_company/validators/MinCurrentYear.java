package com.example.task_company.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinCurrentYearValidator.class)
public @interface MinCurrentYear {
    String message() default "Anno carta non valido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
