package com.github.lassulfi.musicmanager.domain.song.model;

import java.util.UUID;

import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;

public record SongId(String value) {

    public SongId {
        if (value == null) {
          throw new ValidationException("Invalid value for SongId");  
        }
    }

    public static SongId unique() {
        return new SongId(UUID.randomUUID().toString());
    }

    public static SongId with(final String aValue) {
        try {
            return new SongId(UUID.fromString(aValue).toString());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid value for SongId");
        }
    }
}
