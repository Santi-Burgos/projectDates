package san.projectdates.core.repositories;

import java.util.List;
import java.util.UUID;

import san.projectdates.core.entities.Concept;

public interface ConceptRepository {
  Concept saveConcept(Concept newConcept);

  int deleteConcept(UUID conceptId);

  List<Concept> findAllActiveConcept();

  Concept findConceptById(UUID conceptId);

  Concept updateConcept(Concept conceptToUpdate);
}
