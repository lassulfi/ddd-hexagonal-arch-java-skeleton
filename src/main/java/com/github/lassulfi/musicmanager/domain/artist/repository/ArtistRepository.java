package com.github.lassulfi.musicmanager.domain.artist.repository;

import java.util.Optional;

import com.github.lassulfi.musicmanager.domain.artist.model.Artist;
import com.github.lassulfi.musicmanager.domain.artist.model.ArtistId;
import com.github.lassulfi.musicmanager.domain.valueobjects.Name;

public interface ArtistRepository {
    Optional<Artist> artistOfId(ArtistId anId);

    Optional<Artist> artistOfName(Name aName);

    Artist create(Artist anArtist);

    Artist update(Artist anArtist);

    void deleteAll();
}
