package de.georghenkel.jupagoo.interfaces.jsf.demo;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class HelloBean {

  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public void sayHi() {
    this.message = this.message + " received at " + LocalDateTime.now();
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 13 * hash + Objects.hashCode(this.message);
    return hash;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final HelloBean other = (HelloBean) obj;
    if (!Objects.equals(this.message, other.message)) {
      return false;
    }
    return true;
  }
}
