package san.projectdates.core.exceptions;

public class BadRequestException extends DomainException {
  public BadRequestException(String message) {
    super(message, 400);
  }
}
