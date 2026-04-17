package san.projectdates.core.exceptions;

public class ConflictException extends DomainException {
  public ConflictException(String message) {
    super(message, 409);
  }
}
