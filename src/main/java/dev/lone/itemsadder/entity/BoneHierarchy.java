package dev.lone.itemsadder.entity;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.ArrayList;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

public final class BoneHierarchy {
   private final Matrix4f BA;
   private final int[] field117;
   private final String[] BC;
   private final int[] BD;
   private final int[] BE;
   private final int BF;
   private final float[] BG;

   private BoneHierarchy(Matrix4f var1, int[] var2, String[] var3, int[] var4, int[] var5, int var6, @Nullable JSONObject var7) {
      this.BA = new Matrix4f(var1);
      this.field117 = (int[])var2.clone();
      this.BC = (String[])var3.clone();
      this.BD = (int[])var4.clone();
      this.BE = (int[])var5.clone();
      this.BF = var6;
      this.BG = parseBoneMatrices(var1, var7);
   }

   private static float[] parseBoneMatrices(Matrix4f var0, @Nullable JSONObject var1) {
      if (var1 != null && var1.getJSONArray("elements") != null) {
         ArrayList var2 = new ArrayList();
         Vector3f var3 = new Vector3f();

         for (Object var5 : var1.getJSONArray("elements")) {
            JSONObject var6 = (JSONObject)var5;
            JSONArray var7 = var6.getJSONArray("from");
            JSONArray var8 = var6.getJSONArray("to");
            if (var7 != null && var8 != null && var7.size() == 3 && var8.size() == 3) {
               JSONObject var9 = var6.getJSONObject("rotation");

               for (int var10 = 0; var10 < 8; var10++) {
                  float var11 = ((var10 & 1) == 0 ? var7 : var8).getFloatValue(0);
                  float var12 = ((var10 & 2) == 0 ? var7 : var8).getFloatValue(1);
                  float var13 = ((var10 & 4) == 0 ? var7 : var8).getFloatValue(2);
                  if (var9 != null) {
                     JSONArray var14 = var9.getJSONArray("origin");
                     float var15 = (float)Math.toRadians(var9.getFloatValue("angle"));
                     float var16 = (float)Math.sin(var15);
                     float var17 = (float)Math.cos(var15);
                     float var18 = var9.getBooleanValue("rescale") ? 1.0F / var17 : 1.0F;
                     String var19 = var9.getString("axis");
                     if ("x".equals(var19)) {
                        float var20 = var12 - var14.getFloatValue(1);
                        float var21 = var13 - var14.getFloatValue(2);
                        var12 = var14.getFloatValue(1) + (var20 * var17 - var21 * var16) * var18;
                        var13 = var14.getFloatValue(2) + (var20 * var16 + var21 * var17) * var18;
                     } else if ("y".equals(var19)) {
                        float var24 = var13 - var14.getFloatValue(2);
                        float var26 = var11 - var14.getFloatValue(0);
                        var13 = var14.getFloatValue(2) + (var24 * var17 - var26 * var16) * var18;
                        var11 = var14.getFloatValue(0) + (var24 * var16 + var26 * var17) * var18;
                     } else if ("z".equals(var19)) {
                        float var25 = var11 - var14.getFloatValue(0);
                        float var27 = var12 - var14.getFloatValue(1);
                        var11 = var14.getFloatValue(0) + (var25 * var17 - var27 * var16) * var18;
                        var12 = var14.getFloatValue(1) + (var25 * var16 + var27 * var17) * var18;
                     }
                  }

                  var0.transformPosition(var3.set(var11 / 16.0F - 0.5F, var12 / 16.0F - 0.5F, var13 / 16.0F - 0.5F));
                  var2.add(var3.x);
                  var2.add(var3.y);
                  var2.add(var3.z);
               }
            }
         }

         float[] var22 = new float[var2.size()];

         for (int var23 = 0; var23 < var22.length; var23++) {
            var22[var23] = (Float)var2.get(var23);
         }

         return var22;
      } else {
         return new float[0];
      }
   }

   public float[] ou() {
      return this.BG;
   }

   public Matrix4fc getRootTransform() {
      return this.BA;
   }

   public int getParentOf(int var1) {
      return this.field117[Math.max(0, Math.min(var1, this.field117.length - 1))];
   }

   public int findBone(int[] var1, boolean var2) {
      int var3 = 0;
      int var4 = 1;

      for (int var5 = 0; var5 < this.BD.length; var5++) {
         int var6 = this.BD[var5] < var1.length ? var1[this.BD[var5]] : -1;
         if (var6 >= 0) {
            var3 += Math.min(var6, this.BE[var5] - 1) * var4;
         }

         var4 *= this.BE[var5];
      }

      return this.field117[var3 + (var2 ? this.BF : 0)];
   }

   public int getBoneCount() {
      return this.field117.length;
   }

   @Nullable
   public String getBoneName(int var1) {
      return this.BC[var1];
   }
}
