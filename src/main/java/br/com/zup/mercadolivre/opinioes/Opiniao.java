package br.com.zup.mercadolivre.opinioes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.usuarios.Usuario;

@Entity
@Table(name="opinioes")
public class Opiniao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank 
	private String titulo;
	
	@NotBlank @Size(max = 500)
	@Column(columnDefinition = "text") 
	private String descricao;
	
	@NotNull @Min(1) @Max(5) 
	private Integer nota;
	
	@Valid
	@ManyToOne
	private Produto produto;

	@ManyToOne
	private Usuario usuario;	
	
	@Deprecated
	public Opiniao() {}
	
	public Opiniao(@NotBlank String titulo, @NotBlank @Size(max = 500) String descricao,
			@NotNull @Min(1) @Max(5) Integer nota, Produto produto, Usuario usuario) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.nota = nota;
		this.produto = produto;
		this.usuario = usuario;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getNota() {
		return nota;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public Produto getProduto() {
		return produto;
	}
}
