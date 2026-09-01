package dgtic.core.repository;

import dgtic.core.model.entity.Intento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IntentoRepository extends JpaRepository<Intento, Long> {

    @Query("SELECT i FROM Intento i JOIN FETCH i.usuario JOIN FETCH i.problema ORDER BY i.fechaHora DESC")
    List<Intento> listarConDetalle();

    @Query("SELECT i FROM Intento i WHERE i.competenciaId = :idCompetencia AND i.usuario.id = :idUsuario AND i.resultado = 'CORRECTO'")
    List<Intento> resueltosPorUsuarioEnCompetencia(Integer idCompetencia, Integer idUsuario);
}
