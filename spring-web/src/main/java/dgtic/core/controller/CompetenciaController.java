package dgtic.core.controller;

import dgtic.core.model.dto.CompetenciaDTO;
import dgtic.core.model.dto.FormularioIntentoDTO;
import dgtic.core.model.dto.ProblemaDTO;
import dgtic.core.model.entity.Intento;
import dgtic.core.service.CompetenciaService;
import dgtic.core.service.IntentoService;
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
public class CompetenciaController {

  private final CompetenciaService competenciaService;
  private final UsuarioService usuarioService;
  private final IntentoService intentoService;
  private final ProblemaService problemaService;

  @GetMapping("/competencias")
  public String competencias(Model model) {
    model.addAttribute("competencias", competenciaService.listar());
    return "competencias";
  }

  // Crear competencia
  @GetMapping("/competencias/nueva")
  public String nueva(Model model) {
    model.addAttribute("competenciaDTO", new CompetenciaDTO());
    model.addAttribute("usuarios", usuarioService.listar());
    return "competencia-form";
  }

  @PostMapping("/competencias/guardar")
  public String guardar(@Valid @ModelAttribute("competenciaDTO") CompetenciaDTO competenciaDTO,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("usuarios", usuarioService.listar());
      return "competencia-form";
    }
    var competencia = competenciaService.crear(competenciaDTO);
    redirectAttributes.addFlashAttribute("creada", true);
    return "redirect:/competencias/" + competencia.getId();
  }

  // actualizar competencia

  @GetMapping("/competencias/{id}")
  public String detalle(@PathVariable Integer id, Model model) {
    var competencia = competenciaService.buscarPorId(id);
    CompetenciaDTO dto = new CompetenciaDTO();
    dto.setNombre(competencia.getNombre());
    dto.setDescripcion(competencia.getDescripcion());
    dto.setFechaInicio(competencia.getFechaInicio());
    dto.setDuracionMinutos(competencia.getDuracionMinutos());
    dto.setEstado(competencia.getEstado());
    dto.setCreadorId(competencia.getCreadorId());

    model.addAttribute("competencia", competencia);
    model.addAttribute("competenciaDTO", dto);
    model.addAttribute("usuarios", usuarioService.listar());
    model.addAttribute("competenciaProblemas", competenciaService.problemasDeCompetencia(id));
    model.addAttribute("todosLosProblemas", problemaService.listar());
    return "competencia-detalle";
  }

  @PostMapping("/competencias/{id}/actualizar")
  public String actualizar(@PathVariable Integer id,
      @Valid @ModelAttribute("competenciaDTO") CompetenciaDTO competenciaDTO,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("competencia", competenciaService.buscarPorId(id));
      model.addAttribute("usuarios", usuarioService.listar());
      model.addAttribute("competenciaProblemas", competenciaService.problemasDeCompetencia(id));
      model.addAttribute("todosLosProblemas", problemaService.listar());
      return "competencia-detalle";
    }
    competenciaService.actualizar(id, competenciaDTO);
    redirectAttributes.addFlashAttribute("actualizada", true);
    return "redirect:/competencias/" + id;
  }

  // agregar problemaS

  @PostMapping("/competencias/{id}/agregar-problema")
  public String agregarProblemaExistente(@PathVariable Integer id,
      @RequestParam Integer idProblema,
      RedirectAttributes redirectAttributes) {
    competenciaService.agregarProblemaExistente(id, idProblema);
    redirectAttributes.addFlashAttribute("problemaAgregado", true);
    return "redirect:/competencias/" + id;
  }

  @GetMapping("/competencias/{id}/quitar-problema/{idProblema}")
  public String quitarProblema(@PathVariable Integer id,
      @PathVariable Integer idProblema,
      RedirectAttributes redirectAttributes) {
    competenciaService.quitarProblema(id, idProblema);
    redirectAttributes.addFlashAttribute("problemaQuitado", true);
    return "redirect:/competencias/" + id;
  }

  @GetMapping("/competencias/{id}/problemas/nuevo")
  public String nuevoProblemaEnCompetencia(@PathVariable Integer id, Model model) {
    model.addAttribute("competencia", competenciaService.buscarPorId(id));
    model.addAttribute("problemaDTO", new ProblemaDTO());
    return "competencia-problema-nuevo";
  }

  @PostMapping("/competencias/{id}/problemas/crear")
  public String crearProblemaEnCompetencia(@PathVariable Integer id,
      @Valid @ModelAttribute("problemaDTO") ProblemaDTO problemaDTO,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("competencia", competenciaService.buscarPorId(id));
      return "competencia-problema-nuevo";
    }
    competenciaService.crearProblemaYAsignar(id, problemaDTO);
    redirectAttributes.addFlashAttribute("problemaAgregado", true);
    return "redirect:/competencias/" + id;
  }

  // Test resolver problema
  @GetMapping("/competencias/{idCompetencia}/problemas/{idProblema}/resolver")
  public String resolverProblema(@PathVariable Integer idCompetencia,
      @PathVariable Integer idProblema,
      Model model) {

    var problema = competenciaService.problemasDeCompetencia(idCompetencia).stream()
        .filter(cp -> cp.getProblemaId().equals(idProblema))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Este problema no pertenece a la competencia"))
        .getProblema();

    FormularioIntentoDTO intentoDTO = new FormularioIntentoDTO();
    intentoDTO.setIdProblema(idProblema);

    model.addAttribute("competencia", competenciaService.buscarPorId(idCompetencia));
    model.addAttribute("problema", problema);
    model.addAttribute("intentoDTO", intentoDTO);
    model.addAttribute("usuarios", usuarioService.listar());
    return "competencia-problema-resolver";
  }

  @PostMapping("/competencias/{idCompetencia}/problemas/{idProblema}/enviar")
  public String enviarRespuesta(@PathVariable Integer idCompetencia,
      @PathVariable Integer idProblema,
      @Valid @ModelAttribute("intentoDTO") FormularioIntentoDTO formulario,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      var problema = competenciaService.problemasDeCompetencia(idCompetencia).stream()
          .filter(cp -> cp.getProblemaId().equals(idProblema))
          .findFirst().orElseThrow().getProblema();
      model.addAttribute("competencia", competenciaService.buscarPorId(idCompetencia));
      model.addAttribute("problema", problema);
      model.addAttribute("usuarios", usuarioService.listar());
      return "competencia-problema-resolver";
    }

    Intento resultado = intentoService.registrarIntento(formulario, idCompetencia, "COMPETENCIA");

    if ("CORRECTO".equals(resultado.getResultado())) {
      redirectAttributes.addFlashAttribute("success", "¡Respuesta correcta!");
    } else {
      redirectAttributes.addFlashAttribute("error", "Respuesta incorrecta.");
    }

    return "redirect:/competencias/" + idCompetencia + "/problemas/" + idProblema + "/resolver";
  }
}
