package co.com.pragma.api.config;

import co.com.pragma.api.security.CustomAuthenticationEntryPoint;
import co.com.pragma.api.security.JwtAuthenticationManager;
import co.com.pragma.api.security.JwtSecurityContextRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    public SecurityConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            JwtAuthenticationManager jwtAuthenticationManager,
                                                            JwtSecurityContextRepository securityContextRepository) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/v1/login").permitAll()
                        .anyExchange().authenticated()
                )
                .exceptionHandling(exchange -> exchange
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .authenticationManager(jwtAuthenticationManager)
                .securityContextRepository(securityContextRepository)
                .build();
    }

    @Bean
    public JwtAuthenticationManager jwtAuthenticationManager(JwtProvider jwtProvider) {
        return new JwtAuthenticationManager(jwtProvider);
    }

    @Bean
    public JwtSecurityContextRepository securityContextRepository(JwtAuthenticationManager jwtAuthenticationManager) {
        return new JwtSecurityContextRepository(jwtAuthenticationManager);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
