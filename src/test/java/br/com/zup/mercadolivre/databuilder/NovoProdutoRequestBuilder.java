package br.com.zup.mercadolivre.databuilder;

import java.math.BigDecimal;
import java.util.List;

import br.com.zup.mercadolivre.caracteristicas.CaractesticasProdutoRequest;
import br.com.zup.mercadolivre.produtos.NovoProdutoRequest;

public class NovoProdutoRequestBuilder {
	
	private String nome;
	private BigDecimal valor;
	private Integer quantidade;
	private String descricao;
	private Long categoriaId;
	private List<CaractesticasProdutoRequest> caracteristicas;
	
	public NovoProdutoRequestBuilder comNome(String nome) {
		this.nome = nome;
		return this;
	}
	
	public NovoProdutoRequestBuilder comValor(String valor) {
		this.valor = new BigDecimal(valor);
		return this;
	}
	
	public NovoProdutoRequestBuilder comQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
		return this;
	}
	
	public NovoProdutoRequestBuilder comDescricao(String descricao) {
		this.descricao = descricao;
		return this;
	}	

	public NovoProdutoRequestBuilder comCategoriaId(Long categoriaId) {
		this.categoriaId = categoriaId;
		return this;
	}
	
	public NovoProdutoRequestBuilder comCaracteristicas(List<CaractesticasProdutoRequest> caracteristicas) {
		this.caracteristicas = caracteristicas;
		return this;
	}
	
	public NovoProdutoRequest constroi() {
		return new NovoProdutoRequest(nome, valor, quantidade, descricao, categoriaId, caracteristicas);
	}
}
