package dev.lone.itemsadder.block;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import dev.lone.itemsadder.item.ItemStackPairRecord;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.BoxBounds;
import dev.lone.itemsadder.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.math3.util.FastMath;
import org.bukkit.Chunk;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jctools.maps.NonBlockingHashMapLong;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BlockDisplayRegistry {
   public static int me;
   private static final float mf = (float)Math.toRadians(15.0);
   private static final float mg = mf * 0.55F;
   private static final int mh = 5;
   private static final Vector3f mi = new Vector3f(0.5F, 0.5F, 0.5F);
   private static final Vector3f mj = new Vector3f(0.5F, 0.44F, 0.5F);
   private final Player mk;
   private final Map ml = new ConcurrentHashMap();
   private final NonBlockingHashMapLong field49 = new NonBlockingHashMapLong();
   private final Map mn = new ConcurrentHashMap();
   private final ConcurrentLinkedDeque mo = new ConcurrentLinkedDeque();
   @Nullable
   private volatile BlockDisplayMove mp;
   @Nullable
   private Block mq;

   public BlockDisplayRegistry(Player var1, int var2) {
      this.mk = var1;
      me = var2 + 20;
   }

   private static long packChunkCoords(int var0, int var1) {
      return (long)var0 << 32 ^ var1 & 4294967295L;
   }

   public void registerEntry(BlockDisplayEntry var1) {
      if (!var1.isDirty()) {
         var1.setDirty(true);
         int var2 = var1.my.x >> 4;
         int var3 = var1.my.z >> 4;
         this.mn.computeIfAbsent(packChunkCoords(var2, var3), var0 -> new ConcurrentLinkedQueue<>()).add(var1);
         this.ml.put(var1.mr.getUniqueId(), var1);
         this.field49.put(ChunkBlockTracker.packIntVector(var1.my), var1);
      }
   }

   private void unregisterEntry(BlockDisplayEntry var1) {
      var1.setDirty(false);
      int var2 = var1.my.x >> 4;
      int var3 = var1.my.z >> 4;
      long var4 = packChunkCoords(var2, var3);
      ConcurrentLinkedQueue var6 = (ConcurrentLinkedQueue)this.mn.get(var4);
      if (var6 != null) {
         var6.removeIf(var1x -> var1x == var1);
         if (var6.isEmpty()) {
            this.mn.remove(var4, var6);
         }
      }

      this.field49.remove(ChunkBlockTracker.packLocation(var1.mA), var1);
   }

   private List findNearbyEntries(Location var1, double var2) {
      int var4 = var1.getBlockX() >> 4;
      int var5 = var1.getBlockZ() >> 4;
      ArrayList var6 = new ArrayList();

      for (int var7 = -1; var7 <= 1; var7++) {
         for (int var8 = -1; var8 <= 1; var8++) {
            ConcurrentLinkedQueue var9 = (ConcurrentLinkedQueue)this.mn.get(packChunkCoords(var4 + var7, var5 + var8));
            if (var9 != null) {
               for (BlockDisplayEntry var11 : var9) {
                  double var12 = var11.my.toVector3f().distanceSquared(new Vector3f((float)var1.getX(), (float)var1.getY(), (float)var1.getZ()));
                  if (var12 <= (var2 + 2.0) * (var2 + 2.0)) {
                     var6.add(var11);
                  }
               }
            }
         }
      }

      return var6;
   }

   @Nullable
   public BlockDisplayEntry raycastEntry(Player var1) {
      return this.raycastEntryInternal(var1, true);
   }

   @Nullable
   BlockDisplayEntry raycastEntryInternal(Player var1, boolean var2) {
      Location var3 = var1.getEyeLocation();
      Vector var4 = var3.getDirection();
      Vector var5 = var3.toVector();
      double var6 = var1.getGameMode() == GameMode.CREATIVE
         ? Objects.requireNonNull(var1.getAttribute(Attribute.PLAYER_BLOCK_INTERACTION_RANGE)).getValue()
         : 4.5;
      RayTraceResult var8 = var1.getWorld().rayTraceBlocks(var3, var4, var6, FluidCollisionMode.NEVER, true);
      this.mq = var8 == null ? null : var8.getHitBlock();
      double var9 = var8 == null ? var6 * var6 : var8.getHitPosition().distanceSquared(var5);
      long var11 = var1.getWorld().getFullTime();
      if (var2
         && this.mp != null
         && this.mp.mK != null
         && this.mp.mK.cV
         && var11 - this.mp.mL < 3L
         && this.mp.mJ == var6
         && this.mp.mH.distanceSquared(var5) < 0.04
         && this.mp.mI.angle(var4) < Math.toRadians(2.5)) {
         Vector var13 = this.mp.mK.mz.rayIntersectionWithin(var5, var4, var9);
         if (var13 != null) {
            return this.mp.mK;
         }
      }

      BlockDisplayEntry var19 = null;

      for (BlockDisplayEntry var15 : this.findNearbyEntries(var3, var6)) {
         if (var15.isDirty()) {
            Vector var16 = var15.mz.rayIntersectionWithin(var5, var4, var9);
            if (var16 != null) {
               double var17 = var16.distanceSquared(var5);
               var19 = var15;
               var9 = var17;
            }
         }
      }

      this.mp = var19 == null ? null : new BlockDisplayMove(var5, var4, var6, var19, var11);
      return var19;
   }

   @Nullable
   Block getLastHitBlock() {
      return this.mq;
   }

   public void showBlockDisplay(Location var1, String var2, byte var3) {
      Location var4 = this.mk.getLocation();
      boolean var5 = StepSoundTracker.kP && BlockFaceUtils.horizontalDistanceUnchecked(var1, var4) >= StepSoundTracker.kQ;
      this.acquireDisplayEntry(true, null, var2, var3, var1, var5, var4);
   }

   @Nullable
   public BlockDisplayEntry acquireDisplayEntry(
      boolean var1, @Nullable BlockSoundProfile var2, String var3, byte var4, Location var5, boolean var6, @Nullable Location var7
   ) {
      BlockDisplayEntry var8 = null;
      if (var1) {
         var8 = this.getEntryAt(var5);
      }

      if (var8 == null) {
         var8 = (BlockDisplayEntry)this.mo.poll();
         if (var8 == null && this.ml.size() < me) {
            var8 = this.createDisplayEntry();
         }
      }

      if (var8 == null) {
         return null;
      }

      boolean var9 = BlockFaceUtils.isSameBlockAsVector(var5, var8.my);
      if (var9) {
         if (!var8.shouldUpdateForState(var4, var3, var6, false)) {
            var8.refreshTimestamp();
            this.registerEntry(var8);
            return var8;
         }
      } else {
         var8.setLocation(var5);
      }

      if (var2 == null) {
         var2 = (BlockSoundProfile)ContentManager.method8084().au.get(var3);
      }

      if (var2 == null) {
         return null;
      }

      var8.namespacedId = var3;
      var8.ms = var4;
      var8.refreshTimestamp();
      this.updateDisplayState(var8, var2, var4, var6, var7, true, true);
      if (!var9) {
         NativePacketsHolder.PD().teleportFor(var8.mr, this.mk, var5);
      }

      this.registerEntry(var8);
      return var8;
   }

   private BlockDisplayEntry createDisplayEntry() {
      Location var1 = this.mk.getLocation().clone().add(0.0, -1000.0, 0.0);
      ItemDisplay var2 = NativePacketsHolder.PD().spawnItemDisplay(var1, var0 -> {
         var0.setItemDisplayTransform(ItemDisplayTransform.FIXED);
         Transformation var1x = var0.getTransformation();
         var1x.getTranslation().set(mi);
         var0.setTransformation(var1x);
         var0.setInvisible(true);
         var0.setViewRange(0.7F);
         var0.setDisplayHeight(0.7F);
         var0.setDisplayWidth(0.7F);
         var0.setInterpolationDelay(0);
         var0.setInterpolationDuration(0);
         var0.setTeleportDuration(0);
         var0.setShadowRadius(0.0F);
         var0.setShadowStrength(0.0F);
         var0.setGlowColorOverride(null);
      });
      NativePacketsHolder.PD().mountFor(var2, this.mk);
      return new BlockDisplayEntry(var2, var1, null, 0);
   }

   public void faceDisplayToward(BlockDisplayEntry var1, Location var2, boolean var3, boolean var4) {
      double var5 = var1.getLocation().getX();
      double var7 = var1.getLocation().getZ();
      double var9 = var2.getX();
      double var11 = var2.getZ();
      float var13 = -((float)FastMath.atan2(var11 - var7, var9 - var5) - (float) (Math.PI / 2));
      long var14 = this.mk.getWorld().getTime();
      float var16 = Math.abs(var1.mv - var13);
      if (var4 || var16 > mg || var14 - var1.mw >= 5L) {
         ProjectileTypeHelper.getAdapter().method3281(var1.mr, mi, new Quaternionf().rotationY(var13));
         var1.mv = var13;
         var1.mw = var14;
         if (var3) {
            NativePacketsHolder.PD().setVisibilityFor(var1.mr, this.mk, true);
         }
      }
   }

   public void updateDisplayState(BlockDisplayEntry var1, BlockSoundProfile var2, byte var3, boolean var4, @Nullable Location var5, boolean var6, boolean var7) {
      ItemStackPairRecord var8 = var2.getStatusItemByAge(var3);
      if (var8 == null) {
         var8 = var2.getStatusItem(0);
      }

      if (var8 == null) {
         Log.warn("Crop with namespaced ID " + var2.jP + " has no age status for age " + var3 + " or 0, cannot update display!");
      } else {
         ItemDisplay var9 = var1.mr;
         if (var6) {
            double var10 = var1.getLocation().getX() + 0.5;
            double var12 = var1.getLocation().getY();
            double var14 = var1.getLocation().getZ() + 0.5;
            double var16 = var8.du() != null ? var8.du().floatValue() : var2.jU.x;
            double var18 = var8.dv() != null ? var8.dv().floatValue() : var2.jU.y;
            double var20 = var8.dt() != null ? var8.dt().floatValue() : var2.jU.z;
            var1.setBounds(new BoxBounds(var10 - var16 / 2.0, var12, var14 - var20 / 2.0, var10 + var16 / 2.0, var12 + var18, var14 + var20 / 2.0));
         }

         if (var7) {
            Vector3f var22 = mi;
            if (var2.jZ && !var2.ka) {
               var22 = mj;
            }

            ProjectileTypeHelper.getAdapter().method3281(var9, var22, new Quaternionf().identity());
         }

         if (var4 && var8.dq() != null) {
            var1.setActiveFlag(true);
            if (var5 != null) {
               this.faceDisplayToward(var1, var5, false, true);
            }

            var9.setItemStack(var8.dq());
            NativePacketsHolder.PD().setVisibilityFor(var9, this.mk, true);
         } else {
            if (var1.isActiveFlag()) {
               Vector3f var23 = mi;
               if (var2.jZ && !var2.ka) {
                  var23 = mj;
               }

               ProjectileTypeHelper.getAdapter().method3281(var9, var23, new Quaternionf().identity());
               var1.setActiveFlagDup2(false);
            }

            var9.setItemStack(var8.dp());
            NativePacketsHolder.PD().setVisibilityFor(var9, this.mk, true);
         }

         var1.ms = var3;
         var1.namespacedId = var2.jP;
      }
   }

   public BlockDisplayEntry getEntryByUuid(UUID var1) {
      return (BlockDisplayEntry)this.ml.get(var1);
   }

   public void releaseByUuid(UUID var1) {
      this.releaseEntry((BlockDisplayEntry)this.ml.get(var1));
   }

   public void releaseAt(Location var1) {
      this.releaseEntry((BlockDisplayEntry)this.field49.get(ChunkBlockTracker.packLocation(var1)));
   }

   public void releaseMissing(Set var1) {
      for (UUID var3 : this.ml.keySet()) {
         if (!var1.contains(var3)) {
            this.releaseByUuid(var3);
         }
      }
   }

   private void releaseEntry(@Nullable BlockDisplayEntry var1) {
      if (var1 != null && this.ml.remove(var1.mr.getUniqueId(), var1)) {
         this.unregisterEntry(var1);
         Location var2 = this.mk.getLocation().clone().add(0.0, -1000.0, 0.0);
         NativePacketsHolder.PD().teleportFor(var1.mr, this.mk, var2);
         var1.setLocation(var2);
         var1.mr.setItemStack(null);
         NativePacketsHolder.PD().setVisibilityFor(var1.mr, this.mk, true);
         this.mo.add(var1);
      }
   }

   public void releaseChunk(Chunk var1) {
      for (BlockDisplayEntry var3 : this.ml.values()) {
         if (var3.getChunk().equals(var1)) {
            this.releaseByUuid(var3.mr.getUniqueId());
         }
      }
   }

   public void releaseChunkCoords(int var1, int var2, World var3) {
      for (BlockDisplayEntry var5 : this.ml.values()) {
         if (var5.getWorld().equals(var3) && (int)var5.my.x >> 4 == var1 && (int)var5.my.z >> 4 == var2) {
            this.releaseByUuid(var5.mr.getUniqueId());
         }
      }
   }

   public void clearAll() {
      this.clearEntries(true);
   }

   void clearEntries(boolean var1) {
      if (var1) {
         for (BlockDisplayEntry var3 : this.ml.values()) {
            NativePacketsHolder.PD().removeFor(var3.mr, this.mk);
         }

         for (BlockDisplayEntry var5 : this.mo) {
            NativePacketsHolder.PD().removeFor(var5.mr, this.mk);
         }
      }

      this.ml.clear();
      this.field49.clear();
      this.mn.clear();
      this.mo.clear();
   }

   @Nullable
   public BlockDisplayEntry getEntryAt(Location var1) {
      return (BlockDisplayEntry)this.field49.get(ChunkBlockTracker.packLocation(var1));
   }

   public int getEntryCount() {
      return this.ml.size();
   }

   public NonBlockingHashMapLong getPositionIndex() {
      return this.field49;
   }

   public void purgeExpiredEntries() {
      if (!this.mo.isEmpty()) {
         int var1 = 0;
         long var2 = System.currentTimeMillis() - 5000L;

         for (BlockDisplayEntry var5 : this.mo) {
            if (var5.mt <= var2 && this.mo.remove(var5)) {
               NativePacketsHolder.PD().removeFor(var5.mr, this.mk);
               var1++;
            }
         }
      }
   }
}
