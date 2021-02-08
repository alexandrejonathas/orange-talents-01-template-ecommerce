package br.com.zup.mercadolivre.validation;

import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.zup.mercadolivre.pedidos.NovoPedidoRequest;
import br.com.zup.mercadolivre.pedidos.ProdutoPedidoRequest;

@Component
public class ProibeQauntidadeDeProdutoRequisitadaMaiorQueDisponivel implements Validator {

	@Autowired
	private EntityManager em;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return NovoPedidoRequest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(errors.hasErrors()) {
			return;
		}
		NovoPedidoRequest request = (NovoPedidoRequest) target;
		
		Set<ProdutoPedidoRequest> produtosInvalidos = request.buscaItemsComQuantidadeMaiorQueDisponivel(em);
		if(!produtosInvalidos.isEmpty()) {
			for(ProdutoPedidoRequest pr : produtosInvalidos) {
				errors.rejectValue("produtos", null, "A quantidade do produtoId="+pr.getProdutoId()+" não pode ser maior que a disponível");
			}
		}
	}
	
}
