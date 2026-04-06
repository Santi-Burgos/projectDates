package san.projectdates.infrastructure.persistence;

import san.projectdates.core.repositories.ConceptRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import san.projectdates.core.entities.Concept;
import san.projectdates.core.entities.TimeRange;

public class SqlConceptRepository implements ConceptRepository {
  @Override
  public Concept saveConcept(Concept concept) {
    if (concept.getIs24h()) {
      System.out.println("dentro del if");
      return this.executeSave24h(concept);
    } else {
      System.out.println("dentro del else");
      return this.executeSaveLimited(concept);
    }
  }

  public Concept executeSave24h(Concept newConcept) {
    String saveConcept = """
          INSERT INTO concept(concept_id, name, details, capacity, is_active, is_24h)
          VALUES(?, ?, ?, ?, ?)
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
          rs.getInt("capactiy"),
          rs.getBoolean("is_active"),
          rs.getBoolean("is_24h"));
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
          pstmt.setObject(1, java.sql.Time.valueOf(scheduleConcept.getOpenTime()));
          pstmt.setObject(2, java.sql.Time.valueOf(scheduleConcept.getCloseTime()));
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
}
