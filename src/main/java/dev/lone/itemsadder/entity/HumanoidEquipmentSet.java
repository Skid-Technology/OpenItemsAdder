package dev.lone.itemsadder.entity;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class HumanoidEquipmentSet {
   private final String CA;
   private final List CB;
   private final Map field12;
   public static final String CD = "itemsadder:humanoid_equipment";
   public static final String CE = "vanilla_humanoid_rigid_v1";
   public static final String CF = "vanilla_humanoid_articulated_v1";
   public static final String CG = "vanilla_humanoid_articulated_feet_v1";
   public static final List CH = List.of("helmet", "chest", "legs", "boots");

   public HumanoidEquipmentSet(String var1, List var2, Map var3) {
      this.CA = var1;
      this.CB = var2;
      this.field12 = var3;
   }

   public static List method55(String var0, String var1) {
      boolean var2 = "vanilla_humanoid_rigid_v1".equals(var0);

      return switch (var1) {
         case "helmet" -> List.of("head");
         case "chest" -> var2
            ? List.of("body", "left_arm", "right_arm")
            : List.of("body", "left_upper_arm", "left_lower_arm", "right_upper_arm", "right_lower_arm");
         case "legs" -> var2
            ? List.of("waist", "left_leg", "right_leg")
            : List.of("waist", "left_upper_leg", "left_lower_leg", "right_upper_leg", "right_lower_leg");
         case "boots" -> var2
            ? List.of("left_leg", "right_leg")
            : (
               "vanilla_humanoid_articulated_feet_v1".equals(var0)
                  ? List.of("left_upper_leg", "left_lower_leg", "left_foot", "right_upper_leg", "right_lower_leg", "right_foot")
                  : List.of("left_upper_leg", "left_lower_leg", "right_upper_leg", "right_lower_leg")
            );
         default -> throw bj("unknown area " + var1);
      };
   }

   public static HumanoidEquipmentSet method56(JSONObject var0, Set var1) {
      method62(var0, Set.of("version", "enabled", "template", "bones", "areas", "joints"));
      if (var0.get("version") instanceof Number var2 && var2.doubleValue() == 1.0) {
         boolean var24 = method60(var0.get("enabled"));
         String var4 = method59(var0.get("template"));
         if (!Set.of("vanilla_humanoid_rigid_v1", "vanilla_humanoid_articulated_v1", "vanilla_humanoid_articulated_feet_v1").contains(var4)) {
            throw bj("unknown template " + var4);
         }

         JSONObject var5 = method61(var0.get("bones"));
         if (var5.size() > 128) {
            throw bj("too many bone bindings");
         }

         HashMap var6 = new HashMap();
         HashSet var7 = new HashSet();

         for (String var9 : var5.keySet()) {
            String var10 = method59(var5.get(var9));
            if (!var1.contains(var10) || !var7.add(var10)) {
               throw bj("unknown or ambiguous exported bone " + var10);
            }

            var6.put(bi(var9), var10);
         }

         HashMap var25 = new HashMap();

         for (String var30 : List.of("left_arm", "right_arm", "left_leg", "right_leg")) {
            var25.put(var30, 0.5F);
         }

         var25.put("left_foot", 0.8333333F);
         var25.put("right_foot", 0.8333333F);
         if (var0.containsKey("joints")) {
            JSONObject var27 = method61(var0.get("joints"));
            method62(var27, var25.keySet());

            for (String var11 : var27.keySet()) {
               var25.put(var11, method58(var27.get(var11), 0.01, 0.99));
            }
         }

         if ("vanilla_humanoid_articulated_feet_v1".equals(var4)) {
            for (String var32 : List.of("left", "right")) {
               if ((Float)var25.get(var32 + "_foot") <= (Float)var25.get(var32 + "_leg")) {
                  throw bj("foot split must follow knee split");
               }
            }
         }

         JSONObject var29 = method61(var0.get("areas"));
         method62(var29, Set.copyOf(CH));
         ArrayList var33 = new ArrayList();

         for (String var12 : CH) {
            if (var29.containsKey(var12)) {
               JSONObject var13 = method61(var29.get(var12));
               method62(var13, Set.of("enabled", "bindings"));
               boolean var14 = method60(var13.get("enabled"));
               if (var24 && var14) {
                  if (!(var13.get("bindings") instanceof JSONArray var15)) {
                     throw bj(var12 + " requires bindings");
                  }

                  List var35 = method55(var4, var12);
                  if (var15.size() != var35.size()) {
                     throw bj(var12 + " requires " + var35);
                  }

                  HashSet var17 = new HashSet();

                  for (Object var19 : var15) {
                     JSONObject var20 = method61(var19);
                     method62(var20, Set.of("part", "bone", "translation", "rotation", "scale", "inflation"));
                     String var21 = method59(var20.get("part"));
                     if (!var35.contains(var21) || !var17.add(var21)) {
                        throw bj("invalid or duplicate " + var12 + " segment " + var21);
                     }

                     UUID var22 = bi(method59(var20.get("bone")));
                     String var23 = (String)var6.get(var22);
                     if (var23 == null) {
                        throw bj("missing UUID mapping " + var22);
                     }

                     var33.add(
                        new EquipmentSlotRecord(
                           var12,
                           var21,
                           var22,
                           var23,
                           method57(var20, "translation", 0.0F, -1024.0, 1024.0),
                           method57(var20, "rotation", 0.0F, -360.0, 360.0),
                           method57(var20, "scale", 1.0F, 0.001, 64.0),
                           var20.containsKey("inflation") ? method58(var20.get("inflation"), -0.49, 16.0) : 0.0F
                        )
                     );
                  }
               }
            }
         }

         return var24 && !var33.isEmpty() ? new HumanoidEquipmentSet(var4, List.copyOf(var33), Map.copyOf(var25)) : null;
      } else {
         throw bj("unsupported version");
      }
   }

   private static Vector3fRecord method57(JSONObject var0, String var1, float var2, double var3, double var5) {
      if (!var0.containsKey(var1)) {
         return new Vector3fRecord(var2, var2, var2);
      } else if (var0.get(var1) instanceof JSONArray var7 && var7.size() == 3) {
         return new Vector3fRecord(method58(var7.get(0), var3, var5), method58(var7.get(1), var3, var5), method58(var7.get(2), var3, var5));
      } else {
         throw bj(var1 + " requires three values");
      }
   }

   private static UUID bi(String var0) {
      UUID var1;
      try {
         var1 = UUID.fromString(var0);
      } catch (IllegalArgumentException var3) {
         throw bj("invalid UUID " + var0);
      }

      if (!var1.toString().equals(var0)) {
         throw bj("noncanonical UUID " + var0);
      } else {
         return var1;
      }
   }

   private static float method58(Object var0, double var1, double var3) {
      if (var0 instanceof Number var5 && Double.isFinite(var5.doubleValue()) && !(var5.doubleValue() < var1) && !(var5.doubleValue() > var3)) {
         return var5.floatValue();
      } else {
         throw bj("number outside [" + var1 + ", " + var3 + "]");
      }
   }

   private static String method59(Object var0) {
      if (var0 instanceof String var1 && !var1.isBlank()) {
         return var1;
      } else {
         throw bj("expected nonempty string");
      }
   }

   private static boolean method60(Object var0) {
      if (var0 instanceof Boolean var1) {
         return var1;
      } else {
         throw bj("expected boolean");
      }
   }

   private static JSONObject method61(Object var0) {
      if (var0 instanceof JSONObject var1) {
         return var1;
      } else {
         throw bj("expected object");
      }
   }

   private static void method62(JSONObject var0, Set var1) {
      if (!var1.containsAll(var0.keySet())) {
         throw bj("unknown fields " + var0.keySet());
      }
   }

   private static IllegalArgumentException bj(String var0) {
      return new IllegalArgumentException("Humanoid equipment: " + var0);
   }

   public String pe() {
      return this.CA;
   }

   public List pf() {
      return this.CB;
   }

   public Map pg() {
      return this.field12;
   }
}
