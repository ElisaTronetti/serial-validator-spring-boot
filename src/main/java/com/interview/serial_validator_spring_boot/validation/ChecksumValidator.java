package com.interview.serial_validator_spring_boot.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChecksumValidator implements ConstraintValidator<ValidChecksum, String> {
    private final SerialValidationCache serialValidationCache;

    @Autowired
    public ChecksumValidator(SerialValidationCache serialService) {
        this.serialValidationCache = serialService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (serialValidationCache.isAlreadyValidated(value)) {
            return true;
        }

        return computeChecksum(value);
    }

    private boolean computeChecksum(String value) {
        int sum = 0;
        for (char c : value.toCharArray()) {
            sum += c;
        }
        boolean isValid = sum % 7 == 0;
        if (isValid) {
            serialValidationCache.cacheSerial(value);
        }
        return isValid;
    }
}