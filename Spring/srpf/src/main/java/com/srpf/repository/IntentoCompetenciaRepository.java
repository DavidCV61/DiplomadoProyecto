package com.srpf.repository;

import com.srpf.entity.Intento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IntentoCompetenciaRepository extends JpaRepository<Intento, Long> {

  @Query("SELECT DISTINCT i.problema.id FROM Intento i WHERE i.competenciaId = :cid AND i.usuario.id = :uid AND i.resultado = com.srpf.enums.ResultadoIntento.CORRECTO")
  List<Integer> resueltos(@Param("cid") Integer cid, @Param("uid") Integer uid);

  @Query("SELECT COUNT(i) FROM Intento i WHERE i.competenciaId = :cid AND i.usuario.id = :uid AND i.problema.id = :pid AND i.resultado = com.srpf.enums.ResultadoIntento.CORRECTO")
  long correctos(@Param("cid") Integer cid, @Param("uid") Integer uid, @Param("pid") Integer pid);
}
