package com.srpf.service;

import com.srpf.dto.MensajeView;
import com.srpf.exception.MensajeInvalidoException;
import com.srpf.exception.UsuarioNotFoundException;
import java.util.List;

public interface MensajeService {
  List<MensajeView> mios(Integer usuarioId);

  List<MensajeView> conversacion(Integer yo, Integer otro);

  MensajeView enviar(Integer remitenteId, String destinatarioUsername, String contenido)
      throws UsuarioNotFoundException, MensajeInvalidoException;
}
