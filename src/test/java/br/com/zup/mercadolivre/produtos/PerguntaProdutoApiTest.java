package br.com.zup.mercadolivre.produtos;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zup.mercadolivre.autenticacao.AutenticacaoRequest;
import br.com.zup.mercadolivre.caracteristicas.CaractesticasProdutoRequest;
import br.com.zup.mercadolivre.categorias.Categoria;
import br.com.zup.mercadolivre.databuilder.CategoriaBuilder;
import br.com.zup.mercadolivre.databuilder.ProdutoBuilder;
import br.com.zup.mercadolivre.databuilder.UsuarioBuilder;
import br.com.zup.mercadolivre.perguntas.NovaPerguntaProdutoRequest;
import br.com.zup.mercadolivre.perguntas.Pergunta;
import br.com.zup.mercadolivre.security.TokenService;
import br.com.zup.mercadolivre.usuarios.Usuario;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class PerguntaProdutoApiTest {

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
	public void deveCadastrarUmaPergunta() throws Exception {
		Categoria categoria = new CategoriaBuilder().comNome("categoria 1").constroi();
		em.persist(categoria);
		
		Usuario dono = new UsuarioBuilder().comLogin("dono@email.com").comSenha("123456").constroi();
		em.persist(dono);
		
		Produto produto = new ProdutoBuilder()
				.comNome("Produto 1")
				.comValor("29.9")
				.comQuantidade(10)
				.comDescricao("Descricao 1")
				.comCaracteristica(new CaractesticasProdutoRequest("Marca", "Marca 1"))
				.comCaracteristica(new CaractesticasProdutoRequest("Modelo", "Modelo 1"))
				.comCaracteristica(new CaractesticasProdutoRequest("Cor", "Cor 1"))
				.comCategoria(categoria)
				.comUsuario(dono)
				.constroi();
		em.persist(produto);
		
		String email = "interessado@email.com";
		String senha = "123456";
		
		Usuario interessado = new UsuarioBuilder().comLogin(email).comSenha(senha).constroi();
		em.persist(interessado);
		
		NovaPerguntaProdutoRequest request = new NovaPerguntaProdutoRequest("Nova pergunta");
		
		mockMvc.perform(
				post("/produtos/{id}/perguntas", produto.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+getToken(email, senha))
				.content(objectMapper.writeValueAsString(request))
			)
		.andDo(print())
		.andExpect(status().isOk());
		
		Query query = em.createQuery("select p from Pergunta p where p.produto = :produto");
		query.setParameter("produto", produto);
		
		@SuppressWarnings("unchecked")
		List<Pergunta> perguntas = (List<Pergunta>)query.getResultList();
		
		assertTrue(perguntas.size() == 1);
		
	}
	
	@Test
	public void naoDeveCadastrarUmaPerguntaSemTitulo() throws Exception {
		Categoria categoria = new CategoriaBuilder().comNome("categoria 1").constroi();
		em.persist(categoria);
		
		Usuario dono = new UsuarioBuilder().comLogin("dono@email.com").comSenha("123456").constroi();
		em.persist(dono);
		
		Produto produto = new ProdutoBuilder()
				.comNome("Produto 1")
				.comValor("29.9")
				.comQuantidade(10)
				.comDescricao("Descricao 1")
				.comCaracteristica(new CaractesticasProdutoRequest("Marca", "Marca 1"))
				.comCaracteristica(new CaractesticasProdutoRequest("Modelo", "Modelo 1"))
				.comCaracteristica(new CaractesticasProdutoRequest("Cor", "Cor 1"))
				.comCategoria(categoria)
				.comUsuario(dono)
				.constroi();
		em.persist(produto);
		
		String email = "interessado@email.com";
		String senha = "123456";
		
		Usuario interessado = new UsuarioBuilder().comLogin(email).comSenha(senha).constroi();
		em.persist(interessado);
		
		NovaPerguntaProdutoRequest request = new NovaPerguntaProdutoRequest(null);
		
		mockMvc.perform(
				post("/produtos/{id}/perguntas", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+getToken(email, senha))
				.content(objectMapper.writeValueAsString(request))
			)
		.andDo(print())
		.andExpect(status().isBadRequest());
		
	}	
	
	private String getToken(String login, String senha) {
		AutenticacaoRequest loginRequest = new AutenticacaoRequest(login, senha);
		Authentication authentication = authManager.authenticate(loginRequest.converter());
		String token = tokenService.gerar(authentication);
		return token;
	}		
}
