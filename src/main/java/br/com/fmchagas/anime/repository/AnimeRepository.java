package br.com.fmchagas.anime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fmchagas.anime.domain.Anime;

public interface AnimeRepository extends JpaRepository<Anime, Long>{
	List<Anime> findByName(String name);
}
