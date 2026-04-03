package san.projectdates.core.services;

import java.util.List;
import java.util.UUID;

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
    User newUser = new User(username, email, password, lastname, role);
    
    if(isEmailTaken(newUser.getEmail())){
      throw new RuntimeException("El correo está en uso");
    }

    User savedUser = userRepository.saveUser(newUser);

    return new UserResponse(savedUser);
  }

  public UserResponse findUserByEmail(String email) {
    if(!this.isEmailTaken(email)){
      throw new RuntimeException("el usuario con este email no existe");
    }

    User getUserByEmail = this.findEntityByEmail(email);
    return new UserResponse(getUserByEmail);
  }

  public User findEntityByEmail(String email) {
    User getUser = userRepository.getUserByEmail(email);
    if(getUser == null){
      throw new RuntimeException("No se ha podido obtener el usuario con el email: " + email);
    }
    return getUser;  
  }

  public boolean isEmailTaken(String email) {
    return userRepository.emailIsAlreadyUse(email);
  }

  public String deleteUserById(UUID id){
    if(!userRepository.validateUserExist(id)){
      throw new RuntimeException("El usuario no existe");
    }

    int deleteUser = userRepository.deleteUserById(id);
    if(deleteUser == 0){
      throw new RuntimeException("No se ha eliminado ningun usuario");
    }
    String response = "Filas eliminadas: " + deleteUser + "Usuario eliminado con exito";
    return response;
  }

  public List<UserResponse> findAllUsers(){
    List<User>  fullUsers = userRepository.findAllUsers();
    return fullUsers.stream()
      .map(user -> new UserResponse(user))
      .toList();
  }
}
