package com.github.lassulfi.musicmanager.application.usecase.song;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.lassulfi.musicmanager.domain.artist.model.Artist;
import com.github.lassulfi.musicmanager.domain.artist.model.ArtistId;
import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;
import com.github.lassulfi.musicmanager.domain.song.model.SongId;
import com.github.lassulfi.musicmanager.repository.InMemoryArtistRepository;
import com.github.lassulfi.musicmanager.repository.InMemorySongRepository;

public class CreateSongUseCaseTest {

    @Test
    @DisplayName("Should create song")
    void testCreateSong() {
        // given
        final var artist = Artist.newArtist("birth", "stage", "country", "state", "city", "nat",
                LocalDate.of(1980, 1, 1), null);

        final var artistRepository = new InMemoryArtistRepository();
        artistRepository.create(artist);

        final var songRepository = new InMemorySongRepository();

        final var useCase = new CreateSongUseCase(artistRepository, songRepository);

        final var input = new CreateSongUseCase.Input("Song title", "lyrics", LocalDate.of(2023, 3, 3),
                artist.getArtistId().value(), null);

        // when
        final var output = useCase.execute(input);

        // then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals("Song title", output.title());
        Assertions.assertEquals("lyrics", output.lyrics());
        Assertions.assertEquals(LocalDate.of(2023, 3, 3), output.releaseDate());
        Assertions.assertEquals(artist.getArtistId().value(), output.artistId());
        Assertions.assertTrue(output.collaborators().isEmpty());

        // repository side-effects
        final var stored = songRepository.songOfId(SongId.with(output.id()));
        Assertions.assertTrue(stored.isPresent());

        final var updatedArtist = artistRepository.artistOfId(ArtistId.with(artist.getArtistId().value()));
        Assertions.assertTrue(updatedArtist.isPresent());
        Assertions.assertEquals(1, updatedArtist.get().allSongs().size());
    }

    @Test
    @DisplayName("Should fail when artist not found")
    void testCreateSongArtistNotFound() {
        // given
        final var artistRepository = new InMemoryArtistRepository();
        final var songRepository = new InMemorySongRepository();

        final var useCase = new CreateSongUseCase(artistRepository, songRepository);

        final var input = new CreateSongUseCase.Input("Song title", "lyrics", LocalDate.now(),
                ArtistId.unique().value(), null);

        // when / then
        final var ex = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(input));
        Assertions.assertEquals("Artist not found", ex.getMessage());
    }

    @Test
    @DisplayName("Should fail when a collaborator id is not present in artist repository")
    void testCreateSongCollaboratorNotFound() {
        // given
        final var artist = Artist.newArtist("birth", "stage", "country", "state", "city", "nat",
                LocalDate.of(1980, 1, 1), null);

        final var artistRepository = new InMemoryArtistRepository();
        artistRepository.create(artist);

        final var songRepository = new InMemorySongRepository();

        final var useCase = new CreateSongUseCase(artistRepository, songRepository);

        final var missingCollaboratorId = ArtistId.unique().value();

        final var input = new CreateSongUseCase.Input("Song title", "lyrics", LocalDate.now(),
                artist.getArtistId().value(), Collections.singleton(missingCollaboratorId));

        // when / then
        final var ex = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(input));
        Assertions.assertEquals(String.format("Collaborator of id %s not found", missingCollaboratorId),
                ex.getMessage());
    }

    @Test
    @DisplayName("Should create song with collaborators")
    void testCreateSongWithCollaborators() {
        // given
        final var artist = Artist.newArtist("birth", "stage", "country", "state", "city", "nat",
                LocalDate.of(1980, 1, 1), null);

        final var collaborator = Artist.newArtist("col-birth", "col-stage", "country", "state", "city",
                "nat", LocalDate.of(1985, 2, 2), null);

        final var artistRepository = new InMemoryArtistRepository();
        artistRepository.create(artist);
        artistRepository.create(collaborator);

        final var songRepository = new InMemorySongRepository();

        final var useCase = new CreateSongUseCase(artistRepository, songRepository);

        final var input = new CreateSongUseCase.Input("Song title", "lyrics", LocalDate.now(),
                artist.getArtistId().value(), Set.of(collaborator.getArtistId().value()));

        // when
        final var output = useCase.execute(input);

        // then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(1, output.collaborators().size());
        Assertions.assertTrue(output.collaborators().contains(collaborator.getArtistId().value()));

        // persisted
        final var stored = songRepository.songOfId(SongId.with(output.id()));
        Assertions.assertTrue(stored.isPresent());
    }
}
