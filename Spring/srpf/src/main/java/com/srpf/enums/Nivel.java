package com.srpf.enums;

public enum Nivel {
  BASICO("Básico"),
  INTERMEDIO("Intermedio"),
  AVANZADO("Avanzado"),
  EXPERTO("Experto");

  private final String etiqueta;

  Nivel(String etiqueta) {
    this.etiqueta = etiqueta;
  }

  public String getEtiqueta() {
    return etiqueta;
  }
}
