-- DDL para el microservicio de citas y notificaciones
CREATE TABLE IF NOT EXISTS citas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora DATETIME NOT NULL,
    especialidad VARCHAR(255),
    medico_asignado VARCHAR(255),
    estado VARCHAR(50) DEFAULT 'PROGRAMADA',
    paciente_id BIGINT NOT NULL,
    slot_id VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS notificaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_envio DATETIME NOT NULL,
    tipo VARCHAR(50)
);

-- indices sugeridos para optimizar las consultas 
CREATE INDEX idx_citas_paciente ON citas(paciente_id);
CREATE INDEX idx_notificaciones_paciente ON notificaciones(paciente_id);