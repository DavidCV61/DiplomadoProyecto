package com.srpf.security.jwt;

import com.srpf.security.model.UserDetailsImpl;
import com.srpf.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class CookieJwtAuthenticationFilter extends OncePerRequestFilter {

  private final JWTTokenProvider tokenProvider;
  private final UserDetailsServiceImpl userDetailsService;
  private final JwtCookieService cookies;

  public CookieJwtAuthenticationFilter(JWTTokenProvider tokenProvider,
      UserDetailsServiceImpl userDetailsService, JwtCookieService cookies) {
    this.tokenProvider = tokenProvider;
    this.userDetailsService = userDetailsService;
    this.cookies = cookies;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String p = request.getRequestURI();
    return p.startsWith("/css") || p.startsWith("/js") || p.startsWith("/images")
        || p.startsWith("/webjars") || p.startsWith("/vendors") || p.startsWith("/build")
        || p.equals("/favicon.ico");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      UserDetails usuario = identificar(request, response);
      if (usuario != null) {
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(usuario,
            null,
            usuario.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      log.warn("No se pudo autenticar con la cookie -> {}", e.getMessage());
    }
    filterChain.doFilter(request, response);
  }

  private UserDetails identificar(HttpServletRequest request, HttpServletResponse response) {
    String acceso = cookies.leer(request, JwtCookieService.ACCESS);
    if (acceso != null && tokenProvider.validateJwtToken(acceso)) {
      return userDetailsService.loadUserByUsername(tokenProvider.getIssuer(acceso));
    }
    String refresh = cookies.leer(request, JwtCookieService.REFRESH);
    if (refresh != null && tokenProvider.validateRefreshToken(refresh)) {
      UserDetails usuario = userDetailsService.loadUserByUsername(tokenProvider.getIssuer(refresh));
      cookies.emitir(response, (UserDetailsImpl) usuario);
      log.info("Token renovado con refresh token: usuario={}", usuario.getUsername());
      return usuario;
    }
    return null;
  }
}
