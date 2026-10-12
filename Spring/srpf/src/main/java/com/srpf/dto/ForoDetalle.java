package com.srpf.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ForoDetalle(Long id, String titulo, String contenido, String autorNombre,
    LocalDateTime fecha, List<ForoRespuestaView> respuestas) {
}
