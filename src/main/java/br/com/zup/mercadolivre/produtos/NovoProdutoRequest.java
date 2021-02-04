package br.com.zup.mercadolivre.produtos;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.zup.mercadolivre.caracteristicas.CaractesticasProdutoRequest;
import br.com.zup.mercadolivre.categorias.Categoria;
import br.com.zup.mercadolivre.usuarios.Usuario;
import br.com.zup.mercadolivre.validation.IdExists;

public class NovoProdutoRequest {
	
	@NotBlank
	private String nome;

	@DecimalMin(value = "0.01")
	@NotNull
	private BigDecimal valor;

	@NotNull
	@Min(0)
	private Integer quantidade;

	@NotBlank
	@Size(max = 1000)
	private String descricao;

	@NotNull
	@IdExists(domainClass = Categoria.class)
	private Long categoriaId;

	@Valid
	@NotNull
	@Size(min = 3)
	private List<CaractesticasProdutoRequest> caracteristicas;
	
	public NovoProdutoRequest(String nome, BigDecimal valor, Integer quantidade, 
			String descricao, Long categoriaId, List<CaractesticasProdutoRequest> caracteristicas) {
		this.nome = nome;
		this.valor = valor;
		this.quantidade = quantidade;
		this.descricao = descricao;
		this.categoriaId = categoriaId;
		this.caracteristicas = caracteristicas;
	}

	public String getNome() {
		return nome;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public String getDescricao() {
		return descricao;
	}

	public Long getCategoriaId() {
		return categoriaId;
	}

	public List<CaractesticasProdutoRequest> getCaracteristicas() {
		return caracteristicas;
	}

	public Produto toModel(EntityManager em, Long usuarioId) {
		Usuario usuario = em.find(Usuario.class, usuarioId);
		Categoria categoria = em.find(Categoria.class, categoriaId);
		
		return new Produto(nome, valor, quantidade, descricao, usuario, categoria, caracteristicas);
	}

	public Set<String> bucaNomeCaracteristicasIguais() {
		Set<String> caracteristicasIguais = new HashSet<>();
		Set<String> nomesIguais = new HashSet<>();
		for(CaractesticasProdutoRequest c : caracteristicas) {
			if(!caracteristicasIguais.add(c.getNome())) {
				nomesIguais.add(c.getNome());
			}
		}
		return nomesIguais;
	}
	
}
