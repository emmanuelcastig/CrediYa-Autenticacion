package co.com.pragma.r2dbc;

import co.com.pragma.model.solicitante.Solicitante;
import co.com.pragma.model.solicitante.gateways.SolicitanteRepository;
import co.com.pragma.r2dbc.entity.SolicitanteEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitante,
        SolicitanteEntity,
        Long,
        MyReactiveRepository
        > implements SolicitanteRepository {
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Solicitante.class));
    }

    @Override
    public Mono<Solicitante> guardarSolicitante(Solicitante solicitante) {
        return repository.save(toData(solicitante))
                .map(this::toEntity);
    }

    @Override
    public Mono<Solicitante> findByCorreoElectronico(String correoElectronico) {
        return repository.findByCorreoElectronico(correoElectronico)
                .map(this::toEntity);
    }

    @Override
    public Mono<Boolean> existByDocumentoIdentidad(String documentoIdentidad) {
        return repository.existsByDocumentoIdentidad(documentoIdentidad);
    }
}

