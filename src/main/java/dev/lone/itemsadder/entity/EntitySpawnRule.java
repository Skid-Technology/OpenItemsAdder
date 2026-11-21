package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.util.IntRange;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Generated;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public class EntitySpawnRule {
   private final CustomMobSettings ql;
   private final float qm;
   private final int qn;
   @Nullable
   private final List qo;
   @Nullable
   private final List qp;
   private final SpawnGroup field10;
   @Nullable
   private final SpawnBoundsPair qr;
   @Nullable
   private final List qs;
   @Nullable
   private final List qt;
   @Nullable
   private final IntRange qu;

   public EntitySpawnRule(
      CustomMobSettings var1,
      float var2,
      int var3,
      @Nullable List var4,
      @Nullable List var5,
      SpawnGroup var6,
      @Nullable SpawnBoundsPair var7,
      @Nullable List var8,
      @Nullable List var9,
      @Nullable IntRange var10
   ) {
      this.ql = var1;
      this.qm = var2;
      this.qn = var3;
      this.qo = var4;
      this.qp = var5;
      this.field10 = var6;
      this.qr = var7;
      this.qs = var8;
      this.qt = var9;
      this.qu = var10;
   }

   public static EntitySpawnRule create(
      CustomMobSettings var0,
      float var1,
      int var2,
      @Nullable List var3,
      @Nullable List var4,
      SpawnGroup var5,
      @Nullable SpawnBoundsPair var6,
      @Nullable List var7,
      @Nullable List var8,
      @Nullable IntRange var9
   ) {
      if (var0 == null) {
         throw new IllegalArgumentException("entity must not be null");
      }

      if (var1 < 0.0F || var1 > 100.0F) {
         throw new IllegalArgumentException("chance must be between 0 and 100");
      }

      if (var2 <= 0) {
         throw new IllegalArgumentException("amount must be greater than 0");
      }

      if (var5 == null) {
         throw new IllegalArgumentException("spawnLogic must not be null");
      }

      if (var5.qC == SpawnMode.REPLACE && var5.qD.isEmpty()) {
         var5.qD.add(var0.vw);
      }

      SpawnCategory var10 = ProjectileTypeHelper.getAdapter().method3277(var0.vw);
      if (var10 == null) {
         throw new IllegalArgumentException("Failed to calculate the entity category for " + var0.vw);
      } else {
         boolean var11 = var5.qD.stream().map(ProjectileTypeHelper.getAdapter()::method3277).allMatch(var1x -> var1x != null && var1x == var10);
         if (!var11) {
            throw new IllegalArgumentException(
               "The 'type' '"
                  + var0.vw
                  + "' with category "
                  + var10
                  + " is not compatible with the 'types' specified in 'spawn_logic': "
                  + var5.qD
                  + ". All 'types' must belong to the same category as the entity to be spawned. If you want to use types from different categories, set 'spawn_logic.method' to 'SPAWN_NEAR'. This is a technical limitation of the game. Allowing different categories would cause infinite spawning and lag."
            );
         } else {
            return new EntitySpawnRule(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9);
         }
      }
   }

   public boolean isWorldAllowed(World var1) {
      if (this.qp != null && !this.qp.isEmpty()) {
         if (this.qp.contains(var1.getName())) {
            return true;
         }

         for (String var3 : this.qp) {
            if (FilenameUtils.wildcardMatch(var1.getName(), var3)) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public boolean canSpawnAt(EntityType var1, Location var2) {
      if (!this.field10.qD.contains(var1)) {
         return false;
      }

      if (this.qr != null && !this.qr.contains(var2)) {
         return false;
      }

      if (this.qu != null) {
         byte var3 = var2.getBlock().getLightLevel();
         if (!this.qu.contains(var3)) {
            return false;
         }
      }

      if (this.qt != null && !this.qt.isEmpty()) {
         int var5 = (int)(var2.getWorld().getTime() % 24000L);
         boolean var4 = this.qt.stream().anyMatch(var1x -> var1x.contains(var5));
         if (!var4) {
            return false;
         }
      }

      if (this.qo != null && !this.qo.isEmpty()) {
         Biome var6 = var2.getBlock().getBiome();
         if (!this.qo.contains(var6.getKey())) {
            return false;
         }
      }

      if (this.qs != null && !this.qs.isEmpty()) {
         WeatherType var7 = WeatherType.method176(var2);
         return var7 != null && this.qs.contains(var7);
      } else {
         return true;
      }
   }

   public boolean isEnabled() {
      return ThreadLocalRandom.current().nextDouble(0.0, 100.0) < this.qm;
   }

   @Generated
   public static EntitySpawnRuleBuilder toBuilder() {
      return new EntitySpawnRuleBuilder();
   }

   public CustomMobSettings getSettings() {
      return this.ql;
   }

   public float getWeight() {
      return this.qm;
   }

   public int getMaxPerChunk() {
      return this.qn;
   }

   @Nullable
   public List gH() {
      return this.qo;
   }

   @Nullable
   public List gI() {
      return this.qp;
   }

   public SpawnGroup getGroup() {
      return this.field10;
   }

   @Nullable
   public SpawnBoundsPair getBounds() {
      return this.qr;
   }

   @Nullable
   public List gL() {
      return this.qs;
   }

   @Nullable
   public List gM() {
      return this.qt;
   }

   @Nullable
   public IntRange getRange() {
      return this.qu;
   }
}
