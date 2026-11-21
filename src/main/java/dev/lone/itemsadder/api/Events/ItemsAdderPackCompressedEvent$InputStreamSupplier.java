package dev.lone.itemsadder.api.Events;

import java.io.InputStream;

@FunctionalInterface
public interface ItemsAdderPackCompressedEvent$InputStreamSupplier {
   InputStream openStream();
}
