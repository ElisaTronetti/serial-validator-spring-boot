package com.interview.serial_validator_spring_boot.controller;

import com.interview.serial_validator_spring_boot.dto.request.SerialDto;
import com.interview.serial_validator_spring_boot.dto.response.SerialResponse;
import com.interview.serial_validator_spring_boot.dto.response.SuccessSerialResponse;
import com.interview.serial_validator_spring_boot.dto.response.ValidSerialsResponse;
import com.interview.serial_validator_spring_boot.enums.ResponseStatus;
import com.interview.serial_validator_spring_boot.service.SerialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class SerialValidatorController {

    @Autowired
    SerialService serialService;

    @PostMapping("/serial")
    public ResponseEntity<SerialResponse> uploadSerial(
            @Valid @RequestBody SerialDto serial
    ) {
        return ResponseEntity.ok(
                new SuccessSerialResponse(ResponseStatus.VALID.name(), serial.getSerial())
        );
    }

    @GetMapping("/valid-serials")
    public ResponseEntity<ValidSerialsResponse> getValidSerials() {
        Collection<String> validSerials = serialService.getSerials();
        return ResponseEntity.ok(
                new ValidSerialsResponse(validSerials)
        );
    }
}
