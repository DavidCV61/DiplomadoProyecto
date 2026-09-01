package dgtic.core.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProblemaDTO {

    @NotBlank(message = "{problema.titulo.notblank}")
    private String titulo;

    @NotBlank(message = "{problema.enunciado.notblank}")
    private String enunciado;

    @NotBlank(message = "{problema.tema.notblank}")
    private String tema;

    @NotBlank(message = "{problema.nivel.notblank}")
    private String nivel;

    @NotNull(message = "{problema.resultado.notnull}")
    private BigDecimal resultadoEsperado;

    @NotNull(message = "{problema.margen.notnull}")
    @DecimalMin(value = "0.0", message = "{problema.margen.min}")
    private BigDecimal margenTolerancia;
}
