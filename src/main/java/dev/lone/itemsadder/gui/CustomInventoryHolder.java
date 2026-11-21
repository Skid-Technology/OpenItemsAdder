package dev.lone.itemsadder.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CustomInventoryHolder implements InventoryHolder {
   private final Map bPY = new HashMap();
   private final List bPZ = new ArrayList();
   private final List bQa = new ArrayList();
   private final List bQb = new ArrayList();
   private final List bQc = new ArrayList();
   private final Inventory bQd;
   private Predicate bQe;

   public CustomInventoryHolder(int var1) {
      this(var1x -> Bukkit.createInventory(var1x, var1));
   }

   public CustomInventoryHolder(int var1, String var2) {
      this(var2x -> Bukkit.createInventory(var2x, var1, var2));
   }

   public CustomInventoryHolder(InventoryType var1) {
      this(var1x -> Bukkit.createInventory(var1x, var1));
   }

   public CustomInventoryHolder(InventoryType var1, String var2) {
      this(var2x -> Bukkit.createInventory(var2x, var1, var2));
   }

   public CustomInventoryHolder(Function var1) {
      Objects.requireNonNull(var1, "inventoryFunction");
      Inventory var2 = (Inventory)var1.apply(this);
      if (var2.getHolder() != this) {
         throw new IllegalStateException("Inventory holder is not FastInv, found: " + var2.getHolder());
      }

      this.bQd = var2;
   }

   protected void method2669(InventoryOpenEvent var1) {
   }

   protected void method2670(InventoryClickEvent var1) {
   }

   protected void method2671(InventoryDragEvent var1) {
   }

   protected void method2672(InventoryCloseEvent var1) {
   }

   public void aO(ItemStack var1) {
      this.method2667(var1, null);
   }

   public void method2667(ItemStack var1, Consumer var2) {
      int var3 = this.bQd.firstEmpty();
      if (var3 >= 0) {
         this.method2666(var3, var1, var2);
      }
   }

   public void setItem(int slot, ItemStack item) {
      this.method2666(slot, item, null);
   }

   public void method2666(int var1, ItemStack var2, Consumer var3) {
      this.bQd.setItem(var1, var2);
      if (var3 != null) {
         this.bPY.put(var1, var3);
      } else {
         this.bPY.remove(var1);
      }
   }

   public void method2673(int var1, int var2, ItemStack var3) {
      this.method2674(var1, var2, var3, null);
   }

   public void method2674(int var1, int var2, ItemStack var3, Consumer var4) {
      for (int var5 = var1; var5 < var2; var5++) {
         this.method2666(var5, var3, var4);
      }
   }

   public void method2675(int[] var1, ItemStack var2) {
      this.method2676(var1, var2, null);
   }

   public void method2676(int[] var1, ItemStack var2, Consumer var3) {
      for (int var7 : var1) {
         this.method2666(var7, var2, var3);
      }
   }

   public void method2677(Iterable var1, ItemStack var2) {
      this.method2678(var1, var2, null);
   }

   public void method2678(Iterable var1, ItemStack var2, Consumer var3) {
      for (Integer var5 : var1) {
         this.method2666(var5, var2, var3);
      }
   }

   public void cq(int var1) {
      this.bQd.clear(var1);
      this.bPY.remove(var1);
   }

   public void method2679(int... var1) {
      for (int var5 : var1) {
         this.cq(var5);
      }
   }

   public void MF() {
      this.bQd.clear();
      this.bPY.clear();
   }

   public void method2680(Predicate var1) {
      this.bQe = var1;
   }

   public void method2681(Consumer var1) {
      this.bPZ.add(var1);
   }

   public void method2682(Consumer var1) {
      this.bQa.add(var1);
   }

   public void method2683(Consumer var1) {
      this.bQb.add(var1);
   }

   public void method2684(Consumer var1) {
      this.bQc.add(var1);
   }

   public void open(Player player) {
      Objects.requireNonNull(player, "player").openInventory(this.bQd);
   }

   public int[] adZ() {
      int var1 = this.bQd.getSize();
      return IntStream.range(0, var1).filter(var1x -> var1 < 27 || var1x < 9 || var1x % 9 == 0 || (var1x - 8) % 9 == 0 || var1x > var1 - 9).toArray();
   }

   public int[] aea() {
      int var1 = this.bQd.getSize();
      return IntStream.range(0, var1)
         .filter(
            var1x -> var1x < 2 || var1x > 6 && var1x < 10 || var1x == 17 || var1x == var1 - 18 || var1x > var1 - 11 && var1x < var1 - 7 || var1x > var1 - 3
         )
         .toArray();
   }

   public Inventory getInventory() {
      return this.bQd;
   }

   void method2685(InventoryOpenEvent var1) {
      this.method2669(var1);
      this.bPZ.forEach(var1x -> var1x.accept(var1));
   }

   boolean method2686(InventoryCloseEvent var1) {
      this.method2672(var1);
      this.bQa.forEach(var1x -> var1x.accept(var1));
      return this.bQe != null && this.bQe.test((Player)var1.getPlayer());
   }

   void method2687(InventoryClickEvent var1) {
      this.method2670(var1);
      this.bQb.forEach(var1x -> var1x.accept(var1));
      Consumer var2 = (Consumer)this.bPY.get(var1.getRawSlot());
      if (var2 != null) {
         var2.accept(var1);
      }
   }

   void method2688(InventoryDragEvent var1) {
      this.method2671(var1);
      this.bQc.forEach(var1x -> var1x.accept(var1));
   }
}
