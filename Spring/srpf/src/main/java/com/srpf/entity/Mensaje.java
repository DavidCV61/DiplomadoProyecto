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
@Table(name = "mensaje")
@Getter
@Setter
@NoArgsConstructor
public class Mensaje {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "remitente_id", nullable = false)
  private Usuario remitente;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "destinatario_id", nullable = false)
  private Usuario destinatario;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String contenido;

  @CreationTimestamp
  @Column(name = "fecha_hora", nullable = false, updatable = false)
  private LocalDateTime fechaHora;
}
