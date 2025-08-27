package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.RolEntity;
import co.com.pragma.r2dbc.entity.SolicitanteEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RolReactiveRepository extends ReactiveCrudRepository<RolEntity, Long>
        , ReactiveQueryByExampleExecutor<RolEntity> {
}
