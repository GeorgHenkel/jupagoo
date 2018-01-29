package de.georghenkel.jupagoo.interfaces.jsf.storage;

import java.util.Arrays;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import de.georghenkel.jupagoo.domain.storage.model.StorageConnection;
import de.georghenkel.jupagoo.domain.storage.repository.StorageRepository;

@Named
@ViewScoped
public class StorageProviderController {
  @Inject
  private StorageRepository repository;

  public List<StorageConnection> getAvailableProvider() {
    return repository.loadStorageConnections();
  }

  public List<StorageProvider> getManagedStorageProvider() {
    return Arrays.asList(StorageProvider.DROPBOX);
  }
}
