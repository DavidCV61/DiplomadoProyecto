package com.srpf.controller;

import com.srpf.dto.RegistroRequest;
import com.srpf.exception.UsuarioDuplicadoException;
import com.srpf.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
  private final UsuarioService usuarioService;

  @Autowired
  public HomeController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @GetMapping("/")
  public String home() {
    return "index";
  }

  @GetMapping("/index")
  public String index() {
    return "redirect:/";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/acceso-denegado")
  public String accesoDenegado() {
    return "acceso-denegado";
  }

  @GetMapping("/registro")
  public String mostrarRegistro(Authentication authentication, Model model) {
    if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
      return "redirect:/perfil";
    }
    model.addAttribute("registro", new RegistroRequest());
    return "registro";
  }

  @PostMapping("/registro")
  public String procesarRegistro(@Valid @ModelAttribute("registro") RegistroRequest registro,
      BindingResult result) {
    if (registro.getPassword() != null
        && !registro.getPassword().equals(registro.getConfirmarPassword())) {
      result.rejectValue("confirmarPassword", "no.coincide", "Las contrasenas no coinciden");
    }
    if (!result.hasErrors()) {
      try {
        usuarioService.registrar(registro);
        return "redirect:/login?registrado";
      } catch (UsuarioDuplicadoException e) {
        String mensaje = "email".equals(e.getCampo())
            ? "Ese correo ya esta registrado"
            : "Ese nombre de usuario ya existe";
        result.rejectValue(e.getCampo(), "duplicado", mensaje);
      }
    }
    registro.setPassword(null);
    registro.setConfirmarPassword(null);
    return "registro";
  }




}
