package br.com.zup.mercadolivre.databuilder;

import br.com.zup.mercadolivre.categorias.Categoria;

public class CategoriaBuilder {

	private String nome;
	
	private Categoria categoria;
	
	public CategoriaBuilder comNome(String nome) {
		this.nome = nome;
		return this;
	}

	public CategoriaBuilder comCategoria(Categoria categoria) {
		this.categoria = categoria;
		return this;
	}	
	
	public Categoria constroi() {
		return new Categoria(nome, categoria);
	}
	
}
