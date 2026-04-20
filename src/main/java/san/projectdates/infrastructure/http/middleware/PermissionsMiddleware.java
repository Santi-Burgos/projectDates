package san.projectdates.infrastructure.http.middleware;

import java.util.List;

import san.projectdates.core.entities.Permissions;
import san.projectdates.core.services.PermissionsService;

public class PermissionsMiddleware {
  private final PermissionsService permissionService;

  public PermissionsMiddleware(PermissionsService permissionsService){
    this.permissionService = permissionsService;
  }

  Boolean validatePermission(int role, String requeriedPermission){
    List<Permissions> allPermissions = permissionService.getPermissionsByRole(role); 
    return allPermissions.stream()
      .anyMatch(
        permission -> permission.getPermissionName().equals(requeriedPermission)
      );
  }
}
