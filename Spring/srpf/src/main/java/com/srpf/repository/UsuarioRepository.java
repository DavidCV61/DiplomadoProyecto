
package com.srpf.repository;

import com.srpf.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

  Optional<Usuario> findByUsername(String username);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
