package san.projectdates.infrastructure.factories;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import san.projectdates.core.dtos.ImageResultOperation;
import san.projectdates.core.repositories.ImageStorage;

public class ImageFactoryImpl implements ImageStorage {
  private final String uploadDir = "uploads";

  @Override
  public ImageResultOperation save(String fileName, InputStream fileContent) throws IOException {
    
    Path pathDir = Paths.get(uploadDir);
    if (!Files.exists(pathDir)) {
      Files.createDirectories(pathDir);
    }

    String idImageDisk = generateImageIdName(fileName);

    Path targetPath = pathDir.resolve(idImageDisk);
    Files.copy(fileContent, targetPath, StandardCopyOption.REPLACE_EXISTING);
    String finalPath = "/" + uploadDir + "/" + fileName;
    return new ImageResultOperation(finalPath, idImageDisk);
  }

  @Override
  public void delete(String fileName) throws IOException{
    Path pathDir = Paths.get(uploadDir);
    Path targetPath = pathDir.resolve(fileName);
    Files.deleteIfExists(targetPath);
  }

  private String generateImageIdName(String fileName){
    UUID idName = UUID.randomUUID();

    int lastIndexOf = fileName.lastIndexOf('.');
    if(lastIndexOf == -1){
      throw new RuntimeException("Error interno, no se ha podido extraer el path");
    }

    return idName + fileName.substring(lastIndexOf + 1);  
  }
}