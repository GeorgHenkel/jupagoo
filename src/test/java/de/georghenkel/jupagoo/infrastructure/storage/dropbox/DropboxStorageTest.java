package de.georghenkel.jupagoo.infrastructure.storage.dropbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropboxStorageTest {
  private static final Logger LOG = LoggerFactory.getLogger(DropboxStorage.class);

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
  public void shouldNotFindFolder() {

  }

  @Test
  public void shouldUploadFile() {
    storage = new DropboxStorage();

    Stream<String> fileStream = storage.initConnection(ACCESS_TOKEN).listFolder("jupagoo"); // .upload(is,
    List<String> files = fileStream.collect(Collectors.toList());
    LOG.info("Count of files: " + files.size());
    Assert.assertTrue(files.size() > 0);

    files.forEach(LOG::info);
  }
}
