package com.srpf.dto;

public record PosicionView(Integer posicion, String username, int puntuacion,
    Integer tiempoSegundos, int intentos, Integer eloAntes, Integer eloDespues) {

  public Integer cambioElo() {
    return (eloAntes == null || eloDespues == null) ? null : eloDespues - eloAntes;
  }
}
