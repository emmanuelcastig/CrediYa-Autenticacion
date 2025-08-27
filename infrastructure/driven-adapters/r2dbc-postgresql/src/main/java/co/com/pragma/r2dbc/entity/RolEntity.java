package co.com.pragma.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("rol")
public class RolEntity {
    @Id
    private Long idRol;
    private String nombre;
    private String descripcion;
}
