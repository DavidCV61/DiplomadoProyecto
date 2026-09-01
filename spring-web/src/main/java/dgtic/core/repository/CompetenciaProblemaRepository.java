package dgtic.core.repository;

import dgtic.core.model.entity.CompetenciaProblema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompetenciaProblemaRepository extends JpaRepository<CompetenciaProblema, CompetenciaProblema.CompetenciaProblemaId> {

    @Query("SELECT cp FROM CompetenciaProblema cp JOIN FETCH cp.problema WHERE cp.competenciaId = :idCompetencia ORDER BY cp.orden")
    List<CompetenciaProblema> listarPorCompetencia(Integer idCompetencia);
}
