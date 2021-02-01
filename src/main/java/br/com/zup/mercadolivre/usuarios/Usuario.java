package br.com.zup.mercadolivre.usuarios;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Email
	@Column(unique = true)
	private String login;
	
	@NotBlank
	@Size(min = 6)
	private String senha;
	
	@CreationTimestamp
	private LocalDateTime cadastradoEm;

	@Deprecated
	public Usuario() {}
	
	public Usuario(@NotBlank @Email String login, @NotBlank @Size(min = 6) String senha) {
		this.login = login;
		this.senha = new BCryptPasswordEncoder().encode(senha);
	}
	
	@Override
	public String toString() {
		return "Usuario [id="+id+", login="+login+", senha="+senha+"]";
	}
	
}
