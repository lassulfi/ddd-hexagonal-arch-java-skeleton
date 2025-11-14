package com.github.lassulfi.musicmanager.domain.artist.model;

import java.util.UUID;

import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;

public record ArtistId(String value) {

    public ArtistId {
        if (value == null) {
          throw new ValidationException("Invalid value for ArtistId");  
        }
    }

    public static ArtistId unique() {
        return new ArtistId(UUID.randomUUID().toString());
    }

    public static ArtistId with(final String aValue) {
        try {
            return new ArtistId(UUID.fromString(aValue).toString());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid value for ArtistId");
        }
    }
}
