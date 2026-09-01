package dgtic.core.controller;

import dgtic.core.model.dto.FormularioIntentoDTO;
import dgtic.core.model.entity.Intento;
import dgtic.core.service.IntentoService;
import dgtic.core.service.ProblemaService;
import dgtic.core.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class IntentoController {

  private final IntentoService intentoService;
  private final ProblemaService problemaService;
  private final UsuarioService usuarioService;

  @PostMapping("/problemas/resolver/{id}/enviar")
  public String registrar(@PathVariable Integer id,
      @Valid @ModelAttribute("intentoDTO") FormularioIntentoDTO formulario,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("problema", problemaService.buscarPorId(id));
      model.addAttribute("usuarios", usuarioService.listar());
      return "problema-resolver";
    }

    Intento resultado = intentoService.registrarIntento(formulario);

    if ("CORRECTO".equals(resultado.getResultado())) {
      redirectAttributes.addFlashAttribute("success", "¡Respuesta correcta!");
    } else {
      redirectAttributes.addFlashAttribute("error", "Respuesta incorrecta.");
    }
    return "redirect:/problemas/resolver/" + id;
  }

  @GetMapping("/intentos")
  public String listarIntentos(Model model) {
    List<Intento> intentos = intentoService.listar();
    model.addAttribute("intentos", intentos);
    return "intentos";
  }

}
