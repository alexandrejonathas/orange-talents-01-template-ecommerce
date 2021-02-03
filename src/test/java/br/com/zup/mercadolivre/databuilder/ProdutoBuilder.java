package br.com.zup.mercadolivre.databuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.zup.mercadolivre.caracteristicas.CaractesticasProdutoRequest;
import br.com.zup.mercadolivre.categorias.Categoria;
import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.usuarios.Usuario;

public class ProdutoBuilder {

	private String nome;
	private BigDecimal valor; 
	private Integer quantidade;	
	private String descricao; 
	private Categoria categoria;
	private Usuario usuario;
	
	private List<CaractesticasProdutoRequest> caracteristicas = new ArrayList<>();
	
	public ProdutoBuilder comNome(String nome) {
		this.nome = nome;
		return this;
	}
	
	public ProdutoBuilder comValor(String valor) {
		this.valor = new BigDecimal(valor);
		return this;
	}
	
	public ProdutoBuilder comQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
		return this;
	}	
	
	public ProdutoBuilder comDescricao(String descricao) {
		this.descricao = descricao;
		return this;
	}
	
	public ProdutoBuilder comCategoria(Categoria categoria) {
		this.categoria = categoria;
		return this;
	}	

	public ProdutoBuilder comUsuario(Usuario usuario) {
		this.usuario = usuario;
		return this;
	}		
	
	public ProdutoBuilder comCaracteristica(CaractesticasProdutoRequest caracteristica) {
		caracteristicas.add(caracteristica);
		return this;
	}
	
	public Produto constroi() {
		return new Produto(nome, valor, quantidade, descricao, usuario, categoria, caracteristicas);
	}

	
}
