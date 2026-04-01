package san.projectdates.core.services;

import san.projectdates.core.entities.Role;
import san.projectdates.core.entities.User;
import san.projectdates.core.repositories.UserRepository;
import san.projectdates.infrastructure.http.dtos.UserResponse;

public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  public UserResponse createNewUser(String username, String email, String password, String lastname, Role role){
    try{
      User newUser = new User(username, email, password, lastname, role);
      User savedUser = this.userRepository.saveUser(newUser);
      if(savedUser != null){
        return new UserResponse(
          savedUser.getId(),
          savedUser.getUsername(),
          savedUser.getLastname(),
          savedUser.getEmail(),
          savedUser.getRole()
        );
      }
      throw new RuntimeException("El repositorio no devolvió el usuario guardado.");
    }catch(Exception e){
      throw new RuntimeException("Error en la creación del usuario: " + e.getMessage());
    }
  }
}
