package de.georghenkel.jupagoo.infrastructure.storage.dropbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.junit.Before;
import org.junit.Test;

public class DropboxStorageTest {
  private static String ACCESS_TOKEN;

  private DropboxStorage storage;

  @Before
  public void setUp() throws IOException {
    InputStream is = DropboxStorage.class.getClassLoader().getResourceAsStream("access_token.txt");
    try (BufferedReader buffer = new BufferedReader(new InputStreamReader(is))) {
      ACCESS_TOKEN = buffer.lines().findFirst().orElse("");
    }
  }

  @Test
  public void shouldUploadFile() {
    storage = new DropboxStorage();

    storage.initConnection(ACCESS_TOKEN); // .upload(is, path);
  }
}
