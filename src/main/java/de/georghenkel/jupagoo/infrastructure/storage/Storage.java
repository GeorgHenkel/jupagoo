package de.georghenkel.jupagoo.infrastructure.storage;

import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public interface Storage {
  Storage initConnection(String accessToken);

  Stream<String> listFolder(@Nullable String path);

  CompletableFuture<UploadResult> upload(InputStream is, String fileName, @Nullable String path);

  Optional<String> getUploadFailure();
}
