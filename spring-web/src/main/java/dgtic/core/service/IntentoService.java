package dgtic.core.service;

import dgtic.core.model.dto.FormularioIntentoDTO;
import dgtic.core.model.entity.Intento;
import dgtic.core.model.entity.Problema;
import dgtic.core.model.entity.Usuario;
import dgtic.core.repository.IntentoRepository;
import dgtic.core.repository.ProblemaRepository;
import dgtic.core.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IntentoService {

    private final IntentoRepository intentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProblemaRepository problemaRepository;

    public List<Intento> listar() {
        return intentoRepository.listarConDetalle();
    }

    @Transactional
    public Intento registrarIntento(FormularioIntentoDTO formulario) {
        return registrarIntento(formulario, null, "GIMNASIO");
    }

    @Transactional
    public Intento registrarIntento(FormularioIntentoDTO formulario, Integer idCompetencia, String tipo) {

        Usuario usuario = usuarioRepository.findById(formulario.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("No existe el usuario seleccionado"));

        Problema problema = problemaRepository.findById(formulario.getIdProblema())
                .orElseThrow(() -> new RuntimeException("No existe el problema seleccionado"));

        BigDecimal diferencia = formulario.getRespuestaEnviada()
                .subtract(problema.getResultadoEsperado())
                .abs();

        boolean esCorrecto = diferencia.compareTo(problema.getMargenTolerancia()) <= 0;
        String resultado = esCorrecto ? "CORRECTO" : "INCORRECTO";

        Intento intento = new Intento();
        intento.setUsuario(usuario);
        intento.setProblema(problema);
        intento.setRespuestaEnviada(formulario.getRespuestaEnviada());
        intento.setResultado(resultado);
        intento.setFechaHora(LocalDateTime.now());
        intento.setTipo(tipo);
        intento.setCompetenciaId(idCompetencia);

        return intentoRepository.save(intento);
    }

    public Set<Integer> problemasResueltosEnCompetencia(Integer idCompetencia, Integer idUsuario) {
        return intentoRepository.resueltosPorUsuarioEnCompetencia(idCompetencia, idUsuario).stream()
                .map(i -> i.getProblema().getId())
                .collect(Collectors.toSet());
    }
}
