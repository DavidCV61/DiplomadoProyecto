package com.srpf.repository;

import com.srpf.entity.Intento;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IntentoRepository extends JpaRepository<Intento, Long> {

  @Query("SELECT i FROM Intento i JOIN FETCH i.problema WHERE i.usuario.id = :uid ORDER BY i.fechaHora DESC")
  List<Intento> porUsuario(@Param("uid") Integer uid);

  @Query("SELECT i FROM Intento i JOIN FETCH i.problema JOIN FETCH i.usuario ORDER BY i.fechaHora DESC")
  List<Intento> recientes(Pageable pageable);

  @Query("SELECT u.nombre, u.username, u.puntuacionElo FROM Usuario u ORDER BY u.puntuacionElo DESC, u.fechaRegistro ASC")
  List<Object[]> ranking();
}
