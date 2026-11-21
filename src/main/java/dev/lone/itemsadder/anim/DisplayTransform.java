package dev.lone.itemsadder.anim;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.network.NativePacketsHolder;
import java.util.Set;
import org.bukkit.Color;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class DisplayTransform {
   private static final Set awM = Set.of(
      "context",
      "billboard",
      "translation",
      "leftRotation",
      "scale",
      "rightRotation",
      "interpolationDelay",
      "interpolationDuration",
      "teleportDuration",
      "brightness",
      "viewRange",
      "width",
      "height",
      "shadowRadius",
      "shadowStrength",
      "glowing",
      "glowColor"
   );
   final String awN;
   final Billboard billboard;
   final Vector3f awO;
   final Vector3f awP;
   final Quaternionf awQ;
   final Quaternionf awR;
   private final Vector3f[] awS = new Vector3f[4];
   final int awT;
   final int awU;
   final int awV;
   final Brightness brightness;
   final float awW;
   final float awX;
   final float awY;
   final float shadowRadius;
   final float shadowStrength;
   final boolean glowing;
   final Color awZ;

   public DisplayTransform(JSONObject var1, String var2) {
      JSONObject var3 = var1 == null ? new JSONObject() : var1;

      for (String var5 : var3.keySet()) {
         if (!awM.contains(var5)) {
            throw bj("unknown native property " + var5);
         }
      }

      this.awN = var3.getString("context") == null ? "FIXED" : var3.getString("context");
      if (!this.awN.equals("ON_SHELF")) {
         ItemDisplayTransform.valueOf(this.awN);
      }

      String var10001;
      if (var3.getString("billboard") == null) {
         switch (var2) {
            case "rotate_xyz":
               var10001 = "CENTER";
               break;
            case "rotate_y":
               var10001 = "VERTICAL";
               break;
            default:
               var10001 = "FIXED";
         }
      } else {
         var10001 = var3.getString("billboard");
      }

      this.billboard = Billboard.valueOf(var10001);
      this.awO = method2083(var3, "translation", 0.0F, 0.0F, 0.0F);
      this.awP = method2083(var3, "scale", 1.0F, 1.0F, 1.0F);
      this.awQ = method2084(var3, "leftRotation");
      this.awR = method2084(var3, "rightRotation");
      int var8 = 0;

      for (byte var10 = -1; var10 <= 1; var10 += 2) {
         for (byte var6 = -1; var6 <= 1; var6 += 2) {
            this.awS[var8++] = new Vector3f(var10 * 0.5F, var6 * 0.5F, 0.0F).rotate(this.awR).mul(this.awP);
         }
      }

      this.awT = method2087(var3, "interpolationDelay", 0, 0, 1200);
      this.awU = method2087(var3, "interpolationDuration", 2, 0, 1200);
      this.awV = method2087(var3, "teleportDuration", 2, 0, 59);
      JSONObject var11 = var3.getJSONObject("brightness");
      if (var11 != null && !var11.keySet().equals(Set.of("block", "sky"))) {
         throw bj("brightness needs block and sky");
      }

      this.brightness = var11 == null ? null : new Brightness(method2087(var11, "block", 0, 0, 15), method2087(var11, "sky", 0, 0, 15));
      this.awW = method2086(var3, "viewRange", 1.0F, 0.0F, 1024.0F);
      this.awX = method2086(var3, "width", 0.0F, 0.0F, 4096.0F);
      this.awY = method2086(var3, "height", 0.0F, 0.0F, 4096.0F);
      this.shadowRadius = method2086(var3, "shadowRadius", 0.0F, 0.0F, 64.0F);
      this.shadowStrength = method2086(var3, "shadowStrength", 0.0F, 0.0F, 1.0F);
      if (var3.containsKey("glowing") && !(var3.get("glowing") instanceof Boolean)) {
         throw bj("glowing must be boolean");
      }

      this.glowing = var3.getBooleanValue("glowing");
      this.awZ = var3.containsKey("glowColor") ? Color.fromRGB(method2087(var3, "glowColor", 0, 0, 16777215)) : null;
   }

   public void method2080(ItemDisplay var1) {
      NativePacketsHolder.PD().setItemModel(var1, this.awN);
      var1.setBillboard(this.billboard);
      var1.setInterpolationDelay(this.awT);
      var1.setInterpolationDuration(this.awU);
      var1.setTeleportDuration(this.awV);
      var1.setBrightness(this.brightness);
      var1.setViewRange(this.awW);
      var1.setDisplayWidth(this.awX);
      var1.setDisplayHeight(this.awY);
      var1.setShadowRadius(this.shadowRadius);
      var1.setShadowStrength(this.shadowStrength);
      var1.setGlowing(this.glowing);
      var1.setGlowColorOverride(this.awZ);
   }

   public Transformation method2081(double var1, double var3, Quaternionf var5, double var6) {
      Quaternionf var8 = new Quaternionf(var5).rotateZ((float)var6).mul(this.awQ);
      return new Transformation(
         new Vector3f(this.awO), var8, new Vector3f(this.awP).mul((float)(var1 * 2.0), (float)(var3 * 2.0), 1.0F), new Quaternionf(this.awR)
      );
   }

   double method2082(double var1, double var3) {
      double var5 = 0.0;

      for (Vector3f var10 : this.awS) {
         double var11 = var10.x * var1 * 2.0;
         double var13 = var10.y * var3 * 2.0;
         double var15 = var10.z;
         var5 = Math.max(var5, Math.sqrt(var11 * var11 + var13 * var13 + var15 * var15));
      }

      return Math.max(this.shadowRadius, this.awO.length() + var5 * 1.0001);
   }

   private static Vector3f method2083(JSONObject var0, String var1, float var2, float var3, float var4) {
      if (!var0.containsKey(var1)) {
         return new Vector3f(var2, var3, var4);
      } else {
         JSONArray var5 = var0.getJSONArray(var1);
         if (var5 != null && var5.size() == 3) {
            return new Vector3f(method2085(var5, 0), method2085(var5, 1), method2085(var5, 2));
         } else {
            throw bj(var1 + " must have three components");
         }
      }
   }

   private static Quaternionf method2084(JSONObject var0, String var1) {
      if (!var0.containsKey(var1)) {
         return new Quaternionf();
      }

      JSONArray var2 = var0.getJSONArray(var1);
      if (var2 != null && var2.size() == 4) {
         Quaternionf var3 = new Quaternionf(method2085(var2, 0), method2085(var2, 1), method2085(var2, 2), method2085(var2, 3));
         if (Math.abs(var3.lengthSquared() - 1.0F) > 1.0E-5) {
            throw bj(var1 + " must be a unit quaternion");
         } else {
            return var3;
         }
      } else {
         throw bj(var1 + " must have four components");
      }
   }

   private static float method2085(JSONArray var0, int var1) {
      if (var0.get(var1) instanceof Number var3 && Double.isFinite(var3.doubleValue()) && !(Math.abs(var3.doubleValue()) > 4096.0)) {
         return var3.floatValue();
      } else {
         throw bj("invalid transform component");
      }
   }

   private static float method2086(JSONObject var0, String var1, float var2, float var3, float var4) {
      if (!var0.containsKey(var1)) {
         return var2;
      } else if (var0.get(var1) instanceof Number var6 && Double.isFinite(var6.doubleValue()) && !(var6.doubleValue() < var3) && !(var6.doubleValue() > var4)) {
         return var6.floatValue();
      } else {
         throw bj(var1 + " outside " + var3 + ".." + var4);
      }
   }

   private static int method2087(JSONObject var0, String var1, int var2, int var3, int var4) {
      if (!var0.containsKey(var1)) {
         return var2;
      } else if (var0.get(var1) instanceof Number var6
         && Double.isFinite(var6.doubleValue())
         && !(var6.doubleValue() < var3)
         && !(var6.doubleValue() > var4)
         && var6.doubleValue() == Math.rint(var6.doubleValue())) {
         return var6.intValue();
      } else {
         throw bj(var1 + " must be an integer in " + var3 + ".." + var4);
      }
   }

   private static IllegalArgumentException bj(String var0) {
      return new IllegalArgumentException("Particle display: " + var0);
   }
}
