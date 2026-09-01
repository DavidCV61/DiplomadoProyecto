package dgtic.core.service;

import dgtic.core.model.entity.Problema;
import dgtic.core.repository.ProblemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemaService {

    private final ProblemaRepository problemaRepository;

    public List<Problema> listar() {
        return problemaRepository.findAll();
    }

    public long contar() {
        return problemaRepository.count();
    }

    public Problema buscarPorId(Integer id) {
        return problemaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el problema con id " + id));
    }

    public Problema guardar(dgtic.core.model.dto.ProblemaDTO dto) {
        Problema problema = new Problema();
        problema.setTitulo(dto.getTitulo());
        problema.setEnunciado(dto.getEnunciado());
        problema.setTema(dto.getTema());
        problema.setNivel(dto.getNivel());
        problema.setResultadoEsperado(dto.getResultadoEsperado());
        problema.setMargenTolerancia(dto.getMargenTolerancia());
        problema.setFechaCreacion(LocalDateTime.now());
        return problemaRepository.save(problema);
    }

    public Problema actualizar(Integer id, dgtic.core.model.dto.ProblemaDTO dto) {
        Problema problema = buscarPorId(id);
        problema.setTitulo(dto.getTitulo());
        problema.setEnunciado(dto.getEnunciado());
        problema.setTema(dto.getTema());
        problema.setNivel(dto.getNivel());
        problema.setResultadoEsperado(dto.getResultadoEsperado());
        problema.setMargenTolerancia(dto.getMargenTolerancia());
        return problemaRepository.save(problema);
    }

    public void eliminar(Integer id) {
        problemaRepository.deleteById(id);
    }
}
