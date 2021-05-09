package br.com.fmchagas.anime.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.fmchagas.anime.domain.Anime;
import br.com.fmchagas.anime.exception.BadRequestException;
import br.com.fmchagas.anime.mapper.AnimeMapper;
import br.com.fmchagas.anime.repository.AnimeRepository;
import br.com.fmchagas.anime.requests.DtoAnimePostRequestBody;
import br.com.fmchagas.anime.requests.DtoAnimePutRequestBody;

@Service
public class AnimeService {
	private final AnimeRepository animeRepository;
	
	public AnimeService(AnimeRepository animeRepository) {
		this.animeRepository = animeRepository;
	}
	
	/*private static List<Anime> animes;
	
	static {
		animes = new ArrayList<>(List.of(new Anime(1l, "FMA"), new Anime(2l, "DBz")));
	}*/
	
	public Page<Anime> listAll(Pageable pageable){
		return animeRepository.findAll(pageable);
	}
	
	public List<Anime> listAllNonPageable() {
		return animeRepository.findAll();
	}
	
	public List<Anime> findByName(String name){
		return animeRepository.findByName(name);
	}

	public Anime findByIdOrThrowBadRequestException(Long id) {
		return animeRepository.findById(id)
					.orElseThrow(() -> new BadRequestException("Anime Not Found"));
				
				/*animes.stream()
				.filter(anime -> anime.getId().equals(id))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime Not Found"));*/
	}
	
	@Transactional
	public Anime save(DtoAnimePostRequestBody dtoAnimePostRequestBody) {
		//Ante do mapper
		//Anime anime = Anime.builder().name(dtoAnimePostRequestBody.getName()).build();
		
		Anime anime = AnimeMapper.INSTANCE.toAnime(dtoAnimePostRequestBody);

		return animeRepository.save(anime);
		 
		//anime.setId(ThreadLocalRandom.current().nextLong(3, 10000));
		//animes.add(anime);
	}
	
	public void delete(Long id) {
		animeRepository.delete(findByIdOrThrowBadRequestException(id));
		//animes.remove(findById(id));
	}

	public void replace(DtoAnimePutRequestBody dtoAnimePutRequestBody) {
		Anime savedAnime = findByIdOrThrowBadRequestException(dtoAnimePutRequestBody.getId());
		
		//Ates mapper
		/*Anime anime = Anime.builder()
				.id(saveAnime.getId())
				.name(dtoAnimePutRequestBody.getName())
				.build();*/
		
		Anime anime = AnimeMapper.INSTANCE.toAnime(dtoAnimePutRequestBody);
		anime.setId(savedAnime.getId());
		
		animeRepository.save(anime);
		
		//animes.remove(findById(anime.getId()));
		//animes.add(anime);
	}
}
