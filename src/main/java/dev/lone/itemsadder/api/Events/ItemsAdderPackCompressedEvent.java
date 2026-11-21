package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.PackEntry;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemsAdderPackCompressedEvent extends Event {
   private static final HandlerList HANDLERS_LIST = new HandlerList();
   private final Map entries;

   public ItemsAdderPackCompressedEvent() {
      this(new LinkedHashMap());
   }

   public ItemsAdderPackCompressedEvent(Map entries) {
      super(true);
      this.entries = Objects.requireNonNull(entries, "entries");
   }

   public Map getEntries() {
      return this.entries;
   }

   @Nullable
   public PackEntry getEntry(String path) {
      return (PackEntry)this.entries.get(path);
   }

   public void setEntry(String path, byte[] bytes) {
      this.entries.put(path, PackEntry.bytes(path, bytes));
   }

   public void setTextEntry(String path, String text) {
      this.setEntry(path, text.getBytes(StandardCharsets.UTF_8));
   }

   public void removeEntry(String path) {
      PackEntry var2 = (PackEntry)this.entries.remove(path);
      if (var2 != null) {
         var2.remove();
      }
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS_LIST;
   }

   public static HandlerList getHandlerList() {
      return HANDLERS_LIST;
   }
}
