package com.srpf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PublicacionNotFoundException extends Exception {
  public PublicacionNotFoundException(Long id) {
    super("No existe la publicacion con id " + id);
  }
}
