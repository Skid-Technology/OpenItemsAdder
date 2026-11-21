package dev.lone.itemsadder.item;

import dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins.ProtectionPlugins;
import dev.lone.itemsadder.api.Events.ComplexFurnitureBreakEvent;
import dev.lone.itemsadder.api.Events.ComplexFurniturePlacedEvent;
import dev.lone.itemsadder.api.Events.ComplexFurniturePrePlaceEvent;
import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.resourcepack.PackSessionTracker;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.IntTableConstantsE;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.PersistentDataHelper;
import dev.lone.itemsadder.util.SyntheticEntityDamageEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.WeakHashMap;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.Nullable;

public class PlaceableItemSpec extends ItemActionBase {
   public static final String adh = "complex_furniture";
   public static final NamespacedKey adi = new NamespacedKey("itemsadder", "complex_furniture");
   public static WeakHashMap adj = new WeakHashMap();
   public String iZ;
   public boolean qS;
   public Integer jb;
   @Nullable
   public final Integer adk;
   @Nullable
   public final Boolean adl;
   @Nullable
   public final Boolean adm;
   @Nullable
   public final Boolean adn;
   @Nullable
   public final Boolean ado;
   @Nullable
   public final Boolean adp;
   public final double adq;
   public final double adr;
   public final double ads;
   @Nullable
   public final String adt;
   public ItemSoundEffects adu;

   public static PlaceableItemSpec fromDefinition(CustomItemDefinition var0) {
      return (PlaceableItemSpec)var0.aoh.getAction("complex_furniture");
   }

   public PlaceableItemSpec(
      CustomItemDefinition var1,
      String var2,
      @Nullable Boolean var3,
      @Nullable Integer var4,
      @Nullable Integer var5,
      @Nullable Boolean var6,
      @Nullable Boolean var7,
      @Nullable Boolean var8,
      @Nullable Boolean var9,
      @Nullable Boolean var10,
      @Nullable String var11,
      @Nullable ItemSoundEffects var12,
      double var13,
      double var15,
      double var17
   ) {
      super(var1);
      this.qS = var3 != null && var3;
      this.jb = var4;
      this.adk = var5;
      this.adl = var6;
      this.adm = var7;
      this.adn = var8;
      this.ado = var9;
      this.adp = var10;
      this.adt = var11;
      this.adu = var12;
      this.adq = var13;
      this.adr = var15;
      this.ads = var17;
      if (!var2.contains(":")) {
         var2 = var1.getNamespace() + ":" + var2;
      }

      if (CustomEntityManager.getInstance().oN.containsKey(var2)) {
         this.iZ = var2;
      } else {
         throw new IllegalArgumentException("Invalid custom entity id: " + var2);
      }
   }

