package br.com.zup.mercadolivre.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Payload;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Classe desenvolvida com o apoio do Tiago de Freitas
 */
@ExtendWith(MockitoExtension.class)
public class UniqueValueValidatorTest {
	
	@Mock
	private EntityManager em;

	@Mock
	private Query query; 
	
	@InjectMocks
	private UniqueValueValidator uniqueValueValidator;
	
	private String fieldValue = "aaaaa";
	
	@BeforeEach
	public void setUp() {
		var uniqueValue = getUniqueValue();
		uniqueValueValidator.initialize(uniqueValue);
		when(em.createQuery("select 1 from "+DomainClass.class.getName()+" where "+uniqueValue.fieldName()+"=:value"))
		.thenReturn(query);
	}
	
	@Test
	public void deveRetornarTrueAoChegarValorInexistente() {
		when(query.getResultList()).thenReturn(Collections.emptyList());
		assertTrue(uniqueValueValidator.isValid(fieldValue, null));
		Mockito.verify(query).setParameter("value", fieldValue);
	}
	
	@Test
	public void deveRetornarFalseAoChegarValorExistente() {
		when(query.getResultList()).thenReturn(Arrays.asList(new DomainClass()));
		assertFalse(uniqueValueValidator.isValid(fieldValue, null));
		Mockito.verify(query).setParameter("value", fieldValue);
	}	
	
	class DomainClass {
		
	}

	private UniqueValue getUniqueValue() {
		return new UniqueValue() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}
			
			@Override
			public Class<? extends Payload>[] payload() {
				return null;
			}
			
			@Override
			public String message() {
				return null;
			}
			
			@Override
			public Class<?>[] groups() {
				return null;
			}
			
			@Override
			public String fieldName() {
				return "nome";
			}
			
			@Override
			public Class<?> domainClass() {
				return DomainClass.class;
			}
		};
	}
	
}
