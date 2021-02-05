package br.com.zup.mercadolivre.pedidos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.usuarios.Usuario;

public class NovoPedidoRequest {

	private FormaPagamento formaPagamento;
	
	@Valid
	@NotNull
	@Size(min = 1)
	private List<ProdutoPedidoRequest> produtos;

	@JsonCreator(mode = Mode.PROPERTIES)
	public NovoPedidoRequest(FormaPagamento formaPagamento, List<ProdutoPedidoRequest> produtos) {
		this.formaPagamento = formaPagamento;
		this.produtos = produtos;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public List<ProdutoPedidoRequest> getProdutos() {
		return produtos;
	}

	public Pedido toModel(EntityManager em, Usuario usuario) {
		List<PedidoItem> items = new ArrayList<PedidoItem>();
		for(ProdutoPedidoRequest produtoRequest : produtos) {
			Produto produto = em.find(Produto.class, produtoRequest.getProdutoId());
			produto.baixaEstoque(produtoRequest.getQuantidade());
			items.add(new PedidoItem(produto, produtoRequest.getQuantidade(),produto.getValor()));
		}
		return new Pedido(usuario, items);		
	}

	public Set<ProdutoPedidoRequest> buscaItemsComQuantidadeMaiorQueDisponivel(List<Produto> produtosDisponiveis) {
		Set<ProdutoPedidoRequest> produtosInvalidos = new HashSet<ProdutoPedidoRequest>();
		for(Produto pd : produtosDisponiveis) {
			produtosInvalidos = this.produtos.stream()
					.filter(pr -> pr.getQuantidade() > pd.getQuantidade()).collect(Collectors.toSet());
		}
		return produtosInvalidos;
	}
	
}
