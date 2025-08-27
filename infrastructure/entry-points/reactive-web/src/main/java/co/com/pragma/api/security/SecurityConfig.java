package co.com.pragma.api.security;

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

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            JwtAuthenticationManager jwtAuthenticationManager,
                                                            JwtSecurityContextRepository securityContextRepository) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/v1/login").permitAll()
                        .pathMatchers("/api/v1/usuarios/*").hasAnyRole("CLIENTE", "ADMINISTRADOR", "ASESOR")
                        .pathMatchers("/api/v1/usuarios").hasAnyRole("ADMINISTRADOR", "ASESOR")
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
