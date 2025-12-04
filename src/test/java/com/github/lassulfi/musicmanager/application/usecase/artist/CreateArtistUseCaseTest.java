package com.github.lassulfi.musicmanager.application.usecase.artist;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.lassulfi.musicmanager.domain.artist.model.Artist;
import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;
import com.github.lassulfi.musicmanager.repository.InMemoryArtistRepository;

public class CreateArtistUseCaseTest {

    @Test
    @DisplayName("Should create artist")
    void testCreateAnArtist() {
        // given
        final var expectedBirthName = "John Doe";
        final var expectedStageName = "JD";
        final var expectedCountry = "Country";
        final var expectedState = "State";
        final var expectedCity = "City";
        final var expectedNationality = "Nationality";
        final var expectedBirthDate = LocalDate.of(1990, 1, 1);

        final var createInput = new CreateArtistUseCase.Input(expectedBirthName, expectedStageName, expectedCountry, expectedState, expectedCity, expectedNationality, expectedBirthDate, null);

        // when

        final var artistRepository = new InMemoryArtistRepository();

        final var useCase = new CreateArtistUseCase(artistRepository);

        final var output = useCase.execute(createInput);

        // then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedBirthName, output.birthName());
        Assertions.assertEquals(expectedStageName, output.stageName());
        Assertions.assertEquals(expectedCountry, output.country());
        Assertions.assertEquals(expectedState, output.state());
        Assertions.assertEquals(expectedCity, output.city());
        Assertions.assertEquals(expectedNationality, output.nationality());
        Assertions.assertEquals(expectedBirthDate, output.birthDate());
    }

    @Test
    @DisplayName("Should not create an artist with duplicated name")
    void testCreateWithDuplicatedNameShouldFail() throws Exception {
        final var expectedBirthName = "John Doe";
        final var expectedStageName = "JD";
        final var expectedCountry = "Country";
        final var expectedState = "State";
        final var expectedCity = "City";
        final var expectedNationality = "Nationality";
        final var expectedBirthDate = LocalDate.of(1990, 1, 1);

        final var expectedError = "Artist already exists";

        final var anArtist = Artist.newArtist(expectedBirthName, expectedStageName, expectedCountry, expectedState, expectedCity, expectedNationality, expectedBirthDate, null);

        final var artistRepository = new InMemoryArtistRepository();
        artistRepository.create(anArtist);

        final var createInput = new CreateArtistUseCase.Input(expectedBirthName, expectedStageName, expectedCountry, expectedState, expectedCity, expectedNationality, expectedBirthDate, null);

        // when

        final var useCase = new CreateArtistUseCase(artistRepository);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        // then

        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}
