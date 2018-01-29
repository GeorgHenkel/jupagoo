package de.georghenkel.jupagoo.interfaces.jsf.storage;

import org.junit.Assert;
import org.junit.Test;

public class StorageProviderEnumTest {
  @Test
  public void shouldLoadDropboxProviderEnum() {
    String messageKex = StorageProvider.DROPBOX.getMessageKey();
    Assert.assertEquals("StorageProvider." + StorageProvider.DROPBOX.name(), messageKex);
  }
}
