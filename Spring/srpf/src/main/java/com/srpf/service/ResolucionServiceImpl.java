package com.srpf.service;

import com.srpf.entity.Intento;
import com.srpf.entity.Problema;
import com.srpf.enums.ResultadoIntento;
import com.srpf.enums.TipoIntento;
import com.srpf.repository.IntentoRepository;
import com.srpf.repository.ProblemaRepository;
import com.srpf.repository.UsuarioRepository;
import com.srpf.dto.IntentoView;
import com.srpf.dto.ProblemaPreparado;
import com.srpf.exception.ProblemaInvalidoException;
import com.srpf.exception.ProblemaNotFoundException;
import com.srpf.security.model.UserDetailsImpl;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResolucionServiceImpl implements ResolucionService {

  private final ProblemaRepository problemaRepository;
  private final UsuarioRepository usuarioRepository;
  private final IntentoRepository intentoRepository;

  @Autowired
  public ResolucionServiceImpl(ProblemaRepository problemaRepository,
      UsuarioRepository usuarioRepository, IntentoRepository intentoRepository) {
    this.problemaRepository = problemaRepository;
    this.usuarioRepository = usuarioRepository;
    this.intentoRepository = intentoRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public ProblemaPreparado preparar(Integer id)
      throws ProblemaNotFoundException, ProblemaInvalidoException {
    Problema p = problemaRepository.findWithAutorById(id)
        .orElseThrow(() -> new ProblemaNotFoundException(id));
    try {
      GeneradorProblema.Generado g = GeneradorProblema.generar(p.getEnunciado(),
          p.getResultadoEsperado(), p.getParametrosDinamicos());
      return new ProblemaPreparado(p.getId(), p.getTitulo(), g.enunciado(), p.getTema(),
          p.getNivel(), g.resultado(), p.getMargenTolerancia(), g.valoresJson());
    } catch (IllegalArgumentException e) {
      throw new ProblemaInvalidoException("parametrosDinamicos", e.getMessage());
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean enviar(ProblemaPreparado p, BigDecimal respuesta, UserDetailsImpl actor) {
    boolean ok = respuesta.subtract(p.resultado()).abs().compareTo(p.tolerancia()) <= 0;
    Intento i = new Intento();
    i.setUsuario(usuarioRepository.getReferenceById(actor.getId()));
    i.setProblema(problemaRepository.getReferenceById(p.problemaId()));
    i.setRespuestaEnviada(respuesta);
    i.setResultado(ok ? ResultadoIntento.CORRECTO : ResultadoIntento.INCORRECTO);
    i.setValoresGenerados(p.valoresJson());
    i.setTipo(TipoIntento.GIMNASIO);
    intentoRepository.save(i);
    return ok;
  }

  @Override
  @Transactional(readOnly = true)
  public List<IntentoView> misIntentos(Integer usuarioId) {
    return intentoRepository.porUsuario(usuarioId).stream()
        .map(i -> new IntentoView(i.getId(), i.getProblema().getId(), i.getProblema().getTitulo(),
            i.getRespuestaEnviada(), i.getResultado(), i.getTipo(), i.getFechaHora()))
        .toList();
  }
}
