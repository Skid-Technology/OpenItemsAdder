package dev.lone.itemsadder.anim;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.util.DoubleQuaternion;
import dev.lone.itemsadder.util.DoubleVector3;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class AnimationClip {
   private final int DN;
   private final String DO;
   private final String DP;
   private final List DQ;
   private final List DR;
   private final List DS;
   private final List DT;
   private final Map DU;
   private final boolean DV;

   public AnimationClip(int var1, String var2, String var3, List var4, List var5, List var6, List var7, Map var8, boolean var9) {
      var4 = List.copyOf(var4);
      var5 = List.copyOf(var5);
      var6 = List.copyOf(var6);
      var7 = List.copyOf(var7);
      var8 = Map.copyOf(var8);
      this.DN = var1;
      this.DO = var2;
      this.DP = var3;
      this.DQ = var4;
      this.DR = var5;
      this.DS = var6;
      this.DT = var7;
      this.DU = var8;
      this.DV = var9;
   }

   public AnimationClip(int var1, String var2, String var3, List var4, List var5, List var6, List var7, Map var8) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, false);
   }

   public static AnimationClip method81(JSONObject var0) {
      method94(var0, Set.of("version", "preset", "root", "bodies", "joints", "followers", "ignored"), Set.of("slots", "deathAnimation"));
      boolean var1 = method82(var0, "deathAnimation");
      int var2 = method93(var0.get("version"));
      String var3 = method90(var0.get("preset"));
      if (var2 == 1 && (var3.equals("humanoid") || var3.equals("custom"))) {
         String var4 = method90(var0.get("root"));
         JSONArray var5 = method88(var0.get("bodies"), 128);
         JSONArray var6 = method88(var0.get("joints"), 128);
         ArrayList var7 = new ArrayList();
         HashSet var8 = new HashSet();
         HashSet var9 = new HashSet();

         for (Object var11 : var5) {
            JSONObject var12 = method89(var11);
            method94(var12, Set.of("id", "bone", "mass", "collider"), Set.of("detached", "detachTime"));
            String var13 = method90(var12.get("id"));
            String var14 = method90(var12.get("bone"));
            double var15 = method92(var12.get("mass"));
            boolean var17 = method82(var12, "detached");
            Double var18 = method85(var12, "detachTime");
            if ((!var17 || !var13.equals(var4)) && (var18 == null || var17 && !(var18 < 0.0))) {
               if (var8.add(var13) && var9.add(var14)) {
                  JSONObject var19 = method89(var12.get("collider"));
                  String var20 = method90(var19.get("type"));
                  method94(var19, Set.of("type", "center"), Set.of("rotation", "size", "radius", "length"));
                  DoubleVector3 var21 = method86(var19.get("center"));
                  DoubleVector3 var22 = var19.containsKey("size") ? method86(var19.get("size")) : null;
                  DoubleQuaternion var23 = var19.containsKey("rotation") ? method87(var19.get("rotation")) : new DoubleQuaternion(0.0, 0.0, 0.0, 1.0);
                  Double var24 = var19.containsKey("radius") ? method92(var19.get("radius")) : null;
                  Double var25 = var19.containsKey("length") ? method92(var19.get("length")) : null;
                  if (var20.equals("box")
                     ? var22 != null && var24 == null && var25 == null
                     : (
                        var20.equals("sphere")
                           ? var24 != null && var22 == null && var25 == null
                           : var20.equals("capsule") && var24 != null && var25 != null && var22 == null
                     )) {
                     if (var22 == null || !(var22.getX() < 1.0E-4) && !(var22.getY() < 1.0E-4) && !(var22.getZ() < 1.0E-4)) {
                        if (var23.getX() * var23.getX() + var23.getY() * var23.getY() + var23.getZ() * var23.getZ() + var23.getW() * var23.getW() < 1.0E-12) {
                           throw pI();
                        }

                        var7.add(new KeyframeEntry(var13, var14, var15, new BoneKeyframe(var20, var21, var23, var22, var24, var25), var17, var18));
                        continue;
                     }

                     throw pI();
                  }

                  throw pI();
               }

               throw pI();
            }

            throw pI();
         }

         if (var8.contains(var4) && !var7.isEmpty()) {
            ArrayList var26 = new ArrayList();
            HashSet var27 = new HashSet();

            for (Object var30 : var6) {
               JSONObject var32 = method89(var30);
               method94(
                  var32,
                  Set.of("parent", "child", "type", "anchorParent", "anchorChild"),
                  Set.of("axisParent", "axisChild", "swing", "twist", "lower", "upper")
               );
               String var35 = method90(var32.get("parent"));
               String var16 = method90(var32.get("child"));
               String var41 = method90(var32.get("type"));
               if (var8.contains(var35) && var8.contains(var16) && !var35.equals(var16) && var27.add(var16) && !var16.equals(var4)) {
                  if (!var41.equals("ball") && !var41.equals("hinge") && !var41.equals("fixed")) {
                     throw pI();
                  }

                  if (var41.equals("fixed") || method83(var32, "axisParent") != null && method83(var32, "axisChild") != null) {
                     if (var41.equals("fixed") || !(method84(method83(var32, "axisParent")) < 1.0E-8) && !(method84(method83(var32, "axisChild")) < 1.0E-8)) {
                        Double var46 = method85(var32, "lower");
                        Double var50 = method85(var32, "upper");
                        Double var54 = method85(var32, "swing");
                        Double var57 = method85(var32, "twist");
                        if (!var41.equals("ball")
                           || var54 != null
                              && var57 != null
                              && !(var54 <= 0.0)
                              && !(var54 > Math.PI)
                              && !(var57 <= 0.0)
                              && !(var57 > Math.PI)
                              && var46 == null
                              && var50 == null) {
                           if (!var41.equals("hinge")
                              || var46 != null
                                 && var50 != null
                                 && !(var46 > var50)
                                 && !(var46 < -Math.PI)
                                 && !(var50 > Math.PI)
                                 && var54 == null
                                 && var57 == null) {
                              if (!var41.equals("fixed") || var46 == null && var50 == null && var54 == null && var57 == null) {
                                 var26.add(
                                    new BoneAnimation(
                                       var35,
                                       var16,
                                       var41,
                                       method86(var32.get("anchorParent")),
                                       method86(var32.get("anchorChild")),
                                       method83(var32, "axisParent"),
                                       method83(var32, "axisChild"),
                                       var54,
                                       var57,
                                       var46,
                                       var50
                                    )
                                 );
                                 continue;
                              }

                              throw pI();
                           }

                           throw pI();
                        }

                        throw pI();
                     }

                     throw pI();
                  }

                  throw pI();
               }

               throw pI();
            }

            HashSet var29 = new HashSet();
            var29.add(var4);
            var7.stream().filter(KeyframeEntry::pS).forEach(var1x -> var29.add(var1x.id()));
            if (var26.size() == var7.size() - var29.size() && !var27.stream().anyMatch(var29::contains)) {
               HashMap var31 = new HashMap();
               var26.forEach(var1x -> var31.put(var1x.qc(), var1x.qb()));

               for (KeyframeEntry var36 : var7) {
                  String var38 = var36.id();
                  HashSet var42 = new HashSet();

                  while (!var29.contains(var38)) {
                     if (!var42.add(var38) || !var31.containsKey(var38)) {
                        throw pI();
                     }

                     var38 = (String)var31.get(var38);
                  }
               }

               ArrayList var34 = new ArrayList();
               HashSet var37 = new HashSet(var9);

               for (Object var43 : method88(var0.get("followers"), 8192)) {
                  JSONObject var47 = method89(var43);
                  method94(var47, Set.of("bone", "body"), Set.of());
                  String var51 = method90(var47.get("bone"));
                  String var55 = method90(var47.get("body"));
                  if (!var8.contains(var55) || !var37.add(var51)) {
                     throw pI();
                  }

                  var34.add(new BoneBinding(var51, var55));
               }

               ArrayList var40 = new ArrayList();

               for (Object var48 : method88(var0.get("ignored"), 8192)) {
                  String var52 = method90(var48);
                  if (!var37.add(var52)) {
                     throw pI();
                  }

                  var40.add(var52);
               }

               HashMap var45 = new HashMap();
               if (var0.containsKey("slots")) {
                  for (Entry var53 : method89(var0.get("slots")).entrySet()) {
                     String var56 = method90(var53.getValue());
                     if (!var8.contains(var56)) {
                        throw pI();
                     }

                     var45.put(method90(var53.getKey()), var56);
                  }
               }

               return new AnimationClip(var2, var3, var4, var7, var26, var34, var40, var45, var1);
            } else {
               throw pI();
            }
         } else {
            throw pI();
         }
      } else {
         throw pI();
      }
   }

   private static boolean method82(JSONObject var0, String var1) {
      if (!var0.containsKey(var1)) {
         return false;
      } else if (var0.get(var1) instanceof Boolean var2) {
         return var2;
      } else {
         throw pI();
      }
   }

   private static DoubleVector3 method83(JSONObject var0, String var1) {
      return var0.containsKey(var1) ? method86(var0.get(var1)) : null;
   }

   private static double method84(DoubleVector3 var0) {
      return var0 == null ? 0.0 : var0.getX() * var0.getX() + var0.getY() * var0.getY() + var0.getZ() * var0.getZ();
   }

   private static Double method85(JSONObject var0, String var1) {
      return var0.containsKey(var1) ? method91(var0.get(var1)) : null;
   }

   private static DoubleVector3 method86(Object var0) {
      JSONArray var1 = method88(var0, 3);
      if (var1.size() != 3) {
         throw pI();
      } else {
         return new DoubleVector3(method91(var1.get(0)), method91(var1.get(1)), method91(var1.get(2)));
      }
   }

   private static DoubleQuaternion method87(Object var0) {
      JSONArray var1 = method88(var0, 4);
      if (var1.size() != 4) {
         throw pI();
      } else {
         return new DoubleQuaternion(method91(var1.get(0)), method91(var1.get(1)), method91(var1.get(2)), method91(var1.get(3)));
      }
   }

   private static JSONArray method88(Object var0, int var1) {
      if (var0 instanceof JSONArray var2 && var2.size() <= var1) {
         return var2;
      } else {
         throw pI();
      }
   }

   private static JSONObject method89(Object var0) {
      if (var0 instanceof JSONObject var1) {
         return var1;
      } else {
         throw pI();
      }
   }

   private static String method90(Object var0) {
      if (var0 instanceof String var1 && !var1.isBlank() && var1.length() <= 256) {
         return var1;
      } else {
         throw pI();
      }
   }

   private static double method91(Object var0) {
      if (var0 instanceof Number var1 && Double.isFinite(var1.doubleValue()) && !(Math.abs(var1.doubleValue()) > 1024.0)) {
         return var1.doubleValue();
      } else {
         throw pI();
      }
   }

   private static double method92(Object var0) {
      double var1 = method91(var0);
      if (var1 < 1.0E-4) {
         throw pI();
      } else {
         return var1;
      }
   }

   private static int method93(Object var0) {
      double var1 = method91(var0);
      if (var1 != Math.rint(var1)) {
         throw pI();
      } else {
         return (int)var1;
      }
   }

   private static void method94(JSONObject var0, Set var1, Set var2) {
      if (!var0.keySet().containsAll(var1) || !new HashSet<>(var0.keySet()).stream().allMatch(var2x -> var1.contains(var2x) || var2.contains(var2x))) {
         throw pI();
      }
   }

   private static IllegalArgumentException pI() {
      return new IllegalArgumentException("Invalid ragdoll physics profile");
   }

   public int db() {
      return this.DN;
   }

   public String oU() {
      return this.DO;
   }

   public String pJ() {
      return this.DP;
   }

   public List pK() {
      return this.DQ;
   }

   public List pL() {
      return this.DR;
   }

   public List pM() {
      return this.DS;
   }

   public List pN() {
      return this.DT;
   }

   public Map pO() {
      return this.DU;
   }

   public boolean pP() {
      return this.DV;
   }
}
