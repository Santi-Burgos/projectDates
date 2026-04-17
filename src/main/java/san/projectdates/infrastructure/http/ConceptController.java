package san.projectdates.infrastructure.http;

import java.util.List;
import java.util.UUID;

import io.javalin.http.Context;
import san.projectdates.core.dtos.ApiResponse;
import san.projectdates.core.dtos.ConceptRequest;
import san.projectdates.core.dtos.ConceptResponse;
import san.projectdates.core.services.ConceptService;

public class ConceptController {
  private final ConceptService conceptService;

  public ConceptController(ConceptService conceptService) {
    this.conceptService = conceptService;
  }

  public void createConcept(Context ctx) {
    ConceptRequest conceptToCreate = ctx.bodyAsClass(ConceptRequest.class);

    ConceptResponse newConcept = conceptService.createConcept(conceptToCreate);
    ctx.status(201).json(ApiResponse.success(newConcept, "Concepto creado con éxito"));
  }

  public void updateConcept(Context ctx) {
    String idParam = ctx.pathParam("id");
    UUID conceptId = UUID.fromString(idParam);
    ConceptRequest conceptToUpdate = ctx.bodyAsClass(ConceptRequest.class);
    ConceptResponse updatedConcept = conceptService.updateConcept(conceptToUpdate, conceptId);
    
    ctx.status(200).json(ApiResponse.success(updatedConcept, "Concepto actualizado con éxito"));
  }

  public void deleteConcept(Context ctx) {
    String idParam = ctx.pathParam("id");
    UUID id = UUID.fromString(idParam);
    String deleteConcept = conceptService.deleteConcept(id);

    ctx.status(200).json(ApiResponse.success(deleteConcept, "Concepto eliminado con exito"));
  }

  public void findActiveConcept(Context ctx) {
    List<ConceptResponse> conceptList = conceptService.findActiveConcept();
    
    ctx.status(200).json(ApiResponse.success(conceptList, "Lista de conceptos obtenidas con éxito"));
  }

  public void findOneConcept(Context ctx) {
    String idParam = ctx.pathParam("id");
    UUID conceptId = UUID.fromString(idParam);
    ConceptResponse response = conceptService.findOneConcept(conceptId);
    
    ctx.status(200).json(ApiResponse.success(response, "Concepto obtenido con éxito"));
  }
}
