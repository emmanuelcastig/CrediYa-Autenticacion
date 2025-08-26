package co.com.pragma.usecase.autenticacion;

import co.com.pragma.model.solicitante.Solicitante;
import co.com.pragma.model.solicitante.gateways.SolicitanteRepository;
import co.com.pragma.usecase.in.LoginSolicitantePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AutenticacionUseCase implements LoginSolicitantePort {

    private final SolicitanteRepository solicitanteRepository;

    @Override
    public Mono<Solicitante> login(String email, String clave) {
        return solicitanteRepository.findByCorreoElectronico(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Credenciales inválidas")))
                .filter(solicitante -> solicitante.getClave().equals(clave))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Credenciales inválidas")));
    }
}
