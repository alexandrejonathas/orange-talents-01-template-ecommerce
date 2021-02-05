package br.com.zup.mercadolivre.produtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import br.com.zup.mercadolivre.caracteristicas.Caracteristica;
import br.com.zup.mercadolivre.caracteristicas.CaracteristicaResponse;
import br.com.zup.mercadolivre.fotos.Foto;
import br.com.zup.mercadolivre.fotos.FotoResponse;
import br.com.zup.mercadolivre.opinioes.Opiniao;
import br.com.zup.mercadolivre.opinioes.OpiniaoResponse;
import br.com.zup.mercadolivre.perguntas.Pergunta;
import br.com.zup.mercadolivre.perguntas.PerguntaResponse;

public class DetalheProdutoResponse {

	private String nome;

	private String descricao;

	private BigDecimal valor;

	private Integer quantidade;

	private List<FotoResponse> fotos;
	
	private List<PerguntaResponse> perguntas;

	private List<CaracteristicaResponse> caracteristicas;	

	private List<OpiniaoResponse> opinioes;
	
	public DetalheProdutoResponse(Produto produto) {
		this.nome = produto.getNome();
		this.descricao = produto.getDescricao();
		this.valor = produto.getValor();
		this.quantidade = produto.getQuantidade();
		this.fotos = buildFotos(produto.getFotos());
		this.caracteristicas = buildCaracteristicas(produto.getCaracteristicas());
		this.opinioes = buildOpinioes(produto.getOpinioes());
		this.perguntas = buildPerguntas(produto.getPerguntas());
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public List<FotoResponse> getFotos() {
		return fotos;
	}

	public List<CaracteristicaResponse> getCaracteristicas() {
		return caracteristicas;
	}

	public List<OpiniaoResponse> getOpinioes() {
		return opinioes;
	}

	public List<PerguntaResponse> getPerguntas() {
		return perguntas;
	}
	
	public int getTotalDasNotas() {
		return this.opinioes.stream().map(o -> o.getNota()).reduce(0, (subtotal, nota) -> subtotal + nota);
	}
	
	public double getMediaDasNotas() {
		if(opinioes.isEmpty())
			return 0;
		return new BigDecimal(getTotalDasNotas()).divide(new BigDecimal(opinioes.size())).doubleValue();
	}	
	
	private List<FotoResponse> buildFotos(List<Foto> fotos) {
		return fotos.stream().map(FotoResponse::new).collect(Collectors.toList());
	}
	
	private List<CaracteristicaResponse> buildCaracteristicas(List<Caracteristica> caracteristicas) {
		return caracteristicas.stream().map(CaracteristicaResponse::new).collect(Collectors.toList());
	}
	
	private List<OpiniaoResponse> buildOpinioes(List<Opiniao> opinioes) {
		return opinioes.stream().map(OpiniaoResponse::new).collect(Collectors.toList());
	}
	
	private List<PerguntaResponse> buildPerguntas(List<Pergunta> perguntas) {
		return perguntas.stream().map(PerguntaResponse::new).collect(Collectors.toList());
	}
}
