package de.georghenkel.jupagoo.infrastructure.storage.dropbox;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import de.georghenkel.jupagoo.infrastructure.storage.Storage;
import de.georghenkel.jupagoo.infrastructure.storage.UploadResult;

public class DropboxStorage implements Storage {
  private static final Logger LOG = LoggerFactory.getLogger(DropboxStorage.class);

  private DbxClientV2 client;
  private String uploadError;

  @Override
  public Storage initConnection(final String accessToken) {
    LOG.debug("Initializing client connection");

    DbxRequestConfig config =
        DbxRequestConfig.newBuilder("jupagoo/1.0.0").withUserLocale("de_DE").build();
    client = new DbxClientV2(config, accessToken);

    return this;
  }

  @Override
  public Stream<String> listFolder(final String path) {
    assertClientConnected();

    String folder = getPath(path);
    LOG.debug("Creating folder listing for path: " + folder);

    try {
      ListFolderResult result = client.files().listFolder(folder);
      return result.getEntries().stream().map(e -> e.getName()).sorted(String::compareTo);
    } catch (ListFolderErrorException ex) {
      return Stream.of(new String[0]);
    } catch (DbxException ex) {
      LOG.error("Could not retrieve file list", ex);
      throw new RuntimeException("Unable to retrieve files", ex);
    }
  }

  @Override
  public CompletableFuture<UploadResult> upload(final InputStream is, final String fileName,
      final String path) {
    assertClientConnected();

    String uploadPath = getPath(path);
    LOG.debug("Uploading file to path: " + uploadPath);

    CompletableFuture<UploadResult> completableFuture = new CompletableFuture<>();
    Executors.newCachedThreadPool().submit(() -> {
      try {
        client.files().uploadBuilder(uploadPath + "/" + fileName).withAutorename(true)
            .withMute(true).uploadAndFinish(is);
        completableFuture.complete(UploadResult.SUCCESSFULL);

        LOG.debug("Upload finished successfully");
      } catch (IOException | DbxException ex) {
        completableFuture.complete(UploadResult.FAILURE);
        LOG.error("Upload failed", ex);
        uploadError = ex.getMessage();
      }

      return null;
    });

    return completableFuture;
  }

  @Override
  public void deleteFolder(final String path) {
    assertClientConnected();

    if (StringUtils.isBlank(path)) {
      throw new RuntimeException("Path for deletion must not be null");
    }

    String folder = getPath(path);
    LOG.debug("Deleting folder: " + folder);

    try {
      client.files().deleteV2(folder);
    } catch (DbxException ex) {
      LOG.error("Could not delete folder", ex);
      throw new RuntimeException("Unable to delete folder", ex);
    }
  }

  @Override
  public Optional<String> getUploadFailure() {
    return Optional.ofNullable(uploadError);
  }

  private String getPath(final String path) {
    Optional<String> optPath = Optional.ofNullable(path);
    return APPLICATION_FOLDER + "/" + optPath.orElse("");
  }

  private void assertClientConnected() {
    if (client == null) {
      LOG.warn("Client is not initialized, cancelling operation");

      throw new RuntimeException(
          "Dropbox client was not initialized! Run 'initConnection()' first");
    }
  }
}
