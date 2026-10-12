package com.srpf.dto;

import com.srpf.enums.Nivel;
import com.srpf.enums.Tema;

public record ProblemaApiDTO(Integer id, String titulo, Tema tema, Nivel nivel, String autor) {
}
