package dev.lone.itemsadder.entity;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BoneStateList {
   private final List Di;

   public BoneStateList(List var1) {
      this.Di = var1;
   }

   public static BoneStateList of(Collection var0) {
      ArrayList var1 = new ArrayList();

      for (CustomEntityDefinition var3 : var0) {
         if (var3.Aq && var3.Ar) {
            BoneState var4 = createState(var3.getId(), var3.getBoneTransforms(), var3.getScaleFactor());
            if (var4 != null) {
               var1.add(var4);
            }
         }
      }

      if (var1.size() > 128) {
         throw new IllegalArgumentException("Automatic damageable parts exceed 128; configure custom parts");
      } else {
         return var1.isEmpty() ? null : new BoneStateList(List.copyOf(var1));
      }
   }

   public static BoneState createState(String var0, float[] var1, float var2) {
      if (var1.length == 0) {
         return null;
      }

      if (var1.length % 3 == 0 && Float.isFinite(var2) && !(var2 <= 0.0F)) {
         float[] var3 = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY};
         float[] var4 = new float[]{Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};

         for (int var5 = 0; var5 < var1.length; var5++) {
            float var6 = var1[var5] * var2;
            if (!Float.isFinite(var6)) {
               throw new IllegalArgumentException("Invalid automatic damageable geometry");
            }

            int var7 = var5 % 3;
            var3[var7] = Math.min(var3[var7], var6);
            var4[var7] = Math.max(var4[var7], var6);
         }

         return new BoneState(
            UUID.nameUUIDFromBytes(("itemsadder:automatic:" + var0).getBytes(StandardCharsets.UTF_8)),
            var0,
            readFloat((var3[0] + var4[0]) / 2.0F, -1024.0, 1024.0, "automatic center"),
            readFloat((var3[1] + var4[1]) / 2.0F, -1024.0, 1024.0, "automatic center"),
            readFloat((var3[2] + var4[2]) / 2.0F, -1024.0, 1024.0, "automatic center"),
            readFloatOr(Math.max(0.01F, var4[0] - var3[0]), 64.0, "automatic width"),
            readFloatOr(Math.max(0.01F, var4[2] - var3[2]), 64.0, "automatic depth"),
            readFloatOr(Math.max(0.01F, var4[1] - var3[1]), 64.0, "automatic height"),
            null,
            1.0F
         );
      } else {
         throw new IllegalArgumentException("Invalid automatic damageable geometry");
      }
   }

   public BoneStateList parseList(JSONObject var1) {
      HashSet var2 = new HashSet();
      JSONArray var3 = new JSONArray();

      for (BoneState var5 : this.Di) {
         String var6 = var5.id().toString();
         var2.add(var6);
         JSONObject var7 = JSONObject.of(
            "id", var6, "bone", var5.bone(), "center", JSONArray.of(var5.x(), var5.y(), var5.z()), "width", var5.rotX(), "height", var5.rotZ()
         );
         var7.put("depth", var5.rotY());
         var7.put("bodyDamage", var5.scale());
         if (var5.health() != null) {
            var7.put("health", var5.health());
         }

         if (var1.containsKey(var6)) {
            if (!(var1.get(var6) instanceof JSONObject var8)
               || !Set.of("enabled", "center", "width", "depth", "height", "health", "body_damage").containsAll(var8.keySet())) {
               throw new IllegalArgumentException("Invalid damageable part override: " + var6);
            }

            if (var8.containsKey("enabled") && !(var8.get("enabled") instanceof Boolean)) {
               throw new IllegalArgumentException("Part override enabled must be boolean");
            }

            if (Boolean.FALSE.equals(var8.get("enabled"))) {
               continue;
            }

            for (String var10 : var8.keySet()) {
               if (!var10.equals("enabled")) {
                  if (var10.equals("health") && "body".equals(var8.get(var10))) {
                     var7.remove("health");
                  } else {
                     var7.put(var10.equals("body_damage") ? "bodyDamage" : var10, var8.get(var10));
                  }
               }
            }
         }

         var3.add(var7);
      }

      if (!var2.containsAll(var1.keySet())) {
         throw new IllegalArgumentException("Damageable part override references an unknown UUID");
      } else {
         return var3.isEmpty() ? null : parseListStatic(JSONObject.of("parts", var3));
      }
   }

   public static BoneStateList parseListStatic(JSONObject var0) {
      if (var0.keySet().equals(Set.of("parts")) && var0.get("parts") instanceof JSONArray var1 && !var1.isEmpty() && var1.size() <= 128) {
         HashSet var19 = new HashSet();
         HashSet var3 = new HashSet();
         ArrayList var4 = new ArrayList();

         for (Object var6 : var1) {
            if (var6 instanceof JSONObject var7
               && var7.keySet().containsAll(Set.of("id", "bone", "center", "width", "height", "bodyDamage"))
               && Set.of("id", "bone", "center", "width", "depth", "height", "bodyDamage", "health").containsAll(var7.keySet())) {
               String var8 = readString(var7.get("id"), "part id");

               UUID var9;
               try {
                  var9 = UUID.fromString(var8);
               } catch (IllegalArgumentException var18) {
                  throw new IllegalArgumentException("Invalid part UUID", var18);
               }

               if (var9.toString().equals(var8) && var19.add(var9)) {
                  String var10 = readString(var7.get("bone"), "part bone");
                  if (!var3.add(var10)) {
                     throw new IllegalArgumentException("Duplicate damageable part bone");
                  }

                  if (var7.get("center") instanceof JSONArray var11 && var11.size() == 3) {
                     float var20 = readFloat(var11.get(0), -1024.0, 1024.0, "part center");
                     float var13 = readFloat(var11.get(1), -1024.0, 1024.0, "part center");
                     float var14 = readFloat(var11.get(2), -1024.0, 1024.0, "part center");
                     float var15 = readFloatOr(var7.get("width"), 64.0, "part width");
                     float var16 = var7.containsKey("depth") ? readFloatOr(var7.get("depth"), 64.0, "part depth") : var15;
                     Float var17 = var7.containsKey("health") ? readFloatOr(var7.get("health"), 1024.0, "part health") : null;
                     var4.add(
                        new BoneState(
                           var9,
                           var10,
                           var20,
                           var13,
                           var14,
                           var15,
                           var16,
                           readFloatOr(var7.get("height"), 64.0, "part height"),
                           var17,
                           readFloat(var7.get("bodyDamage"), 0.0, 1024.0, "part bodyDamage")
                        )
                     );
                     continue;
                  }

                  throw new IllegalArgumentException("Part center requires three coordinates");
               }

               throw new IllegalArgumentException("Invalid or duplicate part UUID");
            }

            throw new IllegalArgumentException("Invalid damageable part fields");
         }

         return new BoneStateList(List.copyOf(var4));
      } else {
         throw new IllegalArgumentException("Damageable parts requires 1 to 128 parts");
      }
   }

   private static String readString(Object var0, String var1) {
      if (var0 instanceof String var2 && !var2.isBlank()) {
         return var2;
      } else {
         throw new IllegalArgumentException("Invalid " + var1);
      }
   }

   private static float readFloat(Object var0, double var1, double var3, String var5) {
      if (var0 instanceof Number var6 && Double.isFinite(var6.doubleValue()) && !(var6.doubleValue() < var1) && !(var6.doubleValue() > var3)) {
         return var6.floatValue();
      } else {
         throw new IllegalArgumentException("Invalid " + var5);
      }
   }

   private static float readFloatOr(Object var0, double var1, String var3) {
      float var4 = readFloat(var0, 0.0, var1, var3);
      if (!(var4 > 0.0F)) {
         throw new IllegalArgumentException("Invalid " + var3);
      } else {
         return var4;
      }
   }

   public List getStates() {
      return this.Di;
   }
}
