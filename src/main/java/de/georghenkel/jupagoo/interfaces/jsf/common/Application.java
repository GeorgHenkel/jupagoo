package de.georghenkel.jupagoo.interfaces.jsf.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.inject.Named;

@Named("app")
public class Application {
  public String currentDate(@Nullable final String format) {
    String dateFormat = Optional.of(format).orElse("dd.MM.yyyy");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
    return LocalDate.now().format(formatter);
  }
}
