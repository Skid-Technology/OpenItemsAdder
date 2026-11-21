package dev.lone.itemsadder.block;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.network.ViaVersionIntegration;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.DoubleQuad;
import dev.lone.itemsadder.util.ServerCompat;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.Nullable;

public final class PlayerStepState {
   private static final long ls = TimeUnit.MILLISECONDS.toNanos(200L);
   private static final long lt = TimeUnit.SECONDS.toNanos(1L);
   private static final long lu = TimeUnit.MILLISECONDS.toNanos(200L);
   private static final double lv = 8.0;
   public ObjectArrayList lw = new ObjectArrayList();
   @Nullable
   public Location lx;
   @Nullable
   public BoundingBox ly;
   @Nullable
   private Interaction lz;
   private long lA;
   private long lB;
   private long lC;
   private boolean lD;
   @Nullable
   private volatile DoubleQuad lE;
   private boolean lF;
   private boolean lG = true;
   final Location lH;
   final Location field47;
   final Location lJ;
   private final Player lK;
   long lL = 0L;
   final AtomicBoolean lM = new AtomicBoolean();
   final AtomicBoolean lN = new AtomicBoolean(true);
   final AtomicBoolean field48 = new AtomicBoolean();
   final AtomicBoolean lP = new AtomicBoolean();
   final AtomicBoolean lQ = new AtomicBoolean();
   volatile long lr;
   ObjectArrayList lR;
   int lS = Integer.MIN_VALUE;
   int lT = Integer.MIN_VALUE;
   @Nullable
   Float lU;
   @Nullable
   Float lV;
   public boolean lW;
   public short lX;
   public byte lY;
   public byte lZ;

   PlayerStepState(Player var1) {
      World var2 = var1.getWorld();
      this.lH = new Location(var2, 0.0, 0.0, 0.0);
      this.field47 = new Location(var2, 0.0, 0.0, 0.0);
      this.lJ = new Location(var2, 0.0, 0.0, 0.0);
      this.lK = var1;
      this.lR = new ObjectArrayList();
   }

   public float getLookSpeed() {
      if (this.lU != null && this.lV != null) {
         float var1 = BlockFaceUtils.normalizeYaw(this.field47.getYaw() - this.lU);
         float var2 = this.field47.getPitch() - this.lV;
         this.lU = this.field47.getYaw();
         this.lV = this.field47.getPitch();
         return (Math.abs(var1) + Math.abs(var2)) / StepSoundTracker.kM;
      } else {
         this.lU = this.field47.getYaw();
         this.lV = this.field47.getPitch();
         return 0.0F;
      }
   }

   public void clearDisplays() {
      if (!this.lw.isEmpty()) {
         IntList var1 = IntList.of(this.lw.stream().mapToInt(Entity::getEntityId).toArray());
         NativePacketsHolder.PD().sendRemove(this.lK, var1);
      }

      this.lw.clear();
      if (this.lz != null) {
         this.lz.remove();
         this.lz = null;
      }

      this.lG = true;
      this.lx = null;
      this.ly = null;
      this.lA = 0L;
      this.lF = false;
   }

   private boolean hasValidInteraction() {
      return this.lz != null && this.lz.isValid();
   }

   public boolean isOwnInteraction(Interaction var1) {
      return this.lz == var1;
   }

   public void extendHitboxShort() {
      this.extendHitbox(ls);
   }

   public void extendHitboxLong() {
      this.extendHitbox(lt);
   }

   void refreshContinuousTimer() {
      this.lB = System.nanoTime() + lu;
   }

   boolean isContinuousActive() {
      return System.nanoTime() < this.lB;
   }

   void startContinuousHarvest() {
      this.refreshContinuousTimer();
      if (Settings.get().cropContinuousHarvest && !this.lD && (!ServerCompat.viaVersionPresent || !ViaVersionIntegration.method4869(this.lK))) {
         double var1 = PlayerInteractionFacade.getHandler().method5269(this.lK, 8.0);
         Location var3 = this.lK.getEyeLocation();
         this.lE = new DoubleQuad(var3.getX(), var3.getY(), var3.getZ(), var1);
         this.lD = true;
      }
   }

   private void tickContinuousHarvest() {
      if (this.lD) {
         if (Settings.get().cropContinuousHarvest && this.isContinuousActive()) {
            Location var1 = this.lK.getEyeLocation();
            DoubleQuad var2 = this.lE;
            if (var2 != null) {
               this.lE = new DoubleQuad(var1.getX(), var1.getY(), var1.getZ(), var2.getD());
            }
         } else {
            this.stopContinuousHarvest();
         }
      } else if (this.lE != null && System.nanoTime() >= this.lC) {
         this.lE = null;
      }
   }

