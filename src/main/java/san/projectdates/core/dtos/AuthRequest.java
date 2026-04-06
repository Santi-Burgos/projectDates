package san.projectdates.core.dtos;

public record AuthRequest(
  String email,
  String password
){
  public AuthRequest{
    if(email == null | email.isBlank() | password == null | password.isBlank()){
      throw new IllegalArgumentException("El email o el password son obligatorios");
    }
  }
}
