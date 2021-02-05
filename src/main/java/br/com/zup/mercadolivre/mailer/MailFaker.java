package br.com.zup.mercadolivre.mailer;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class MailFaker implements Mailer{

	@Override
	public void send(String template, String subject, String body, String from, String to) {
		System.out.println("Subject: "+subject);
		System.out.println("Body: "+body);
		System.out.println("From: "+from);
		System.out.println("To:"+to);
	}

}
