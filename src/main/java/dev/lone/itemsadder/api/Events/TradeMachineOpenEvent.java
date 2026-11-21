package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemMerchantAction;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

public class TradeMachineOpenEvent extends TradeMachineEvent {
   private static final HandlerList HANDLERS_LIST = new HandlerList();

   public TradeMachineOpenEvent(Player player, ItemMerchantAction internal, CustomItemDefinition item, List merchantRecipes) {
      super(player, internal, item, merchantRecipes);
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
