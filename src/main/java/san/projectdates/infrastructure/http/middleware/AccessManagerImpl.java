package san.projectdates.infrastructure.http.middleware;

import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.security.RouteRole;
import san.projectdates.core.entities.User;
import san.projectdates.infrastructure.security.AppPermission;

import java.util.Set;

public class AccessManagerImpl {

  private final PermissionsMiddleware permissionsMiddleware;

  public AccessManagerImpl(PermissionsMiddleware permissionsMiddleware) {
    this.permissionsMiddleware = permissionsMiddleware;
  }

  public void handle(Context ctx) throws Exception {
    Set<RouteRole> routeRoles = ctx.routeRoles();

    if (routeRoles.isEmpty()) {
      return;
    }

    User user = ctx.attribute("currentUser");
    if (user == null) {
      throw new ForbiddenResponse("No autenticado");
    }

    int roleId = user.getRole().getValue();

    for (RouteRole role : routeRoles) {
      if (role instanceof AppPermission permission) {
        if (permissionsMiddleware.validatePermission(roleId, permission.name())) {
          return;
        }
      }
    }

    throw new ForbiddenResponse("No tienes permisos suficientes para esta acción");
  }
}
