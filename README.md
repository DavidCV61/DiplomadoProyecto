# SRPF – Sistema de Resolución de Problemas y Foro

Proyecto final del Diplomado de Java de la DGTIC-UNAM.

Autor: David Cañedo Vértiz

## Tecnologías

- Java 17 y Maven, con wrapper incluido
- Spring Boot 4, Spring Security 7 y Spring Data JPA
- Thymeleaf y Bootstrap 5
- MariaDB
- JWT con refresh token, usando la librería jjwt 0.12.5
- Lombok y Slf4j para los logs

## Cómo ejecutarlo

### 1. Requisitos

JDK 17 y MariaDB instalados y encendidos.

### 2. Base de datos

Desde la raíz del repositorio:

```bash
mariadb -u TU_USUARIO -p -e "CREATE DATABASE IF NOT EXISTS srpf"
mariadb -u TU_USUARIO -p srpf < BaseDeDatos/schema.sql
```

### 3. Configuración

Edita `Spring/srpf/src/main/resources/application.properties` y cambia estos tres valores:

```properties
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
jwt.secret=tu_clave
```

- `tu_usuario` y `tu_password`: usuario y contraseña de tu MariaDB.
- `tu_clave`: cualquier texto de 32 caracteres o más, que se usa para firmar los JWT.

### 4. Ejecutar

```bash
cd Spring/srpf
./mvnw spring-boot:run
```

La aplicación queda en http://localhost:8088

## Generalidades

SRPF es una plataforma para resolver problemas de programación y matemáticas, participar en competencias y ver un ranking. Cada usuario tiene una puntuación Elo, similar a la de Codeforces, que sube o baja según su desempeño.

Módulos principales:

- **Problemas:** catálogo de problemas, algunos dinámicos, con valores distintos en cada intento.
- **Competencias:** competencias con problemas y posiciones, que afectan el Elo.
- **Ranking:** clasificación de usuarios por Elo.
- **Enviados:** historial de intentos.
- **Foro:** publicaciones y respuestas.
- **Perfiles y Mensajes:** perfil de cada usuario y mensajes privados.
- **Administración:** gestión de usuarios, solo para ADMIN.

### Seguridad

Hay dos cadenas de Spring Security:

- **Web con Thymeleaf:** login con formulario. Al entrar, el servidor genera un token de acceso de 15 minutos y un refresh token de 7 días, y los guarda en las cookies `token` y `refresh_token`, con HttpOnly y SameSite Lax. Un filtro, `CookieJwtAuthenticationFilter`, lee la cookie en cada petición y renueva las cookies si el token expiró pero el refresh sigue válido. Incluye protección CSRF y un logout que borra las cookies.
- **API en `/api/**`:** sin sesión ni cookies. `POST /api/auth/login` devuelve los tokens en JSON, y las demás rutas exigen `Authorization: Bearer <token>`. `POST /api/auth/refresh` renueva el token.

Roles con jerarquía: `ADMIN > AUTOR > USER`. Las contraseñas se guardan con BCrypt. Al registrarse, el rol siempre es USER.

Nota: el Documento de Especificación describe la primera versión del proyecto, donde la parte web usaba sesión. Para el proyecto final se migró esa parte a JWT en cookies. La arquitectura de dos cadenas es la misma.

## Manual de uso

1. **Registro:** en el menú, "Registrarse". Captura usuario, correo y contraseña, y confirma la contraseña. Si todo es válido, te manda al login.
2. **Inicio de sesión:** "Iniciar Sesion" con tu usuario y contraseña. Al entrar, el inicio te saluda con tu nombre.
3. **Problemas:** abre un problema, escribe tu respuesta y envíala. Si es dinámico, los números cambian en cada intento.
4. **Competencias:** entra a una competencia, participa resolviendo sus problemas y revisa tu posición.
5. **Ranking y Enviados:** consulta la tabla de posiciones y tu historial de intentos.
6. **Foro:** lee y crea publicaciones, y responde.
7. **Perfiles y Mensajes:** entra al perfil de otro usuario y envíale un mensaje. En "Mensajes" ves tus conversaciones.
8. **Mi Perfil y cierre de sesión:** el botón con tu nombre abre "Mi Perfil" y "Cerrar Sesion".
9. **Roles:** un usuario AUTOR o ADMIN ve el menú "Autor", para crear problemas y competencias. Solo ADMIN ve "Administracion", para gestionar usuarios. Si un USER entra a una ruta sin permiso, se muestra "acceso denegado".

### Probar la API

1. `POST http://localhost:8088/api/auth/login` con cuerpo JSON `{"username": "...", "password": "..."}`. Responde con el token y el refresh token.
2. Llamar una ruta protegida, por ejemplo `POST /api/problemas/{id}/respuesta`: sin token responde 401, y con `Authorization: Bearer <token>` responde 200.
3. `POST /api/auth/refresh` con el refresh token para obtener tokens nuevos.

## Trabajo futuro

Respuestas a respuestas en el foro, foto de perfil, Markdown en las preguntas, amigos y equipos, edición de publicaciones y mejoras en la interfaz. Revocación de tokens guardándolos en base de datos.
