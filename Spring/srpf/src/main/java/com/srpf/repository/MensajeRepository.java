package com.srpf.repository;

import com.srpf.entity.Mensaje;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

  @Query("SELECT m FROM Mensaje m JOIN FETCH m.remitente JOIN FETCH m.destinatario WHERE m.remitente.id = :uid OR m.destinatario.id = :uid ORDER BY m.fechaHora DESC, m.id DESC")
  List<Mensaje> deUsuario(@Param("uid") Integer uid);

  @Query("SELECT m FROM Mensaje m JOIN FETCH m.remitente JOIN FETCH m.destinatario WHERE (m.remitente.id = :a AND m.destinatario.id = :b) OR (m.remitente.id = :b AND m.destinatario.id = :a) ORDER BY m.fechaHora ASC, m.id ASC")
  List<Mensaje> entre(@Param("a") Integer a, @Param("b") Integer b);
}
