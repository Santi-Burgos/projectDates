package san.projectdates.infrastructure.http;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import com.fasterxml.jackson.core.type.TypeReference;
import san.projectdates.core.dtos.ApiResponse;
import san.projectdates.core.dtos.ConceptRequest;
import san.projectdates.core.dtos.ConceptResponse;
import san.projectdates.core.entities.TimeRange;
import san.projectdates.core.services.ConceptService;

public class ConceptController {
  private final ConceptService conceptService;

  public ConceptController(ConceptService conceptService){
    this.conceptService = conceptService;
  }

  public void createConcept(Context ctx){
    try{
      String name = ctx.formParam("name");
      String details = ctx.formParam("details");
      int capacity = ctx.formParamAsClass("capacity", Integer.class).getOrDefault(0);
      boolean isActive = ctx.formParamAsClass("isActive", Boolean.class).getOrDefault(true);
      boolean is24h = ctx.formParamAsClass("is24h", Boolean.class).getOrDefault(false);
      UploadedFile file = ctx.uploadedFile("image");

      String scheduleJson = ctx.formParam("schedule");
      List<TimeRange> schedule = List.of();
      if (scheduleJson != null && !scheduleJson.isEmpty()) {
        schedule = ctx.jsonMapper().fromJsonString(scheduleJson, new TypeReference<List<TimeRange>>(){}.getType());
      }

      ConceptRequest conceptToCreate = new ConceptRequest(
        name,
        details,
        capacity,
        isActive,
        is24h,
        schedule
      );
      ConceptResponse newConcept = conceptService.createConcept(conceptToCreate, file.content(), file.filename());
      ctx.status(201).json(ApiResponse.success(newConcept, "Concepto creado con éxito"));
    }catch(IOException e){
      ctx.status(500).json(ApiResponse.error("No se puede guardar la imagen en la base de datos"));
    }
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
