package com.srpf.exception;

public class ProblemaInvalidoException extends Exception {
  private final String campo;

  public ProblemaInvalidoException(String campo, String mensaje) {
    super(mensaje);
    this.campo = campo;
  }

  public String getCampo() {
    return campo;
  }
}
