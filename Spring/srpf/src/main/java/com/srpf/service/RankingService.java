package com.srpf.service;

import com.srpf.dto.IntentoGlobalView;
import com.srpf.dto.RankingView;
import java.util.List;

public interface RankingService {
  List<RankingView> ranking();

  List<IntentoGlobalView> intentosGlobales();
}
