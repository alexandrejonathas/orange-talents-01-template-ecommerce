package br.com.zup.mercadolivre.usuarios;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import br.com.zup.mercadolivre.validation.UniqueValue;

public class NovoUsuarioRequest {

	@Email
	@NotBlank
	@UniqueValue(domainClass = Usuario.class, fieldName = "login")
	private String login;

	@Size(min = 6)
	@NotBlank
	private String senha;

	public NovoUsuarioRequest(@NotEmpty @Email String login, @NotBlank @Size(min = 6) String senha) {
		this.login = login;
		this.senha = senha;
	}

	@Override
	public String toString() {
		return "Usuario [login="+login+", senha="+senha+"]";
	}

	public String getLogin() {
		return login;
	}

	public String getSenha() {
		return senha;
	}

	public Usuario toModel() {
		return new Usuario(login, senha);
	}
	
}
