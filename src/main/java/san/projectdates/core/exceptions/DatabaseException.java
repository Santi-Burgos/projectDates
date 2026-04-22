package san.projectdates.core.exceptions;

public class DatabaseException extends InternalServerErrorException {
  public DatabaseException(String message) {
    super(message);
  }
}
