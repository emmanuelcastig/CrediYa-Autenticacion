package co.com.pragma.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SolicitanteRequest {
    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String documentoIdentidad;

    @NotNull
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNacimiento;

    @NotNull
    private int idRol;

    @NotBlank
    private String direccion;

    @NotBlank
    private String telefono;

    @NotBlank
    @Email(message = "El correo electronico debe ser valido")
    private String correoElectronico;

    @NotNull
    @Min(value = 0, message = "El salario base debe ser mayor o igual a 0")
    @Max(value = 15000000, message = "El salario base debe ser menor o igual a 15000000")
    private BigDecimal salarioBase;
}
