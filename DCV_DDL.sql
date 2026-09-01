DROP DATABASE IF EXISTS srpf;
CREATE DATABASE srpf CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE srpf;

-- Tabla de usuarios
CREATE TABLE usuario (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    puntuacion_elo INT NOT NULL DEFAULT 1000,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

-- Tabla de problemas de física
CREATE TABLE problema (
    id INT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(200) NOT NULL,
    enunciado TEXT NOT NULL,
    tema ENUM('Mecánica', 'Termodinámica', 'Electromagnetismo', 'Óptica', 'Física Moderna', 'Ondas', 'Otros') NOT NULL,
    nivel ENUM('Básico', 'Intermedio', 'Avanzado', 'Experto') NOT NULL DEFAULT 'Básico',
    resultado_esperado DECIMAL(15,6) NOT NULL,
    margen_tolerancia DECIMAL(10,6) NOT NULL DEFAULT 0.0,
    parametros_dinamicos JSON NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

-- Tabla de intentos de resolución
CREATE TABLE intento (
    id BIGINT NOT NULL AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    problema_id INT NOT NULL,
    competencia_id INT NULL,
    respuesta_enviada DECIMAL(15,6) NOT NULL,
    resultado ENUM('CORRECTO', 'INCORRECTO') NOT NULL,
    valores_generados JSON NULL,
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo ENUM('COMPETENCIA', 'GIMNASIO') NOT NULL DEFAULT 'COMPETENCIA',
    PRIMARY KEY (id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (problema_id) REFERENCES problema(id) ON DELETE RESTRICT
);

-- Tabla de competencias
CREATE TABLE competencia (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT NULL,
    fecha_inicio DATETIME NOT NULL,
    duracion_minutos SMALLINT NOT NULL,
    estado ENUM('PROGRAMADA', 'EN_CURSO', 'FINALIZADA', 'CANCELADA') NOT NULL DEFAULT 'PROGRAMADA',
    creador_id INT NOT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (creador_id) REFERENCES usuario(id) ON DELETE RESTRICT
);

-- Relación entre competencias y problemas
CREATE TABLE competencia_problema (
    competencia_id INT NOT NULL,
    problema_id INT NOT NULL,
    orden SMALLINT NOT NULL DEFAULT 1,
    PRIMARY KEY (competencia_id, problema_id),
    FOREIGN KEY (competencia_id) REFERENCES competencia(id) ON DELETE CASCADE,
    FOREIGN KEY (problema_id) REFERENCES problema(id) ON DELETE RESTRICT
);

-- Participantes en competencias
CREATE TABLE participante_competencia (
    id BIGINT NOT NULL AUTO_INCREMENT,
    competencia_id INT NOT NULL,
    usuario_id INT NOT NULL,
    fecha_inscripcion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_inicio_participacion DATETIME NULL,
    fecha_fin_participacion DATETIME NULL,
    puntuacion_final INT NOT NULL DEFAULT 0,
    tiempo_total_segundos INT NULL,
    intentos_totales SMALLINT NOT NULL DEFAULT 0,
    posicion_final SMALLINT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_competencia_usuario (competencia_id, usuario_id),
    FOREIGN KEY (competencia_id) REFERENCES competencia(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Mensajes privados entre usuarios
CREATE TABLE mensaje (
    id BIGINT NOT NULL AUTO_INCREMENT,
    remitente_id INT NOT NULL,
    destinatario_id INT NOT NULL,
    contenido TEXT NOT NULL,
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    leido BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    FOREIGN KEY (remitente_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (destinatario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Publicaciones del foro
CREATE TABLE foro_publicacion (
    id BIGINT NOT NULL AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    contenido TEXT NOT NULL,
    fecha_publicacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Respuestas a publicaciones del foro
CREATE TABLE foro_respuesta (
    id BIGINT NOT NULL AUTO_INCREMENT,
    publicacion_id BIGINT NOT NULL,
    usuario_id INT NOT NULL,
    contenido TEXT NOT NULL,
    fecha_respuesta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (publicacion_id) REFERENCES foro_publicacion(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);
