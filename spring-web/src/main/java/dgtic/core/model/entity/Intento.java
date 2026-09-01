package dgtic.core.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "intento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Intento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problema_id", nullable = false)
    private Problema problema;

    @Column(name = "competencia_id")
    private Integer competenciaId;

    @Column(name = "respuesta_enviada", nullable = false, precision = 15, scale = 6)
    private BigDecimal respuestaEnviada;

    @Column(length = 15)
    private String resultado;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column(length = 15)
    private String tipo;
}
