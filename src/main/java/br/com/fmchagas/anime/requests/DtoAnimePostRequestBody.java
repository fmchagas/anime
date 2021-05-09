package br.com.fmchagas.anime.requests;

import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DtoAnimePostRequestBody {
	@NotEmpty(message = "O nome não pode ser vazio")
	@Schema(description = "Esse é o nome do anime", example = "Dragon Boll", required = true)
	private String name;
}
