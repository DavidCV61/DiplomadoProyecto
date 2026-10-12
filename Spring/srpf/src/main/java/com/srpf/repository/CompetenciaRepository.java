package com.srpf.repository;

import com.srpf.entity.Competencia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompetenciaRepository extends JpaRepository<Competencia, Integer> {

  @Query("SELECT c FROM Competencia c JOIN FETCH c.creador ORDER BY c.fechaInicio DESC")
  List<Competencia> listar();

  @Query("SELECT c FROM Competencia c JOIN FETCH c.creador WHERE c.id = :id")
  Optional<Competencia> buscar(@Param("id") Integer id);
}
