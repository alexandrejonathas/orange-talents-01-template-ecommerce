package br.com.zup.mercadolivre.databuilder;

import br.com.zup.mercadolivre.opnioes.NovaOpiniaoProdutoRequest;

public class NovaOpiniaoProdutoRequestBuilder {

	private String titulo;
	
	private String descricao;
	
	private Integer nota;
	
	public NovaOpiniaoProdutoRequestBuilder comTitulo(String titulo) {
		this.titulo = titulo;
		return this;
	}
	
	public NovaOpiniaoProdutoRequestBuilder comDescricao(String descricao) {
		this.descricao = descricao;
		return this;
	}	
	
	public NovaOpiniaoProdutoRequestBuilder comNota(Integer nota) {
		this.nota = nota;
		return this;
	}	
	
	public NovaOpiniaoProdutoRequest constroi() {
		return new NovaOpiniaoProdutoRequest(titulo, descricao, nota);
	}
}
