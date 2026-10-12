package com.srpf.repository;

import com.srpf.entity.Intento;
import com.srpf.enums.ResultadoIntento;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface EstadisticaRepository extends Repository<Intento, Long> {

  @Query("SELECT i.usuario.id, COUNT(i), COUNT(DISTINCT CASE WHEN i.resultado = :ok THEN i.problema.id END) FROM Intento i GROUP BY i.usuario.id")
  List<Object[]> porUsuario(@Param("ok") ResultadoIntento ok);
}
