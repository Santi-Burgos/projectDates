package san.projectdates.infrastructure.http;

import san.projectdates.core.dtos.ApiResponse;
import san.projectdates.core.dtos.UserCreateRequest;
import san.projectdates.core.dtos.UserResponse;
import san.projectdates.core.services.UserService;
import io.javalin.http.Context;

import java.util.List;
import java.util.UUID;

public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  public void create(Context ctx) {
    UserCreateRequest data = ctx.bodyAsClass(UserCreateRequest.class);
    UserResponse savedUser = userService.createNewUser(
      data.username(),
      data.email(),
      data.password(),
      data.lastname(),
      data.role()
    );

    ctx.status(201).json(ApiResponse.success(savedUser, "User creado con éxito"));
  }

  public void findOne(Context ctx) {
    String email = ctx.queryParam("email");
    UserResponse userFinded = userService.findUserByEmail(email);
    ctx.status(200).json(ApiResponse.success(userFinded, "User obtenido con éxito"));
  }

  public void delete(Context ctx) {
    String idParam = ctx.pathParam("id");
    UUID id = UUID.fromString(idParam);
    String deleteMessage = userService.deleteUserById(id);
    
    ctx.status(200).json(ApiResponse.success(deleteMessage, "User eliminado con éxito"));
  }

  public void findAll(Context ctx){
    List<UserResponse> getAllUsers = userService.findAllUsers();
    ctx.status(200).json(ApiResponse.success(getAllUsers, "Users obtenidos con éxito"));
  }
}
