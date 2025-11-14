package com.github.lassulfi.musicmanager.domain.valueobjects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;

public class NameTest {

    @Test
    @DisplayName("Should instantiate a valid name")
    void testCreateName() {
        // given
        final var expectedBirthName = "Stefani Joanne Angelina Germanotta";
        final var expectedStageName = "Lady Gaga";

        // when
        final var actualName = new Name(expectedBirthName, expectedStageName);

        // then
        Assertions.assertEquals(expectedBirthName, actualName.birthName());
        Assertions.assertEquals(expectedStageName, actualName.stageName());
    }

    @Test
    @DisplayName("Should not instantiate an invalid name")
    void testCreateNameWithInvalidStageName() {
        // given
        final var expectedMessage = "Invalid stage name";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> new Name(null, null));

        // then
        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }
}
