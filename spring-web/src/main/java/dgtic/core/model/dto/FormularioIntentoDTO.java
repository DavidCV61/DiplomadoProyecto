package dgtic.core.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FormularioIntentoDTO {

    @NotNull(message = "{intento.usuario.notnull}")
    private Integer idUsuario;

    @NotNull(message = "{intento.problema.notnull}")
    private Integer idProblema;

    @NotNull(message = "{intento.respuesta.notnull}")
    private BigDecimal respuestaEnviada;
}
