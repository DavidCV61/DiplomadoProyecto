package com.srpf.service;

import com.srpf.dto.PerfilPublicoDTO;
import com.srpf.exception.UsuarioNotFoundException;
import java.util.List;

public interface PerfilService {
  List<PerfilPublicoDTO> buscar(String q);

  PerfilPublicoDTO perfil(String username) throws UsuarioNotFoundException;

  List<PerfilPublicoDTO> estadisticas();
}
