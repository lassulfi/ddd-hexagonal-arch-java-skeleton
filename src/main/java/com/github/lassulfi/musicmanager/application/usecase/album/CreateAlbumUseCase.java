package com.github.lassulfi.musicmanager.application.usecase.album;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.lassulfi.musicmanager.application.usecase.UseCase;
import com.github.lassulfi.musicmanager.domain.album.model.Album;
import com.github.lassulfi.musicmanager.domain.album.repository.AlbumRepository;
import com.github.lassulfi.musicmanager.domain.artist.model.ArtistId;
import com.github.lassulfi.musicmanager.domain.artist.repository.ArtistRepository;
import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;
import com.github.lassulfi.musicmanager.domain.song.model.SongId;
import com.github.lassulfi.musicmanager.domain.song.repository.SongRepository;

public class CreateAlbumUseCase extends UseCase<CreateAlbumUseCase.Input, CreateAlbumUseCase.Output> {

    private final ArtistRepository artistRepository;

    private final SongRepository songRepository;

    private final AlbumRepository albumRepository;

    public CreateAlbumUseCase(final ArtistRepository artistRepository, final SongRepository songRepository,
            AlbumRepository albumRepository) {
        this.artistRepository = Objects.requireNonNull(artistRepository);
        this.songRepository = Objects.requireNonNull(songRepository);
        this.albumRepository = Objects.requireNonNull(albumRepository);
    }

    @Override
    public Output execute(Input input) {

        var anArtist = artistRepository.artistOfId(ArtistId.with(input.artistId))
                .orElseThrow(() -> new ValidationException("Artist not found"));

        var songs = input.songs.parallelStream()
                .map(s -> songRepository.songOfId(SongId.with(s))
                        .orElseThrow(() -> new ValidationException(String.format("Song of ID %s not found", s))))
                .collect(Collectors.toSet());

        final var anAlbum = Album.newAlbum(input.title, input.releaseDate, anArtist.getArtistId());

        albumRepository.create(anAlbum);

        anArtist.addAlbum(anAlbum.getAlbumId());

        artistRepository.update(anArtist);

        songs.parallelStream().forEach(song -> {
            song.assignToAlbum(anAlbum.getAlbumId());

            songRepository.update(song);
        });

        return new Output(anAlbum.getAlbumId().value(), anAlbum.getTitle(), anAlbum.getReleaseDate(),
                anArtist.getArtistId().value(),
                songs.parallelStream().map(s -> s.getSongId().value()).collect(Collectors.toSet()));
    }

    public record Input(String title, LocalDate releaseDate, String artistId, Set<String> songs) {
    }

    public record Output(String id, String title, LocalDate releaseDate, String artistId, Set<String> songs) {
    }
}
