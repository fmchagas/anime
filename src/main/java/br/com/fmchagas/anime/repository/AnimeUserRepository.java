package br.com.fmchagas.anime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fmchagas.anime.domain.AnimeUser;

public interface AnimeUserRepository extends JpaRepository<AnimeUser, Long>{
	AnimeUser findByUserName(String userName);
}
