package br.com.zup.mercadolivre.caracteristicas;

import javax.validation.constraints.NotBlank;

public class CaractesticasProdutoRequest {

	@NotBlank
	private String nome;

	@NotBlank
	private String descricao;

	public CaractesticasProdutoRequest(String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

}
