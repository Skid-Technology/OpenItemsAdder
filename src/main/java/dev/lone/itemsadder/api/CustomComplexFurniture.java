package dev.lone.itemsadder.api;

import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.PlaceableItemSpec;
import dev.lone.itemsadder.item.PlacedItemInstance;
import java.util.HashSet;
import java.util.Set;
import lombok.Generated;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class CustomComplexFurniture {
   private static final Set EMPTY_SET = new HashSet();
   private final PlacedItemInstance internal;
   private final CustomEntity customEntity;

   public CustomComplexFurniture(PlacedItemInstance instance) {
      this.internal = instance;
      this.customEntity = new CustomEntity(instance.entity());
   }

   public static Set getNamespacedIdsInRegistry() {
      Set var0 = ContentManager.method8084().method8146("complex_furniture");
      return var0 == null ? EMPTY_SET : var0;
   }

   @Nullable
   public static CustomComplexFurniture byAlreadySpawned(LivingEntity entity) {
      PlacedItemInstance var1 = PlaceableItemSpec.findByEntity(entity);
      return var1 == null ? null : new CustomComplexFurniture(var1);
   }

   @Nullable
   public static CustomComplexFurniture byAlreadySpawned(Block block) {
      for (Entity var2 : block.getLocation().getWorld().getNearbyEntities(block.getLocation().add(0.5, 0.5, 0.5), 0.1, 0.1, 0.1)) {
         if (var2 instanceof LivingEntity var3) {
            PlacedItemInstance var4 = PlaceableItemSpec.findByEntity(var3);
            if (var4 != null) {
               return new CustomComplexFurniture(var4);
            }
         }
      }

      return null;
   }

   @Nullable
   public static CustomComplexFurniture spawn(String namespacedId, Location location, BlockFace blockFace, boolean forceNonSolid) {
      CustomItemDefinition var4 = ContentManager.method8084().method8154(namespacedId);
      if (var4 == null) {
         return null;
      }

      PlaceableItemSpec var5 = (PlaceableItemSpec)var4.aoh.getAction("complex_furniture");
      if (var5 == null) {
         return null;
      }

      PlacedItemInstance var6 = var5.placeInstance(null, var4, null, location, blockFace, forceNonSolid);
      return var6 == null ? null : new CustomComplexFurniture(var6);
   }

   public void remove(boolean dropItem) {
      remove(this.internal.entity().getLivingEntity(), dropItem);
   }

   public static void remove(LivingEntity entity, boolean dropItem) {
      if (entity != null) {
         PlacedItemInstance var2 = PlaceableItemSpec.findByEntity(entity);
         if (var2 != null) {
            var2.remove();
         }
      }
   }

   public void teleport(Entity destinationEntity) {
      this.teleport(destinationEntity.getLocation());
   }

   public void teleport(Location location) {
      PlaceableItemSpec.method344(this.internal);
      this.internal.entity().teleport(location);
   }

   public void setColor(Color color) {
      this.customEntity.setColorAllBones(color.asRGB());
   }

   @Nullable
   public Integer getOriginalLightLevel() {
      return this.internal.spec().adk;
   }

   @Nullable
   public Boolean getOriginalIsSolid() {
      return this.internal.spec().adl;
   }

   public void setCurrentLightLevel(int level) {
      BlockUtils.clearBlockState(this.getEntity().getLocation().getBlock());
      BlockUtils.markBlockState(this.getEntity().getLocation().getBlock(), level);
   }

   public LivingEntity getEntity() {
      return (LivingEntity)this.customEntity.getEntity();
   }

   @Generated
   public CustomEntity getCustomEntity() {
      return this.customEntity;
   }
}
