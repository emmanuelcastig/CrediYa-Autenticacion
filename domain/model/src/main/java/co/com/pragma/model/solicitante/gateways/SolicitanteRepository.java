package co.com.pragma.model.solicitante.gateways;

import co.com.pragma.model.solicitante.Solicitante;
import reactor.core.publisher.Mono;

public interface SolicitanteRepository {
    Mono<Solicitante> guardarSolicitante(Solicitante solicitante);
    Mono<Solicitante> findByCorreoElectronico(String correoElectronico);
    Mono<Boolean> existByDocumentoIdentidad(String documentoIdentidad);
}
