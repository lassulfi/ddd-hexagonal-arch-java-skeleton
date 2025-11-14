package com.github.lassulfi.musicmanager.domain.artist.model;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;

public class ArtistTest {

    @Test
    @DisplayName("Should instantiate a new artist")
    void testCreateNewArtist() {
        // given
        final var expectedBirthName = "John Doe";
        final var expectedStageName = "JD";
        final var expectedCountry = "Country";
        final var expectedState = "State";
        final var expectedCity = "City";
        final var expectedNationality = "Nationality";
        final var expectedBirthDate = LocalDate.of(1990, 1, 1);

        // when
        final var actualArtist = Artist.newArtist(expectedBirthName, expectedStageName, expectedCountry, expectedState, expectedCity, expectedNationality,
                expectedBirthDate, null);

        // then
        Assertions.assertNotNull(actualArtist.getArtistId());
        Assertions.assertNotNull(actualArtist.getName());
        Assertions.assertNotNull(actualArtist.getLocation());
        Assertions.assertNotNull(actualArtist.getNationality());
        Assertions.assertNotNull(actualArtist.getLifetime());
    }

    @Test
    @DisplayName("Should add album to the artist")
    void testAddAlbumAdds() {
        // given
        final var actualArtist = Artist.newArtist("birth", "stage", "c", "s", "ci", "n", LocalDate.now(), null);
        final var expectedTitle = "My Album";
        final var expectedReleaseDate = LocalDate.of(2020, 5, 5);

        // when
        actualArtist.addAlbum(expectedTitle, expectedReleaseDate);

        // then
        Assertions.assertEquals(1, actualArtist.allAlbums().size());
        final var album = actualArtist.allAlbums().stream().findFirst().orElseThrow();
        Assertions.assertEquals(expectedTitle, album.getTitle());
        Assertions.assertEquals(expectedReleaseDate, album.getReleaseDate());
    }

    @Test
    @DisplayName("Should not add album with invalid title")
    void testAddAlbumWithInvalidTitle() {
        // given
        final var actualArtist = Artist.newArtist("birth", "stage", "c", "s", "ci", "n", LocalDate.now(), null);
        final var expectedMessage = "Invalid Album title";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> actualArtist.addAlbum(null, LocalDate.now()));

        // then
        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should not add duplicate album")
    void testAddAlbumWithDuplicateAlbum() {
        // given
        final var actualArtist = Artist.newArtist("birth", "stage", "c", "s", "ci", "n", LocalDate.now(), null);
        final var expectedTitle = "My Album";
        final var expectedReleaseDate = LocalDate.of(2020, 6, 6);
        actualArtist.addAlbum(expectedTitle, expectedReleaseDate);
        final var expectedMessage = "Album already released.";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> actualArtist.addAlbum(expectedTitle, expectedReleaseDate));

        // then
        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should add song to the artist")
    void testAddSongAdds() {
        // given
        final var actualArtist = Artist.newArtist("birth", "stage", "c", "s", "ci", "n", LocalDate.now(), null);
        final var title = "Song 1";
        final var releaseDate = LocalDate.of(2021, 2, 2);

        // when
        actualArtist.addSong(title, "lyrics", releaseDate, Collections.emptySet());

        // then
        Assertions.assertEquals(1, actualArtist.allSongs().size());
        final var song = actualArtist.allSongs().stream().findFirst().orElseThrow();
        Assertions.assertEquals(title, song.getTitle());
        Assertions.assertEquals(releaseDate, song.getReleaseDate());
    }
}
