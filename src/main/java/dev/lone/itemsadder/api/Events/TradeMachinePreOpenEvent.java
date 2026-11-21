package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemMerchantAction;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

public class TradeMachinePreOpenEvent extends TradeMachineEvent implements Cancellable {
   private static final HandlerList HANDLERS_LIST = new HandlerList();
   private boolean isCancelled;

   public TradeMachinePreOpenEvent(Player player, ItemMerchantAction internal, CustomItemDefinition item, List merchantRecipes) {
      super(player, internal, item, merchantRecipes);
   }

   public boolean isCancelled() {
      return this.isCancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.isCancelled = cancelled;
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS_LIST;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return HANDLERS_LIST;
   }
}
