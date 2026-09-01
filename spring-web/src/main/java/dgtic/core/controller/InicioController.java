package dgtic.core.controller;

import dgtic.core.model.dto.UsuarioDTO;
import dgtic.core.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class InicioController {

  private final UsuarioService usuarioService;

  @GetMapping("/")
  public String inicio(Model model) {
    if (!model.containsAttribute("usuarioDTO")) {
      model.addAttribute("usuarioDTO", new UsuarioDTO());
    }
    return "inicio";
  }

  @PostMapping("/registrar")
  public String registrar(@Valid @ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("usuarioDTO", usuarioDTO);
      return "inicio";
    }

    usuarioService.registrar(usuarioDTO);
    redirectAttributes.addFlashAttribute("registrado", true);
    return "redirect:/";
  }

}
