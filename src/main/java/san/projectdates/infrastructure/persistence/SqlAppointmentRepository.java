package san.projectdates.infrastructure.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;

import san.projectdates.core.entities.Appointment;
import san.projectdates.core.repositories.AppointmentRepository;

public class SqlAppointmentRepository implements AppointmentRepository{
  
  @Override
  public Appointment saveReservation(Appointment saveAppointment){
    String querySaveReservation = """
      INSERT INTO appointments (appointments_id, user_id, concept_id, start_at, end_at, created_at, appointments_day)
      VALUES (?, ?, ?, ?, ?, ?, ?)
      RETURNING *
    """;
    try(
      Connection conn = DbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(querySaveReservation);
    ){
      pstmt.setObject(1, saveAppointment.getId());
      pstmt.setObject(2, saveAppointment.getUserId());
      pstmt.setObject(3, saveAppointment.getConceptId());
      pstmt.setObject(4, saveAppointment.startAtAsLocalTime());
      pstmt.setObject(5, saveAppointment.endAtAsLocalTime());
      pstmt.setObject(6, saveAppointment.getCreatedAt());
      pstmt.setObject(7, saveAppointment.appoinmentDayAsLocalDate());
      
      ResultSet rs = pstmt.executeQuery();

      if(rs.next()){
        return new Appointment(
          rs.getObject("appointments_id", UUID.class),
          rs.getObject("concept_id", UUID.class),
          rs.getObject("user_id", UUID.class),
          rs.getTime("start_at").toString(),
          rs.getTime("end_at").toString(),
          rs.getObject("created_at", OffsetDateTime.class).toLocalDateTime(),
          rs.getDate("appointments_day").toString()
        );
      }

      return null;
    }catch(SQLException e){
      throw new RuntimeException("Error al crear la resevar: " + e.getMessage());
    }
  }

  @Override 
  public Appointment findReservationByDate(Appointment appointmentData){
    String queryFindReservation = """
      SELECT * FROM appointments
      WHERE concept_id = ?
      AND start_at::time = ?
      AND appointments_day::date = ?
    """;

    try(
      Connection conn = DbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(queryFindReservation);
    ){
      pstmt.setObject(1, appointmentData.getConceptId());
      pstmt.setObject(2, appointmentData.startAtAsLocalTime());
      pstmt.setObject(3, appointmentData.appoinmentDayAsLocalDate());

      ResultSet rs = pstmt.executeQuery();
      if(rs.next()){
        return new Appointment(
          rs.getObject("appointments_id", UUID.class),
          rs.getObject("concept_id", UUID.class),
          rs.getObject("user_id", UUID.class),
          rs.getTime("start_at").toString(),
          rs.getTime("end_at").toString(),
          rs.getObject("created_at", OffsetDateTime.class).toLocalDateTime(),
          rs.getDate("appointments_day").toString()
        );
      }
      return null;
    }catch(SQLException e){
      throw new RuntimeException("Error al buscar la reserva por día: " + e.getMessage());
    }
  }

  @Override
  public void cancelReservation(UUID appointmentId){
  }
}
