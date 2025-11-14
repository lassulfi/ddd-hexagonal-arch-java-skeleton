package com.github.lassulfi.musicmanager.domain.artist.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.github.lassulfi.musicmanager.domain.album.model.Album;
import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;
import com.github.lassulfi.musicmanager.domain.song.model.Song;
import com.github.lassulfi.musicmanager.domain.valueobjects.Lifetime;
import com.github.lassulfi.musicmanager.domain.valueobjects.Location;
import com.github.lassulfi.musicmanager.domain.valueobjects.Name;
import com.github.lassulfi.musicmanager.domain.valueobjects.Nationality;

public class Artist {

    private final ArtistId artistId;

    private Name name;

    private Location location;

    private Nationality nationality;

    private Lifetime lifetime;

    private Set<Album> albums;

    private Set<Song> songs;

    public Artist(final ArtistId artistId, final String birthName, final String stageName, final String country,
            final String state, final String city, final String nationality, final LocalDate birthDate,
            final LocalDate dateOfDeath, Set<Album> albums, Set<Song> songs) {
        if (artistId == null) {
            throw new ValidationException("Invalid ArtistId");
        }
        this.artistId = artistId;
        this.setName(birthName, stageName);
        this.setLocation(country, city, state);
        this.setNationality(nationality);
        this.setLifetime(birthDate, dateOfDeath);

        this.albums = albums != null ? albums : new HashSet<>(2);
        this.songs = songs != null ? songs : new HashSet<>(2);
    }

    public static Artist newArtist(final String birthName, final String stageName, final String country,
            final String state, final String city, final String nationality, final LocalDate birthDate,
            final LocalDate dateOfDeath) {
        return new Artist(ArtistId.unique(), birthName, stageName, country, state, city, nationality, birthDate,
                dateOfDeath, null, null);
    }

    public ArtistId getArtistId() {
        return artistId;
    }

    public Name getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public Lifetime getLifetime() {
        return lifetime;
    }

    private void setName(final String birthName, final String stageName) {
        this.name = new Name(birthName, stageName);
    }

    private void setLocation(String country, String city, String state) {
        this.location = new Location(country, state, city);
    }

    private void setNationality(final String nationality) {
        this.nationality = new Nationality(nationality);
    }

    private void setLifetime(LocalDate birthDate, LocalDate dateOfDeath) {
        this.lifetime = new Lifetime(birthDate, dateOfDeath);
    }

    public Set<Album> allAlbums() {
        return Collections.unmodifiableSet(this.albums);
    }

    public void addAlbum(String title, LocalDate releaseDate) {
        if (title == null || title.isBlank()) {
            throw new ValidationException("Invalid Album title");
        }

        this.allAlbums().stream().filter(a -> a.getTitle().equals(title) && a.getReleaseDate().equals(releaseDate))
                .findFirst().ifPresent(a -> {
                    throw new ValidationException("Album already released.");
                });

        final var anAlbum = Album.newAlbum(title, releaseDate, this.artistId);

        this.albums.add(anAlbum);
    }

    public Set<Song> allSongs() {
        return Collections.unmodifiableSet(songs);
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
        result = prime * result + ((artistId == null) ? 0 : artistId.hashCode());
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
        Artist other = (Artist) obj;
        if (artistId == null) {
            if (other.artistId != null)
                return false;
        } else if (!artistId.equals(other.artistId))
            return false;
        return true;
    }
}
