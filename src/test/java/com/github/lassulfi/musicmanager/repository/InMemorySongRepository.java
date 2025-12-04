package com.github.lassulfi.musicmanager.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.github.lassulfi.musicmanager.domain.song.model.Song;
import com.github.lassulfi.musicmanager.domain.song.model.SongId;
import com.github.lassulfi.musicmanager.domain.song.repository.SongRepository;

public class InMemorySongRepository implements SongRepository {

    private final Map<String, Song> songs;

    public InMemorySongRepository() {
        this.songs = new HashMap<>();
    }

    @Override
    public Optional<Song> songOfId(SongId anId) {
        return Optional.ofNullable(this.songs.get(Objects.requireNonNull(anId).value().toString()));
    }

    @Override
    public Song create(Song aSong) {
        this.songs.put(aSong.getSongId().value().toString(), aSong);
        return aSong;
    }

    @Override
    public Song update(Song aSong) {
        this.songs.put(aSong.getSongId().value().toString(), aSong);
        return aSong;
    }

    @Override
    public void deleteAll() {
        this.songs.clear();
    }

}
