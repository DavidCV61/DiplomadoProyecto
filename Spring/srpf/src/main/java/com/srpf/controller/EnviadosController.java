package com.srpf.controller;

import com.srpf.security.model.UserDetailsImpl;
import com.srpf.service.RankingService;
import com.srpf.service.ResolucionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnviadosController {

  private final ResolucionService resolucionService;
  private final RankingService rankingService;

  @Autowired
  public EnviadosController(ResolucionService resolucionService,
      RankingService rankingService) {
    this.resolucionService = resolucionService;
    this.rankingService = rankingService;
  }

  @GetMapping("/enviados")
  public String enviados(@AuthenticationPrincipal UserDetailsImpl actor, Model model) {
    if (actor == null) {
      return "redirect:/login";
    }
    model.addAttribute("intentos", resolucionService.misIntentos(actor.getId()));
    model.addAttribute("globales", rankingService.intentosGlobales());
    return "enviados";
  }
}
