package dgtic.core.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "participante_competencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipanteCompetencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competencia_id", nullable = false)
    private Competencia competencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_inscripcion")
    private LocalDateTime fechaInscripcion;

    @Column(name = "fecha_inicio_participacion")
    private LocalDateTime fechaInicioParticipacion;

    @Column(name = "puntuacion_final")
    private Integer puntuacionFinal;

    @Column(name = "intentos_totales")
    private Integer intentosTotales;
}
