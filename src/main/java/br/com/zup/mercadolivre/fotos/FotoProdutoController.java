package br.com.zup.mercadolivre.fotos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.security.CheckSecurity;

@RestController
public class FotoProdutoController {

	@PersistenceContext
	private EntityManager em;	
	
	@Autowired
	private UploadFake uploadFake;	
	
	@Transactional
	@CheckSecurity.Produtos.pertenceAoUsuario
	@PostMapping("/produtos/{id}/fotos")
	public ResponseEntity<?> cadastraFotos(@PathVariable Long id, @Valid FotosProdutoRequest request) {		
		Produto produto = em.find(Produto.class, id);
		
		if(produto == null) {
			return ResponseEntity.notFound().build();
		}
		
		List<String> links = uploadFake.send(request.getFiles());
		
		produto.associaLinks(links);
		
		em.persist(produto);
		
		return ResponseEntity.ok().build();
	}	
	
}
