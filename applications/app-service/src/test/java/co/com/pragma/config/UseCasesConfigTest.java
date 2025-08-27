package co.com.pragma.config;

import co.com.pragma.model.rol.gateways.RolRepository;
import co.com.pragma.model.solicitante.gateways.SolicitanteRepository;
import co.com.pragma.usecase.autenticacion.AutenticacionUseCase;
import co.com.pragma.usecase.in.BuscarRolPort;
import co.com.pragma.usecase.in.CrearSolicitantePort;
import co.com.pragma.usecase.in.LoginSolicitantePort;
import co.com.pragma.usecase.rol.RolUseCase;
import co.com.pragma.usecase.solicitante.SolicitanteUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(TestConfig.class)) {


            CrearSolicitantePort crearSolicitantePort = context.getBean(CrearSolicitantePort.class);
            LoginSolicitantePort loginSolicitantePort = context.getBean(LoginSolicitantePort.class);
            BuscarRolPort buscarRolPort = context.getBean(BuscarRolPort.class);

            assertNotNull(crearSolicitantePort, "El bean CrearSolicitantePort no fue registrado");
            assertNotNull(loginSolicitantePort, "El bean LoginSolicitantePort no fue registrado");
            assertNotNull(buscarRolPort, "El bean BuscarRolPort no fue registrado");


            assertTrue(crearSolicitantePort instanceof SolicitanteUseCase);
            assertTrue(loginSolicitantePort instanceof AutenticacionUseCase);
            assertTrue(buscarRolPort instanceof RolUseCase);
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {
        @Bean
        public SolicitanteRepository solicitanteRepository() {
            return mock(SolicitanteRepository.class);
        }

        @Bean
        public RolRepository rolRepository() {
            return mock(RolRepository.class);
        }
    }
}
