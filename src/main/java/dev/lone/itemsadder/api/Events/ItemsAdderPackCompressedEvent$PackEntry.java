package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.EntryWriter;
import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.InputStreamSupplier;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;

public class ItemsAdderPackCompressedEvent$PackEntry {
   private final String path;
   @Nullable
   private InputStreamSupplier inputStreamSupplier;
   @Nullable
   private EntryWriter writer;
   @Nullable
   private byte[] bytes;
   private boolean removed;

   private ItemsAdderPackCompressedEvent$PackEntry(String path, @Nullable InputStreamSupplier inputStreamSupplier, @Nullable EntryWriter writer) {
      this.path = Objects.requireNonNull(path, "path");
      this.inputStreamSupplier = inputStreamSupplier;
      this.writer = writer;
   }

   public static ItemsAdderPackCompressedEvent$PackEntry stream(String path, InputStreamSupplier inputStreamSupplier) {
      return new ItemsAdderPackCompressedEvent$PackEntry(path, Objects.requireNonNull(inputStreamSupplier, "inputStreamSupplier"), null);
   }

   public static ItemsAdderPackCompressedEvent$PackEntry writer(String path, EntryWriter writer) {
      return new ItemsAdderPackCompressedEvent$PackEntry(path, null, Objects.requireNonNull(writer, "writer"));
   }

   public static ItemsAdderPackCompressedEvent$PackEntry bytes(String path, byte[] bytes) {
      ItemsAdderPackCompressedEvent$PackEntry var2 = new ItemsAdderPackCompressedEvent$PackEntry(path, null, null);
      var2.setBytes(bytes);
      return var2;
   }

   public String getPath() {
      return this.path;
   }

   public InputStream openStream() {
      if (this.bytes != null) {
         return new ByteArrayInputStream(this.bytes);
      } else if (this.inputStreamSupplier != null) {
         return this.inputStreamSupplier.openStream();
      } else if (this.writer != null) {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         this.writer.writeTo(var1);
         return new ByteArrayInputStream(var1.toByteArray());
      } else {
         return InputStream.nullInputStream();
      }
   }

   public byte[] getBytes() {
      try (InputStream var1 = this.openStream()) {
         return var1.readAllBytes();
      }
   }

   public String getText() {
      return new String(this.getBytes(), StandardCharsets.UTF_8);
   }

   public void setBytes(byte[] bytes) {
      this.bytes = Objects.requireNonNull(bytes, "bytes");
      this.inputStreamSupplier = null;
      this.writer = null;
      this.removed = false;
   }

   public void setStream(InputStreamSupplier supplier) {
      this.inputStreamSupplier = Objects.requireNonNull(supplier, "supplier");
      this.writer = null;
      this.bytes = null;
      this.removed = false;
   }

   public void setText(String text) {
      this.setBytes(text.getBytes(StandardCharsets.UTF_8));
   }

   public boolean isRemoved() {
      return this.removed;
   }

   public void remove() {
      this.removed = true;
   }
}
