package com.srpf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "competencia_problema")
@IdClass(CompetenciaProblema.PK.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompetenciaProblema {

  @Id
  @Column(name = "competencia_id")
  private Integer competenciaId;

  @Id
  @Column(name = "problema_id")
  private Integer problemaId;

  @Column(nullable = false)
  private Short orden;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  public static class PK implements Serializable {
    private Integer competenciaId;
    private Integer problemaId;
  }
}
