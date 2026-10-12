package com.srpf.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record RespuestaApiRequest(
    @NotNull(message = "La respuesta es obligatoria")
    @Digits(integer = 9, fraction = 6, message = "Maximo 9 enteros y 6 decimales")
    BigDecimal respuesta) {
}
