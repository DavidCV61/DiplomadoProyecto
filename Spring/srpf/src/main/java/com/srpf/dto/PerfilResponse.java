package com.srpf.dto;

import java.time.LocalDateTime;

public record PerfilResponse(
    String nombre,
    String username,
    String email,
    String rol,
    Integer puntuacionElo,
    LocalDateTime fechaRegistro) {
}
