package san.projectdates.core.dtos;

import java.util.UUID;
import san.projectdates.core.entities.Appointment;

public record AppointmentResponse(
  UUID appointmentId, 
  UUID conceptId,
  UUID userId, 
  String startAt,
  String endAt,
  String createdAt,
  String appointmentDay
){
  public AppointmentResponse(Appointment data){
    this(
      data.getId(),
      data.getConceptId(),
      data.getUserId(),
      data.getStartAt(),
      data.getEndAt(),
      data.getCreatedAt() != null ? data.getCreatedAt().toString() : null,
      data.getAppointmentDay()
    );
  }
}