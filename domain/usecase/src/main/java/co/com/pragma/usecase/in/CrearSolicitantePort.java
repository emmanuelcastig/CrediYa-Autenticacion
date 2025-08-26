package co.com.pragma.usecase.in;

import co.com.pragma.model.solicitante.Solicitante;
import reactor.core.publisher.Mono;

public interface CrearSolicitantePort {
    Mono<Solicitante> crearSolicitante(Solicitante solicitante);
    Mono<Boolean> solicitanteExiste(String numeroDocumento);
}
