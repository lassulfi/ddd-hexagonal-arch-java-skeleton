package com.github.lassulfi.musicmanager.domain.song.model;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.lassulfi.musicmanager.domain.artist.model.ArtistId;
import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;

public class SongTest {

    @Test
    @DisplayName("Should instantiate a new song")
    void testCreateNewSong() {
        // given
        final var expectedArtistId = ArtistId.unique();
        final var expectedTitle = "Good Song";
        final var expectedLyrics = "la la";
        final var expectedDate = LocalDate.of(2021, 1, 1);

        // when
        final var actualSong = Song.newSong(expectedTitle, expectedLyrics, expectedDate, expectedArtistId);

        // then
        Assertions.assertNotNull(actualSong.getSongId());
        Assertions.assertEquals(expectedTitle, actualSong.getTitle());
        Assertions.assertEquals(expectedLyrics, actualSong.getLyrics());
        Assertions.assertEquals(expectedArtistId, actualSong.getArtistId());
    }

    @Test
    @DisplayName("Should not instantiate a new song given an invalid title")
    void testCreateSongWithInvalidTitle() {
        // given
        final var expectedDate = LocalDate.now();
        final var expectedLyrics = "x";
        final var expectedMessage = "Invalid title";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> Song.newSong(null, expectedLyrics, expectedDate, ArtistId.unique()));

        // then
        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should not instantiate a new song given an invalid artistId")
    void testCreateSongWithInvalidArtistId() {
        // given
        final var expectedDate = LocalDate.now();
        final var expectedTitle = "Good Song";
        final var expectedMessage = "Invalid ArtistId";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> Song.newSong(expectedTitle, "x", expectedDate, null));

        // then
        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should add collaborator to the song")
    void testAddCollaboratorAddsACollaborator() {
        // given
        final var expectedArtistId = ArtistId.unique();
        final var actualSong = Song.newSong("t", null, LocalDate.now(), expectedArtistId);

        final var expectedCollaboratorId = ArtistId.unique();

        // when
        actualSong.addCollaborator(expectedCollaboratorId);

        // then
        Assertions.assertEquals(1, actualSong.allCollaborators().size());
        Assertions.assertTrue(actualSong.allCollaborators().contains(expectedCollaboratorId));
    }

    @Test
    @DisplayName("Should not add an invalid collaborator")
    void testAddCollaboratorWithInvalidCollaboratorId() {
        // given
        final var expectedArtistId = ArtistId.unique();
        final var actualSong = Song.newSong("t", null, LocalDate.now(), expectedArtistId);
        final var expectedMessage = "Invalid collaborator ArtistId";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> actualSong.addCollaborator(null));

        // then
        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should not add artist as collaborator")
    void testAddCollaboratorWithArtistAsCollaborator() {
        // given
        final var expectedArtistId = ArtistId.unique();
        final var actualSong = Song.newSong("t", null, LocalDate.now(), expectedArtistId);
        final var expectedMessage = "Artist cannot be the collaborator for the song";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> actualSong.addCollaborator(expectedArtistId));

        // then
        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Should not add duplicate collaborator")
    void testAddCollaboratorWithDuplicateCollaborator() {
        // given
        final var expectedArtistId = ArtistId.unique();
        final var actualSong = Song.newSong("t", null, LocalDate.now(), expectedArtistId);

        final var expectedCollaborator = ArtistId.unique();
        actualSong.addCollaborator(expectedCollaborator);

        final var expectedMessage = "Collaborator already added";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> actualSong.addCollaborator(expectedCollaborator));

        // then
        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("allCollaborators should return all collaborators")
    void testAllCollaborators() {
        // given
        final var expectedArtistId = ArtistId.unique();
        final var actualSong = Song.newSong("t", null, LocalDate.now(), expectedArtistId);

        // when / then
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            actualSong.allCollaborators().add(ArtistId.unique());
        });
    }
}
