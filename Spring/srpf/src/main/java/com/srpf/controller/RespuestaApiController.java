package com.srpf.controller;

import com.srpf.dto.ProblemaPreparado;
import com.srpf.dto.RespuestaApiRequest;
import com.srpf.dto.RespuestaApiResponse;
import com.srpf.entity.Problema;
import com.srpf.exception.ProblemaInvalidoException;
import com.srpf.exception.ProblemaNotFoundException;
import com.srpf.repository.ProblemaRepository;
import com.srpf.security.exception.ExceptionResponse;
import com.srpf.security.model.UserDetailsImpl;
import com.srpf.service.ResolucionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/problemas")
public class RespuestaApiController {

  private final ResolucionService resolucionService;
  private final ProblemaRepository problemaRepository;

  @Autowired
  public RespuestaApiController(ResolucionService resolucionService,
      ProblemaRepository problemaRepository) {
    this.resolucionService = resolucionService;
    this.problemaRepository = problemaRepository;
  }

  @PostMapping("/{id}/respuesta")
  public ResponseEntity<?> responder(@PathVariable Integer id,
      @Valid @RequestBody RespuestaApiRequest request,
      @AuthenticationPrincipal UserDetailsImpl actor)
      throws ProblemaNotFoundException, ProblemaInvalidoException {
    Problema problema = problemaRepository.findById(id)
        .orElseThrow(() -> new ProblemaNotFoundException(id));
    Object parametros = problema.getParametrosDinamicos();
    if (parametros != null && !parametros.toString().isBlank()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(400,
          "Los problemas dinamicos solo se resuelven desde la web"));
    }
    ProblemaPreparado preparado = resolucionService.preparar(id);
    boolean ok = resolucionService.enviar(preparado, request.respuesta(), actor);
    return ResponseEntity.ok(new RespuestaApiResponse(id, ok, ok ? "CORRECTO" : "INCORRECTO"));
  }
}
