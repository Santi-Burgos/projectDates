package san.projectdates.infrastructure.http.middleware;

import io.javalin.http.Context;
import san.projectdates.core.dtos.ApiResponse;
import san.projectdates.core.exceptions.DomainException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  public static void handleDomainException(@NotNull DomainException e, @NotNull Context ctx) {
    logger.error("Domain Exception: {} - Status: {}", e.getMessage(), e.getStatus());
    ctx.status(e.getStatus()).json(ApiResponse.error(e.getMessage()));
  }

  public static void handleGeneralException(Exception e, Context ctx) {
    logger.error("Unexpected Error: ", e);
    ctx.status(500).json(ApiResponse.error("Ha ocurrido un error inesperado interno"));
  }
}
