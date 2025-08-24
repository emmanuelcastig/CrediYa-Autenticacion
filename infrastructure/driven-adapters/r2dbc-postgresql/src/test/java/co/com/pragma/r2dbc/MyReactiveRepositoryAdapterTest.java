package co.com.pragma.r2dbc;

import co.com.pragma.model.solicitante.Solicitante;
import co.com.pragma.r2dbc.entity.SolicitanteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {

    @InjectMocks
    MyReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    MyReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private Solicitante domain;
    private SolicitanteEntity entity;

    @BeforeEach
    void setUp() {
        domain = new Solicitante();
        domain.setId(1L);
        domain.setCorreoElectronico("test@mail.com");

        entity = new SolicitanteEntity();
        entity.setId(1L);
        entity.setCorreoElectronico("test@mail.com");
    }


    @Test
    void guardarSolicitanteDebeGuardarYRetornarDomain() {
        when(mapper.map(domain, SolicitanteEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Solicitante.class)).thenReturn(domain);

        StepVerifier.create(repositoryAdapter.guardarSolicitante(domain))
                .expectNextMatches(s -> s.getCorreoElectronico().equals("test@mail.com"))
                .verifyComplete();

        verify(repository).save(entity);
    }

    @Test
    void guardarSolicitanteDebePropagarError() {
        when(mapper.map(domain, SolicitanteEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(repositoryAdapter.guardarSolicitante(domain))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("DB error"))
                .verify();
    }

    @Test
    void findByCorreoElectronicoDebeRetornarDomain() {
        when(repository.findByCorreoElectronico("test@mail.com")).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Solicitante.class)).thenReturn(domain);

        StepVerifier.create(repositoryAdapter.findByCorreoElectronico("test@mail.com"))
                .expectNextMatches(s -> s.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    void findByCorreoElectronicoDebeRetornarVacio() {
        when(repository.findByCorreoElectronico("notfound@mail.com")).thenReturn(Mono.empty());

        StepVerifier.create(repositoryAdapter.findByCorreoElectronico("notfound@mail.com"))
                .verifyComplete();
    }

    @Test
    void guardarSolicitanteConMapperNullDebeRetornarError() {
        when(mapper.map(domain, SolicitanteEntity.class)).thenReturn(null);
        when(repository.save(null)).thenReturn(Mono.error(new NullPointerException("entity null")));

        StepVerifier.create(repositoryAdapter.guardarSolicitante(domain))
                .expectError(NullPointerException.class)
                .verify();
    }
}
