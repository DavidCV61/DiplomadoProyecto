package com.srpf.controller;

import com.srpf.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RankingController {

  private final RankingService rankingService;

  @Autowired
  public RankingController(RankingService rankingService) {
    this.rankingService = rankingService;
  }

  @GetMapping("/ranking")
  public String ranking(Model model) {
    model.addAttribute("ranking", rankingService.ranking());
    return "ranking";
  }
}
