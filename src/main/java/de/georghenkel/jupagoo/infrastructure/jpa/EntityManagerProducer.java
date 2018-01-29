package de.georghenkel.jupagoo.infrastructure.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class EntityManagerProducer {
  @PersistenceContext
  EntityManager entityManager;

  @Produces
  public EntityManager createEntityManager() {
    return entityManager;
  }
}
