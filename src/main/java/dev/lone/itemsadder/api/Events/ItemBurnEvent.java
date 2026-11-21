package dev.lone.itemsadder.api.Events;

import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemBurnEvent extends Event {
   private static final HandlerList HANDLERS = new HandlerList();
   private final Item item;
   private final ItemStack itemStack;
   private final Block block;
   @Nullable
   private final String customFireNamespacedID;

   public ItemBurnEvent(Item item, @Nullable String customFireNamespacedID) {
      this.item = item;
      this.itemStack = item.getItemStack().clone();
      this.block = item.getLocation().getBlock();
      this.customFireNamespacedID = customFireNamespacedID;
   }

   public Item getItem() {
      return this.item;
   }

   public ItemStack getItemStack() {
      return this.itemStack.clone();
   }

   public Block getBlock() {
      return this.block;
   }

   @Nullable
   public String getCustomFireNamespacedID() {
      return this.customFireNamespacedID;
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return HANDLERS;
   }
}
