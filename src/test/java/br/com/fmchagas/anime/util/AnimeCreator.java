package br.com.fmchagas.anime.util;

import br.com.fmchagas.anime.domain.Anime;

public class AnimeCreator {
	
	public static Anime createAnimeToBeSaved() {
		return Anime.builder()
				.name("FMA")
				.build();
	}
	
	public static Anime createValidAnime() {
		return Anime.builder()
				.name("FMA")
				.id(1L)
				.build();
	}
	
	public static Anime createValidUpdatedAnime() {
		return Anime.builder()
				.name("Full Metal Alchimist")
				.id(1L)
				.build();
	}
}
