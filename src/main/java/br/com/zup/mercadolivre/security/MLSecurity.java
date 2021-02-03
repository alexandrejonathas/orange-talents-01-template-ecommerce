package br.com.zup.mercadolivre.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MLSecurity {

	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public Long getUserId() {		
		var authUser = (AuthUser) getAuthentication().getPrincipal(); 
		return authUser.getUserId();
	}	
	
}
