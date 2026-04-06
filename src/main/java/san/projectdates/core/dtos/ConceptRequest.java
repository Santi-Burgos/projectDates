package san.projectdates.core.dtos;

import java.util.List;
import san.projectdates.core.entities.TimeRange;

public record ConceptRequest(
  String name,
  String details, 
  int capacity,
  Boolean isActive,
  Boolean is24h,
  List<TimeRange> schedule
){ }
