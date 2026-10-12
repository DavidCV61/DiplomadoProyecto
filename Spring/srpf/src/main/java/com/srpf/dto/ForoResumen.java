package com.srpf.dto;

import java.time.LocalDateTime;

public record ForoResumen(Long id, String titulo, String autorNombre, LocalDateTime fecha,
    long respuestas) {
}
