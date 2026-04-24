package san.projectdates.infrastructure.entrypoints;

import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import san.projectdates.core.repositories.ImageStorage;
import san.projectdates.core.services.UploadFileService;
import san.projectdates.infrastructure.factories.ImageFactoryImpl;

public class UploadImageServlet extends HttpServlet {
  private UploadFileService uploadFileService; 

  @Override
  public void init(){
    ImageStorage storageAdapter = new ImageFactoryImpl();
    this.uploadFileService = new UploadFileService(storageAdapter);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    Part filePart = request.getPart("file");
    String fileName = filePart.getSubmittedFileName();
    try(InputStream input = filePart.getInputStream()){
      uploadFileService.execute(fileName, input);
    }

    response.setStatus(HttpServletResponse.SC_OK);
  }
}
