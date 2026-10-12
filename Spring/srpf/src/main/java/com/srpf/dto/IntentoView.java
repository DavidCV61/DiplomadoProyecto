package com.srpf.dto;

import com.srpf.enums.ResultadoIntento;
import com.srpf.enums.TipoIntento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IntentoView(Long id, Integer problemaId, String problemaTitulo,
    BigDecimal respuesta, ResultadoIntento resultado, TipoIntento tipo, LocalDateTime fecha) {
}
