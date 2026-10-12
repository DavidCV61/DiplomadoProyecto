package com.srpf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UsuarioDuplicadoException extends Exception {
  private final String campo;

  public UsuarioDuplicadoException(String campo, String valor) {
    super("Ya existe un usuario con " + campo + ": " + valor);
    this.campo = campo;
  }

  public String getCampo() {
    return campo;
  }
}
