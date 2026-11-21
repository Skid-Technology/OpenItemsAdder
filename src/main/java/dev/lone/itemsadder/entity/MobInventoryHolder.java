package dev.lone.itemsadder.entity;

import org.bukkit.entity.Mob;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class MobInventoryHolder implements InventoryHolder {
   private final Mob aaj;
   private Inventory aak;

   MobInventoryHolder(Mob var1) {
      this.aaj = var1;
   }

   public Mob getMob() {
      return this.aaj;
   }

   public Inventory getInventory() {
      return this.aak;
   }
}
