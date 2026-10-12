package com.srpf.service;

import com.srpf.entity.Usuario;
import com.srpf.enums.ResultadoIntento;
import com.srpf.repository.EstadisticaRepository;
import com.srpf.repository.PerfilRepository;
import com.srpf.dto.PerfilPublicoDTO;
import com.srpf.exception.UsuarioNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerfilServiceImpl implements PerfilService {

  private final PerfilRepository perfilRepository;
  private final EstadisticaRepository estadisticaRepository;

  @Autowired
  public PerfilServiceImpl(PerfilRepository perfilRepository,
      EstadisticaRepository estadisticaRepository) {
    this.perfilRepository = perfilRepository;
    this.estadisticaRepository = estadisticaRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<PerfilPublicoDTO> buscar(String q) {
    Map<Integer, long[]> conteo = contar();
    return perfilRepository.buscar(normalizar(q)).stream().map(u -> aDto(u, conteo)).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public PerfilPublicoDTO perfil(String username) throws UsuarioNotFoundException {
    String n = normalizar(username).toLowerCase(Locale.ROOT);
    Usuario u = perfilRepository.porUsername(n).orElseThrow(() -> new UsuarioNotFoundException(n));
    return aDto(u, contar());
  }

  @Override
  @Transactional(readOnly = true)
  public List<PerfilPublicoDTO> estadisticas() {
    return buscar("").stream()
        .sorted(Comparator.comparingLong(PerfilPublicoDTO::resueltos).reversed()
            .thenComparing(PerfilPublicoDTO::username))
        .toList();
  }

  private Map<Integer, long[]> contar() {
    Map<Integer, long[]> conteo = new HashMap<>();
    for (Object[] f : estadisticaRepository.porUsuario(ResultadoIntento.CORRECTO)) {
      conteo.put(((Number) f[0]).intValue(),
          new long[] {((Number) f[1]).longValue(), ((Number) f[2]).longValue()});
    }
    return conteo;
  }

  private PerfilPublicoDTO aDto(Usuario u, Map<Integer, long[]> conteo) {
    long[] c = conteo.getOrDefault(u.getId(), new long[2]);
    return new PerfilPublicoDTO(u.getId(), u.getUsername(), u.getNombre(), u.getRol().name(),
        u.getPuntuacionElo(), u.getFechaRegistro(), c[0], c[1]);
  }

  private String normalizar(String s) {
    String t = s == null ? "" : s.trim();
    return t.length() > 50 ? t.substring(0, 50) : t;
  }
}
