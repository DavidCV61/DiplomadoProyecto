package com.srpf.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ForoPublicacionRequest {

  @NotBlank(message = "El titulo es obligatorio")
  @Size(max = 200, message = "Maximo 200 caracteres")
  private String titulo;

  @NotBlank(message = "El contenido es obligatorio")
  @Size(max = 5000, message = "Maximo 5000 caracteres")
  private String contenido;
}
