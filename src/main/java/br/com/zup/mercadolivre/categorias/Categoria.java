package br.com.zup.mercadolivre.categorias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "categorias")
public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(unique = true)
	private String nome;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Categoria categoria;
	
	@Deprecated
	public Categoria() {}

	public Categoria(@NotBlank String nome) {
		this.nome = nome;
	}	
	
	public Categoria(@NotBlank String nome, Categoria categoria) {
		this.nome = nome;
		this.categoria = categoria;
	}

	@Override
	public String toString() {
		return "Categoria [id="+ id +", nome="+ nome +"]";
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Categoria getCategoria() {
		return categoria;
	}	
}
