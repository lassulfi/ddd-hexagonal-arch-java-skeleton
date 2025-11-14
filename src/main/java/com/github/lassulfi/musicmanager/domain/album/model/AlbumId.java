package com.github.lassulfi.musicmanager.domain.album.model;

import java.util.UUID;

import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;

public record AlbumId(String value) {

    public AlbumId {
        if (value == null) {
          throw new ValidationException("Invalid value for AlbumId");  
        }
    }

    public static AlbumId unique() {
        return new AlbumId(UUID.randomUUID().toString());
    }

    public static AlbumId with(final String aValue) {
        try {
            return new AlbumId(UUID.fromString(aValue).toString());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid value for AlbumId");
        }
    }
}
