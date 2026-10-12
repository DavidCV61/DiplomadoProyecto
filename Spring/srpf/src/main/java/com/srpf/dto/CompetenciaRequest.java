package com.srpf.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
public class CompetenciaRequest {

  @NotBlank(message = "El nombre es obligatorio")
  @Size(max = 150, message = "Maximo 150 caracteres")
  private String nombre;

  @Size(max = 2000, message = "Maximo 2000 caracteres")
  private String descripcion;

  @NotNull(message = "La fecha de inicio es obligatoria")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime fechaInicio;

  @NotNull(message = "La duracion es obligatoria")
  @Min(value = 1, message = "Minimo 1 minuto")
  @Max(value = 600, message = "Maximo 600 minutos")
  private Integer duracionMinutos;

  @NotEmpty(message = "Selecciona al menos un problema")
  private List<Integer> problemaIds = new ArrayList<>();
}
