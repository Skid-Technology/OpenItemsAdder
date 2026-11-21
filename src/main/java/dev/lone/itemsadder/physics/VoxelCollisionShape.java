package dev.lone.itemsadder.physics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public final class VoxelCollisionShape {
   private final int IJ;
   private final int IK;
   private final int IL;
   private final int IM;
   private final double[] IN;
   private final BoxBounds[][] field121;
   private final boolean IP;
   private static final BoxBounds[] IQ = new BoxBounds[0];

   private VoxelCollisionShape(int var1, int var2, int var3, int var4, double[] var5, BoxBounds[][] var6, boolean var7) {
      this.IJ = var1;
      this.IK = var2;
      this.IL = var3;
      this.IM = var4;
      this.IN = var5;
      this.field121 = var6;
      this.IP = var7;
   }

   public static VoxelCollisionShape method8990(double var0) {
      double[] var2 = new double[1];
      Arrays.fill(var2, var0);
      return new VoxelCollisionShape(0, 0, 1, 1, var2, null, true);
   }

   public static VoxelCollisionShape method8991(World var0, double var1, double var3, double var5) {
      int var7 = (int)Math.floor(var1);
      int var8 = (int)Math.floor(var5);
      return method8993(var0, var7 - 2, var7 + 2, var8 - 2, var8 + 2, var3);
   }

   static VoxelCollisionShape method8992(World var0, double var1, double var3, double var5, double var7, double var9) {
      return method8993(var0, (int)Math.floor(var1) - 2, (int)Math.floor(var3) + 2, (int)Math.floor(var5) - 2, (int)Math.floor(var7) + 2, var9);
   }

   private static VoxelCollisionShape method8993(World var0, int var1, int var2, int var3, int var4, double var5) {
      int var7 = var2 - var1 + 1;
      int var8 = var4 - var3 + 1;
      double[] var9 = new double[var7 * var8];
      BoxBounds[][] var10 = new BoxBounds[var7 * var8][];
      boolean var11 = true;
      double var12 = Double.NaN;
      Vector var14 = new Vector(0, -1, 0);
      Location var15 = new Location(var0, 0.0, var5 + 3.0, 0.0);

      for (int var16 = var1; var16 <= var2; var16++) {
         for (int var17 = var3; var17 <= var4; var17++) {
            var15.setX(var16 + 0.5);
            var15.setZ(var17 + 0.5);
            int var18 = (var16 - var1) * var8 + var17 - var3;
            RayTraceResult var19 = var0.rayTraceBlocks(var15, var14, 34.0, FluidCollisionMode.NEVER, true);
            var9[var18] = var19 == null ? Double.NaN : var19.getHitPosition().getY();
            if (var19 != null && var19.getHitBlock() != null) {
               if (Double.isNaN(var12)) {
                  var12 = var9[var18];
               } else if (Math.abs(var12 - var9[var18]) > 1.0E-6) {
                  var11 = false;
               }

               ArrayList var20 = new ArrayList();
               int var21 = var19.getHitBlock().getY();

               for (int var22 = var21; var22 >= Math.max(var21 - 3, var0.getMinHeight()); var22--) {
                  Block var23 = var0.getBlockAt(var16, var22, var17);

                  for (BoundingBox var25 : var23.getCollisionShape().getBoundingBoxes()) {
                     var20.add(
                        new BoxBounds(
                           var23.getX() + var25.getMinX(),
                           var22 + var25.getMinY(),
                           var23.getZ() + var25.getMinZ(),
                           var23.getX() + var25.getMaxX(),
                           var22 + var25.getMaxY(),
                           var23.getZ() + var25.getMaxZ()
                        )
                     );
                  }
               }

               var10[var18] = var20.toArray(IQ);
               BoxBounds var26 = var20.isEmpty() ? null : (BoxBounds)var20.getFirst();
               if (var26 == null
                  || Math.abs(var26.IR - var16) > 1.0E-6
                  || Math.abs(var26.IU - var16 - 1.0) > 1.0E-6
                  || Math.abs(var26.IT - var17) > 1.0E-6
                  || Math.abs(var26.IW - var17 - 1.0) > 1.0E-6
                  || Math.abs(var26.IV - var9[var18]) > 1.0E-6) {
                  var11 = false;
               }
            } else {
               var11 = false;
               var10[var18] = IQ;
            }
         }
      }

      return new VoxelCollisionShape(var1, var3, var7, var8, var9, var11 ? null : var10, var11);
   }

   boolean st() {
      return this.IP;
   }

   void method8994(Consumer var1) {
      if (this.field121 != null) {
         for (BoxBounds[] var5 : this.field121) {
            for (BoxBounds var9 : var5) {
               var1.accept(var9);
            }
         }
      }
   }

   BoxBounds[] method8995(double var1, double var3) {
      if (this.IP) {
         return IQ;
      }

      int var5 = (int)Math.floor(var1) - this.IJ;
      int var6 = (int)Math.floor(var3) - this.IK;
      return var5 >= 0 && var5 < this.IL && var6 >= 0 && var6 < this.IM ? this.field121[var5 * this.IM + var6] : IQ;
   }

   public double method8996(double var1, double var3) {
      if (this.IP) {
         return this.IN[0];
      }

      int var5 = (int)Math.floor(var1) - this.IJ;
      int var6 = (int)Math.floor(var3) - this.IK;
      return var5 >= 0 && var5 < this.IL && var6 >= 0 && var6 < this.IM ? this.IN[var5 * this.IM + var6] : Double.NaN;
   }

   public boolean method8997(double var1, double var3, double var5, double var7) {
      return this.IP && this.IL == 1 && this.IM == 1
         || (int)Math.floor(var1) > this.IJ
            && (int)Math.floor(var3) < this.IJ + this.IL - 1
            && (int)Math.floor(var5) > this.IK
            && (int)Math.floor(var7) < this.IK + this.IM - 1;
   }

   boolean method8998(VoxelCollisionShape var1) {
      if (var1 == null
         || this.IJ != var1.IJ
         || this.IK != var1.IK
         || this.IL != var1.IL
         || this.IM != var1.IM
         || this.IP != var1.IP
         || !Arrays.equals(this.IN, var1.IN)) {
         return false;
      }

      if (this.field121 == var1.field121) {
         return true;
      }

      if (this.field121 != null && var1.field121 != null) {
         for (int var2 = 0; var2 < this.field121.length; var2++) {
            BoxBounds[] var3 = this.field121[var2];
            BoxBounds[] var4 = var1.field121[var2];
            if (var3.length != var4.length) {
               return false;
            }

            for (int var5 = 0; var5 < var3.length; var5++) {
               BoxBounds var6 = var3[var5];
               BoxBounds var7 = var4[var5];
               if (var6.IR != var7.IR || var6.IS != var7.IS || var6.IT != var7.IT || var6.IU != var7.IU || var6.IV != var7.IV || var6.IW != var7.IW) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
