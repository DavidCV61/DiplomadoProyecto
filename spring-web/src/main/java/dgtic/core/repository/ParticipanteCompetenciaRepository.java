package dgtic.core.repository;

import dgtic.core.model.entity.ParticipanteCompetencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipanteCompetenciaRepository extends JpaRepository<ParticipanteCompetencia, Long> {
    Optional<ParticipanteCompetencia> findByCompetenciaIdAndUsuarioId(Integer competenciaId, Integer usuarioId);
}
