package com.srpf.service;

import com.srpf.entity.ForoPublicacion;
import com.srpf.entity.ForoRespuesta;
import com.srpf.repository.ForoPublicacionRepository;
import com.srpf.repository.ForoRespuestaRepository;
import com.srpf.repository.UsuarioRepository;
import com.srpf.dto.ForoDetalle;
import com.srpf.dto.ForoPublicacionRequest;
import com.srpf.dto.ForoRespuestaRequest;
import com.srpf.dto.ForoRespuestaView;
import com.srpf.dto.ForoResumen;
import com.srpf.exception.PublicacionNotFoundException;
import com.srpf.security.model.UserDetailsImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ForoServiceImpl implements ForoService {

  private final ForoPublicacionRepository publicacionRepository;
  private final ForoRespuestaRepository respuestaRepository;
  private final UsuarioRepository usuarioRepository;

  @Autowired
  public ForoServiceImpl(ForoPublicacionRepository publicacionRepository,
      ForoRespuestaRepository respuestaRepository, UsuarioRepository usuarioRepository) {
    this.publicacionRepository = publicacionRepository;
    this.respuestaRepository = respuestaRepository;
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ForoResumen> listar() {
    Map<Long, Long> conteo = new HashMap<>();
    for (Object[] fila : respuestaRepository.contarPorPublicacion()) {
      conteo.put(((Number) fila[0]).longValue(), ((Number) fila[1]).longValue());
    }
    return publicacionRepository.listar().stream()
        .map(p -> new ForoResumen(p.getId(), p.getTitulo(), p.getUsuario().getNombre(),
            p.getFechaPublicacion(), conteo.getOrDefault(p.getId(), 0L)))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public ForoDetalle detalle(Long id) throws PublicacionNotFoundException {
    ForoPublicacion p = publicacionRepository.buscar(id)
        .orElseThrow(() -> new PublicacionNotFoundException(id));
    List<ForoRespuestaView> respuestas = respuestaRepository.porPublicacion(id).stream()
        .map(r -> new ForoRespuestaView(r.getUsuario().getNombre(), r.getContenido(),
            r.getFechaRespuesta()))
        .toList();
    return new ForoDetalle(p.getId(), p.getTitulo(), p.getContenido(),
        p.getUsuario().getNombre(), p.getFechaPublicacion(), respuestas);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Long publicar(ForoPublicacionRequest request, UserDetailsImpl actor) {
    ForoPublicacion p = new ForoPublicacion();
    p.setUsuario(usuarioRepository.getReferenceById(actor.getId()));
    p.setTitulo(request.getTitulo().trim());
    p.setContenido(request.getContenido().trim());
    return publicacionRepository.saveAndFlush(p).getId();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void responder(Long id, ForoRespuestaRequest request, UserDetailsImpl actor)
      throws PublicacionNotFoundException {
    ForoPublicacion p = publicacionRepository.findById(id)
        .orElseThrow(() -> new PublicacionNotFoundException(id));
    ForoRespuesta r = new ForoRespuesta();
    r.setPublicacion(p);
    r.setUsuario(usuarioRepository.getReferenceById(actor.getId()));
    r.setContenido(request.getContenido().trim());
    respuestaRepository.saveAndFlush(r);
  }
}
