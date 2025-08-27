package co.com.pragma.usecase.rol;

import co.com.pragma.model.rol.Rol;
import co.com.pragma.model.rol.gateways.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class RolUseCaseTest {

    private RolRepository rolRepository;
    private RolUseCase rolUseCase;

    @BeforeEach
    void setUp() {
        rolRepository = mock(RolRepository.class);
        rolUseCase = new RolUseCase(rolRepository);
    }

    @Test
    void testBuscarPorId_Exitoso() {
        // Arrange
        Long rolId = 1L;
        Rol rol = new Rol();
        rol.setIdRol(rolId);
        rol.setNombre("ADMIN");

        when(rolRepository.findById(rolId)).thenReturn(Mono.just(rol));

        // Act & Assert
        StepVerifier.create(rolUseCase.buscarPorId(rolId))
                .expectNextMatches(r -> r.getIdRol().equals(rolId) && r.getNombre().equals("ADMIN"))
                .verifyComplete();

        verify(rolRepository, times(1)).findById(rolId);
    }

    @Test
    void testBuscarPorId_NoExiste() {
        // Arrange
        Long rolId = 2L;
        when(rolRepository.findById(rolId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(rolUseCase.buscarPorId(rolId))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("No se encontró el rol con id: " + rolId))
                .verify();

        verify(rolRepository, times(1)).findById(rolId);
    }
}
