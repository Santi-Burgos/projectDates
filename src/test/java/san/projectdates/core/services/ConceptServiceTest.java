package san.projectdates.core.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import san.projectdates.core.dtos.ConceptRequest;
import san.projectdates.core.dtos.ConceptResponse;
import san.projectdates.core.dtos.ImageResultOperation;
import san.projectdates.core.entities.Concept;
import san.projectdates.core.entities.TimeRange;
import san.projectdates.core.repositories.ConceptRepository;
import san.projectdates.core.repositories.ImageStorage;
import san.projectdates.infrastructure.factories.ErrorFactoryImpl;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ConceptServiceTest {
  private ConceptRepository mockRepository;
  private ConceptService conceptService;

  @BeforeEach
  public void setUp() {
    this.mockRepository = new ConceptRepository() {
      @Override
      public Concept saveConcept(Concept newConcept) {
        return newConcept;
      }

      @Override
      public int deleteConcept(UUID id) {
        return 1;
      }

      @Override
      public List<Concept> findAllActiveConcept() {
        return new ArrayList<>();
      }

      @Override
      public Concept findConceptById(UUID conceptId) {
        return new Concept();
      }

      @Override
      public Concept updateConcept(Concept conceptToUpdate) {
        return new Concept();
      }
    };

    ImageStorage mockImageStorage = new ImageStorage() {
      @Override
      public ImageResultOperation save(String fileName, InputStream fileContent) throws IOException {
        return new ImageResultOperation("/uploads/" + fileName, fileName);
      }
      @Override
      public void delete(String filename) throws IOException{}
    };

    ErrorFactoryImpl errorFactoryImpl = new ErrorFactoryImpl();
    this.conceptService = new ConceptService(mockRepository, errorFactoryImpl, mockImageStorage);
  }

  @Test
  public void shouldCreateNewConceptSuccessfully() throws IOException {
    List<TimeRange> schedule = List.of();
    ConceptRequest conceptReq = new ConceptRequest(
        "Concepto test",
        "Concept test para que de succesfully",
        25,
        true,
        true,
        schedule);
    InputStream imageStream = new ByteArrayInputStream("fake-image-content".getBytes());
    String fileName = "test-image.jpg";

    ConceptResponse result = conceptService.createConcept(conceptReq, imageStream, fileName);
    Assertions.assertNotNull(result, "El objeto ConceptResponse no debería ser nulo");
    Assertions.assertEquals("Concepto test", result.name());
  }

}
