package de.georghenkel.jupagoo.infrastructure.storage.box;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxAPIException;
import com.box.sdk.BoxFolder;
import de.georghenkel.jupagoo.infrastructure.storage.AbstractStorage;
import de.georghenkel.jupagoo.infrastructure.storage.Storage;
import de.georghenkel.jupagoo.infrastructure.storage.UploadResult;

public class BoxStorage extends AbstractStorage {
  private static final Logger LOG = LoggerFactory.getLogger(BoxStorage.class);

  private BoxAPIConnection client;

  @Override
  public Storage initConnection(final String accessToken) {
    LOG.debug("Initializing client connection");

    client = new BoxAPIConnection(accessToken);
    return this;
  }

  @Override
  protected Stream<String> getFolderListing(final String folder) {
    try {
      BoxFolder boxFolder = new BoxFolder(client, folder);
      return Stream.of(boxFolder).map(info -> info.getID());
    } catch (BoxAPIException ex) {
      LOG.warn("Folder " + folder + " not found", ex);
      return Stream.of(new String[0]);
    }
  }

  @Override
  protected void upload(final InputStream is, final String fileName, final String path,
      final CompletableFuture<UploadResult> completableFuture) {
    Executors.newCachedThreadPool().submit(() -> {
      try {
        BoxFolder boxFolder = new BoxFolder(client, path);
        boxFolder.uploadFile(is, fileName, 1024,
            (numBytes, totalBytes) -> LOG.info("Uploading: " + (numBytes / totalBytes)));
        completableFuture.complete(UploadResult.SUCCESSFULL);

        LOG.debug("Upload finished successfully");
      } catch (BoxAPIException ex) {
        completableFuture.complete(UploadResult.FAILURE);
        LOG.error("Upload failed", ex);
        uploadError = ex.getMessage();
      }
    });
  }

  @Override
  public void delete(final String folder) {
    try {
      BoxFolder boxFolder = new BoxFolder(client, folder);
      boxFolder.delete(true);
    } catch (BoxAPIException ex) {
      LOG.error("Could not delete folder", ex);
      throw ex;
    }
  }

  @Override
  protected void assertClientConnected() {
    if (client == null) {
      LOG.warn("Client is not initialized, cancelling operation");

      throw new RuntimeException("Box client was not initialized! Run 'initConnection()' first");
    }
  }
}
