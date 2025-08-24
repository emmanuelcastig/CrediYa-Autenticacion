package co.com.pragma.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "DTO para crear un nuevo solicitante")
public class SolicitanteRequest {

    @NotBlank
    @Schema(description = "Nombre del solicitante", example = "Juan")
    private String nombre;

    @NotBlank
    @Schema(description = "Apellido del solicitante", example = "Pérez")
    private String apellido;

    @NotNull
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    @Schema(description = "Fecha de nacimiento (debe ser pasada)", example = "1990-05-15")
    private LocalDate fechaNacimiento;

    @NotBlank
    @Schema(description = "Dirección de residencia", example = "Calle 123 #45-67, Bogotá")
    private String direccion;

    @NotBlank
    @Schema(description = "Teléfono de contacto", example = "3001234567")
    private String telefono;

    @NotBlank
    @Email(message = "El correo electrónico debe ser válido")
    @Schema(description = "Correo electrónico válido", example = "juan.perez@email.com")
    private String correoElectronico;

    @NotNull
    @Min(value = 0, message = "El salario base debe ser mayor o igual a 0")
    @Max(value = 15000000, message = "El salario base debe ser menor o igual a 15000000")
    @Schema(description = "Salario base en pesos colombianos", example = "2500000")
    private BigDecimal salarioBase;
}
