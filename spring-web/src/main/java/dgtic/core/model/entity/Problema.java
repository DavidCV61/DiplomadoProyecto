package dgtic.core.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "problema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Problema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String enunciado;

    @Column(length = 30)
    private String tema;

    @Column(length = 20)
    private String nivel;

    @Column(name = "resultado_esperado", precision = 15, scale = 6)
    private BigDecimal resultadoEsperado;

    @Column(name = "margen_tolerancia", precision = 10, scale = 6)
    private BigDecimal margenTolerancia;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}
