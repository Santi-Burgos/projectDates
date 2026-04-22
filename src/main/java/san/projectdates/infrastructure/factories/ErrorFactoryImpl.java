package san.projectdates.infrastructure.factories;

import san.projectdates.core.exceptions.*;
import san.projectdates.core.repositories.ErrorFactory;

public class ErrorFactoryImpl implements ErrorFactory{
  @Override
  public DomainException badRequest(String message) {
    return new BadRequestException(message);
  }

  @Override
  public DomainException internalServerError(String message) {
    return new InternalServerErrorException(message);
  }

  @Override
  public DomainException forbbiden(String message) {
    return new ForbiddenException(message);
  }

  @Override
  public DomainException unauthorized(String message) {
    return new UnauthorizedException(message);
  }

  @Override
  public DomainException notFound(String message){
    return new EntityNotFoundException(message);
  }

  @Override
  public DomainException conflict(String message) {
    return new ConflictException(message);
  }

  @Override
  public DomainException databaseError(String message) {
    return new DatabaseException(message);
  }
}

