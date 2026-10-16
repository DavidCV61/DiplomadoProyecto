package com.srpf.security;

import com.srpf.security.jwt.CookieJwtAuthenticationFilter;
import com.srpf.security.jwt.JWTTokenProvider;
import com.srpf.security.jwt.JwtCookieService;
import com.srpf.security.jwt.JwtCookieSuccessHandler;
import com.srpf.security.service.AuthenticationProviderImpl;
import com.srpf.security.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      AuthenticationProviderImpl authenticationProvider,
      JWTTokenProvider tokenProvider,
      UserDetailsServiceImpl userDetailsService,
      JwtCookieService jwtCookies) throws Exception {
    http
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(
                "/", "/index",
                "/login", "/registro", "/acceso-denegado", "/error",
                "/webjars/**", "/css/**", "/js/**", "/vendors/**", "/build/**",
                "/images/**", "/favicon.ico",
                "/problemas", "/problemas/**",
                "/ranking",
                "/competencias", "/competencias/**",
                "/enviados",
                "/foro", "/foro/**")
            .permitAll()
            .requestMatchers("/auth/**", "/perfil").authenticated()
            .requestMatchers("/autor/**").hasRole("AUTOR")
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated())
        .formLogin(login -> login
            .loginPage("/login")
            .successHandler(new JwtCookieSuccessHandler(jwtCookies))
            .permitAll())
        .logout(logout -> logout
            .logoutUrl("/doLogout")
            .logoutSuccessUrl("/")
            .deleteCookies("JSESSIONID", JwtCookieService.ACCESS, JwtCookieService.REFRESH)
            .invalidateHttpSession(true)
            .permitAll())
        .exceptionHandling(ex -> ex
            .accessDeniedPage("/acceso-denegado"))
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(
            new CookieJwtAuthenticationFilter(tokenProvider, userDetailsService, jwtCookies),
            UsernamePasswordAuthenticationFilter.class)
        .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
        .cors(Customizer.withDefaults())
        .authenticationProvider(authenticationProvider);
    return http.build();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy("""
        ROLE_ADMIN > ROLE_AUTOR
        ROLE_AUTOR > ROLE_USER
        """);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(11, new SecureRandom());
  }
}
