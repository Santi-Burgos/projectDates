package san.projectdates.core.dtos;

import san.projectdates.core.entities.Role;

public record UserCreateRequest(
  String username,
  String email,
  String password,
  String lastname,
  Role role
) {}
