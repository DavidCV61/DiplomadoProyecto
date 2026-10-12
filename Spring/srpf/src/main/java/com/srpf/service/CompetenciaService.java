package com.srpf.service;

import com.srpf.dto.CompetenciaDetalle;
import com.srpf.dto.CompetenciaItem;
import com.srpf.dto.CompetenciaRequest;
import com.srpf.dto.ProblemaOpcion;
import com.srpf.dto.ProblemaPreparado;
import com.srpf.security.model.UserDetailsImpl;
import java.math.BigDecimal;
import java.util.List;

public interface CompetenciaService {
  Integer crear(CompetenciaRequest request, UserDetailsImpl actor);

  List<ProblemaOpcion> problemasDinamicos();

  List<CompetenciaItem> listar(Integer usuarioId);

  CompetenciaDetalle detalle(Integer id, Integer usuarioId);

  void inscribir(Integer id, Integer usuarioId);

  long validarAcceso(Integer competenciaId, Integer problemaId, Integer usuarioId);

  boolean enviar(Integer competenciaId, ProblemaPreparado preparado, BigDecimal respuesta,
      UserDetailsImpl actor);
}
