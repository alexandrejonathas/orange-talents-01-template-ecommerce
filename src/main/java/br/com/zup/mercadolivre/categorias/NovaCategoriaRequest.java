package br.com.zup.mercadolivre.categorias;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.constraints.NotBlank;

import br.com.zup.mercadolivre.validation.IdExists;
import br.com.zup.mercadolivre.validation.UniqueValue;

public class NovaCategoriaRequest {

	@NotBlank
	@UniqueValue(domainClass = Categoria.class, fieldName = "nome")
	private String nome;

	@IdExists(domainClass = Categoria.class)
	private Long categoriaId;

	public NovaCategoriaRequest(@NotBlank String nome, Long categoriaId) {
		this.nome = nome;
		this.categoriaId = categoriaId;
	}

	@Override
	public String toString() {
		return "Categoria [nome=" + nome + ", categoriaId=" + categoriaId + "]";
	}

	public String getNome() {
		return nome;
	}

	public Long getCategoriaId() {
		return categoriaId;
	}

	public Categoria toModel(EntityManager em) {
		Categoria categoria = categoriaId == null ? null : em.find(Categoria.class, categoriaId);
		return new Categoria(nome, categoria);
	}

	/*private Categoria getCategoria(EntityManager em, Long categoriaId) {
		Categoria categoria = null;
		if(categoriaId != null) {
			Query query = em.createQuery("select c from Categoria c where c.id = :id");
			query.setParameter("id", categoriaId);
			categoria = (Categoria) query.getSingleResult();
		}
		return categoria;
	}*/

}
