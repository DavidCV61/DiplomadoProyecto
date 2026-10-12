package com.srpf.controller;

import com.srpf.enums.Rol;
import com.srpf.exception.RolInvalidoException;
import com.srpf.exception.UsuarioNotFoundException;
import com.srpf.security.model.UserDetailsImpl;
import com.srpf.service.AdminUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

  private final AdminUsuarioService adminUsuarioService;

  @Autowired
  public AdminController(AdminUsuarioService adminUsuarioService) {
    this.adminUsuarioService = adminUsuarioService;
  }

  @GetMapping("/admin/usuarios")
  public String usuarios(Model model) {
    model.addAttribute("usuarios", adminUsuarioService.listar());
    model.addAttribute("roles", Rol.values());
    return "admin/usuarios";
  }

  @PostMapping("/admin/usuarios/{id}/rol")
  public String cambiarRol(@PathVariable Integer id, @RequestParam Rol rol,
      @AuthenticationPrincipal UserDetailsImpl actor, RedirectAttributes flash) {
    try {
      adminUsuarioService.cambiarRol(id, rol, actor);
      flash.addFlashAttribute("mensaje", "Rol actualizado correctamente.");
    } catch (UsuarioNotFoundException e) {
      flash.addFlashAttribute("error", "El usuario no existe.");
    } catch (RolInvalidoException e) {
      flash.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/admin/usuarios";
  }
}
