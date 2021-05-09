package br.com.fmchagas.anime.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import br.com.fmchagas.anime.domain.Anime;
import br.com.fmchagas.anime.domain.AnimeUser;
import br.com.fmchagas.anime.repository.AnimeRepository;
import br.com.fmchagas.anime.repository.AnimeUserRepository;
import br.com.fmchagas.anime.requests.DtoAnimePostRequestBody;
import br.com.fmchagas.anime.util.AnimeCreator;
import br.com.fmchagas.anime.util.DtoAnimePostRequestBodyCreator;
import br.com.fmchagas.anime.wrapper.PageableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {
	@Autowired 
	@Qualifier(value = "testRestTemplateRoleUser")
	private TestRestTemplate testRestTemplateRoleUser;
	
	@Autowired 
	@Qualifier(value = "testRestTemplateRoleAdmin")
	private TestRestTemplate testRestTemplateRoleAdmin;
	
	@Autowired AnimeRepository animeRepository;
	@Autowired AnimeUserRepository animeUserRepository;
	private static final AnimeUser USER =  AnimeUser.builder()
			.name("Fernando")
			.userName("fmchagas")
			.password("{bcrypt}$2a$10$Y4pqFmBMbSmNBt2webGJleCmSB828oL2vjKPX5uZpypuvGSCqXcMu")
			.authorities("ROLE_USER")
			.build();
	private static final AnimeUser ADMIN =  AnimeUser.builder()
			.name("Administrator")
			.userName("root")
			.password("{bcrypt}$2a$10$Y4pqFmBMbSmNBt2webGJleCmSB828oL2vjKPX5uZpypuvGSCqXcMu")
			.authorities("ROLE_ADMIN,ROLE_USER")
			.build();

	@TestConfiguration
	@Lazy
	static class Config{
		@Bean(name="testRestTemplateRoleUser")
		public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
			RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
					.rootUri("http://localhost:" + port)
					.basicAuthentication("fmchagas", "root");
			return new TestRestTemplate(restTemplateBuilder);
		}
		
		@Bean(name="testRestTemplateRoleAdmin")
		public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
			RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
					.rootUri("http://localhost:" + port)
					.basicAuthentication("root", "root");
			return new TestRestTemplate(restTemplateBuilder);
		}
	}
	
	@Test
	@DisplayName("List returns list of anime inside page object when successfull")
	void list_ReturnsListOfAnimesInsidePageObjet_WhenSuccessful() {
		animeUserRepository.save(USER);		
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		String expecteName = savedAnime.getName();

		PageableResponse<Anime> animePage = testRestTemplateRoleUser.exchange("/animes", HttpMethod.GET, null,
				new ParameterizedTypeReference<PageableResponse<Anime>>() {
				}).getBody();

		Assertions.assertThat(animePage).isNotNull();

		Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);

		Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expecteName);
	}
	
	@Test
	@DisplayName("List All returns list of anime when successfull")
	void listAll_ReturnsListOfAnimes_WhenSuccessful() {
		animeUserRepository.save(USER);
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		String expecteName = savedAnime.getName();

		List<Anime> animes = testRestTemplateRoleUser.exchange("/animes/all", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Anime>>() {
				}).getBody();

		Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

		Assertions.assertThat(animes.get(0).getName()).isEqualTo(expecteName);
	}

	@Test
	@DisplayName("Find By Id return an anime when successfull")
	void findById_ReturnAnAnime_WhenSuccessful() {
		animeUserRepository.save(USER);
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		Long expecteId = savedAnime.getId();

		Anime anime = testRestTemplateRoleUser.getForObject("/animes/{id}", Anime.class, expecteId);

		Assertions.assertThat(anime).isNotNull();

		Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expecteId);
	}
	 

	@Test
	@DisplayName("Find By Name returns list of anime when successfull")
	void findByName_ReturnsListOfAnime_WhenSuccessful() {
		animeUserRepository.save(USER);
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		String expecteName = savedAnime.getName();
		String url = String.format("/animes/find?name=%s", expecteName);
		
		List<Anime> animes = testRestTemplateRoleUser.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Anime>>() {
				}).getBody();

		Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

		Assertions.assertThat(animes.get(0).getName()).isEqualTo(expecteName);
	}
	
	@Test
	@DisplayName("Find By Name return empty list when anime not found")
	void findByName_ReturnsListEmpty_WhenAnimeNotFound() {
		animeUserRepository.save(USER);
		List<Anime> animes = testRestTemplateRoleUser.exchange("/animes/find?name=nanatsu", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Anime>>() {
				}).getBody();

		Assertions.assertThat(animes).isNotNull().isEmpty();
	}
	
	@Test
	@DisplayName("Save create anime when successfull")
	void save_PersistAnime_WhenSuccessful() {
		animeUserRepository.save(USER);
		DtoAnimePostRequestBody createDtoAnimePostRequestBody = DtoAnimePostRequestBodyCreator.createDtoAnimePostRequestBody();
		
		ResponseEntity<Anime> animeSaved = testRestTemplateRoleUser.postForEntity("/animes", createDtoAnimePostRequestBody, Anime.class);
		
		Assertions.assertThat(animeSaved).isNotNull();
		Assertions.assertThat(animeSaved.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(animeSaved.getBody()).isNotNull();
		Assertions.assertThat(animeSaved.getBody().getId()).isNotNull();
	}
	
	@Test
	@DisplayName("Replace updates anime when successfull")
	void replace_UpdatesAnime_WhenSuccessful() {
		animeUserRepository.save(USER);
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		savedAnime.setName("Naruto");
		
		ResponseEntity<Void> animeReplaced = testRestTemplateRoleUser.exchange("/animes", HttpMethod.PUT,
				new HttpEntity<>(savedAnime), Void.class);

		Assertions.assertThat(animeReplaced).isNotNull();
		
		Assertions.assertThat(animeReplaced.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("Delete return 403 when user not is admin")
	void delete_Returns403Anime_WhenUserNotIsAdmn() {
		animeUserRepository.save(USER);
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		ResponseEntity<Void> animeReplaced = testRestTemplateRoleUser.exchange("/animes/admin/{id}", HttpMethod.DELETE,
				null, Void.class, savedAnime.getId());

		Assertions.assertThat(animeReplaced).isNotNull();
		
		Assertions.assertThat(animeReplaced.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
	
	@Test
	@DisplayName("Delete removes anime when Successful")
	void delete_RemovesAnime_WhenSuccessful() {
		animeUserRepository.save(ADMIN);
		Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
		
		ResponseEntity<Void> animeReplaced = testRestTemplateRoleAdmin.exchange("/animes/admin/{id}", HttpMethod.DELETE,
				null, Void.class, savedAnime.getId());

		Assertions.assertThat(animeReplaced).isNotNull();
		
		Assertions.assertThat(animeReplaced.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
}
