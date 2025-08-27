package co.com.pragma.config;

import co.com.pragma.model.rol.gateways.RolRepository;
import co.com.pragma.model.solicitante.gateways.SolicitanteRepository;
import co.com.pragma.usecase.autenticacion.AutenticacionUseCase;
import co.com.pragma.usecase.in.BuscarRolPort;
import co.com.pragma.usecase.in.LoginSolicitantePort;
import co.com.pragma.usecase.rol.RolUseCase;
import co.com.pragma.usecase.solicitante.SolicitanteUseCase;
import co.com.pragma.usecase.in.CrearSolicitantePort;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "co.com.pragma.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    private final SolicitanteRepository solicitanteRepository;
    private final RolRepository rolRepository;

    public UseCasesConfig(SolicitanteRepository solicitanteRepository, RolRepository rolRepository) {
        this.solicitanteRepository = solicitanteRepository;
        this.rolRepository = rolRepository;
    }

    @Bean
    @Primary
    public CrearSolicitantePort crearSolicitantePort() {
        return new SolicitanteUseCase(solicitanteRepository);
    }

    @Bean
    @Primary
    public LoginSolicitantePort loginSolicitantePort() {
        return new AutenticacionUseCase(solicitanteRepository);
    }

    @Bean
    @Primary
    public BuscarRolPort buscarRolPort() {
        return new RolUseCase(rolRepository);
    }
}
