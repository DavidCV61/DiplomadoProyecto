package com.srpf.controller;

import com.srpf.dto.ProblemaPreparado;
import com.srpf.dto.RespuestaRequest;
import com.srpf.exception.ProblemaInvalidoException;
import com.srpf.exception.ProblemaNotFoundException;
import com.srpf.security.model.UserDetailsImpl;
import com.srpf.service.ResolucionService;
import jakarta.servlet.http.HttpSession;
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
public class ResolverController {

  private final ResolucionService resolucionService;

  @Autowired
  public ResolverController(ResolucionService resolucionService) {
    this.resolucionService = resolucionService;
  }

  private static String clave(Integer id) {
    return "resolucion_" + id;
  }

  @GetMapping("/problemas/{id}/resolver")
  public String formulario(@PathVariable Integer id,
      @AuthenticationPrincipal UserDetailsImpl actor, HttpSession session, Model model)
      throws ProblemaNotFoundException {
    if (actor == null) {
      return "redirect:/login";
    }
    model.addAttribute("respuesta", new RespuestaRequest());
    try {
      ProblemaPreparado p = resolucionService.preparar(id);
      session.setAttribute(clave(id), p);
      model.addAttribute("preparado", p);
    } catch (ProblemaInvalidoException e) {
      model.addAttribute("errorProblema",
          "Este problema tiene parametros invalidos: " + e.getMessage());
    }
    return "problema/resolver";
  }

  @PostMapping("/problemas/{id}/resolver")
  public String enviar(@PathVariable Integer id,
      @Valid @ModelAttribute("respuesta") RespuestaRequest respuesta, BindingResult result,
      @AuthenticationPrincipal UserDetailsImpl actor, HttpSession session, Model model,
      RedirectAttributes flash) {
    if (actor == null) {
      return "redirect:/login";
    }
    ProblemaPreparado p = (ProblemaPreparado) session.getAttribute(clave(id));
    if (p == null) {
      return "redirect:/problemas/" + id + "/resolver";
    }
    if (result.hasErrors()) {
      model.addAttribute("preparado", p);
      return "problema/resolver";
    }
    boolean ok = resolucionService.enviar(p, respuesta.getRespuesta(), actor);
    session.removeAttribute(clave(id));
    flash.addFlashAttribute("resultado", ok ? "ok" : "mal");
    return "redirect:/problemas/" + id + "/resolver";
  }
}
