package br.com.zup.mercadolivre.autenticacao;

import javax.validation.constraints.NotBlank;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.sun.istack.NotNull;

public class AutenticacaoRequest {

	@NotNull
	@NotBlank
	private String login;

	@NotNull
	@NotBlank
	private String senha;

	public AutenticacaoRequest(@NotBlank String login, @NotBlank String senha) {
		this.login = login;
		this.senha = senha;
	}

	public UsernamePasswordAuthenticationToken converter() {
		return new UsernamePasswordAuthenticationToken(login, senha);
	}

	public String getLogin() {
		return login;
	}

	public String getSenha() {
		return senha;
	}

}
