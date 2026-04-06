package san.projectdates.infrastructure.router;

import san.projectdates.core.services.AuthService;
import san.projectdates.core.services.ConceptService;
import san.projectdates.core.services.UserService;
import san.projectdates.infrastructure.http.AuthController;
import san.projectdates.infrastructure.http.ConceptController;
import san.projectdates.infrastructure.http.UserController;
import static io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.config.JavalinConfig;

public class AppRouter {
  private final UserController userController;
  private final AuthController authController;
  private final ConceptController conceptController;

  public AppRouter(UserService userService, AuthService authService, ConceptService conceptService) {
    this.userController = new UserController(userService);
    this.authController = new AuthController(authService);
    this.conceptController = new ConceptController(conceptService);
  }

  public void registerRoutes(JavalinConfig config) {
    config.routes.apiBuilder(() -> {
      path("/api/users", () -> {
        post(userController::create);
        delete("/{id}", userController::delete);
        get("/{email}", userController::findOne);
        get("/", userController::findAll);
      });

      path("/api/auth", () ->{
        post(authController::login);
      });

      path("/api/concept", () ->{
        post(conceptController::createConcept);
      });
    });
  }
}
