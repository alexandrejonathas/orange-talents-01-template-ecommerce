package br.com.zup.mercadolivre.produtos;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "fotos")
public class Foto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String nome;
	
	@NotNull
	private String path;

	@NotNull
	@ManyToOne
	private Produto produto;
	
	public Foto(Produto produto, MultipartFile file) {
		this.nome = UUID.randomUUID().toString() + "_" +file.getOriginalFilename();
		this.path = "http://localhost:8080/fotos/"+this.nome;
		this.produto = produto;
	}
	
}
