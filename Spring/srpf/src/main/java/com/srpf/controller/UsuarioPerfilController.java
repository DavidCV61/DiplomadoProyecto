package com.srpf.controller;

import com.srpf.dto.MensajeRequest;
import com.srpf.dto.PerfilPublicoDTO;
import com.srpf.exception.MensajeInvalidoException;
import com.srpf.exception.UsuarioNotFoundException;
import com.srpf.security.model.UserDetailsImpl;
import com.srpf.service.MensajeService;
import com.srpf.service.PerfilService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioPerfilController {

  private final PerfilService perfilService;
  private final MensajeService mensajeService;

  @Autowired
  public UsuarioPerfilController(PerfilService perfilService, MensajeService mensajeService) {
    this.perfilService = perfilService;
    this.mensajeService = mensajeService;
  }

  @GetMapping("/usuarios")
  public String buscar(@RequestParam(required = false, defaultValue = "") String q,
      @AuthenticationPrincipal UserDetailsImpl actor, Model model) {
    if (actor == null) {
      return "redirect:/login";
    }
    model.addAttribute("q", q);
    model.addAttribute("usuarios", perfilService.buscar(q));
    return "usuarios/lista";
  }

  @GetMapping("/usuarios/{username}")
  public String perfil(@PathVariable String username,
      @AuthenticationPrincipal UserDetailsImpl actor, Model model)
      throws UsuarioNotFoundException {
    if (actor == null) {
      return "redirect:/login";
    }
    cargar(model, username, actor);
    model.addAttribute("mensajeForm", new MensajeRequest());
    return "usuarios/perfil";
  }

  @PostMapping("/usuarios/{username}/mensajes")
  public String enviar(@PathVariable String username,
      @Valid @ModelAttribute("mensajeForm") MensajeRequest mensajeForm, BindingResult result,
      @AuthenticationPrincipal UserDetailsImpl actor, Model model, RedirectAttributes flash)
      throws UsuarioNotFoundException {
    if (actor == null) {
      return "redirect:/login";
    }
    if (!result.hasErrors()) {
      try {
        mensajeService.enviar(actor.getId(), username, mensajeForm.getContenido());
        flash.addFlashAttribute("enviado", true);
        return "redirect:/usuarios/" + username;
      } catch (MensajeInvalidoException e) {
        result.rejectValue("contenido", "invalido", e.getMessage());
      }
    }
    cargar(model, username, actor);
    return "usuarios/perfil";
  }

  @GetMapping("/mensajes")
  public String bandeja(@AuthenticationPrincipal UserDetailsImpl actor, Model model) {
    if (actor == null) {
      return "redirect:/login";
    }
    model.addAttribute("mensajes", mensajeService.mios(actor.getId()));
    return "mensajes/bandeja";
  }

  private void cargar(Model model, String username, UserDetailsImpl actor)
      throws UsuarioNotFoundException {
    PerfilPublicoDTO p = perfilService.perfil(username);
    boolean esYo = p.id().equals(actor.getId());
    model.addAttribute("perfil", p);
    model.addAttribute("esYo", esYo);
    model.addAttribute("conversacion",
        esYo ? List.of() : mensajeService.conversacion(actor.getId(), p.id()));
  }
}
