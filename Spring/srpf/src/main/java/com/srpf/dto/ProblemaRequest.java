package com.srpf.dto;

import com.srpf.enums.Nivel;
import com.srpf.enums.Tema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProblemaRequest {

  @NotBlank(message = "El titulo es obligatorio")
  @Size(max = 200, message = "Maximo 200 caracteres")
  private String titulo;

  @NotBlank(message = "El enunciado es obligatorio")
  private String enunciado;

  @NotNull(message = "Selecciona un tema")
  private Tema tema;

  @NotNull(message = "Selecciona un nivel")
  private Nivel nivel = Nivel.BASICO;

  @NotNull(message = "El resultado esperado es obligatorio")
  @Digits(integer = 9, fraction = 6, message = "Maximo 9 enteros y 6 decimales")
  private BigDecimal resultadoEsperado;

  @NotNull(message = "La tolerancia es obligatoria")
  @PositiveOrZero(message = "No puede ser negativa")
  @Digits(integer = 4, fraction = 6, message = "Maximo 4 enteros y 6 decimales")
  private BigDecimal margenTolerancia = BigDecimal.ZERO;

  private String parametrosDinamicos;
}
