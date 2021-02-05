package br.com.zup.mercadolivre.pedidos;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.usuarios.Usuario;

@Entity
@Table(name = "pedido_items")
public class PedidoItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne
	private Pedido pedido;
	
	@NotNull
	@ManyToOne
	private Produto produto;
	
	@NotNull
	private Integer quantidade;
	
	@NotNull
	private BigDecimal valor;

	@Deprecated
	public PedidoItem() {}
	
	public PedidoItem(@NotNull Produto produto, @NotNull Integer quantidade, BigDecimal valor) {
		this.produto = produto;
		this.quantidade = quantidade;
		this.valor = valor;
	}
	
	public PedidoItem associaPedido(Pedido pedido) {
		this.pedido = pedido;
		return this;
	}

	public Usuario getDono() {
		return produto.getUsuario();
	}
}
