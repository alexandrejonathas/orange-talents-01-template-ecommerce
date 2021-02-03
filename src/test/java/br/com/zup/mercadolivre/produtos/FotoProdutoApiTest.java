package br.com.zup.mercadolivre.produtos;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zup.mercadolivre.autenticacao.AutenticacaoRequest;
import br.com.zup.mercadolivre.caracteristicas.CaractesticasProdutoRequest;
import br.com.zup.mercadolivre.categorias.Categoria;
import br.com.zup.mercadolivre.databuilder.ProdutoBuilder;
import br.com.zup.mercadolivre.databuilder.UsuarioBuilder;
import br.com.zup.mercadolivre.security.TokenService;
import br.com.zup.mercadolivre.usuarios.Usuario;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class FotoProdutoApiTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private TokenService tokenService;

	public void deveRetornar403QuandoAcessarSemToken() throws Exception {
		mockMvc.perform(put("/produtos/{id}/fotos", "1").contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isForbidden());
	}

	@Transactional
	public void deveRetornar403QuandoTentarAlterarProdutoDeOutroUsuario() throws Exception {
		Usuario owner = new UsuarioBuilder().comLogin("owner@email.com").comSenha("123456").constroi();
		em.persist(owner);

		String emailHacker = "hacker@email.com";
		String senhaHacker = "123456";

		Usuario hacker = new UsuarioBuilder().comLogin(emailHacker).comSenha(senhaHacker).constroi();
		em.persist(hacker);

		Produto produto = criaProduto(owner, getCategoria());

		String token = getToken(emailHacker, senhaHacker);

		mockMvc.perform(
				put("/produtos/{id}/fotos", String.valueOf(produto.getId())).contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + token).content(objectMapper.writeValueAsString(produto)))
				.andDo(print()).andExpect(status().isForbidden());
	}

	@Transactional
	public void deveRetornar200QuandoTentarAlterarProduto() throws Exception {
		String email = "owner@email.com";
		String senha = "123456";

		Usuario owner = new UsuarioBuilder().comLogin(email).comSenha(senha).constroi();

		em.persist(owner);

		Produto produto = criaProduto(owner, getCategoria());

		String token = getToken(email, senha);

		mockMvc.perform(
				put("/produtos/{id}/fotos", String.valueOf(produto.getId()))
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + token)
				)
				.andDo(print())
				.andExpect(status().isOk());
	}

	
	@Transactional
	public void deveAtualizarUmProdutoComSuasFotos() throws Exception {
		String email = "owner@email.com";
		String senha = "123456";

		Usuario owner = new UsuarioBuilder().comLogin(email).comSenha(senha).constroi();

		em.persist(owner);

		Produto produto = criaProduto(owner, getCategoria());

		String token = getToken(email, senha);
		
		mockMvc.perform(
					put("/produtos/{id}/fotos", produto.getId())
					.header("Authorization", "Bearer "+token)
				)
				.andDo(print())
				.andExpect(status().isOk());
	}	
	
	
	private Categoria getCategoria() {
		Categoria categoria = new Categoria("Categoria 1");
		em.persist(categoria);
		return categoria;
	}

	private Produto criaProduto(Usuario usuario, Categoria categoria) {
		Produto produto = new ProdutoBuilder().comUsuario(usuario).comNome("Produto 1").comValor("29.90")
				.comQuantidade(10).comDescricao("Produto novo").comCategoria(categoria)
				.comCaracteristica(new CaractesticasProdutoRequest("Marca", "Marca 1"))
				.comCaracteristica(new CaractesticasProdutoRequest("Modelo", "Modelo 1"))
				.comCaracteristica(new CaractesticasProdutoRequest("Cor", "Cor 1")).constroi();

		em.persist(produto);

		return produto;
	}

	private String getToken(String login, String senha) {
		AutenticacaoRequest loginRequest = new AutenticacaoRequest(login, senha);
		Authentication authentication = authManager.authenticate(loginRequest.converter());
		String token = tokenService.gerar(authentication);
		return token;
	}

}
