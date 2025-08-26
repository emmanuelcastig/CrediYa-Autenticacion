package co.com.pragma.config;

import co.com.pragma.model.solicitante.gateways.SolicitanteRepository;
import co.com.pragma.usecase.autenticacion.AutenticacionUseCase;
import co.com.pragma.usecase.in.LoginSolicitantePort;
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

    public UseCasesConfig(SolicitanteRepository solicitanteRepository) {
        this.solicitanteRepository = solicitanteRepository;
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
}
