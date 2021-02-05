package br.com.zup.mercadolivre.produtos;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.com.zup.mercadolivre.caracteristicas.CaractesticasProdutoRequest;
import br.com.zup.mercadolivre.categorias.Categoria;
import br.com.zup.mercadolivre.databuilder.CategoriaBuilder;
import br.com.zup.mercadolivre.databuilder.ProdutoBuilder;
import br.com.zup.mercadolivre.databuilder.UsuarioBuilder;
import br.com.zup.mercadolivre.perguntas.Pergunta;
import br.com.zup.mercadolivre.usuarios.Usuario;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class DetalheProdutoApiTest {

	@Autowired
	private MockMvc mockMvc;

	@PersistenceContext
	private EntityManager em;	
	
	@Test
	public void deveReceberOsDetalhesDeUmProduto() throws Exception {
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

		Usuario interessado = new UsuarioBuilder().comLogin("interessado@email.com").comSenha("123456").constroi();
		em.persist(interessado);		
		
		Pergunta pergunta = new Pergunta("Pergunta 1", produto, interessado);
		em.persist(pergunta);
		
		//TODO remover esse clear e implementar o método associaPergunta ao produto
		em.clear();
		
		mockMvc.perform(
				get("/produtos/{id}", produto.getId())
			)
			.andDo(print())
			.andExpect(status().isOk());
		
		Produto produtoSalvo = em.find(Produto.class, produto.getId());
		
		assertTrue(produtoSalvo.getPerguntas().size() > 0);
	}
	
}
