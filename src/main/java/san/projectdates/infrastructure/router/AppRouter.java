package san.projectdates.infrastructure.router;

import san.projectdates.core.services.AuthService;
import san.projectdates.core.services.ConceptService;
import san.projectdates.core.services.UserService;
import san.projectdates.infrastructure.http.AuthController;
import san.projectdates.infrastructure.http.ConceptController;
import san.projectdates.infrastructure.http.UserController;
import static io.javalin.apibuilder.ApiBuilder.*;
import san.projectdates.infrastructure.http.middleware.JwtAuthenticationFilter;
import san.projectdates.infrastructure.security.JwtService;
import io.javalin.config.JavalinConfig;

public class AppRouter {
  private final UserController userController;
  private final AuthController authController;
  private final ConceptController conceptController;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public AppRouter(UserService userService, AuthService authService, ConceptService conceptService,
      JwtService jwtService) {
    this.userController = new UserController(userService);
    this.authController = new AuthController(authService);
    this.conceptController = new ConceptController(conceptService);
    this.jwtAuthenticationFilter = new JwtAuthenticationFilter(userService, jwtService);
  }

  public void registerRoutes(JavalinConfig config) {
    config.routes.apiBuilder(() -> {
      before("/api/*", jwtAuthenticationFilter::handleValidateToken);

      path("/api/users", () -> {
        post("/register", userController::create);
        delete("/{id}", userController::delete);
        get("/{email}", userController::findOne);
        get("/", userController::findAll);
      });

      path("/api/auth", () -> {
        post(authController::login);
      });

      path("/api/concept", () -> {
        post(conceptController::createConcept);
        get("/{id}", conceptController::findOneConcept);
        get(conceptController::findActiveConcept);
        delete("/{id}", conceptController::deleteConcept);
      });
    });
  }
}
