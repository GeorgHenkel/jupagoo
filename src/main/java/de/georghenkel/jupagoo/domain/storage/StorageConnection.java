package de.georghenkel.jupagoo.domain.storage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import de.georghenkel.jupagoo.domain.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "STORAGE_CONNECTION")
@Getter
@Setter
@ToString(callSuper = true)
public class StorageConnection extends AbstractEntity {
  private static final long serialVersionUID = 1L;

  @Column(name = "PROVIDER", nullable = false)
  @Enumerated(EnumType.STRING)
  private StorageType provider;

  @Column(name = "ACCESS_TOKEN", nullable = false)
  private String accessToken;
}
