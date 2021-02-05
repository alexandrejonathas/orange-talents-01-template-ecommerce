package br.com.zup.mercadolivre.databuilder;

import java.util.ArrayList;
import java.util.List;

import br.com.zup.mercadolivre.pedidos.FormaPagamento;
import br.com.zup.mercadolivre.pedidos.NovoPedidoRequest;
import br.com.zup.mercadolivre.pedidos.ProdutoPedidoRequest;

public class NovoPedidoRequestBuilder {

	private FormaPagamento formaPagamento;
	
	private List<ProdutoPedidoRequest> produtos = new ArrayList<ProdutoPedidoRequest>();
	
	public NovoPedidoRequestBuilder comFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
		return this;
	}
	
	public NovoPedidoRequestBuilder addProduto(ProdutoPedidoRequest produto) {
		produtos.add(produto);
		return this;
	}

	public NovoPedidoRequest constroi() {
		return new NovoPedidoRequest(formaPagamento, produtos);
	}
}
