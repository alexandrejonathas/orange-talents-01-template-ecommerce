package br.com.zup.mercadolivre.produtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import br.com.zup.mercadolivre.caracteristicas.CaractesticasProdutoRequest;
import br.com.zup.mercadolivre.categorias.Categoria;
import br.com.zup.mercadolivre.usuarios.Usuario;

public class ProdutoTest {

	@DisplayName("O produto precisa ter no mínimo 3 caracteristicas")
	@ParameterizedTest
	@MethodSource("geradorTeste1")
	public void teste1(List<CaractesticasProdutoRequest> caracteristicas) {
		Categoria categoria = new Categoria("Teste 1");
		Usuario usuario = new Usuario("dono@email.com", "123456");
		
		new Produto("Produto 1", new BigDecimal("29.9"), 10, "Descricao 1", usuario, categoria, caracteristicas);
	}
	
	public static Stream<Arguments> geradorTeste1() {
		return Stream.of(
				Arguments.of(
						List.of(new CaractesticasProdutoRequest("Marca", "Marca 1"),
								new CaractesticasProdutoRequest("Modelo", "Modelo 1"),
								new CaractesticasProdutoRequest("Cor", "Cor 1")),
						List.of(new CaractesticasProdutoRequest("Marca", "Marca 1"),
								new CaractesticasProdutoRequest("Modelo", "Modelo 1"),
								new CaractesticasProdutoRequest("Cor", "Cor 1"),
								new CaractesticasProdutoRequest("Tamanho", "Tamanho 1"))
					)
			);
	}
	
	@DisplayName("O produto não pode ser criado com menos de 3 caracteristicas")
	@ParameterizedTest
	@MethodSource("geradorTeste2")
	public void teste2(List<CaractesticasProdutoRequest> caracteristicas) {
		Categoria categoria = new Categoria("Teste 1");
		Usuario usuario = new Usuario("dono@email.com", "123456");
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> 
		new Produto("Produto 1", new BigDecimal("29.9"), 10, "Descricao 1", usuario, categoria, caracteristicas));
	}	
	public static Stream<Arguments> geradorTeste2() {
		return Stream.of(
				Arguments.of(
						List.of(new CaractesticasProdutoRequest("Marca", "Marca 1"),
								new CaractesticasProdutoRequest("Modelo", "Modelo 1")),
						
						List.of(new CaractesticasProdutoRequest("Marca", "Marca 1"))
					)
			);
	}	
	
}
