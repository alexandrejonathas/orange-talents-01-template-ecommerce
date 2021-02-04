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
import br.com.zup.mercadolivre.databuilder.NovaOpiniaoProdutoRequestBuilder;
import br.com.zup.mercadolivre.databuilder.ProdutoBuilder;
import br.com.zup.mercadolivre.databuilder.UsuarioBuilder;
import br.com.zup.mercadolivre.opnioes.NovaOpiniaoProdutoRequest;
import br.com.zup.mercadolivre.opnioes.Opiniao;
import br.com.zup.mercadolivre.security.TokenService;
import br.com.zup.mercadolivre.usuarios.Usuario;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class OpiniaoProdutoApiTest {

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
	
	@Transactional
	@Test
	public void deveCadastrarUmaOpiniaoDoProduto() throws Exception {
		Categoria categoria = new CategoriaBuilder().comNome("Categoria 1").constroi();
		em.persist(categoria);
		
		Usuario dono = new UsuarioBuilder().comLogin("dono@email.com").comSenha("123456").constroi();
		em.persist(dono);
		
		Produto produto = new ProdutoBuilder()
				.comNome("Produto 1")
				.comValor("29.9")
				.comQuantidade(10)
				.comDescricao("Descricao 1")
				.comCategoria(categoria)
				.comUsuario(dono)
				.comCaracteristica(new CaractesticasProdutoRequest("Marca", "Marca 1"))
				.comCaracteristica(new CaractesticasProdutoRequest("Modelo", "Modelo 1"))
				.comCaracteristica(new CaractesticasProdutoRequest("Cor", "Cor 1"))
				.constroi();
		em.persist(produto);
		
		NovaOpiniaoProdutoRequest request = new NovaOpiniaoProdutoRequestBuilder()
				.comTitulo("Primeira opniao")
				.comDescricao("Essa é minha opnião")
				.comNota(5).constroi();
		
		String email = "opinador@email.com";
		String senha = "123456";
		
		Usuario opinador = new Usuario(email, senha);
		em.persist(opinador);
		
		mockMvc.perform(
				post("/produtos/{id}/opinioes", produto.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + getToken(email, senha))
				.content(objectMapper.writeValueAsString(request))
			)
			.andDo(print())
			.andExpect(status().isOk());
		
		Query query = em.createQuery("select o from Opiniao o where o.produto = :produto");
		query.setParameter("produto", produto);
		
		@SuppressWarnings("unchecked")
		List<Opiniao> opinioes = (List<Opiniao>)query.getResultList();
		
		assertTrue(opinioes.size() == 1);
		assertTrue(opinioes.get(0).getProduto().equals(produto));
		assertTrue(opinioes.get(0).getUsuario().equals(opinador));
		
	}
	
	@Transactional
	@Test
	public void naoDeveCadastrarUmaOpiniaoDoProdutoSemEstarLogado() throws Exception {
		Categoria categoria = new CategoriaBuilder().comNome("Categoria 1").constroi();
		em.persist(categoria);
		
		Usuario dono = new UsuarioBuilder().comLogin("dono@email.com").comSenha("123456").constroi();
		em.persist(dono);
		
		Produto produto = new ProdutoBuilder()
				.comNome("Produto 1")
				.comValor("29.9")
				.comQuantidade(10)
				.comDescricao("Descricao 1")
				.comCategoria(categoria)
				.comUsuario(dono)
				.comCaracteristica(new CaractesticasProdutoRequest("Marca", "Marca 1"))
				.comCaracteristica(new CaractesticasProdutoRequest("Modelo", "Modelo 1"))
				.comCaracteristica(new CaractesticasProdutoRequest("Cor", "Cor 1"))
				.constroi();
		em.persist(produto);
		
		NovaOpiniaoProdutoRequest request = new NovaOpiniaoProdutoRequestBuilder()
				.comTitulo("Primeira opniao")
				.comDescricao("Essa é minha opnião")
				.comNota(5).constroi();
		
		mockMvc.perform(
				post("/produtos/{id}/opinioes", produto.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andDo(print())
			.andExpect(status().isForbidden());
		
	}
	
	@Transactional
	@Test
	public void naoDeveCadastrarUmaOpiniaoDoProdutoComDadosInvalidos() throws Exception {
		Categoria categoria = new CategoriaBuilder().comNome("Categoria 1").constroi();
		em.persist(categoria);
		
		Usuario dono = new UsuarioBuilder().comLogin("dono@email.com").comSenha("123456").constroi();
		em.persist(dono);
		
		Produto produto = new ProdutoBuilder()
				.comNome("Produto 1")
				.comValor("29.9")
				.comQuantidade(10)
				.comDescricao("Descricao 1")
				.comCategoria(categoria)
				.comUsuario(dono)
				.comCaracteristica(new CaractesticasProdutoRequest("Marca", "Marca 1"))
				.comCaracteristica(new CaractesticasProdutoRequest("Modelo", "Modelo 1"))
				.comCaracteristica(new CaractesticasProdutoRequest("Cor", "Cor 1"))
				.constroi();
		em.persist(produto);
		
		NovaOpiniaoProdutoRequest request = new NovaOpiniaoProdutoRequestBuilder()
				.comTitulo(null)
				.comDescricao(null)
				.comNota(0).constroi();
		
		mockMvc.perform(
				post("/produtos/{id}/opinioes", produto.getId())
				.contentType(MediaType.APPLICATION_JSON)
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
