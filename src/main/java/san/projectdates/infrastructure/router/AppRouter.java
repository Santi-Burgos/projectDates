package san.projectdates.infrastructure.router;

import san.projectdates.core.services.AppointmentService;
import san.projectdates.core.services.AuthService;
import san.projectdates.core.services.ConceptService;
import san.projectdates.core.services.UserService;
import san.projectdates.infrastructure.http.AppointmentController;
import san.projectdates.infrastructure.http.AuthController;
import san.projectdates.infrastructure.http.ConceptController;
import san.projectdates.infrastructure.http.UserController;
import static io.javalin.apibuilder.ApiBuilder.*;
import san.projectdates.infrastructure.http.middleware.JwtAuthenticationFilter;
import san.projectdates.infrastructure.security.JwtService;
import io.javalin.config.JavalinConfig;
import san.projectdates.infrastructure.security.AppPermission;
import san.projectdates.infrastructure.http.middleware.PermissionsMiddleware;
import san.projectdates.infrastructure.http.middleware.AccessManagerImpl;

public class AppRouter {
  private final UserController userController;
  private final AuthController authController;
  private final ConceptController conceptController;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AppointmentController appointmentController;
  private final PermissionsMiddleware permissionsMiddleware;

  public AppRouter(
      UserService userService,
      AuthService authService,
      ConceptService conceptService,
      JwtService jwtService,
      AppointmentService appointmentService,
      PermissionsMiddleware permissionsMiddleware) {
    this.userController = new UserController(userService);
    this.authController = new AuthController(authService);
    this.conceptController = new ConceptController(conceptService);
    this.jwtAuthenticationFilter = new JwtAuthenticationFilter(userService, jwtService);
    this.appointmentController = new AppointmentController(appointmentService);
    this.permissionsMiddleware = permissionsMiddleware;
  }

  public void registerRoutes(JavalinConfig config) {
    config.routes.apiBuilder(() -> {
      before("/api/*", jwtAuthenticationFilter::handleValidateToken);
      beforeMatched(new AccessManagerImpl(permissionsMiddleware)::handle);

      path("/api/users", () -> {
        post("/register", userController::create);
        delete("/{id}", userController::delete);
        get("/{email}", userController::findOne);
        get("/", userController::findAll);
        patch("/{id}", userController::update, AppPermission.UPDATE_USER);
      });

      path("/api/auth", () -> {
        post(authController::login);
      });

      path("/api/concept", () -> {
        post(conceptController::createConcept, AppPermission.CREATE_CONCEPT);
        get("/{id}", conceptController::findOneConcept);
        get(conceptController::findActiveConcept);
        patch("/{id}", conceptController::updateConcept, AppPermission.UPDATE_CONCEPT);
        delete("/{id}", conceptController::deleteConcept, AppPermission.DELETE_CONCEPT);
      });

      path("/api/reservation", () -> {
        post(appointmentController::createReservation, AppPermission.CREATE_SLOT);
        get(appointmentController::getAllAppointments);
      });
    });
  }
}
