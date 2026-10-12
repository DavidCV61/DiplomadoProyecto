package com.srpf.service;

import com.srpf.dto.IntentoView;
import com.srpf.dto.ProblemaPreparado;
import com.srpf.exception.ProblemaInvalidoException;
import com.srpf.exception.ProblemaNotFoundException;
import com.srpf.security.model.UserDetailsImpl;
import java.math.BigDecimal;
import java.util.List;

public interface ResolucionService {
  ProblemaPreparado preparar(Integer problemaId)
      throws ProblemaNotFoundException, ProblemaInvalidoException;

  boolean enviar(ProblemaPreparado preparado, BigDecimal respuesta, UserDetailsImpl actor);

  List<IntentoView> misIntentos(Integer usuarioId);
}
