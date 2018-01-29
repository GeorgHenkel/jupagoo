package de.georghenkel.jupagoo.domain.storage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import de.georghenkel.jupagoo.application.model.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "STORAGE_CONNECTION")
@NamedQueries({@NamedQuery(name = StorageConnection.QUERY_FIND_ALL,
    query = "select sc from StorageConnection sc")})
public class StorageConnection extends AbstractEntity {
  private static final long serialVersionUID = 1L;

  public static final String QUERY_FIND_ALL = "StorageConnection.findAll";

  @Column(name = "PROVIDER", nullable = false)
  @Enumerated(EnumType.STRING)
  private StorageType provider;

  @Column(name = "ACCESS_TOKEN", nullable = false)
  private String accessToken;
}
