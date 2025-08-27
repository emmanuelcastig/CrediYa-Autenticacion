package co.com.pragma.r2dbc;

import co.com.pragma.model.rol.Rol;
import co.com.pragma.r2dbc.entity.RolEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class RolReactiveRepositoryAdapterTest {

    private RolReactiveRepository repository;
    private ObjectMapper mapper;
    private RolReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(RolReactiveRepository.class);
        mapper = mock(ObjectMapper.class);
        adapter = new RolReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    void testFindById_Exitoso() {
        // Arrange
        Long id = 1L;
        RolEntity entity = new RolEntity();
        entity.setIdRol(id);
        entity.setNombre("ADMIN");

        Rol rol = new Rol();
        rol.setIdRol(id);
        rol.setNombre("ADMIN");

        when(repository.findById(id)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Rol.class)).thenReturn(rol);

        // Act & Assert
        StepVerifier.create(adapter.findById(id))
                .expectNextMatches(r -> r.getIdRol().equals(id) && r.getNombre().equals("ADMIN"))
                .verifyComplete();

        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).map(entity, Rol.class);
    }

    @Test
    void testFindById_NoExiste() {
        // Arrange
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(adapter.findById(id))
                .verifyComplete(); // Debe terminar vacío

        verify(repository, times(1)).findById(id);
        verifyNoInteractions(mapper);
    }
}
