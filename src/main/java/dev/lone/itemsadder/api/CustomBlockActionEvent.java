package dev.lone.itemsadder.api;

import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CustomBlockActionEvent extends Event implements Cancellable {
   Player li;
   Block aaU;
   CustomBlockDefinition agk;
   final CustomBlockChunkStoreBase agl;
   private static final HandlerList agm = new HandlerList();
   private boolean isCancelled;
   private boolean vG = true;
   private boolean agn;

   public CustomBlockActionEvent(Player var1, Block var2, CustomBlockDefinition var3, CustomBlockChunkStoreBase var4) {
      super(false);
      this.li = var1;
      this.aaU = var2;
      this.agk = var3;
      this.isCancelled = false;
      this.agl = var4;
   }

   @NotNull
   public HandlerList getHandlers() {
      return agm;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return agm;
   }

   public boolean isCancelled() {
      return this.isCancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.isCancelled = cancelled;
   }

   @NotNull
   public Player getPlayer() {
      return this.li;
   }

   public void aW(Player var1) {
      this.li = var1;
   }

   public Block getBlock() {
      return this.aaU;
   }

   public void method9651(Block var1) {
      this.aaU = var1;
   }

   public CustomBlockDefinition Bu() {
      return this.agk;
   }

   public void method9652(CustomBlockDefinition var1) {
      this.agk = var1;
   }

   public CustomBlockChunkStoreBase Bv() {
      return this.agl;
   }

   public void aI(boolean var1) {
      this.vG = var1;
   }

   public boolean Bw() {
      return this.vG;
   }

   public void aJ(boolean var1) {
      this.agn = var1;
   }

   public boolean Bx() {
      return this.agn;
   }
}
