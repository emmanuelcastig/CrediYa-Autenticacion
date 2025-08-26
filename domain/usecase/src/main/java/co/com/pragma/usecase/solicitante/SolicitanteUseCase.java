package co.com.pragma.usecase.solicitante;

import co.com.pragma.model.solicitante.Solicitante;
import co.com.pragma.model.solicitante.gateways.SolicitanteRepository;
import co.com.pragma.usecase.in.CrearSolicitantePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitanteUseCase implements CrearSolicitantePort {
    private final SolicitanteRepository solicitanteRepository;

    @Override
    public Mono<Solicitante> crearSolicitante(Solicitante solicitante) {
        return solicitanteRepository.findByCorreoElectronico(solicitante.getCorreoElectronico())
                .flatMap(existing -> Mono.<Solicitante>error(
                        new IllegalArgumentException("El correo electrónico ya está en uso")))
                .switchIfEmpty(Mono.defer(() -> solicitanteRepository.guardarSolicitante(solicitante)));

    }

    @Override
    public Mono<Boolean> solicitanteExiste(String numeroDocumento) {
        return solicitanteRepository.existByDocumentoIdentidad(numeroDocumento);
    }

}
