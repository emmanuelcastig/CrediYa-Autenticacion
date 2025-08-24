package co.com.pragma.usecase.solicitante;

import co.com.pragma.model.solicitante.Solicitante;
import co.com.pragma.model.solicitante.gateways.SolicitanteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class SolicitanteUseCaseTest {

    private SolicitanteRepository repository;
    private SolicitanteUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(SolicitanteRepository.class);
        useCase = new SolicitanteUseCase(repository);
    }

    @Test
    void crearSolicitanteExitoso() {

        Solicitante nuevo = new Solicitante();
        nuevo.setCorreoElectronico("test@mail.com");

        Mockito.when(repository.findByCorreoElectronico("test@mail.com"))
                .thenReturn(Mono.empty());
        Mockito.when(repository.guardarSolicitante(Mockito.any()))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.crearSolicitante(nuevo))
                .expectNextMatches(s -> s.getCorreoElectronico().equals("test@mail.com"))
                .verifyComplete();

        Mockito.verify(repository).guardarSolicitante(nuevo);
    }

    @Test
    void crearSolicitanteCorreoDuplicado() {

        Solicitante existente = new Solicitante();
        existente.setCorreoElectronico("test@mail.com");

        Solicitante nuevo = new Solicitante();
        nuevo.setCorreoElectronico("test@mail.com");

        Mockito.when(repository.findByCorreoElectronico("test@mail.com"))
                .thenReturn(Mono.just(existente));

        StepVerifier.create(useCase.crearSolicitante(nuevo))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El correo electrónico ya está en uso"))
                .verify();

        Mockito.verify(repository, Mockito.never()).guardarSolicitante(Mockito.any());
    }

    @Test
    void crearSolicitanteErrorEnRepositorio() {
        Solicitante nuevo = new Solicitante();
        nuevo.setCorreoElectronico("error@mail.com");

        Mockito.when(repository.findByCorreoElectronico("error@mail.com"))
                .thenReturn(Mono.error(new RuntimeException("DB failure")));

        StepVerifier.create(useCase.crearSolicitante(nuevo))
                .expectErrorMatches(ex -> ex instanceof RuntimeException &&
                        ex.getMessage().equals("DB failure"))
                .verify();

        Mockito.verify(repository, Mockito.never()).guardarSolicitante(Mockito.any());
    }
}