package san.projectdates.infrastructure.http.middleware;

import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import io.jsonwebtoken.Claims;
import san.projectdates.core.entities.User;
import san.projectdates.core.services.UserService;
import san.projectdates.infrastructure.security.JwtService;

public class JwtAuthenticationFilter {
  private final UserService userService;
  private final JwtService jwtService;

  public JwtAuthenticationFilter(UserService userService, JwtService jwtService) {
    this.userService = userService;
    this.jwtService = jwtService;
  }

  public void handleValidateToken(Context ctx) {
    String path = ctx.path();
    String method = ctx.method().name();
    boolean isLogin = path.equals("/api/auth") && method.equals("POST");
    boolean isRegister = path.equals("/api/users/register") && method.equals("POST");

    if (isLogin || isRegister) {
      return;
    }

    try {
      String token = getToken(ctx);
      Claims claims = validateToken(token);
      User user = validateUser(claims);
      setCurrentUser(ctx, user);
    } catch (Exception e) {
      throw new UnauthorizedResponse("No autorizado: " + e.getMessage());
    }
  }

  private String getToken(Context ctx) {
    String header = ctx.header("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      throw new RuntimeException("Token no proporcionado");
    }
    return header.substring(7);
  }

  private Claims validateToken(String token) {
    return jwtService.parseToken(token);
  }

  private User validateUser(Claims claims) {
    String email = claims.getSubject();
    return userService.findEntityByEmail(email);
  }

  private void setCurrentUser(Context ctx, User user) {
    ctx.attribute("currentUser", user);
  }
}
