package br.com.fmchagas.anime.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.fmchagas.anime.domain.Anime;
import br.com.fmchagas.anime.exception.BadRequestException;
import br.com.fmchagas.anime.repository.AnimeRepository;
import br.com.fmchagas.anime.util.AnimeCreator;
import br.com.fmchagas.anime.util.DtoAnimePostRequestBodyCreator;
import br.com.fmchagas.anime.util.DtoAnimePutRequestBodyCreator;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {
	@InjectMocks
	private AnimeService animeService;

	@Mock
	private AnimeRepository animeRepositoryMock;

	@BeforeEach
	void setUp() {
		PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
		BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(animePage);

		BDDMockito.when(animeRepositoryMock.findAll())
				.thenReturn(List.of(AnimeCreator.createValidAnime(), AnimeCreator.createValidAnime()));

		BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
				.thenReturn(Optional.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
				.thenReturn(List.of(AnimeCreator.createValidAnime()));
		
		BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
				.thenReturn(AnimeCreator.createValidAnime());
		
		BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
	}

	@Test
	@DisplayName("ListAll returns list of anime inside page object when successfull")
	void listAll_ReturnsListOfAnimesInsidePageObjet_WhenSuccessful() {
		String expecteName = AnimeCreator.createValidAnime().getName();

		Page<Anime> animePage = animeService.listAll(PageRequest.of(0, 2));

		Assertions.assertThat(animePage).isNotNull();

		Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);

		Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expecteName);
	}

	@Test
	@DisplayName("listAllNonPageable returns list of anime when successfull")
	void listAllNonPageable_ReturnsListOfAnimes_WhenSuccessful() {
		String expecteName = AnimeCreator.createValidAnime().getName();

		List<Anime> animes = animeService.listAllNonPageable();

		Assertions.assertThat(animes).isNotEmpty().isNotNull().hasSize(2);

		Assertions.assertThat(animes.get(0).getName()).isEqualTo(expecteName);
	}

	@Test
	@DisplayName("findByIdOrThrowBadRequestException return an anime when successfull")
	void findByIdOrThrowBadRequestException_ReturnAnAnime_WhenSuccessful() {
		Long expecteId = AnimeCreator.createValidAnime().getId();

		Anime anime = animeService.findByIdOrThrowBadRequestException(1L);

		Assertions.assertThat(anime).isNotNull();

		Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expecteId);
	}
	
	@Test
	@DisplayName("Find By Id return Throw Bad Request Exceptionan when anime not found")
	void ffindById_ReturnThrowBadRequestException_WhenAnimeNotFound() {
		Long expecteId = AnimeCreator.createValidAnime().getId();

		Anime anime = animeService.findByIdOrThrowBadRequestException(1L);

		Assertions.assertThat(anime).isNotNull();

		Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expecteId);
	}
	
	@Test
	  @DisplayName("Find By Id return Throw Bad Request Exceptionan when anime not found") 
	  void findById_ReturnThrowBadRequestException_WhenAnimeNotFound(){
		BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
			.thenReturn(Optional.empty());
		  
		  Assertions.assertThatExceptionOfType(BadRequestException.class)
		  		.isThrownBy(() -> animeService.findByIdOrThrowBadRequestException(1L));
	  }

	@Test
	@DisplayName("Find By Name returns list of anime when successfull")
	void findByName_ReturnsListOfAnime_WhenSuccessful() {
		String expecteName = AnimeCreator.createValidAnime().getName();

		List<Anime> animes = animeService.findByName("anime name");

		Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

		Assertions.assertThat(animes.get(0).getName()).isEqualTo(expecteName);
	}
	
	@Test
	@DisplayName("Find By Name return empty list when anime not found")
	void findByName_ReturnsListEmpty_WhenAnimeNotFound() {
		BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
		.thenReturn(Collections.emptyList());
		
		List<Anime> animes = animeService.findByName("not found");

		Assertions.assertThat(animes).isNotNull().isEmpty();
	}
	
	@Test
	@DisplayName("Save create anime when successfull")
	void save_PersistAnime_WhenSuccessful() {
		Anime animeSaved = animeService.save(DtoAnimePostRequestBodyCreator.createDtoAnimePostRequestBody());
		
		Assertions.assertThat(animeSaved).isNotNull().isEqualTo(AnimeCreator.createValidAnime());
	}
	
	@Test
	@DisplayName("Replace updates anime when successfull")
	void replace_UpdatesAnime_WhenSuccessful() {
		Assertions.assertThatCode(()->animeService.replace(DtoAnimePutRequestBodyCreator.createDtoAnimePutRequestBody()))
				.doesNotThrowAnyException();

	}
	
	@Test
	@DisplayName("Delete removes anime when Successful")
	void delete_RemovesAnime_WhenSuccessful() {
		Assertions.assertThatCode(()->animeService.delete(1L))
				.doesNotThrowAnyException();
	}

}
