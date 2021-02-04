package br.com.zup.mercadolivre.eventos;

import br.com.zup.mercadolivre.perguntas.Pergunta;

public class PerguntaCriadaEvent {
	
	private String ulrDetalheProduto;
	private Pergunta pergunta;

	public PerguntaCriadaEvent(String ulrDetalheProduto, Pergunta pergunta) {
		this.ulrDetalheProduto = ulrDetalheProduto;
		this.pergunta = pergunta;
	}

	public String getUlrDetalheProduto() {
		return ulrDetalheProduto;
	}

	public Pergunta getPergunta() {
		return pergunta;
	}

}
