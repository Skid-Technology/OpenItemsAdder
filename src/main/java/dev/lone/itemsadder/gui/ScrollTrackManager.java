package dev.lone.itemsadder.gui;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.network.ConnectionRecord;
import dev.lone.itemsadder.network.NativePacketsHolder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public final class ScrollTrackManager {
   static final String aKD = "_iainternal:gui/scroll_track";
   private ScrollTrackButton aKE = new ScrollTrackButton(null, null, "_iainternal:gui/scroll_track");

   public synchronized void method2593(ItemStack var1, ItemStack var2) {
      this.aKE = new ScrollTrackButton(ae(var1), ae(var2), this.aKE.uM());
   }

   public synchronized void method2594(NamespacedKey var1) {
      this.aKE = new ScrollTrackButton(this.aKE.Mj(), this.aKE.Mk(), var1 == null ? "_iainternal:gui/scroll_track" : var1.toString());
   }

   public synchronized void method2595(Plugin var1, NamespacedKey var2, NamespacedKey var3) {
      this.method2596(var1, var2, var3, null);
   }

   public synchronized void method2596(Plugin var1, NamespacedKey var2, NamespacedKey var3, NamespacedKey var4) {
      this.method2594(ScrollableGuiAssets.method6617(var1, var2, var3, var4));
   }

   String ca(Player var1) {
      String var2 = this.Mi().uM();
      if (!var2.startsWith("_iainternal:gui/scroll_track/custom/")) {
         return var2;
      }

      ConnectionRecord var3 = NativePacketsHolder.PE().cB(var1);
      return var3 != null && ContentManager.method8084().aL.manager.isCustomModelSupported(var1.getUniqueId(), var3.bl(), var2)
         ? var2
         : "_iainternal:gui/scroll_track";
   }

   synchronized ScrollTrackButton Mi() {
      return this.aKE;
   }

   private static ItemStack ae(ItemStack var0) {
      if (var0 == null) {
         return null;
      } else if (var0.getAmount() > 0 && var0.getType() != Material.AIR && var0.getType() != Material.CAVE_AIR && var0.getType() != Material.VOID_AIR) {
         return var0.clone();
      } else {
         throw new IllegalArgumentException("Navigation icons must not be empty");
      }
   }
}
