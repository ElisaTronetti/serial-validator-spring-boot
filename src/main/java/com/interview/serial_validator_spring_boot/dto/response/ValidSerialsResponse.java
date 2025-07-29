package com.interview.serial_validator_spring_boot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public class ValidSerialsResponse {
    Collection<String> serials;
}
