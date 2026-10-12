package com.srpf.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthApiController {

  @GetMapping("/whoami")
  public String whoAmI(Authentication authentication) {
    return "Hola " + authentication.getName()
        + ", tus roles son: " + authentication.getAuthorities();
  }
}
