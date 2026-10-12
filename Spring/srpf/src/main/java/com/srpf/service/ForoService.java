package com.srpf.service;

import com.srpf.dto.ForoDetalle;
import com.srpf.dto.ForoPublicacionRequest;
import com.srpf.dto.ForoRespuestaRequest;
import com.srpf.dto.ForoResumen;
import com.srpf.exception.PublicacionNotFoundException;
import com.srpf.security.model.UserDetailsImpl;
import java.util.List;

public interface ForoService {
  List<ForoResumen> listar();

  ForoDetalle detalle(Long id) throws PublicacionNotFoundException;

  Long publicar(ForoPublicacionRequest request, UserDetailsImpl actor);

  void responder(Long id, ForoRespuestaRequest request, UserDetailsImpl actor)
      throws PublicacionNotFoundException;
}
