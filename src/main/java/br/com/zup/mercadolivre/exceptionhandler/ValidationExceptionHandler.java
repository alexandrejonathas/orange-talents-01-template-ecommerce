package br.com.zup.mercadolivre.exceptionhandler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ValidationErrorOutput handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		var globalErrors = ex.getBindingResult().getGlobalErrors();
		var fieldErrors = ex.getBindingResult().getFieldErrors();

		return buildValidationErrorOutput(globalErrors, fieldErrors);
	}
	
	private ValidationErrorOutput buildValidationErrorOutput(List<ObjectError> globalErrors, 
			List<FieldError> fieldErrors) {
		ValidationErrorOutput validationErrorOutput = new ValidationErrorOutput();
		
		globalErrors.forEach(e -> validationErrorOutput.addError(getErrorMessage(e)));
		
		fieldErrors.forEach(e -> validationErrorOutput.addFieldError(e.getField(), getErrorMessage(e)));
		
		return validationErrorOutput;
	}
	
	private String getErrorMessage(ObjectError error) {
		return messageSource.getMessage(error, LocaleContextHolder.getLocale());
	}
}
