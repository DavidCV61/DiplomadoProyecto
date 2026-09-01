package dgtic.core.controller;

import dgtic.core.model.dto.UsuarioDTO;
import dgtic.core.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

  private final UsuarioService usuarioService;

  @GetMapping
  public String lista(Model model) {
    model.addAttribute("usuarios", usuarioService.listar());
    return "usuarios";
  }

  // Formulario y actualizar

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Integer id, Model model) {
    var usuario = usuarioService.buscarPorId(id);
    UsuarioDTO dto = new UsuarioDTO();
    dto.setNombre(usuario.getNombre());
    dto.setUsername(usuario.getUsername());
    dto.setEmail(usuario.getEmail());
    dto.setPassword("sin-cambios");
    model.addAttribute("usuarioDTO", dto);
    model.addAttribute("id", id);
    return "usuario-editar";
  }

  @PostMapping("/actualizar/{id}")
  public String actualizar(@PathVariable Integer id,
      @Valid @ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("id", id);
      return "usuario-editar";
    }
    usuarioService.actualizar(id, usuarioDTO);
    redirectAttributes.addFlashAttribute("actualizado", true);
    return "redirect:/usuarios";
  }
  // eliminar

  @GetMapping("/eliminar/{id}")
  public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
    usuarioService.eliminar(id);
    redirectAttributes.addFlashAttribute("eliminado", true);
    return "redirect:/usuarios";
  }
}
