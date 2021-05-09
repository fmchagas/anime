package br.com.fmchagas.anime.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.fmchagas.anime.domain.Anime;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SpringClient {
	public static void main(String[] args) {
		//ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 3);
		//log.info(entity);
		
		
		//Anime[] animes = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);
		//log.info(Arrays.toString(animes));
		
		ResponseEntity<List<Anime>> animesList = new RestTemplate().exchange("http://localhost:8080/animes/all", 
				HttpMethod.GET, 
				null,
				new ParameterizedTypeReference<>() {});
		log.info(animesList.getBody());
	}
}
