package br.com.zup.mercadolivre.pedidos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.validation.IdExists;

public class ProdutoPedidoRequest {

	@NotNull
	@IdExists(domainClass = Produto.class)
	private Long produtoId;

	@Min(1)
	@Positive
	@NotNull
	private Integer quantidade;

	public ProdutoPedidoRequest(Long produtoId, Integer quantidade) {
		this.produtoId = produtoId;
		this.quantidade = quantidade;
	}

	public Long getProdutoId() {
		return produtoId;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

}
