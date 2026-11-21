package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent.Cause;
import lombok.Generated;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ItemsAdderLoadDataEvent extends Event {
   private static final HandlerList HANDLERS_LIST = new HandlerList();
   private final Cause cause;
   private final boolean skipRecipes;

   @Deprecated
   public ItemsAdderLoadDataEvent(boolean isReload) {
      this.cause = isReload ? Cause.RELOAD : Cause.FIRST_LOAD;
      this.skipRecipes = false;
   }

   public ItemsAdderLoadDataEvent(boolean isReload, boolean skipRecipes) {
      this.cause = isReload ? Cause.RELOAD : Cause.FIRST_LOAD;
      this.skipRecipes = skipRecipes;
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS_LIST;
   }

   public static HandlerList getHandlerList() {
      return HANDLERS_LIST;
   }

   @Generated
   public Cause getCause() {
      return this.cause;
   }

   @Generated
   public boolean isSkipRecipes() {
      return this.skipRecipes;
   }
}
