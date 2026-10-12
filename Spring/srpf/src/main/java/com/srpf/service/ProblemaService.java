package com.srpf.service;

import com.srpf.entity.Problema;
import com.srpf.enums.Nivel;
import com.srpf.enums.Tema;
import com.srpf.dto.ProblemaRequest;
import com.srpf.dto.ProblemaResponse;
import com.srpf.exception.ProblemaInvalidoException;
import com.srpf.exception.ProblemaNotFoundException;
import com.srpf.security.model.UserDetailsImpl;
import java.util.List;

public interface ProblemaService {
  List<Problema> filtrar(Tema tema, Nivel nivel);

  ProblemaResponse obtener(Integer id) throws ProblemaNotFoundException;

  ProblemaRequest obtenerParaEdicion(Integer id, UserDetailsImpl actor)
      throws ProblemaNotFoundException;

  Integer crear(ProblemaRequest request, UserDetailsImpl actor)
      throws ProblemaInvalidoException;

  void actualizar(Integer id, ProblemaRequest request, UserDetailsImpl actor)
      throws ProblemaNotFoundException, ProblemaInvalidoException;

  boolean puedeEditar(Integer autorId, UserDetailsImpl actor);
}
