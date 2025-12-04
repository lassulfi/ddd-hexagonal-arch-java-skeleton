package com.github.lassulfi.musicmanager.application.usecase.artist;

import java.time.LocalDate;
import java.util.Objects;

import com.github.lassulfi.musicmanager.application.usecase.UseCase;
import com.github.lassulfi.musicmanager.domain.artist.model.Artist;
import com.github.lassulfi.musicmanager.domain.artist.repository.ArtistRepository;
import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;
import com.github.lassulfi.musicmanager.domain.valueobjects.Name;

public class CreateArtistUseCase extends UseCase<CreateArtistUseCase.Input, CreateArtistUseCase.Output> {
    
    private final ArtistRepository artistRepository;

    public CreateArtistUseCase(ArtistRepository artistRepository) {
        this.artistRepository = Objects.requireNonNull(artistRepository);
    }

    @Override
    public Output execute(final Input input) {
        if (artistRepository.artistOfName(new Name(input.birthName, input.stageName)).isPresent()) {
            throw new ValidationException("Artist already exists");
        }

        final var artist = artistRepository.create(Artist.newArtist(input.birthName, input.stageName, input.country, input.state, input.city, input.nationality, input.birthDate, input.dateOfDeath));

        return new Output(artist.getArtistId().value(), artist.getName().birthName(), artist.getName().stageName(), artist.getLocation().country(), artist.getLocation().state(), artist.getLocation().city(), artist.getNationality().nationality(), artist.getLifetime().birthDate(), artist.getLifetime().dateOfDeath());    }

    public record Input(String birthName, String stageName, String country,
            String state, String city, String nationality, LocalDate birthDate,
            LocalDate dateOfDeath) {
    }

    public record Output(String id, String birthName, String stageName, String country,
            String state, String city, String nationality, LocalDate birthDate,
            LocalDate dateOfDeath) {
    }
}
