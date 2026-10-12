package com.srpf.service;

import com.srpf.dto.CompetenciaDetalle;
import com.srpf.dto.CompetenciaItem;
import com.srpf.dto.CompetenciaRequest;
import com.srpf.dto.PosicionView;
import com.srpf.dto.ProblemaCompItem;
import com.srpf.dto.ProblemaOpcion;
import com.srpf.dto.ProblemaPreparado;
import com.srpf.entity.Competencia;
import com.srpf.entity.CompetenciaProblema;
import com.srpf.entity.Intento;
import com.srpf.entity.ParticipanteCompetencia;
import com.srpf.entity.Problema;
import com.srpf.entity.Usuario;
import com.srpf.enums.EstadoCompetencia;
import com.srpf.enums.ResultadoIntento;
import com.srpf.enums.TipoIntento;
import com.srpf.exception.CompetenciaException;
import com.srpf.repository.CompetenciaProblemaRepository;
import com.srpf.repository.CompetenciaRepository;
import com.srpf.repository.IntentoCompetenciaRepository;
import com.srpf.repository.ParticipanteCompetenciaRepository;
import com.srpf.repository.ProblemaRepository;
import com.srpf.repository.UsuarioRepository;
import com.srpf.security.model.UserDetailsImpl;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompetenciaServiceImpl implements CompetenciaService {

  private static final Comparator<ParticipanteCompetencia> ORDEN = Comparator
      .comparing(ParticipanteCompetencia::getPuntuacionFinal, Comparator.reverseOrder())
      .thenComparing(p -> p.getTiempoTotalSegundos() == null ? Integer.MAX_VALUE
          : p.getTiempoTotalSegundos())
      .thenComparing(ParticipanteCompetencia::getIntentosTotales);

  private final CompetenciaRepository competenciaRepository;
  private final CompetenciaProblemaRepository competenciaProblemaRepository;
  private final ParticipanteCompetenciaRepository participanteRepository;
  private final IntentoCompetenciaRepository intentoCompetenciaRepository;
  private final ProblemaRepository problemaRepository;
  private final UsuarioRepository usuarioRepository;

  @Autowired
  public CompetenciaServiceImpl(CompetenciaRepository competenciaRepository,
      CompetenciaProblemaRepository competenciaProblemaRepository,
      ParticipanteCompetenciaRepository participanteRepository,
      IntentoCompetenciaRepository intentoCompetenciaRepository,
      ProblemaRepository problemaRepository, UsuarioRepository usuarioRepository) {
    this.competenciaRepository = competenciaRepository;
    this.competenciaProblemaRepository = competenciaProblemaRepository;
    this.participanteRepository = participanteRepository;
    this.intentoCompetenciaRepository = intentoCompetenciaRepository;
    this.problemaRepository = problemaRepository;
    this.usuarioRepository = usuarioRepository;
  }

  private static boolean esDinamico(Problema p) {
    Object json = p.getParametrosDinamicos();
    return json != null && !json.toString().isBlank();
  }

  private EstadoCompetencia estado(Competencia c) {
    if (c.getEstado() == EstadoCompetencia.FINALIZADA
        || c.getEstado() == EstadoCompetencia.CANCELADA) {
      return c.getEstado();
    }
    LocalDateTime ahora = LocalDateTime.now();
    if (ahora.isBefore(c.getFechaInicio())) {
      return EstadoCompetencia.PROGRAMADA;
    }
    if (ahora.isBefore(c.fechaFin())) {
      return EstadoCompetencia.EN_CURSO;
    }
    cerrar(c);
    return EstadoCompetencia.FINALIZADA;
  }

  private static boolean mejor(ParticipanteCompetencia a, ParticipanteCompetencia b) {
    if (a.getPuntuacionFinal() != b.getPuntuacionFinal().intValue()) {
      return a.getPuntuacionFinal() > b.getPuntuacionFinal();
    }
    int ta = a.getTiempoTotalSegundos() == null ? Integer.MAX_VALUE : a.getTiempoTotalSegundos();
    int tb = b.getTiempoTotalSegundos() == null ? Integer.MAX_VALUE : b.getTiempoTotalSegundos();
    return ta < tb;
  }

  private void cerrar(Competencia c) {
    List<ParticipanteCompetencia> todos = participanteRepository.porCompetencia(c.getId());
    List<ParticipanteCompetencia> activos = new ArrayList<>();
    for (ParticipanteCompetencia p : todos) {
      if (p.getIntentosTotales() > 0) {
        activos.add(p);
      } else {
        p.setEloAntes(p.getUsuario().getPuntuacionElo());
        p.setEloDespues(p.getUsuario().getPuntuacionElo());
      }
    }
    activos.sort(ORDEN);
    int n = activos.size();
    double[] elo = new double[n];
    int[] rango = new int[n];
    for (int i = 0; i < n; i++) {
      elo[i] = activos.get(i).getUsuario().getPuntuacionElo();
      int mejores = 0;
      for (int j = 0; j < n; j++) {
        if (j != i && mejor(activos.get(j), activos.get(i))) {
          mejores++;
        }
      }
      rango[i] = mejores + 1;
    }
    for (int i = 0; i < n; i++) {
      ParticipanteCompetencia p = activos.get(i);
      int delta = 0;
      if (n > 1) {
        double esperado = semilla(elo, i, elo[i]);
        double objetivo = Math.sqrt(rango[i] * esperado);
        double lo = 1;
        double hi = 8000;
        for (int k = 0; k < 60; k++) {
          double mid = (lo + hi) / 2;
          if (semilla(elo, i, mid) > objetivo) {
            lo = mid;
          } else {
            hi = mid;
          }
        }
        delta = (int) Math.round(((lo + hi) / 2 - elo[i]) / 2.0);
      }
      int antes = (int) elo[i];
      int despues = Math.max(0, antes + delta);
      p.setPosicionFinal((short) rango[i]);
      p.setEloAntes(antes);
      p.setEloDespues(despues);
      p.setFechaFinParticipacion(c.fechaFin());
      p.getUsuario().setPuntuacionElo(despues);
    }
    c.setEstado(EstadoCompetencia.FINALIZADA);
    competenciaRepository.save(c);
  }

  private static double semilla(double[] elo, int i, double rating) {
    double s = 1;
    for (int j = 0; j < elo.length; j++) {
      if (j != i) {
        s += 1.0 / (1.0 + Math.pow(10, (rating - elo[j]) / 400.0));
      }
    }
    return s;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Integer crear(CompetenciaRequest r, UserDetailsImpl actor) {
    List<Integer> ids = r.getProblemaIds().stream().distinct().toList();
    List<Problema> problemas = problemaRepository.findAllById(ids);
    if (problemas.size() != ids.size()) {
      throw new CompetenciaException("Alguno de los problemas seleccionados no existe");
    }
    for (Problema p : problemas) {
      if (!esDinamico(p)) {
        throw new CompetenciaException(
            "El problema \"" + p.getTitulo() + "\" no es dinamico; usa solo problemas dinamicos");
      }
    }
    Competencia c = new Competencia();
    c.setNombre(r.getNombre().trim());
    c.setDescripcion(r.getDescripcion() == null || r.getDescripcion().isBlank() ? null
        : r.getDescripcion().trim());
    c.setFechaInicio(r.getFechaInicio());
    c.setDuracionMinutos(r.getDuracionMinutos().shortValue());
    c.setEstado(EstadoCompetencia.PROGRAMADA);
    c.setCreador(usuarioRepository.getReferenceById(actor.getId()));
    c = competenciaRepository.save(c);
    short orden = 1;
    for (Integer pid : ids) {
      competenciaProblemaRepository.save(new CompetenciaProblema(c.getId(), pid, orden++));
    }
    return c.getId();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProblemaOpcion> problemasDinamicos() {
    return problemaRepository.findAll().stream().filter(CompetenciaServiceImpl::esDinamico)
        .map(p -> new ProblemaOpcion(p.getId(), p.getTitulo())).toList();
  }

  @Override
  @Transactional
  public List<CompetenciaItem> listar(Integer uid) {
    Map<Integer, Long> conteo = new HashMap<>();
    for (Object[] fila : participanteRepository.contarPorCompetencia()) {
      conteo.put((Integer) fila[0], ((Number) fila[1]).longValue());
    }
    Set<Integer> inscritas = new HashSet<>(participanteRepository.competenciasDe(uid));
    List<CompetenciaItem> lista = new ArrayList<>();
    for (Competencia c : competenciaRepository.listar()) {
      EstadoCompetencia e = estado(c);
      lista.add(new CompetenciaItem(c.getId(), c.getNombre(), c.getCreador().getUsername(), e,
          c.getFechaInicio(), c.fechaFin(), c.getDuracionMinutos(), conteo.getOrDefault(c.getId(), 0L),
          inscritas.contains(c.getId())));
    }
    return lista;
  }

  @Override
  @Transactional
  public CompetenciaDetalle detalle(Integer id, Integer uid) {
    Competencia c = competenciaRepository.buscar(id)
        .orElseThrow(() -> new CompetenciaException("La competencia no existe"));
    EstadoCompetencia e = estado(c);
    Optional<ParticipanteCompetencia> mi = participanteRepository.buscar(id, uid);
    Set<Integer> resueltos = mi.isPresent()
        ? new HashSet<>(intentoCompetenciaRepository.resueltos(id, uid)) : Set.of();
    List<ProblemaCompItem> problemas = new ArrayList<>();
    for (Object[] fila : competenciaProblemaRepository.problemas(id)) {
      Integer pid = (Integer) fila[0];
      problemas.add(new ProblemaCompItem(pid, (String) fila[1], ((Number) fila[2]).intValue(),
          resueltos.contains(pid)));
    }
    List<ParticipanteCompetencia> ps = new ArrayList<>(
        participanteRepository.porCompetencia(id).stream()
            .filter(p -> p.getIntentosTotales() > 0).toList());
    ps.sort(ORDEN);
    List<PosicionView> tabla = new ArrayList<>();
    for (int i = 0; i < ps.size(); i++) {
      ParticipanteCompetencia p = ps.get(i);
      int rango = 1;
      for (int j = 0; j < ps.size(); j++) {
        if (j != i && mejor(ps.get(j), p)) {
          rango++;
        }
      }
      tabla.add(new PosicionView(rango, p.getUsuario().getUsername(), p.getPuntuacionFinal(),
          p.getTiempoTotalSegundos(), p.getIntentosTotales(), p.getEloAntes(),
          p.getEloDespues()));
    }
    LocalDateTime ahora = LocalDateTime.now();
    long paraInicio = Math.max(0, Duration.between(ahora, c.getFechaInicio()).getSeconds());
    long paraFin = Math.max(0, Duration.between(ahora, c.fechaFin()).getSeconds());
    boolean puede = mi.isEmpty() && e != EstadoCompetencia.FINALIZADA
        && e != EstadoCompetencia.CANCELADA;
    return new CompetenciaDetalle(c.getId(), c.getNombre(), c.getDescripcion(),
        c.getCreador().getUsername(), c.getFechaInicio(), c.fechaFin(), c.getDuracionMinutos(), e,
        mi.isPresent(), puede, paraInicio, paraFin, problemas, tabla);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void inscribir(Integer id, Integer uid) {
    Competencia c = competenciaRepository.buscar(id)
        .orElseThrow(() -> new CompetenciaException("La competencia no existe"));
    EstadoCompetencia e = estado(c);
    if (e == EstadoCompetencia.FINALIZADA || e == EstadoCompetencia.CANCELADA) {
      throw new CompetenciaException("La competencia ya no admite inscripciones");
    }
    if (participanteRepository.buscar(id, uid).isPresent()) {
      throw new CompetenciaException("Ya estas inscrito en esta competencia");
    }
    ParticipanteCompetencia p = new ParticipanteCompetencia();
    p.setCompetencia(c);
    p.setUsuario(usuarioRepository.getReferenceById(uid));
    participanteRepository.save(p);
  }

  @Override
  @Transactional
  public long validarAcceso(Integer cid, Integer pid, Integer uid) {
    Competencia c = competenciaRepository.buscar(cid)
        .orElseThrow(() -> new CompetenciaException("La competencia no existe"));
    if (estado(c) != EstadoCompetencia.EN_CURSO) {
      throw new CompetenciaException("La competencia no esta en curso");
    }
    if (participanteRepository.buscar(cid, uid).isEmpty()) {
      throw new CompetenciaException("Debes inscribirte antes de participar");
    }
    if (!competenciaProblemaRepository.existsById(new CompetenciaProblema.PK(cid, pid))) {
      throw new CompetenciaException("El problema no pertenece a esta competencia");
    }
    if (intentoCompetenciaRepository.correctos(cid, uid, pid) > 0) {
      throw new CompetenciaException("Ya resolviste este problema");
    }
    return Math.max(0, Duration.between(LocalDateTime.now(), c.fechaFin()).getSeconds());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean enviar(Integer cid, ProblemaPreparado p, BigDecimal respuesta,
      UserDetailsImpl actor) {
    Integer uid = actor.getId();
    validarAcceso(cid, p.problemaId(), uid);
    Competencia c = competenciaRepository.buscar(cid).orElseThrow();
    ParticipanteCompetencia part = participanteRepository.buscar(cid, uid).orElseThrow();
    boolean ok = respuesta.subtract(p.resultado()).abs().compareTo(p.tolerancia()) <= 0;

    Intento i = new Intento();
    i.setUsuario(usuarioRepository.getReferenceById(uid));
    i.setProblema(problemaRepository.getReferenceById(p.problemaId()));
    i.setCompetenciaId(cid);
    i.setRespuestaEnviada(respuesta);
    i.setResultado(ok ? ResultadoIntento.CORRECTO : ResultadoIntento.INCORRECTO);
    i.setValoresGenerados(p.valoresJson());
    i.setTipo(TipoIntento.COMPETENCIA);
    intentoCompetenciaRepository.save(i);

    LocalDateTime ahora = LocalDateTime.now();
    if (part.getFechaInicioParticipacion() == null) {
      part.setFechaInicioParticipacion(ahora);
    }
    part.setIntentosTotales((short) (part.getIntentosTotales() + 1));
    if (ok) {
      int seg = (int) Duration.between(c.getFechaInicio(), ahora).getSeconds();
      part.setPuntuacionFinal(part.getPuntuacionFinal() + 1);
      part.setTiempoTotalSegundos(
          (part.getTiempoTotalSegundos() == null ? 0 : part.getTiempoTotalSegundos()) + seg);
    }
    participanteRepository.save(part);
    return ok;
  }
}
