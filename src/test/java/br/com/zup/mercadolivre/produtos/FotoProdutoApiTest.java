package br.com.zup.mercadolivre.produtos;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zup.mercadolivre.autenticacao.AutenticacaoRequest;
import br.com.zup.mercadolivre.caracteristicas.CaractesticasProdutoRequest;
import br.com.zup.mercadolivre.categorias.Categoria;
import br.com.zup.mercadolivre.databuilder.ProdutoBuilder;
import br.com.zup.mercadolivre.databuilder.UsuarioBuilder;
import br.com.zup.mercadolivre.security.TokenService;
import br.com.zup.mercadolivre.usuarios.Usuario;

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
	
	@Test
	public void deveRetornar403QuandoAcessarSemToken() throws Exception {
		mockMvc.perform(multipart("/produtos/{id}/fotos", "1")
				.file(new MockMultipartFile("files", "teste.txt".getBytes()))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isForbidden());
	}

	@Test
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

		mockMvc.perform(multipart("/produtos/{id}/fotos", produto.getId())
					.file(new MockMultipartFile("files", "teste.txt".getBytes()))
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + token).content(objectMapper.writeValueAsString(produto)))
				.andDo(print()).andExpect(status().isForbidden());
	}

	@Test
	@Transactional
	public void deveCadastrarAsFotosDoProduto() throws Exception {
		String email = "owner@email.com";
		String senha = "123456";

		Usuario owner = new UsuarioBuilder().comLogin(email).comSenha(senha).constroi();

		em.persist(owner);

		Produto produto = criaProduto(owner, getCategoria());

		String token = getToken(email, senha);

		mockMvc.perform(multipart("/produtos/{id}/fotos", produto.getId())
					.file(new MockMultipartFile("files", "teste1.png".getBytes()))
					.file(new MockMultipartFile("files", "teste2.png".getBytes()))
					.file(new MockMultipartFile("files", "teste3.png".getBytes()))
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + token)
				)
				.andDo(print())
				.andExpect(status().isOk());
		
		Query query = em.createQuery("select f from Foto f where f.produto = :produto");
		query.setParameter("produto", produto);
		
		var fotos = query.getResultList();
		assertTrue(fotos.size() == 3);
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
