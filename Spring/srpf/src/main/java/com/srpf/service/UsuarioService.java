package com.srpf.service;

import com.srpf.dto.PerfilResponse;
import com.srpf.dto.RegistroRequest;
import com.srpf.exception.UsuarioDuplicadoException;
import com.srpf.exception.UsuarioNotFoundException;

public interface UsuarioService {

  PerfilResponse obtenerPerfil(String username) throws UsuarioNotFoundException;

  void registrar(RegistroRequest request) throws UsuarioDuplicadoException;
}
