package br.com.zup.mercadolivre.perguntas;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zup.mercadolivre.eventos.PerguntaCriadaEvent;
import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.security.MercadoLivreSecurity;
import br.com.zup.mercadolivre.usuarios.Usuario;

@RestController
public class PerguntaProdutoController {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	//1
	private MercadoLivreSecurity mlSecurity;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Transactional
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/produtos/{id}/perguntas")
	public ResponseEntity<?> cadastra(@PathVariable Long id, 
			@RequestBody @Valid NovaPerguntaProdutoRequest request,
			UriComponentsBuilder uriBuilder) {
		//1
		Produto produto = em.find(Produto.class, id);
		//4
		if(produto == null) {
			return ResponseEntity.notFound().build();
		}
		//1
		Usuario interessado = em.find(Usuario.class, mlSecurity.getUserId());
		
		//1
		Pergunta pergunta = request.toModel(produto, interessado);
		em.persist(pergunta);
		
		//7
		String urlDetalheProduto = uriBuilder.path("/produtos/{id}").buildAndExpand(id).toString();
		publisher.publishEvent(new PerguntaCriadaEvent(urlDetalheProduto, pergunta));
		
		return ResponseEntity.ok().build();
	}
	
}
