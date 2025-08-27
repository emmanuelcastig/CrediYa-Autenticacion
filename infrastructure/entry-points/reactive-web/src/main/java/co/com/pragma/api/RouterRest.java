package co.com.pragma.api;

import co.com.pragma.api.dto.LoginRequest;
import co.com.pragma.api.dto.LoginResponse;
import co.com.pragma.api.dto.SolicitanteRequest;
import co.com.pragma.api.dto.SolicitanteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    beanClass = Handler.class,
                    beanMethod = "crearSolicitante",
                    operation = @Operation(
                            operationId = "crearSolicitante",
                            summary = "Crear un nuevo solicitante",
                            description = "Recibe los datos de un solicitante y lo guarda en la base de datos",
                            tags = {"Solicitantes"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Datos del solicitante",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = SolicitanteRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Solicitante creado correctamente",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = SolicitanteResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Datos inválidos o validación fallida"
                                    ),
                                    @ApiResponse(
                                            responseCode = "409",
                                            description = "Conflicto - El correo electrónico ya está en uso"
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/{documento}",
                    beanClass = Handler.class,
                    beanMethod = "solicitanteExistente",
                    operation = @Operation(
                            operationId = "verificarSolicitanteExistente",
                            summary = "Verificar si un solicitante existe",
                            description = "Verifica si un solicitante existe en el sistema basado en su documento de identidad",
                            tags = {"Solicitantes"},
                            parameters = {
                                    @Parameter(
                                            name = "documento",
                                            description = "Número de documento de identidad del solicitante",
                                            required = true,
                                            example = "1234567890",
                                            schema = @Schema(type = "string")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Solicitante existe en el sistema",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = java.util.Map.class,
                                                            example = "{\"existe\": true, \"mensaje\": \"El solicitante existe\"}")
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Solicitante no encontrado",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = java.util.Map.class,
                                                            example = "{\"existe\": false, \"mensaje\": \"No se encontró solicitante\"}")
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Documento inválido o mal formado"
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/login",
                    beanClass = Handler.class,
                    beanMethod = "login",
                    operation = @Operation(
                            operationId = "login",
                            summary = "Inicio de sesión de usuario",
                            description = "Permite a un usuario autenticarse con correo electrónico y clave. Devuelve un token JWT y el rol del usuario.",
                            tags = {"Autenticación"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Datos de login",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = LoginRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Login exitoso, retorna token y rol",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = LoginResponse.class,
                                                            example = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR...\", \"rol\": \"CLIENTE\"}")
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Credenciales inválidas",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = java.util.Map.class,
                                                            example = "{\"error\": \"Credenciales invalidas\"}")
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor"
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/usuarios"), handler::crearSolicitante)
                .andRoute(GET("/api/v1/usuarios/{documento}"), handler::solicitanteExistente)
                .andRoute(POST("/api/v1/login"), handler::login);
    }
}
