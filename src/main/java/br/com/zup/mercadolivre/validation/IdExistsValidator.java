package br.com.zup.mercadolivre.validation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdExistsValidator implements ConstraintValidator<IdExists, Long> {

	@PersistenceContext
	private EntityManager em;

	private Class<?> domainClass;
	
	@Override
	public void initialize(IdExists params) {
		this.domainClass = params.domainClass();
	}
	
	@Override
	public boolean isValid(Long id, ConstraintValidatorContext context) {
		if(id == null)
			return true;
		return em.find(domainClass, id) != null;
	}

}
