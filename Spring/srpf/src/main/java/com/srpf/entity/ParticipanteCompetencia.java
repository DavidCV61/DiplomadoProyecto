package com.srpf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "participante_competencia")
@Getter
@Setter
@NoArgsConstructor
public class ParticipanteCompetencia {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "competencia_id", nullable = false)
  private Competencia competencia;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @CreationTimestamp
  @Column(name = "fecha_inscripcion", nullable = false, updatable = false)
  private LocalDateTime fechaInscripcion;

  @Column(name = "fecha_inicio_participacion")
  private LocalDateTime fechaInicioParticipacion;

  @Column(name = "fecha_fin_participacion")
  private LocalDateTime fechaFinParticipacion;

  @Column(name = "puntuacion_final", nullable = false)
  private Integer puntuacionFinal = 0;

  @Column(name = "tiempo_total_segundos")
  private Integer tiempoTotalSegundos;

  @Column(name = "intentos_totales", nullable = false)
  private Short intentosTotales = 0;

  @Column(name = "posicion_final")
  private Short posicionFinal;

  @Column(name = "elo_antes")
  private Integer eloAntes;

  @Column(name = "elo_despues")
  private Integer eloDespues;
}
