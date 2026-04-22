package san.projectdates.core.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import san.projectdates.core.dtos.ConceptRequest;
import san.projectdates.core.dtos.ConceptResponse;
import san.projectdates.core.entities.Concept;
import san.projectdates.core.entities.TimeRange;
import san.projectdates.core.repositories.ConceptRepository;
import san.projectdates.infrastructure.factories.ErrorFactoryImpl;

public class ConceptServiceTest {
  private ConceptRepository mockRepository;
  private ConceptService conceptService;

  @BeforeEach
  public void setUp(){
    this.mockRepository = new ConceptRepository() {
      @Override
      public Concept saveConcept(Concept newConcept){
         return newConcept;
      }

      @Override
      public int deleteConcept(UUID id){
        return 1;
      }

      @Override
      public List<Concept> findAllActiveConcept(){
        return new ArrayList<>();
      }

      @Override
      public Concept findConceptById(UUID conceptId){
        return new Concept();
      }
      
      @Override
      public Concept updateConcept(Concept conceptToUpdate){
        return new Concept();
      }
    };

    ErrorFactoryImpl errorFactoryImpl = new ErrorFactoryImpl();
    this.conceptService = new ConceptService(mockRepository, errorFactoryImpl);
  }

  @Test
  public void shouldCreateNewConceptSuccessfully(){
    List<TimeRange> schedule = List.of(); 
    ConceptRequest conceptReq = new ConceptRequest(
      "Concepto test",
      "Concept test para que de succesfully",
      25,
      true,
      true,
      schedule
    ); 
    ConceptResponse result = conceptService.createConcept(conceptReq);
    Assertions.assertNotNull(result, "El objeto ConceptResponse no debería ser nulo");
    Assertions.assertEquals("Concepto test", result.name());
  }

}
