package com.interview.serial_validator_spring_boot.dto.request;

import com.interview.serial_validator_spring_boot.validation.ValidChecksum;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class SerialDto {
    @Pattern(regexp = "^[A-Z][A-Za-z0-9]{15}$", message = "Checksum failed")
    @ValidChecksum
    @NonNull
    private String serial;
}
