package com.github.lassulfi.musicmanager.domain.valueobjects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;

public class LocationTest {

    @Test
    @DisplayName("Should instantiate a valid location")
    void testCreateLocation() {
        // given
        final var expectedCountry = "Brazil";
        final var expectedState = "Sao Paulo";
        final var expectedCity = "Sao Paulo";

        // when
        final var actualLocation = new Location(expectedCountry, expectedState, expectedCity);

        // then
        Assertions.assertEquals(expectedCountry, actualLocation.country());
        Assertions.assertEquals(expectedState, actualLocation.state());
        Assertions.assertEquals(expectedCity, actualLocation.city());
    }

    @Test
    @DisplayName("Should not instantiate an invalid location")
    void testCreateLocationWithInvalidCountry() {
        // given
        final var expectedMessage = "Invalid country location";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> new Location(null, null, null));

        // then
        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }
}
