package dev.lone.itemsadder.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerHeldItemEvent extends Event implements Cancellable {
   private final ItemStack aHV;
   private final int aHW;
   private final Player aHX;
   private boolean cancelled = false;
   private static final HandlerList aHY = new HandlerList();

   public PlayerHeldItemEvent(Player var1, ItemStack var2, int var3) {
      super(true);
      this.aHX = var1;
      this.aHV = var2;
      this.aHW = var3;
   }

   public PlayerHeldItemEvent(Player var1) {
      this(var1, var1.getInventory().getItemInMainHand(), var1.getInventory().getHeldItemSlot());
   }

   public ItemStack getItemStack() {
      return this.aHV;
   }

   public int LC() {
      return this.aHW;
   }

   public Player getPlayer() {
      return this.aHX;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean canceled) {
      this.cancelled = true;
   }

   @NotNull
   public HandlerList getHandlers() {
      return aHY;
   }

   public static HandlerList getHandlerList() {
      return aHY;
   }
}
