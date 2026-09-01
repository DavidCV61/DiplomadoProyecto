package dgtic.core.controller;

import dgtic.core.model.dto.FormularioIntentoDTO;
import dgtic.core.model.dto.ProblemaDTO;
import dgtic.core.service.ProblemaService;
import dgtic.core.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ProblemaController {

  private final ProblemaService problemaService;
  private final UsuarioService usuarioService;

  // Listar problemas

  @GetMapping("/problemas")
  public String problemas(Model model) {
    model.addAttribute("problemas", problemaService.listar());

    return "problemas";
  }

  // Resolucion
  // Recordatoprio a mi mismo: Quitar la opcion listar luego

  @GetMapping("/problemas/resolver/{id}")
  public String resolver(@PathVariable Integer id, Model model) {
    var problema = problemaService.buscarPorId(id);

    FormularioIntentoDTO intentoDTO = new FormularioIntentoDTO();
    intentoDTO.setIdProblema(id);

    model.addAttribute("problema", problema);
    model.addAttribute("intentoDTO", intentoDTO);
    model.addAttribute("usuarios", usuarioService.listar());
    return "problema-resolver";
  }

  // Crear problema

  @GetMapping("/problemas/nuevo")
  public String nuevo(Model model) {
    model.addAttribute("problemaDTO", new ProblemaDTO());
    return "problema-form";
  }

  @PostMapping("/problemas/guardar")
  public String guardar(@Valid @ModelAttribute("problemaDTO") ProblemaDTO problemaDTO,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      return "problema-form";
    }
    problemaService.guardar(problemaDTO);
    redirectAttributes.addFlashAttribute("guardado", true);
    return "redirect:/problemas";
  }

  // Editar problemas
  @GetMapping("/problemas/editar/{id}")
  public String editar(@PathVariable Integer id, Model model) {
    var problema = problemaService.buscarPorId(id);
    ProblemaDTO dto = new ProblemaDTO();
    dto.setTitulo(problema.getTitulo());
    dto.setEnunciado(problema.getEnunciado());
    dto.setTema(problema.getTema());
    dto.setNivel(problema.getNivel());
    dto.setResultadoEsperado(problema.getResultadoEsperado());
    dto.setMargenTolerancia(problema.getMargenTolerancia());
    model.addAttribute("problemaDTO", dto);
    model.addAttribute("id", id);
    return "problema-editar";
  }

  @PostMapping("/problemas/actualizar/{id}")
  public String actualizar(@PathVariable Integer id,
      @Valid @ModelAttribute("problemaDTO") ProblemaDTO problemaDTO,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("id", id);
      return "problema-editar";
    }
    problemaService.actualizar(id, problemaDTO);
    redirectAttributes.addFlashAttribute("actualizado", true);
    return "redirect:/problemas";
  }

  // eliminar

  @GetMapping("/problemas/eliminar/{id}")
  public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
    problemaService.eliminar(id);
    redirectAttributes.addFlashAttribute("eliminado", true);
    return "redirect:/problemas";
  }
}
