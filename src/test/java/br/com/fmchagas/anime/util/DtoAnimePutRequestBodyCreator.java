package br.com.fmchagas.anime.util;

import br.com.fmchagas.anime.requests.DtoAnimePutRequestBody;

public class DtoAnimePutRequestBodyCreator {
	public static DtoAnimePutRequestBody createDtoAnimePutRequestBody() {
		var dtoAnimePutRequestBody = new DtoAnimePutRequestBody();
		dtoAnimePutRequestBody.setId(AnimeCreator.createValidUpdatedAnime().getId());
		dtoAnimePutRequestBody.setName(AnimeCreator.createValidUpdatedAnime().getName());
		return dtoAnimePutRequestBody;
	}
}
