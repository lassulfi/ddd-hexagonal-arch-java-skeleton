package com.github.lassulfi.musicmanager.domain.valueobjects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;

public class NationalityTest {

    @Test
    @DisplayName("Should instantiate a nationality name")
    void testCreateNationality() {
        // given
        final var expectedNationality = "Brazilian";

        // when
        final var actualNationality = new Nationality(expectedNationality);

        // then
        Assertions.assertEquals(expectedNationality, actualNationality.nationality());
    }

    @Test
    @DisplayName("Should not instantiate an invalid nationality")
    void testCreateNationalityWithInvalidNationality() {
        // given
        final var expectedMessage = "Invalid nationality";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> new Nationality(null));

        // then
        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }
}
