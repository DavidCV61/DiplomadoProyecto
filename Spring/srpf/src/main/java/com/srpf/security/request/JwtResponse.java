package com.srpf.security.request;

public record JwtResponse(String accessToken, String refreshToken, String tokenType,
    long expiresInSeconds, long refreshExpiresInSeconds) {
}
