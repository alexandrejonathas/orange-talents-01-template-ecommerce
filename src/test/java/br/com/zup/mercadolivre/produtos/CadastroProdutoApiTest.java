package br.com.zup.mercadolivre.produtos;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;

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
import br.com.zup.mercadolivre.databuilder.NovoProdutoRequestBuilder;
import br.com.zup.mercadolivre.security.TokenService;
import br.com.zup.mercadolivre.usuarios.Usuario;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class CadastroProdutoApiTest {

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
	public void deveObterUmStatus403() throws Exception {
		Categoria categoria = new CategoriaBuilder().comNome("Categoria 1").constroi();
		em.persist(categoria);
		
		var caracteristicas = new ArrayList<CaractesticasProdutoRequest>();
		caracteristicas.add(new CaractesticasProdutoRequest("Marca", "Marca 1"));
		caracteristicas.add(new CaractesticasProdutoRequest("Modelo", "Modelo 1"));
		caracteristicas.add(new CaractesticasProdutoRequest("Cor", "Cor 1"));
		
		NovoProdutoRequest request = new NovoProdutoRequestBuilder()
				.comNome("Produto 1").comValor("29.90")
				.comQuantidade(10).comDescricao("Produto novo")
				.comCategoriaId(categoria.getId())
				.comCaracteristicas(caracteristicas)
				.constroi();

		mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isForbidden());
	}

	@Test
	public void deveCadastrarUmProduto() throws Exception {

		Categoria categoria = new CategoriaBuilder().comNome("Categoria 1").constroi();
		em.persist(categoria);
		
		var caracteristicas = new ArrayList<CaractesticasProdutoRequest>();
		caracteristicas.add(new CaractesticasProdutoRequest("Marca", "Marca 1"));
		caracteristicas.add(new CaractesticasProdutoRequest("Modelo", "Modelo 1"));
		caracteristicas.add(new CaractesticasProdutoRequest("Cor", "Cor 1"));
		
		NovoProdutoRequest request = new NovoProdutoRequestBuilder()
				.comNome("Produto 1").comValor("29.90")
				.comQuantidade(10).comDescricao("Produto novo")
				.comCategoriaId(categoria.getId())
				.comCaracteristicas(caracteristicas)
				.constroi();
		
		String token = getToken();
		
		mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token).content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isOk());
		
		Query query = em.createQuery("select p from Produto p where p.nome = :nome");
		query.setParameter("nome", "Produto 1");
		
		Produto produto = (Produto) query.getSingleResult();
		
		assertNotNull(produto);
		assertEquals("Produto 1", produto.getNome());		
		
	}
	
	@Test
	public void naoDeveCadastrarUmProdutoComDadosInvalidos() throws Exception {

		NovoProdutoRequest request = new NovoProdutoRequestBuilder().comNome("").comValor("0")
				.comQuantidade(-1).comDescricao("").comCategoriaId(null).constroi();
		
		String token = getToken();
		
		mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token).content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.fieldErrors[?(@.field == 'nome')].message").value("must not be blank"))
				.andExpect(jsonPath("$.fieldErrors[?(@.field == 'valor')].message").value("must be greater than or equal to 0.01"))
				.andExpect(jsonPath("$.fieldErrors[?(@.field == 'quantidade')].message").value("must be greater than or equal to 0"))
				.andExpect(jsonPath("$.fieldErrors[?(@.field == 'descricao')].message").value("must not be blank"))
				.andExpect(jsonPath("$.fieldErrors[?(@.field == 'categoriaId')].message").value("must not be null"));
	}
	
	@Test
	public void naoDeveCadastrarUmProdutoComCaracteristicasNull() throws Exception {

		Categoria categoria = new CategoriaBuilder().comNome("Categoria 1").constroi();
		em.persist(categoria);
		
		NovoProdutoRequest request = new NovoProdutoRequestBuilder()
				.comNome("Produto 1").comValor("29.90")
				.comQuantidade(10).comDescricao("Produto novo")
				.comCategoriaId(categoria.getId())
				.constroi();
		
		String token = getToken();
		
		mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.fieldErrors..message", hasItem("must not be null")));
	}
	
	@Test
	public void naoDeveCadastrarUmProdutoComCaracteristicasVazia() throws Exception {

		Categoria categoria = new CategoriaBuilder().comNome("Categoria 1").constroi();
		em.persist(categoria);
		
		NovoProdutoRequest request = new NovoProdutoRequestBuilder()
				.comNome("Produto 1").comValor("29.90")
				.comQuantidade(10).comDescricao("Produto novo")
				.comCategoriaId(categoria.getId())
				.comCaracteristicas(Collections.emptyList())
				.constroi();
		
		String token = getToken();
		
		mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.fieldErrors..message", hasItem("size must be between 3 and 2147483647")));
	}	
	
	@Test
	public void naoDeveCadastrarUmProdutoComCaracteristicasInvalidas() throws Exception {

		Categoria categoria = new CategoriaBuilder().comNome("Categoria 1").constroi();
		em.persist(categoria);

		var caracteristicas = new ArrayList<CaractesticasProdutoRequest>();
		caracteristicas.add(new CaractesticasProdutoRequest("", "Marca 1"));
		caracteristicas.add(new CaractesticasProdutoRequest("Modelo", ""));
		
		NovoProdutoRequest request = new NovoProdutoRequestBuilder()
				.comNome("Produto 1").comValor("29.90")
				.comQuantidade(10).comDescricao("Produto novo")
				.comCategoriaId(categoria.getId())
				.comCaracteristicas(caracteristicas)
				.constroi();
		
		String token = getToken();
		
		mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token)
				.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.fieldErrors..message", hasItem("must not be blank")));
	}		

	private String getToken() {
		Usuario usuario = new Usuario("user@email.com", "123456");
		em.persist(usuario);

		AutenticacaoRequest loginRequest = new AutenticacaoRequest("user@email.com", "123456");
		Authentication authentication = authManager.authenticate(loginRequest.converter());
		String token = tokenService.gerar(authentication);
		return token;
	}

}
