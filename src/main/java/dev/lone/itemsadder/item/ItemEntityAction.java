package dev.lone.itemsadder.item;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.ObjectPair;
import dev.lone.itemsadder.util.RayTraceTarget;
import dev.lone.itemsadder.util.RayTraceWalker;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.world.InstrumentRegistry;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.Nullable;

public abstract class ItemEntityAction extends ItemActionBase {
   public static final NamespacedKey acU = new NamespacedKey("itemsadder", "placeable_entity_item");
   public static final NamespacedKey acV = new NamespacedKey("itemsadder", "placeable_behaviour_type");
   public final EntityType acW;
   boolean acX = true;
   boolean bP = false;
   public ItemSoundEffects acY;
   public float acZ = 1.0F;
   public float xS = 1.0F;
   public float ut = 1.0F;
   public float ada;
   public float adb;
   public float adc;
   protected boolean add;
   protected boolean ade;
   protected boolean adf;

   public ItemEntityAction(CustomItemDefinition var1, EntityType var2, boolean var3, boolean var4) {
      super(var1);
      this.acW = var2;
      this.acX = var3;
      this.bP = var4;
      this.acY = new ItemSoundEffects();
   }

   public boolean At() {
      return this.acX;
   }

   public boolean Au() {
      return this.bP;
   }

   public void method358(float var1, float var2, float var3) {
      this.acZ = var1;
      this.xS = var2;
      this.ut = var3;
   }

   public void method359(float var1, float var2, float var3) {
      this.ada = var1;
      this.adb = var2;
      this.adc = var3;
   }

   public void method360(boolean var1, boolean var2, boolean var3) {
      this.add = var1;
      this.ade = var2;
      this.adf = var3;
   }

   public void method361(Entity var1, CustomItemDefinition var2) {
      var1.getPersistentDataContainer().set(acU, PersistentDataType.STRING, var2.getNamespacedID());
   }

   public static boolean method362(Entity var0) {
      return var0 instanceof ArmorStand || var0 instanceof ItemFrame || var0 instanceof ItemDisplay;
   }

   public static boolean method363(Entity var0) {
      return !method362(var0) ? false : var0.getPersistentDataContainer().has(acU, PersistentDataType.STRING);
   }

   @Nullable
   public static ItemEntityAction method364(Entity var0, String var1) {
      ContentManager var2 = ContentManager.method8084();
      if (var2 == null) {
         return null;
      }

      CustomItemDefinition var3 = var2.method8154((String)var0.getPersistentDataContainer().get(acU, PersistentDataType.STRING));
      return var3 == null ? null : (ItemEntityAction)var3.aoh.getAction(var1);
   }

   public static ItemStack method365(Entity var0) {
      return switch (var0) {
         case null -> throw new RuntimeException("Failed to get ItemStack. Null entity.");
         case ArmorStand var3 -> var3.getHelmet();
         case ItemFrame var4 -> var4.getItem();
         case ItemDisplay var5 -> var5.getItemStack();
         default -> throw new RuntimeException("Failed to get ItemStack. Entity '" + var0.getType() + "' can't be a furniture!");
      };
   }

   @Nullable
   public static CustomItemDefinition method366(Entity var0) {
      ItemStack var1 = method365(var0);
      CustomItemDefinition var2 = ContentManager.method8084().method8149(var1);
      return var2 == null ? null : var2;
   }

   @Nullable
   public static Entity aC(Player var0) {
      ObjectPair var1 = aD(var0);
      return var1 == null ? null : (Entity)var1.first;
   }

