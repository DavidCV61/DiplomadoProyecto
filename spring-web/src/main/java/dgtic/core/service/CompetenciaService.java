package dgtic.core.service;

import dgtic.core.model.dto.CompetenciaDTO;
import dgtic.core.model.dto.ProblemaDTO;
import dgtic.core.model.entity.Competencia;
import dgtic.core.model.entity.CompetenciaProblema;
import dgtic.core.model.entity.Problema;
import dgtic.core.repository.CompetenciaProblemaRepository;
import dgtic.core.repository.CompetenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetenciaService {

  private final CompetenciaRepository competenciaRepository;
  private final CompetenciaProblemaRepository competenciaProblemaRepository;
  private final ProblemaService problemaService;

  public List<Competencia> listar() {
    return competenciaRepository.findAll();
  }

  public Competencia buscarPorId(Integer id) {
    return competenciaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("No existe la competencia con id " + id));
  }

  @Transactional
  public Competencia crear(CompetenciaDTO dto) {
    Competencia competencia = new Competencia();
    competencia.setNombre(dto.getNombre());
    competencia.setDescripcion(dto.getDescripcion());
    competencia.setFechaInicio(dto.getFechaInicio());
    competencia.setDuracionMinutos(dto.getDuracionMinutos());
    competencia.setEstado(dto.getEstado());
    competencia.setCreadorId(dto.getCreadorId());
    competencia.setFechaCreacion(LocalDateTime.now());
    return competenciaRepository.save(competencia);
  }

  @Transactional
  public Competencia actualizar(Integer id, CompetenciaDTO dto) {
    Competencia competencia = buscarPorId(id);
    competencia.setNombre(dto.getNombre());
    competencia.setDescripcion(dto.getDescripcion());
    competencia.setFechaInicio(dto.getFechaInicio());
    competencia.setDuracionMinutos(dto.getDuracionMinutos());
    competencia.setEstado(dto.getEstado());
    return competenciaRepository.save(competencia);
  }

  public List<CompetenciaProblema> problemasDeCompetencia(Integer idCompetencia) {
    return competenciaProblemaRepository.listarPorCompetencia(idCompetencia);
  }

  @Transactional
  public void agregarProblemaExistente(Integer idCompetencia, Integer idProblema, Integer orden) {
    Competencia competencia = buscarPorId(idCompetencia);
    if (competenciaProblemaRepository.existsById(
        new CompetenciaProblema.CompetenciaProblemaId(idCompetencia, idProblema))) {
      return;
    }
    CompetenciaProblema cp = new CompetenciaProblema();
    cp.setCompetenciaId(competencia.getId());
    cp.setProblemaId(idProblema);
    cp.setOrden(orden != null ? orden : 1);
    competenciaProblemaRepository.save(cp);
  }

  @Transactional
  public void crearProblemaYAsignar(Integer idCompetencia, ProblemaDTO problemaDTO, Integer orden) {
    Problema problema = problemaService.guardar(problemaDTO);
    agregarProblemaExistente(idCompetencia, problema.getId(), orden);
  }

  @Transactional
  public void quitarProblema(Integer idCompetencia, Integer idProblema) {
    competenciaProblemaRepository.deleteById(
        new CompetenciaProblema.CompetenciaProblemaId(idCompetencia, idProblema));
  }

  @Transactional
  public void agregarProblemaExistente(Integer idCompetencia, Integer idProblema) {
    Competencia competencia = buscarPorId(idCompetencia);
    if (competenciaProblemaRepository.existsById(
        new CompetenciaProblema.CompetenciaProblemaId(idCompetencia, idProblema))) {
      return;
    }

    int siguienteOrden = problemasDeCompetencia(idCompetencia).size() + 1;

    CompetenciaProblema cp = new CompetenciaProblema();
    cp.setCompetenciaId(competencia.getId());
    cp.setProblemaId(idProblema);
    cp.setOrden(siguienteOrden);
    competenciaProblemaRepository.save(cp);
  }

  @Transactional
  public void crearProblemaYAsignar(Integer idCompetencia, ProblemaDTO problemaDTO) {
    Problema problema = problemaService.guardar(problemaDTO);
    agregarProblemaExistente(idCompetencia, problema.getId());
  }
}
