package com.srpf.entity;

import com.srpf.enums.EstadoCompetencia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "competencia")
@Getter
@Setter
@NoArgsConstructor
public class Competencia {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, length = 150)
  private String nombre;

  @Column(columnDefinition = "TEXT")
  private String descripcion;

  @Column(name = "fecha_inicio", nullable = false)
  private LocalDateTime fechaInicio;

  @Column(name = "duracion_minutos", nullable = false)
  private Short duracionMinutos;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EstadoCompetencia estado = EstadoCompetencia.PROGRAMADA;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "creador_id", nullable = false)
  private Usuario creador;

  @CreationTimestamp
  @Column(name = "fecha_creacion", nullable = false, updatable = false)
  private LocalDateTime fechaCreacion;

  public LocalDateTime fechaFin() {
    return fechaInicio.plusMinutes(duracionMinutos);
  }
}
