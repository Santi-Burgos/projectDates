package san.projectdates.infrastructure.security;

import io.javalin.security.RouteRole;

public record AppPermission(String name) implements RouteRole {
  public static final AppPermission CREATE_SLOT = new AppPermission("create_appointment");
  public static final AppPermission UPDATE_SLOT = new AppPermission("update_appointment");
  public static final AppPermission DELETE_SLOT = new AppPermission("delete_appointment");

  public static final AppPermission CREATE_CONCEPT = new AppPermission("create_concept");
  public static final AppPermission UPDATE_CONCEPT = new AppPermission("update_concept");
  public static final AppPermission DELETE_CONCEPT = new AppPermission("delete_concept");
}
