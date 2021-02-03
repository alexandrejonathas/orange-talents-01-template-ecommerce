package br.com.zup.mercadolivre.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.zup.mercadolivre.usuarios.UsuarioRepository;

@Service
public class AuthService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var possivelUsuario = usuarioRepository.findByLogin(username);
		
		if(possivelUsuario.isEmpty()) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}
		
		return new AuthUser(possivelUsuario.get(), Collections.emptyList());
	}

}
