package san.projectdates.core.exceptions;

public class EntityNotFoundException extends DomainException{
  public EntityNotFoundException(String message){ 
    super(message, 404);
  }
}
