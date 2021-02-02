package br.com.zup.mercadolivre.categorias;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class CadastroCategoriaApiTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private EntityManager em;
	
	@Test
	public void deveCadastrarUmaCategoria() throws Exception {
		NovaCategoriaRequest request = new NovaCategoriaRequest("Tecnologia", null);
		this.mockMvc.perform(
				post("/categorias")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void naoDeveCadastrarUmaCategoriaSemUmNome() throws Exception {
		NovaCategoriaRequest request = new NovaCategoriaRequest("", null);
		this.mockMvc.perform(
				post("/categorias")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors[?(@.field=='nome')].message").value("must not be blank"));
	}
		
	@Test
	public void naoDeveCadastrarUmaCategoriaComCategoriaInexistente( ) throws Exception {
		NovaCategoriaRequest request = new NovaCategoriaRequest("Tecnologia", 100L);
		this.mockMvc.perform(
				post("/categorias")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors[?(@.field == 'categoriaId')].message").value("O valor contido em categoriaId nao existe"));
	}
	
	@Transactional
	@Test
	public void naoDeveCadastrarUmaCategoriaComNomeExistente() throws Exception {
		Categoria categoria = new Categoria("Tecnologia", null);
		em.persist(categoria);
		
		NovaCategoriaRequest request = new NovaCategoriaRequest("Tecnologia", null);
		this.mockMvc.perform(
				post("/categorias")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors[?(@.field=='nome')].message").value("nome ja existe"));
	}		
	
	@Transactional
	@Test
	public void deveCadastrarUmaCategoriaComoSubCategoria( ) throws Exception {
		Categoria categoria = new Categoria("Tecnologia", null);		
		em.persist(categoria);
		
		NovaCategoriaRequest request = new NovaCategoriaRequest("Celulares", categoria.getId());
		
		this.mockMvc.perform(
				post("/categorias")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andDo(print())
			.andExpect(status().isOk());
	}	
	
}
