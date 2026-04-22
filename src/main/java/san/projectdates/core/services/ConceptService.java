package san.projectdates.core.services;

import java.util.List;
import java.util.UUID;

import san.projectdates.core.dtos.ConceptRequest;
import san.projectdates.core.dtos.ConceptResponse;
import san.projectdates.core.entities.Concept;
import san.projectdates.core.repositories.ConceptRepository;
import san.projectdates.core.repositories.ErrorFactory;

public class ConceptService {
  private final ConceptRepository conceptRepository;
  private final ErrorFactory errorFactory;

  public ConceptService(ConceptRepository conceptRepository, ErrorFactory errorFactory) {
    this.conceptRepository = conceptRepository;
    this.errorFactory = errorFactory;
  }

  public ConceptResponse createConcept(ConceptRequest conceptInfo) {
    Concept newConcept = new Concept(
        conceptInfo.name(),
        conceptInfo.details(),
        conceptInfo.capacity(),
        conceptInfo.isActive(),
        conceptInfo.is24h(),
        conceptInfo.schedule());

    Concept createdConcept = conceptRepository.saveConcept(newConcept);
    return new ConceptResponse(createdConcept);
  }

  public List<ConceptResponse> findActiveConcept() {
    List<Concept> allConcepts = conceptRepository.findAllActiveConcept();

    return allConcepts.stream()
        .map(concept -> new ConceptResponse(concept))
        .toList();
  }

  public ConceptResponse findOneConcept(UUID concetpId) {
    Concept concept = conceptRepository.findConceptById(concetpId);

    return new ConceptResponse(concept);
  }

  public ConceptResponse toggleConceptStatus(UUID conceptId) {
    Concept concept = conceptRepository.findConceptById(conceptId);
    concept.setToggleStatus();

    return new ConceptResponse(concept);
  }

  public String deleteConcept(UUID conceptId) {
    int deletedConcept = conceptRepository.deleteConcept(conceptId);
    if (deletedConcept == 0) {
      throw errorFactory.badRequest("No se ha eliminado ningun usuario");
    }
    String response = "Filas eliminadas: " + deletedConcept + " Usuario eliminado con exito";
    return response;
  }

  public ConceptResponse updateConcept(ConceptRequest newConceptToEdit, UUID conceptId) {
    Concept existingConcept = conceptRepository.findConceptById(conceptId);
    if (existingConcept == null) {
      throw  errorFactory.notFound("Concepto no encontrado con ID: " + conceptId);
    }

    existingConcept.merge(newConceptToEdit);

    Concept updatedConcept = conceptRepository.updateConcept(existingConcept);
    return new ConceptResponse(updatedConcept);
  }
}
