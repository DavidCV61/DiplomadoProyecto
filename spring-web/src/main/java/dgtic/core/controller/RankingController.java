package dgtic.core.controller;

import dgtic.core.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RankingController {

    private final UsuarioService usuarioService;

    @GetMapping("/ranking")
    public String ranking(Model model) {
        model.addAttribute("usuarios", usuarioService.ranking());
        return "ranking";
    }
}
