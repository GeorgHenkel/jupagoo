package de.georghenkel.jupagoo.application.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public abstract class AbstractEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Column(name = "ID")
  private Long id;
}
