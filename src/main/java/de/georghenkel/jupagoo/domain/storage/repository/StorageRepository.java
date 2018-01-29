package de.georghenkel.jupagoo.domain.storage.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import de.georghenkel.jupagoo.domain.storage.model.StorageConnection;

@Stateless
public class StorageRepository {
  @Inject
  private EntityManager entityManager;

  public List<StorageConnection> loadStorageConnections() {
    return entityManager.createNamedQuery(StorageConnection.QUERY_FIND_ALL, StorageConnection.class)
        .getResultList();
  }
}
