package com.github.lassulfi.musicmanager.domain.valueobjects;

import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;

public record Nationality(String nationality) {
    public Nationality {
        if (nationality == null || nationality.isBlank()) {
            throw new ValidationException("Invalid nationality");
        }
    }
}
