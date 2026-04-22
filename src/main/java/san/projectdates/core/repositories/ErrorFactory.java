package san.projectdates.core.repositories;

import san.projectdates.core.exceptions.DomainException;

public interface ErrorFactory {
  DomainException badRequest(String message);
  
  DomainException internalServerError(String message);

  DomainException forbbiden(String message);

  DomainException unauthorized(String message);

  DomainException notFound(String message);

  DomainException conflict(String message);

  DomainException databaseError(String message);
}