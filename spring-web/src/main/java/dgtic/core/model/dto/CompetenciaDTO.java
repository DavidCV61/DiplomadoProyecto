package dgtic.core.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompetenciaDTO {

    @NotBlank(message = "{competencia.nombre.notblank}")
    private String nombre;

    private String descripcion;

    @NotNull(message = "{competencia.fecha.notnull}")
    private LocalDateTime fechaInicio;

    @NotNull(message = "{competencia.duracion.notnull}")
    @Min(value = 1, message = "{competencia.duracion.min}")
    private Integer duracionMinutos;

    @NotBlank(message = "{competencia.estado.notblank}")
    private String estado;

    @NotNull(message = "{competencia.creador.notnull}")
    private Integer creadorId;
}
