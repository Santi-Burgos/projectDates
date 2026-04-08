package san.projectdates.infrastructure.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import san.projectdates.core.entities.User;
import san.projectdates.core.repositories.UserRepository;
import san.projectdates.core.entities.Role;

public class SqlUserRepository implements UserRepository {

  @Override
  public User saveUser(User user){
    String querySaveUser = """
    INSERT INTO users(user_id, email, password, username, lastname, birthday, rol_id, created_at)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    RETURNING *
    """;

    try(
      Connection conn = DbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(querySaveUser);
    ){
      pstmt.setObject(1, user.getId());
      pstmt.setString(2, user.getEmail());
      pstmt.setString(3, user.getPassword());
      pstmt.setString(4, user.getUsername());
      pstmt.setString(5, user.getLastname());
      pstmt.setObject(6, user.getBirthday());
      pstmt.setInt(7, user.getRole().getValue());
      pstmt.setObject(8, user.getCreatedAt());

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return new User(
          rs.getObject("user_id", UUID.class),
          rs.getString("username"),
          rs.getString("email"),
          rs.getString("password"),
          rs.getString("lastname"),
          Role.fromValue(rs.getInt("rol_id")),       
          rs.getObject("birthday", LocalDate.class),
          rs.getObject("created_at", OffsetDateTime.class)
        );
      }
      return null;
    }catch(Exception e){
      throw new RuntimeException("Error al guardar en la base de datos: " + e.getMessage());
    }
  }
  
  @Override
  public Boolean emailIsAlreadyUse(String email){
    String queryFindEmail = """
      SELECT EXISTS
        (SELECT 1 FROM users WHERE email = ?)
    """;
    try(
      Connection conn = DbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(queryFindEmail);
    ){
      pstmt.setString(1, email);
      ResultSet rs = pstmt.executeQuery();

      if(rs.next()){
        return rs.getBoolean(1);
      }
      return false;
    }catch(SQLException e){
      throw new RuntimeException("Error al buscar en la base de datos: " + e.getMessage());
    }
  }

  @Override
  public Boolean validateUserExist(UUID id){
    String queryValidateUser = """
      SELECT EXISTS
        (SELECT 1 FROM users WHERE user_id = ?)
      """;
    try(
      Connection conn = DbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(queryValidateUser);  
    ){
      pstmt.setObject(1, id);
      ResultSet rs = pstmt.executeQuery();
      if(rs.next()){
        return rs.getBoolean(1);
      }
      return false;
    }catch(SQLException e){
      throw new RuntimeException("Error al buscar en la base de datos");
    }
  }


  @Override
  public int deleteUserById(UUID id){
    String queryDeleteUser = """
      DELETE FROM users
      WHERE user_id = ?  
    """;
    try(
      Connection conn = DbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(queryDeleteUser);
    ){
      pstmt.setObject(1, id);
      
      int affectedRows = pstmt.executeUpdate();

      return affectedRows;
    }catch(SQLException e){
      throw new RuntimeException("Error al buscar en la base de datos: " + e.getMessage());
    }
  }

  @Override
  public User getUserByEmail(String email){
    String queryFindUserByEmail = """
      SELECT * FROM users 
      WHERE email = ?
    """;
    try(
      Connection conn = DbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(queryFindUserByEmail);
    ){
      pstmt.setString(1, email);
      ResultSet rs = pstmt.executeQuery();
      if(rs.next()){
        return new User(
          rs.getObject("user_id", UUID.class),
          rs.getString("username"),
          rs.getString("email"),
          rs.getString("password"),
          rs.getString("lastname"),
          Role.fromValue(rs.getInt("rol_id")),       
          rs.getObject("birthday", LocalDate.class),
          rs.getObject("created_at", OffsetDateTime.class)
        );
      }
    return null;
    }catch(SQLException e){
      throw new RuntimeException("Error al buscar en la base de datos: " + e.getMessage());
    }
  }


  public List<User> findAllUsers(){
    String querySelectAllUser = """
      SELECT * FROM users
    """;
    List<User> users = new ArrayList<>();

    try(
      Connection conn = DbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(querySelectAllUser);
      ResultSet rs = pstmt.executeQuery();
    ){
      while (rs.next()) {
        UUID id = rs.getObject("user_id", UUID.class);
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        String lastname = rs.getString("lastname"); 
        Role role = Role.fromValue(rs.getInt("rol_id")); 
        LocalDate birthday = rs.getObject("birthday", LocalDate.class);
        OffsetDateTime createdAt = rs.getObject("created_at", OffsetDateTime.class);
        User user = new User(id, username, email, password, lastname, role, birthday, createdAt);
        
        users.add(user);
      }
      
      return users;
    } catch (SQLException e) {
      throw new RuntimeException("Error al buscar los usuarios en la base de datos: " + e.getMessage());
    }
  }
}
