package san.projectdates.core.dtos;

import java.time.LocalTime;
import java.util.UUID;

public record AppointmentRequest(
  UUID conceptId,
  UUID userId,
  LocalTime start_at,
  LocalTime end_at
){}
