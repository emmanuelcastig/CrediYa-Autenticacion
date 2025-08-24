CREATE TABLE IF NOT EXISTS solicitantes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo_electronico VARCHAR(150) UNIQUE NOT NULL,
    salario_base NUMERIC(15,2) NOT NULL
    );
