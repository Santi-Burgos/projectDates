package san.projectdates.infrastructure.http;

import java.util.List;
import java.util.UUID;

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

  public void deleteConcept(Context ctx){
    try{
      String idParam = ctx.pathParam("id");
      UUID id = UUID.fromString(idParam);
      String deleteConcept = conceptService.deleteConcept(id);

      ctx.status(200).json(deleteConcept);
    }catch(Exception e){
      ctx.status(500).result(e.getMessage());
    }
  }

  public void findActiveConcept(Context ctx){
    try{
      List<ConceptResponse> conceptList = conceptService.findActiveConcept();

      ctx.status(200).json(conceptList);
    }catch(Exception e){
      ctx.status(500).result(e.getMessage());
    }
  }

  public void findOneConcept(Context ctx){
    try {
      String idParam = ctx.pathParam("id");
      UUID conceptId = UUID.fromString(idParam);
      ConceptResponse response = conceptService.findOneConcept(conceptId);

      ctx.status(200).json(response);
    } catch (Exception e) {
      ctx.status(500).result(e.getMessage());
    }
  }
}
