package br.com.zup.mercadolivre.pedidos;

public enum FormaPagamento {

	/**
	 * paypal.com/{idGeradoDaCompra}?redirectUrl={urlRetornoAppPosPagamento}
	 * pagseguro.com?returnId={idGeradoDaCompra}&redirectUrl={urlRetornoAppPosPagamento}
	 */
	
	PAYPAL("paypal.com"), PAGSEGURO("pagseguro.com");
	
	private String url;
	
	private FormaPagamento(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getUrlRetorno(String identificadorPedido, String urlRetornoAppPosPagamento) {
		return this.getUrlByGateway()+identificadorPedido+"&redirectUrl="+urlRetornoAppPosPagamento;
	}
	
	private String getUrlByGateway( ) {
		return PAYPAL.equals(this) ? this.url+"/" : this.url+"?returnId=";
	}
}
