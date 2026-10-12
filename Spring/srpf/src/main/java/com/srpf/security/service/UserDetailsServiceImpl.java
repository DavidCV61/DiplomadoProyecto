package com.srpf.security.service;

import com.srpf.entity.Usuario;
import com.srpf.repository.UsuarioRepository;
import com.srpf.security.model.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UsuarioRepository usuarioRepository;

  @Autowired
  public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("Security - UserDetailsServiceImpl.loadUserByUsername {}", username);
    String normalizado = username == null ? "" : username.trim().toLowerCase(Locale.ROOT);
    Usuario usuario = usuarioRepository.findByUsername(normalizado)
        .orElseThrow(() -> new UsernameNotFoundException("User not found in database"));
    return new UserDetailsImpl(usuario);
  }
}
