package com.github.lassulfi.musicmanager.domain.song.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.github.lassulfi.musicmanager.domain.artist.model.ArtistId;
import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;

public class Song {

    private final SongId songId;

    private String title;

    private String lyrics;

    private ArtistId artistId;

    private LocalDate releaseDate;

    private Set<ArtistId> collaborators;

    public Song(SongId songId, String title, LocalDate releaseDate, String lyrics, ArtistId artistId, Set<ArtistId> collaborators) {
        if (songId == null) {
            throw new ValidationException("Invalid SongId");
        }
        this.songId = songId;

        this.setTitle(title);
        this.setLyrics(lyrics);
        this.setArtistId(artistId);
        this.setReleaseDate(releaseDate);
        this.collaborators = collaborators != null ? collaborators : new HashSet<>(2);
    }

    public static Song newSong(String title, String lyrics, LocalDate releaseDate, ArtistId artistId) {
        return new Song(SongId.unique(), title, releaseDate, lyrics, artistId, null);
    }

    public SongId getSongId() {
        return songId;
    }

    public String getTitle() {
        return title;
    }

    public String getLyrics() {
        return lyrics;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public ArtistId getArtistId() {
        return artistId;
    }

    public Set<ArtistId> allCollaborators() {
        return Collections.unmodifiableSet(this.collaborators);
    }

    private void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new ValidationException("Invalid title");
        }
        this.title = title;
    }

    private void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    private void setArtistId(ArtistId artistId) {
        if (artistId == null) {
            throw new ValidationException("Invalid ArtistId");
        }
        this.artistId = artistId;
    }

    private void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void addCollaborator(ArtistId collaboratorId) {
        if (collaboratorId == null) {
            throw new ValidationException("Invalid collaborator ArtistId");
        }

        if (this.artistId == collaboratorId) {
            throw new ValidationException("Artist cannot be the collaborator for the song");
        }

        this.allCollaborators().stream().filter(c -> c.equals(collaboratorId)).findFirst().ifPresent(c -> {
            throw new ValidationException("Collaborator already added");
        });

        this.collaborators.add(collaboratorId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((songId == null) ? 0 : songId.hashCode());
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
        Song other = (Song) obj;
        if (songId == null) {
            if (other.songId != null)
                return false;
        } else if (!songId.equals(other.songId))
            return false;
        return true;
    }
}
