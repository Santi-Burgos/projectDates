package san.projectdates.infrastructure.http;

import san.projectdates.core.services.UserService;
import san.projectdates.infrastructure.http.dtos.UserCreateRequest;
import san.projectdates.infrastructure.http.dtos.UserResponse;
import io.javalin.http.Context;

public class UserController {
  private final UserService userService;

  public UserController(UserService userService){
    this.userService = userService;
  }

  public void create(Context ctx){
    try{
      UserCreateRequest data = ctx.bodyAsClass(UserCreateRequest.class);
      UserResponse savedUser = userService.createNewUser(
          data.username(),
          data.email(),
          data.password(),
          data.lastname(),
          data.role()
      );

      ctx.status(201).json(savedUser);
    }catch(Exception e){
      ctx.status(400).result(e.getMessage());
    }
  }
}
