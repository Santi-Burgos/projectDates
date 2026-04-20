package san.projectdates.core.entities;

public class Permissions {
  private Number id;
  private String permissionName;

  public Permissions(){}

  public Permissions(Number id, String permissionName){
    this.id = id;
    this.permissionName = permissionName;
  }

  public Number getId() {
    return id;
  }

  public void setId(Number id) {
    this.id = id;
  }

  public String getPermissionName() {
    return permissionName;
  }

  public void setPermissionName(String permissionName) {
    this.permissionName = permissionName;
  }
}
