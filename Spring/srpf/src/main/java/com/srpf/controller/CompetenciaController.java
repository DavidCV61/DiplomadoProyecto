package com.srpf.controller;

import com.srpf.dto.CompetenciaRequest;
import com.srpf.dto.ProblemaPreparado;
import com.srpf.dto.RespuestaRequest;
import com.srpf.exception.CompetenciaException;
import com.srpf.exception.ProblemaInvalidoException;
import com.srpf.exception.ProblemaNotFoundException;
import com.srpf.security.model.UserDetailsImpl;
import com.srpf.service.CompetenciaService;
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
public class CompetenciaController {

  private final CompetenciaService competenciaService;
  private final ResolucionService resolucionService;

  @Autowired
  public CompetenciaController(CompetenciaService competenciaService,
      ResolucionService resolucionService) {
    this.competenciaService = competenciaService;
    this.resolucionService = resolucionService;
  }

  private static boolean puedeCrear(UserDetailsImpl actor) {
    return actor.getAuthorities().stream().anyMatch(
        a -> a.getAuthority().endsWith("AUTOR") || a.getAuthority().endsWith("ADMIN"));
  }

  private static String clave(Integer cid, Integer pid) {
    return "competencia_" + cid + "_" + pid;
  }

  @GetMapping("/competencias")
  public String listar(@AuthenticationPrincipal UserDetailsImpl actor, Model model) {
    if (actor == null) {
      return "redirect:/login";
    }
    model.addAttribute("competencias", competenciaService.listar(actor.getId()));
    model.addAttribute("puedeCrear", puedeCrear(actor));
    return "competencias";
  }

  @GetMapping("/competencias/{id}")
  public String detalle(@PathVariable Integer id, @AuthenticationPrincipal UserDetailsImpl actor,
      Model model, RedirectAttributes flash) {
    if (actor == null) {
      return "redirect:/login";
    }
    try {
      model.addAttribute("c", competenciaService.detalle(id, actor.getId()));
      return "competencias/detalle";
    } catch (CompetenciaException e) {
      flash.addFlashAttribute("error", e.getMessage());
      return "redirect:/competencias";
    }
  }

  @PostMapping("/competencias/{id}/inscribir")
  public String inscribir(@PathVariable Integer id, @AuthenticationPrincipal UserDetailsImpl actor,
      RedirectAttributes flash) {
    if (actor == null) {
      return "redirect:/login";
    }
    try {
      competenciaService.inscribir(id, actor.getId());
      flash.addFlashAttribute("ok", "Inscripcion realizada");
    } catch (CompetenciaException e) {
      flash.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/competencias/" + id;
  }

  @GetMapping("/competencias/{cid}/problemas/{pid}/resolver")
  public String resolver(@PathVariable Integer cid, @PathVariable Integer pid,
      @AuthenticationPrincipal UserDetailsImpl actor, HttpSession session, Model model,
      RedirectAttributes flash) {
    if (actor == null) {
      return "redirect:/login";
    }
    try {
      long restante = competenciaService.validarAcceso(cid, pid, actor.getId());
      ProblemaPreparado p = resolucionService.preparar(pid);
      session.setAttribute(clave(cid, pid), p);
      model.addAttribute("preparado", p);
      model.addAttribute("cid", cid);
      model.addAttribute("restante", restante);
      model.addAttribute("respuesta", new RespuestaRequest());
      return "competencias/resolver";
    } catch (CompetenciaException e) {
      flash.addFlashAttribute("error", e.getMessage());
      return "redirect:/competencias/" + cid;
    } catch (ProblemaNotFoundException | ProblemaInvalidoException e) {
      flash.addFlashAttribute("error", "El problema no esta disponible: " + e.getMessage());
      return "redirect:/competencias/" + cid;
    }
  }

  @PostMapping("/competencias/{cid}/problemas/{pid}/resolver")
  public String enviar(@PathVariable Integer cid, @PathVariable Integer pid,
      @Valid @ModelAttribute("respuesta") RespuestaRequest respuesta, BindingResult result,
      @AuthenticationPrincipal UserDetailsImpl actor, HttpSession session, Model model,
      RedirectAttributes flash) {
    if (actor == null) {
      return "redirect:/login";
    }
    ProblemaPreparado p = (ProblemaPreparado) session.getAttribute(clave(cid, pid));
    if (p == null) {
      return "redirect:/competencias/" + cid + "/problemas/" + pid + "/resolver";
    }
    if (result.hasErrors()) {
      model.addAttribute("preparado", p);
      model.addAttribute("cid", cid);
      model.addAttribute("restante", 0L);
      return "competencias/resolver";
    }
    try {
      boolean ok = competenciaService.enviar(cid, p, respuesta.getRespuesta(), actor);
      session.removeAttribute(clave(cid, pid));
      if (ok) {
        flash.addFlashAttribute("ok", "Correcto. Problema resuelto");
        return "redirect:/competencias/" + cid;
      }
      flash.addFlashAttribute("resultado", "mal");
      return "redirect:/competencias/" + cid + "/problemas/" + pid + "/resolver";
    } catch (CompetenciaException e) {
      flash.addFlashAttribute("error", e.getMessage());
      return "redirect:/competencias/" + cid;
    }
  }

  @GetMapping("/autor/competencias/nueva")
  public String nueva(@AuthenticationPrincipal UserDetailsImpl actor, Model model) {
    if (actor == null) {
      return "redirect:/login";
    }
    if (!puedeCrear(actor)) {
      return "redirect:/acceso-denegado";
    }
    model.addAttribute("competencia", new CompetenciaRequest());
    model.addAttribute("problemas", competenciaService.problemasDinamicos());
    return "autor/competencia-form";
  }

  @PostMapping("/autor/competencias")
  public String crear(@Valid @ModelAttribute("competencia") CompetenciaRequest request,
      BindingResult result, @AuthenticationPrincipal UserDetailsImpl actor, Model model,
      RedirectAttributes flash) {
    if (actor == null) {
      return "redirect:/login";
    }
    if (!puedeCrear(actor)) {
      return "redirect:/acceso-denegado";
    }
    if (!result.hasErrors()) {
      try {
        Integer id = competenciaService.crear(request, actor);
        flash.addFlashAttribute("ok", "Competencia creada");
        return "redirect:/competencias/" + id;
      } catch (CompetenciaException e) {
        model.addAttribute("errorGeneral", e.getMessage());
      }
    }
    model.addAttribute("problemas", competenciaService.problemasDinamicos());
    return "autor/competencia-form";
  }
}
