package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.SolicitanteRequest;
import co.com.pragma.api.dto.SolicitanteResponse;
import co.com.pragma.model.solicitante.Solicitante;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SolicitanteMapperTest {

    private final SolicitanteMapper mapper = Mappers.getMapper(SolicitanteMapper.class);

    @Test
    void testToDomain_FromRequest() {

        SolicitanteRequest request = new SolicitanteRequest();
        request.setNombre("Juan");
        request.setApellido("Pérez");
        request.setCorreoElectronico("juan@example.com");
        request.setFechaNacimiento(LocalDate.of(1990, 1, 15));
        request.setSalarioBase(new BigDecimal("2500000"));
        request.setDireccion("Calle 123 # 45-67");
        request.setTelefono("3001234567");

        Solicitante domain = mapper.toDomain(request);

        assertNotNull(domain);
        assertEquals("Juan", domain.getNombre());
        assertEquals("Pérez", domain.getApellido());
        assertEquals("juan@example.com", domain.getCorreoElectronico());
        assertEquals(LocalDate.of(1990, 1, 15), domain.getFechaNacimiento());
        assertEquals(new BigDecimal("2500000"), domain.getSalarioBase());
        assertEquals("Calle 123 # 45-67", domain.getDireccion());
        assertEquals("3001234567", domain.getTelefono());
    }

    @Test
    void testToDomain_WithNullRequest() {

        Solicitante domain = mapper.toDomain(null);


        assertNull(domain);
    }

    @Test
    void testToDomain_WithPartialData() {
        // Given
        SolicitanteRequest request = new SolicitanteRequest();
        request.setNombre("Ana");
        request.setApellido("Gomez");
        request.setCorreoElectronico("ana@example.com");

        Solicitante domain = mapper.toDomain(request);

        assertNotNull(domain);
        assertEquals("Ana", domain.getNombre());
        assertEquals("Gomez", domain.getApellido());
        assertEquals("ana@example.com", domain.getCorreoElectronico());
        assertNull(domain.getSalarioBase());
        assertNull(domain.getDireccion());
        assertNull(domain.getTelefono());
        assertNull(domain.getFechaNacimiento());
    }

    @Test
    void testToResponse_FromDomain() {
        // Given
        Solicitante domain = Solicitante.builder()
                .id(1L)
                .nombre("Maria")
                .apellido("Lopez")
                .correoElectronico("maria@example.com")
                .fechaNacimiento(LocalDate.of(1985, 5, 20))
                .salarioBase(new BigDecimal("3500000"))
                .direccion("Av Principal # 100-50")
                .telefono("3109876543")
                .build();

        // When
        SolicitanteResponse response = mapper.toResponse(domain);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Maria", response.getNombre());
        assertEquals("Lopez", response.getApellido());
        assertEquals("maria@example.com", response.getCorreoElectronico());
        assertEquals(LocalDate.of(1985, 5, 20), response.getFechaNacimiento());
        assertEquals(new BigDecimal("3500000"), response.getSalarioBase());
        assertEquals("Av Principal # 100-50", response.getDireccion());
        assertEquals("3109876543", response.getTelefono());
    }

    @Test
    void testToResponse_WithNullDomain() {
        SolicitanteResponse response = mapper.toResponse(null);

        assertNull(response);
    }

}