package san.projectdates.core.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class Appointment {
  UUID id;
  UUID conceptId;
  UUID userId;
  String startAt;
  String endAt;
  LocalDateTime createdAt;
  String appointmentDay;

  public Appointment(){ }

  public Appointment(UUID conceptId, UUID userId, String startAt, String endAt, String appointmentDay){
    this.setId();
    this.setConceptId(conceptId);
    this.setUserId(userId);
    this.setStartAt(startAt);
    this.setEndAt(endAt);
    this.setCreatedAt();
    this.setAppointmentDay(appointmentDay);
  }

  public Appointment(UUID id, UUID conceptId, UUID userId, String startAt, String endAt, LocalDateTime createdAt, String appointmentDay){
    this.id = id;
    this.conceptId = conceptId;
    this.userId = userId;
    this.startAt = startAt;
    this.endAt = endAt;
    this.createdAt = createdAt;
    this.appointmentDay = appointmentDay;
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

  public void setConceptId(UUID conceptId){
    if(conceptId == null){
      throw new RuntimeException("ConnceptId debe ser un Id y no estas vacío");
    }
    this.conceptId = conceptId;
  }

  public UUID getUserId(){
    return userId;
  }

  public void setUserId(UUID userId){
    if(userId == null){
      throw new RuntimeException("ConnceptId debe ser un Id y no estas vacío");
    }
    this.userId = userId;
  }

  public String getStartAt(){
    return startAt;
  }
  
  public void setStartAt(String startAt){
    if(startAt == null){
      throw new RuntimeException("Necesitas una fecha de comienzo");
    }
    this.startAt = startAt;
  }

  public String getEndAt(){
    return endAt;
  }
  
  public void setEndAt(String endAt){
    if(endAt == null){
      throw new RuntimeException("Necesitas una fecha de comienzo");
    }
    this.endAt = endAt;
  }

  public LocalTime startAtAsLocalTime(){
    return LocalTime.parse(startAt);
  }

  public LocalTime endAtAsLocalTime(){
    return LocalTime.parse(endAt);
  }

  public LocalDateTime getCreatedAt(){
    return createdAt;
  }

  public void setCreatedAt(){
    LocalDateTime createdAt = LocalDateTime.now();
    this.createdAt = createdAt;
  }

  public String getAppointmentDay(){
    return appointmentDay;
  }

  public void setAppointmentDay(String appointmentDay){
    if(appointmentDay == null){
      throw new RuntimeException("Necesita un fecha");
    }

    this.appointmentDay = appointmentDay;
  }

  public LocalDate appoinmentDayAsLocalDate(){
    return LocalDate.parse(appointmentDay);
  }
}
