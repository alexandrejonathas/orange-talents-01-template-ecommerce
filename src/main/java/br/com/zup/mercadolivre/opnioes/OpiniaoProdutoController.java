package br.com.zup.mercadolivre.opnioes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.security.MercadoLivreSecurity;
import br.com.zup.mercadolivre.usuarios.Usuario;

@RestController
public class OpiniaoProdutoController {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private MercadoLivreSecurity mlSecurity;
	
	@Transactional
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/produtos/{id}/opinioes")
	public ResponseEntity<?> cadastra(@PathVariable Long id, @RequestBody @Valid NovaOpiniaoProdutoRequest request) {
		Produto produto = em.find(Produto.class, id);
		
		if(produto == null) {
			return ResponseEntity.notFound().build();
		}
		
		Usuario usuario = em.find(Usuario.class, mlSecurity.getUserId());
		produto.associarOpiniao(request, usuario);
		
		em.persist(produto);
		
		return ResponseEntity.ok().build();
	}
	
}
