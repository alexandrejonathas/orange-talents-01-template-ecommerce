package br.com.zup.mercadolivre.pedidos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zup.mercadolivre.mailer.Mailer;
import br.com.zup.mercadolivre.security.MercadoLivreSecurity;
import br.com.zup.mercadolivre.usuarios.Usuario;
import br.com.zup.mercadolivre.validation.ProibeQauntidadeDeProdutoRequisitadaMaiorQueDisponivel;

@RestController
public class PedidoController {

	/**
	 * Testar essa implementação
	 * Pode ser utilizado para recuperar o usuário @AuthenticationPrincipal Usuario usuario
	 * @param request
	 */
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private MercadoLivreSecurity mlSecurity;
	
	@Autowired
	private ProibeQauntidadeDeProdutoRequisitadaMaiorQueDisponivel quantidadeDisponivelValidator;
	
	@Autowired
	private Mailer mailer;
	
	@InitBinder
	public void init(WebDataBinder binder) {
		binder.addValidators(quantidadeDisponivelValidator);
	}
	
	@Transactional
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/pedidos")
	public ResponseEntity<?> cadastra(@RequestBody @Valid NovoPedidoRequest request, 
			UriComponentsBuilder uriBuilder) {
		Usuario comprador = em.find(Usuario.class, mlSecurity.getUserId());
		
		Pedido pedido = request.toModel(em, comprador);
		em.persist(pedido);
		
		Usuario dono = pedido.getDonoProduto();
		
		mailer.send(null, "Novo pedido #"+pedido.getIdentificadoPedido(), "Um novo pedido foi realizado", "contato@mercadolivre.com", dono.getLogin());
		
		String urlRetorno = uriBuilder.path("/pedidos/{identificadorPedido}/retorno")
				.buildAndExpand(pedido.getIdentificadoPedido()).toString();
		
		return ResponseEntity.status(302).header(HttpHeaders.LOCATION, 
				request.getFormaPagamento().getUrlRetorno(pedido.getIdentificadoPedido(), urlRetorno)).build();
	}
	
}
