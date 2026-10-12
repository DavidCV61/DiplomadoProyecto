# SRPF

Proyecto del Diplomado de Desarrollo en Java (DGTIC, UNAM), Módulo 9: Spring Security.

## Tecnologías

Java 17, Spring Boot 4, Spring Security 7, Spring Data JPA, Thymeleaf, MariaDB, Maven, jjwt.

## Cómo ejecutarlo

Requisitos: Java 17 y MariaDB.

1. Crear la base de datos `srpf` y ejecutar el script `BaseDeDatos/schema.sql`.

2. Abrir `Spring/srpf/src/main/resources/application.properties` y cambiar estas líneas por tus datos:

       spring.datasource.username=tu_usuario
       spring.datasource.password=tu_password
       jwt.secret=tu_clave

   `jwt.secret` debe ser un texto largo (mínimo 64 caracteres).

3. Desde la carpeta `Spring/srpf`, ejecutar:

       ./mvnw spring-boot:run

4. Abrir http://localhost:8088
