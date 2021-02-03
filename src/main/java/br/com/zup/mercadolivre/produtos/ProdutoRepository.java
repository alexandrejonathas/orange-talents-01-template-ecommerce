package br.com.zup.mercadolivre.produtos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	@Query("select p from Produto p where p.id = :id and p.usuario.id = :usuarioId")
	Optional<Produto> findByIdAndUsuarioId(@Param("id") Long id, @Param("usuarioId") Long usuarioId);
	
}
