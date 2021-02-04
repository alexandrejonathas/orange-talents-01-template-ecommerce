package br.com.zup.mercadolivre.produtos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "fotos")
public class Foto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String url;

	@NotNull
	@ManyToOne
	private Produto produto;
	
	public Foto(Produto produto, String url) {
		this.url = url;
		this.produto = produto;
	}
	
}
