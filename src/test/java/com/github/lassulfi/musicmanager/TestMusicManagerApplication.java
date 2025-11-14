package com.github.lassulfi.musicmanager;

import org.springframework.boot.SpringApplication;

public class TestMusicManagerApplication {

	public static void main(String[] args) {
		SpringApplication.from(MusicManagerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
