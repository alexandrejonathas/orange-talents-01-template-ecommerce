package br.com.zup.mercadolivre.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.zup.mercadolivre.usuarios.NovoUsuarioRequest;

//TODO pensar em como melhorar essa classe de testes
@ExtendWith(MockitoExtension.class)
public class UniqueValueTest {
	
	private UniqueValueValidator uniqueValueValidator;
	
	private NovoUsuarioRequest request;
	
	@BeforeEach
	public void setUp() {		
		this.uniqueValueValidator = Mockito.mock(UniqueValueValidator.class);		
		this.request = new NovoUsuarioRequest("user@email.com", "123456");
	}
	
	@Test
	public void devePassarAoVerificarUmValorInexistente() {
		Mockito.when(this.uniqueValueValidator.isValid(this.request, null)).thenReturn(true);
		assertTrue(this.uniqueValueValidator.isValid(this.request, null));
	}
	
	@Test
	public void naoDevePassarAoVerificarUmValorExistente() {
		Mockito.when(this.uniqueValueValidator.isValid(this.request, null)).thenReturn(false);
		assertFalse(this.uniqueValueValidator.isValid(this.request, null));
	}	
	
}
