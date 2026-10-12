package com.srpf.security.controller;

import com.srpf.security.exception.ExceptionResponse;
import com.srpf.security.jwt.JWTTokenProvider;
import com.srpf.security.model.UserDetailsImpl;
import com.srpf.security.request.JwtResponse;
import com.srpf.security.request.LoginUserRequest;
import com.srpf.security.request.RefreshTokenRequest;
import com.srpf.security.service.AuthenticationProviderImpl;
import com.srpf.security.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationProviderImpl authenticationProvider;
  private final JWTTokenProvider tokenProvider;
  private final UserDetailsServiceImpl userDetailsService;

  @Autowired
  public AuthController(AuthenticationProviderImpl authenticationProvider,
      JWTTokenProvider tokenProvider, UserDetailsServiceImpl userDetailsService) {
    this.authenticationProvider = authenticationProvider;
    this.tokenProvider = tokenProvider;
    this.userDetailsService = userDetailsService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginUserRequest request) {
    try {
      Authentication authentication = authenticationProvider.authenticate(
          UsernamePasswordAuthenticationToken.unauthenticated(request.username(),
              request.password()));
      UserDetailsImpl usuario = (UserDetailsImpl) authentication.getPrincipal();
      return ResponseEntity.ok(emitir(usuario));
    } catch (AuthenticationException e) {
      return noAutorizado("Credenciales invalidas");
    }
  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refresh(@Valid @RequestBody RefreshTokenRequest request) {
    String refresh = request.refreshToken();
    if (!tokenProvider.validateRefreshToken(refresh)) {
      return noAutorizado("Refresh token invalido o expirado");
    }
    try {
      UserDetailsImpl usuario = (UserDetailsImpl) userDetailsService
          .loadUserByUsername(tokenProvider.getIssuer(refresh));
      return ResponseEntity.ok(emitir(usuario));
    } catch (UsernameNotFoundException e) {
      return noAutorizado("Refresh token invalido o expirado");
    }
  }

  private JwtResponse emitir(UserDetailsImpl usuario) {
    return new JwtResponse(tokenProvider.generateJwtToken(usuario),
        tokenProvider.generateRefreshToken(usuario), "Bearer",
        tokenProvider.getExpiryDuration() / 1000L,
        tokenProvider.getRefreshExpiryDuration() / 1000L);
  }

  private ResponseEntity<ExceptionResponse> noAutorizado(String mensaje) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ExceptionResponse(401, mensaje));
  }
}
