package dgtic.core.service;

import dgtic.core.model.entity.Usuario;
import dgtic.core.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> ranking() {
        return usuarioRepository.findAllByOrderByPuntuacionEloDesc();
    }

    public long contar() {
        return usuarioRepository.count();
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el usuario con id " + id));
    }

    public Usuario registrar(dgtic.core.model.dto.UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setPasswordHash(dto.getPassword());
        usuario.setRol("USER");
        usuario.setPuntuacionElo(1000);
        usuario.setFechaRegistro(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Integer id, dgtic.core.model.dto.UsuarioDTO dto) {
        Usuario usuario = buscarPorId(id);
        usuario.setNombre(dto.getNombre());
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }
}
