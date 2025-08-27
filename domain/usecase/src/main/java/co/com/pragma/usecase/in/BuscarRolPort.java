package co.com.pragma.usecase.in;

import co.com.pragma.model.rol.Rol;
import reactor.core.publisher.Mono;

public interface BuscarRolPort {
    Mono<Rol> buscarPorId(Long id);
}
