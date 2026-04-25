package san.projectdates.core.repositories;

import java.io.IOException;
import java.io.InputStream;

import san.projectdates.core.dtos.ImageResultOperation;

public interface ImageStorage {
  ImageResultOperation save(String fileName, InputStream fileContent) throws IOException; 

  void delete(String fileName) throws IOException;
}