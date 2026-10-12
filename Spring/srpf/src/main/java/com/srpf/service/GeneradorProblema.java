package com.srpf.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import org.springframework.boot.json.JsonParserFactory;

public final class GeneradorProblema {

  private static final Pattern NOMBRE = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");

  public record Generado(String enunciado, BigDecimal resultado, String valoresJson)
      implements Serializable {
  }

  private GeneradorProblema() {
  }

  public static void validar(String json, BigDecimal fijo) {
    generar("", fijo, json);
  }

  public static Generado generar(String enunciado, BigDecimal fijo, String json) {
    if (json == null || json.isBlank()) {
      return new Generado(enunciado, fijo, null);
    }
    Map<String, Object> raiz;
    try {
      raiz = JsonParserFactory.getJsonParser().parseMap(json);
    } catch (RuntimeException e) {
      throw new IllegalArgumentException("Los parametros no son un JSON valido");
    }
    Map<String, Double> valores = new LinkedHashMap<>();
    Object vars = raiz.get("variables");
    if (vars instanceof Map<?, ?> mapa) {
      for (Map.Entry<?, ?> e : mapa.entrySet()) {
        String nombre = String.valueOf(e.getKey());
        if (!NOMBRE.matcher(nombre).matches()) {
          throw new IllegalArgumentException("Nombre de variable invalido: " + nombre);
        }
        if (!(e.getValue() instanceof Map<?, ?> def)) {
          throw new IllegalArgumentException("La variable " + nombre + " debe ser un objeto");
        }
        valores.put(nombre, valorDe(nombre, def));
      }
    } else if (vars != null) {
      throw new IllegalArgumentException("'variables' debe ser un objeto");
    }
    BigDecimal resultado = fijo;
    Object formula = raiz.get("formula");
    if (formula instanceof String f && !f.isBlank()) {
      double r;
      try {
        r = ExpresionMatematica.evaluar(f, valores);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Formula invalida: " + e.getMessage());
      }
      if (!Double.isFinite(r)) {
        throw new IllegalArgumentException("La formula no produce un numero finito");
      }
      resultado = BigDecimal.valueOf(r).setScale(6, RoundingMode.HALF_UP);
    }
    String texto = enunciado;
    StringBuilder out = new StringBuilder("{");
    boolean primero = true;
    for (Map.Entry<String, Double> e : valores.entrySet()) {
      String plano = BigDecimal.valueOf(e.getValue()).stripTrailingZeros().toPlainString();
      texto = texto.replace("{" + e.getKey() + "}", plano);
      if (!primero) {
        out.append(',');
      }
      out.append('"').append(e.getKey()).append("\":").append(plano);
      primero = false;
    }
    out.append('}');
    return new Generado(texto, resultado, out.toString());
  }

  private static double valorDe(String nombre, Map<?, ?> def) {
    if (def.get("valor") instanceof Number v) {
      return v.doubleValue();
    }
    if (def.get("min") instanceof Number min && def.get("max") instanceof Number max) {
      double a = min.doubleValue();
      double b = max.doubleValue();
      if (b < a) {
        throw new IllegalArgumentException("En " + nombre + " max es menor que min");
      }
      int dec = def.get("decimales") instanceof Number d
          ? Math.max(0, Math.min(6, d.intValue())) : 2;
      double x = a + ThreadLocalRandom.current().nextDouble() * (b - a);
      return BigDecimal.valueOf(x).setScale(dec, RoundingMode.HALF_UP).doubleValue();
    }
    throw new IllegalArgumentException(
        "La variable " + nombre + " necesita 'valor', o 'min' y 'max'");
  }
}
