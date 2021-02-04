package br.com.zup.mercadolivre.produtos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.mercadolivre.security.CheckSecurity;
import br.com.zup.mercadolivre.security.MercadoLivreSecurity;

@RestController
public class ProdutoController {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private MercadoLivreSecurity mlSecurity;	
	
	@Transactional
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/produtos")
	public void cadastra(@RequestBody @Valid NovoProdutoRequest request) {
		Produto produto = request.toModel(em, mlSecurity.getUserId());
		em.persist(produto);
	}
	
	@Transactional
	@CheckSecurity.Produtos.pertenceAoUsuario
	@PostMapping("/produtos/{id}/fotos")
	public ResponseEntity<?> cadastraFotos(@PathVariable Long id, @Valid FotosProdutoRequest request) {		
		Produto produto = em.find(Produto.class, id);
		
		if(produto == null) {
			return ResponseEntity.notFound().build();
		}
		
		produto.buildFotos(request.getFiles());
		
		em.persist(produto);
		
		return ResponseEntity.ok().build();
	}
	
}
