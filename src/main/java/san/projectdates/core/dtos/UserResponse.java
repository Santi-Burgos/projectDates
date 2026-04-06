package san.projectdates.core.dtos;

import java.util.UUID;
import san.projectdates.core.entities.Role;
import san.projectdates.core.entities.User;

public record UserResponse (
  UUID id,
  String username,
  String lastname,
  String email,
  Role role
){
  public UserResponse(User user){
    this(
      user.getId(),
      user.getUsername(),
      user.getLastname(),
      user.getEmail(),
      user.getRole()
    );
  }
}
