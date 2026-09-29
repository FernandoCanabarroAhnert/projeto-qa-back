package com.fapa.back.shared.annotations.validators;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fapa.back.shared.annotations.IsEnum;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsEnumValidator implements ConstraintValidator<IsEnum, String> {

    private Set<String> allowedValues;

    @Override
    public void initialize(IsEnum annotation) {
        this.allowedValues = Stream.of(annotation.enumClass().getEnumConstants())
            .map(Enum::name)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return allowedValues.contains(value.toUpperCase());
    }

}
