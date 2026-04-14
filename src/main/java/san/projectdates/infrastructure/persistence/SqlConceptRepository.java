package san.projectdates.infrastructure.persistence;

import san.projectdates.core.repositories.ConceptRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.sql.Time;

import san.projectdates.core.entities.Concept;
import san.projectdates.core.entities.TimeRange;

public class SqlConceptRepository implements ConceptRepository {

  @Override
  public Concept saveConcept(Concept concept) {
    if (concept.getIs24h()) {
      return this.executeSave24h(concept);
    } else {
      return this.executeSaveLimited(concept);
    }
  }

  public Concept executeSave24h(Concept newConcept) {
    String saveConcept = """
          INSERT INTO concept(concept_id, name, details, capacity, is_active, is_24h)
          VALUES(?, ?, ?, ?, ?, ?)
          RETURNING *
        """;

    try (
      Connection conn = DbConfig.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(saveConcept)
    ){
      pstmt.setObject(1, newConcept.getId());
      pstmt.setString(2, newConcept.getName());
      pstmt.setString(3, newConcept.getDetails());
      pstmt.setInt(4, newConcept.getCapacity());
      pstmt.setBoolean(5, newConcept.getIsActive());
      pstmt.setBoolean(6, newConcept.getIs24h());

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return new Concept(
          rs.getObject("concept_id", java.util.UUID.class),
          rs.getString("name"),
          rs.getString("details"),
          rs.getInt("capacity"),
          rs.getBoolean("is_active"),
          rs.getBoolean("is_24h")
        );
      }

      return null;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      throw new RuntimeException("Error al guardar el concepto: " + e.getMessage());
    }
  }

  public Concept executeSaveLimited(Concept newConcept) {

    String saveConcept = """
          INSERT INTO concept(concept_id, name, details, capacity, is_active, is_24h)
          VALUES(?, ?, ?, ?, ?, ?)
          RETURNING *
        """;
    String saveSchedule = """
          INSERT INTO slot(start_at, end_at, concept_id)
          VALUES(?, ?, ?)
        """;

    Connection conn = null;
    try {
      conn = DbConfig.getConnection();
      conn.setAutoCommit(false);

      try (PreparedStatement pstmt = conn.prepareStatement(saveConcept)) {
        pstmt.setObject(1, newConcept.getId());
        pstmt.setString(2, newConcept.getName());
        pstmt.setString(3, newConcept.getDetails());
        pstmt.setInt(4, newConcept.getCapacity());
        pstmt.setBoolean(5, newConcept.getIsActive());
        pstmt.setBoolean(6, newConcept.getIs24h());

        pstmt.executeQuery();
      }

      try (PreparedStatement pstmt = conn.prepareStatement(saveSchedule)) {
        for (TimeRange scheduleConcept : newConcept.getSchedule()) {
          pstmt.setObject(1, Time.valueOf(scheduleConcept.getOpenTime()));
          pstmt.setObject(2, Time.valueOf(scheduleConcept.getCloseTime()));
          pstmt.setObject(3, newConcept.getId());
          pstmt.addBatch();
        }
        pstmt.executeBatch();
      }

      conn.commit();
      return newConcept;
    } catch (SQLException e) {
      if (conn != null) {
        try {
          conn.rollback();
        } catch (SQLException ex) {
          System.out.println(ex.getMessage());
          throw new RuntimeException("Error al hacer rollback: " + ex.getMessage());
        }
      }
      System.out.println(e.getMessage());
      throw new RuntimeException("Error al guardar el concepto: " + e.getMessage());
    } finally {
      if (conn != null) {
        try {
          conn.setAutoCommit(true);
          conn.close();
        } catch (SQLException e) {
          System.out.println(e.getMessage());
          throw new RuntimeException("Error al cerrar conexión: " + e.getMessage());
        }
      }
    }
  }

  @Override
  public int deleteConcept(UUID conceptId) {
    String queryDeleteConcept = """
          DELELTE FROM concept
          WHERE concept_id = ?
        """;
    try (
        Connection conn = DbConfig.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(queryDeleteConcept);) {
      pstmt.setObject(1, conceptId);
      int affectedRows = pstmt.executeUpdate();
      return affectedRows;
    } catch (SQLException e) {
      throw new RuntimeException("Error al eliminar de la base de datos: " + e.getMessage());
    }
  }

  @Override
  public List<Concept> findAllActiveConcept() {
    String queryGetAllConcept = """
          SELECT
          c.concept_id,
          c.name,
          c.details,
          c.is_active,
          c.capacity,
          c.is_24h AS fullTime,
          s.slot_id,
          s.start_at,
          s.end_at
          FROM concept c
          LEFT JOIN slot s
            ON c.concept_id = s.concept_id
          WHERE c.is_active = true
        """;
    Map<UUID, Concept> conceptMap = new LinkedHashMap<>();
    Map<UUID, List<TimeRange>> tempSchedules = new HashMap<>();

    try (
        Connection conn = DbConfig.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(queryGetAllConcept);
        ResultSet rs = pstmt.executeQuery();) {
      while (rs.next()) {
        UUID conceptId = rs.getObject("concept_id", UUID.class);
        Concept concept = conceptMap.get(conceptId);

        if (concept == null) {
          concept = new Concept(
              conceptId,
              rs.getString("name"),
              rs.getString("details"),
              rs.getInt("capacity"),
              rs.getBoolean("is_active"),
              rs.getBoolean("fullTime"));

          conceptMap.put(conceptId, concept);
          tempSchedules.put(conceptId, new ArrayList<>());
        }

        String start = rs.getString("start_at");
        String end = rs.getString("end_at");

        if (start != null && end != null && concept.getIs24h() != true) {
          tempSchedules.get(conceptId).add(new TimeRange(start, end));
        }
      }

      for (UUID id : conceptMap.keySet()) {
        Concept concept = conceptMap.get(id);
        List<TimeRange> schedule = tempSchedules.get(id);

        concept.setSchedule(concept.getIs24h(), schedule);
      }

      return new ArrayList<>(conceptMap.values());
    } catch (SQLException e) {
      throw new RuntimeException("Error al obtener de la base de datos: " + e.getMessage());
    }
  }

  @Override
  public Concept findConceptById(UUID conceptId) {
    String queryFindConceptById = """
          SELECT
          c.concept_id,
          c.name,
          c.details,
          c.is_active,
          c.capacity,
          c.is_24h AS fullTime,
          s.slot_id,
          s.start_at,
          s.end_at
          FROM concept c
          LEFT JOIN slot s
          ON c.concept_id = s.concept_id
          WHERE c.concept_id = ?
        """;
    try (
        Connection conn = DbConfig.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(queryFindConceptById);) {
      pstmt.setObject(1, conceptId);
      ResultSet rs = pstmt.executeQuery();

      Concept concept = null;
      List<TimeRange> slots = new ArrayList<>();

      while (rs.next()) {
        if (concept == null) {
          concept = new Concept(
              rs.getObject("concept_id", UUID.class),
              rs.getString("name"),
              rs.getString("details"),
              rs.getInt("capacity"),
              rs.getBoolean("is_active"),
              rs.getBoolean("fullTime"));
        }

        String start = rs.getString("start_at");
        String end = rs.getString("end_at");

        if (start != null && end != null) {
          slots.add(new TimeRange(start, end));
        }
      }

      if (concept != null && concept.getIs24h() != true) {
        concept.setSchedule(concept.getIs24h(), slots);
      }

      return concept;
    } catch (SQLException e) {
      throw new RuntimeException("Error al obtener el concepto por id: " + e.getMessage());
    }
  }

  @Override
  public Concept updateConcept(Concept conceptToUpdate) {
    String updateConceptQuery = """
          UPDATE concept
          SET name = ?, details = ?, capacity = ?, is_active = ?, is_24h = ?
          WHERE concept_id = ?
        """;

    String deleteSlotsQuery = "DELETE FROM slot WHERE concept_id = ?";
    String insertSlotsQuery = "INSERT INTO slot(start_at, end_at, concept_id) VALUES(?, ?, ?)";

    Connection conn = null;
    try {
      conn = DbConfig.getConnection();
      conn.setAutoCommit(false);

      try (PreparedStatement pstmt = conn.prepareStatement(updateConceptQuery)) {
        pstmt.setString(1, conceptToUpdate.getName());
        pstmt.setString(2, conceptToUpdate.getDetails());
        pstmt.setInt(3, conceptToUpdate.getCapacity());
        pstmt.setBoolean(4, conceptToUpdate.getIsActive());
        pstmt.setBoolean(5, conceptToUpdate.getIs24h());
        pstmt.setObject(6, conceptToUpdate.getId());
        pstmt.executeUpdate();
      }

      try (PreparedStatement pstmt = conn.prepareStatement(deleteSlotsQuery)) {
        pstmt.setObject(1, conceptToUpdate.getId());
        pstmt.executeUpdate();
      }

      if (!conceptToUpdate.getIs24h() && conceptToUpdate.getSchedule() != null) {
        try (PreparedStatement pstmt = conn.prepareStatement(insertSlotsQuery)) {
          for (TimeRange scheduleConcept : conceptToUpdate.getSchedule()) {
            pstmt.setObject(1, Time.valueOf(scheduleConcept.getOpenTime()));
            pstmt.setObject(2, Time.valueOf(scheduleConcept.getCloseTime()));
            pstmt.setObject(3, conceptToUpdate.getId());
            pstmt.addBatch();
          }
          pstmt.executeBatch();
        }
      }

      conn.commit();
      return conceptToUpdate;
    } catch (SQLException e) {
      if (conn != null) {
        try {
          conn.rollback();
        } catch (SQLException ex) {
          throw new RuntimeException("Error al hacer rollback: " + ex.getMessage());
        }
      }
      throw new RuntimeException("Error al actualizar el concepto: " + e.getMessage());
    } finally {
      if (conn != null) {
        try {
          conn.setAutoCommit(true);
          conn.close();
        } catch (SQLException e) {
          throw new RuntimeException("Error al cerrar conexión: " + e.getMessage());
        }
      }
    }
  }
}
