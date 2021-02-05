package br.com.zup.mercadolivre.pedidos;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import br.com.zup.mercadolivre.databuilder.NovoPedidoRequestBuilder;
import br.com.zup.mercadolivre.databuilder.ProdutoBuilder;
import br.com.zup.mercadolivre.databuilder.UsuarioBuilder;
import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.security.TokenService;
import br.com.zup.mercadolivre.usuarios.Usuario;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class CadastraPedidoApi {

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
	public void deveCadastrarUmPedido() throws Exception {
		Usuario dono = new UsuarioBuilder().comLogin("dono@email.com").comSenha("123456").constroi();
		em.persist(dono);
		
		Categoria categoria = new CategoriaBuilder().comNome("Categoria 1").constroi();
		em.persist(categoria);		
		
		Produto produto1 = criaProduto("Produto 1", "29.9", 10, dono, categoria);
		Produto produto2 = criaProduto("Produto 2", "24.9", 10, dono, categoria);
		
		NovoPedidoRequest request = new NovoPedidoRequestBuilder()
				.comFormaPagamento(FormaPagamento.PAYPAL)
				.addProduto(new ProdutoPedidoRequest(produto1.getId(), 1))
				.addProduto(new ProdutoPedidoRequest(produto2.getId(), 5))
				.constroi();
		
		String login = "comprador@email.com";
		String senha = "123456";
		
		Usuario comprador = new UsuarioBuilder().comLogin(login).comSenha(senha).constroi();
		em.persist(comprador);
		
		String token = getToken(login, senha);
		
		mockMvc.perform(
				post("/pedidos")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+token)
				.content(objectMapper.writeValueAsString(request))
			)
		.andDo(print())
		.andExpect(status().is3xxRedirection());
		
	}
	
	@Test
	public void naoDeveCadastrarUmPedidoComQuantidadeSuperiorADisponivel() throws Exception {
		Usuario dono = new UsuarioBuilder().comLogin("dono@email.com").comSenha("123456").constroi();
		em.persist(dono);
		
		Categoria categoria = new CategoriaBuilder().comNome("Categoria 1").constroi();
		em.persist(categoria);		
		
		Produto produto1 = criaProduto("Produto 1", "29.9", 10, dono, categoria);
		
		NovoPedidoRequest request = new NovoPedidoRequestBuilder()
				.comFormaPagamento(FormaPagamento.PAYPAL)
				.addProduto(new ProdutoPedidoRequest(produto1.getId(), 100))
				.constroi();
		
		String login = "comprador@email.com";
		String senha = "123456";
		
		Usuario comprador = new UsuarioBuilder().comLogin(login).comSenha(senha).constroi();
		em.persist(comprador);
		
		String token = getToken(login, senha);
		
		mockMvc.perform(
				post("/pedidos")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+token)
				.content(objectMapper.writeValueAsString(request))
			)
		.andDo(print())
		.andExpect(status().isBadRequest());
		
	}	
	
	
	@Test
	public void naoDeveCadastrarUmPedidoComComProdutoInexistente() throws Exception {
		Usuario dono = new UsuarioBuilder().comLogin("dono@email.com").comSenha("123456").constroi();
		em.persist(dono);
		
		Categoria categoria = new CategoriaBuilder().comNome("Categoria 1").constroi();
		em.persist(categoria);		
		
		criaProduto("Produto 1", "29.9", 10, dono, categoria);
		
		NovoPedidoRequest request = new NovoPedidoRequestBuilder()
				.comFormaPagamento(FormaPagamento.PAYPAL)
				.addProduto(new ProdutoPedidoRequest(1000L, 100))
				.constroi();
		
		String login = "comprador@email.com";
		String senha = "123456";
		
		Usuario comprador = new UsuarioBuilder().comLogin(login).comSenha(senha).constroi();
		em.persist(comprador);
		
		String token = getToken(login, senha);
		
		mockMvc.perform(
				post("/pedidos")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+token)
				.content(objectMapper.writeValueAsString(request))
			)
		.andDo(print())
		.andExpect(status().isBadRequest());
		
	}	
	
	private Produto criaProduto(String nome, String valor, int quantidade, Usuario usuario, Categoria categoria) {
		
		Produto produto = new ProdutoBuilder().comUsuario(usuario).comNome(nome).comValor(valor)
				.comQuantidade(quantidade).comDescricao("Produto novo").comCategoria(categoria)
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
