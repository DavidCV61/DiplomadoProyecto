package com.srpf.entity;

import com.srpf.enums.ResultadoIntento;
import com.srpf.enums.TipoIntento;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "intento")
@Getter
@Setter
@NoArgsConstructor
public class Intento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "problema_id", nullable = false)
  private Problema problema;

  @Column(name = "competencia_id")
  private Integer competenciaId;

  @Column(name = "respuesta_enviada", nullable = false, precision = 15, scale = 6)
  private BigDecimal respuestaEnviada;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ResultadoIntento resultado;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "valores_generados")
  private String valoresGenerados;

  @CreationTimestamp
  @Column(name = "fecha_hora", nullable = false, updatable = false)
  private LocalDateTime fechaHora;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TipoIntento tipo = TipoIntento.GIMNASIO;
}
