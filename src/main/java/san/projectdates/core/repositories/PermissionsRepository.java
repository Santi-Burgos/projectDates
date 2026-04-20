package san.projectdates.core.repositories;

import san.projectdates.core.entities.Permissions;
import java.util.List;

public interface PermissionsRepository {
  List<Permissions> findAllPermissionsByRole(int role);
}
