package br.com.zup.mercadolivre.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import br.com.zup.mercadolivre.usuarios.Usuario;

public class AuthUser extends User {

	private static final long serialVersionUID = 1L;

	private Long userId;
	private String username;

	public AuthUser(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getLogin(), usuario.getSenha(), authorities);

		userId = usuario.getId();
		username = usuario.getLogin();
	}

	public Long getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

}
