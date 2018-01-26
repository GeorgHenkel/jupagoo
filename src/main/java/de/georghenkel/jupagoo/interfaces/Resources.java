package de.georghenkel.jupagoo.interfaces;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Dependent
public class Resources {
  @Produces
  public Logger getLogger(final InjectionPoint p) {
    return LoggerFactory.getLogger(p.getMember().getDeclaringClass().getName());
  }
}
