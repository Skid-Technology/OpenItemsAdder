package dev.lone.itemsadder.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PaginatedInventoryHolder extends CustomInventoryHolder {
   private final List bQm = new ArrayList();
   private final List bQn = new ArrayList();
   private List bQo;
   private int aJw = 1;
   private IntFunction bQp;
   private IntFunction bQq;
   private int bQr = -1;
   private int bQs = -1;

   public PaginatedInventoryHolder(int var1) {
      this(var1x -> Bukkit.createInventory(var1x, var1));
   }

   public PaginatedInventoryHolder(int var1, String var2) {
      this(var2x -> Bukkit.createInventory(var2x, var1, var2));
   }

   public PaginatedInventoryHolder(InventoryType var1) {
      this(var1x -> Bukkit.createInventory(var1x, var1));
   }

   public PaginatedInventoryHolder(InventoryType var1, String var2) {
      this(var2x -> Bukkit.createInventory(var2x, var1, var2));
   }

   public PaginatedInventoryHolder(Function var1) {
      super(var1x -> (Inventory)var1.apply((PaginatedInventoryHolder)var1x));
      this.bQo = IntStream.range(0, Math.max(9, this.getInventory().getSize() - 9)).boxed().collect(Collectors.toList());
   }

   public void aQ(ItemStack var1) {
      this.method2699(var1, null);
   }

   public void method2699(ItemStack var1, Consumer var2) {
      this.bQm.add(var1);
      this.bQn.add(var2);
   }

   public void method2700(Collection var1) {
      this.method2701(var1, Collections.nCopies(var1.size(), null));
   }

   public void method2701(Collection var1, Collection var2) {
      Objects.requireNonNull(var1, "content");
      Objects.requireNonNull(var2, "handlers");
      if (var1.size() != var2.size()) {
         throw new IllegalArgumentException("The content and handlers lists must have the same size");
      }

      this.bQm.addAll(var1);
      this.bQn.addAll(var2);
   }

   public void method2702(int var1, ItemStack var2) {
      this.method2703(var1, var2, null);
   }

   public void method2703(int var1, ItemStack var2, Consumer var3) {
      this.bQm.set(var1, var2);
      this.bQn.set(var1, var3);
   }

   public void ax(List var1) {
      this.clearContent();
      this.method2700(var1);
   }

   public void method2704(Collection var1, Collection var2) {
      Objects.requireNonNull(var1, "content");
      Objects.requireNonNull(var2, "handlers");
      if (var1.size() != var2.size()) {
         throw new IllegalArgumentException("The content and handlers lists must have the same size");
      }

      this.clearContent();
      this.method2701(var1, var2);
   }

   public void clearContent() {
      this.bQm.clear();
      this.bQn.clear();
   }

   public void aeg() {
      this.ei(this.aJw - 1);
   }

   public void aeh() {
      this.ei(this.aJw + 1);
   }

   public void ei(int var1) {
      int var2 = this.aej();
      this.aJw = Math.max(1, Math.min(var1, var2));
      int var3 = this.bQo.size() * (this.aJw - 1);

      for (int var5 : this.bQo) {
         if (var3 >= this.bQm.size()) {
            this.cq(var5);
         } else {
            this.method2666(var5, (ItemStack)this.bQm.get(var3), (Consumer)this.bQn.get(var3++));
         }
      }

      if (this.aJw > 1 && this.bQp != null) {
         this.method2666(this.bQr, (ItemStack)this.bQp.apply(this.aJw - 1), var1x -> this.aeg());
      } else if (this.bQr >= 0) {
         this.cq(this.bQr);
      }

      if (this.aJw < var2 && this.bQq != null) {
         this.method2666(this.bQs, (ItemStack)this.bQq.apply(this.aJw + 1), var1x -> this.aeh());
      } else if (this.bQs >= 0) {
         this.cq(this.bQs);
      }

      this.ej(var1);
   }

   public void ay(List var1) {
      this.bQo = Objects.requireNonNull(var1, "contentSlots");
   }

   public void method2705(int var1, IntFunction var2) {
      if (var1 >= 0 && var1 < this.getInventory().getSize()) {
         this.bQr = var1;
         this.bQp = var2;
      } else {
         throw new IllegalArgumentException("Invalid slot: " + var1);
      }
   }

   public void method2706(int var1, ItemStack var2) {
      this.method2705(var1, var1x -> var2);
   }

   public void method2707(int var1, IntFunction var2) {
      if (var1 >= 0 && var1 < this.getInventory().getSize()) {
         this.bQs = var1;
         this.bQq = var2;
      } else {
         throw new IllegalArgumentException("Invalid slot: " + var1);
      }
   }

   public void method2708(int var1, ItemStack var2) {
      this.method2707(var1, var1x -> var2);
   }

   @Override
   public void open(Player player) {
      this.ei(this.aJw);
      super.open(player);
   }

   protected void ej(int var1) {
   }

   public int aei() {
      return this.aJw;
   }

   public int aej() {
      int var1 = this.bQm.size() / this.bQo.size();
      int var2 = this.bQm.size() % this.bQo.size();
      return var2 == 0 ? var1 : var1 + 1;
   }

   public boolean aek() {
      return this.aJw == 1;
   }

   public boolean ael() {
      return this.aJw == this.aej();
   }
}
