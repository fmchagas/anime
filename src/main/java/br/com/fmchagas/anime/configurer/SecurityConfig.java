package br.com.fmchagas.anime.configurer;

import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.fmchagas.anime.service.AnimeUserService;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Profile("!nosecurity")
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	private final AnimeUserService animeUserService;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		//.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
		.authorizeRequests()
		.antMatchers("/animes/admin/**").hasRole("ADMIN")
		.antMatchers("/animes/**").hasRole("USER")
		.antMatchers("/actuator/**").permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.formLogin()
		.and()
		.httpBasic()
		;

	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

		/*auth.inMemoryAuthentication()
				.withUser("root2")
				.password(passwordEncoder.encode("root"))
				.roles("USER","ADMIN")
				.and()
				.withUser("fmchagas2")
				.password(passwordEncoder.encode("root"))
				.roles("USER")
				.and();*/
		
		auth.userDetailsService(animeUserService).passwordEncoder(passwordEncoder);
	}
}
