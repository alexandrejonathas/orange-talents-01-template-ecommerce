package br.com.zup.mercadolivre.validation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.Assert;

public class UniqueValueValidation implements ConstraintValidator<UniqueValue, Object>{

	@PersistenceContext
	private EntityManager em;
	
	private Class<?> domainClass;
	
	private String fieldName;
	
	@Override
	public void initialize(UniqueValue params) {
		this.domainClass = params.domainClass();
		this.fieldName = params.fieldName();
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		var query = em.createQuery("select 1 from "+domainClass.getName()+" where "+fieldName+"=:value");
		query.setParameter("value", value);
		var list = query.getResultList();
		
		Assert.state(list.size() <= 1, "Foi encontrado mais de um "+domainClass+" com o atributo "+fieldName+" = "+value);

		return list.isEmpty();
	}

}
