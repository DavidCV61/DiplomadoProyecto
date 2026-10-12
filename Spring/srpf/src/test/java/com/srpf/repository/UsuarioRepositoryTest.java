package com.srpf.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.srpf.entity.Usuario;
import com.srpf.enums.Rol;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.security.SecureRandom;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
class UsuarioRepositoryTest {
  private final TestEntityManager testEntityManager;
  private final UsuarioRepository usuarioRepository;

  @Autowired
  public UsuarioRepositoryTest(TestEntityManager testEntityManager, UsuarioRepository usuarioRepository) {
    this.testEntityManager = testEntityManager;
    this.usuarioRepository = usuarioRepository;
  }

  private void crearUsuario(String nombre, String username, String email, String password, Rol rol) {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11, new SecureRandom());
    if (!usuarioRepository.existsByUsername(username)) {
      usuarioRepository.save(new Usuario(nombre, username, email, passwordEncoder.encode(password), rol));
    }
    Usuario existente = usuarioRepository.findByUsername(username).orElseThrow();
    Usuario encontrado = testEntityManager.find(Usuario.class, existente.getId());
    assertThat(encontrado.getUsername()).isEqualTo(username);
    assertThat(encontrado.getRol()).isEqualTo(rol);
    assertThat(encontrado.getPasswordHash()).startsWith("$2a$");
  }

  @Test
  public void testCreateUser() {
    crearUsuario("Usuario Demo", "usuario", "usuario@srpf.local", "usuario123", Rol.USER);
  }

  @Test
  public void testCreateAutor() {
    crearUsuario("Autor Demo", "autor", "autor@srpf.local", "autor123", Rol.AUTOR);
  }

  @Test
  public void testCreateAdmin() {
    crearUsuario("Administrador", "admin", "admin@srpf.local", "admin123", Rol.ADMIN);
  }
}
