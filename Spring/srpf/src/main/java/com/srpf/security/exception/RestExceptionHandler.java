package com.srpf.security.exception;

import com.srpf.controller.ApiController;
import com.srpf.controller.RespuestaApiController;
import com.srpf.exception.ProblemaInvalidoException;
import com.srpf.exception.ProblemaNotFoundException;
import com.srpf.security.controller.AuthController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {AuthController.class, ApiController.class,
    RespuestaApiController.class})
public class RestExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ExceptionResponse> validacion(MethodArgumentNotValidException e) {
    String mensaje = e.getBindingResult().getFieldErrors().stream()
        .map(f -> f.getField() + ": " + f.getDefaultMessage())
        .reduce((a, b) -> a + "; " + b).orElse("Datos invalidos");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(400, mensaje));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ExceptionResponse> cuerpoInvalido(HttpMessageNotReadableException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ExceptionResponse(400, "El cuerpo de la peticion no es un JSON valido"));
  }

  @ExceptionHandler(ProblemaNotFoundException.class)
  public ResponseEntity<ExceptionResponse> problemaNoExiste(ProblemaNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ExceptionResponse(404, "El problema no existe"));
  }

  @ExceptionHandler(ProblemaInvalidoException.class)
  public ResponseEntity<ExceptionResponse> problemaInvalido(ProblemaInvalidoException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ExceptionResponse(400, "El problema tiene una configuracion invalida"));
  }
}
