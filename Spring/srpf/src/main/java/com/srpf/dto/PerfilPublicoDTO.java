package com.srpf.dto;

import java.time.LocalDateTime;

public record PerfilPublicoDTO(Integer id, String username, String nombre, String rol, int elo,
    LocalDateTime fechaRegistro, long intentos, long resueltos) {
}
