package com.github.lassulfi.musicmanager.domain.artist.model;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.lassulfi.musicmanager.domain.album.model.AlbumId;
import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;
import com.github.lassulfi.musicmanager.domain.song.model.SongId;

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
        final var expectedAlbumId = AlbumId.unique();

        // when
        actualArtist.addAlbum(expectedAlbumId);

        // then
        Assertions.assertEquals(1, actualArtist.allAlbums().size());
        final var album = actualArtist.allAlbums().stream().findFirst().orElseThrow();
        Assertions.assertEquals(expectedAlbumId, album);
    }

    @Test
    @DisplayName("Should not add duplicate album")
    void testAddAlbumWithDuplicateAlbum() {
        // given
        final var actualArtist = Artist.newArtist("birth", "stage", "c", "s", "ci", "n", LocalDate.now(), null);
        final var albumId = AlbumId.unique();
        actualArtist.addAlbum(albumId);
        final var expectedMessage = "Album already released.";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> actualArtist.addAlbum(albumId));

        // then
        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should add song to the artist")
    void testAddSongAdds() {
        // given
        final var actualArtist = Artist.newArtist("birth", "stage", "c", "s", "ci", "n", LocalDate.now(), null);
        final var expectedSongId = SongId.unique();

        // when
        actualArtist.addSong(expectedSongId);

        // then
        Assertions.assertEquals(1, actualArtist.allSongs().size());
        final var song = actualArtist.allSongs().stream().findFirst().orElseThrow();
        Assertions.assertEquals(expectedSongId, song);
    }
}
