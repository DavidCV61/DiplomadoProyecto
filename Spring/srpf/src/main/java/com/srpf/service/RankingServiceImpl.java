package com.srpf.service;

import com.srpf.repository.IntentoRepository;
import com.srpf.dto.IntentoGlobalView;
import com.srpf.dto.RankingView;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RankingServiceImpl implements RankingService {

  private final IntentoRepository intentoRepository;

  @Autowired
  public RankingServiceImpl(IntentoRepository intentoRepository) {
    this.intentoRepository = intentoRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<RankingView> ranking() {
    List<RankingView> salida = new ArrayList<>();
    int indice = 0;
    int posicion = 0;
    Integer anterior = null;
    for (Object[] f : intentoRepository.ranking()) {
      indice++;
      int elo = ((Number) f[2]).intValue();
      if (anterior == null || elo != anterior) {
        posicion = indice;
      }
      anterior = elo;
      salida.add(new RankingView(posicion, (String) f[0], (String) f[1], elo));
    }
    return salida;
  }

  @Override
  @Transactional(readOnly = true)
  public List<IntentoGlobalView> intentosGlobales() {
    return intentoRepository.recientes(PageRequest.of(0, 50)).stream()
        .map(i -> new IntentoGlobalView(i.getId(), i.getUsuario().getUsername(),
            i.getProblema().getId(), i.getProblema().getTitulo(), i.getResultado(),
            i.getTipo(), i.getFechaHora()))
        .toList();
  }
}
