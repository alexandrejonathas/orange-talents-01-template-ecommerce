package br.com.zup.mercadolivre.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.zup.mercadolivre.pedidos.NovoPedidoRequest;
import br.com.zup.mercadolivre.pedidos.ProdutoPedidoRequest;
import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.produtos.ProdutoRepository;

@Component
public class ProibeQauntidadeDeProdutoRequisitadaMaiorQueDisponivel implements Validator {

	@Autowired
	private ProdutoRepository repository;
	
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
		List<Produto> produtos = getProdutos(request.getProdutos());
		
		Set<ProdutoPedidoRequest> produtosInvalidos = request.buscaItemsComQuantidadeMaiorQueDisponivel(produtos);
		if(!produtosInvalidos.isEmpty()) {
			for(ProdutoPedidoRequest pr : produtosInvalidos) {
				errors.rejectValue("produtos", null, "A quantidade do produtoId="+pr.getProdutoId()+" não pode ser maior que a disponível");
			}
		}
	}

	private List<Produto> getProdutos(List<ProdutoPedidoRequest> produtosRequest) {
		List<Produto> produtos = new ArrayList<>();
		for(ProdutoPedidoRequest pr : produtosRequest) {
			Optional<Produto> produto = repository.findById(pr.getProdutoId());
			produtos.add(produto.get());
		}
		return produtos;
	}

	
}
