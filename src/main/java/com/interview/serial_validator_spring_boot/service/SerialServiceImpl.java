package com.interview.serial_validator_spring_boot.service;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SerialServiceImpl implements SerialService {
    private final Set<String> validSerials = ConcurrentHashMap.newKeySet();

    @Override
    public void cacheSerial(String serial) {
        validSerials.add(serial);
    }

    @Override
    public boolean isAlreadyValidated(String serial) {
        return validSerials.contains(serial);
    }

    @Override
    public Collection<String> getSerials() {
        return validSerials;
    }
}
