USE srpf;

INSERT INTO usuario (nombre, username, email, password_hash, rol, puntuacion_elo) VALUES
('Administrador', 'admin', 'admin@email.com', 'admin123', 'ADMIN', 3000);

INSERT INTO problema (titulo, enunciado, tema, nivel, resultado_esperado, margen_tolerancia) VALUES
('Gravedad en la Tierra', '¿Cuál es el valor de la aceleración de la gravedad en la superficie terrestre (en m/s²)?', 'Mecánica', 'Básico', 9.8, 0.1),
('Velocidad del sonido', '¿Cuál es la velocidad del sonido en el aire a nivel del mar (en m/s)?', 'Ondas', 'Básico', 343.0, 5.0);

INSERT INTO competencia (nombre, descripcion, fecha_inicio, duracion_minutos, estado, creador_id) VALUES
('Cometencia Test', 'Competencia pruebas.', '2026-09-01 10:00:00', 60, 'PROGRAMADA', 1);
