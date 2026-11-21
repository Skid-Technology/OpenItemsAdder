package dev.lone.itemsadder.entity;

import beer.devs.nativepackets.api.NativeValue;
import dev.lone.itemsadder.anim.ComposedDisplayState;
import dev.lone.itemsadder.anim.InterpolationCache;
import dev.lone.itemsadder.network.NativePacketsHolder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public final class AnimatedDisplayEntity extends DisplayEntity {
   private static final RotationPolicy[] aSP = RotationPolicy.values();
   private final CustomEntityImpl aSQ;
   private final CustomEntityDefinition aSR;
   private final CustomEntityModel aSS;
   private final ComposedDisplayState[] aST = new ComposedDisplayState[aSP.length];
   private final ComposedDisplayState[] aSU = new ComposedDisplayState[aSP.length];
   private final Location aSV;
   private final Matrix4f aSW = new Matrix4f();
   private boolean aSX;
   private int interpolationDuration;
   private boolean aSY;
   private int Mw;
   private int[] aSZ = new int[0];
   private boolean aTa;
   private boolean aTb;
   @Nullable
   private NativeValue aTc;

   public AnimatedDisplayEntity(Location var1, CustomEntityModel var2, CustomEntityDefinition var3, CustomEntityImpl var4) {
      super(var1, false, var2, var4);
      this.aSV = new Location(null, var1.getX(), var1.getY(), var1.getZ(), var1.getYaw(), var1.getPitch());
      this.aSS = var2;
      this.aSR = var3;
      this.aSQ = var4;

      for (RotationPolicy var8 : aSP) {
         BoneHierarchy[] var9 = var3.method8741(var8);
         if (var9.length != 0) {
            DisplayEntity[] var10 = new DisplayEntity[var9.length - 1];
            Matrix4f[] var11 = new Matrix4f[var10.length];

            for (int var12 = 0; var12 < var10.length; var12++) {
               var10[var12] = new DisplayEntity(var1, false, var2, var4);
               var11[var12] = new Matrix4f();
            }

            this.aSU[var8.ordinal()] = new ComposedDisplayState(var9, var10, var11, new InterpolationCache());
         }
      }
   }

   @Override
   public void NG() {
      super.NG();

      for (ComposedDisplayState var4 : this.aSU) {
         if (var4 != null) {
            for (DisplayEntity var8 : var4.aTe) {
               var8.NG();
            }
         }
      }
   }

   public boolean method3129(RotationPolicy var1) {
      this.aSQ.flushIfActive();
      if (this.aST[var1.ordinal()] != null) {
         return true;
      }

      if (!this.aSQ.isLifecycleDone()) {
         return false;
      }

      if (this.aSY) {
         this.NK();
      }

      ComposedDisplayState var2 = this.aSU[var1.ordinal()];
      if (var2 == null) {
         throw new IllegalStateException("Missing geometry fragments for " + this.aSR.getNamespacedID() + " profile " + var1.getKey());
      }

      for (int var3 = 0; var3 < var2.aTe.length; var3++) {
         DisplayEntity var4 = var2.aTe[var3];
         var4.setColor(this.arJ);
         var4.setEnchanted(this.aQu);
         var4.method2999(this.aSV.getX(), this.aSV.getY(), this.aSV.getZ(), true);
         if (this.aSX) {
            Matrix4f var5 = multiplyTransforms(this.aSW, var2.aTd[var3 + 1].getRootTransform(), var2.aTf[var3]);
            if (this.aSY) {
               var4.method3065(var5, this.interpolationDuration);
            } else {
               var4.method3060(var5, this.interpolationDuration);
            }
         }

         var4.addUpdatePackets(List.of(), this.method3144(var2.aTd[var3 + 1], false));
      }

      this.aST[var1.ordinal()] = var2;
      if (!this.aSQ.isLifecycleDone()) {
         this.aST[var1.ordinal()] = null;
         return false;
      }

      for (DisplayEntity var6 : var2.aTe) {
         this.aSQ.scheduleBoneUpdate(var6.aQn);
      }

      this.aSQ.clearPendingSyncs();
      return true;
   }

   public int[] method3130(RotationPolicy var1) {
      ComposedDisplayState var2 = this.aST[var1.ordinal()];
      if (var2 == null) {
         return new int[]{this.aQn};
      }

      int[] var3 = new int[var2.aTe.length];

      for (int var4 = 0; var4 < var2.aTe.length; var4++) {
         var3[var4] = var2.aTe[var4].aQn;
      }

      return appendIndex(this.aQn, var3);
   }

   public List method3131(RotationPolicy var1) {
      ArrayList var2 = new ArrayList();
      var2.add(this.snapshot());
      ComposedDisplayState var3 = this.aST[var1.ordinal()];
      if (var3 != null) {
         for (DisplayEntity var7 : var3.aTe) {
            var2.add(var7.snapshot());
         }
      }

      return List.copyOf(var2);
   }

   public int[] Od() {
      LinkedHashSet var1 = new LinkedHashSet();
      var1.add(this.aQn);

      for (ComposedDisplayState var5 : this.aST) {
         if (var5 != null) {
            for (DisplayEntity var9 : var5.aTe) {
               var1.add(var9.aQn);
            }
         }
      }

      return var1.stream().mapToInt(Integer::intValue).toArray();
   }

   public void method3132(int var1) {
      int var2 = Math.max(0, var1);
      if (this.Mw != var2) {
         this.Mw = var2;
         this.Oe();
      }
   }

   public boolean method3133(int[] var1) {
      if (Arrays.equals(this.aSZ, var1)) {
         return false;
      }

      this.aSZ = (int[])var1.clone();
      this.Oe();
      return true;
   }

   @Override
   public void method3060(Matrix4f var1, int var2) {
      if (this.aSX && this.aSW.equals(var1) && this.interpolationDuration == var2 && !this.aSY) {
         super.method3060(var1, var2);
      } else {
         this.aSW.set(var1);
         this.aSX = true;
         this.interpolationDuration = var2;
         this.aSY = false;
         super.method3060(var1, var2);
         this.Oe();

         for (ComposedDisplayState var6 : this.aST) {
            if (var6 != null) {
               for (int var7 = 0; var7 < var6.aTe.length; var7++) {
                  var6.aTe[var7].method3060(multiplyTransforms(var1, var6.aTd[var7 + 1].getRootTransform(), var6.aTf[var7]), var2);
               }
            }
         }
      }
   }

   @Override
   public void method3061(Matrix4f var1) {
      super.method3061(var1);

      for (ComposedDisplayState var5 : this.aST) {
         if (var5 != null) {
            for (int var6 = 0; var6 < var5.aTe.length; var6++) {
               var5.aTe[var6].method3061(multiplyTransforms(var1, var5.aTd[var6 + 1].getRootTransform(), var5.aTf[var6]));
            }
         }
      }
   }

   @Override
   public void method3062(BoneMatrixView var1, int var2) {
      super.method3062(var1, var2);

      for (ComposedDisplayState var6 : this.aST) {
         if (var6 != null) {
            for (int var7 = 0; var7 < var6.aTe.length; var7++) {
               var6.aTe[var7].method3063(var1, var2, var6.aTd[var7 + 1].getRootTransform());
            }
         }
      }
   }

   @Override
   protected void NK() {
      if (this.aSY) {
         this.method3060(this.getTransformMatrix(), 1);
      }

      super.NK();
   }

   @Override
   public void method3065(Matrix4f var1, int var2) {
      if (!this.aSX || !this.aSW.equals(var1) || this.interpolationDuration != var2 || !this.aSY) {
         this.aSW.set(var1);
         this.aSX = true;
         this.interpolationDuration = var2;
         this.aSY = true;
         super.method3065(var1, var2);
         this.Oe();

         for (ComposedDisplayState var6 : this.aST) {
            if (var6 != null) {
               for (int var7 = 0; var7 < var6.aTe.length; var7++) {
                  var6.aTe[var7].method3065(multiplyTransforms(var1, var6.aTd[var7 + 1].getRootTransform(), var6.aTf[var7]), var2);
               }
            }
         }
      }
   }

   @Override
   public boolean NI() {
      if (!super.NI()) {
         return false;
      }

      for (ComposedDisplayState var4 : this.aST) {
         if (var4 != null) {
            for (DisplayEntity var8 : var4.aTe) {
               if (!var8.NI()) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public static Matrix4f multiplyTransforms(Matrix4fc var0, Matrix4fc var1, Matrix4f var2) {
      return var2.set(var0).mul(var1);
   }

   public static int[] appendIndex(int var0, int[] var1) {
      int[] var2 = new int[var1.length + 1];
      var2[0] = var0;
      System.arraycopy(var1, 0, var2, 1, var1.length);
      return var2;
   }

   @Override
   public void method2999(double var1, double var3, double var5, boolean var7) {
      this.aSV.setX(var1);
      this.aSV.setY(var3);
      this.aSV.setZ(var5);
      super.method2999(var1, var3, var5, var7);

      for (ComposedDisplayState var11 : this.aST) {
         if (var11 != null) {
            for (DisplayEntity var15 : var11.aTe) {
               var15.method2999(var1, var3, var5, var7);
            }
         }
      }
   }

   @Override
   public void addSpawnPackets(List viewers) {
      for (RotationPolicy var5 : aSP) {
         List var6 = this.method3142(var5, viewers);
         if (!var6.isEmpty() && this.method3129(var5)) {
            ComposedDisplayState var7 = this.aST[var5.ordinal()];
            this.method3136(var7);

            for (EntityViewer var9 : var6) {
               if (var9.kW()) {
                  this.method3137(var9, var7);
               }
            }
         }
      }
   }

   @Override
   public void spawnFor(EntityViewer viewer) {
      if (viewer.kW()) {
         RotationPolicy var2 = viewer.getRotationPolicy();
         if (this.method3129(var2)) {
            ComposedDisplayState var3 = this.aST[var2.ordinal()];
            this.method3136(var3);
            this.method3137(viewer, var3);
         }
      }
   }

   private void method3136(ComposedDisplayState var1) {
      this.method3143(var1);

      for (int var2 = 0; var2 < var1.aTe.length; var2++) {
         DisplayEntity var3 = var1.aTe[var2];
         int var4 = this.method3144(var1.aTd[var2 + 1], this.aTa);
         if (this.aTb) {
            var3.addUpdatePackets(List.of(), -1);
         } else if (this.aTa) {
            this.method3145(var3, List.of(), var4);
         } else {
            var3.addUpdatePackets(List.of(), var4);
         }
      }
   }

   private void method3137(EntityViewer var1, ComposedDisplayState var2) {
      this.method3070(var1);

      for (DisplayEntity var6 : var2.aTe) {
         var6.spawnFor(var1);
      }
   }

   @Override
   public void despawnFor(EntityViewer viewer) {
      super.despawnFor(viewer);
      ComposedDisplayState var2 = this.aST[viewer.getRotationPolicy().ordinal()];
      if (var2 != null) {
         for (DisplayEntity var6 : var2.aTe) {
            var6.despawnFor(viewer);
         }
      }
   }

   @Override
   public void addUpdatePackets(List viewers, int ignored) {
      this.aTa = false;
      this.Oe();
      if (this.aTb) {
         this.method3141(viewers);
      } else {
         this.method3138(viewers, false);
      }
   }

   @Override
   public void updateFor(EntityViewer viewer, int ignored) {
      this.aTa = false;
      this.Oe();
      if (this.method3129(viewer.getRotationPolicy())) {
         if (this.aTb) {
            this.method3141(List.of(viewer));
         } else {
            this.method3139(viewer, false);
         }
      }
   }

   @Override
   public void addTickPackets(List viewers, int ignored) {
      this.aTa = true;
      this.Oe();
      if (this.aTb) {
         this.method3141(viewers);
      } else {
         this.method3138(viewers, true);
      }
   }

   private void method3138(List var1, boolean var2) {
      this.NK();

      for (RotationPolicy var6 : aSP) {
         List var7 = this.method3142(var6, var1);
         if (!var7.isEmpty() && this.method3129(var6)) {
            ComposedDisplayState var8 = this.aST[var6.ordinal()];
            this.method3146(var8, var7);

            for (int var9 = 0; var9 < var8.aTe.length; var9++) {
               int var10 = this.method3144(var8.aTd[var9 + 1], var2);
               if (var2) {
                  this.method3145(var8.aTe[var9], var7, var10);
               } else {
                  var8.aTe[var9].addUpdatePackets(var7, var10);
               }
            }
         }
      }

      this.NQ();
   }

   private void method3139(EntityViewer var1, boolean var2) {
      this.NK();
      ComposedDisplayState var3 = this.aST[var1.getRotationPolicy().ordinal()];
      this.method3146(var3, List.of(var1));

      for (int var4 = 0; var4 < var3.aTe.length; var4++) {
         int var5 = this.method3144(var3.aTd[var4 + 1], var2);
         if (var2) {
            this.method3145(var3.aTe[var4], List.of(var1), var5);
         } else {
            var3.aTe[var4].updateFor(var1, var5);
         }
      }

      this.NQ();
   }

   @Override
   public void addMetadataPackets(List viewers) {
      this.method3087(viewers);
      boolean var2 = this.aQs || this.aQr;

      for (RotationPolicy var6 : aSP) {
         List var7 = this.method3142(var6, viewers);
         if (!var7.isEmpty()) {
            ComposedDisplayState var8 = this.aST[var6.ordinal()];
            if (var8 == null) {
               if (!this.method3129(var6)) {
                  continue;
               }

               var8 = this.aST[var6.ordinal()];
            }

            for (EntityViewer var10 : var7) {
               if (var10.kW() && var10.kX()) {
                  if (this.aRr || var10.la()) {
                     this.method3147(var8, var10);
                  } else if (var2 || this.method3088(var10)) {
                     this.method3084(var10);
                  }
               }
            }

            for (DisplayEntity var12 : var8.aTe) {
               var12.addMetadataPackets(var7);
            }
         }
      }

      this.NQ();
   }

   @Override
   public void addEquipmentPackets(boolean fixClientDelay, @Nullable LivingEntity baseEntity, List viewers) {
      super.addEquipmentPackets(fixClientDelay, baseEntity, viewers);

      for (RotationPolicy var7 : aSP) {
         ComposedDisplayState var8 = this.aST[var7.ordinal()];
         if (var8 != null) {
            List var9 = this.method3142(var7, viewers);

            for (DisplayEntity var13 : var8.aTe) {
               var13.addEquipmentPackets(fixClientDelay, baseEntity, var9);
            }
         }
      }
   }

   @Override
   public void sendEquipment(@Nullable Player basePlayer, List viewers, ItemStack item) {
      this.method3072(viewers, NativePacketsHolder.PE().Py().wrapBukkitItem(item == null ? new ItemStack(Material.AIR) : item.clone()));
   }

   @Override
   public void method3072(List var1, NativeValue var2) {
      this.aTb = true;
      this.aTc = var2;
      this.method3076(var2);
      this.Oe();
      this.method3141(var1);
   }

   public void method3140(List var1) {
      this.aTb = false;
      this.aTc = null;
      this.Oe();
      this.method3138(var1, this.aTa);
   }

   private void method3141(List var1) {
      this.NK();

      for (RotationPolicy var5 : aSP) {
         ComposedDisplayState var6 = this.aST[var5.ordinal()];
         if (var6 != null) {
            List var7 = this.method3142(var5, var1);
            this.method3146(var6, var7);

            for (DisplayEntity var11 : var6.aTe) {
               var11.addUpdatePackets(var7, -1);
            }
         }
      }

      this.NQ();
   }

   @Override
   public void addTeleportPackets(List viewers) {
      this.Oe();

      for (RotationPolicy var5 : aSP) {
         List var6 = this.method3142(var5, viewers);
         if (!var6.isEmpty()) {
            ComposedDisplayState var7 = this.aST[var5.ordinal()];
            if (var7 != null) {
               this.method3020(var6, (byte)(this.aQf ? 1 : 0));

               for (DisplayEntity var11 : var7.aTe) {
                  var11.method3024(var6, this.aQf);
               }
            }
         }
      }
   }

   @Override
   public void Nz() {
      super.Nz();

      for (ComposedDisplayState var4 : this.aST) {
         if (var4 != null) {
            for (DisplayEntity var8 : var4.aTe) {
               var8.Nz();
            }
         }
      }
   }

   @Override
   public void setColor(int color) {
      super.setColor(color);
      this.Oe();

      for (ComposedDisplayState var5 : this.aST) {
         if (var5 != null) {
            for (DisplayEntity var9 : var5.aTe) {
               var9.setColor(color);
            }
         }
      }
   }

   @Override
   public void setEnchanted(boolean enchanted) {
      super.setEnchanted(enchanted);
      this.Oe();

      for (ComposedDisplayState var5 : this.aST) {
         if (var5 != null) {
            for (DisplayEntity var9 : var5.aTe) {
               var9.setEnchanted(enchanted);
            }
         }
      }
   }

   private List method3142(RotationPolicy var1, List var2) {
      List var3 = this.aSQ.getViewersByRotation(var1);
      if (var2 == this.aSQ.getViewers()) {
         return var3;
      }

      ArrayList var4 = new ArrayList();

      for (EntityViewer var6 : var2) {
         if (var6.getRotationPolicy() == var1) {
            var4.add(var6);
         }
      }

      return var4;
   }

   private void method3143(ComposedDisplayState var1) {
      if (this.aTb) {
         this.method3076(this.aTc);
      } else {
         BoneHierarchy var2 = var1.aTd[0];
         if (this.aTa) {
            this.method3074(this.method3144(var2, true), "texture_swap".equals(this.aSS.nT()) ? this.arJ : cv(this.arJ));
         } else {
            this.method3073(this.method3144(var2, false), false);
         }
      }
   }

   private int method3144(BoneHierarchy var1, boolean var2) {
      if (this.aSZ.length <= 0 && this.aSS.od() <= 0) {
         return this.aSS.method441(this.Mw, var2) >= var1.getBoneCount()
            ? var1.getParentOf(var1.getBoneCount() - 1)
            : var1.getParentOf(this.aSS.method441(this.Mw, var2));
      } else {
         return var1.findBone(this.aSZ, var2 && "texture_swap".equals(this.aSS.nT()));
      }
   }

   private void method3145(DisplayEntity var1, List var2, int var3) {
      if ("texture_swap".equals(this.aSS.nT())) {
         var1.addUpdatePackets(var2, var3);
      } else {
         var1.method3071(var2, var3);
      }
   }

   private void method3146(ComposedDisplayState var1, List var2) {
      for (EntityViewer var4 : var2) {
         if (var4.kW()) {
            if (var4.kX()) {
               this.method3147(var1, var4);
            } else {
               var4.lb();
            }
         }
      }
   }

   private void method3147(ComposedDisplayState var1, EntityViewer var2) {
      int var3 = this.method3066(var2);
      float var4 = this.method3058(var2);
      if (var4 != 0.0F) {
         this.method3143(var1);
         this.method3083(var2, this.method3081(var3, var4), var3);
      } else {
         int var5 = this.NJ();
         Object var6 = var1.aTg.method3151(var3, 127, var5, var3x -> {
            this.method3143(var1);
            return this.method3082(var3x, 0.0F, var5);
         });
         if (var6 == null) {
            this.method3143(var1);
         }

         this.method3083(var2, var6, var3);
      }
   }

   private void Oe() {
      this.NP();
   }

   @Override
   protected void NP() {
      super.NP();

      for (ComposedDisplayState var4 : this.aST) {
         if (var4 != null) {
            var4.aTg.tU();
         }
      }
   }
}
