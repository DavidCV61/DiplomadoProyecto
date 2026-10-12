package com.srpf.service.impl;

import com.srpf.dto.PerfilResponse;
import com.srpf.dto.RegistroRequest;
import com.srpf.entity.Usuario;
import com.srpf.enums.Rol;
import com.srpf.exception.UsuarioDuplicadoException;
import com.srpf.exception.UsuarioNotFoundException;
import com.srpf.repository.UsuarioRepository;
import com.srpf.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService {
  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional(readOnly = true)
  public PerfilResponse obtenerPerfil(String username) throws UsuarioNotFoundException {
    log.info("UsuarioService - obtenerPerfil {}", username);
    Usuario usuario = usuarioRepository.findByUsername(username)
        .orElseThrow(() -> new UsuarioNotFoundException(username));
    return new PerfilResponse(
        usuario.getNombre(),
        usuario.getUsername(),
        usuario.getEmail(),
        usuario.getRol().name(),
        usuario.getPuntuacionElo(),
        usuario.getFechaRegistro());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void registrar(RegistroRequest request) throws UsuarioDuplicadoException {
    String username = request.getUsername().trim().toLowerCase(Locale.ROOT);
    String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
    log.info("UsuarioService - registrar {}", username);

    if (usuarioRepository.existsByUsername(username)) {
      throw new UsuarioDuplicadoException("username", username);
    }
    if (usuarioRepository.existsByEmail(email)) {
      throw new UsuarioDuplicadoException("email", email);
    }

    // El rol y el ELO los asigna el servidor, nunca el formulario
    Usuario usuario = new Usuario(
        request.getNombre().trim(),
        username,
        email,
        passwordEncoder.encode(request.getPassword()),
        Rol.USER);
    try {
      usuarioRepository.saveAndFlush(usuario);
    } catch (DataIntegrityViolationException e) {
      // Red de seguridad: dos registros simultaneos que pasaron el exists
      log.warn("UsuarioService - registro duplicado por concurrencia: {}", username);
      throw new UsuarioDuplicadoException("username", username);
    }
  }
}
