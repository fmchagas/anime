package br.com.fmchagas.anime.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.fmchagas.anime.repository.AnimeUserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimeUserService implements UserDetailsService{
	private final AnimeUserRepository animeUserRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username){
		return Optional.ofNullable(animeUserRepository.findByUserName(username))
				.orElseThrow(()-> new UsernameNotFoundException("Anime User not found"));
	}

}
