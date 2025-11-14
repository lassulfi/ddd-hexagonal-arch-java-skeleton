package com.github.lassulfi.musicmanager.domain.valueobjects;

import java.time.LocalDate;

import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;

public record Lifetime(LocalDate birthDate, LocalDate dateOfDeath) {
    public Lifetime {
        if (birthDate == null) {
            throw new ValidationException("Invalid birthdate");
        }

        if (birthDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Birthdate cannot be a future date");
        }

        if (dateOfDeath != null && dateOfDeath.isBefore(birthDate)) {
            throw new ValidationException("Date of death is before birthdate");
        }
    }
}
