package com.github.lassulfi.musicmanager.domain.valueobjects;

import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;

public record Location(String country, String state, String city) {
    public Location {
        if (country == null || country.isBlank()) {
            throw new ValidationException("Invalid country location");
        }
    }
}
