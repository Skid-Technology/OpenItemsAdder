package dev.lone.itemsadder.gui;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.network.ConnectionRecord;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.resourcepack.ResourcePackManager;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ScrollTrackButton {
   private final ItemStack aKF;
   private final ItemStack aKG;
   private final String aKH;

   ScrollTrackButton(ItemStack var1, ItemStack var2, String var3) {
      this.aKF = var1;
      this.aKG = var2;
      this.aKH = var3;
   }

   ItemStack method35(Player var1, boolean var2, boolean var3) {
      ItemStack var4 = var2 ? this.aKG : this.aKF;
      if (var4 != null) {
         return var4.clone();
      }

      ItemStack var5 = method38(var1, "_iainternal:gui/scroll_track", (var2 ? "down" : "up") + (var3 ? "_enabled" : "_disabled"), Material.ARROW);
      ItemMeta var6 = var5.getItemMeta();
      var6.setDisplayName(Main.aFi.lj(var2 ? "ia-scrollbar-down" : "ia-scrollbar-up"));
      var6.setLore(List.of(Main.aFi.lj("ia-scrollbar-page")));
      var5.setItemMeta(var6);
      return var5;
   }

   ItemStack method36(Player var1, int var2, int var3, int var4, int var5) {
      int var6 = ScrollableState.method2590(var2, var3, var4, 72);
      return this.method37(var1, var6, var3, var5, 5);
   }

   ItemStack method37(Player var1, int var2, int var3, int var4, int var5) {
      boolean var6 = var2 >= -9 && var2 <= 17;
      String var7 = this.aKH.startsWith("_iainternal:gui/scroll_track/custom/") ? this.aKH : "_iainternal:gui/scroll_track";
      ItemStack var8 = method38(var1, var7, var6 ? "offset_" + var2 : "track", var6 ? Material.LIGHT_GRAY_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE);
      ItemMeta var9 = var8.getItemMeta();
      var9.setDisplayName(
         Main.aFi
            .lj("ia-scrollbar-position")
            .replace("{first}", Integer.toString(var3 + 1))
            .replace("{last}", Integer.toString(Math.min(var4, var3 + var5)))
            .replace("{total}", Integer.toString(var4))
      );
      var9.setLore(List.of(Main.aFi.lj("ia-scrollbar-click"), Main.aFi.lj("ia-scrollbar-page")));
      var8.setItemMeta(var9);
      return var8;
   }

   private static ItemStack method38(Player var0, String var1, String var2, Material var3) {
      ConnectionRecord var4 = NativePacketsHolder.PE().cB(var0);
      ResourcePackManager var5 = ContentManager.method8084().aL.manager;
      int var6 = var4 == null ? -1 : var5.getScrollableModelVersion(var0.getUniqueId(), var4.bl(), var1.replace(":", ":item/") + "/" + var2);
      if (var6 < 0 && !var1.equals("_iainternal:gui/scroll_track") && var4 != null) {
         var6 = var5.getScrollableModelVersion(var0.getUniqueId(), var4.bl(), "_iainternal:gui/scroll_track".replace(":", ":item/") + "/" + var2);
      }

      ItemStack var7 = new ItemStack(var6 > 0 ? Material.PAPER : var3);
      if (var6 > 0) {
         ItemMeta var8 = var7.getItemMeta();
         var8.setCustomModelData(var6);
         var7.setItemMeta(var8);
      }

      return var7;
   }

   public ItemStack Mj() {
      return this.aKF;
   }

   public ItemStack Mk() {
      return this.aKG;
   }

   public String uM() {
      return this.aKH;
   }
}
