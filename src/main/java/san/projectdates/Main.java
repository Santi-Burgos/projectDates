package san.projectdates;

import org.flywaydb.core.Flyway;

import io.javalin.Javalin;
import san.projectdates.core.repositories.UserRepository;
import san.projectdates.core.services.UserService;
import san.projectdates.infrastructure.persistence.DbConfig;
import san.projectdates.infrastructure.persistence.SqlUserRepository;
import san.projectdates.infrastructure.router.AppRouter;

public class Main {
  public static void main(String[] args) {
    Flyway flyway = Flyway.configure()
        .dataSource(DbConfig.getDataSource())
        .load();

    System.out.println("Migrando database...");
    flyway.migrate();
    System.out.println("Migracion concluida");

    UserRepository repo = new SqlUserRepository();
    UserService service = new UserService(repo);
    AppRouter router = new AppRouter(service);

    var app = Javalin.create(config -> {
      config.bundledPlugins.enableDevLogging();
      config.routes.get("/", ctx -> ctx.result("Hello World"));
      router.registerRoutes(config);
    });

    app.start(8000);
    System.out.println("Servidor corriendo en http://localhost:8000");
  }
}