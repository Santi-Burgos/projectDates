package san.projectdates.core.dtos;

import java.util.UUID;

public record AppointmentRequest(
  UUID conceptId,
  String startAt,
  String endAt,
  String appointmentDay
){ }
