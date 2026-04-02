package san.projectdates.core.repositories;

import san.projectdates.core.entities.User;
import java.util.UUID;

public interface UserRepository {
  User saveUser(User newUser);

  Boolean emailIsAlreadyUse(String email);
  
  Boolean validateUserExist(UUID id);

  int deleteUserById(UUID id);

  User getUserByEmail(String email);
}
