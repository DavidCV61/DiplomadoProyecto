package com.srpf.repository;

import com.srpf.entity.ForoRespuesta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ForoRespuestaRepository extends JpaRepository<ForoRespuesta, Long> {

  @Query("SELECT r FROM ForoRespuesta r JOIN FETCH r.usuario WHERE r.publicacion.id = :id ORDER BY r.fechaRespuesta ASC")
  List<ForoRespuesta> porPublicacion(@Param("id") Long id);

  @Query("SELECT r.publicacion.id, COUNT(r) FROM ForoRespuesta r GROUP BY r.publicacion.id")
  List<Object[]> contarPorPublicacion();
}
