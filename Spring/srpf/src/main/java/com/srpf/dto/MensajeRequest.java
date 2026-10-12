package com.srpf.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MensajeRequest {

  @NotBlank(message = "Escribe un mensaje")
  @Size(max = 2000, message = "Maximo 2000 caracteres")
  private String contenido;
}
