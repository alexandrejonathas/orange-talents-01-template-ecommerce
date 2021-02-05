package br.com.zup.mercadolivre.produtos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DetalheProdutoController {

	@PersistenceContext
	private EntityManager em;
	
	@GetMapping("/produtos/{id}")
	public ResponseEntity<?> busca(@PathVariable Long id) {
		Produto produto = em.find(Produto.class, id);
		
		if(produto == null) {
			return ResponseEntity.notFound().build();
		}
		
		DetalheProdutoResponse response = new DetalheProdutoResponse(produto);
		
		return ResponseEntity.ok(response);
		
	}
	
}
