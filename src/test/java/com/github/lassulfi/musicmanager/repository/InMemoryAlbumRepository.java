package com.github.lassulfi.musicmanager.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.github.lassulfi.musicmanager.domain.album.model.Album;
import com.github.lassulfi.musicmanager.domain.album.model.AlbumId;
import com.github.lassulfi.musicmanager.domain.album.repository.AlbumRepository;

public class InMemoryAlbumRepository implements AlbumRepository {

    private final Map<String, Album> albums;

    public InMemoryAlbumRepository() {
        this.albums = new HashMap<>();
    }

    @Override
    public Optional<Album> albumOfId(AlbumId anId) {
        return Optional.ofNullable(this.albums.get(Objects.requireNonNull(anId).value().toString()));
    }

    @Override
    public Album create(Album anAlbum) {
        this.albums.put(anAlbum.getAlbumId().value().toString(), anAlbum);
        return anAlbum;
    }

    @Override
    public Album update(Album anAlbum) {
        this.albums.put(anAlbum.getAlbumId().value().toString(), anAlbum);
        return anAlbum;
    }

    @Override
    public void deleteAll() {
        this.albums.clear();
    }

}
