package com.github.lassulfi.musicmanager.domain.album.model;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.lassulfi.musicmanager.domain.artist.model.ArtistId;
import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;

public class AlbumTest {

	@Test
	@DisplayName("Should instantiate a new album")
	void testCreateNewAlbum() {
		// given
		final var expectedArtistId = ArtistId.unique();
		final var expectedTitle = "Good Album";
		final var expectedDate = LocalDate.of(2020, 1, 1);
		
		// when
		final var actualAlbum = Album.newAlbum(expectedTitle, expectedDate, expectedArtistId);
		
		// then
		Assertions.assertNotNull(actualAlbum.getAlbumId());
		Assertions.assertEquals(expectedTitle, actualAlbum.getTitle());
		Assertions.assertEquals(expectedDate, actualAlbum.getReleaseDate());
		Assertions.assertEquals(expectedArtistId, actualAlbum.getArtistId());
	}
	
	@Test
	@DisplayName("Should not instantiate a new album given an invalid albumId")
	void testCreateAlbumWithInvalidAlbumId() {
		// given
		final var expectedDate = LocalDate.now();
		final var expectedTitle = "Good Album";
		final var expectedMessage = "Invalid ArtistId";

		// when
		final var actualException = Assertions.assertThrows(ValidationException.class,
				() -> Album.newAlbum(expectedTitle, expectedDate, null));

		//then
		Assertions.assertEquals(expectedMessage, actualException.getMessage());
	}

	@Test
	@DisplayName("Should add songs to the album")
	void testAddSongAddsSong() {
		// given
		final var expectedArtistId = ArtistId.unique();
		final var expectedTitle = "t";
		final var actualAlbum = Album.newAlbum(expectedTitle, LocalDate.now(), expectedArtistId);

		final var expectedCollaboratorId = ArtistId.unique();
		final var expectedCollaborators = Collections.singleton(expectedCollaboratorId);
		
		final var expectedSong = "song 1";

		// when
		actualAlbum.addSong(expectedSong, null, LocalDate.now(), expectedCollaborators);

		// then
		Assertions.assertEquals(1, actualAlbum.allSongs().size());
	}

}
