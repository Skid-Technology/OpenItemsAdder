package dev.lone.itemsadder.api.Events;

import java.io.OutputStream;

@FunctionalInterface
public interface ItemsAdderPackCompressedEvent$EntryWriter {
   void writeTo(OutputStream var1);
}
