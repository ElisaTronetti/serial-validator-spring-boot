package com.interview.serial_validator_spring_boot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FailedSerialResponse implements SerialResponse {
    private String status;
    private final String reason;
}
