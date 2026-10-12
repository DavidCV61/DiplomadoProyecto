package com.srpf.service;

import com.srpf.entity.Usuario;
import com.srpf.enums.Rol;
import com.srpf.exception.RolInvalidoException;
import com.srpf.exception.UsuarioNotFoundException;
import com.srpf.security.model.UserDetailsImpl;
import java.util.List;

public interface AdminUsuarioService {
  List<Usuario> listar();

  void cambiarRol(Integer id, Rol rol, UserDetailsImpl actor)
      throws UsuarioNotFoundException, RolInvalidoException;
}
