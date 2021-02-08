package br.com.zup.mercadolivre.exceptionhandler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;

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
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public ValidationErrorOutput handleMethodArgumentNotValidException(BindException ex) {
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
	
	
	/*private ResponseEntity<Object> tratarInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = joiningPath(ex.getPath());

		ProblemaTipo problemaTipo = ProblemaTipo.MENSAGEM_INCOMPREENSIVEL;
		String detalhe = String.format(
				"A propriedade '%s' recebeu o valor '%s', "
						+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());

		Problem problema = criarProblemaBuilder(status, problemaTipo, detalhe)
				.message(MSG_ERRO_GENERICO_USUARIO_FINAL).build();

		return handleExceptionInternal(ex, problema, headers, status, request);
	}

	private ResponseEntity<Object> tratarIgnoredPropertyException(PropertyBindingException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		String path = joiningPath(ex.getPath());

		String detalhe = String.format(
				"A propriedade %s não existe. " + "Corrija ou remova essa propriedade e tente novamente.", path);
		Problem problema = criarProblemaBuilder(status, problemaTipo, detalhe)
				.message(MSG_ERRO_GENERICO_USUARIO_FINAL).build();

		return handleExceptionInternal(ex, problema, headers, status, request);
	}*/

	private String joiningPath(List<Reference> references) {
		return references.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
	}	
	
}
