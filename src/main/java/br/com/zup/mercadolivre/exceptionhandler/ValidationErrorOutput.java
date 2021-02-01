package br.com.zup.mercadolivre.exceptionhandler;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorOutput {

	private List<String> globalErrorMessages = new ArrayList<>();
	private List<FieldErrorOutput> fieldErrors = new ArrayList<>();

	public void addError(String message) {
		this.globalErrorMessages.add(message);
	}

	public void addFieldError(String field, String message) {
		FieldErrorOutput fieldError = new FieldErrorOutput(field, message);
		fieldErrors.add(fieldError);
	}

	public List<String> getGlobalErrorMessages() {
		return globalErrorMessages;
	}

	public List<FieldErrorOutput> getFieldErrors() {
		return fieldErrors;
	}

}
