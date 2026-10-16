package com.srpf.security.jwt;

import com.srpf.security.model.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
public class JwtCookieSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtCookieService cookies;

  public JwtCookieSuccessHandler(JwtCookieService cookies) {
    this.cookies = cookies;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    UserDetailsImpl usuario = (UserDetailsImpl) authentication.getPrincipal();
    cookies.emitir(response, usuario);
    log.info("Login web exitoso, cookies JWT emitidas: usuario={}", usuario.getUsername());
    response.sendRedirect(request.getContextPath() + "/");
  }
}
