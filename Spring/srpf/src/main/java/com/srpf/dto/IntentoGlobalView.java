package com.srpf.dto;

import com.srpf.enums.ResultadoIntento;
import com.srpf.enums.TipoIntento;
import java.time.LocalDateTime;

public record IntentoGlobalView(Long id, String username, Integer problemaId,
    String problemaTitulo, ResultadoIntento resultado, TipoIntento tipo,
    LocalDateTime fecha) {
}
