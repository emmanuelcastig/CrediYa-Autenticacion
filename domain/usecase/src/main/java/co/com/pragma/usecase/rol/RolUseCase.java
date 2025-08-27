package co.com.pragma.usecase.rol;

import co.com.pragma.model.rol.Rol;
import co.com.pragma.model.rol.gateways.RolRepository;
import co.com.pragma.usecase.in.BuscarRolPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RolUseCase implements BuscarRolPort {
    private final RolRepository rolRepository;
    @Override
    public Mono<Rol> buscarPorId(Long id) {
        return rolRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No se encontró el rol con id: " + id)));
    }
}
