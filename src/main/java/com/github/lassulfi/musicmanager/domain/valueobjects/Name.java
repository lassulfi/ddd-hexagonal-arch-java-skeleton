package com.github.lassulfi.musicmanager.domain.valueobjects;

import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;

public record Name(String birthName, String stageName) {
    public Name {
        if (stageName == null || stageName.isBlank()) {
            throw new ValidationException("Invalid stage name");
        }
    }
}
