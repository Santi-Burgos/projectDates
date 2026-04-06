package san.projectdates.core.repositories;

import san.projectdates.core.entities.Concept;

public interface ConceptRepository {
  Concept saveConcept(Concept newConcept);
}
