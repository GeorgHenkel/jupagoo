package de.georghenkel.jupagoo.infrastructure.storage.dropbox;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
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

		DbxRequestConfig config = DbxRequestConfig.newBuilder("jupagoo/1.0.0").withUserLocale("de_DE").build();
		client = new DbxClientV2(config, accessToken);

		return this;
	}

	@Override
	public Stream<String> listFolder(final String path) {
		Optional<String> optPath = Optional.ofNullable(path);
		LOG.debug("Creating folder listing for path: " + optPath.orElse(""));

		assertClientConnected();

		try {
			ListFolderResult result = client.files().listFolder(optPath.orElse(""));

			return result.getEntries().stream().map(e -> e.getName()).sorted(String::compareTo);
		} catch (DbxException ex) {
			LOG.error("Could not retrieve file list", ex);
			throw new RuntimeException("Unable to retrieve files", ex);
		}
	}

	@Override
	public CompletableFuture<UploadResult> upload(final InputStream is, final String fileName, final String path) {
		Optional<String> optPath = Optional.ofNullable(path);
		LOG.debug("Uploading file to path: " + optPath.orElse(""));

		assertClientConnected();

		CompletableFuture<UploadResult> completableFuture = new CompletableFuture<>();
		Executors.newCachedThreadPool().submit(() -> {
			try (InputStream in = new FileInputStream("test.txt")) {
				client.files().uploadBuilder(optPath.orElse("") + fileName).withAutorename(true).withMute(true)
						.uploadAndFinish(in);
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
	public Optional<String> getUploadFailure() {
		return Optional.ofNullable(uploadError);
	}

	private void assertClientConnected() {
		if (client == null) {
			LOG.warn("Client is not initialized, cancelling operation");

			throw new RuntimeException("Dropbox client was not initialized! Run 'initConnection()' first");
		}
	}
}
