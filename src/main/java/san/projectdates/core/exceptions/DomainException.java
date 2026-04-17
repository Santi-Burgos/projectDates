package san.projectdates.core.exceptions;

public abstract class DomainException extends RuntimeException {
  private final int status;

  protected DomainException(String message, int status){
    super(message);
    this.status = status;
  }

  public int getStatus() {
    return status;
  }
}

