package com.srpf.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistroRequest {

  @NotBlank(message = "El nombre es obligatorio")
  @Size(max = 100, message = "Maximo 100 caracteres")
  private String nombre;

  @NotBlank(message = "El usuario es obligatorio")
  @Size(min = 3, max = 50, message = "Entre 3 y 50 caracteres")
  @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Solo letras, numeros, punto, guion y guion bajo")
  private String username;

  @NotBlank(message = "El correo es obligatorio")
  @Email(message = "Correo invalido")
  @Size(max = 150, message = "Maximo 150 caracteres")
  private String email;

  @NotBlank(message = "La contrasena es obligatoria")
  @Size(min = 8, max = 72, message = "Entre 8 y 72 caracteres")
  private String password;

  @NotBlank(message = "Confirma la contrasena")
  private String confirmarPassword;
}
