package br.com.zup.mercadolivre.mailer;

public interface Mailer {
	void send(String template, String subject, String body, String from, String to);
}
