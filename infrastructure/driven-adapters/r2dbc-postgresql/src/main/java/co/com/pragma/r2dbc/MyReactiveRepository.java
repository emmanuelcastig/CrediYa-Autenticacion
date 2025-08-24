package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.SolicitanteEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MyReactiveRepository extends ReactiveCrudRepository<SolicitanteEntity, Long>
        , ReactiveQueryByExampleExecutor<SolicitanteEntity> {
    Mono<SolicitanteEntity> findByCorreoElectronico(String correoElectronico);
}
