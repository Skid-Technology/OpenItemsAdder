package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.util.EventRegistrar;
import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public interface PlayerInteractionHandler {
   default boolean isAvailable() {
      return false;
   }

   default void runOnMainThread(Runnable callback) {
      throw new UnsupportedOperationException("Native mining event ordering is unavailable");
   }

   int cx(Player var1);

   void interactEntity(Player var1, Entity var2);

   void interactEntityWithSlot(Player var1, Entity var2, EquipmentSlot var3);

   default void interactEntityAt(Player var1, Entity var2, Vector var3, EquipmentSlot var4) {
      PlayerInteractAtEntityEvent var5 = new PlayerInteractAtEntityEvent(var1, var2, var3, var4);
      if (EventRegistrar.callEvent(var5)) {
         this.interactEntityWithSlot(var1, var2, var4);
      }
   }

   default void interactEntityWithSlotDup2(Player var1, Entity var2, EquipmentSlot var3) {
      PlayerInteractEntityEvent var4 = new PlayerInteractEntityEvent(var1, var2, var3);
      if (EventRegistrar.callEvent(var4)) {
         this.interactEntityWithSlot(var1, var2, var3);
      }
   }

   default void dropItems(Player bukkitPlayer, @NotNull Collection completions) {
   }

   default void dropItemsDup2(Player bukkitPlayer, @NotNull Collection completions) {
   }

   void setReach(Player var1, float var2);

   void be(Player var1);

   double method5269(Player var1, double var2);

   void cH(Player var1);

   boolean placeBlock(Player var1, EquipmentSlot var2, Block var3, Location var4, BlockFace var5);

   void consumeItem(Player var1, ItemStack var2, int var3);

   void swingHand(Player var1, EquipmentSlot var2);

   default void interactBlock(Player var1, Block var2, Block var3) {
      BlockPlaceEvent var4 = new BlockPlaceEvent(var2, var2.getState(), var3, var1.getInventory().getItemInMainHand(), var1, true);
      EventRegistrar.callEvent(var4);
   }
}
