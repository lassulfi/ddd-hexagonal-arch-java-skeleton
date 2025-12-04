package com.github.lassulfi.musicmanager.domain.album.model;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.lassulfi.musicmanager.domain.artist.model.ArtistId;
import com.github.lassulfi.musicmanager.domain.exceptions.ValidationException;
import com.github.lassulfi.musicmanager.domain.song.model.SongId;

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

		final var expectedSong = SongId.unique();

		// when
		actualAlbum.addSong(expectedSong);

		// then
		Assertions.assertEquals(1, actualAlbum.allSongs().size());
	}

	@Test
	@DisplayName("Should not add duplicated songs to the album")
	void testAddSongWithDuplicatedSong() {
		// given
		final var expectedArtistId = ArtistId.unique();
		final var expectedTitle = "t";
		final var actualAlbum = Album.newAlbum(expectedTitle, LocalDate.now(), expectedArtistId);
		
		final var expectedSong = SongId.unique();
		actualAlbum.addSong(expectedSong);
		
		final var expectedMessage = String.format("Song of id %s already added to the album", expectedSong.toString());

		// when
		final var actualException = Assertions.assertThrows(ValidationException.class, () -> actualAlbum.addSong(expectedSong));

		// then
		Assertions.assertEquals(expectedMessage, actualException.getMessage());
	}

}
