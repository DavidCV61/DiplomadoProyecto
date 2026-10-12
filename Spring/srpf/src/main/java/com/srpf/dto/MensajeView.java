package com.srpf.dto;

import java.time.LocalDateTime;

public record MensajeView(Long id, String remitenteUsername, String remitenteNombre,
    String destinatarioUsername, String destinatarioNombre, String contenido,
    LocalDateTime fecha) {
}
