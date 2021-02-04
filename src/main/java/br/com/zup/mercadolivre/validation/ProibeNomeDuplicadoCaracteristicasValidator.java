package br.com.zup.mercadolivre.validation;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.zup.mercadolivre.produtos.NovoProdutoRequest;

@Component
public class ProibeNomeDuplicadoCaracteristicasValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return NovoProdutoRequest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(errors.hasErrors()) {
			return;
		}
		
		NovoProdutoRequest request = (NovoProdutoRequest) target;
		
		Set<String> nomesDuplicados = request.bucaNomeCaracteristicasIguais();
		
		if(!nomesDuplicados.isEmpty()) {
			for(String nome : nomesDuplicados) {
				errors.rejectValue("nome", null, "O valor "+ nome +" está duplicado na requisição");
			}
		}
		
	}

}
