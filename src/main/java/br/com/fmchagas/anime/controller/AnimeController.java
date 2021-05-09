package br.com.fmchagas.anime.controller;

import java.util.List;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fmchagas.anime.domain.Anime;
import br.com.fmchagas.anime.requests.DtoAnimePostRequestBody;
import br.com.fmchagas.anime.requests.DtoAnimePutRequestBody;
import br.com.fmchagas.anime.service.AnimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("animes")
@RequiredArgsConstructor
public class AnimeController {
	private final AnimeService animeService;
		
	@GetMapping
	@Operation(summary = "Lista todos os animes paginado", description = "O padrão são 2 registros, use o parametro size para alterar o valor padrão",
	tags = {"anime"})
	public ResponseEntity<Page<Anime>> list(@ParameterObject Pageable pageable){
		return ResponseEntity.ok(animeService.listAll(pageable));
	}
	
	@GetMapping(path = "/all")
	@Operation(summary = "Lista todos os animes", tags = {"anime"})
	public ResponseEntity<List<Anime>> all(){
		return ResponseEntity.ok(animeService.listAllNonPageable());
	}
	
	
	@GetMapping(path = "/find")
	public ResponseEntity<List<Anime>> findByName(@RequestParam(required = false) String name){
		return ResponseEntity.ok(animeService.findByName(name));
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<Anime> findById(@PathVariable Long id){
		return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
	}
	
	@PostMapping
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Anime> save(@RequestBody @Validated DtoAnimePostRequestBody dtoAnimePostRequestBody){
		return new ResponseEntity<>(animeService.save(dtoAnimePostRequestBody), HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<Void> replace(@RequestBody DtoAnimePutRequestBody dtoAnimePutRequestBody){
		animeService.replace(dtoAnimePutRequestBody);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping(path = "/admin/{id}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Suceso na operação"),
			@ApiResponse(responseCode = "400", description = "Quando Anime não existe na base de dados")
	})
	public ResponseEntity<Void> delete(@PathVariable Long id){
		animeService.delete(id);
		return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
