package com.srpf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UsuarioNotFoundException extends Exception {
  public UsuarioNotFoundException(Integer id) {
    super("Usuario con id " + id + " NO fue encontrado");
  }

  public UsuarioNotFoundException(String username) {
    super("Usuario " + username + " NO fue encontrado");
  }
}
