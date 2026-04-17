package san.projectdates;

import org.flywaydb.core.Flyway;

import io.javalin.Javalin;
import san.projectdates.core.repositories.AppointmentRepository;
import san.projectdates.core.repositories.ConceptRepository;
import san.projectdates.core.repositories.UserRepository;
import san.projectdates.core.services.UserService;
import san.projectdates.infrastructure.persistence.DbConfig;
import san.projectdates.infrastructure.persistence.SqlAppointmentRepository;
import san.projectdates.infrastructure.persistence.SqlConceptRepository;
import san.projectdates.infrastructure.persistence.SqlUserRepository;
import san.projectdates.infrastructure.router.AppRouter;
import san.projectdates.core.services.AppointmentService;
import san.projectdates.core.services.AuthService;
import san.projectdates.core.services.ConceptService;
import san.projectdates.infrastructure.security.JwtService;
import san.projectdates.core.exceptions.DomainException;
import san.projectdates.infrastructure.factories.ErrorFactoryImpl;
import san.projectdates.infrastructure.http.middleware.GlobalExceptionHandler;

public class Main {
  public static void main(String[] args) {
    Flyway flyway = Flyway.configure()
        .dataSource(DbConfig.getDataSource())
        .load();

    System.out.println("Migrando database...");
    flyway.migrate();
    System.out.println("Migracion concluida");

    UserRepository repo = new SqlUserRepository();
    ConceptRepository conceptRepo = new SqlConceptRepository();
    AppointmentRepository appointmentRepo = new SqlAppointmentRepository();
    ErrorFactoryImpl errorFactory = new ErrorFactoryImpl();

    JwtService jwtService = new JwtService();
    ConceptService conceptService = new ConceptService(conceptRepo);
    UserService userService = new UserService(repo, errorFactory);
    AuthService authService = new AuthService(userService, jwtService);
    AppointmentService appointmentService = new AppointmentService(appointmentRepo, conceptRepo, errorFactory);

    AppRouter router = new AppRouter(
      userService, 
      authService, 
      conceptService, 
      jwtService,
      appointmentService
    );

    var app = Javalin.create(config -> {
      config.bundledPlugins.enableDevLogging();
      config.routes.get("/", ctx -> ctx.result("Hello World"));
      router.registerRoutes(config);

      config.routes.exception(DomainException.class, GlobalExceptionHandler::handleDomainException);
      config.routes.exception(Exception.class, GlobalExceptionHandler::handleGeneralException);
    });

    app.start(8000);
    System.out.println("Servidor corriendo en http://localhost:8000");
  }
}