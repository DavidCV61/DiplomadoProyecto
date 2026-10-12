
package com.srpf.enums;

public enum Tema {
  MECANICA("Mecánica"),
  TERMODINAMICA("Termodinámica"),
  ELECTROMAGNETISMO("Electromagnetismo"),
  OPTICA("Óptica"),
  FISICA_MODERNA("Física Moderna"),
  ONDAS("Ondas"),
  OTROS("Otros");

  private final String etiqueta;

  Tema(String etiqueta) {
    this.etiqueta = etiqueta;
  }

  public String getEtiqueta() {
    return etiqueta;
  }
}
