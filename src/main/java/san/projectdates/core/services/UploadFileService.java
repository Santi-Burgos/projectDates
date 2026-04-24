package san.projectdates.core.services;

import java.io.IOException;
import java.io.InputStream;

import san.projectdates.core.repositories.ImageStorage;

public class UploadFileService{
  private final ImageStorage storage;

  public UploadFileService(ImageStorage storage) {
    this.storage = storage;
  }

  public void execute(String name, InputStream data) throws IOException {
    storage.save(name, data);
  }
}

