package san.projectdates.infrastructure.http;

import java.util.UUID;

import io.javalin.http.Context;

import san.projectdates.core.dtos.AppointmentRequest;
import san.projectdates.core.dtos.AppointmentResponse;
import san.projectdates.core.services.AppointmentService;

public class AppointmentController {
  private final AppointmentService appointmentService; 

  public AppointmentController(
    AppointmentService appointmentService
  ){  
    this.appointmentService = appointmentService;
  }

  public void createReservation(Context ctx){
    try{
      AppointmentRequest appointmenReq = ctx.bodyAsClass(AppointmentRequest.class);
      UUID userId = ctx.attribute("currentUserId");
      AppointmentResponse appointmentRes = appointmentService.createReservation(appointmenReq, userId);
        
      ctx.status(200).json(appointmentRes);
    }catch(Exception e){
      ctx.status(500).result(e.getMessage());
    }
  }
}
