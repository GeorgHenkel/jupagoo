package de.georghenkel.jupagoo.infrastructure.storage.google;

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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import de.georghenkel.jupagoo.infrastructure.storage.Storage;
import de.georghenkel.jupagoo.infrastructure.storage.UploadResult;

@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GoogleDriveStorageTest {
  private String accessToken;
  private Storage storage;

  @Before
  public void setUp() throws IOException {
    InputStream is =
        GoogleDriveStorageTest.class.getClassLoader().getResourceAsStream("access_token.txt");
    try (BufferedReader buffer = new BufferedReader(new InputStreamReader(is))) {
      accessToken = buffer.lines().findFirst().orElse("");
    }

    storage = new GoogleDriveStorage().initConnection(accessToken);
  }

  @Test
  public void step1_shouldFindNoFolder() {
    Stream<String> fileStream = storage.listFolder(null);
    Assert.assertTrue(fileStream.count() == 0);
  }

  @Test
  public void step2_shouldUploadFile() {
    InputStream is =
        GoogleDriveStorageTest.class.getClassLoader().getResourceAsStream("upload_test.txt");
    CompletableFuture<UploadResult> uploadFuture = storage.upload(is, "test.txt", "test");

    try {
      UploadResult result = uploadFuture.get();
      Assert.assertEquals(UploadResult.SUCCESSFULL, result);
    } catch (InterruptedException | ExecutionException ex) {
      Assert.fail(ex.getMessage());
    }
  }

  @Test
  public void step3_shouldFindFolder() {
    Stream<String> fileStream = storage.listFolder("test");
    Assert.assertTrue(fileStream.count() == 1);
  }

  @Test
  public void step4_shouldDeleteFolder() {
    storage.deleteFolder("test");
    Stream<String> fileStream = storage.listFolder("test");
    Assert.assertTrue(fileStream.count() == 0);
  }
}
