

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "STORAGE_CONNECTION")
@Data
public class StorageConnection extends AbstractEntity {
  @Column(name = "PROVIDER", nullable = false)
  @Enumerated(EnumType.STRING)
  private StorageType provider;

  @Column(name = "ACCESS_TOKEN", nullable = false)
  private String accessToken;
}
