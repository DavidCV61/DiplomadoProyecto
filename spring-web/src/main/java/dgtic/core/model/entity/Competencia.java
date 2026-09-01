package dgtic.core.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "competencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Competencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(length = 20)
    private String estado;

    @Column(name = "creador_id")
    private Integer creadorId;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}
