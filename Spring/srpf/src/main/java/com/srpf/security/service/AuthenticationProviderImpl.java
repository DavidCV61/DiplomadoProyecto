package com.srpf.security.service;

import com.srpf.entity.Usuario;
import com.srpf.repository.UsuarioRepository;
import com.srpf.security.model.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationProviderImpl implements AuthenticationProvider {
  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) {
    String username = authentication.getName().trim().toLowerCase(Locale.ROOT);
    String pwd = authentication.getCredentials().toString();
    Usuario usuario = usuarioRepository.findByUsername(username)
        .orElseThrow(() -> {
          log.warn("Security - login fallido, usuario inexistente: {}", username);
          return new BadCredentialsException("Usuario o contraseña incorrectos");
        });
    if (passwordEncoder.matches(pwd, usuario.getPasswordHash())) {
      UserDetailsImpl principal = new UserDetailsImpl(usuario);
      log.info("Security - login correcto: {}", username);
      return UsernamePasswordAuthenticationToken.authenticated(principal, null, principal.getAuthorities());
    } else {
      log.warn("Security - login fallido, contraseña incorrecta: {}", username);
      throw new BadCredentialsException("Usuario o contraseña incorrectos");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
