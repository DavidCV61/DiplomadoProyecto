package com.srpf.controller;

import com.srpf.enums.Nivel;
import com.srpf.enums.Tema;
import com.srpf.dto.PerfilPublicoDTO;
import com.srpf.dto.ProblemaApiDTO;
import com.srpf.service.PerfilService;
import com.srpf.service.ProblemaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

  private final ProblemaService problemaService;
  private final PerfilService perfilService;

  @Autowired
  public ApiController(ProblemaService problemaService, PerfilService perfilService) {
    this.problemaService = problemaService;
    this.perfilService = perfilService;
  }

  @GetMapping("/problemas")
  public List<ProblemaApiDTO> problemas(@RequestParam(required = false) Tema tema,
      @RequestParam(required = false) Nivel nivel) {
    return problemaService.filtrar(tema, nivel).stream()
        .map(p -> new ProblemaApiDTO(p.getId(), p.getTitulo(), p.getTema(), p.getNivel(),
            p.getAutor().getNombre()))
        .toList();
  }

  @GetMapping("/estadisticas/usuarios")
  public List<PerfilPublicoDTO> estadisticas() {
    return perfilService.estadisticas();
  }
}
