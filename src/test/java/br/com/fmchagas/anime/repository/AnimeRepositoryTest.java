package br.com.fmchagas.anime.repository;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.fmchagas.anime.domain.Anime;
import br.com.fmchagas.anime.util.AnimeCreator;

@DataJpaTest
@DisplayName("Test for Anime Repository")
class AnimeRepositoryTest {
	@Autowired
	private AnimeRepository animeRepository;
	
	@Test
	@DisplayName("Save creates anime when Successful")
	void save_PersistAnime_WhenSuccessful() {
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		
		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
		
		Assertions.assertThat(animeSaved).isNotNull();
		
		Assertions.assertThat(animeSaved.getId()).isNotNull();
		
		Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
	}
	
	@Test
	@DisplayName("Save updates anime when Successful")
	void save_UpdatesAnime_WhenSuccessful() {
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		
		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
		
		animeSaved.setName("Overload");
		Anime animeUpdated = this.animeRepository.save(animeToBeSaved);
		
		
		Assertions.assertThat(animeUpdated).isNotNull();
		
		Assertions.assertThat(animeUpdated.getId()).isNotNull();
		
		Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeSaved.getName());
	}
	
	@Test
	@DisplayName("Delete removes anime when Successful")
	void delete_RemovesAnime_WhenSuccessful() {
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		
		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
		
		this.animeRepository.delete(animeSaved);
		
		Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());
		
		Assertions.assertThat(animeOptional).isEmpty();
	}
	
	@Test
	@DisplayName("Find By Name returns list of anime when Successful")
	void findByName_ReturnsListOfAnime_WhenSuccessful() {
		Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
		
		Anime animeSaved = this.animeRepository.save(animeToBeSaved);
						
		List<Anime> animes = this.animeRepository.findByName(animeSaved.getName());

		Assertions.assertThat(animes)
					.isNotEmpty()
					.contains(animeSaved);
	}
	
	@Test
	@DisplayName("Find By Name return empty list when anime not found")
	void findByName_ReturnsListEmpty_WhenAnimeNotFound() {
		List<Anime> animes = this.animeRepository.findByName("NotFound");

		Assertions.assertThat(animes).isEmpty();
	}
	
	@Test
	@DisplayName("Save throw ConstraintViolationException when name is empty")
	void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {
		Anime anime = new Anime();
		
		//Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
				//.isInstanceOf(ConstraintViolationException.class);
		
		Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
		.isThrownBy(() -> this.animeRepository.save(anime))
		.withMessageContaining("O nome não pode ser vazio");
	}
	
	
}
