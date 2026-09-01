package dgtic.core.repository;

import dgtic.core.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findAllByOrderByPuntuacionEloDesc();
}
