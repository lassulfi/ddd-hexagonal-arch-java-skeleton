package com.github.lassulfi.musicmanager.application.usecase.album;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.lassulfi.musicmanager.domain.artist.model.Artist;
import com.github.lassulfi.musicmanager.domain.artist.model.ArtistId;
import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;
import com.github.lassulfi.musicmanager.domain.song.model.Song;
import com.github.lassulfi.musicmanager.domain.song.model.SongId;
import com.github.lassulfi.musicmanager.repository.InMemoryAlbumRepository;
import com.github.lassulfi.musicmanager.repository.InMemoryArtistRepository;
import com.github.lassulfi.musicmanager.repository.InMemorySongRepository;

public class CreateAlbumUseCaseTest {

    @Test
    @DisplayName("Should create album")
    void testCreateAlbum() {
        // given
        final var artist = Artist.newArtist("birth", "stage", "country", "state", "city", "nat",
                LocalDate.of(1980, 1, 1), null);

        final var artistRepo = new InMemoryArtistRepository();
        artistRepo.create(artist);

        final var songRepo = new InMemorySongRepository();
        final var song1 = Song.newSong("s1", "l1", LocalDate.of(2020, 1, 1), artist.getArtistId());
        final var song2 = Song.newSong("s2", "l2", LocalDate.of(2020, 2, 2), artist.getArtistId());
        songRepo.create(song1);
        songRepo.create(song2);

        final var albumRepo = new InMemoryAlbumRepository();

        final var useCase = new CreateAlbumUseCase(artistRepo, songRepo, albumRepo);

        final var input = new CreateAlbumUseCase.Input("Album Title", LocalDate.of(2021, 5, 5),
                artist.getArtistId().value(), Set.of(song1.getSongId().value(), song2.getSongId().value()));

        // when
        final var output = useCase.execute(input);

        // then
        assertNotNull(output.id());
        assertEquals("Album Title", output.title());
        assertEquals(LocalDate.of(2021, 5, 5), output.releaseDate());
        assertEquals(artist.getArtistId().value(), output.artistId());
        assertEquals(2, output.songs().size());

        // album persisted and artist updated
        final var updatedArtist = artistRepo.artistOfId(ArtistId.with(artist.getArtistId().value())).orElseThrow();
        assertEquals(1, updatedArtist.allAlbums().size());
        final var albumId = updatedArtist.allAlbums().iterator().next();
        assertTrue(albumRepo.albumOfId(albumId).isPresent());

        // songs assigned to album
        final var stored1 = songRepo.songOfId(song1.getSongId()).orElseThrow();
        final var stored2 = songRepo.songOfId(song2.getSongId()).orElseThrow();
        assertEquals(albumId, stored1.getAlbum());
        assertEquals(albumId, stored2.getAlbum());
    }

    @Test
    @DisplayName("Should fail when artist not found")
    void testCreateAlbumArtistNotFound() {
        final var artistRepo = new InMemoryArtistRepository();
        final var songRepo = new InMemorySongRepository();
        final var albumRepo = new InMemoryAlbumRepository();

        final var useCase = new CreateAlbumUseCase(artistRepo, songRepo, albumRepo);

        final var input = new CreateAlbumUseCase.Input("Album", LocalDate.now(), ArtistId.unique().value(), Set.of());

        final var ex = assertThrows(ValidationException.class, () -> useCase.execute(input));
        assertEquals("Artist not found", ex.getMessage());
    }

    @Test
    @DisplayName("Should fail when a song id is not found")
    void testCreateAlbumSongNotFound() {
        // given
        final var artist = Artist.newArtist("birth", "stage", "country", "state", "city", "nat",
                LocalDate.of(1980, 1, 1), null);
        final var artistRepo = new InMemoryArtistRepository();
        artistRepo.create(artist);

        final var songRepo = new InMemorySongRepository();
        final var albumRepo = new InMemoryAlbumRepository();

        final var useCase = new CreateAlbumUseCase(artistRepo, songRepo, albumRepo);

        final var missingSongId = SongId.unique().value();

        final var input = new CreateAlbumUseCase.Input("Album", LocalDate.now(), artist.getArtistId().value(),
                Set.of(missingSongId));

        final var ex = assertThrows(ValidationException.class, () -> useCase.execute(input));
        assertEquals(String.format("Song of ID %s not found", missingSongId), ex.getMessage());
    }
}
