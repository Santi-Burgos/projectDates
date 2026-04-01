package san.projectdates.infrastructure.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.SQLException;

public class DbConfig {
  private static HikariDataSource dataSource;

  static{
    Dotenv dotenv = Dotenv.load();
    HikariConfig config = new HikariConfig();

    config.setJdbcUrl(dotenv.get("DB_URL"));
    config.setUsername(dotenv.get("DB_USER"));
    config.setPassword(dotenv.get("DB_PASS"));

    int maxPool = Integer.parseInt(dotenv.get("DB_MAX_POOL", "10"));
    config.setMaximumPoolSize(maxPool);

    dataSource = new HikariDataSource(config);
  }

  public static HikariDataSource getDataSource(){
    return dataSource;
  }

  public static Connection getConnection() throws SQLException{
    return dataSource.getConnection();
  }
}
