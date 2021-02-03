package br.com.zup.mercadolivre.databuilder;

import br.com.zup.mercadolivre.usuarios.Usuario;

public class UsuarioBuilder {

	private String login;
	private String senha;
	
	public UsuarioBuilder comLogin(String login) {
		this.login = login;
		return this;
	}
	
	public UsuarioBuilder comSenha(String senha) {
		this.senha = senha;
		return this;
	}	
	
	public Usuario constroi() {
		return new Usuario(login, senha);
	}
	
}
