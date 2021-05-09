package br.com.fmchagas.anime.util;

import br.com.fmchagas.anime.requests.DtoAnimePostRequestBody;

public class DtoAnimePostRequestBodyCreator {
	public static DtoAnimePostRequestBody createDtoAnimePostRequestBody() {
		var dtoAnimePostRequestBody = new DtoAnimePostRequestBody();
		dtoAnimePostRequestBody.setName(AnimeCreator.createAnimeToBeSaved().getName());
		return dtoAnimePostRequestBody;
	}
}
