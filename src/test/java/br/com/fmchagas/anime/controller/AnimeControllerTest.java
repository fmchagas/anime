package br.com.fmchagas.anime.controller;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.fmchagas.anime.domain.Anime;
import br.com.fmchagas.anime.requests.DtoAnimePostRequestBody;
import br.com.fmchagas.anime.requests.DtoAnimePutRequestBody;
import br.com.fmchagas.anime.service.AnimeService;
import br.com.fmchagas.anime.util.AnimeCreator;
import br.com.fmchagas.anime.util.DtoAnimePostRequestBodyCreator;
import br.com.fmchagas.anime.util.DtoAnimePutRequestBodyCreator;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {
	@InjectMocks
	private AnimeController animeController;

	@Mock
	private AnimeService animeServiceMock;

	@BeforeEach
	void setUp() {
		PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
		BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any())).thenReturn(animePage);

		BDDMockito.when(animeServiceMock.listAllNonPageable())
				.thenReturn(List.of(AnimeCreator.createValidAnime(), AnimeCreator.createValidAnime()));

		BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.any()))
				.thenReturn(AnimeCreator.createValidAnime());
		
		BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
				.thenReturn(List.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(DtoAnimePostRequestBody.class)))
				.thenReturn(AnimeCreator.createValidAnime());
		
		BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(DtoAnimePutRequestBody.class));
		
		BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());
	}

	@Test
	@DisplayName("List returns list of anime inside page object when successfull")
	void list_ReturnsListOfAnimesInsidePageObjet_WhenSuccessful() {
		String expecteName = AnimeCreator.createValidAnime().getName();

		Page<Anime> animePage = animeController.list(null).getBody();

		Assertions.assertThat(animePage).isNotNull();

		Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);

		Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expecteName);
	}

	@Test
	@DisplayName("List All returns list of anime when successfull")
	void listAll_ReturnsListOfAnimes_WhenSuccessful() {
		String expecteName = AnimeCreator.createValidAnime().getName();

		List<Anime> animes = animeController.all().getBody();

		Assertions.assertThat(animes).isNotEmpty().isNotNull().hasSize(2);

		Assertions.assertThat(animes.get(0).getName()).isEqualTo(expecteName);
	}

	@Test
	@DisplayName("Find By Id return an anime when successfull")
	void findById_ReturnAnAnime_WhenSuccessful() {
		Long expecteId = AnimeCreator.createValidAnime().getId();

		Anime anime = animeController.findById(1L).getBody();

		Assertions.assertThat(anime).isNotNull();

		Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expecteId);
	}
	 

	@Test
	@DisplayName("Find By Name returns list of anime when successfull")
	void findByName_ReturnsListOfAnime_WhenSuccessful() {
		String expecteName = AnimeCreator.createValidAnime().getName();

		List<Anime> animes = animeController.findByName("anime name").getBody();

		Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

		Assertions.assertThat(animes.get(0).getName()).isEqualTo(expecteName);
	}
	
	@Test
	@DisplayName("Find By Name return empty list when anime not found")
	void findByName_ReturnsListEmpty_WhenAnimeNotFound() {
		BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
		.thenReturn(Collections.emptyList());
		
		List<Anime> animes = animeController.findByName("not found").getBody();

		Assertions.assertThat(animes).isNotNull().isEmpty();
	}
	
	@Test
	@DisplayName("Save create anime when successfull")
	void save_PersistAnime_WhenSuccessful() {
		Anime animeSaved = animeController.save(DtoAnimePostRequestBodyCreator.createDtoAnimePostRequestBody()).getBody();
		
		Assertions.assertThat(animeSaved).isNotNull().isEqualTo(AnimeCreator.createValidAnime());
	}
	
	@Test
	@DisplayName("Replace updates anime when successfull")
	void replace_UpdatesAnime_WhenSuccessful() {
		Assertions.assertThatCode(()->animeController.replace(DtoAnimePutRequestBodyCreator.createDtoAnimePutRequestBody()))
				.doesNotThrowAnyException();
		
		ResponseEntity<Void> entity = animeController.replace(DtoAnimePutRequestBodyCreator.createDtoAnimePutRequestBody());
		Assertions.assertThat(entity).isNotNull();
		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("Delete removes anime when Successful")
	void delete_RemovesAnime_WhenSuccessful() {
		ResponseEntity<Void> entity = animeController.delete(1L);
		Assertions.assertThat(entity).isNotNull();
		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
		Assertions.assertThatCode(()->animeController.delete(1L)).doesNotThrowAnyException();
	}
}
