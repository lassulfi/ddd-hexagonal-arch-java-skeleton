package com.github.lassulfi.musicmanager.domain.artist.repository;

import java.util.Optional;

import com.github.lassulfi.musicmanager.domain.artist.model.Artist;
import com.github.lassulfi.musicmanager.domain.artist.model.ArtistId;

public interface ArtistRepository {
    Optional<Artist> artistOfId(ArtistId anId);

    Artist create(Artist anArtist);

    Artist update(Artist anArtist);

    void deleteAll();
}
