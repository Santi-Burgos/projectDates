package san.projectdates.infrastructure.http;

import san.projectdates.core.services.UserService;
import san.projectdates.infrastructure.http.dtos.UserCreateRequest;
import san.projectdates.infrastructure.http.dtos.UserResponse;
import io.javalin.http.Context;

import java.util.List;
import java.util.UUID;

public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  public void create(Context ctx) {
    try {
      UserCreateRequest data = ctx.bodyAsClass(UserCreateRequest.class);
      UserResponse savedUser = userService.createNewUser(
          data.username(),
          data.email(),
          data.password(),
          data.lastname(),
          data.role());

      ctx.status(201).json(savedUser);
    } catch (Exception e) {
      ctx.status(400).result(e.getMessage());
    }
  }

  public void findOne(Context ctx) {
    try {
      String email = ctx.queryParam("email");
      System.out.println(email);
      UserResponse userFinded = userService.findUserByEmail(email);
      ctx.status(200).json(userFinded);
    } catch (Exception e) {
      ctx.status(400).result(e.getMessage());
    }
  }

  public void delete(Context ctx) {
    try {
      String idParam = ctx.pathParam("id");
      UUID id = UUID.fromString(idParam);
      String deleteMessage = userService.deleteUserById(id);

      ctx.status(200).result(deleteMessage);
    } catch (Exception e) {
      ctx.status(400).result(e.getMessage());
    }
  }

  public void findAll(Context ctx){
    try {
      List<UserResponse> getAllUsers = userService.findAllUsers();
      ctx.status(200).json(getAllUsers);
    } catch (Exception e) {
      ctx.status(400).result(e.getMessage());
    }
  }
}
