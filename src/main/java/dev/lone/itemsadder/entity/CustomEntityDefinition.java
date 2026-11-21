package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.anim.AnimationPresetLibrary;
import dev.lone.itemsadder.core.NamespacedIdentity;
import dev.lone.itemsadder.util.CustomHashMap;
import dev.lone.itemsadder.util.FloatVector3;
import dev.lone.itemsadder.util.Rotation3D;
import java.util.EnumMap;
import java.util.HashMap;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class CustomEntityDefinition implements NamespacedIdentity {
   @Nullable
   public Matrix4fc zZ;
   @Nullable
   private Matrix4fc Aa;
   @Nullable
   private TransformDecomposition Ab;
   @Nullable
   private CustomEntityDefinition Ac;
   private int Ad = -1;
   private float Ae = 1.0F;
   private final CustomEntityModel Af;
   private final String Ag;
   private final String Ah;
   private FloatVector3 Ai;
   private FloatVector3 Aj;
   private Rotation3D Ak;
   private final int Al;
   public final String[] Am;
   public BonePurpose An;
   public boolean Ao;
   public boolean Ap;
   public boolean Aq;
   public boolean Ar;
   private final String As;
   final HashMap At;
   final CustomHashMap Au;
   private int[] Av = new int[0];
   private String[] Aw = new String[0];
   private EnumMap Ax;
   private float[] Ay = new float[0];
   @Nullable
   public CustomEntityDefinition Az;
   public final boolean field115;

   public CustomEntityDefinition(
      CustomEntityModel var1, String var2, String var3, FloatVector3 var4, FloatVector3 var5, int var6, BonePurpose var7, String[] var8, HashMap var9
   ) {
      this(var1, var1.getNamespace(), var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public CustomEntityDefinition(
      CustomEntityModel var1,
      String var2,
      String var3,
      String var4,
      FloatVector3 var5,
      FloatVector3 var6,
      int var7,
      BonePurpose var8,
      String[] var9,
      HashMap var10
   ) {
      this.Af = var1;
      this.Ag = var2;
      this.Ah = var3;
      this.Ai = new FloatVector3(var5.x / 16.0F, var5.y / 16.0F, var5.z / 16.0F);
      this.Aj = var6;
      this.Al = var7;
      this.Am = var9;
      this.At = var10;
      this.field115 = var1.AM && (aU(var3) || var1.ba(var3));
      this.Ak = new Rotation3D(RotationOrder.YXZ, RotationConvention.VECTOR_OPERATOR, Math.toRadians(var6.x), Math.toRadians(var6.y), Math.toRadians(var6.z));
      this.Au = new CustomHashMap();
      var10.forEach((var1x, var2x) -> this.Au.put(var1x.hashCode(), var2x));
      if (this.field115) {
         this.As = var10.isEmpty() ? aX(var3) : var1.getNamespace() + ":entity/" + var4 + "/" + var3;
      } else {
         this.As = this.getNamespace() + ":entity/" + var4 + "/" + var3;
      }

      this.setPurpose(var8);
   }

   public static boolean aU(String var0) {
      return aW(var0) || aV(var0);
   }

   public static boolean aV(String var0) {
      switch (var0) {
         case "hat_slot":
         case "left_arm_slot":
         case "right_arm_slot":
            return true;
         default:
            return var0.contains("dummy_");
      }
   }

   public static boolean aW(String var0) {
      switch (var0) {
         case "parm_left_3":
         case "parm_right_4":
         case "pbody_2":
         case "phead_0":
         case "pleg_left_1":
         case "pleg_right_5":
            return true;
         default:
            return var0.contains("dummy_");
      }
   }

   public static String aX(String var0) {
      String var1 = AnimationPresetLibrary.bw(var0);
      return "_iainternal:entity/player/" + (var1 == null ? var0 : var1);
   }

   public void setPurpose(BonePurpose var1) {
      this.An = var1;
      this.Aq = var1 != BonePurpose.MOUNT_PIVOT && var1 != BonePurpose.LOCATOR_PIVOT;
      this.Ar = var1.nO();
      this.Ap = var1 == BonePurpose.LEFT_HAND_PIVOT || var1 == BonePurpose.RIGHT_HAND_PIVOT || var1 == BonePurpose.HAT_PIVOT;
   }

   public void setTransform(FloatVector3 var1, FloatVector3 var2, Rotation3D var3) {
      this.Ai = var1;
      this.Aj = var2;
      this.Ak = var3;
   }

   public void setParent(@Nullable CustomEntityDefinition var1) {
      this.Az = var1;
   }

   public FloatVector3 getOffset() {
      return this.Ai;
   }

   public FloatVector3 getScale() {
      return this.Aj;
   }

   public Rotation3D getRotation() {
      return this.Ak;
   }

   @Override
   public boolean ny() {
      return true;
   }

   @Override
   public int nz() {
      return this.Al;
   }

   public HashMap nA() {
      return this.At;
   }

   @Override
   public boolean nB() {
      return false;
   }

   @Deprecated
   @Override
   public String method8734(boolean var1, boolean var2) {
      if (this.ny()) {
         return this.As.contains(":") ? this.As : (var2 ? this.getNamespace() + ":" : "") + this.As;
      } else {
         return (var2 ? this.getNamespace() + ":" : "") + "item/ia_auto/" + this.getId() + (var1 ? ".json" : "");
      }
   }

   @Override
   public String nC() {
      return this.As;
   }

   @Override
   public String getNamespace() {
      return this.Ag;
   }

   @Override
   public String getId() {
      return this.Ah;
   }

   @Override
   public String getNamespacedID() {
      return this.Ag + ":" + this.Ah;
   }

   public int method8735(Integer var1) {
      Integer var2 = (Integer)this.Au.get(var1);
      return var2 == null ? this.Al : var2;
   }

   void method8736(int[] var1, String[] var2) {
      this.Av = var1;
      this.Aw = var2;
   }

   void method8737(EnumMap var1) {
      this.Ax = new EnumMap(var1);
      BoneHierarchy[] var2 = (BoneHierarchy[])var1.get(RotationPolicy.COMPATIBLE);
      if (var2 != null) {
         int var3 = 0;

         for (BoneHierarchy var7 : var2) {
            if (var7 != null) {
               var3 += var7.ou().length;
            }
         }

         this.Ay = new float[var3];
         int var10 = 0;

         for (BoneHierarchy var8 : var2) {
            if (var8 != null) {
               float[] var9 = var8.ou();
               System.arraycopy(var9, 0, this.Ay, var10, var9.length);
               var10 += var9.length;
            }
         }
      }
   }

   public float[] getBoneTransforms() {
      return this.Ay;
   }

   void method8738(Matrix4fc var1, TransformDecomposition var2, float var3) {
      this.Aa = new Matrix4f(var1);
      this.Ab = var2;
      this.Ae = var3;
   }

   void method8739(@Nullable CustomEntityDefinition var1, int var2, Matrix4fc var3) {
      this.Ac = var1;
      this.Ad = var2;
      this.zZ = new Matrix4f().scaling(-1.0F, 1.0F, -1.0F).mul(var3).scale(this.Ae);
   }

   void method8740(int var1) {
      this.Ad = var1;
   }

   @Nullable
   public Matrix4fc getTransform() {
      return this.Aa;
   }

   @Nullable
   public TransformDecomposition getDecomposition() {
      return this.Ab;
   }

   @Nullable
   public CustomEntityDefinition getParent() {
      return this.Ac;
   }

   public int nH() {
      return this.Ad;
   }

   public float getScaleFactor() {
      return this.Ae;
   }

   public boolean nJ() {
      return this.Ax != null;
   }

   public BoneHierarchy[] method8741(RotationPolicy var1) {
      if (this.Ax == null) {
         return new BoneHierarchy[0];
      }

      BoneHierarchy[] var2 = (BoneHierarchy[])this.Ax.get(var1);
      return var2 == null ? new BoneHierarchy[0] : (BoneHierarchy[])var2.clone();
   }

   public int method8742(int var1) {
      return var1 >= 0 && var1 < this.Av.length ? this.Av[var1] : this.Al;
   }

   public int nK() {
      return this.Aw.length;
   }

   @Nullable
   public String method8743(int var1) {
      return this.Aw[var1];
   }

   @Override
   public String toString() {
      return "Bone{entityModel="
         + this.Af.getNamespacedID()
         + ", id='"
         + this.Ah
         + "', localPos="
         + this.Ai
         + ", localRot="
         + this.Aj
         + ", customModelData="
         + this.Al
         + ", type="
         + this.An
         + ", isRenderable="
         + this.Aq
         + ", hasModel="
         + this.Ar
         + ", modelPath='"
         + this.As
         + "', baseHeadBone="
         + (this.Az != null ? this.Az.getNamespacedID() : "none")
         + "}";
   }

   public boolean nL() {
      return this.An.nN() || this.Az != null && this.Az.An.nN();
   }
}
