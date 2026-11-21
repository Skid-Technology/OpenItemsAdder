package dev.lone.itemsadder.anim;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AnimationWindow {
   private final List EC;
   private final float ED;
   private final float field17;
   private final float EF;
   private final float EG;
   private final float EH;

   public AnimationWindow(List var1, float var2, float var3, float var4, float var5, float var6) {
      this.EC = var1;
      this.ED = var2;
      this.field17 = var3;
      this.EF = var4;
      this.EG = var5;
      this.EH = var6;
   }

   public static AnimationWindow method78(JSONObject var0) {
      if (!var0.keySet().equals(Set.of("legs", "maximumOffset", "response", "maxSlopeDegrees", "maxDistance", "contactHeight"))) {
         throw new IllegalArgumentException("Invalid terrain feet fields");
      }

      if (var0.get("legs") instanceof JSONArray var1 && !var1.isEmpty() && var1.size() <= 8) {
         ArrayList var15 = new ArrayList();

         for (Object var4 : var1) {
            if (var4 instanceof JSONObject var5
               && (
                  var5.keySet().equals(Set.of("upper", "lower", "foot", "bend", "soleHeight"))
                     || var5.keySet().equals(Set.of("upper", "lower", "foot", "bend", "soleHeight", "soleOffset"))
               )) {
               for (String var8 : List.of("upper", "lower", "foot")) {
                  if (!(var5.get(var8) instanceof String var6) || var6.isBlank()) {
                     throw new IllegalArgumentException("Terrain leg requires named bones");
                  }
               }

               if (var5.get("bend") instanceof JSONArray var16 && var16.size() == 3) {
                  float var18 = method79(var16.get(0), -1.0F, 1.0F);
                  float var19 = method79(var16.get(1), -1.0F, 1.0F);
                  float var10 = method79(var16.get(2), -1.0F, 1.0F);
                  if (var18 * var18 + var19 * var19 + var10 * var10 < 1.0E-4F) {
                     throw new IllegalArgumentException("Terrain bend direction is empty");
                  }

                  float var11 = 0.0F;
                  float var12 = 0.0F;
                  if (var5.containsKey("soleOffset")) {
                     if (!(var5.get("soleOffset") instanceof JSONArray var13) || var13.size() != 2) {
                        throw new IllegalArgumentException("Terrain sole offset requires two coordinates");
                     }

                     var11 = method79(var13.get(0), -2.0F, 2.0F);
                     var12 = method79(var13.get(1), -2.0F, 2.0F);
                  }

                  var15.add(
                     new AnimationEventMarker(
                        var5.getString("upper"),
                        var5.getString("lower"),
                        var5.getString("foot"),
                        var18,
                        var19,
                        var10,
                        method79(var5.get("soleHeight"), 0.0F, 1.0F),
                        var11,
                        var12
                     )
                  );
                  continue;
               }

               throw new IllegalArgumentException("Terrain leg bend requires three coordinates");
            }

            throw new IllegalArgumentException("Invalid terrain leg fields");
         }

         return new AnimationWindow(
            List.copyOf(var15),
            method79(var0.get("maximumOffset"), 0.01F, 2.0F),
            method79(var0.get("response"), 0.01F, 1.0F),
            method79(var0.get("maxSlopeDegrees"), 0.0F, 70.0F),
            method79(var0.get("maxDistance"), 1.0F, 64.0F),
            method79(var0.get("contactHeight"), 0.0F, 1.0F)
         );
      } else {
         throw new IllegalArgumentException("Terrain feet requires 1 to 8 explicit legs");
      }
   }

   private static float method79(Object var0, float var1, float var2) {
      if (var0 instanceof Number var3 && Double.isFinite(var3.doubleValue()) && !(var3.doubleValue() < var1) && !(var3.doubleValue() > var2)) {
         return var3.floatValue();
      } else {
         throw new IllegalArgumentException("Terrain feet parameter outside " + var1 + ".." + var2);
      }
   }

   public List qm() {
      return this.EC;
   }

   public float qn() {
      return this.ED;
   }

   public float oJ() {
      return this.field17;
   }

   public float qo() {
      return this.EF;
   }

   public float qp() {
      return this.EG;
   }

   public float method80() {
      return this.EH;
   }
}
