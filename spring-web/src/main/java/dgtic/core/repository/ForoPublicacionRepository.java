package dgtic.core.repository;

import dgtic.core.model.entity.ForoPublicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ForoPublicacionRepository extends JpaRepository<ForoPublicacion, Long> {

    @Query("SELECT f FROM ForoPublicacion f JOIN FETCH f.usuario ORDER BY f.fechaPublicacion DESC")
    List<ForoPublicacion> listarConAutor();
}
