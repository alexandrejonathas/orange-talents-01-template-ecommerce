package br.com.zup.mercadolivre.categorias;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoriaController {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	@PostMapping("/categorias")
	public void cadastra(@RequestBody @Valid NovaCategoriaRequest request) {
		Categoria categoria = request.toModel(em);
		em.persist(categoria);
	}
	
}
