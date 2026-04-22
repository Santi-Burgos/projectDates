package san.projectdates.core.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import san.projectdates.core.dtos.AuthRequest;
import san.projectdates.core.dtos.AuthResponse;
import san.projectdates.core.entities.User;
import san.projectdates.core.repositories.ErrorFactory;
import san.projectdates.infrastructure.security.JwtService;

public class AuthService {
  private final UserService userService;
  private final JwtService jwtService;
  private final ErrorFactory errorFactory;

  public AuthService(
    UserService userService,
    JwtService jwtService,
    ErrorFactory errorFactory
  ){
    this.userService = userService;
    this.jwtService = jwtService;
    this.errorFactory = errorFactory;
  }

  public AuthResponse login(AuthRequest loginData){
    User fullEntity = userService.findEntityByEmail(loginData.email());
    
    BCrypt.Result isMatch = BCrypt.verifyer().verify(loginData.password().toCharArray(), fullEntity.getPassword());
    if(!isMatch.verified){
      throw errorFactory.unauthorized("El email o el password son invalidos");
    }

    String access_token = jwtService.createToken(fullEntity);

    return new AuthResponse(
      fullEntity,
      access_token
    );
  }

  public void logout(){}

  public void refreshToken(){}
}
