package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.api.Events.TradeMachineEvent.Type;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemMerchantAction;
import java.util.List;
import lombok.Generated;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.MerchantRecipe;

public abstract class TradeMachineEvent extends PlayerEvent {
   private final ItemMerchantAction internal;
   private final Type type;
   private List merchantRecipes;

   public TradeMachineEvent(Player player, ItemMerchantAction internal, CustomItemDefinition item, List merchantRecipes) {
      super(player, false);
      this.internal = internal;
      this.merchantRecipes = merchantRecipes;
      if (item instanceof CustomBlockDefinition) {
         this.type = Type.BLOCK;
      } else if (item.aoh.hasAction("furniture")) {
         this.type = Type.FURNITURE;
      } else {
         if (!item.aoh.hasAction("complex_furniture")) {
            throw new IllegalArgumentException("The provided item does not have a valid behaviour for this event.");
         }

         this.type = Type.COMPLEX_FURNITURE;
      }
   }

   public String getNamespacedID() {
      return this.internal.aeT.getNamespacedID();
   }

   public String getTitle() {
      return this.internal.getTitle();
   }

   public String getPermission() {
      return this.internal.getPermission();
   }

   public List getRecipes() {
      return this.merchantRecipes;
   }

   public void setRecipes(List recipes) {
      this.merchantRecipes = recipes;
   }

   @Generated
   public Type getType() {
      return this.type;
   }
}
