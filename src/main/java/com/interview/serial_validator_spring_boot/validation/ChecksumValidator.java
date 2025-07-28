package com.interview.serial_validator_spring_boot.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ChecksumValidator implements ConstraintValidator<ValidChecksum, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        int sum = 0;
        for (char c : value.toCharArray()) {
            sum += c;
        }
        return sum % 7 == 0;
    }
}