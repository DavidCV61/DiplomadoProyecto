package com.srpf.controller;

import com.srpf.exception.UsuarioNotFoundException;
import com.srpf.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController {
  private final UsuarioService usuarioService;

  @Autowired
  public UsuarioController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @GetMapping("/perfil")
  public String perfil(Authentication authentication, Model model) {
    try {
      model.addAttribute("perfil", usuarioService.obtenerPerfil(authentication.getName()));
      return "usuario/perfil";
    } catch (UsuarioNotFoundException e) {
      return "redirect:/login";
    }
  }
}
