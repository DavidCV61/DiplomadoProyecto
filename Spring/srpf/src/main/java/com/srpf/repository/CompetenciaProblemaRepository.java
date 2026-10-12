package com.srpf.repository;

import com.srpf.entity.CompetenciaProblema;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompetenciaProblemaRepository
    extends JpaRepository<CompetenciaProblema, CompetenciaProblema.PK> {

  @Query("SELECT p.id, p.titulo, cp.orden FROM CompetenciaProblema cp JOIN Problema p ON p.id = cp.problemaId WHERE cp.competenciaId = :cid ORDER BY cp.orden ASC")
  List<Object[]> problemas(@Param("cid") Integer cid);
}
