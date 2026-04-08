package san.projectdates.core.services;

import java.util.List;
import java.util.UUID;

import san.projectdates.core.dtos.ConceptRequest;
import san.projectdates.core.dtos.ConceptResponse;
import san.projectdates.core.entities.Concept;
import san.projectdates.core.repositories.ConceptRepository;

public class ConceptService {
  private final ConceptRepository conceptRepository;

  public ConceptService(ConceptRepository conceptRepository){
    this.conceptRepository = conceptRepository;
  }

  public ConceptResponse createConcept(ConceptRequest conceptInfo){
    Concept newConcept = new Concept(
      conceptInfo.name(),
      conceptInfo.details(),
      conceptInfo.capacity(),
      conceptInfo.isActive(),
      conceptInfo.is24h(),
      conceptInfo.schedule()
    );

    Concept createdConcept = conceptRepository.saveConcept(newConcept);
    return new ConceptResponse(createdConcept);
  } 

  public List<ConceptResponse> findActiveConcept(){
    List<Concept> allConcepts = conceptRepository.findAllActiveConcept();
    
    return allConcepts.stream()
      .map( concept -> new ConceptResponse(concept))
      .toList();
  } 

  public ConceptResponse findOneConcept(UUID concetpId){
    Concept concept = conceptRepository.findConceptById(concetpId);

    return new ConceptResponse(concept);
  }

  public ConceptResponse toggleConceptStatus(UUID conceptId){
    Concept concept = conceptRepository.findConceptById(conceptId);
    concept.setToggleStatus();

    return new ConceptResponse(concept);
  }

  public String deleteConcept(UUID conceptId){
    int deletedConcept = conceptRepository.deleteConcept(conceptId); 
    if(deletedConcept == 0){
      throw new RuntimeException("No se ha eliminado ningun usuario");
    }
    String response = "Filas eliminadas: " + deletedConcept + " Usuario eliminado con exito";
    return response;
  }
}
