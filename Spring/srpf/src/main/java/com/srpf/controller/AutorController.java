package com.srpf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AutorController {

  @GetMapping("/autor/crear-problema")
  public String crearProblema() {
    return "autor/crear-problema";
  }

  @GetMapping("/autor/crear-concurso")
  public String crearConcurso() {
    return "autor/crear-concurso";
  }
}
