package com.srpf.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ForoRespuestaRequest {

  @NotBlank(message = "La respuesta no puede estar vacia")
  @Size(max = 5000, message = "Maximo 5000 caracteres")
  private String contenido;
}
