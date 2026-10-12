package com.srpf.dto;

import com.srpf.enums.EstadoCompetencia;
import java.time.LocalDateTime;

public record CompetenciaItem(Integer id, String nombre, String creador, EstadoCompetencia estado,
    LocalDateTime fechaInicio, LocalDateTime fechaFin, int duracionMinutos, long participantes,
    boolean inscrito) {
}
