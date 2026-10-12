package com.srpf.security.jwt;

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
public class JWTAuthenticationFilter extends OncePerRequestFilter {

  private final JWTTokenProvider tokenProvider;
  private final UserDetailsServiceImpl userDetailsService;

  public JWTAuthenticationFilter(JWTTokenProvider tokenProvider,
      UserDetailsServiceImpl userDetailsService) {
    this.tokenProvider = tokenProvider;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String cabecera = request.getHeader("Authorization");
    if (cabecera == null || !cabecera.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    String jwt = cabecera.substring(7).trim();
    try {
      if (tokenProvider.validateJwtToken(jwt)) {
        String username = tokenProvider.getIssuer(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
            UsernamePasswordAuthenticationToken.authenticated(userDetails, null,
                userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      log.warn("No se pudo autenticar con el token -> {}", e.getMessage());
    }
    filterChain.doFilter(request, response);
  }
}