   boolean isInContinuousRange(int var1, int var2, int var3) {
      DoubleQuad var4 = this.lE;
      return var4 != null && isOutsideBlockRange(var4.getA(), var4.getB(), var4.getC(), var1, var2, var3, var4.getD());
   }

   static boolean isOutsideBlockRange(double var0, double var2, double var4, int var6, int var7, int var8, double var9) {
      double var11 = Math.max(Math.max(var6 - var0, 0.0), var0 - var6 - 1.0);
      double var13 = Math.max(Math.max(var7 - var2, 0.0), var2 - var7 - 1.0);
      double var15 = Math.max(Math.max(var8 - var4, 0.0), var4 - var8 - 1.0);
      double var17 = var9 + 1.0;
      return var11 * var11 + var13 * var13 + var15 * var15 >= var17 * var17;
   }

   private void stopContinuousHarvest() {
      if (this.lD) {
         if (this.lK.isOnline()) {
            PlayerInteractionFacade.getHandler().cH(this.lK);
         }

         this.lC = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(Math.max(200L, this.lK.getPing() + 100L));
         this.lD = false;
      }
   }

   private void updateInteractionHitbox(BoundingBox var1, boolean var2) {
      if (!Settings.get().cropPreciseHitbox) {
         if (this.lz != null) {
            this.lz.remove();
            this.lz = null;
         }

         this.lF = false;
      } else {
         this.lF = var2;
         boolean var3 = !var2 || this.lA != 0L && System.nanoTime() < this.lA;
         if (!var3) {
            this.lA = 0L;
         }

         float var4 = var3 ? 0.0F : (float)Math.max(var1.getWidthX(), var1.getWidthZ());
         float var5 = var3 ? 0.0F : (float)var1.getHeight();
         Location var6 = new Location(this.lK.getWorld(), var1.getCenterX(), var1.getMinY(), var1.getCenterZ());
         if (!this.hasValidInteraction()) {
            this.lz = (Interaction)this.lK.getWorld().spawn(var6, Interaction.class, var2x -> {
               var2x.setPersistent(false);
               var2x.setVisibleByDefault(false);
               var2x.setResponsive(true);
               var2x.setInvisible(true);
               var2x.setInteractionWidth(var4);
               var2x.setInteractionHeight(var5);
            });
            this.lK.showEntity(Main.getInstance(), this.lz);
         } else {
            this.lz.teleport(var6);
            this.setInteractionSize(var4, var5);
         }
      }
   }

   private void extendHitbox(long var1) {
      this.lA = Math.max(this.lA, System.nanoTime() + var1);
      this.setInteractionSize(0.0F, 0.0F);
   }

   private void restoreHitbox() {
      if (Settings.get().cropPreciseHitbox
         && this.lA != 0L
         && System.nanoTime() >= this.lA
         && this.lF
         && !this.lG
         && this.ly != null
         && this.hasValidInteraction()) {
         this.lA = 0L;
         this.setInteractionSize((float)Math.max(this.ly.getWidthX(), this.ly.getWidthZ()), (float)this.ly.getHeight());
      }
   }

   private void setInteractionSize(float var1, float var2) {
      if (this.hasValidInteraction()) {
         if (this.lz.getInteractionWidth() != var1) {
            this.lz.setInteractionWidth(var1);
         }

         if (this.lz.getInteractionHeight() != var2) {
            this.lz.setInteractionHeight(var2);
         }
      }
   }

   public void hideDisplays() {
      if (!this.lG) {
         Location var1 = new Location(this.lK.getWorld(), 0.0, -1000.0, 0.0);
         ObjectListIterator var2 = this.lw.iterator();

         while (var2.hasNext()) {
            ItemDisplay var3 = (ItemDisplay)var2.next();
            NativePacketsHolder.PD().teleportFor(var3, this.lK, var1);
         }

         this.setInteractionSize(0.0F, 0.0F);
         this.lG = true;
      }
   }

   public void reset(boolean var1) {
      if (var1) {
         this.clearDisplays();
      }

      this.lw.clear();
      this.lG = true;
      this.lx = null;
      this.ly = null;
      this.lA = 0L;
      this.lB = 0L;
      this.lC = 0L;
      this.lD = false;
      this.lE = null;
      this.lF = false;
      this.lR.clear();
   }
}
