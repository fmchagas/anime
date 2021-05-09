package br.com.fmchagas.anime.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.fmchagas.anime.domain.Anime;
import br.com.fmchagas.anime.requests.DtoAnimePostRequestBody;
import br.com.fmchagas.anime.requests.DtoAnimePutRequestBody;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {
	public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);
	
	public abstract Anime toAnime(DtoAnimePostRequestBody dtoAnimePostRequestBody);
	
	public abstract Anime toAnime(DtoAnimePutRequestBody dtoAnimePutRequestBody);
}
