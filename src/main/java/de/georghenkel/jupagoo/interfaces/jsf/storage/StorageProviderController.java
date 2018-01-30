package de.georghenkel.jupagoo.interfaces.jsf.storage;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import de.georghenkel.jupagoo.domain.storage.model.StorageConnection;
import de.georghenkel.jupagoo.domain.storage.repository.StorageRepository;

@Named
@ViewScoped
public class StorageProviderController implements Serializable {
  private static final long serialVersionUID = 1L;

  @Inject
  private StorageRepository repository;

  private List<StorageConnection> managedConnections;

  @PostConstruct
  public void init() {
    managedConnections = repository.loadStorageConnections();
  }

  public List<StorageConnection> getManagedConnections() {
    return managedConnections;
  }

  public List<StorageProvider> getAvailableProviders() {
    return Arrays.asList(StorageProvider.DROPBOX, StorageProvider.GOOGLE_DRIVE,
        StorageProvider.BOX);
  }

  public boolean isManaged(final StorageProvider provider) {
    return managedConnections.stream().anyMatch(mc -> mc.getProvider() == provider.getType());
  }
}
