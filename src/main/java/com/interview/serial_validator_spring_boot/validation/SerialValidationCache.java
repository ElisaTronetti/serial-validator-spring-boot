package com.interview.serial_validator_spring_boot.validation;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SerialValidationCache {
    private final Set<String> validSerials = ConcurrentHashMap.newKeySet();

    public void cacheSerial(String serial) {
        validSerials.add(serial);
    }

    public boolean isAlreadyValidated(String serial) {
        return validSerials.contains(serial);
    }

    public Collection<String> getSerials() {
        return validSerials;
    }
}
