package san.projectdates.infrastructure.http;

import san.projectdates.core.dtos.ApiResponse;
import san.projectdates.core.dtos.AuthRequest;
import san.projectdates.core.dtos.AuthResponse;
import san.projectdates.core.services.AuthService;
import io.javalin.http.Context;

public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService){
    this.authService = authService;
  }

  public void login(Context ctx){
    AuthRequest authRequest= ctx.bodyAsClass(AuthRequest.class); 
    AuthResponse authResponse = authService.login(authRequest);
    ctx.status(200).json(ApiResponse.success(authResponse, "Login exitoso"));
  }
}
