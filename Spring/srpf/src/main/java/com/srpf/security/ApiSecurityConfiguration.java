package com.srpf.security;

import com.srpf.security.jwt.JWTAuthenticationFilter;
import com.srpf.security.jwt.JWTTokenProvider;
import com.srpf.security.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class ApiSecurityConfiguration {

  @Bean
  @Order(1)
  public SecurityFilterChain apiFilterChain(HttpSecurity http, JWTTokenProvider tokenProvider,
      UserDetailsServiceImpl userDetailsService) throws Exception {
    JWTAuthenticationFilter jwtFilter =
        new JWTAuthenticationFilter(tokenProvider, userDetailsService);
    http
        .securityMatcher("/api/**")
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**", "/api/problemas", "/api/estadisticas/**").permitAll()
            .anyRequest().authenticated())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(e -> e
            .authenticationEntryPoint((req, res, ex) -> escribir(res, 401, "No autenticado"))
            .accessDeniedHandler((req, res, ex) -> escribir(res, 403, "Acceso denegado")));
    return http.build();
  }

  private static void escribir(HttpServletResponse res, int status, String mensaje)
      throws IOException {
    res.setStatus(status);
    res.setContentType("application/json;charset=UTF-8");
    res.getWriter().write("{\"status\":" + status + ",\"mensaje\":\"" + mensaje + "\"}");
  }
}
