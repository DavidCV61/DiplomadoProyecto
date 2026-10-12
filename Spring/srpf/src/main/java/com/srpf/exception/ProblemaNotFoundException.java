package com.srpf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProblemaNotFoundException extends Exception {
  public ProblemaNotFoundException(Integer id) {
    super("No existe el problema con id " + id);
  }
}
