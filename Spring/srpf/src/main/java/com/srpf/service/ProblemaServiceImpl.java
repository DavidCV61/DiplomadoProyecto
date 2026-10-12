package com.srpf.service;

import com.srpf.entity.Problema;
import com.srpf.enums.Nivel;
import com.srpf.repository.ProblemaRepository;
import com.srpf.repository.UsuarioRepository;
import com.srpf.enums.Rol;
import com.srpf.enums.Tema;
import com.srpf.dto.ProblemaRequest;
import com.srpf.dto.ProblemaResponse;
import com.srpf.exception.ProblemaInvalidoException;
import com.srpf.exception.ProblemaNotFoundException;
import com.srpf.security.model.UserDetailsImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProblemaServiceImpl implements ProblemaService {

  private final ProblemaRepository problemaRepository;
  private final UsuarioRepository usuarioRepository;

  @Autowired
  public ProblemaServiceImpl(ProblemaRepository problemaRepository,
      UsuarioRepository usuarioRepository) {
    this.problemaRepository = problemaRepository;
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Problema> filtrar(Tema tema, Nivel nivel) {
    return problemaRepository.filtrar(tema, nivel);
  }

  @Override
  @Transactional(readOnly = true)
  public ProblemaResponse obtener(Integer id) throws ProblemaNotFoundException {
    Problema p = buscar(id);
    return new ProblemaResponse(p.getId(), p.getTitulo(), p.getEnunciado(), p.getTema(),
        p.getNivel(), p.getAutor().getId(), p.getAutor().getNombre(), p.getFechaCreacion());
  }

  @Override
  @Transactional(readOnly = true)
  public ProblemaRequest obtenerParaEdicion(Integer id, UserDetailsImpl actor)
      throws ProblemaNotFoundException {
    Problema p = buscar(id);
    verificarPropiedad(p, actor);
    ProblemaRequest r = new ProblemaRequest();
    r.setTitulo(p.getTitulo());
    r.setEnunciado(p.getEnunciado());
    r.setTema(p.getTema());
    r.setNivel(p.getNivel());
    r.setResultadoEsperado(p.getResultadoEsperado());
    r.setMargenTolerancia(p.getMargenTolerancia());
    r.setParametrosDinamicos(p.getParametrosDinamicos());
    return r;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Integer crear(ProblemaRequest request, UserDetailsImpl actor)
      throws ProblemaInvalidoException {
    Problema p = new Problema();
    p.setAutor(usuarioRepository.getReferenceById(actor.getId()));
    copiar(p, request);
    guardar(p);
    return p.getId();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void actualizar(Integer id, ProblemaRequest request, UserDetailsImpl actor)
      throws ProblemaNotFoundException, ProblemaInvalidoException {
    Problema p = buscar(id);
    verificarPropiedad(p, actor);
    copiar(p, request);
    guardar(p);
  }

  @Override
  public boolean puedeEditar(Integer autorId, UserDetailsImpl actor) {
    if (actor == null) {
      return false;
    }
    return actor.getRol() == Rol.ADMIN
        || (actor.getRol() == Rol.AUTOR && actor.getId().equals(autorId));
  }

  private Problema buscar(Integer id) throws ProblemaNotFoundException {
    return problemaRepository.findWithAutorById(id)
        .orElseThrow(() -> new ProblemaNotFoundException(id));
  }

  private void verificarPropiedad(Problema p, UserDetailsImpl actor) {
    if (!puedeEditar(p.getAutor().getId(), actor)) {
      throw new AccessDeniedException("No puedes editar este problema");
    }
  }

  private void copiar(Problema p, ProblemaRequest r) {
    p.setTitulo(r.getTitulo().trim());
    p.setEnunciado(r.getEnunciado().trim());
    p.setTema(r.getTema());
    p.setNivel(r.getNivel());
    p.setResultadoEsperado(r.getResultadoEsperado());
    p.setMargenTolerancia(r.getMargenTolerancia());
    String json = r.getParametrosDinamicos();
    p.setParametrosDinamicos(json == null || json.isBlank() ? null : json.trim());
  }

  private void guardar(Problema p) throws ProblemaInvalidoException {
    try {
      GeneradorProblema.validar(p.getParametrosDinamicos(), p.getResultadoEsperado());
    } catch (IllegalArgumentException e) {
      throw new ProblemaInvalidoException("parametrosDinamicos", e.getMessage());
    }
    try {
      problemaRepository.saveAndFlush(p);
    } catch (DataIntegrityViolationException e) {
      throw new ProblemaInvalidoException("parametrosDinamicos",
          "Los parametros deben ser un JSON valido");
    }
  }
}
