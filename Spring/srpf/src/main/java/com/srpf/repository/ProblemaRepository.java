package com.srpf.repository;

import com.srpf.entity.Problema;
import com.srpf.enums.Nivel;
import com.srpf.enums.Tema;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProblemaRepository extends JpaRepository<Problema, Integer> {

  @EntityGraph(attributePaths = "autor")
  List<Problema> findByTema(Tema tema);

  @EntityGraph(attributePaths = "autor")
  List<Problema> findByNivel(Nivel nivel);

  @EntityGraph(attributePaths = "autor")
  List<Problema> findByTemaAndNivel(Tema tema, Nivel nivel);

  @EntityGraph(attributePaths = "autor")
  Optional<Problema> findWithAutorById(Integer id);

  @EntityGraph(attributePaths = "autor")
  @Query("""
      SELECT p FROM Problema p
      WHERE (:tema IS NULL OR p.tema = :tema)
        AND (:nivel IS NULL OR p.nivel = :nivel)
      ORDER BY p.fechaCreacion DESC
      """)
  List<Problema> filtrar(@Param("tema") Tema tema, @Param("nivel") Nivel nivel);
}
