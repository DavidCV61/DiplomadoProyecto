package com.srpf.service;

import com.srpf.entity.Mensaje;
import com.srpf.entity.Usuario;
import com.srpf.repository.MensajeRepository;
import com.srpf.repository.PerfilRepository;
import com.srpf.repository.UsuarioRepository;
import com.srpf.dto.MensajeView;
import com.srpf.exception.MensajeInvalidoException;
import com.srpf.exception.UsuarioNotFoundException;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MensajeServiceImpl implements MensajeService {

  private final MensajeRepository mensajeRepository;
  private final PerfilRepository perfilRepository;
  private final UsuarioRepository usuarioRepository;

  @Autowired
  public MensajeServiceImpl(MensajeRepository mensajeRepository,
      PerfilRepository perfilRepository, UsuarioRepository usuarioRepository) {
    this.mensajeRepository = mensajeRepository;
    this.perfilRepository = perfilRepository;
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<MensajeView> mios(Integer usuarioId) {
    return mensajeRepository.deUsuario(usuarioId).stream().map(this::aVista).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<MensajeView> conversacion(Integer yo, Integer otro) {
    return mensajeRepository.entre(yo, otro).stream().map(this::aVista).toList();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public MensajeView enviar(Integer remitenteId, String destinatarioUsername, String contenido)
      throws UsuarioNotFoundException, MensajeInvalidoException {
    String nombre = destinatarioUsername == null ? ""
        : destinatarioUsername.trim().toLowerCase(Locale.ROOT);
    Usuario destinatario = perfilRepository.porUsername(nombre)
        .orElseThrow(() -> new UsuarioNotFoundException(nombre));
    if (destinatario.getId().equals(remitenteId)) {
      throw new MensajeInvalidoException("No puedes enviarte mensajes a ti mismo");
    }
    String texto = contenido == null ? "" : contenido.trim();
    if (texto.isEmpty()) {
      throw new MensajeInvalidoException("El mensaje no puede estar vacio");
    }
    if (texto.length() > 2000) {
      throw new MensajeInvalidoException("El mensaje no puede pasar de 2000 caracteres");
    }
    Mensaje m = new Mensaje();
    m.setRemitente(usuarioRepository.getReferenceById(remitenteId));
    m.setDestinatario(destinatario);
    m.setContenido(texto);
    mensajeRepository.saveAndFlush(m);
    return aVista(m);
  }

  private MensajeView aVista(Mensaje m) {
    return new MensajeView(m.getId(), m.getRemitente().getUsername(), m.getRemitente().getNombre(),
        m.getDestinatario().getUsername(), m.getDestinatario().getNombre(), m.getContenido(),
        m.getFechaHora());
  }
}
