package br.com.zup.mercadolivre.perguntas;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.Assert;

import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.usuarios.Usuario;

@Entity
@Table(name = "perguntas")
public class Pergunta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(columnDefinition = "text")
	private String titulo;

	@NotNull
	@ManyToOne
	private Produto produto;

	@NotNull
	@ManyToOne
	private Usuario interessado;

	@CreationTimestamp
	private LocalDateTime instante;

	@Deprecated
	public Pergunta() {}
	
	public Pergunta(@NotBlank String titulo, @NotNull Produto produto, @NotNull Usuario interessado) {
		this.titulo = titulo;
		this.produto = produto;
		this.interessado = interessado;

		Assert.state(produto != null, "O produto não pode vir null");
		Assert.state(interessado != null, "O usuário não pode vir null");
	}

	public String getTitulo() {
		return titulo;
	}

	public Produto getProduto() {
		return produto;
	}

}
