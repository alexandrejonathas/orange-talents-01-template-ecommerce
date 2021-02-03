package br.com.zup.mercadolivre.produtos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import br.com.zup.mercadolivre.caracteristicas.Caracteristica;
import br.com.zup.mercadolivre.caracteristicas.CaractesticasProdutoRequest;
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

	@OneToMany(mappedBy = "produto", cascade = CascadeType.PERSIST)
	private List<Caracteristica> caracteristicas;
	
	@OneToMany(mappedBy = "produto", cascade = CascadeType.PERSIST)
	private List<Foto> fotos = new ArrayList<>();
	
	@Deprecated
	public Produto() {}
	
	public Produto(@NotBlank String nome, @NotNull BigDecimal valor, @NotNull @Size(min = 0) Integer quantidade,
			@NotBlank @Size(max = 1000) String descricao, Usuario usuario, Categoria categoria,
			List<CaractesticasProdutoRequest> caracteristicas) {
		this.nome = nome;
		this.valor = valor;
		this.quantidade = quantidade;
		this.descricao = descricao;
		this.usuario = usuario;
		this.categoria = categoria;
		this.caracteristicas = buildCaracteristicas(caracteristicas);
	}	

	private List<Caracteristica> buildCaracteristicas(List<CaractesticasProdutoRequest> caracteristicas) {
		return caracteristicas.stream()
				.map(c -> new Caracteristica(c.getNome(), c.getDescricao(), this))
				.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void buildFotos(MultipartFile[] files) {
		for(MultipartFile file : files) {
			this.fotos.add(new Foto(this, file));
		}
	}
	
}
