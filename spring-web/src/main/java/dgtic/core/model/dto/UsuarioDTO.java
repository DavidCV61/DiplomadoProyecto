package dgtic.core.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    @NotBlank(message = "{usuario.nombre.notblank}")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "{usuario.username.notblank}")
    @Size(max = 50)
    private String username;

    @NotBlank(message = "{usuario.email.notblank}")
    @Email(message = "{usuario.email.invalido}")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "{usuario.password.notblank}")
    @Size(min = 4, max = 255, message = "{usuario.password.size}")
    private String password;
}
