package br.com.zup.mercadolivre.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = IdExistsValidator.class)
@Retention(RUNTIME)
@Target(FIELD)
public @interface IdExists {

	String message() default "{br.com.zup.mercadolivre.idexists}";
	
	Class<?>[] groups() default { };
	
	Class<? extends Payload>[] payload() default { };
	
	Class<?> domainClass();
}