   @Nullable
   public static ObjectPair aD(Player var0) {
      RayTraceWalker var2 = RayTraceWalker.forPlayer(var0, 5.0, 0.02);
      if (var2.matchesTarget(RayTraceTarget.ENTITY)) {
         if (var2.hasHitEntity()) {
            Entity var1 = var2.getHitEntity();
            if (!method363(var1)) {
               return null;
            }

            RayTraceWalker var3 = new RayTraceWalker(var0, 5.0);
            if (var3.matchesTarget(RayTraceTarget.BLOCK) && var3.hasHitBlock()) {
               Block var4 = var3.getHitBlock();
               if (var4.getType() != Material.BARRIER) {
                  Location var5 = var0.getEyeLocation();
                  if (var3.getHitLocation().distance(var5) < var2.getHitLocation().distance(var5) && !var4.getBoundingBox().contains(var1.getBoundingBox())) {
                     return null;
                  }
               }

               SchedulerUtils.runLaterIf(() -> FurnitureItemEntity.method420(var1), 2L, var1.getType() != EntityType.ARMOR_STAND);
               if (var4.getType() == Material.BARRIER && !var1.getBoundingBox().contains(var4.getBoundingBox().expand(-0.5))) {
                  return null;
               }
            }

            return new ObjectPair(var1, var2.getHitLocation());
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public void method367(Entity var1) {
      ProjectileTypeHelper.setVelocity(var1, this.acZ, this.ut, this.xS);
   }

   public void method368(Entity var1) {
      this.method369(var1, null);
   }

   public void method369(Entity var1, @Nullable Player var2) {
      BoundingBox var3 = var1.getBoundingBox().expand(-0.5);
      List var4 = FurnitureItemEntity.method426(var1.getWorld(), var3.getMinX(), var3.getMaxX(), var3.getMinY(), var3.getMaxY(), var3.getMinZ(), var3.getMaxZ());
      if (var4.size() > 15) {
         AtomicInteger var5 = new AtomicInteger();
         SchedulerUtils.scheduleRepeatingSync(var3x -> {
            try {
               int var4x;
               for (var4x = var5.get(); var4x < var5.get() + 5; var4x++) {
                  Block var5x = ((Location)var4.get(var4x)).getBlock();
                  if (BlockFaceUtils.isChunkLoadedAt(var5x) && InstrumentRegistry.aP(var5x) && (var2 == null || BlockFaceUtils.simulatePlace(var5x, var2))) {
                     var5x.setType(Material.BARRIER, false);
                  }
               }

               var5.set(var4x);
            } catch (IndexOutOfBoundsException var6x) {
               var3x.cancel();
            }
         }, 0L, 1L);
      } else {
         for (Location var6 : var4) {
            Block var7 = var6.getBlock();
            if (BlockFaceUtils.isChunkLoadedAt(var7) && InstrumentRegistry.aP(var7) && (var2 == null || BlockFaceUtils.simulatePlace(var7, var2))) {
               var7.setType(Material.BARRIER, false);
            }
         }
      }
   }

   public void method370(Entity var1) {
      method371(var1, null);
   }

   public static void method371(Entity var0, @Nullable Player var1) {
      BoundingBox var2 = var0.getBoundingBox().expand(-0.5);
      List var3 = FurnitureItemEntity.method426(var0.getWorld(), var2.getMinX(), var2.getMaxX(), var2.getMinY(), var2.getMaxY(), var2.getMinZ(), var2.getMaxZ());
      if (var3.size() > 15) {
         AtomicInteger var4 = new AtomicInteger();
         SchedulerUtils.scheduleRepeatingSync(var3x -> {
            try {
               int var4x;
               for (var4x = var4.get(); var4x < var4.get() + 5; var4x++) {
                  Block var5x = ((Location)var3.get(var4x)).getBlock();
                  if (var5x.getType() == Material.BARRIER && (var1 == null || BlockFaceUtils.simulatePlace(var5x, var1))) {
                     var5x.setType(Material.AIR, false);
                  }
               }

               var4.set(var4x);
            } catch (IndexOutOfBoundsException var6x) {
               var3x.cancel();
            }
         }, 0L, 1L);
      } else {
         for (Location var5 : var3) {
            Block var6 = var5.getBlock();
            if (var6.getType() == Material.BARRIER && (var1 == null || BlockFaceUtils.simulatePlace(var6, var1))) {
               var6.setType(Material.AIR, false);
            }
         }
      }
   }
}
