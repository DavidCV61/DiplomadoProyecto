package com.srpf.controller;

import com.srpf.dto.ForoPublicacionRequest;
import com.srpf.dto.ForoRespuestaRequest;
import com.srpf.exception.PublicacionNotFoundException;
import com.srpf.security.model.UserDetailsImpl;
import com.srpf.service.ForoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ForoController {

  private final ForoService foroService;

  @Autowired
  public ForoController(ForoService foroService) {
    this.foroService = foroService;
  }

  @GetMapping("/foro")
  public String listar(Model model) {
    model.addAttribute("publicaciones", foroService.listar());
    return "foro";
  }

  @GetMapping("/foro/nueva")
  public String nueva(@AuthenticationPrincipal UserDetailsImpl actor, Model model) {
    if (actor == null) {
      return "redirect:/login";
    }
    model.addAttribute("publicacion", new ForoPublicacionRequest());
    return "foro/nueva";
  }

  @PostMapping("/foro")
  public String publicar(@Valid @ModelAttribute("publicacion") ForoPublicacionRequest publicacion,
      BindingResult result, @AuthenticationPrincipal UserDetailsImpl actor,
      RedirectAttributes flash) {
    if (actor == null) {
      return "redirect:/login";
    }
    if (result.hasErrors()) {
      return "foro/nueva";
    }
    Long id = foroService.publicar(publicacion, actor);
    flash.addFlashAttribute("mensaje", "Publicacion creada.");
    return "redirect:/foro/" + id;
  }

  @GetMapping("/foro/{id}")
  public String detalle(@PathVariable Long id, Model model) throws PublicacionNotFoundException {
    model.addAttribute("publicacion", foroService.detalle(id));
    model.addAttribute("respuesta", new ForoRespuestaRequest());
    return "foro/detalle";
  }

  @PostMapping("/foro/{id}/responder")
  public String responder(@PathVariable Long id,
      @Valid @ModelAttribute("respuesta") ForoRespuestaRequest respuesta,
      BindingResult result, @AuthenticationPrincipal UserDetailsImpl actor,
      Model model, RedirectAttributes flash) throws PublicacionNotFoundException {
    if (actor == null) {
      return "redirect:/login";
    }
    if (result.hasErrors()) {
      model.addAttribute("publicacion", foroService.detalle(id));
      return "foro/detalle";
    }
    foroService.responder(id, respuesta, actor);
    flash.addFlashAttribute("mensaje", "Respuesta publicada.");
    return "redirect:/foro/" + id;
  }
}
