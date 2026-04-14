package san.projectdates.core.repositories;

import san.projectdates.core.entities.Appointment;
import san.projectdates.core.dtos.AppointmentRequest;

public interface AppoimentRepository {
  Appointment saveReservation();


  Appointment findReservationByDate(AppointmentRequest appointmentData);
}
