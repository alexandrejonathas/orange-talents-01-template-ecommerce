package br.com.zup.mercadolivre.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.zup.mercadolivre.produtos.ProdutoRepository;

@Component
public class MercadoLivreSecurity {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public Long getUserId() {		
		var authUser = (AuthUser) getAuthentication().getPrincipal(); 
		return authUser.getUserId();
	}	
	
	public boolean gerenciaProduto(Long id) {
		if(id == null)
			return false;
		return produtoRepository.findByIdAndUsuarioId(id, getUserId()).isPresent();
	}
	
}
