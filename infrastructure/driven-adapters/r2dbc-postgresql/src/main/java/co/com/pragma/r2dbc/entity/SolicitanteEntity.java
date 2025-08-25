package co.com.pragma.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Table("solicitantes")
public class SolicitanteEntity {
    @Id
    private Long id;
    private String nombre;
    private String apellido;
    private String documentoIdentidad;
    private LocalDate fechaNacimiento;
    private int idRol;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private BigDecimal salarioBase;
}
