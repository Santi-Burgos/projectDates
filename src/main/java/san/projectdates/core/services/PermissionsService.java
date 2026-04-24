package san.projectdates.core.services;

import san.projectdates.core.entities.Permissions;
import san.projectdates.core.repositories.PermissionsRepository;

import java.util.List;

public class PermissionsService {
  private final PermissionsRepository permissionsRepository;

  public PermissionsService(PermissionsRepository permissionsRepository){
    this.permissionsRepository = permissionsRepository;
  }

  public List<Permissions> getPermissionsByRole(int role){
    List<Permissions> allPermissionsByRole = permissionsRepository.findAllPermissionsByRole(role);
    return allPermissionsByRole;
  }
}
