package san.projectdates.core.services;

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
}
