package br.com.zup.mercadolivre.usuarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class CadastroUsuarioApiTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;	
	
	@PersistenceContext
	private EntityManager em;
	
	@Test
	public void deveCadastrarUmUsuario() throws Exception {
		NovoUsuarioRequest request = new NovoUsuarioRequest("user@email.com", "123456");
		this.mockMvc.perform(
				post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andDo(print())
			.andExpect(status().isOk());
		
		Query query = em.createQuery("select u from Usuario u where u.login = :login");
		query.setParameter("login", "user@email.com");
		
		Usuario usuario = (Usuario) query.getSingleResult();
		
		assertNotNull(usuario);
		assertEquals("user@email.com", usuario.getLogin());
		assertNotNull(usuario.getSenha());
	}
	
	@Test
	public void naoDeveCadastrarUmUsuarioSemLoginESenha() throws Exception {
		NovoUsuarioRequest request = new NovoUsuarioRequest(null, null);
		this.mockMvc.perform(
				post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[?(@.field == 'login')].message").value("must not be blank"))
			.andExpect(jsonPath("$.fieldErrors[?(@.field == 'senha')].message").value("must not be blank"));
	}
	
	@Test
	public void naoDeveCadastrarUmUsuarioComEmailInvalido() throws Exception {
		NovoUsuarioRequest request = new NovoUsuarioRequest("emailinvalido.com", "123456");
		this.mockMvc.perform(
				post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[?(@.field == 'login')].message").value("must be a well-formed email address"));		
	}
	
	@Test
	public void naoDeveCadastrarUmUsuarioComSenhaMenorQueSeisCaracteres() throws Exception {
		NovoUsuarioRequest request = new NovoUsuarioRequest("senhamenorque6@email.com", "123");
		this.mockMvc.perform(
				post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[?(@.field == 'senha')].message").value("size must be between 6 and 2147483647"));		
	}	

	@Test
	@Transactional
	public void naoDeveCadastrarUmUsuarioComEmailDuplicado() throws Exception {
		Usuario usuario = new Usuario("duplicado@email.com", "123456");
		em.persist(usuario);		
		
		NovoUsuarioRequest request = new NovoUsuarioRequest("duplicado@email.com", "123456");
		
		this.mockMvc.perform(
				post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))				
			)
		.andDo(print())
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.fieldErrors[?(@.field=='login')].message").value("login ja existe"));		
		
	}
}
