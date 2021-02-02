package br.com.zup.mercadolivre.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UniqueValueValidator.class)
@Retention(RUNTIME)
@Target(FIELD)
public @interface UniqueValue {

	String message() default "{br.com.zup.mercadolivre.uniquevalue}";
	
	Class<?>[] groups() default { };
	
	Class<? extends Payload>[] payload() default { };
	
	Class<?> domainClass();
	
	String fieldName();
}
