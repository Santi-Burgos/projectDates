package san.projectdates.infrastructure.http;

import io.javalin.http.Context;
import san.projectdates.core.dtos.ConceptRequest;
import san.projectdates.core.dtos.ConceptResponse;
import san.projectdates.core.services.ConceptService;

public class ConceptController {
  private final ConceptService conceptService;

  public ConceptController(ConceptService conceptService){
    this.conceptService = conceptService;
  }

  public void createConcept(Context ctx){
    try{
      ConceptRequest conceptToCreate = ctx.bodyAsClass(ConceptRequest.class);

      ConceptResponse newConcept = conceptService.createConcept(conceptToCreate);
      ctx.status(201).json(newConcept);
    }catch(Exception e){
      ctx.status(500).result(e.getMessage());
    }
  }
}
