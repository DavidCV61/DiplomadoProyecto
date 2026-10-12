package com.srpf.dto;

import com.srpf.enums.EstadoCompetencia;
import java.time.LocalDateTime;
import java.util.List;

public record CompetenciaDetalle(Integer id, String nombre, String descripcion, String creador,
    LocalDateTime fechaInicio, LocalDateTime fechaFin, int duracionMinutos,
    EstadoCompetencia estado, boolean inscrito, boolean puedeInscribirse,
    long segundosParaInicio, long segundosParaFin, List<ProblemaCompItem> problemas,
    List<PosicionView> tabla) {
}
