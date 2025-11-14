package com.github.lassulfi.musicmanager.domain.album.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.github.lassulfi.musicmanager.domain.artist.model.ArtistId;
import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;
import com.github.lassulfi.musicmanager.domain.song.model.Song;

public class Album {

    private final AlbumId albumId;

    private String title;

    private LocalDate releaseDate;

    private ArtistId artistId;

    private Set<Song> songs;

    public Album(AlbumId albumId, String title, LocalDate releaseDate, ArtistId artistId, Set<Song> songs) {
        if (albumId == null) {
            throw new ValidationException("Invalid AlbumId");
        }
        this.albumId = albumId;
        this.setTitle(title);
        this.setReleaseDate(releaseDate);
        this.setArtistId(artistId);
        this.songs = songs != null ? songs : new HashSet<>(10);
    }

    public static Album newAlbum(String title, LocalDate releaseDate, ArtistId artistId) {
        return new Album(AlbumId.unique(), title, releaseDate, artistId, null);
    }

    public AlbumId getAlbumId() {
        return albumId;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public ArtistId getArtistId() {
        return artistId;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    private void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    private void setArtistId(ArtistId artistId) {
        if (artistId == null) {
            throw new ValidationException("Invalid ArtistId");
        }
        this.artistId = artistId;
    }

    public Set<Song> allSongs() {
        return Collections.unmodifiableSet(this.songs);
    }

    public void addSong(String title, String lyrics, LocalDate releaseDate, Set<ArtistId> collaborators) {
        final var aSong = Song.newSong(title, lyrics, releaseDate, this.artistId);

        for (var collaborator : collaborators) {
            aSong.addCollaborator(collaborator);
        }

        this.songs.add(aSong);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((albumId == null) ? 0 : albumId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Album other = (Album) obj;
        if (albumId == null) {
            if (other.albumId != null)
                return false;
        } else if (!albumId.equals(other.albumId))
            return false;
        return true;
    }   
}
