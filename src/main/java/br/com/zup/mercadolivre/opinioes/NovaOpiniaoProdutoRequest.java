package br.com.zup.mercadolivre.opinioes;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.usuarios.Usuario;

public class NovaOpiniaoProdutoRequest {

	@NotBlank
	private String titulo;
	
	@NotBlank
	@Size(max = 500)
	private String descricao;
	
	@NotNull
	@Min(1)
	@Max(5)
	private Integer nota;

	public NovaOpiniaoProdutoRequest(@NotBlank String titulo, @NotBlank @Size(max = 500) String descricao,
			@NotNull @Size(min = 1, max = 5) Integer nota) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.nota = nota;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getNota() {
		return nota;
	}
	
	public Opiniao toModel(Produto produto, Usuario usuario) {
		return new Opiniao(titulo, descricao, nota, produto, usuario);		
	}

	
}
