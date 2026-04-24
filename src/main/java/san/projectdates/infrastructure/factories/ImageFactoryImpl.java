package san.projectdates.infrastructure.factories;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import san.projectdates.core.repositories.ImageStorage;

public class ImageFactoryImpl implements ImageStorage {
  private final String uploadDir = "uploads";

  @Override
  public String save(String fileName, InputStream fileContent) throws IOException {
    Path pathDir = Paths.get(uploadDir);
    if (!Files.exists(pathDir)) {
      Files.createDirectories(pathDir);
    }

    Path targetPath = pathDir.resolve(fileName);
    Files.copy(fileContent, targetPath, StandardCopyOption.REPLACE_EXISTING);
    return "/" + uploadDir + "/" + fileName;
  }

}