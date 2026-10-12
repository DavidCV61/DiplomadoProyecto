package com.srpf.repository;

import com.srpf.entity.ForoPublicacion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ForoPublicacionRepository extends JpaRepository<ForoPublicacion, Long> {

  @Query("SELECT p FROM ForoPublicacion p JOIN FETCH p.usuario ORDER BY p.fechaPublicacion DESC")
  List<ForoPublicacion> listar();

  @Query("SELECT p FROM ForoPublicacion p JOIN FETCH p.usuario WHERE p.id = :id")
  Optional<ForoPublicacion> buscar(@Param("id") Long id);
}
