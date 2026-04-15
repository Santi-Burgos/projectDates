package san.projectdates.core.services;

import java.util.List;
import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;
import san.projectdates.core.config.RedisPool;
import san.projectdates.core.dtos.AppointmentRequest;
import san.projectdates.core.dtos.AppointmentResponse;
import san.projectdates.core.entities.Appointment;
import san.projectdates.core.entities.Concept;
import san.projectdates.core.entities.TimeRange;
import san.projectdates.core.repositories.AppointmentRepository;
import san.projectdates.core.repositories.ConceptRepository; 

public class AppointmentService {
  private final AppointmentRepository appointmentRepository;
  private final ConceptRepository conceptRepository;


  public AppointmentService(
    AppointmentRepository appointmentRepository,
    ConceptRepository conceptRepository
  ){
    this.appointmentRepository = appointmentRepository;
    this.conceptRepository = conceptRepository;
  }

  public AppointmentResponse createReservation(AppointmentRequest appointmentData, UUID userId){
    String lockKey = "lock:reservation:" + appointmentData.conceptId() + ":" + appointmentData.startAt() + ":" + appointmentData.endAt();
    String lockValue = UUID.randomUUID().toString();
    int expireTimeSeconds = 5;
    
    try(Jedis jedis = RedisPool.getResource()){
      String result = jedis.set(lockKey, lockValue, new SetParams().nx().ex(expireTimeSeconds));

      if(!"OK".equals(result)){
        throw new RuntimeException("Error al obtener el lock");
      }

      Appointment appointmentFull = new Appointment(
        appointmentData.conceptId(),
        userId,
        appointmentData.startAt(),
        appointmentData.endAt(),
        appointmentData.appointmentDay()
      );

      try{
        if(this.validateReservation(appointmentFull)){
          appointmentRepository.saveReservation(appointmentFull);

          return new AppointmentResponse(appointmentFull);
        }else{
          throw new RuntimeException("Ya hay una reserva con estas caracteristicas");
        }
      }finally{
        jedis.del(lockKey);
      }
    }catch(Exception e){
      throw new RuntimeException("Error de redis:  " + e.getMessage());
    }
  }

  public boolean validateReservation(Appointment appointmentData){
    Concept conceptDetails = conceptRepository.findConceptById(appointmentData.getConceptId());
    if(!conceptDetails.getIs24h()){
      if(!fnValidateHours(conceptDetails, appointmentData)){
        return false;
      };
    }

    Appointment hasReservation = appointmentRepository.findReservationByDate(appointmentData);
    //falta una validacion para saber si no inicia pero si un bloque mio coincide con este

    if(hasReservation == null){
      return true; 
    }
    return false;
  }

  static Boolean fnValidateHours(Concept concept, Appointment appointmentRequest){
    List<TimeRange> disponivelHours = concept.getSchedule();
    for(TimeRange range : disponivelHours){
      if(
        !range.openAsLocalTime().isAfter(appointmentRequest.startAtAsLocalTime()) && 
        !range.closeAsLocalTime().isBefore(appointmentRequest.endAtAsLocalTime())
      ){
        return true;
      }
    }
    return false;
  }
}
