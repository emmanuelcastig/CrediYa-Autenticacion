
CREATE TABLE IF NOT EXISTS rol (
                                   id_rol BIGSERIAL PRIMARY KEY,
                                   nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL
    );


CREATE TABLE IF NOT EXISTS solicitantes (
                                            id BIGSERIAL PRIMARY KEY,
                                            nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    documento_identidad VARCHAR(50) UNIQUE NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    id_rol BIGINT NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo_electronico VARCHAR(150) UNIQUE NOT NULL,
    salario_base NUMERIC(15,2) NOT NULL,
    CONSTRAINT fk_solicitante_rol FOREIGN KEY (id_rol)
    REFERENCES rol(id_rol)
    );