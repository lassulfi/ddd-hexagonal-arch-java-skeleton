package com.github.lassulfi.musicmanager.domain.song.repository;

import java.util.Optional;

import com.github.lassulfi.musicmanager.domain.song.model.Song;
import com.github.lassulfi.musicmanager.domain.song.model.SongId;

public interface SongRepository {
    Optional<Song> songOfId(SongId anId);

    Song create(Song aSong);

    Song update(Song aSong);

    void deleteAll();
}
