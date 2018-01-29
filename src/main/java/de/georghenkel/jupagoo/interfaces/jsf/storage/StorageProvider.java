package de.georghenkel.jupagoo.interfaces.jsf.storage;

import de.georghenkel.jupagoo.domain.storage.model.StorageType;

public enum StorageProvider {
  DROPBOX(StorageType.DROPBOX, "icons/dropbox.png"), GOOGLE_DRIVE(StorageType.GOOGLE_DRIVE,
      "icons/google_drive.png"), BOX(StorageType.BOX, "icons/box.png");

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
