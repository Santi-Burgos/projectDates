package san.projectdates.core.entities;

import java.time.LocalDate;
import java.util.UUID;

public class Appointment {
  UUID id;
  UUID conceptId;
  UUID userId;
  LocalDate startAt;
  LocalDate endAt;
  LocalDate createdAt;

  public Appointment(){ }

  public Appointment(UUID id, UUID conceptId, UUID userId, LocalDate startAt, LocalDate endAt, LocalDate createdAt){
    this.id = id;
    this.conceptId = conceptId;
    this.userId = userId;
    this.startAt = startAt;
    this.endAt = endAt;
    this.createdAt = createdAt;
  }

  public UUID getId(){
    return id;
  }

  public void setId(){
    this.id = UUID.randomUUID();
  }

  public UUID getConceptId(){
    return conceptId;
  }

  public void getConceptId(UUID conceptId){
    if(conceptId == null){
      throw new RuntimeException("ConnceptId debe ser un Id y no estas vacío");
    }
    this.conceptId = conceptId;
  }

  public UUID getUser(){
    return userId;
  }

  public void setUser(UUID userId){
    if(userId == null){
      throw new RuntimeException("ConnceptId debe ser un Id y no estas vacío");
    }
    this.userId = userId;
  }

  public LocalDate getStartAt(){
    return startAt;
  }
  
  public void setStartAt(LocalDate startAt){
    if(startAt == null){
      throw new RuntimeException("Necesitas una fecha de comienzo");
    }
    this.startAt = startAt;
  }

  public LocalDate getEndAt(){
    return endAt;
  }
  
  public void setEndAt(LocalDate endAt){
    if(endAt == null){
      throw new RuntimeException("Necesitas una fecha de comienzo");
    }
    this.endAt = endAt;
  }

  public void setCreatedAt(){
    LocalDate createdAt = LocalDate.now();
    this.createdAt = createdAt;
  }
}
