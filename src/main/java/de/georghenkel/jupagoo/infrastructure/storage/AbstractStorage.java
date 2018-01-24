package de.georghenkel.jupagoo.infrastructure.storage;

import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractStorage implements Storage {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractStorage.class);

  protected String uploadError;

  protected abstract void assertClientConnected();

  protected abstract Stream<String> getFolderListing(final String path);

  protected abstract void delete(final String folder);

  protected abstract void upload(final InputStream is, final String fileName, final String path,
      final CompletableFuture<UploadResult> completableFuture);

  @Override
  public Stream<String> listFolder(final String path) {
    assertClientConnected();

    String folder = getPath(path);
    LOG.debug("Creating folder listing for path: " + folder);

    return getFolderListing(folder);
  }

  @Override
  public CompletableFuture<UploadResult> upload(final InputStream is, final String fileName,
      final String path) {
    assertClientConnected();

    String folder = getPath(path);
    LOG.debug("Uploading file to path: " + folder);

    CompletableFuture<UploadResult> completableFuture = new CompletableFuture<>();
    upload(is, fileName, folder, completableFuture);

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

    delete(folder);
  }


  @Override
  public Optional<String> getUploadFailure() {
    return Optional.ofNullable(uploadError);
  }

  protected String getPath(final String path) {
    Optional<String> optPath = Optional.ofNullable(path);
    return APPLICATION_FOLDER + "/" + optPath.orElse("");
  }
}
