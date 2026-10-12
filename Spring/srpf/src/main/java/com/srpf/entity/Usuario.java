
package com.srpf.entity;

import com.srpf.enums.Rol;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "usuario")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @Column(name = "username", nullable = false, unique = true, length = 50)
  private String username;

  @Column(name = "email", nullable = false, unique = true, length = 150)
  private String email;

  @Column(name = "password_hash", nullable = false, length = 255)
  private String passwordHash;

  @Enumerated(EnumType.STRING)
  @Column(name = "rol", nullable = false)
  private Rol rol = Rol.USER;

  @Column(name = "puntuacion_elo", nullable = false)
  private Integer puntuacionElo = 1000;

  @CreationTimestamp
  @Column(name = "fecha_registro", nullable = false, updatable = false)
  private LocalDateTime fechaRegistro;

  public Usuario() {
  }

  public Usuario(String nombre, String username, String email, String passwordHash, Rol rol) {
    this.nombre = nombre;
    this.username = username;
    this.email = email;
    this.passwordHash = passwordHash;
    this.rol = (rol != null) ? rol : Rol.USER;
    this.puntuacionElo = 1000;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public Rol getRol() {
    return rol;
  }

  public void setRol(Rol rol) {
    this.rol = rol;
  }

  public Integer getPuntuacionElo() {
    return puntuacionElo;
  }

  public void setPuntuacionElo(Integer puntuacionElo) {
    this.puntuacionElo = puntuacionElo;
  }

  public LocalDateTime getFechaRegistro() {
    return fechaRegistro;
  }

  public void setFechaRegistro(LocalDateTime fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Usuario usuario))
      return false;
    return Objects.equals(id, usuario.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Usuario{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", rol=" + rol +
        ", puntuacionElo=" + puntuacionElo +
        '}';
  }
}
