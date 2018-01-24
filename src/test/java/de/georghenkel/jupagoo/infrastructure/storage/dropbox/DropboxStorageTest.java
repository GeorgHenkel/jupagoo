package de.georghenkel.jupagoo.infrastructure.storage.dropbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.georghenkel.jupagoo.infrastructure.storage.UploadResult;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DropboxStorageTest {
  private static final Logger LOG = LoggerFactory.getLogger(DropboxStorage.class);

  private static String ACCESS_TOKEN;

  private final DropboxStorage storage = new DropboxStorage();

  @Before
  public void setUp() throws IOException {
    InputStream is = DropboxStorage.class.getClassLoader().getResourceAsStream("access_token.txt");
    try (BufferedReader buffer = new BufferedReader(new InputStreamReader(is))) {
      ACCESS_TOKEN = buffer.lines().findFirst().orElse("");
    }
  }

  @Test
  public void step1_shouldFindNoFolder() {
    Stream<String> fileStream = storage.initConnection(ACCESS_TOKEN).listFolder(null);
    Assert.assertTrue(fileStream.count() == 0);
  }

  @Test
  public void step2_shouldUploadFile() {
    InputStream is = DropboxStorage.class.getClassLoader().getResourceAsStream("upload_test.txt");
    CompletableFuture<UploadResult> uploadFuture =
        storage.initConnection(ACCESS_TOKEN).upload(is, "test.txt", "test");

    try {
      UploadResult result = uploadFuture.get();
      Assert.assertEquals(UploadResult.SUCCESSFULL, result);
    } catch (InterruptedException | ExecutionException ex) {
      Assert.fail(ex.getMessage());
    }
  }

  @Test
  public void step3_shouldFindFolder() {
    Stream<String> fileStream = storage.initConnection(ACCESS_TOKEN).listFolder("test");
    Assert.assertTrue(fileStream.count() == 1);
  }

  @Test
  public void step4_shouldDeleteFolder() {
    storage.initConnection(ACCESS_TOKEN).deleteFolder("test");
    Stream<String> fileStream = storage.initConnection(ACCESS_TOKEN).listFolder("test");
    Assert.assertTrue(fileStream.count() == 0);
  }
}
