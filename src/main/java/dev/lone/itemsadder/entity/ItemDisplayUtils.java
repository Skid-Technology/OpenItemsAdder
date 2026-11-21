package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.Utils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Nullable;

public class ItemDisplayUtils {
   private static ItemDisplay method6588(
      Player var0, @Nullable ItemDisplay var1, double var2, double var4, double var6, double var8, double var10, double var12
   ) {
      World var14 = var0.getWorld();
      boolean var15 = var1 != null;
      double var16 = (var2 + var8) / 2.0;
      double var18 = (var4 + var10) / 2.0;
      double var20 = (var6 + var12) / 2.0;
      double var22 = Math.sqrt(Math.pow(var8 - var2, 2.0) + Math.pow(var10 - var4, 2.0) + Math.pow(var12 - var6, 2.0));
      Location var24 = new Location(var14, var16, var18, var20);
      if (var1 == null) {
         var1 = NativePacketsHolder.PD().ac(var24);
         var1.setInvisible(true);
         ItemStack var25 = new ItemStack(Material.PAPER);
         ItemOperationsService.getRegistryBridge().method3437(var25, "minecraft:block_outline");
         ItemOperationsService.getRegistryBridge().method3438(var25, Utils.parseBukkitColor("383838"));
         var1.setItemStack(var25);
         var1.setTeleportDuration(0);
         var1.setInterpolationDuration(2);
      }

      if (var0.getWorld().getTime() >= 13000L && var0.getWorld().getTime() <= 23500L) {
         var1.setBrightness(new Brightness(8, 8));
      } else {
         var1.setBrightness(new Brightness(15, 15));
      }

      double var51 = var8 - var2;
      double var27 = var10 - var4;
      double var29 = var12 - var6;
      boolean var31 = Math.abs(var51) >= Math.abs(var27) && Math.abs(var51) >= Math.abs(var29);
      boolean var32 = Math.abs(var27) >= Math.abs(var51) && Math.abs(var27) >= Math.abs(var29);
      boolean var33 = Math.abs(var29) >= Math.abs(var51) && Math.abs(var29) >= Math.abs(var27);
      double var34 = var0.getEyeLocation().distance(var24);
      double var36 = 1.0;
      double var38 = 4.0;
      double var40 = 0.0018;
      double var42 = 0.016;
      double var44 = Math.max(var36, Math.min(var38, var34));
      double var46 = (var44 - var36) / (var38 - var36);
      double var48 = var40 + var46 * (var42 - var40);
      var1.setBillboard(Billboard.FIXED);
      Transformation var50 = var1.getTransformation();
      if (var31) {
         var50.getScale().set((float)var22, var48, var48);
      } else if (var32) {
         var50.getScale().set(var48, (float)var22, var48);
      } else if (var33) {
         var50.getScale().set((float)var22, var48, var48);
         var24.setYaw(90.0F);
      }

      var1.setTransformation(var50);
      if (var15) {
         NativePacketsHolder.PD().setVisibilityFor(var1, var0, true);
         NativePacketsHolder.PD().teleportFor(var1, var0, var24);
      } else {
         NativePacketsHolder.PD().mountFor(var1, var0);
         NativePacketsHolder.PD().setVisibilityFor(var1, var0, true);
         NativePacketsHolder.PD().teleportFor(var1, var0, var24);
      }

      return var1;
   }

   public static void method6589(Player var0, BoundingBox var1, ObjectArrayList var2) {
      double var3 = var1.getMinX();
      double var5 = var1.getMinY();
      double var7 = var1.getMinZ();
      double var9 = var1.getMaxX();
      double var11 = var1.getMaxY();
      double var13 = var1.getMaxZ();
      var2.add(method6588(var0, null, var3, var5, var7, var9, var5, var7));
      var2.add(method6588(var0, null, var3, var5, var7, var3, var5, var13));
      var2.add(method6588(var0, null, var9, var5, var7, var9, var5, var13));
      var2.add(method6588(var0, null, var3, var5, var13, var9, var5, var13));
      var2.add(method6588(var0, null, var3, var11, var7, var9, var11, var7));
      var2.add(method6588(var0, null, var3, var11, var7, var3, var11, var13));
      var2.add(method6588(var0, null, var9, var11, var7, var9, var11, var13));
      var2.add(method6588(var0, null, var3, var11, var13, var9, var11, var13));
      var2.add(method6588(var0, null, var3, var5, var7, var3, var11, var7));
      var2.add(method6588(var0, null, var9, var5, var7, var9, var11, var7));
      var2.add(method6588(var0, null, var3, var5, var13, var3, var11, var13));
      var2.add(method6588(var0, null, var9, var5, var13, var9, var11, var13));
   }

   public static void method6590(Player var0, BoundingBox var1, ObjectArrayList var2) {
      double var3 = var1.getMinX();
      double var5 = var1.getMinY();
      double var7 = var1.getMinZ();
      double var9 = var1.getMaxX();
      double var11 = var1.getMaxY();
      double var13 = var1.getMaxZ();
      method6588(var0, (ItemDisplay)var2.get(0), var3, var5, var7, var9, var5, var7);
      method6588(var0, (ItemDisplay)var2.get(1), var3, var5, var7, var3, var5, var13);
      method6588(var0, (ItemDisplay)var2.get(2), var9, var5, var7, var9, var5, var13);
      method6588(var0, (ItemDisplay)var2.get(3), var3, var5, var13, var9, var5, var13);
      method6588(var0, (ItemDisplay)var2.get(4), var3, var11, var7, var9, var11, var7);
      method6588(var0, (ItemDisplay)var2.get(5), var3, var11, var7, var3, var11, var13);
      method6588(var0, (ItemDisplay)var2.get(6), var9, var11, var7, var9, var11, var13);
      method6588(var0, (ItemDisplay)var2.get(7), var3, var11, var13, var9, var11, var13);
      method6588(var0, (ItemDisplay)var2.get(8), var3, var5, var7, var3, var11, var7);
      method6588(var0, (ItemDisplay)var2.get(9), var9, var5, var7, var9, var11, var7);
      method6588(var0, (ItemDisplay)var2.get(10), var3, var5, var13, var3, var11, var13);
      method6588(var0, (ItemDisplay)var2.get(11), var9, var5, var13, var9, var11, var13);
   }
}
