package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.internal.SwitchMap39;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.nms.NmsEntityAdapter;
import dev.lone.itemsadder.nms.NmsRegistry;
import dev.lone.itemsadder.util.EnvironmentDetector;
import dev.lone.itemsadder.util.LocationScanner;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Trident;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.Nullable;

public final class ProjectileTypeHelper {
   private final NmsEntityAdapter aVM = (NmsEntityAdapter)NmsRegistry.method2777(NmsEntityAdapter.class);
   private static ProjectileTypeHelper aVN;

   ProjectileTypeHelper() {
   }

   private static ProjectileTypeHelper getInstance() {
      if (aVN == null) {
         aVN = new ProjectileTypeHelper();
      }

      return aVN;
   }

   public static NmsEntityAdapter getAdapter() {
      return getInstance().aVM;
   }

   public static void method3217(ArmorStand var0, boolean var1) {
      if (EnvironmentDetector.paperAdventure) {
         try {
            var0.setCanTick(var1);
         } catch (NoSuchMethodError var3) {
         }
      }
   }

   public static void method3218(Entity entity, float yaw) {
      getInstance().aVM.method3234(entity, yaw);
   }

   public static void method3219(Entity entity, boolean val) {
      getInstance().aVM.method3239(entity, val);
   }

   public static void method3220(Entity entity, float val) {
      getInstance().aVM.method3241(entity, val);
   }

   @Deprecated
   public static void setVelocity(Entity entity, double var3, double var4, double var5) {
      getInstance().aVM.method3242(entity, var3, var4, var5);
   }

   @Deprecated
   public static void setBounds(Entity var0, BoundingBox var1) {
      getInstance().aVM.method3243(var0, var1.getMinX(), var1.getMaxX(), var1.getMinY(), var1.getMaxY(), var1.getMinZ(), var1.getMaxZ());
   }

   @Deprecated
   public static BoundingBox computeBounds(Entity var0, double var1, double var3, double var5, double var7, double var9, double var11) {
      getInstance().aVM.method3243(var0, var1, var3, var5, var7, var9, var11);
      return var0.getBoundingBox();
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static boolean method3224(LivingEntity var0) {
      switch (SwitchMap39.aVO[var0.getType().ordinal()]) {
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
            return true;
         default:
            return false;
      }
   }

   public static boolean method3225(Entity var0) {
      return !isProjectileType(var0.getType()) ? false : var0.getLocation().getBlock().getLightFromSky() > 11;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static boolean isProjectileType(EntityType var0) {
      if (var0.name().equals("ZOMBIE_NAUTILUS")) {
         return true;
      }

      switch (SwitchMap39.aVO[var0.ordinal()]) {
         case 4:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
            return true;
         case 5:
         case 6:
         case 7:
         case 8:
         default:
            return false;
      }
   }

   public static void method3227(Projectile var0) {
      ItemStack var1 = ItemStackUtils.method7253(var0);
      if (var1 != null) {
         var1 = var1.clone();
      }

      var0.remove();
      if (!(var0 instanceof Arrow) || ((Arrow)var0).getPickupStatus() == PickupStatus.ALLOWED) {
         if (var1 != null) {
            LocationScanner.dropItem(var0.getLocation(), var1);
         }
      }
   }

   public static boolean method3228(Material var0) {
      return var0 == Material.ITEM_FRAME || var0 == Material.GLOW_ITEM_FRAME;
   }

   public static boolean isAllowedEntityType(EntityType var0) {
      return var0 == EntityType.ITEM_FRAME || var0 == EntityType.GLOW_ITEM_FRAME;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static boolean method3230(Projectile var0) {
      return switch (SwitchMap39.aVO[var0.getType().ordinal()]) {
         case 17, 18, 19 -> var0 instanceof Trident
            ? ItemStackUtils.aL(getProjectileItem(var0)) <= 0
            : (var0 instanceof AbstractArrow var1 ? var1.getPickupStatus() != PickupStatus.DISALLOWED : true);
         default -> false;
      };
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Nullable
   public static ItemStack getProjectileItem(Projectile var0) {
      try {
         switch (SwitchMap39.aVO[var0.getType().ordinal()]) {
            case 20:
               return ((ThrownExpBottle)var0).getItem();
            case 21:
               return ((Egg)var0).getItem();
            case 22:
               return ((EnderPearl)var0).getItem();
            case 23:
               return ((ThrownPotion)var0).getItem();
            case 24:
               return ((Snowball)var0).getItem();
            default:
               if (var0 instanceof AbstractArrow) {
                  if (EnvironmentDetector.paperAdventure) {
                     return ((AbstractArrow)var0).getItemStack();
                  }

                  return getAdapter().method3262(var0);
               }
         }
      } catch (NoSuchMethodError var2) {
      }

      return null;
   }
}
