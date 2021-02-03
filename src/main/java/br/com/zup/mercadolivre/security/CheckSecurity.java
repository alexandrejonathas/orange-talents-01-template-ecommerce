package br.com.zup.mercadolivre.security;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

public @interface CheckSecurity {

	public @interface Produtos {
		
		@PreAuthorize("isAuthenticated() and @mercadoLivreSecurity.gerenciaProduto(#id)")
		@Retention(RUNTIME)
		@Target({ METHOD })
		public @interface pertenceAoUsuario { }
		
	}
	
}
