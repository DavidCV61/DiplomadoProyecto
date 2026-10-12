package com.srpf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ExpresionMatematica {

  private final String src;
  private final Map<String, Double> vars;
  private int pos;

  private ExpresionMatematica(String src, Map<String, Double> vars) {
    this.src = src;
    this.vars = vars;
  }

  public static double evaluar(String formula, Map<String, Double> vars) {
    ExpresionMatematica p = new ExpresionMatematica(formula, vars);
    double r = p.expr();
    p.saltar();
    if (p.pos < p.src.length()) {
      throw new IllegalArgumentException("caracter inesperado '" + p.src.charAt(p.pos) + "'");
    }
    return r;
  }

  private void saltar() {
    while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) {
      pos++;
    }
  }

  private boolean consumir(char c) {
    saltar();
    if (pos < src.length() && src.charAt(pos) == c) {
      pos++;
      return true;
    }
    return false;
  }

  private double expr() {
    double v = termino();
    while (true) {
      if (consumir('+')) {
        v += termino();
      } else if (consumir('-')) {
        v -= termino();
      } else {
        return v;
      }
    }
  }

  private double termino() {
    double v = unario();
    while (true) {
      if (consumir('*')) {
        v *= unario();
      } else if (consumir('/')) {
        v /= unario();
      } else {
        return v;
      }
    }
  }

  private double unario() {
    if (consumir('-')) {
      return -unario();
    }
    if (consumir('+')) {
      return unario();
    }
    return potencia();
  }

  private double potencia() {
    double base = atomo();
    if (consumir('^')) {
      return Math.pow(base, unario());
    }
    return base;
  }

  private double atomo() {
    saltar();
    if (consumir('(')) {
      double v = expr();
      if (!consumir(')')) {
        throw new IllegalArgumentException("falta cerrar parentesis");
      }
      return v;
    }
    if (pos >= src.length()) {
      throw new IllegalArgumentException("expresion incompleta");
    }
    char c = src.charAt(pos);
    if (Character.isDigit(c) || c == '.') {
      int ini = pos;
      while (pos < src.length()
          && (Character.isDigit(src.charAt(pos)) || src.charAt(pos) == '.')) {
        pos++;
      }
      return Double.parseDouble(src.substring(ini, pos));
    }
    if (Character.isLetter(c) || c == '_') {
      int ini = pos;
      while (pos < src.length()
          && (Character.isLetterOrDigit(src.charAt(pos)) || src.charAt(pos) == '_')) {
        pos++;
      }
      String nombre = src.substring(ini, pos);
      if (consumir('(')) {
        List<Double> args = new ArrayList<>();
        if (!consumir(')')) {
          do {
            args.add(expr());
          } while (consumir(','));
          if (!consumir(')')) {
            throw new IllegalArgumentException("falta cerrar parentesis en " + nombre);
          }
        }
        return funcion(nombre, args);
      }
      return variable(nombre);
    }
    throw new IllegalArgumentException("caracter inesperado '" + c + "'");
  }

  private double variable(String nombre) {
    Double v = vars.get(nombre);
    if (v != null) {
      return v;
    }
    if (nombre.equals("pi")) {
      return Math.PI;
    }
    if (nombre.equals("e")) {
      return Math.E;
    }
    throw new IllegalArgumentException("variable desconocida '" + nombre + "'");
  }

  private double funcion(String nombre, List<Double> a) {
    switch (nombre) {
      case "pow":
        exigir(nombre, a, 2);
        return Math.pow(a.get(0), a.get(1));
      case "sqrt":
        exigir(nombre, a, 1);
        return Math.sqrt(a.get(0));
      case "sin":
        exigir(nombre, a, 1);
        return Math.sin(a.get(0));
      case "cos":
        exigir(nombre, a, 1);
        return Math.cos(a.get(0));
      case "tan":
        exigir(nombre, a, 1);
        return Math.tan(a.get(0));
      case "asin":
        exigir(nombre, a, 1);
        return Math.asin(a.get(0));
      case "acos":
        exigir(nombre, a, 1);
        return Math.acos(a.get(0));
      case "atan":
        exigir(nombre, a, 1);
        return Math.atan(a.get(0));
      case "ln":
        exigir(nombre, a, 1);
        return Math.log(a.get(0));
      case "log":
        exigir(nombre, a, 1);
        return Math.log10(a.get(0));
      case "exp":
        exigir(nombre, a, 1);
        return Math.exp(a.get(0));
      case "abs":
        exigir(nombre, a, 1);
        return Math.abs(a.get(0));
      case "rad":
        exigir(nombre, a, 1);
        return Math.toRadians(a.get(0));
      default:
        throw new IllegalArgumentException("funcion desconocida '" + nombre + "'");
    }
  }

  private void exigir(String nombre, List<Double> a, int n) {
    if (a.size() != n) {
      throw new IllegalArgumentException(nombre + " requiere " + n + " argumento(s)");
    }
  }
}
