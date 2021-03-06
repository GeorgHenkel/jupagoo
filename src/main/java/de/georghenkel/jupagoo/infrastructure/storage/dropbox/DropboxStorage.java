package de.georghenkel.jupagoo.infrastructure.storage.dropbox;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import de.georghenkel.jupagoo.infrastructure.storage.AbstractStorage;
import de.georghenkel.jupagoo.infrastructure.storage.Storage;
import de.georghenkel.jupagoo.infrastructure.storage.UploadResult;

public class DropboxStorage extends AbstractStorage {
  private static final Logger LOG = LoggerFactory.getLogger(DropboxStorage.class);

  private DbxClientV2 client;

  @Override
  public Storage initConnection(final String accessToken) {
    LOG.debug("Initializing client connection");

    DbxRequestConfig config =
        DbxRequestConfig.newBuilder("jupagoo/1.0.0").withUserLocale("de_DE").build();
    client = new DbxClientV2(config, accessToken);

    return this;
  }

  @Override
  protected Stream<String> getFolderListing(final String folder) {
    try {
      ListFolderResult result = client.files().listFolder(folder);
      return result.getEntries().stream().map(e -> e.getName()).sorted(String::compareTo);
    } catch (ListFolderErrorException ex) {
      LOG.warn("Folder " + folder + " not found", ex);
      return Stream.of(new String[0]);
    } catch (DbxException ex) {
      LOG.error("Could not retrieve file list", ex);
      throw new RuntimeException("Unable to retrieve files", ex);
    }
  }

  @Override
  protected void upload(final InputStream is, final String fileName, final String path,
      final CompletableFuture<UploadResult> completableFuture) {
    Executors.newCachedThreadPool().submit(() -> {
      try {
        client.files().uploadBuilder(path + "/" + fileName).withAutorename(true).withMute(true)
            .uploadAndFinish(is);
        completableFuture.complete(UploadResult.SUCCESSFULL);

        LOG.debug("Upload finished successfully");
      } catch (IOException | DbxException ex) {
        completableFuture.complete(UploadResult.FAILURE);
        LOG.error("Upload failed", ex);
        uploadError = ex.getMessage();
      }
    });
  }

  @Override
  public void delete(final String folder) {
    try {
      client.files().deleteV2(folder);
    } catch (DbxException ex) {
      LOG.error("Could not delete folder", ex);
      throw new RuntimeException("Unable to delete folder", ex);
    }
  }

  @Override
  protected void assertClientConnected() {
    if (client == null) {
      LOG.warn("Client is not initialized, cancelling operation");

      throw new RuntimeException(
          "Dropbox client was not initialized! Run 'initConnection()' first");
    }
  }
}
