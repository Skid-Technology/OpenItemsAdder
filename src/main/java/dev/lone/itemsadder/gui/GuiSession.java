package dev.lone.itemsadder.gui;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.network.NativePacketsHolder;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

final class GuiSession implements InventoryHolder {
   final GuiSessionManager aIo;
   final Player aIp;
   final Inventory aIq;
   final BiConsumer aIr;
   final Runnable aIs;
   ScrollTrackButton aIt;
   Component aIu;
   final AtomicBoolean aIv = new AtomicBoolean();
   int aIw = -1;
   boolean cz;
   boolean aIx;

   GuiSession(GuiSessionManager var1, Player var2, BiConsumer var3, Runnable var4) {
      this.aIo = var1;
      this.aIp = var2;
      this.aIr = var3;
      this.aIs = var4;
      this.aIt = var1.aIa.Mi();
      this.aIu = var1.aIc.MG();
      this.aIq = var1.aIe.method3552(this, var1.aId, var1x -> this.bZ(var1x) ? (this.aIw + var1x / 9) * 8 + var1x % 9 : -1);
   }

   public Inventory getInventory() {
      return this.aIq;
   }

   boolean bZ(int var1) {
      return this.aIw >= 0 && var1 >= 0 && var1 < 45 && var1 % 9 < 8 && (this.aIw + var1 / 9) * 8 + var1 % 9 < this.aIo.aIf * 8;
   }

   void method2439() {
      if (!this.cz) {
         this.cz = true;
         this.aIo.aIg.remove(this.aIp.getUniqueId(), this);
         this.aIo.LI();
         this.aIs.run();
         this.aIo.LD();
         Consumer var1 = this.aIo.aIj;
         if (this.aIx && var1 != null) {
            this.aIo.method2408("close", () -> var1.accept(this.aIp));
         }
      }
   }

   void LM() {
      if (this.aIo.aIf > 5) {
         this.aIq.setItem(8, this.aIt.method35(this.aIp, false, this.aIw > 0));

         for (int var1 = 0; var1 < 4; var1++) {
            this.aIq.setItem(17 + var1 * 9, this.aIt.method36(this.aIp, var1, this.aIw, this.aIo.aIf - 5, this.aIo.aIf));
         }

         this.aIq.setItem(53, this.aIt.method35(this.aIp, true, this.aIw < this.aIo.aIf - 5));
      }
   }

   void method2440(InventoryClickEvent var1) {
      int var2 = var1.getRawSlot();
      if (this.cz || this.aIo.aIg.get(this.aIp.getUniqueId()) != this) {
         var1.setCancelled(true);
      } else if (var2 >= 0 && var2 < 54 && !this.bZ(var2)) {
         var1.setCancelled(true);
         if (this.aIr != null && var2 < 45 && var2 % 9 == 8) {
            this.aIr.accept(var2 / 9, var1);
         } else if (this.aIr == null && this.aIo.aIf > 5 && var2 % 9 == 8) {
            boolean var9 = var1.isShiftClick();
            NativePacketsHolder.PE().IP().entity(Main.getInstance(), this.aIp, () -> {
               if (!this.cz && this.aIo.aIg.get(this.aIp.getUniqueId()) == this && this.aIp.getOpenInventory().getTopInventory() == this.aIq) {
                  int var3x = var2 == 8 ? -1 : (var2 == 53 ? 1 : ScrollableState.method2592(var2 / 9 - 1, 4, this.aIw, this.aIo.aIf - 5));
                  int var4x = Math.max(0, Math.min(this.aIw + ScrollableState.method2591(var3x, var9), this.aIo.aIf - 5));
                  if (var4x != this.aIw) {
                     this.aIw = var4x;
                     this.LM();
                     this.aIp.updateInventory();
                  }
               }
            }, () -> {});
         }
      } else {
         BiConsumer var3 = this.aIo.aIk;
         if (var3 != null) {
            int var4 = this.bZ(var2) ? (this.aIw + var2 / 9) * 8 + var2 % 9 : -1;
            if (!this.aIo.method2408("click", () -> var3.accept(var4, var1))) {
               var1.setCancelled(true);
            }

            if (var1.isCancelled()) {
               return;
            }
         }

         if (var1.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && var2 >= 54) {
            var1.setCancelled(true);
            ItemStack var11 = var1.getCurrentItem();
            if (!GuiSessionManager.method2410(var11)) {
               ItemStack var12 = var11.clone();
               this.method2441(var12);
               var1.setCurrentItem(GuiSessionManager.method2410(var12) ? null : var12);
            }
         } else if (var1.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            var1.setCancelled(true);
            ItemStack var10 = var1.getCursor();
            if (GuiSessionManager.method2410(var10)) {
               return;
            }

            int var5 = var10.getMaxStackSize();

            for (int var6 = 0; var6 < 90 && var10.getAmount() < var5; var6++) {
               if (var6 >= 54 || this.bZ(var6)) {
                  ItemStack var7 = var6 < 54 ? this.aIq.getItem(var6) : this.aIp.getInventory().getItem(var6 - 54);
                  if (!GuiSessionManager.method2410(var7) && var7.isSimilar(var10)) {
                     int var8 = Math.min(var5 - var10.getAmount(), var7.getAmount());
                     var10.setAmount(var10.getAmount() + var8);
                     var7.setAmount(var7.getAmount() - var8);
                     if (var6 < 54) {
                        this.aIq.setItem(var6, GuiSessionManager.method2410(var7) ? null : var7);
                     } else {
                        this.aIp.getInventory().setItem(var6 - 54, GuiSessionManager.method2410(var7) ? null : var7);
                     }
                  }
               }
            }

            this.aIp.setItemOnCursor(var10);
         }
      }
   }

   private void method2441(ItemStack var1) {
      for (int var2 = 0; var2 < 2 && !GuiSessionManager.method2410(var1); var2++) {
         for (int var3 = 0; var3 < 45 && !GuiSessionManager.method2410(var1); var3++) {
            if (this.bZ(var3)) {
               ItemStack var4 = this.aIq.getItem(var3);
               if ((var2 != 0 || !GuiSessionManager.method2410(var4) && var4.isSimilar(var1)) && (var2 != 1 || GuiSessionManager.method2410(var4))) {
                  int var5 = Math.min(var1.getMaxStackSize(), this.aIq.getMaxStackSize()) - (GuiSessionManager.method2410(var4) ? 0 : var4.getAmount());
                  int var6 = Math.max(0, Math.min(var5, var1.getAmount()));
                  if (var6 != 0) {
                     ItemStack var7 = var1.clone();
                     var7.setAmount(var6 + (GuiSessionManager.method2410(var4) ? 0 : var4.getAmount()));
                     this.aIq.setItem(var3, var7);
                     var1.setAmount(var1.getAmount() - var6);
                  }
               }
            }
         }
      }
   }
}
