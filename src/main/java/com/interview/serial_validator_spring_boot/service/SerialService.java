package com.interview.serial_validator_spring_boot.service;

import java.util.Collection;

public interface SerialService {
    void cacheSerial(String serial);
    boolean isAlreadyValidated(String serial);
    Collection<String> getSerials();
}
