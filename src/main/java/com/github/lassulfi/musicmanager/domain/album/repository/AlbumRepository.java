package com.github.lassulfi.musicmanager.domain.album.repository;

import java.util.Optional;

import com.github.lassulfi.musicmanager.domain.album.model.Album;
import com.github.lassulfi.musicmanager.domain.album.model.AlbumId;

public interface AlbumRepository {
    Optional<Album> albumOfId(AlbumId anId);

    Album create(Album anAlbum);

    Album update(Album anAlbum);

    void deleteAll();
}
