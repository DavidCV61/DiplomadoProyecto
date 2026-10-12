package com.srpf.security.model;

import com.srpf.entity.Usuario;
import com.srpf.enums.Rol;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
  private final Integer id;
  private final String name;
  private final String username;
  private final String email;
  private final String password;
  private final Rol rol;

  public UserDetailsImpl(Usuario usuario) {
    this.id = usuario.getId();
    this.name = usuario.getNombre();
    this.username = usuario.getUsername();
    this.email = usuario.getEmail();
    this.password = usuario.getPasswordHash();
    this.rol = usuario.getRol();
  }

  public Integer getId() {
    return id;
  }

  /**
   * getName
   * 
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * getEmail
   * 
   * @return email
   */
  public String getEmail() {
    return email;
  }

  public Rol getRol() {
    return rol;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
  }

  /**
   * getUsername
   * 
   * @return username
   */
  @Override
  public String getUsername() {
    return username;
  }

  /**
   * getPassword
   * 
   * @return password hash
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * isEnabled
   * 
   * @return if user is enabled
   */
  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
}
