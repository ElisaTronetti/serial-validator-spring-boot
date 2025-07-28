package com.interview.serial_validator_spring_boot.exceptions;

import com.interview.serial_validator_spring_boot.dto.response.FailedSerialResponse;
import com.interview.serial_validator_spring_boot.enums.ResponseStatus;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailedSerialResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("Validation failed");

        FailedSerialResponse errorResponse = new FailedSerialResponse(ResponseStatus.INVALID.name(), errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }
}