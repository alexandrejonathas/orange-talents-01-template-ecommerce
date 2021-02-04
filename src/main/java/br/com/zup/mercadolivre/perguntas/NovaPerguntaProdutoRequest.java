package br.com.zup.mercadolivre.perguntas;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.usuarios.Usuario;

public class NovaPerguntaProdutoRequest {

	@NotBlank
	private String titulo;

	@JsonCreator(mode = Mode.PROPERTIES)
	public NovaPerguntaProdutoRequest(@NotBlank String titulo) {
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}

	public Pergunta toModel(Produto produto, Usuario interessado) {
		return new Pergunta(titulo, produto, interessado);
	}

}
