package san.projectdates.core.dtos;

import java.util.UUID;
import san.projectdates.core.entities.Role;

public record AuthResponse(
  UUID id,
  String username,
  String email,
  Role role,
  String access_token
){

}
