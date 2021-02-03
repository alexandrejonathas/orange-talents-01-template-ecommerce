package br.com.zup.mercadolivre.produtos;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.zup.mercadolivre.categorias.Categoria;
import br.com.zup.mercadolivre.usuarios.Usuario;

@Entity
@Table(name = "produtos")
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank 
	private String nome;
	
	@NotNull 
	private BigDecimal valor; 
	
	@NotNull 
	@Min(0) 
	private Integer quantidade;
	
	@NotBlank 
	@Size(max = 1000) 
	private String descricao; 
	
	@ManyToOne
	private Usuario usuario;
	
	@ManyToOne
	private Categoria categoria;

	public Produto(@NotBlank String nome, @NotNull BigDecimal valor, @NotNull @Size(min = 0) Integer quantidade,
			@NotBlank @Size(max = 1000) String descricao, Usuario usuario, Categoria categoria) {
		this.nome = nome;
		this.valor = valor;
		this.quantidade = quantidade;
		this.descricao = descricao;
		this.usuario = usuario;
		this.categoria = categoria;
	}	

}
