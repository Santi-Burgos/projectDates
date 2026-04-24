package san.projectdates.core.services;

import java.util.List;
import java.util.UUID;

import san.projectdates.core.dtos.UserCreateRequest;
import san.projectdates.core.dtos.UserResponse;
import san.projectdates.core.entities.Role;
import san.projectdates.core.entities.User;
import san.projectdates.core.repositories.ErrorFactory;
import san.projectdates.core.repositories.UserRepository;

public class UserService {
  private final UserRepository userRepository;
  private final ErrorFactory error;

  public UserService(UserRepository userRepository, ErrorFactory errorFactory){
    this.userRepository = userRepository;
    this.error = errorFactory;
  }

  public UserResponse createNewUser(String username, String email, String password, String lastname, Role role){
    User newUser = new User(username, email, password, lastname, role);
    
    if(isEmailTaken(newUser.getEmail())){
      throw error.conflict("El correo está en uso");
    }

    User savedUser = userRepository.saveUser(newUser);

    return new UserResponse(savedUser);
  }

  public UserResponse findUserByEmail(String email) {
    if(!this.isEmailTaken(email)){
      throw error.conflict("El usuario con este email no existe");
    }
    User getUserByEmail = this.findEntityByEmail(email);

    return new UserResponse(getUserByEmail);
  }

  public User findEntityByEmail(String email) {
    User getUser = userRepository.getUserByEmail(email);
    if(getUser == null){
      throw error.notFound("No se ha podido obtener el usuario con el email: " + email);
    }
    return getUser;  
  }

  public boolean isEmailTaken(String email) {
    return userRepository.emailIsAlreadyUse(email);
  }

  public String deleteUserById(UUID id){
    if(!userRepository.validateUserExist(id)){
      throw error.notFound("El usuario no existe");
    }

    int deleteUser = userRepository.deleteUserById(id);
    if(deleteUser == 0){
      throw error.notFound("No se ha eliminado ningun usuario");
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

  public UserResponse updateUser(UUID id, UserCreateRequest updateRequest) {
    User user = userRepository.getUserById(id);
    if (user == null) {
      throw error.notFound("Usuario no encontrado");
    }

    if (updateRequest.email() != null && !user.getEmail().equals(updateRequest.email()) && isEmailTaken(updateRequest.email())) {
      throw error.conflict("El correo ya está en uso");
    }

    user.merge(updateRequest);

    User updatedUser = userRepository.updateUser(user);
    return new UserResponse(updatedUser);
  }
}
