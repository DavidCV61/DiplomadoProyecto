package com.srpf.service;

import com.srpf.entity.Usuario;
import com.srpf.repository.UsuarioRepository;
import com.srpf.enums.Rol;
import com.srpf.exception.RolInvalidoException;
import com.srpf.exception.UsuarioNotFoundException;
import com.srpf.security.model.UserDetailsImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminUsuarioServiceImpl implements AdminUsuarioService {

  private final UsuarioRepository usuarioRepository;

  @Autowired
  public AdminUsuarioServiceImpl(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Usuario> listar() {
    return usuarioRepository.findAll(Sort.by("id"));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void cambiarRol(Integer id, Rol rol, UserDetailsImpl actor)
      throws UsuarioNotFoundException, RolInvalidoException {
    if (actor.getRol() != Rol.ADMIN) {
      throw new org.springframework.security.access.AccessDeniedException("Solo un ADMIN cambia roles");
    }
    if (actor.getId().equals(id)) {
      throw new RolInvalidoException("No puedes cambiar tu propio rol");
    }
    Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id));
    u.setRol(rol);
    usuarioRepository.saveAndFlush(u);
  }
}
