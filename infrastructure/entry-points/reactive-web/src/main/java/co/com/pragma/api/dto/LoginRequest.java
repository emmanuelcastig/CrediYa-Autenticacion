package co.com.pragma.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotNull(message = "El email es obligatorio")
    private String email;
    @NotNull(message = "La clave es obligatoria")
    private String clave;
}
