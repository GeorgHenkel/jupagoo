package de.georghenkel.jupagoo.infrastructure.storage.google;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import de.georghenkel.jupagoo.infrastructure.storage.AbstractStorage;
import de.georghenkel.jupagoo.infrastructure.storage.Storage;
import de.georghenkel.jupagoo.infrastructure.storage.UploadResult;
import de.georghenkel.jupagoo.infrastructure.storage.dropbox.DropboxStorage;

public class GoogleDriveStorage extends AbstractStorage {
  private static final Logger LOG = LoggerFactory.getLogger(DropboxStorage.class);

  private static final String APPLICATION_NAME = "jupagoo";
  private static final java.io.File DATA_STORE_DIR =
      new java.io.File(System.getProperty("user.home"), ".credentials/drive-java-quickstart");
  private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_FILE);

  private Drive client;

  @Override
  public Storage initConnection(final String accessToken) {
    LOG.debug("Initializing client connection");

    try {
      HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
      JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();

      Credential credential = authorize(jacksonFactory, httpTransport, dataStoreFactory, SCOPES);
      client = new Drive.Builder(httpTransport, jacksonFactory, credential)
          .setApplicationName(APPLICATION_NAME).build();
    } catch (IOException | GeneralSecurityException ex) {
      LOG.error("Unable to initialize client connection", ex);
      throw new RuntimeException("Unable to initialize client connection", ex);
    }

    return this;
  }

  @Override
  protected Stream<String> getFolderListing(final String folder) {
    try {
      FileList fileList = client.files().list().setQ("'" + folder + "' in parents").execute();
      return fileList.getFiles().stream().map(e -> e.getName()).sorted(String::compareTo);
    } catch (IOException ex) {
      LOG.error("Could not retrieve file list", ex);
      throw new RuntimeException("Unable to retrieve files", ex);
    }
  }

  @Override
  protected void upload(final InputStream is, final String fileName, final String path,
      final CompletableFuture<UploadResult> completableFuture) {
    Executors.newCachedThreadPool().submit(() -> {
      try {
        // FIXME: load folder id
        String folderId = "0BwwA4oUTeiV1TGRPeTVjaWRDY1E";
        File metadata = new File();
        metadata.setName(fileName);
        // metadata.setParents(Collections.singletonList(folderId));

        InputStreamContent mediaContent = new InputStreamContent(null, is);
        client.files().create(metadata, mediaContent).setFields("id").execute();

        completableFuture.complete(UploadResult.SUCCESSFULL);
        LOG.debug("Upload finished successfully");
      } catch (IOException ex) {
        completableFuture.complete(UploadResult.FAILURE);
        LOG.error("Upload failed", ex);
        uploadError = ex.getMessage();
      }
    });
  }

  @Override
  public void delete(final String folder) {
    // FIXME
  }

  private Credential authorize(final JacksonFactory jsonFactory, final HttpTransport httpTransport,
      final FileDataStoreFactory dataStoreFactory, final List<String> scopes) throws IOException {

    InputStream in = this.getClass().getClassLoader().getResourceAsStream("client_secret.json");
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
        JacksonFactory.getDefaultInstance(), clientSecrets, scopes)
            .setDataStoreFactory(dataStoreFactory).setAccessType("online").build();

    LocalServerReceiver localServerReicer = new LocalServerReceiver.Builder().setPort(9876).build();
    return new AuthorizationCodeInstalledApp(flow, localServerReicer).authorize("user");
  }

  @Override
  protected void assertClientConnected() {
    if (client == null) {
      LOG.warn("Client is not initialized, cancelling operation");

      throw new RuntimeException(
          "Google Drive client was not initialized! Run 'initConnection()' first");
    }
  }
}
