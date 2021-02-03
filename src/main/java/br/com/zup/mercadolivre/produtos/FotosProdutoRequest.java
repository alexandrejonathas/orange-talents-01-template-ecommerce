package br.com.zup.mercadolivre.produtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

public class FotosProdutoRequest {

	@NotNull
	@Size(min = 1)
	private MultipartFile[] files;

	@JsonCreator(mode = Mode.PROPERTIES)
	public FotosProdutoRequest(MultipartFile[] files) {
		this.files = files;
	}
	
	public MultipartFile[] getFiles() {
		return files;
	}
	
}
