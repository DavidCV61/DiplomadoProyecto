package dgtic.core.service;

import dgtic.core.model.dto.ForoPublicacionDTO;
import dgtic.core.model.entity.ForoPublicacion;
import dgtic.core.model.entity.Usuario;
import dgtic.core.repository.ForoPublicacionRepository;
import dgtic.core.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ForoService {

    private final ForoPublicacionRepository foroPublicacionRepository;
    private final UsuarioRepository usuarioRepository;

    public List<ForoPublicacion> listar() {
        return foroPublicacionRepository.listarConAutor();
    }

    @Transactional
    public ForoPublicacion publicar(ForoPublicacionDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("No existe el usuario seleccionado"));

        ForoPublicacion publicacion = new ForoPublicacion();
        publicacion.setUsuario(usuario);
        publicacion.setTitulo(dto.getTitulo());
        publicacion.setContenido(dto.getContenido());
        publicacion.setFechaPublicacion(LocalDateTime.now());

        return foroPublicacionRepository.save(publicacion);
    }
}
