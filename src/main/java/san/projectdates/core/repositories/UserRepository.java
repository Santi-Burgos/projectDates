package san.projectdates.core.repositories;

import san.projectdates.core.entities.User;

public interface UserRepository {
  User saveUser(User newUser);

  void getUserByEmail(String email);
}
