package com.srpf.dto;

import com.srpf.enums.Nivel;
import com.srpf.enums.Tema;
import java.time.LocalDateTime;

public record ProblemaResponse(
    Integer id,
    String titulo,
    String enunciado,
    Tema tema,
    Nivel nivel,
    Integer autorId,
    String autorNombre,
    LocalDateTime fechaCreacion) {
}
