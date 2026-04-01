package san.projectdates.infrastructure.router;

import san.projectdates.core.services.UserService;
import san.projectdates.infrastructure.http.UserController;
import static io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.config.JavalinConfig;

public class AppRouter {
  private final UserController userController;

  public AppRouter(UserService userService) {
    this.userController = new UserController(userService);
  }

  public void registerRoutes(JavalinConfig config) {
    config.routes.apiBuilder(() -> {
      path("/api/users", () -> {
        post(userController::create);
      });
    });
  }

}
