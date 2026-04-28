package san.projectdates.core.repositories;

import san.projectdates.core.entities.Appointment;

import java.util.List;
import java.util.UUID;


public interface AppointmentRepository {
  Appointment saveReservation(Appointment saveAppointment);

  Appointment updateAppointment(Appointment updateAppointment);

  Appointment findReservationByDate(Appointment appointmentData);

  Appointment findOverlappingReservation(Appointment appointmentData);

  void cancelReservation(UUID appointmentId);

  List<Appointment>getAllAppointments();
}
