package br.com.zup.mercadolivre.validation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
		if(id == null) {			
			return true;
		}
		Query query = em.createQuery("select 1 from "+domainClass.getName()+" where id = :id");
		query.setParameter("id", id);
		return query.getFirstResult() > 0;
	}

}
