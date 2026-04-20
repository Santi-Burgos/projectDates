package san.projectdates.infrastructure.persistence;

import san.projectdates.core.entities.Permissions;
import san.projectdates.core.repositories.PermissionsRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlPermissionsRepository implements PermissionsRepository {

  @Override
  public List<Permissions> findAllPermissionsByRole(int role) {
    String query = """
          SELECT p.permission_id, p.permission_name
          FROM permission p
          JOIN role_permission rp ON p.permission_id = rp.permission_id
          WHERE rp.rol_id = ?
        """;
    List<Permissions> permissions = new ArrayList<>();
    try (
        Connection conn = DbConfig.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query);) {
      pstmt.setInt(1, role);
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          permissions.add(new Permissions(
              (Number) rs.getObject("permission_id"),
              rs.getString("permission_name")));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error al obtener los permisos del rol en la DB: " + e.getMessage());
    }
    return permissions;
  }
}
