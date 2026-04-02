package san.projectdates.core.entities;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class User {
  private UUID id;
  private String username;
  private String email;
  private String password;
  private String lastname;
  private Role role;
  private LocalDate birthday;
  private OffsetDateTime createdAt;

  public User(){}

  public User(String username, String email, String password, String lastname, Role role){
    this.setId();
    this.setUsername(username);
    this.setLastname(lastname);
    this.setEmail(email);
    this.setPassword(password);
    this.setRole(role);
    this.createdAt = OffsetDateTime.now();
  }

  public User(UUID id, String username, String email, String password, String lastname, Role role, LocalDate birthday, OffsetDateTime createdAt) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.lastname = lastname;
    this.role = role;
    this.birthday = birthday;
    this.createdAt = createdAt;
  }


  public UUID getId(){
    return id;
  }

  public void setId(){
    this.id = UUID.randomUUID();
  }

  public String getUsername(){
    return username;
  }

  public void setUsername(String username){
    this.username = username;
  }

  public String getEmail(){
    return email;
  }

  public void setEmail(String email){
    if(!email.contains("@")){
      throw new RuntimeException("El correo debe incluir @");
    }
    email = email.toLowerCase();

    this.email = email;
  }

  public String getLastname(){
    return lastname;
  }

  public void setLastname(String lastname){
    this.lastname = lastname;
  }

  public LocalDate getBirthday(){  
    return birthday;
  }

  public void setBirthday(LocalDate birthday){
    this.birthday = birthday;
  }

  public String getPassword(){
    return password;
  }

  public void setPassword(String password){
    if (password == null || password.trim().length() < 8) {
      throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
    }

    char[] passwordChars = password.toCharArray();
    String hashedPassword = BCrypt.withDefaults().hashToString(12, passwordChars);
    this.password = hashedPassword;
  }

  public OffsetDateTime getCreatedAt(){
    return createdAt;
  }

  public Role getRole(){
    return role;
  }

  public void setRole(Role role){
    if(role == null){
      this.role = Role.USER;
      return;
    }
    this.role = role;
  }
}
