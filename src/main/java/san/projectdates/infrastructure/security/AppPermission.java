package san.projectdates.infrastructure.security;

import io.javalin.security.RouteRole;

public record AppPermission(String name) implements RouteRole {
  public static final AppPermission CREATE_SLOT = new AppPermission("create_slot");
  public static final AppPermission CREATE_CONCEPT = new AppPermission("create_concept");
}
