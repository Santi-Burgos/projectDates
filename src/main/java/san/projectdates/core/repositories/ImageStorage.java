package san.projectdates.core.repositories;

import java.io.IOException;
import java.io.InputStream;

public interface ImageStorage {
  String save(String fileName, InputStream fileContent) throws IOException; 
}