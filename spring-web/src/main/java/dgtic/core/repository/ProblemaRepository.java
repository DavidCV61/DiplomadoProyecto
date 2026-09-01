package dgtic.core.repository;

import dgtic.core.model.entity.Problema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemaRepository extends JpaRepository<Problema, Integer> {
}
