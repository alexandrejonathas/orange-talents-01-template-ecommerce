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

@ExtendWith(MockitoExtension.class)
public class IdExistsValidatorTest {

	@Mock
	private EntityManager em;
	
	@Mock
	private Query query;
	
	@InjectMocks
	private IdExistsValidator idExistsValidator;
	
	private Long domainClassId = 1L;
	
	@BeforeEach
	public void setUp() {		
		var idExists = getIdExists();
		idExistsValidator.initialize(idExists);
		when(em.createQuery("select 1 from "+idExists.domainClass().getName()+" where id = :id"))
			.thenReturn(query);
	}
	
	@Test
	public void deveRetornarTrueQuandoIdExistir() {
		when(query.getResultList()).thenReturn(Arrays.asList(new DomainClass()));
		assertTrue(idExistsValidator.isValid(domainClassId, null));
		Mockito.verify(query).setParameter("id", domainClassId);
	}
	
	@Test
	public void deveRetornarFalseQuandoIdNaoExistir() {
		when(query.getResultList()).thenReturn(Collections.emptyList());
		assertFalse(idExistsValidator.isValid(domainClassId, null));
		Mockito.verify(query).setParameter("id", domainClassId);
	}	
	
	class DomainClass {
		
	}
	
	private IdExists getIdExists() {
		return new IdExists() {
			
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
			public Class<?> domainClass() {
				return DomainClass.class;
			}
		};
	}
	
}
