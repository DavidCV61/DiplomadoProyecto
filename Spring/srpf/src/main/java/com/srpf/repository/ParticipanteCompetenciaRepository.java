package com.srpf.repository;

import com.srpf.entity.ParticipanteCompetencia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParticipanteCompetenciaRepository
    extends JpaRepository<ParticipanteCompetencia, Long> {

  @Query("SELECT p FROM ParticipanteCompetencia p WHERE p.competencia.id = :cid AND p.usuario.id = :uid")
  Optional<ParticipanteCompetencia> buscar(@Param("cid") Integer cid, @Param("uid") Integer uid);

  @Query("SELECT p FROM ParticipanteCompetencia p JOIN FETCH p.usuario WHERE p.competencia.id = :cid")
  List<ParticipanteCompetencia> porCompetencia(@Param("cid") Integer cid);

  @Query("SELECT p.competencia.id, COUNT(p) FROM ParticipanteCompetencia p GROUP BY p.competencia.id")
  List<Object[]> contarPorCompetencia();

  @Query("SELECT p.competencia.id FROM ParticipanteCompetencia p WHERE p.usuario.id = :uid")
  List<Integer> competenciasDe(@Param("uid") Integer uid);
}
