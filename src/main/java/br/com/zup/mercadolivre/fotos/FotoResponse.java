package br.com.zup.mercadolivre.fotos;

public class FotoResponse {

	private String url;

	public FotoResponse(Foto foto) {
		this.url = foto.getUrl();
	}

	public String getUrl() {
		return url;
	};
}
