package com.github.lassulfi.musicmanager.application.usecase.song;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.lassulfi.musicmanager.application.usecase.UseCase;
import com.github.lassulfi.musicmanager.domain.artist.model.Artist;
import com.github.lassulfi.musicmanager.domain.artist.model.ArtistId;
import com.github.lassulfi.musicmanager.domain.artist.repository.ArtistRepository;
import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;
import com.github.lassulfi.musicmanager.domain.song.model.Song;
import com.github.lassulfi.musicmanager.domain.song.repository.SongRepository;

public class CreateSongUseCase extends UseCase<CreateSongUseCase.Input, CreateSongUseCase.Output> {

    private final ArtistRepository artistRepository;

    private final SongRepository songRepository;

    public CreateSongUseCase(final ArtistRepository artistRepository, final SongRepository songRepository) {
        this.artistRepository = Objects.requireNonNull(artistRepository);
        this.songRepository = Objects.requireNonNull(songRepository);
    }

    @Override
    public Output execute(final Input input) {
        var anArtist = artistRepository.artistOfId(ArtistId.with(input.artistId))
                .orElseThrow(() -> new ValidationException("Artist not found"));

        var collaborators = Optional.ofNullable(input.collaborators).map(values -> values.parallelStream()
                .map(c -> artistRepository.artistOfId(ArtistId.with(c)).orElseThrow(
                        () -> new ValidationException(String.format("Collaborator of id %s not found", c))))
                .map(Artist::getArtistId)
                .collect(Collectors.toSet())).orElse(Collections.emptySet());

        var aSong = Song.newSong(input.title, input.lyrics, input.releaseDate, anArtist.getArtistId());
        for (var collaboratorId : collaborators) {
            aSong.addCollaborator(collaboratorId);
        }

        songRepository.create(aSong);

        anArtist.addSong(aSong.getSongId());

        artistRepository.update(anArtist);

        return new Output(aSong.getSongId().value(), aSong.getTitle(), aSong.getLyrics(), aSong.getReleaseDate(),
                aSong.getArtistId().value(), collaborators.stream().map(c -> c.value()).collect(Collectors.toSet()));
    }

    public record Input(String title, String lyrics, LocalDate releaseDate, String artistId,
            Set<String> collaborators) {
    }

    public record Output(String id, String title, String lyrics, LocalDate releaseDate, String artistId,
            Set<String> collaborators) {
    }
}
