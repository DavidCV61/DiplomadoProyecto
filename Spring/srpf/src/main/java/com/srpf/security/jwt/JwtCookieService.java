package com.srpf.security.jwt;

import com.srpf.security.model.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class JwtCookieService {

  public static final String ACCESS = "token";
  public static final String REFRESH = "refresh_token";

  private final JWTTokenProvider tokenProvider;

  public JwtCookieService(JWTTokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  public void emitir(HttpServletResponse response, UserDetailsImpl usuario) {
    agregar(response, ACCESS, tokenProvider.generateJwtToken(usuario),
        Duration.ofMillis(tokenProvider.getExpiryDuration()));
    agregar(response, REFRESH, tokenProvider.generateRefreshToken(usuario),
        Duration.ofMillis(tokenProvider.getRefreshExpiryDuration()));
  }

  public void borrar(HttpServletResponse response) {
    agregar(response, ACCESS, "", Duration.ZERO);
    agregar(response, REFRESH, "", Duration.ZERO);
  }

  public String leer(HttpServletRequest request, String nombre) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return null;
    }
    for (Cookie cookie : cookies) {
      if (nombre.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }

  private void agregar(HttpServletResponse response, String nombre, String valor,
      Duration duracion) {
    ResponseCookie cookie = ResponseCookie.from(nombre, valor)
        .httpOnly(true)
        .secure(false)
        .sameSite("Lax")
        .path("/")
        .maxAge(duracion)
        .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }
}
