package com.srpf.entity;

import com.srpf.enums.Nivel;
import com.srpf.enums.Tema;
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
@Table(name = "problema")
@Getter
@Setter
@NoArgsConstructor
public class Problema {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, length = 200)
  private String titulo;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String enunciado;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Tema tema;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Nivel nivel = Nivel.BASICO;

  @Column(name = "resultado_esperado", nullable = false, precision = 15, scale = 6)
  private BigDecimal resultadoEsperado;

  @Column(name = "margen_tolerancia", nullable = false, precision = 10, scale = 6)
  private BigDecimal margenTolerancia = BigDecimal.ZERO;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "parametros_dinamicos")
  private String parametrosDinamicos;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "autor_id", nullable = false)
  private Usuario autor;

  @CreationTimestamp
  @Column(name = "fecha_creacion", nullable = false, updatable = false)
  private LocalDateTime fechaCreacion;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Problema other)) {
      return false;
    }
    return id != null && id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
