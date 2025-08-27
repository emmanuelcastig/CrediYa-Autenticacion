package co.com.pragma.model.solicitante;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitante {
    private Long id;
    private String nombre;
    private String apellido;
    private String documentoIdentidad;
    private LocalDate fechaNacimiento;
    private String clave;
    private Long idRol;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private BigDecimal salarioBase;
}