   @Nullable
   public void place(@Nullable Player var1, CustomItemDefinition var2, @Nullable ItemStack var3, Location var4, @Nullable BlockFace var5) {
      this.placeInstance(var1, var2, var3, var4, var5, false);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Nullable
   public PlacedItemInstance placeInstance(
      @Nullable Player var1, CustomItemDefinition var2, @Nullable ItemStack var3, Location var4, @Nullable BlockFace var5, boolean var6
   ) {
      if (!EventRegistrar.callEvent(new ComplexFurniturePrePlaceEvent(var1, this, var4))) {
         return null;
      }

      LivingEntity var7 = null;
      CustomEntityImpl var8 = null;

      try {
         if (!PermissionNodes.method1627(var1, this.adt)) {
            return null;
         }

         int var9 = this.jb != null ? this.jb : Settings.get().customEntitiesMaxPerChunk;
         int var10 = 0;

         for (Entity var14 : var4.getChunk().getEntities()) {
            if (CustomEntityManager.getInstance().isCustomEntityLoaded(var14)) {
               if (++var10 >= var9) {
                  return null;
               }
            }
         }

         BoundingBox var19 = BoundingBox.of(var4.getBlock());

         for (Entity var23 : var4.getWorld().getNearbyEntities(var19, var0 -> CustomEntityManager.getInstance().isCustomEntityLoaded(var0))) {
            if (var19.overlaps(var23.getBoundingBox())) {
               return null;
            }
         }

         if (Boolean.TRUE.equals(this.adn)) {
            if (var5 != null) {
               switch (IntTableConstantsE.SWITCH_MAP[var5.ordinal()]) {
                  case 1:
                  case 2:
                     if (var1 != null) {
                        float var21 = var1.getLocation().getYaw();
                        if (var21 < 0.0F) {
                           var21 += 360.0F;
                        }

                        if (var21 >= 315.0F || var21 < 45.0F) {
                           var4.setYaw(Boolean.FALSE.equals(this.adp) ? 180.0F : 0.0F);
                        } else if (var21 >= 45.0F && var21 < 135.0F) {
                           var4.setYaw(Boolean.FALSE.equals(this.adp) ? 270.0F : 90.0F);
                        } else if (var21 >= 135.0F && var21 < 225.0F) {
                           var4.setYaw(Boolean.FALSE.equals(this.adp) ? 0.0F : 180.0F);
                        } else if (var21 >= 225.0F && var21 < 315.0F) {
                           var4.setYaw(Boolean.FALSE.equals(this.adp) ? 90.0F : 270.0F);
                        }
                     }
                     break;
                  case 3:
                     var4.setYaw(180.0F);
                     break;
                  case 4:
                     var4.setYaw(0.0F);
                     break;
                  case 5:
                     var4.setYaw(270.0F);
                     break;
                  case 6:
                     var4.setYaw(90.0F);
               }
            }
         } else if (var1 != null) {
            var4.setYaw(var1.getLocation().getYaw() + (Boolean.TRUE.equals(this.adp) ? 0 : 180));
         }

         if (var5 != null) {
            switch (IntTableConstantsE.SWITCH_MAP[var5.ordinal()]) {
               case 1:
                  var4.add(0.0, this.adr, 0.0);
                  break;
               case 2:
                  var4.add(0.0, this.adq, 0.0);
                  break;
               case 3:
                  var4.add(0.0, 0.0, -this.ads);
                  break;
               case 4:
                  var4.add(0.0, 0.0, this.ads);
                  break;
               case 5:
                  var4.add(this.ads, 0.0, 0.0);
                  break;
               case 6:
                  var4.add(-this.ads, 0.0, 0.0);
            }
         }

         var8 = CustomEntityManager.getInstance().method8261(this.iZ, var4, null, var4.getWorld().getPlayers(), false, false, false, false, this.qS, var4x -> {
            var4x.setInvulnerable(true);
            var4x.setAI(false);
            var4x.setRemoveWhenFarAway(false);
            if (this.adl != null && this.adl || this.adk != null && this.adk > 0) {
               var4x.setGravity(false);
            }

            var4x.getPersistentDataContainer().set(adi, PersistentDataType.STRING, var2.getNamespacedID());
            if (var1 != null && var1.getGameMode() != GameMode.CREATIVE) {
               ItemStackUtils.au(var3);
            }
         });
         var7 = var8.getBukkitEntity();
         assert var7 != null;
         if (this.adk != null && this.adk > 0 || !var6 && this.adl != null && this.adl) {
            BoundingBox var22 = var7.getBoundingBox();
            findPlacedInBounds(var4.getWorld(), var22, true).forEach(var3x -> {
               Block var4x = var3x.getBlock();
               if (var1 == null || BlockFaceUtils.simulatePlace(var4x, var1)) {
                  if (this.adk != null && this.adk > 0) {
                     BlockUtils.markBlockState(var4x, this.adk);
                  }

                  if (!var6 && this.adl != null && this.adl && !var4x.getType().isSolid()) {
                     var4x.setType(Material.BARRIER);
                  }
               }
            });
         }

         FurnitureItemEntity.method418(var7, var1, true);
         this.adu.method10000(var4);
         return findByEntity(var8.getLivingEntity());
      } finally {
         if (var8 != null) {
            EventRegistrar.callEvent(new ComplexFurniturePlacedEvent(var1, var7, var8));
         }
      }
   }

   public static void method344(PlacedItemInstance var0) {
      LivingEntity var1 = var0.adx.getBukkitEntity();
      assert var1 != null;
      BoundingBox var2 = var1.getBoundingBox();
      findPlacedInBounds(var1.getWorld(), var2, false).forEach(var0x -> {
         Block var1x = var0x.getBlock();
         if (var1x.getType() == Material.BARRIER) {
            var1x.setType(Material.AIR);
         }

         BlockUtils.clearBlockState(var1x);
      });
   }

   public static void method345(PlacedItemInstance var0) {
      LivingEntity var1 = var0.adx.getBukkitEntity();
      assert var1 != null;
      BoundingBox var2 = var1.getBoundingBox();
      findPlacedInBounds(var1.getWorld(), var2, false).forEach(var1x -> {
         Block var2x = var1x.getBlock();
         if (var0.adz.adk != null && var0.adz.adk > 0) {
            BlockUtils.markBlockState(var2x, var0.adz.adk);
         }

         if (var0.adz.adl != null && var0.adz.adl && !var2x.getType().isSolid()) {
            var2x.setType(Material.BARRIER);
         }
      });
   }

   public static void method346(Player var0, PlacedItemInstance var1) {
      if (PermissionNodes.method1626(var0, var1.adz.adt)) {
         if (ProtectionPlugins.canBreakFurniture(var0, var1.entity().getBukkitEntity()) && BlockFaceUtils.simulateDamage(var0, var1.entity().getBukkitEntity())
            )
          {
            LivingEntity var2 = var1.adx.getBukkitEntity();
            assert var2 != null;
            boolean var3 = false;
            PlaceableItemSpec var4 = fromDefinition(var1.ady);
            if (var4.adm != null && var4.adm && var0.getGameMode() != GameMode.CREATIVE) {
               var3 = true;
            }

            ComplexFurnitureBreakEvent var5 = new ComplexFurnitureBreakEvent(var0, var2, var1.entity(), var3);
            EventRegistrar.callEvent(var5);
            if (!var5.isCancelled()) {
               BoundingBox var6 = var2.getBoundingBox();
               findPlacedInBounds(var0.getWorld(), var6, false).forEach(var1x -> {
                  Block var2x = var1x.getBlock();
                  if (BlockFaceUtils.simulateBreak(var2x, var0)) {
                     if (var2x.getType() == Material.BARRIER) {
                        var2x.setType(Material.AIR);
                     }

                     BlockUtils.clearBlockState(var2x);
                  }
               });
               CustomEntityManager.getInstance().removeWithEffects(var1.adx, true, false);
               if (var5.isDropItem()) {
                  LocationScanner.dropItemNear(var2.getLocation(), var1.ady.Ds(), var0);
               }

               FurnitureItemEntity.method419(var6, var0, true);
               method347(var0, var1);
               var4.adu.method9999(var2.getLocation());
            }
         }
      }
   }

   private static void method347(Player var0, PlacedItemInstance var1) {
      SyntheticEntityDamageEvent var2 = new SyntheticEntityDamageEvent(var0, var1.adx.getBukkitEntity(), DamageCause.ENTITY_ATTACK, 0.0);
      var1.ady.method8761(var0, ItemEventType.PLACED_FURNITURE_BREAK, var2x -> {
         ItemStack var3 = var1.ady.Ds();
         return var2x.applyOnDamage(var2, var1.ady, var3);
      });
   }

   public static List findPlacedInBounds(World var0, BoundingBox var1, boolean var2) {
      HashSet var3 = new HashSet();
      int var4 = (int)Math.floor(var1.getMinX());
      int var5 = (int)Math.floor(var1.getMaxX());
      int var6 = (int)Math.floor(var1.getMinY());
      int var7 = (int)Math.floor(var1.getMaxY());
      int var8 = (int)Math.floor(var1.getMinZ());
      int var9 = (int)Math.floor(var1.getMaxZ());

      for (int var10 = var4; var10 <= var5; var10++) {
         for (int var11 = var6; var11 <= var7; var11++) {
            for (int var12 = var8; var12 <= var9; var12++) {
               if (BlockFaceUtils.isChunkLoadedAtCoords(var0, var10 >> 4, var12 >> 4)) {
                  if (!var2) {
                     var3.add(new Location(var0, var10, var11, var12));
                  } else {
                     Block var13 = var0.getBlockAt(var10, var11, var12);
                     BoundingBox var14 = BoundingBox.of(var13);
                     if (var14.overlaps(var1)) {
                        BoundingBox var15 = var14.clone().intersection(var1);
                        double var16 = var15.getWidthX() * var15.getHeight() * var15.getWidthZ();
                        double var18 = 1.0;
                        Log.devLog(var16 / var18 + " " + var13.getType() + " " + BlockFaceUtils.locationCoordsString(var13.getLocation()));
                        if (var16 / var18 >= 0.3) {
                           var3.add(var13.getLocation());
                        }
                     }
                  }
               }
            }
         }
      }

      return new ArrayList(var3);
   }

   @Nullable
   public static PlacedItemInstance findByPlayer(Player var0) {
      if (!Settings.get().customEntitiesEnabled) {
         return null;
      }

      RayTraceResult var2 = var0.getWorld()
         .rayTraceEntities(
            var0.getEyeLocation(),
            var0.getEyeLocation().getDirection(),
            5.0,
            0.1,
            var1x -> var1x.getEntityId() != var0.getEntityId()
               && PersistentDataHelper.getStringValue(var1x, adi) != null
               && CustomEntityManager.getInstance().isCustomEntityUuid(var1x.getUniqueId())
         );
      if (var2 != null && var2.getHitEntity() != null) {
         Entity var1 = var2.getHitEntity();
         CustomEntityImpl var3 = CustomEntityManager.getInstance().getByUuid(var1.getUniqueId());
         if (var3 == null) {
            return null;
         } else {
            String var4 = PersistentDataHelper.getStringValue(var1, adi);
            CustomItemDefinition var5 = ContentManager.method8084().method8154(var4);
            if (var5 == null) {
               Log.warn("Unknown custom item: " + var4 + ". Probably was removed from config or was disabled.");
               CustomEntityManager.getInstance().removeWithEffects(var3, true, false);
               return null;
            } else {
               return PackSessionTracker.forPlayer(adj, var0).recordRequest(() -> Log.sendLocalized(var0, "interact-too-fast"))
                  ? null
                  : new PlacedItemInstance(var3, var5, fromDefinition(var5));
            }
         }
      } else {
         return null;
      }
   }

   @Nullable
   public static PlacedItemInstance findByEntity(LivingEntity var0) {
      if (!Settings.get().customEntitiesEnabled) {
         return null;
      } else {
         CustomEntityImpl var1 = CustomEntityManager.getInstance().getByUuid(var0.getUniqueId());
         if (var1 == null) {
            return null;
         } else {
            String var2 = PersistentDataHelper.getStringValue(var0, adi);
            if (var2 == null) {
               return null;
            } else {
               CustomItemDefinition var3 = ContentManager.method8084().method8154(var2);
               if (var3 == null) {
                  Log.warn("Unknown custom item: " + var2 + ". Probably was removed from config or was disabled.");
                  CustomEntityManager.getInstance().removeWithEffects(var1, true, false);
                  return null;
               } else {
                  return new PlacedItemInstance(var1, var3, fromDefinition(var3));
               }
            }
         }
      }
   }

   static {
      PackSessionTracker.registerQuitListener(adj);
   }
}
