package com.srpf.repository;

import com.srpf.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface PerfilRepository extends Repository<Usuario, Integer> {

  @Query("SELECT u FROM Usuario u WHERE :q = '' OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%')) ORDER BY u.puntuacionElo DESC, u.nombre ASC")
  List<Usuario> buscar(@Param("q") String q);

  @Query("SELECT u FROM Usuario u WHERE u.username = :username")
  Optional<Usuario> porUsername(@Param("username") String username);
}
