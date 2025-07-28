package com.interview.serial_validator_spring_boot.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ChecksumValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidChecksum {
    String message() default "Checksum failed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}