
DROP DATABASE IF EXISTS srpf;
CREATE DATABASE srpf CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE srpf;

CREATE TABLE usuario (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol ENUM('USER', 'AUTOR', 'ADMIN') NOT NULL DEFAULT 'USER',
    puntuacion_elo INT NOT NULL DEFAULT 1000,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE problema (
    id INT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(200) NOT NULL,
    enunciado TEXT NOT NULL,
    tema ENUM('MECANICA', 'TERMODINAMICA', 'ELECTROMAGNETISMO', 'OPTICA', 'FISICA_MODERNA', 'ONDAS', 'OTROS') NOT NULL,
    nivel ENUM('BASICO', 'INTERMEDIO', 'AVANZADO', 'EXPERTO') NOT NULL DEFAULT 'BASICO',
    resultado_esperado DECIMAL(15,6) NOT NULL,
    margen_tolerancia DECIMAL(10,6) NOT NULL DEFAULT 0.0,
    parametros_dinamicos JSON NULL,
    autor_id INT NOT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (autor_id) REFERENCES usuario(id) ON DELETE RESTRICT
);

CREATE TABLE competencia (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT NULL,
    fecha_inicio DATETIME NOT NULL,
    duracion_minutos SMALLINT NOT NULL,
    estado ENUM('PROPUESTA', 'PROGRAMADA', 'EN_CURSO', 'FINALIZADA', 'CANCELADA') NOT NULL DEFAULT 'PROPUESTA',
    creador_id INT NOT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (creador_id) REFERENCES usuario(id) ON DELETE RESTRICT
);

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
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE RESTRICT,
    FOREIGN KEY (problema_id) REFERENCES problema(id) ON DELETE RESTRICT,
    FOREIGN KEY (competencia_id) REFERENCES competencia(id) ON DELETE RESTRICT
);

CREATE TABLE competencia_problema (
    competencia_id INT NOT NULL,
    problema_id INT NOT NULL,
    orden SMALLINT NOT NULL DEFAULT 1,
    PRIMARY KEY (competencia_id, problema_id),
    FOREIGN KEY (competencia_id) REFERENCES competencia(id) ON DELETE RESTRICT,
    FOREIGN KEY (problema_id) REFERENCES problema(id) ON DELETE RESTRICT
);

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
    elo_antes INT NULL,
    elo_despues INT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_competencia_usuario (competencia_id, usuario_id),
    FOREIGN KEY (competencia_id) REFERENCES competencia(id) ON DELETE RESTRICT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE RESTRICT
);

CREATE TABLE mensaje (
    id BIGINT NOT NULL AUTO_INCREMENT,
    remitente_id INT NOT NULL,
    destinatario_id INT NOT NULL,
    contenido TEXT NOT NULL,
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    leido BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    FOREIGN KEY (remitente_id) REFERENCES usuario(id) ON DELETE RESTRICT,
    FOREIGN KEY (destinatario_id) REFERENCES usuario(id) ON DELETE RESTRICT
);

CREATE TABLE foro_publicacion (
    id BIGINT NOT NULL AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    contenido TEXT NOT NULL,
    fecha_publicacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE RESTRICT
);

CREATE TABLE foro_respuesta (
    id BIGINT NOT NULL AUTO_INCREMENT,
    publicacion_id BIGINT NOT NULL,
    usuario_id INT NOT NULL,
    contenido TEXT NOT NULL,
    fecha_respuesta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (publicacion_id) REFERENCES foro_publicacion(id) ON DELETE RESTRICT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE RESTRICT
);

CREATE INDEX idx_intento_usuario ON intento(usuario_id);
CREATE INDEX idx_mensaje_conversacion ON mensaje(remitente_id, destinatario_id);
CREATE INDEX idx_foro_respuesta_pub ON foro_respuesta(publicacion_id);
