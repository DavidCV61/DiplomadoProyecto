package com.srpf.dto;

import com.srpf.enums.Nivel;
import com.srpf.enums.Tema;
import java.io.Serializable;
import java.math.BigDecimal;

public record ProblemaPreparado(Integer problemaId, String titulo, String enunciado, Tema tema,
    Nivel nivel, BigDecimal resultado, BigDecimal tolerancia, String valoresJson)
    implements Serializable {
}
