package br.com.zup.mercadolivre.eventos;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.zup.mercadolivre.perguntas.Pergunta;

@Component
public class PerguntaCriadaListener {

	@EventListener
	public void aoCriar(PerguntaCriadaEvent event) {
		Pergunta pergunta = event.getPergunta();
		System.out.println("Você tem uma nova pergunta sobre o produto "+pergunta.getProduto().getNome());
		System.out.println("Para detalhes acesse: "+event.getUlrDetalheProduto());
		System.out.println(pergunta.getTitulo());
	}
	
}
