package san.projectdates.infrastructure.http.dtos;

import java.util.UUID;
import san.projectdates.core.entities.Role;

public record UserResponse (
  UUID id,
  String username,
  String lastname,
  String email,
  Role role
){}
