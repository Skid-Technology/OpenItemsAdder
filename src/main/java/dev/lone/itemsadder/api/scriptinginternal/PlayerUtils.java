package dev.lone.itemsadder.api.scriptinginternal;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.command.RemoveCommand;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.RayTraceTarget;
import dev.lone.itemsadder.util.RayTraceWalker;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class PlayerUtils {
   public static boolean isHeld(Player player, String namespacedId) {
      ItemStack var2 = player.getItemInHand();
      CustomItemDefinition var3 = ContentManager.method8084().method8149(var2);
      if (var3 == null) {
         return false;
      }

      CustomItemDefinition var4 = ContentManager.method8084().method8154(namespacedId);
      return var4 == null ? false : var4.method8778(var3);
   }

   public static boolean isHeld(Player player, CustomStack customStack) {
      ItemStack var2 = player.getItemInHand();
      CustomItemDefinition var3 = ContentManager.method8084().method8149(var2);
      return var3 == null ? false : var3.method8778(customStack.internal());
   }

   public static boolean isHeld(Player player, Material material) {
      return player.getItemInHand().getType() == material;
   }

   public static boolean setHeld(Player player, String namespacedId) {
      CustomItemDefinition var2 = ContentManager.method8084().method8154(namespacedId);
      if (var2 == null) {
         return false;
      }

      player.getInventory().setItemInHand(var2.Ds());
      return true;
   }

   public static boolean giveItem(Player player, String namespacedId) {
      CustomItemDefinition var2 = ContentManager.method8084().method8154(namespacedId);
      if (var2 == null) {
         Material var3 = Material.matchMaterial(namespacedId);
         if (var3 == null || var3 == Material.AIR) {
            return false;
         }

         ItemStack var4 = new ItemStack(var3);
         ItemStackUtils.method7243(player, var4);
      } else {
         ItemStackUtils.method7243(player, var2.Ds());
      }

      return true;
   }

   public static boolean removeItem(Player player, String namespacedId) {
      CustomItemDefinition var3 = ContentManager.method8084().method8154(namespacedId);
      ItemStack var2;
      if (var3 == null) {
         Material var4 = Material.matchMaterial(namespacedId);
         if (var4 == null || var4 == Material.AIR) {
            return false;
         }

         var2 = new ItemStack(var4);
      } else {
         var2 = var3.Ds();
      }

      return RemoveCommand.method1502(player, var2) > 0;
   }

   public static Block blockInFront(Player player) {
      return player.getTargetBlockExact(8);
   }

   public static Block blockInFront(Player player, int maxDistance) {
      return player.getTargetBlockExact(maxDistance);
   }

   @Nullable
   public static Entity entityInFront(Player player) {
      return entityInFront(player, 8);
   }

   @Nullable
   public static Entity entityInFront(Player player, int maxDistance) {
      RayTraceWalker var3 = RayTraceWalker.forPlayer(player, maxDistance, 0.02);
      return var3.matchesTarget(RayTraceTarget.ENTITY) && var3.hasHitEntity() ? var3.getHitEntity() : null;
   }
}
