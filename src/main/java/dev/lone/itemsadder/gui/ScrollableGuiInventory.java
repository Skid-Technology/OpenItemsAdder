package dev.lone.itemsadder.gui;

import dev.lone.itemsadder.network.ContainerConnection;
import dev.lone.itemsadder.network.ContainerPacketListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class ScrollableGuiInventory extends CustomInventoryHolder {
   private final Map aLI = new HashMap();
   private final Map aLJ = new HashMap();
   private final Map aLK = new ConcurrentHashMap();
   private final Inventory aLL;
   private ContainerConnection aLM;

   ScrollableGuiInventory(String var1) {
      super(54, var1);
      this.aLL = Bukkit.createInventory(new GuiInventoryHolder(this), 54, var1);
   }

   @Override
   public Inventory getInventory() {
      return this.aLL;
   }

   @Override
   public void open(Player player) {
      player.openInventory(this.aLL);
   }

   @Override
   public void method2666(int slot, ItemStack item, Consumer click) {
      if (item != null && !item.getType().isAir()) {
         ItemStack var4 = item.clone();
         this.aLJ.put(slot, var4);
         this.aLK.put(slot, ContainerPacketListener.aKI.aKK.an(var4));
         if (click == null) {
            this.aLI.remove(slot);
         } else {
            this.aLI.put(slot, click);
         }
      } else {
         this.cq(slot);
      }
   }

   @Override
   public void cq(int slot) {
      this.aLJ.remove(slot);
      this.aLK.remove(slot);
      this.aLI.remove(slot);
   }

   @Override
   public void method2667(ItemStack item, Consumer click) {
      for (int var3 = 0; var3 < 54; var3++) {
         if (!this.aLJ.containsKey(var3)) {
            this.method2666(var3, item, click);
            return;
         }
      }
   }

   @Override
   public void MF() {
      this.aLJ.clear();
      this.aLK.clear();
      this.aLI.clear();
   }

   void method2668(int var1, InventoryClickEvent var2) {
      Consumer var3 = (Consumer)this.aLI.get(var1);
      if (var3 != null) {
         GuiClickEvent var4 = new GuiClickEvent(this, var2.getView(), var2.getSlotType(), var1, var2.getClick(), var2.getAction(), var2.getHotbarButton(), var1);
         var4.setCancelled(true);
         var3.accept(var4);
      }
   }
}
