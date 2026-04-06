package san.projectdates.core.dtos;

import java.util.UUID;

import san.projectdates.core.entities.Concept;
import san.projectdates.core.entities.TimeRange;
import java.util.List;

public record ConceptResponse(
  UUID id,
  String name,
  String details,
  int capacity,
  Boolean is_active,
  Boolean is_24hs,
  List<TimeRange> schedule
){
  public ConceptResponse(Concept concept){
    this(
      concept.getId(),
      concept.getName(),
      concept.getDetails(),
      concept.getCapacity(),
      concept.getIsActive(),
      concept.getIs24h(),
      concept.getSchedule()
    );
  }
}
