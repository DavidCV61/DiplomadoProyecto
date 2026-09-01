package dgtic.core.controller;

import dgtic.core.model.dto.ForoPublicacionDTO;
import dgtic.core.service.ForoService;
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
public class ForoController {

    private final ForoService foroService;
    private final UsuarioService usuarioService;

    @GetMapping("/foro")
    public String foro(Model model) {
        if (!model.containsAttribute("publicacionDTO")) {
            model.addAttribute("publicacionDTO", new ForoPublicacionDTO());
        }
        model.addAttribute("publicaciones", foroService.listar());
        model.addAttribute("usuarios", usuarioService.listar());
        return "foro";
    }

    @PostMapping("/foro/publicar")
    public String publicar(@Valid @ModelAttribute("publicacionDTO") ForoPublicacionDTO publicacionDTO,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("publicaciones", foroService.listar());
            model.addAttribute("usuarios", usuarioService.listar());
            return "foro";
        }

        foroService.publicar(publicacionDTO);
        redirectAttributes.addFlashAttribute("publicado", true);
        return "redirect:/foro";
    }
}
