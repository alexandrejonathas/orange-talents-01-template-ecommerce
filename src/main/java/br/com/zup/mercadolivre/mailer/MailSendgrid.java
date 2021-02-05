package br.com.zup.mercadolivre.mailer;

import org.springframework.stereotype.Component;

@Component
public class MailSendgrid implements Mailer {

	@Override
	public void send(String template, String subject, String body, String from, String to)  {
		System.out.println("Não foi implementando o send grid");
	}
}
