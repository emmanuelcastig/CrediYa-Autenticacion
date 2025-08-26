package co.com.pragma.usecase.in;

import co.com.pragma.model.solicitante.Solicitante;
import reactor.core.publisher.Mono;

public interface LoginSolicitantePort {
    Mono<Solicitante> login(String email, String clave);
}
