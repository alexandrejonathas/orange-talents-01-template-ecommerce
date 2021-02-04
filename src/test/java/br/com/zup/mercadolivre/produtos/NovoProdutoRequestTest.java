package br.com.zup.mercadolivre.produtos;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import br.com.zup.mercadolivre.caracteristicas.CaractesticasProdutoRequest;
import br.com.zup.mercadolivre.databuilder.NovoProdutoRequestBuilder;

public class NovoProdutoRequestTest {

	@DisplayName("Cria produto com diversos tipos de categorias")
	@ParameterizedTest
	@MethodSource("geradorTeste1")
	public void teste1(int esperado, List<CaractesticasProdutoRequest> caracteristicas) {
		NovoProdutoRequest request = new NovoProdutoRequestBuilder()
				.comNome("Produto 1")
				.comCaracteristicas(caracteristicas).constroi();
				
		
		Assertions.assertEquals(esperado, request.bucaNomeCaracteristicasIguais().size());
	}
	
	public static Stream<Arguments> geradorTeste1() {
		return Stream.of(
				Arguments.of(0, List.of()),
				Arguments.of(0, List.of(new CaractesticasProdutoRequest("key1", "value1"))),
				Arguments.of(0, List.of(new CaractesticasProdutoRequest("key1", "value1"), 
						new CaractesticasProdutoRequest("key2", "value2"))),
				Arguments.of(1, List.of(new CaractesticasProdutoRequest("key1", "value1"), 
						new CaractesticasProdutoRequest("key1", "value1")))
			);
	}
	
}
