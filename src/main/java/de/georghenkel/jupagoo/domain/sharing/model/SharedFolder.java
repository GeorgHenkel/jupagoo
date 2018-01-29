package de.georghenkel.jupagoo.domain.sharing.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import de.georghenkel.jupagoo.application.model.AbstractEntity;
import de.georghenkel.jupagoo.domain.storage.model.StorageConnection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "SHARED_FOLDER")
@Getter
@Setter
@ToString(callSuper = true)
public class SharedFolder extends AbstractEntity {
  private static final long serialVersionUID = 1L;

  @Column(name = "NAME", unique = true, nullable = false)
  private String name;

  @Column(name = "ACCESS_KEY", unique = true, nullable = false)
  private String accessKey;

  @Column(name = "CREATION_DATE", nullable = false)
  private LocalDateTime creationDate;

  @Column(name = "ACCESS_VALID_UNTIL")
  private LocalDate accessValidUntil;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DEFAULT_STORAGE_ID")
  private StorageConnection defaultStorage;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "FALLBACK_STORAGE_ID")
  private StorageConnection fallbackStorage;
}
