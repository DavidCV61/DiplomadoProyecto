package dgtic.core.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "competencia_problema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CompetenciaProblema.CompetenciaProblemaId.class)
public class CompetenciaProblema {

    @Id
    @Column(name = "competencia_id")
    private Integer competenciaId;

    @Id
    @Column(name = "problema_id")
    private Integer problemaId;

    @Column(name = "orden")
    private Integer orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problema_id", insertable = false, updatable = false)
    private Problema problema;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompetenciaProblemaId implements Serializable {
        private Integer competenciaId;
        private Integer problemaId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CompetenciaProblemaId)) return false;
            CompetenciaProblemaId that = (CompetenciaProblemaId) o;
            return Objects.equals(competenciaId, that.competenciaId) && Objects.equals(problemaId, that.problemaId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(competenciaId, problemaId);
        }
    }
}
