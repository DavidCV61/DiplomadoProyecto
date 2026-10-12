package com.srpf.controller;

import com.srpf.enums.Nivel;
import com.srpf.enums.Tema;
import com.srpf.dto.ProblemaRequest;
import com.srpf.dto.ProblemaResponse;
import com.srpf.exception.ProblemaInvalidoException;
import com.srpf.exception.ProblemaNotFoundException;
import com.srpf.security.model.UserDetailsImpl;
import com.srpf.service.ProblemaService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProblemaController {

  private final ProblemaService problemaService;

  @Autowired
  public ProblemaController(ProblemaService problemaService) {
    this.problemaService = problemaService;
  }

  @GetMapping("/problemas")
  public String listar(@RequestParam(required = false) Tema tema,
      @RequestParam(required = false) Nivel nivel, Model model) {
    model.addAttribute("problemas", problemaService.filtrar(tema, nivel));
    model.addAttribute("temas", Tema.values());
    model.addAttribute("niveles", Nivel.values());
    model.addAttribute("temaSel", tema);
    model.addAttribute("nivelSel", nivel);
    return "problemas";
  }

  @GetMapping("/problemas/{id}")
  public String detalle(@PathVariable Integer id,
      @AuthenticationPrincipal UserDetailsImpl actor, Model model)
      throws ProblemaNotFoundException {
    ProblemaResponse p = problemaService.obtener(id);
    model.addAttribute("problema", p);
    model.addAttribute("puedeEditar", problemaService.puedeEditar(p.autorId(), actor));
    return "problema/detalle";
  }

  @GetMapping("/autor/problemas/nuevo")
  public String nuevo(Model model) {
    model.addAttribute("problema", new ProblemaRequest());
    cargarFormulario(model, "Nuevo problema", "/autor/problemas");
    return "autor/problema-form";
  }

  @PostMapping("/autor/problemas")
  public String crear(@Valid @ModelAttribute("problema") ProblemaRequest problema,
      BindingResult result, @AuthenticationPrincipal UserDetailsImpl actor,
      Model model, RedirectAttributes flash) {
    if (!result.hasErrors()) {
      try {
        problemaService.crear(problema, actor);
        flash.addFlashAttribute("guardado", true);
        return "redirect:/problemas";
      } catch (ProblemaInvalidoException e) {
        result.rejectValue(e.getCampo(), "invalido", e.getMessage());
      }
    }
    cargarFormulario(model, "Nuevo problema", "/autor/problemas");
    return "autor/problema-form";
  }

  @GetMapping("/autor/problemas/{id}/editar")
  public String editar(@PathVariable Integer id,
      @AuthenticationPrincipal UserDetailsImpl actor, Model model)
      throws ProblemaNotFoundException {
    model.addAttribute("problema", problemaService.obtenerParaEdicion(id, actor));
    cargarFormulario(model, "Editar problema", "/autor/problemas/" + id + "/actualizar");
    return "autor/problema-form";
  }

  @PostMapping("/autor/problemas/{id}/actualizar")
  public String actualizar(@PathVariable Integer id,
      @Valid @ModelAttribute("problema") ProblemaRequest problema, BindingResult result,
      @AuthenticationPrincipal UserDetailsImpl actor, Model model, RedirectAttributes flash)
      throws ProblemaNotFoundException {
    if (!result.hasErrors()) {
      try {
        problemaService.actualizar(id, problema, actor);
        flash.addFlashAttribute("actualizado", true);
        return "redirect:/problemas";
      } catch (ProblemaInvalidoException e) {
        result.rejectValue(e.getCampo(), "invalido", e.getMessage());
      }
    }
    cargarFormulario(model, "Editar problema", "/autor/problemas/" + id + "/actualizar");
    return "autor/problema-form";
  }

  private void cargarFormulario(Model model, String titulo, String accion) {
    model.addAttribute("tituloPagina", titulo);
    model.addAttribute("accion", accion);
    model.addAttribute("temas", Tema.values());
    model.addAttribute("niveles", Nivel.values());
  }
}
