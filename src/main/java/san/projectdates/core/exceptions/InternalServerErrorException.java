package san.projectdates.core.exceptions;

public class InternalServerErrorException extends DomainException {
  public InternalServerErrorException(String message) {
    super(message, 500);
  }
}
