package com.srpf.security.jwt;

import com.srpf.security.model.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JWTTokenProvider {

  private static final String TOKEN_TYPE_CLAIM = "typ";
  private static final String TOKEN_TYPE_ACCESS = "access";
  private static final String TOKEN_TYPE_REFRESH = "refresh";

  private final SecretKey key;
  private final long jwtExpirationInSeconds;
  private final long refreshExpirationInSeconds;

  public JWTTokenProvider(@Value("${jwt.secret}") String secret,
      @Value("${jwt.expirationDateInMs}") long jwtExpirationInSeconds,
      @Value("${jwt.refreshExpirationDateInMs}") long refreshExpirationInSeconds) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.jwtExpirationInSeconds = jwtExpirationInSeconds;
    this.refreshExpirationInSeconds = refreshExpirationInSeconds;
  }

  public String generateJwtToken(UserDetailsImpl user) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInSeconds * 1000L);
    List<String> roles = user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority).toList();
    return Jwts.builder()
        .subject("SRPF")
        .issuer(user.getUsername())
        .audience().add("API").and()
        .claim("auth", roles)
        .claim("issid", user.getId())
        .claim("issname", user.getName())
        .claim(TOKEN_TYPE_CLAIM, TOKEN_TYPE_ACCESS)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(key)
        .compact();
  }

  public String generateRefreshToken(UserDetailsImpl user) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + refreshExpirationInSeconds * 1000L);
    return Jwts.builder()
        .subject("SRPF")
        .issuer(user.getUsername())
        .audience().add("API").and()
        .claim("issid", user.getId())
        .claim(TOKEN_TYPE_CLAIM, TOKEN_TYPE_REFRESH)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(key)
        .compact();
  }

  public Claims getClaims(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }

  public String getIssuer(String token) {
    return getClaims(token).getIssuer();
  }

  public boolean validateJwtToken(String token) {
    return validar(token, TOKEN_TYPE_ACCESS);
  }

  public boolean validateRefreshToken(String token) {
    return validar(token, TOKEN_TYPE_REFRESH);
  }

  private boolean validar(String token, String tipoEsperado) {
    try {
      Claims claims = getClaims(token);
      if (!tipoEsperado.equals(claims.get(TOKEN_TYPE_CLAIM, String.class))) {
        log.warn("Tipo de token invalido, se esperaba {}", tipoEsperado);
        return false;
      }
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      log.warn("Token invalido -> {}", e.getMessage());
      return false;
    }
  }

  public long getExpiryDuration() {
    return jwtExpirationInSeconds * 1000L;
  }

  public long getRefreshExpiryDuration() {
    return refreshExpirationInSeconds * 1000L;
  }
}
