package dgtic.core.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ForoPublicacionDTO {

    @NotNull(message = "{foro.usuario.notnull}")
    private Integer idUsuario;

    @NotBlank(message = "{foro.titulo.notblank}")
    private String titulo;

    @NotBlank(message = "{foro.contenido.notblank}")
    private String contenido;
}
