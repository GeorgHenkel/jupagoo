package de.georghenkel.jupagoo.interfaces.jsf.storage;

import de.georghenkel.jupagoo.domain.storage.model.StorageType;

public enum StorageProvider {
  DROPBOX(StorageType.DROPBOX, "fa-dropbox"), GOOGLE_DRIVE(StorageType.GOOGLE_DRIVE,
      "fa-google"), BOX(StorageType.BOX, "fa-cloud");

  private StorageType type;
  private String icon;

  private StorageProvider(final StorageType type, final String icon) {
    this.type = type;
    this.icon = icon;
  }

  public StorageType getType() {
    return type;
  }

  public String getMessageKey() {
    return "StorageProvider." + name();
  }

  public String getIcon() {
    return icon;
  }
}
