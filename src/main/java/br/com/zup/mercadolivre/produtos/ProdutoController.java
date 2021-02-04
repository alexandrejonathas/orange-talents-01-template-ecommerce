package br.com.zup.mercadolivre.produtos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.mercadolivre.security.MercadoLivreSecurity;
import br.com.zup.mercadolivre.validation.ProibeNomeDuplicadoCaracteristicasValidator;

@RestController
public class ProdutoController {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private MercadoLivreSecurity mlSecurity;	
	
	@Autowired
	private ProibeNomeDuplicadoCaracteristicasValidator proibeNomeDuplicadoCaracteristicas;
	
	@InitBinder
	public void init(WebDataBinder binder) {
		binder.addValidators(proibeNomeDuplicadoCaracteristicas);
	}
	
	@Transactional
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/produtos")
	public void cadastra(@RequestBody @Valid NovoProdutoRequest request) {
		Produto produto = request.toModel(em, mlSecurity.getUserId());
		em.persist(produto);
	}
	
}
