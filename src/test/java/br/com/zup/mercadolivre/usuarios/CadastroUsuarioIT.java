package br.com.zup.mercadolivre.usuarios;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
public class CadastroUsuarioIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;	
	
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
		NovoUsuarioRequest request = new NovoUsuarioRequest("user.com", "123456");
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
		NovoUsuarioRequest request = new NovoUsuarioRequest("user@email.com", "123");
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
	public void naoDeveCadastrarUmUsuarioComEmailDuplicado() throws Exception {
		NovoUsuarioRequest request = new NovoUsuarioRequest("user@email.com", "123456");
		this.mockMvc.perform(
				post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))				
			);
		
		this.mockMvc.perform(
				post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))				
			)
		.andDo(print())
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.fieldErrors[?(@.field=='login')].message").value("login ja existe."));		
		
	}
}
