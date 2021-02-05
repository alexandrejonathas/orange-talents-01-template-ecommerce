package br.com.zup.mercadolivre.pedidos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import br.com.zup.mercadolivre.usuarios.Usuario;

@Entity
@Table(name="pedidos")
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String identificadoPedido;
	
	@NotNull
	@ManyToOne
	private Usuario comprador;
	
	@Enumerated(EnumType.STRING)
	private StatusPedido status = StatusPedido.INICIADO;
	
	@CreationTimestamp
	private LocalDateTime dataCriacao;
	
	@Size(min = 1)
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.PERSIST)
	private List<PedidoItem> items;
	
	@Deprecated
	public Pedido() {}
	
	public Pedido(Usuario comprador, List<PedidoItem> items) {
		this.identificadoPedido = UUID.randomUUID().toString();
		this.comprador = comprador;
		this.items = items.stream().map(i -> i.associaPedido(this)).collect(Collectors.toList());
	}

	public String getIdentificadoPedido() {
		return identificadoPedido;
	}

	public Usuario getDonoProduto() {
		if(items.isEmpty()) {
			return null;
		}
		return items.get(0).getDono();
	}

	public List<PedidoItem> getItems() {
		return items;
	}
	
}
