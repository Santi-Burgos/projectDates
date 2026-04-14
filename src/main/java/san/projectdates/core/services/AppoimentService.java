package san.projectdates.core.services;

import java.util.List;
import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;
import san.projectdates.core.config.RedisPool;
import san.projectdates.core.dtos.AppointmentRequest;
import san.projectdates.core.entities.Appointment;
import san.projectdates.core.entities.Concept;
import san.projectdates.core.entities.TimeRange;
import san.projectdates.core.repositories.AppoimentRepository;
import san.projectdates.core.repositories.ConceptRepository; 

public class AppoimentService {
  private final AppoimentRepository appoimentRepository;
  private final ConceptRepository conceptRepository;


  public AppoimentService(
    AppoimentRepository appoimentRepository,
    ConceptRepository conceptRepository
  ){
    this.appoimentRepository = appoimentRepository;
    this.conceptRepository = conceptRepository;
  }

  public void createReservation(AppointmentRequest appoimentData){
    String lockKey = "lock:reservation:" + appoimentData.conceptId() + ":" + appoimentData.start_at() + ":" + appoimentData.end_at();
    String lockValue = UUID.randomUUID().toString();
    int expireTimeSeconds = 5;
    
    try(Jedis jedis = RedisPool.getResource()){
      String result = jedis.set(lockKey, lockValue, new SetParams().nx().ex(expireTimeSeconds));
      
      if(!"OK".equals(result)){
        throw new RuntimeException("Error al obtener el lock");
      }

      try{
        if(this.validateReservation(appoimentData)){
          appoimentRepository.saveReservation();
        }
      }finally{
        jedis.del(lockKey);
      }
    }catch(Exception e){
      throw new RuntimeException("Error de redis:  " + e.getMessage());
    }
  }

  public boolean validateReservation(AppointmentRequest appointmentData){
    Concept conceptDetails = conceptRepository.findConceptById(appointmentData.conceptId());
    
    if(!conceptDetails.getIs24h()){
      if(!fnValidateHours(conceptDetails, appointmentData)){
        return false;
      };
    }
    
    Appointment hasReservation = appoimentRepository.findReservationByDate(appointmentData);
    if(hasReservation == null){
      return true; 
    }

    return false;
  }

  static Boolean fnValidateHours(Concept concept, AppointmentRequest appointmentRequest){
    List<TimeRange> disponivelHours = concept.getSchedule();
    for(TimeRange range : disponivelHours){
      if(!range.openAsLocalTime().isAfter(appointmentRequest.start_at()) && !range.closeAsLocalTime().isBefore(appointmentRequest.end_at())){
        return true;
      }
    }
    return false;
  }
}
