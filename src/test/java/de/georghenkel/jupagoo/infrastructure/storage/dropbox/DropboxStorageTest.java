package de.georghenkel.jupagoo.infrastructure.storage.dropbox;

import org.junit.Test;

public class DropboxStorageTest {
  private static final String ACCESS_TOKEN =
      "E18GubRhyLQAAAAAAAAM8d7dARm1gGLqQ2cf8sOjsdbY6o3wVOJrCqF7J7MNsaH0";

  private DropboxStorage storage;

  @Test
  public void shouldUploadFile() {
    storage = new DropboxStorage();

    storage.initConnection(ACCESS_TOKEN); // .upload(is, path);
  }
}
