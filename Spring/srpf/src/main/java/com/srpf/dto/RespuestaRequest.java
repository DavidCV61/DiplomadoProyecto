package com.srpf.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RespuestaRequest {

  @NotNull(message = "Escribe tu respuesta")
  @Digits(integer = 9, fraction = 6, message = "Maximo 9 enteros y 6 decimales")
  private BigDecimal respuesta;
}
