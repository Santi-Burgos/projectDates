package san.projectdates.core.dtos;

import java.util.UUID;
import san.projectdates.core.entities.Role;
import san.projectdates.core.entities.User;

public record AuthResponse(
  UUID id,
  String username,
  String email,
  Role role,
  String access_token
){

  public AuthResponse(User fullEntity, String access_token){
    this(
      fullEntity.getId(),
      fullEntity.getUsername(),
      fullEntity.getEmail(),
      fullEntity.getRole(),
      access_token
    );
  }
}
