package com.github.lassulfi.musicmanager.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.github.lassulfi.musicmanager.domain.artist.model.Artist;
import com.github.lassulfi.musicmanager.domain.artist.model.ArtistId;
import com.github.lassulfi.musicmanager.domain.artist.repository.ArtistRepository;
import com.github.lassulfi.musicmanager.domain.valueobjects.Name;

public class InMemoryArtistRepository implements ArtistRepository {

    private final Map<String, Artist> artists;
    private final Map<Name, Artist> artistsByName;

    public InMemoryArtistRepository() {
        this.artists = new HashMap<>();
        this.artistsByName = new HashMap<>();
    }

    @Override
    public Optional<Artist> artistOfId(ArtistId anId) {
        return Optional.ofNullable(this.artists.get(Objects.requireNonNull(anId).value().toString()));
    }

    @Override
    public Optional<Artist> artistOfName(Name aName) {
        return Optional.ofNullable(this.artistsByName.get(Objects.requireNonNull(aName)));
    }

    @Override
    public Artist create(Artist anArtist) {
        this.artists.put(anArtist.getArtistId().value().toString(), anArtist);
        this.artistsByName.put(anArtist.getName(), anArtist);

        return anArtist;
    }

    @Override
    public Artist update(Artist anArtist) {
        this.artists.put(anArtist.getArtistId().value().toString(), anArtist);
        this.artistsByName.put(anArtist.getName(), anArtist);

        return anArtist;
    }

    @Override
    public void deleteAll() {
        this.artists.clear();
        this.artistsByName.clear();
    }

}
