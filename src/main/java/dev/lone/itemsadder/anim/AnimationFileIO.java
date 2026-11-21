package dev.lone.itemsadder.anim;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public final class AnimationFileIO {
   private static final int TY = 8192;
   private static final int TZ = 8192;
   private static final int Ua = 1000000;
   private static final Set Ub = Set.of("loop", "once", "hold");
   private static final Set Uc = Set.of("other", "idle", "idle_angry", "walk", "walk_angry", "jump", "attack", "death", "fly", "swim", "sneak", "sneak_walk");
   private static final Set Ud = Set.of(
      "hat_slot", "left_arm_slot", "right_arm_slot", "parm_left_3", "parm_right_4", "pbody_2", "phead_0", "pleg_left_1", "pleg_right_5"
   );
   private static final Set Ue = Set.of("", "normal", "head", "hatPivot", "locator");

   private AnimationFileIO() {
   }

   public static AnimationBinaryPayload method9201(JSONObject var0) {
      JSONObject var1 = method9212(var0, "legacy source");
      method9205(var1);
      JSONObject var2 = new JSONObject();
      var2.put("coordinateMode", "armorstand-head-1.21.1");
      var2.put("source", var1);
      JSONObject var3 = new JSONObject();
      var3.put("bones", new JSONArray());
      var3.put("animations", new JSONArray());
      var3.put("legacy", var2);
      AnimationBinaryPayload var4 = new AnimationBinaryPayload(var3, new float[0]);
      AnimationBinaryCodec.method9160(var4);
      return var4;
   }

   public static JSONObject method9202(AnimationBinaryPayload var0) {
      AnimationBinaryCodec.method9160(var0);
      JSONObject var1 = var0.vH();
      JSONObject var2 = method9213(var1.get("legacy"), "document is not legacy mode");
      if (!"armorstand-head-1.21.1".equals(method9215(var2.get("coordinateMode"), "legacy.coordinateMode"))) {
         throw bj("unsupported legacy coordinate mode");
      }

      JSONObject var3 = method9212(method9213(var2.get("source"), "legacy.source"), "legacy.source");
      method9205(var3);
      return var3;
   }

   public static void method9203(Path var0, Path var1) {
      JSONObject var2;
      try {
         var2 = JSON.parseObject(Files.readString(var0));
      } catch (RuntimeException var14) {
         throw method9220("invalid legacy JSON", var14);
      }

      if (var2 == null) {
         throw bj("legacy source must be an object");
      }

      byte[] var3 = AnimationBinaryCodec.method9160(method9201(var2));
      Path var4 = var1.toAbsolutePath();
      Path var5 = var4.getParent();
      if (var5 == null) {
         throw bj("target has no parent directory");
      }

      Path var6 = Files.createTempFile(var5, ".iad2-convert-", ".tmp");

      try {
         Files.write(var6, var3);

         try {
            Files.createLink(var4, var6);
         } catch (FileAlreadyExistsException var12) {
            throw bj("target already exists: " + var1);
         }
      } finally {
         Files.deleteIfExists(var6);
      }
   }

   public static void method9204(String[] var0) {
      if (var0.length != 2) {
         throw new IllegalArgumentException("Usage: EntityV2Migration <legacy-metadata> <new-iad2>");
      }

      method9203(Path.of(var0[0]), Path.of(var0[1]));
   }

   private static void method9205(JSONObject var0) {
      method9219(var0, Set.of("bones", "animations"), Set.of("rotationMode", "hitbox"), "legacy source");
      if (var0.containsKey("rotationMode") && !Set.of("precise", "smooth").contains(method9215(var0.get("rotationMode"), "rotationMode"))) {
         throw bj("unsupported rotationMode");
      }

      if (var0.containsKey("hitbox")) {
         method9209(method9213(var0.get("hitbox"), "hitbox"));
      }

      JSONArray var1 = method9214(var0.get("bones"), "bones", 8192);
      JSONArray var2 = method9214(var0.get("animations"), "animations", 8192);
      if (!var1.isEmpty() && !var2.isEmpty()) {
         HashMap var3 = new HashMap();

         for (Object var5 : var1) {
            JSONObject var6 = method9213(var5, "bone");
            method9219(var6, Set.of("name", "boneType", "parents", "pos", "rot"), Set.of("scales", "maxHeadRotX", "maxHeadRotY"), "bone");
            String var7 = method9215(var6.get("name"), "bone.name");
            if (Ud.contains(var7) || var7.contains("dummy_")) {
               throw bj("player emotes are unsupported");
            }

            if (var3.putIfAbsent(var7, var6) != null) {
               throw bj("duplicate bone name");
            }

            String var8 = method9216(var6.get("boneType"), "bone.boneType");
            if (Set.of("leftHandPivot", "rightHandPivot", "mount").contains(var8)) {
               throw bj("unsupported legacy bone type '" + var8 + "' for bone '" + var7 + "': its armor-stand coordinate mode has no V2 display adapter");
            }

            if (!Ue.contains(var8)) {
               throw bj("unknown legacy bone type '" + var8 + "' for bone '" + var7 + "'");
            }

            method9210(var6.get("pos"), "bone.pos", false);
            method9210(var6.get("rot"), "bone.rot", false);

            for (Object var10 : method9214(var6.get("parents"), "bone.parents", 8192)) {
               method9215(var10, "bone.parent");
            }

            if (var6.containsKey("scales")) {
               for (Object var19 : method9214(var6.get("scales"), "bone.scales", 1000000)) {
                  method9215(var19, "bone.scale");
               }
            }

            if (var6.containsKey("maxHeadRotX") && method9218(var6.get("maxHeadRotX"), "bone.maxHeadRotX") < 0.0) {
               throw bj("negative maxHeadRotX");
            }

            if (var6.containsKey("maxHeadRotY") && method9218(var6.get("maxHeadRotY"), "bone.maxHeadRotY") < 0.0) {
               throw bj("negative maxHeadRotY");
            }
         }

         for (JSONObject var13 : var3.values()) {
            for (Object var17 : method9214(var13.get("parents"), "bone.parents", 8192)) {
               if (!var3.containsKey(var17)) {
                  throw bj("unknown bone parent");
               }
            }
         }

         method9211(var3);
         HashSet var12 = new HashSet();

         for (Object var16 : var2) {
            method9206(method9213(var16, "animation"), var3.keySet(), var12);
         }
      } else {
         throw bj("bones and animations must not be empty");
      }
   }

   private static void method9206(JSONObject var0, Set var1, Set var2) {
      method9219(
         var0,
         Set.of("name", "animType", "maxDistance", "loopMode", "length", "bones", "effects"),
         Set.of("canPlayerMove", "loopStartTime", "loopEndTime"),
         "animation"
      );
      String var3 = method9215(var0.get("name"), "animation.name");
      if (!var2.add(var3)) {
         throw bj("duplicate animation name");
      }

      if (Uc.contains(method9215(var0.get("animType"), "animation.animType")) && Ub.contains(method9215(var0.get("loopMode"), "animation.loopMode"))) {
         method9218(var0.get("maxDistance"), "animation.maxDistance");
         double var4 = method9218(var0.get("length"), "animation.length");
         if (!(var4 < 0.0) && !(var4 > 49999.95)) {
            boolean var6 = var0.containsKey("loopStartTime");
            boolean var7 = var0.containsKey("loopEndTime");
            if (var6 != var7) {
               throw bj("partial loop range");
            }

            if (!var6
               || !(method9218(var0.get("loopStartTime"), "loopStartTime") < 0.0)
                  && !(method9218(var0.get("loopEndTime"), "loopEndTime") < method9218(var0.get("loopStartTime"), "loopStartTime"))
                  && !(method9218(var0.get("loopEndTime"), "loopEndTime") > var4)) {
               if (var0.containsKey("canPlayerMove") && !(var0.get("canPlayerMove") instanceof Boolean)) {
                  throw bj("canPlayerMove must be boolean");
               }

               int var8 = (int)Math.ceil(var4 * 20.0) + 1;
               JSONObject var9 = method9213(var0.get("bones"), "animation.bones");
               if (var9.isEmpty()) {
                  throw bj("animation has no bones");
               }

               for (Entry var11 : var9.entrySet()) {
                  if (!var1.contains(var11.getKey())) {
                     throw bj("animation has unknown bone");
                  }

                  JSONArray var12 = method9214(method9213(var11.getValue(), "bone animation").get("keyframes"), "keyframes", 1000000);
                  if (var12.size() != var8) {
                     throw bj("keyframe count mismatch");
                  }

                  for (Object var14 : var12) {
                     method9207(method9213(var14, "keyframe"));
                  }
               }

               method9208(method9213(var0.get("effects"), "effects"), var8);
            } else {
               throw bj("invalid loop range");
            }
         } else {
            throw bj("invalid animation length");
         }
      } else {
         throw bj("unsupported animation mode");
      }
   }

   private static void method9207(JSONObject var0) {
      method9219(var0, Set.of("pos", "rot", "scale_str", "scale"), Set.of(), "keyframe");
      method9210(var0.get("pos"), "keyframe.pos", false);
      method9210(var0.get("rot"), "keyframe.rot", false);
      method9210(var0.get("scale"), "keyframe.scale", false);
      method9215(var0.get("scale_str"), "keyframe.scale_str");
   }

   private static void method9208(JSONObject var0, int var1) {
      method9219(var0, Set.of("sounds", "particles"), Set.of(), "effects");

      for (String var3 : Set.of("sounds", "particles")) {
         JSONObject var4 = method9213(var0.get(var3), "effects." + var3);
         method9219(var4, Set.of("keyframes"), Set.of(), "effects." + var3);
         JSONArray var5 = method9214(var4.get("keyframes"), "effects." + var3 + ".keyframes", var1);
         if (var5.size() != var1) {
            throw bj("effect frame count mismatch");
         }

         for (Object var7 : var5) {
            method9214(var7, "effect frame", 1024);
         }
      }
   }

   private static void method9209(JSONObject var0) {
      method9219(var0, Set.of("pos", "size"), Set.of(), "hitbox");
      method9210(var0.get("pos"), "hitbox.pos", false);
      method9210(var0.get("size"), "hitbox.size", true);
   }

   private static void method9210(Object var0, String var1, boolean var2) {
      JSONArray var3 = method9214(var0, var1, 3);
      if (var3.size() != 3) {
         throw bj(var1 + " must contain three values");
      }

      for (Object var5 : var3) {
         double var6 = method9218(var5, var1);
         if (var2 && !(var6 > 0.0)) {
            throw bj(var1 + " must be positive");
         }
      }
   }

   private static void method9211(Map var0) {
      HashMap var1 = new HashMap();
      HashMap var2 = new HashMap();

      for (String var4 : var0.keySet()) {
         var1.put(var4, 0);
         var2.put(var4, new HashSet());
      }

      for (Entry var10 : var0.entrySet()) {
         for (Object var6 : method9214(((JSONObject)var10.getValue()).get("parents"), "bone.parents", 8192)) {
            var1.merge((String)var10.getKey(), 1, Integer::sum);
            ((Set)var2.get(var6)).add((String)var10.getKey());
         }
      }

      ArrayDeque var9 = new ArrayDeque();

      for (Entry var13 : var1.entrySet()) {
         if ((Integer)var13.getValue() == 0) {
            var9.add((String)var13.getKey());
         }
      }

      int var12 = 0;

      while (!var9.isEmpty()) {
         String var14 = (String)var9.remove();
         var12++;

         for (String var7 : (Set)var2.get(var14)) {
            if (var1.merge(var7, -1, Integer::sum) == 0) {
               var9.add(var7);
            }
         }
      }

      if (var12 != var0.size()) {
         throw bj("bone hierarchy cycle");
      }
   }

   private static JSONObject method9212(JSONObject var0, String var1) {
      if (var0 == null) {
         throw bj(var1 + " must be an object");
      }

      try {
         return JSON.parseObject(var0.toJSONString(new Feature[0]));
      } catch (RuntimeException var3) {
         throw method9220("cannot copy " + var1, var3);
      }
   }

   private static JSONObject method9213(Object var0, String var1) {
      if (var0 instanceof JSONObject var2) {
         return var2;
      } else {
         throw bj(var1 + " must be an object");
      }
   }

   private static JSONArray method9214(Object var0, String var1, int var2) {
      if (var0 instanceof JSONArray var3 && var3.size() <= var2) {
         return var3;
      } else {
         throw bj(var1 + " must be a bounded array");
      }
   }

   private static String method9215(Object var0, String var1) {
      return method9217(var0, var1, false);
   }

   private static String method9216(Object var0, String var1) {
      return method9217(var0, var1, true);
   }

   private static String method9217(Object var0, String var1, boolean var2) {
      if (var0 instanceof String var3 && (var2 || !var3.isEmpty()) && var3.length() <= 256) {
         return var3;
      } else {
         throw bj(var1 + " must be a bounded string");
      }
   }

   private static double method9218(Object var0, String var1) {
      if (var0 instanceof Number var2 && Double.isFinite(var2.doubleValue())) {
         return var2.doubleValue();
      } else {
         throw bj(var1 + " must be finite");
      }
   }

   private static void method9219(JSONObject var0, Set var1, Set var2, String var3) {
      HashSet var4 = new HashSet(var0.keySet());
      if (!var4.containsAll(var1)) {
         throw bj("missing " + var3 + " field");
      }

      var4.removeAll(var1);
      var4.removeAll(var2);
      if (!var4.isEmpty()) {
         throw bj("unsupported " + var3 + " field: " + (String)var4.iterator().next());
      }
   }

   private static IllegalArgumentException bj(String var0) {
      return new IllegalArgumentException("Invalid legacy migration: " + var0);
   }

   private static IllegalArgumentException method9220(String var0, Throwable var1) {
      return new IllegalArgumentException("Invalid legacy migration: " + var0, var1);
   }
}
