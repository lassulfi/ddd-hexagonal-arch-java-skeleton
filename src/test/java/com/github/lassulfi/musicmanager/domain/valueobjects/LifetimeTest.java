package com.github.lassulfi.musicmanager.domain.valueobjects;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;

public class LifetimeTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"2007-12-03"})
    @DisplayName("Should instantiate a valid lifetime")
    void testCreateLifetime(String dateOfDeath) {
        // given
        final var expectedDateOfDeath = dateOfDeath != null ? LocalDate.parse(dateOfDeath) : null;
        final var expectedBirthDate = dateOfDeath != null ? expectedDateOfDeath.minusYears(10L) : LocalDate.now();

        // when
        final var actualLifetime = new Lifetime(expectedBirthDate, expectedDateOfDeath);

        // then
        Assertions.assertEquals(expectedBirthDate, actualLifetime.birthDate());
        Assertions.assertEquals(expectedDateOfDeath, actualLifetime.dateOfDeath());
    }

    @Test
    @DisplayName("Should not instantiate lifetime for null birthdate")
    void testCreateLifetimeForNullBirthDate() {
        // given
        final var expectedMessage = "Invalid birthdate";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> new Lifetime(null, null));

        // then
        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should not instantiate lifetime for future birth dates")
    void testCreateLifetimeForFutureBirthDate() {
        // given
        final var expectedMessage = "Birthdate cannot be a future date";
        final var actualBirthDate = LocalDate.now().plusDays(10L);

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> new Lifetime(actualBirthDate, null));
        
        // then
        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should nor instantiate lifetime if death date is before birth date")
    void testCreateLifetimeForDeathDateThatAreBeforeBirthDate() {
        // given
        final var expectedMessage = "Date of death is before birthdate";
        final var actualBirthDate = LocalDate.now();
        final var actualDeathDate = actualBirthDate.minusYears(10L);

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> new Lifetime(actualBirthDate, actualDeathDate));

        // then
        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }
}
