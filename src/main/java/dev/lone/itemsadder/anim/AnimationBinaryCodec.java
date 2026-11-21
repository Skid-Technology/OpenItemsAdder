package dev.lone.itemsadder.anim;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader.Feature;
import dev.lone.itemsadder.entity.BoneState;
import dev.lone.itemsadder.entity.BoneStateList;
import dev.lone.itemsadder.entity.EquipmentSlotRecord;
import dev.lone.itemsadder.entity.HumanoidEquipmentSet;
import dev.lone.itemsadder.particle.ParticleEmitterManager;
import dev.lone.itemsadder.util.DoubleVector3;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class AnimationBinaryCodec {
   private static final int TB = 16;
   private static final int TC = 2;
   private static final int TD = 0;
   private static final int TE = 67108864;
   private static final int TF = 16777216;
   private static final int TG = 8192;
   private static final int TH = 8192;
   private static final int TI = 1000000;
   private static final int TJ = 256;
   private static final double TK = 1000000.0;
   private static final Set TL = Set.of("normal", "head", "hatPivot", "leftHandPivot", "rightHandPivot", "mount", "locator");
   private static final Set TM = Set.of("head", "body", "left_arm", "right_arm", "left_leg", "right_leg");
   private static final Set TN = Set.of("compatible", "single_axis_45", "unrestricted");
   private static final Set TO = Set.of("loop", "once", "hold");
   private static final Set TP = Set.of("other", "idle", "idle_angry", "walk", "walk_angry", "jump", "attack", "death", "fly", "swim", "sneak", "sneak_walk");
   private static final Set TQ = Set.of("age", "aggressive", "attack_progress");
   private static final byte[] TR = new byte[]{73, 65, 68, 50};

   private AnimationBinaryCodec() {
   }

   public static byte[] method9160(AnimationBinaryPayload var0) {
      method9164(var0.vH(), var0.vI());
      byte[] var1 = method9185(method9162(var0.vH())).getBytes(StandardCharsets.UTF_8);
      float[] var2 = var0.vI();
      int var3 = aw(var2.length);
      if (var1.length <= 16777216 && 16 + var1.length + var3 <= 67108864) {
         ByteBuffer var4 = ByteBuffer.allocate(16 + var1.length + var3).order(ByteOrder.LITTLE_ENDIAN);
         var4.put(TR).putShort((short)2).putShort((short)0).putInt(var1.length).putInt(var2.length);
         var4.put(var1);

         for (float var8 : var2) {
            var4.putFloat(var8);
         }

         return var4.array();
      } else {
         throw bj("document too large");
      }
   }

   public static AnimationBinaryPayload method9161(byte[] var0) {
      Objects.requireNonNull(var0, "bytes");
      if (var0.length >= 16 && var0.length <= 67108864) {
         ByteBuffer var1 = ByteBuffer.wrap(var0).order(ByteOrder.LITTLE_ENDIAN);

         for (byte var5 : TR) {
            if (var1.get() != var5) {
               throw bj("bad magic");
            }
         }

         if (Short.toUnsignedInt(var1.getShort()) == 2 && Short.toUnsignedInt(var1.getShort()) == 0) {
            int var11 = var1.getInt();
            int var12 = var1.getInt();
            if (var11 >= 0 && var11 <= 16777216 && var12 >= 0 && var12 % 12 == 0) {
               int var13 = aw(var12);
               if (16L + var11 + var13 != var0.length) {
                  throw bj("truncated or trailing data");
               }

               byte[] var14 = new byte[var11];
               var1.get(var14);

               JSONObject var6;
               try {
                  var6 = JSON.parseObject(
                     StandardCharsets.UTF_8
                        .newDecoder()
                        .onMalformedInput(CodingErrorAction.REPORT)
                        .onUnmappableCharacter(CodingErrorAction.REPORT)
                        .decode(ByteBuffer.wrap(var14))
                        .toString()
                  );
               } catch (CharacterCodingException var9) {
                  throw method9195("invalid metadata UTF-8", var9);
               } catch (RuntimeException var10) {
                  throw method9195("invalid metadata JSON", var10);
               }

               if (var6 == null) {
                  throw bj("metadata must be an object");
               }

               float[] var7 = new float[var12];

               for (int var8 = 0; var8 < var7.length; var8++) {
                  var7[var8] = var1.getFloat();
                  if (!Float.isFinite(var7[var8])) {
                     throw bj("non-finite matrix");
                  }
               }

               var6 = method9163(var6);
               method9164(var6, var7);
               return new AnimationBinaryPayload(var6, var7);
            } else {
               throw bj("invalid lengths");
            }
         } else {
            throw bj("unsupported version or flags");
         }
      } else {
         throw bj("invalid file length");
      }
   }

   private static JSONObject method9162(JSONObject var0) {
      JSONObject var1 = JSONObject.of("bones", var0.get("bones"));
      if (var0.containsKey("transformSpace")) {
         var1.put("transformSpace", var0.get("transformSpace"));
      }

      JSONObject var2 = JSONObject.of("rig", var1, "timelines", JSONObject.of("animations", var0.get("animations")));
      JSONObject var3 = new JSONObject();

      for (String var5 : List.of("appearanceGroups", "geometryProfiles", "geometryModels", "textures", "textureMeta", "hurtTextures")) {
         if (var0.containsKey(var5)) {
            var3.put(var5, var0.get(var5));
         }
      }

      if (!var3.isEmpty()) {
         var2.put("assets", var3);
      }

      JSONObject var7 = new JSONObject();

      for (String var6 : List.of("entity", "extensions")) {
         if (var0.containsKey(var6)) {
            var7.put(var6, var0.get(var6));
         }
      }

      if (!var7.isEmpty()) {
         var2.put("runtime", var7);
      }

      if (var0.containsKey("legacy")) {
         var2.put("legacy", var0.get("legacy"));
      }

      return var2;
   }

   private static JSONObject method9163(JSONObject var0) {
      method9187(var0, Set.of("rig", "timelines"), Set.of("assets", "runtime", "legacy"), "wire document");
      JSONObject var1 = method9188(var0.get("rig"), "rig");
      method9187(var1, Set.of("bones"), Set.of("transformSpace"), "rig");
      JSONObject var2 = method9188(var0.get("timelines"), "timelines");
      method9187(var2, Set.of("animations"), Set.of(), "timelines");
      JSONObject var3 = JSONObject.of("bones", var1.get("bones"), "animations", var2.get("animations"));
      if (var1.containsKey("transformSpace")) {
         var3.put("transformSpace", var1.get("transformSpace"));
      }

      if (var0.containsKey("assets")) {
         JSONObject var4 = method9188(var0.get("assets"), "assets");
         method9187(var4, Set.of(), Set.of("appearanceGroups", "geometryProfiles", "geometryModels", "textures", "textureMeta", "hurtTextures"), "assets");

         for (String var6 : var4.keySet()) {
            var3.put(var6, var4.get(var6));
         }
      }

      if (var0.containsKey("runtime")) {
         JSONObject var7 = method9188(var0.get("runtime"), "runtime");
         method9187(var7, Set.of(), Set.of("entity", "extensions"), "runtime");

         for (String var9 : var7.keySet()) {
            var3.put(var9, var7.get(var9));
         }
      }

      if (var0.containsKey("legacy")) {
         var3.put("legacy", var0.get("legacy"));
      }

      return var3;
   }

   public static void method9164(JSONObject var0, float[] var1) {
      Objects.requireNonNull(var0, "metadata");
      Objects.requireNonNull(var1, "matrices");
      if (var1.length % 12 != 0) {
         throw bj("matrix count is not divisible by 12");
      }

      for (float var5 : var1) {
         if (!Float.isFinite(var5)) {
            throw bj("non-finite matrix");
         }
      }

      method9187(
         var0,
         Set.of("bones", "animations"),
         Set.of(
            "legacy",
            "hitbox",
            "extensions",
            "appearanceGroups",
            "geometryProfiles",
            "geometryModels",
            "transformSpace",
            "textures",
            "textureMeta",
            "hurtTextures",
            "entity"
         ),
         "metadata"
      );
      if (var0.containsKey("legacy")) {
         if (var0.containsKey("textures") || var0.containsKey("textureMeta") || var0.containsKey("hurtTextures")) {
            throw bj("legacy mode cannot contain native embedded textures");
         }

         if (var0.containsKey("appearanceGroups")) {
            throw bj("legacy mode cannot contain native appearance groups");
         }

         if (var0.containsKey("geometryProfiles")) {
            throw bj("legacy mode cannot contain native geometry profiles");
         }

         if (var0.containsKey("geometryModels")) {
            throw bj("legacy mode cannot contain native geometry models");
         }

         if (var0.containsKey("transformSpace")) {
            throw bj("legacy mode cannot contain native local transforms");
         }

         if (var0.containsKey("entity")) {
            throw bj("legacy mode cannot contain an entity definition");
         }

         method9179(method9188(var0.get("legacy"), "legacy"), var1, var0);
      } else {
         boolean var24 = var0.containsKey("transformSpace");
         boolean var25 = var0.getJSONObject("entity") != null && var0.getJSONObject("entity").getBooleanValue("playerEmote");
         if (var0.containsKey("textures") || var0.containsKey("textureMeta")) {
            AnimationDataCompiler.method9223(
               method9188(var0.get("textures"), "textures"),
               var0.containsKey("textureMeta") ? method9188(var0.get("textureMeta"), "textureMeta") : new JSONObject()
            );
         }

         if (var24 && !"local".equals(var0.get("transformSpace"))) {
            throw bj("unsupported transform space");
         }

         TreeMap var26 = new TreeMap();
         if (var0.containsKey("appearanceGroups")) {
            JSONObject var27 = method9188(var0.get("appearanceGroups"), "appearanceGroups");
            if (var27.size() > 64) {
               throw bj("too many appearance groups");
            }

            for (Entry var7 : var27.entrySet()) {
               if (!((String)var7.getKey()).matches("[a-z0-9_-]{1,64}")) {
                  throw bj("invalid appearance group id");
               }

               JSONArray var8 = method9189(var7.getValue(), "appearance group states", 64);
               if (var8.isEmpty() || !"default".equals(method9190(var8.get(0), "appearance state"))) {
                  throw bj("appearance group states must begin with default");
               }

               ArrayList var9 = new ArrayList();
               String var10 = null;

               for (int var11 = 0; var11 < var8.size(); var11++) {
                  String var12 = method9190(var8.get(var11), "appearance state");
                  if (!var12.matches("[a-z0-9_-]{1,64}") || var11 > 0 && ("default".equals(var12) || var11 > 1 && var10.compareTo(var12) >= 0)) {
                     throw bj("appearance group states must be valid, unique and sorted");
                  }

                  var9.add(var12);
                  var10 = var12;
               }

               var26.put((String)var7.getKey(), var9);
            }
         }

         if (var0.containsKey("entity")) {
            method9167(method9188(var0.get("entity"), "entity"));
         }

         boolean var28 = var0.getJSONObject("entity") != null && "texture_swap".equals(var0.getJSONObject("entity").getString("hurtMode"));
         if (var28 != var0.containsKey("hurtTextures")) {
            throw bj("hurt textures require texture_swap mode");
         }

         if (var28) {
            if (!var0.containsKey("geometryModels")) {
               throw bj("texture_swap requires embedded geometry");
            }

            JSONObject var29 = method9188(var0.get("textures"), "textures");
            JSONObject var31 = method9188(var0.get("hurtTextures"), "hurtTextures");

            for (String var37 : var31.keySet()) {
               if (!var29.containsKey(var37) || !(var31.get(var37) instanceof String var33) || !var29.containsKey(var33)) {
                  throw bj("hurt texture mapping must reference embedded textures");
               }
            }
         }

         if (var0.containsKey("hitbox")) {
            throw bj("native V2 custom hitboxes are not supported; use the base entity hitbox");
         }

         if (var0.containsKey("extensions") && !(var0.get("extensions") instanceof JSONObject)) {
            throw bj("extensions must be an object");
         }

         JSONArray var30 = method9189(var0.get("bones"), "bones", 8192);
         JSONArray var32 = method9189(var0.get("animations"), "animations", 8192);
         HashMap var34 = new HashMap();
         ArrayList var36 = new ArrayList();

         for (Object var42 : var30) {
            JSONObject var48 = method9188(var42, "bone");
            method9187(
               var48,
               Set.of("name", "boneType", "parents", "staticMatrix", "geometryScale"),
               Set.of("maxHeadRotX", "maxHeadRotY", "locomotionPriority"),
               "bone"
            );
            if (var48.containsKey("locomotionPriority") && !(var48.get("locomotionPriority") instanceof Boolean)) {
               throw bj("bone.locomotionPriority must be boolean");
            }

            String var13 = method9190(var48.get("name"), "bone.name");
            if (var34.putIfAbsent(var13, var48) != null) {
               throw bj("duplicate bone name");
            }

            var36.add(var13);
            String var14 = method9191(var48.get("boneType"), "bone.boneType", false);
            if (!TL.contains(var14)) {
               throw bj("unknown bone type");
            }

            if (!var25 && "hatPivot".equals(var14)) {
               throw bj("native V2 custom-entity hatPivot is not supported");
            }

            if (var25 && ("normal".equals(var14) || "head".equals(var14)) && !TM.contains(var13) && !var0.containsKey("geometryModels")) {
               throw bj("V2 player emote extra bone requires bundled geometry: " + var13);
            }

            method9194(var48.get("staticMatrix"), var1.length / 12, "bone.staticMatrix");
            double var15 = method9192(var48.get("geometryScale"), "bone.geometryScale");
            if (!(var15 > 0.0)) {
               throw bj("bone.geometryScale must be positive");
            }

            if (var48.containsKey("maxHeadRotX") && method9192(var48.get("maxHeadRotX"), "bone.maxHeadRotX") < 0.0) {
               throw bj("bone.maxHeadRotX must be non-negative");
            }

            if (var48.containsKey("maxHeadRotY") && method9192(var48.get("maxHeadRotY"), "bone.maxHeadRotY") < 0.0) {
               throw bj("bone.maxHeadRotY must be non-negative");
            }

            JSONArray var17 = method9189(var48.get("parents"), "bone.parents", 8192);
            if (var17.size() > 256) {
               throw bj("bone hierarchy is too deep");
            }

            HashSet var18 = new HashSet();

            for (Object var20 : var17) {
               if (!var18.add(method9190(var20, "bone.parent"))) {
                  throw bj("duplicate parent");
               }
            }
         }

         for (Entry var43 : var34.entrySet()) {
            for (Object var56 : method9189(((JSONObject)var43.getValue()).get("parents"), "bone.parents", 8192)) {
               if (!var34.containsKey(var56)) {
                  throw bj("unknown parent");
               }
            }
         }

         method9182(var34);
         JSONObject var40 = var0.getJSONObject("extensions");
         if (var0.getJSONObject("entity") == null
            || !"parts_only".equals(var0.getJSONObject("entity").getString("interactionMode"))
            || !var25 && var40 != null && var40.containsKey("itemsadder:damageable_parts")) {
            if (var40 != null && var40.containsKey("itemsadder:humanoid_equipment")) {
               if (var25 || !var24 || var0.containsKey("legacy")) {
                  throw bj("humanoid equipment requires a custom native V2 model with local transforms");
               }

               HumanoidEquipmentSet var44 = HumanoidEquipmentSet.method56(
                  method9188(var40.get("itemsadder:humanoid_equipment"), "humanoid equipment"), var34.keySet()
               );
               if (var44 != null) {
                  for (EquipmentSlotRecord var57 : var44.pf()) {
                     if (!Set.of("normal", "head", "locator").contains(((JSONObject)var34.get(var57.bone())).getString("boneType"))) {
                        throw bj("humanoid equipment requires normal, head or locator bones");
                     }
                  }
               }
            }

            if (var40 != null && var40.containsKey("itemsadder:damageable_parts")) {
               if (var25) {
                  throw bj("damageable parts are not supported for player emotes");
               }

               BoneStateList var45 = BoneStateList.parseListStatic(method9188(var40.get("itemsadder:damageable_parts"), "damageable parts"));

               for (BoneState var58 : var45.getStates()) {
                  if (!var34.containsKey(var58.bone())) {
                     throw bj("damageable parts reference an unknown bone");
                  }
               }
            }

            if (var24) {
               for (JSONObject var52 : var34.values()) {
                  JSONArray var59 = var52.getJSONArray("parents");
                  if (!var59.isEmpty()) {
                     JSONArray var63 = ((JSONObject)var34.get(var59.getString(0))).getJSONArray("parents");
                     if (!var59.subList(1, var59.size()).equals(var63)) {
                        throw bj("local transforms require an ordered parent chain");
                     }
                  }
               }
            }

            if (var0.containsKey("geometryProfiles")) {
               method9178(method9188(var0.get("geometryProfiles"), "geometryProfiles"), var34, var1, var25);
            }

            if (var0.containsKey("geometryModels")) {
               if (!var0.containsKey("geometryProfiles")) {
                  throw bj("geometry models require geometry profiles");
               }

               method9168(
                  method9188(var0.get("geometryModels"), "geometryModels"), method9188(var0.get("geometryProfiles"), "geometryProfiles"), var34, var26, var25
               );
            }

            if (var28) {
               method9169(var0);
            }

            HashSet var47 = new HashSet();

            for (Object var60 : var32) {
               JSONObject var64 = method9188(var60, "animation");
               if (!var47.add(method9190(var64.get("name"), "animation.name"))) {
                  throw bj("duplicate animation name");
               }

               method9180(var64, var34.keySet(), var36, var1.length / 12, var26, var24);
            }

            if (var40 != null && var40.containsKey("itemsadder:ragdoll_physics")) {
               AnimationClip var54 = AnimationClip.method81(method9188(var40.get("itemsadder:ragdoll_physics"), "ragdoll physics"));
               HashSet var61 = new HashSet();

               for (KeyframeEntry var70 : var54.pK()) {
                  if (!var34.containsKey(var70.bone())) {
                     throw bj("ragdoll references an unknown bone");
                  }

                  var61.add(var70.bone());
               }

               for (BoneBinding var71 : var54.pM()) {
                  if (!var34.containsKey(var71.bone())) {
                     throw bj("ragdoll references an unknown bone");
                  }

                  var61.add(var71.bone());
               }

               for (String var72 : var54.pN()) {
                  if (!var34.containsKey(var72)) {
                     throw bj("ragdoll references an unknown bone");
                  }

                  var61.add(var72);
               }

               if (var0.containsKey("geometryModels")) {
                  for (String var73 : var34.keySet()) {
                     String var16 = ((JSONObject)var34.get(var73)).getString("boneType");
                     if (("normal".equals(var16) || "head".equals(var16)) && !var61.contains(var73)) {
                        throw bj("ragdoll must assign every rendered bone exactly once");
                     }
                  }
               }

               method9165(var54, var34, var1, var24);
            }

            if (var40 != null && var40.containsKey("itemsadder:flight_blend")) {
               method9170(method9188(var40.get("itemsadder:flight_blend"), "flight blend"), var47);
            }

            if (var40 != null && var40.containsKey("itemsadder:procedural_motion")) {
               method9171(method9188(var40.get("itemsadder:procedural_motion"), "procedural motion"), var34.keySet());
            }

            if (var40 != null && var40.containsKey("itemsadder:terrain_feet")) {
               if (!var24) {
                  throw bj("terrain feet requires local V2 transforms");
               }

               AnimationWindow var55 = AnimationWindow.method78(method9188(var40.get("itemsadder:terrain_feet"), "terrain feet"));
               HashMap var62 = new HashMap();
               HashSet var69 = new HashSet();

               for (AnimationEventMarker var76 : var55.qm()) {
                  for (String var80 : List.of(var76.qr(), var76.qs(), var76.qt())) {
                     JSONObject var82 = (JSONObject)var34.get(var80);
                     if (var82 == null || !var69.add(var80)) {
                        throw bj("terrain legs require distinct existing bones");
                     }

                     for (String var21 : var82.getJSONArray("parents").toJavaList(String.class, new Feature[0])) {
                        if (!Set.of("normal", "locator").contains(((JSONObject)var34.get(var21)).getString("boneType"))) {
                           throw bj("terrain legs cannot inherit special bone motion");
                        }
                     }

                     if (!Set.of("normal", "locator").contains(var82.getString("boneType"))) {
                        throw bj("terrain legs require normal or locator bones");
                     }

                     Matrix4f var84 = method9166(var80, var34, var1, true, var62);
                     Vector3f var85 = var84.getScale(new Vector3f());
                     float var22 = Math.max(var85.x, Math.max(var85.y, var85.z));
                     float var23 = Math.min(var85.x, Math.min(var85.y, var85.z));
                     if (var23 < 1.0E-4F
                        || var22 - var23 > var22 * 1.0E-4F
                        || var84.determinant3x3() <= 0.0F
                        || !var84.isFinite()
                        || Math.abs(var84.m00() * var84.m10() + var84.m01() * var84.m11() + var84.m02() * var84.m12()) > var85.x * var85.y * 1.0E-4F
                        || Math.abs(var84.m00() * var84.m20() + var84.m01() * var84.m21() + var84.m02() * var84.m22()) > var85.x * var85.z * 1.0E-4F
                        || Math.abs(var84.m10() * var84.m20() + var84.m11() * var84.m21() + var84.m12() * var84.m22()) > var85.y * var85.z * 1.0E-4F) {
                        throw bj("terrain legs require positive uniform rest scale without shear");
                     }
                  }

                  if (!var76.qr().equals(((JSONObject)var34.get(var76.qs())).getJSONArray("parents").stream().findFirst().orElse(null))
                     || !var76.qs().equals(((JSONObject)var34.get(var76.qt())).getJSONArray("parents").stream().findFirst().orElse(null))) {
                     throw bj("terrain legs require direct upper/lower/foot parenting");
                  }

                  if (((Matrix4f)var62.get(var76.qr()))
                           .getTranslation(new Vector3f())
                           .distance(((Matrix4f)var62.get(var76.qs())).getTranslation(new Vector3f()))
                        < 0.001F
                     || ((Matrix4f)var62.get(var76.qs()))
                           .getTranslation(new Vector3f())
                           .distance(((Matrix4f)var62.get(var76.qt())).getTranslation(new Vector3f()))
                        < 0.001F) {
                     throw bj("terrain leg segments must have nonzero length");
                  }
               }

               for (AnimationEventMarker var77 : var55.qm()) {
                  for (Object var81 : ((JSONObject)var34.get(var77.qr())).getJSONArray("parents")) {
                     if (var69.contains(var81)) {
                        throw bj("terrain leg subtrees cannot overlap");
                     }
                  }
               }
            }
         } else {
            throw bj("parts_only requires damageable parts on a custom entity");
         }
      }
   }

   private static void method9165(AnimationClip var0, Map var1, float[] var2, boolean var3) {
      HashMap var4 = new HashMap();

      for (KeyframeEntry var6 : var0.pK()) {
         var4.put(var6.id(), var6.bone());
      }

      HashMap var15 = new HashMap();

      for (BoneAnimation var7 : var0.pL()) {
         Matrix4f var8 = method9166((String)var4.get(var7.qb()), var1, var2, var3, var15);
         Matrix4f var9 = method9166((String)var4.get(var7.qc()), var1, var2, var3, var15);
         DoubleVector3 var10 = var7.qd();
         DoubleVector3 var11 = var7.qe();
         Vector3f var12 = var8.transformPosition((float)var10.getX(), (float)var10.getY(), (float)var10.getZ(), new Vector3f());
         Vector3f var13 = var9.transformPosition((float)var11.getX(), (float)var11.getY(), (float)var11.getZ(), new Vector3f());
         float var14 = var12.distance(var13);
         if (!Float.isFinite(var14) || var14 > 0.02F) {
            throw bj("ragdoll joint " + var7.qb() + "/" + var7.qc() + " anchors differ by " + var14 + " block in the authored rest pose");
         }
      }
   }

   private static Matrix4f method9166(String var0, Map var1, float[] var2, boolean var3, Map var4) {
      Matrix4f var5 = (Matrix4f)var4.get(var0);
      if (var5 != null) {
         return var5;
      }

      JSONObject var6 = (JSONObject)var1.get(var0);
      int var7 = var6.getIntValue("staticMatrix") * 12;
      Matrix4f var8 = new Matrix4f(
         var2[var7],
         var2[var7 + 1],
         var2[var7 + 2],
         0.0F,
         var2[var7 + 3],
         var2[var7 + 4],
         var2[var7 + 5],
         0.0F,
         var2[var7 + 6],
         var2[var7 + 7],
         var2[var7 + 8],
         0.0F,
         var2[var7 + 9],
         var2[var7 + 10],
         var2[var7 + 11],
         1.0F
      );
      JSONArray var9 = var6.getJSONArray("parents");
      if (var3 && !var9.isEmpty()) {
         var8 = new Matrix4f(method9166(var9.getString(0), var1, var2, true, var4)).mul(var8);
      }

      var4.put(var0, var8);
      return var8;
   }

   private static void method9167(JSONObject var0) {
      method9187(
         var0,
         Set.of("namespace", "id", "type", "canSunBurn"),
         Set.of("ragdollOnDeath", "playerEmote", "hurtMode", "interactionMode", "clientInterpolation"),
         "entity"
      );
      if (var0.containsKey("clientInterpolation")) {
         JSONObject var1 = method9188(var0.get("clientInterpolation"), "entity.clientInterpolation");
         method9187(var1, Set.of("forceAll", "intervalTicks", "idleIntervalTicks"), Set.of(), "entity.clientInterpolation");
         if (!(var1.get("forceAll") instanceof Boolean)) {
            throw bj("entity.clientInterpolation.forceAll must be boolean");
         }

         for (String var3 : List.of("intervalTicks", "idleIntervalTicks")) {
            int var4 = method9193(var1.get(var3), "entity.clientInterpolation." + var3);
            if (var4 < 1 || var4 > 8) {
               throw bj("entity.clientInterpolation." + var3 + " must be from 1 to 8");
            }
         }
      }

      if (var0.containsKey("interactionMode") && !Set.of("global", "parts_only").contains(method9190(var0.get("interactionMode"), "entity.interactionMode"))) {
         throw bj("invalid interaction mode");
      }

      if (var0.containsKey("hurtMode") && !Set.of("colored", "texture_swap", "none").contains(method9190(var0.get("hurtMode"), "entity.hurtMode"))) {
         throw bj("invalid hurt mode");
      }

      for (String var6 : List.of("namespace", "id")) {
         String var7 = method9190(var0.get(var6), "entity." + var6);
         if (!var7.matches("[a-z0-9._-]+") || var7.equals(".") || var7.equals("..")) {
            throw bj("unsafe entity " + var6);
         }
      }

      if (!method9190(var0.get("type"), "entity.type").matches("[A-Z0-9_]+")) {
         throw bj("invalid entity type");
      }

      if (!(var0.get("canSunBurn") instanceof Boolean)) {
         throw bj("entity.canSunBurn must be boolean");
      }

      if (var0.containsKey("ragdollOnDeath") && !(var0.get("ragdollOnDeath") instanceof Boolean)) {
         throw bj("entity.ragdollOnDeath must be boolean");
      }

      if (var0.containsKey("playerEmote") && !(var0.get("playerEmote") instanceof Boolean)) {
         throw bj("entity.playerEmote must be boolean");
      }
   }

   private static void method9168(JSONObject var0, JSONObject var1, Map var2, Map var3, boolean var4) {
      method9187(var0, TN, Set.of(), "geometryModels");
      HashSet var5 = new HashSet();

      for (Entry var7 : var2.entrySet()) {
         String var8 = ((JSONObject)var7.getValue()).getString("boneType");
         if (("normal".equals(var8) || "head".equals(var8)) && (!var4 || !TM.contains(var7.getKey()))) {
            var5.add((String)var7.getKey());
         }
      }

      for (String var26 : TN) {
         JSONObject var27 = method9188(var0.get(var26), "geometry model profile");
         method9187(var27, var5, Set.of(), "geometry model bones");
         JSONObject var9 = method9188(var1.get(var26), "geometry profile");

         for (String var11 : var5) {
            JSONArray var12 = method9189(var27.get(var11), "geometry model fragments", 256);
            if (var12.size() != method9189(var9.get(var11), "geometry fragments", 256).size()) {
               throw bj("geometry model fragment count mismatch");
            }

            for (Object var14 : var12) {
               JSONObject var15 = method9188(var14, "geometry model fragment");
               method9187(var15, Set.of("model", "variants", "appearanceGroups"), Set.of(), "geometry model fragment");
               method9188(var15.get("model"), "geometry model");
               JSONArray var16 = method9189(var15.get("appearanceGroups"), "geometry appearance groups", 64);
               ArrayList var17 = new ArrayList();
               String var18 = null;
               int var19 = 1;

               for (Object var21 : var16) {
                  String var22 = method9190(var21, "geometry appearance group");
                  List var23 = (List)var3.get(var22);
                  if (var23 == null || var18 != null && var18.compareTo(var22) >= 0) {
                     throw bj("geometry appearance groups must be known, sorted and unique");
                  }

                  if (var19 > 256 / var23.size()) {
                     throw bj("geometry appearance combinations exceed 256");
                  }

                  var19 *= var23.size();
                  var17.add(var22);
                  var18 = var22;
               }

               JSONObject var28 = method9188(var15.get("variants"), "geometry model variants");
               if (var28.size() != var19 - 1) {
                  throw bj("geometry variants must cover every non-default appearance");
               }

               for (int var29 = 1; var29 < var19; var29++) {
                  String var31 = Integer.toString(var29);
                  if (!var28.containsKey(var31)) {
                     throw bj("geometry variants must use mixed-radix indices");
                  }
               }

               for (Entry var32 : var28.entrySet()) {
                  try {
                     int var33 = Integer.parseInt((String)var32.getKey());
                     if (var33 < 1 || var33 >= var19 || !Integer.toString(var33).equals(var32.getKey())) {
                        throw bj("invalid geometry variant index");
                     }
                  } catch (NumberFormatException var24) {
                     throw method9195("invalid geometry variant index", var24);
                  }

                  JSONObject var34 = method9188(var32.getValue(), "geometry model variant");
                  method9187(var34, Set.of("textures"), Set.of(), "geometry model variant");
                  method9188(var34.get("textures"), "geometry model variant textures");
               }
            }
         }
      }
   }

   private static void method9169(JSONObject var0) {
      String var1 = method9190(method9188(var0.get("entity"), "entity").get("namespace"), "entity.namespace");
      String var2 = var1 + ":ia_v2/";
      JSONObject var3 = method9188(var0.get("textures"), "textures");
      JSONObject var4 = method9188(var0.get("hurtTextures"), "hurtTextures");
      JSONObject var5 = method9188(var0.get("geometryModels"), "geometryModels");

      for (String var7 : TN) {
         JSONObject var8 = method9188(var5.get(var7), "geometry model profile");

         for (Object var10 : var8.values()) {
            for (Object var12 : method9189(var10, "geometry model fragments", 256)) {
               JSONObject var13 = method9188(var12, "geometry model fragment");
               JSONObject var14 = method9188(var13.get("model"), "geometry model");
               JSONObject var15 = var14.containsKey("textures") ? method9188(var14.get("textures"), "geometry textures") : new JSONObject();
               ArrayList var16 = new ArrayList();

               for (Object var19 : var14.containsKey("elements") ? method9189(var14.get("elements"), "geometry elements", 1000000) : new JSONArray()) {
                  JSONObject var20 = method9188(var19, "geometry element");
                  if (var20.containsKey("faces")) {
                     JSONObject var21 = method9188(var20.get("faces"), "geometry faces");

                     for (Object var23 : var21.values()) {
                        String var24 = method9190(method9188(var23, "geometry face").get("texture"), "geometry face texture");
                        if (!var24.startsWith("#")) {
                           throw bj("hurt texture faces must reference the texture dictionary");
                        }

                        var16.add(var24);
                     }
                  }
               }

               JSONObject var28 = method9188(var13.get("variants"), "geometry model variants");
               ArrayList var29 = new ArrayList();
               var29.add(new JSONObject(var15));

               for (Object var32 : var28.values()) {
                  JSONObject var34 = method9188(var32, "geometry model variant");
                  JSONObject var36 = new JSONObject(var15);
                  var36.putAll(method9188(var34.get("textures"), "geometry variant textures"));
                  var29.add(var36);
               }

               for (JSONObject var33 : var29) {
                  ArrayList var35 = new ArrayList(var16);
                  if (var33.containsKey("particle")) {
                     var35.add(method9190(var33.get("particle"), "particle texture"));
                  }

                  for (String var38 : var35) {
                     String var25 = var38;
                     HashSet var26 = new HashSet();

                     while (var25.startsWith("#")) {
                        String var27 = var25.substring(1);
                        if (!var26.add(var27)) {
                           throw bj("geometry texture alias cycle");
                        }

                        if (!var33.containsKey(var27)) {
                           throw bj("unknown geometry texture alias");
                        }

                        var25 = method9190(var33.get(var27), "geometry texture alias");
                     }

                     if (!var25.startsWith(var2)) {
                        throw bj("hurt textures require embedded used geometry textures");
                     }

                     String var39 = var25.substring(var2.length());
                     if (!var3.containsKey(var39)) {
                        throw bj("used geometry texture is not embedded");
                     }

                     if (!var4.containsKey(var39)) {
                        throw bj("used geometry texture has no hurt mapping");
                     }
                  }
               }
            }
         }
      }
   }

   private static void method9170(JSONObject var0, Set var1) {
      method9187(var0, Set.of("straight"), Set.of("left", "right", "deadzone", "maxTurnRate", "smoothing"), "flight blend");

      for (String var3 : List.of("straight", "left", "right")) {
         if (var0.containsKey(var3) && !var1.contains(method9190(var0.get(var3), "flight blend " + var3))) {
            throw bj("flight blend references an unknown " + var3 + " animation");
         }
      }

      double var8 = var0.containsKey("deadzone") ? method9192(var0.get("deadzone"), "flight blend deadzone") : 0.5;
      double var4 = var0.containsKey("maxTurnRate") ? method9192(var0.get("maxTurnRate"), "flight blend maxTurnRate") : 8.0;
      double var6 = var0.containsKey("smoothing") ? method9192(var0.get("smoothing"), "flight blend smoothing") : 0.35;
      if (var8 < 0.0 || var4 <= var8 || var6 <= 0.0 || var6 > 1.0) {
         throw bj("invalid flight blend tuning");
      }
   }

   private static void method9171(JSONObject var0, Set var1) {
      method9187(var0, Set.of("preset", "historyTicks"), Set.of("phaseSource", "root", "body", "head", "neck", "tail", "bodyFrame"), "procedural motion");
      String var2 = method9190(var0.get("preset"), "procedural motion preset");
      if (!Set.of("dragon", "vanilla_ender_dragon", "custom").contains(var2)) {
         throw bj("unknown procedural motion preset");
      }

      if (var0.containsKey("phaseSource") && !Set.of("elapsed", "native").contains(method9190(var0.get("phaseSource"), "procedural motion phaseSource"))) {
         throw bj("unknown procedural motion phaseSource");
      }

      int var3 = method9193(var0.get("historyTicks"), "procedural motion historyTicks");
      if (var3 >= 2 && var3 <= 64) {
         HashSet var4 = new HashSet();

         for (String var6 : List.of("root", "body", "head")) {
            if (var0.containsKey(var6)) {
               method9172(method9188(var0.get(var6), var6), var6, var1, var4, var3);
            }
         }

         for (String var13 : List.of("neck", "tail")) {
            if (var0.containsKey(var13)) {
               method9173(method9188(var0.get(var13), var13), var13, var1, var4, var3);
            }
         }

         if (var0.containsKey("bodyFrame")) {
            if (!var0.containsKey("body")) {
               throw bj("bodyFrame requires body motion");
            }

            JSONObject var12 = method9188(var0.get("bodyFrame"), "bodyFrame");
            method9187(var12, Set.of("anchor", "targets", "pivot"), Set.of(), "bodyFrame");
            String var14 = method9190(var12.get("anchor"), "bodyFrame anchor");
            if (!var1.contains(var14)) {
               throw bj("bodyFrame anchor is missing");
            }

            HashSet var7 = new HashSet();

            for (Object var9 : method9189(var12.get("targets"), "bodyFrame targets", 64)) {
               String var10 = method9190(var9, "bodyFrame target");
               if (!var1.contains(var10) || !var7.add(var10)) {
                  throw bj("bodyFrame contains a missing or duplicate target");
               }
            }

            if (var7.isEmpty()) {
               throw bj("bodyFrame requires targets");
            }

            for (Object var16 : method9189(var12.get("pivot"), "bodyFrame pivot", 3)) {
               if (Math.abs(method9192(var16, "bodyFrame pivot")) > 1024.0) {
                  throw bj("bodyFrame pivot is out of range");
               }
            }

            if (((JSONArray)var12.get("pivot")).size() != 3) {
               throw bj("bodyFrame pivot requires three coordinates");
            }
         }
      } else {
         throw bj("procedural motion historyTicks must be between 2 and 64");
      }
   }

   private static void method9172(JSONObject var0, String var1, Set var2, Set var3, int var4) {
      method9187(
         var0,
         Set.of("bone", "sampleDelay", "referenceDelay", "yawInfluence", "verticalInfluence", "rollInfluence", "follow", "response"),
         Set.of("verticalSampleDelay", "verticalReferenceDelay", "rollReferenceStartDelay", "rollReferenceEndDelay", "rollReferenceBlend"),
         var1
      );
      method9175(method9190(var0.get("bone"), var1 + " bone"), var1, var2, var3);
      method9176(var0.get("sampleDelay"), var1 + " sampleDelay", var4);
      method9176(var0.get("referenceDelay"), var1 + " referenceDelay", var4);
      if (var0.containsKey("verticalSampleDelay")) {
         method9176(var0.get("verticalSampleDelay"), var1 + " verticalSampleDelay", var4);
      }

      if (var0.containsKey("verticalReferenceDelay")) {
         method9176(var0.get("verticalReferenceDelay"), var1 + " verticalReferenceDelay", var4);
      }

      method9174(var0, var1, var4);
      method9177(var0, var1, false);
   }

   private static void method9173(JSONObject var0, String var1, Set var2, Set var3, int var4) {
      method9187(
         var0,
         Set.of(
            "bones",
            "startDelay",
            "endDelay",
            "referenceDelay",
            "yawInfluence",
            "verticalInfluence",
            "rollInfluence",
            "follow",
            "response",
            "waveAmplitude",
            "waveFrequency"
         ),
         Set.of("rollReferenceStartDelay", "rollReferenceEndDelay", "rollReferenceBlend"),
         var1
      );

      for (Object var6 : method9189(var0.get("bones"), var1 + " bones", 64)) {
         method9175(method9190(var6, var1 + " bone"), var1, var2, var3);
      }

      method9176(var0.get("startDelay"), var1 + " startDelay", var4);
      method9176(var0.get("endDelay"), var1 + " endDelay", var4);
      method9176(var0.get("referenceDelay"), var1 + " referenceDelay", var4);
      method9174(var0, var1, var4);
      method9177(var0, var1, true);
   }

   private static void method9174(JSONObject var0, String var1, int var2) {
      if (var0.containsKey("rollReferenceStartDelay")) {
         method9176(var0.get("rollReferenceStartDelay"), var1 + " rollReferenceStartDelay", var2);
      }

      if (var0.containsKey("rollReferenceEndDelay")) {
         method9176(var0.get("rollReferenceEndDelay"), var1 + " rollReferenceEndDelay", var2);
      }

      if (var0.containsKey("rollReferenceBlend") && Math.abs(method9192(var0.get("rollReferenceBlend"), var1 + " rollReferenceBlend")) > 2.0) {
         throw bj(var1 + " rollReferenceBlend is out of range");
      }
   }

   private static void method9175(String var0, String var1, Set var2, Set var3) {
      if (!var2.contains(var0)) {
         throw bj(var1 + " contains a missing bone");
      }

      if (!var3.add(var0)) {
         throw bj("procedural motion contains the same bone twice");
      }
   }

   private static void method9176(Object var0, String var1, int var2) {
      double var3 = method9192(var0, var1);
      if (var3 < 0.0 || var3 > var2 - 1) {
         throw bj(var1 + " exceeds the history span");
      }
   }

   private static void method9177(JSONObject var0, String var1, boolean var2) {
      for (String var4 : List.of("yawInfluence", "verticalInfluence", "rollInfluence")) {
         if (Math.abs(method9192(var0.get(var4), var1 + " " + var4)) > 128.0) {
            throw bj(var1 + " " + var4 + " is out of range");
         }
      }

      double var11 = method9192(var0.get("follow"), var1 + " follow");
      double var5 = method9192(var0.get("response"), var1 + " response");
      if (!(var11 < 0.0) && !(var11 > 2.0) && !(var5 <= 0.0) && !(var5 > 1.0)) {
         if (var2) {
            double var7 = method9192(var0.get("waveAmplitude"), var1 + " waveAmplitude");
            double var9 = method9192(var0.get("waveFrequency"), var1 + " waveFrequency");
            if (var7 < 0.0 || var7 > 45.0 || var9 < 0.0 || var9 > Math.PI) {
               throw bj("invalid " + var1 + " wave");
            }
         }
      } else {
         throw bj("invalid " + var1 + " follow/response");
      }
   }

   private static void method9178(JSONObject var0, Map var1, float[] var2, boolean var3) {
      method9187(var0, TN, Set.of(), "geometryProfiles");
      HashSet var4 = new HashSet();

      for (Entry var6 : var1.entrySet()) {
         String var7 = ((JSONObject)var6.getValue()).getString("boneType");
         if (("normal".equals(var7) || "head".equals(var7)) && (!var3 || !TM.contains(var6.getKey()))) {
            String var8 = (String)var6.getKey();
            if (!var8.matches("[a-z0-9._-]+") || var8.equals(".") || var8.equals("..")) {
               throw bj("unsafe geometry profile bone name");
            }

            var4.add(var8);
         }
      }

      int var16 = 0;

      for (String var18 : TN) {
         JSONObject var19 = method9188(var0.get(var18), "geometry profile");
         method9187(var19, var4, Set.of(), "geometry profile bones");
         int var9 = 0;

         for (String var11 : var4) {
            JSONArray var12 = method9189(var19.get(var11), "geometry fragments", 256);
            if (var12.isEmpty()) {
               throw bj("geometry profile bone has no fragments");
            }

            var9 += var12.size();
            var16 += var12.size();
            if (var9 > 8192 || var16 > 16384) {
               throw bj("too many geometry fragments");
            }

            for (Object var14 : var12) {
               method9194(var14, var2.length / 12, "fragment matrix");
            }

            int var20 = method9193(var12.get(0), "primary fragment matrix") * 12;

            for (int var21 = 0; var21 < 12; var21++) {
               float var15 = var21 != 0 && var21 != 4 && var21 != 8 ? 0.0F : 1.0F;
               if (var2[var20 + var21] != var15) {
                  throw bj("primary fragment matrix must be identity");
               }
            }
         }
      }
   }

   private static void method9179(JSONObject var0, float[] var1, JSONObject var2) {
      method9187(var0, Set.of("coordinateMode", "source"), Set.of(), "legacy");
      if (!"armorstand-head-1.21.1".equals(method9190(var0.get("coordinateMode"), "legacy.coordinateMode"))) {
         throw bj("unsupported legacy coordinate mode");
      } else {
         method9188(var0.get("source"), "legacy.source");
         if (var1.length != 0 || !method9189(var2.get("bones"), "bones", 8192).isEmpty() || !method9189(var2.get("animations"), "animations", 8192).isEmpty()) {
            throw bj("legacy mode must not contain native poses");
         }
      }
   }

   private static void method9180(JSONObject var0, Set var1, List var2, int var3, Map var4, boolean var5) {
      method9187(
         var0,
         Set.of("name", "length", "loopMode", "animType", "canPlayerMove", "effects", "tracks"),
         Set.of(
            "sampleRate",
            "loopStartTime",
            "loopEndTime",
            "affectedBones",
            "channelMasks",
            "layer",
            "priority",
            "blendIn",
            "blendOut",
            "strideDistance",
            "locomotionMode",
            "restOnStop",
            "semanticDriver",
            "semanticPeriod",
            "semanticGroup",
            "interpolation",
            "clientInterpolation",
            "selectionWeight"
         ),
         "animation"
      );
      String var6 = method9190(var0.get("name"), "animation.name");
      if (var0.containsKey("selectionWeight")) {
         int var7 = method9193(var0.get("selectionWeight"), "animation.selectionWeight");
         if (!var5 || var7 < 1 || var7 > 1000000) {
            throw bj("animation.selectionWeight requires local transforms and an integer from 1 to 1000000");
         }
      }

      double var31 = method9192(var0.get("length"), "animation.length");
      int var9 = var0.containsKey("sampleRate") ? method9193(var0.get("sampleRate"), "animation.sampleRate") : 20;
      if (var9 >= 1 && var9 <= 1000) {
         String var10 = method9190(var0.get("loopMode"), "animation.loopMode");
         String var11 = method9190(var0.get("animType"), "animation.animType");
         String var12 = null;
         int var13 = 8;
         if (var0.containsKey("clientInterpolation")) {
            JSONObject var14 = method9188(var0.get("clientInterpolation"), "animation.clientInterpolation");
            method9187(var14, Set.of("mode", "intervalTicks"), Set.of(), "animation.clientInterpolation");
            var12 = method9190(var14.get("mode"), "animation.clientInterpolation.mode");
            if (!Set.of("automatic", "manual").contains(var12)) {
               throw bj("animation.clientInterpolation.mode must be automatic or manual");
            }

            var13 = method9193(var14.get("intervalTicks"), "animation.clientInterpolation.intervalTicks");
            if (var13 < 1 || var13 > 8) {
               throw bj("animation.clientInterpolation.intervalTicks must be from 1 to 8");
            }

            if (!var5) {
               throw bj("animation.clientInterpolation requires local transforms");
            }
         }

         boolean var32 = var0.containsKey("loopStartTime");
         boolean var15 = var0.containsKey("loopEndTime");
         if (!(var31 < 0.0) && var32 == var15) {
            if (var32) {
               double var16 = method9192(var0.get("loopStartTime"), "animation.loopStartTime");
               double var18 = method9192(var0.get("loopEndTime"), "animation.loopEndTime");
               if (var16 < 0.0
                  || var18 < var16
                  || var18 > var31
                  || "loop".equals(var10) && (var16 >= var18 || method9181(var16, var9) >= method9181(var18, var9))) {
                  throw bj("invalid animation loop range");
               }
            }

            if (TO.contains(var10) && TP.contains(var11)) {
               boolean var33 = Set.of("walk", "walk_angry", "sneak_walk", "fly", "swim").contains(var11);
               boolean var17 = "elapsed".equals(var0.get("locomotionMode"));
               if (var0.containsKey("locomotionMode") && !var17) {
                  throw bj("unknown locomotion mode");
               }

               if (!var17
                  && (var33 != var0.containsKey("strideDistance") || var33 && !(method9192(var0.get("strideDistance"), "animation.strideDistance") > 0.0))) {
                  throw bj("locomotion animations require a positive strideDistance");
               }

               if (var17 && (!var33 || var0.containsKey("strideDistance"))) {
                  throw bj("elapsed locomotion must not declare a stride distance");
               }

               if (var0.containsKey("restOnStop") && (!Boolean.TRUE.equals(var0.get("restOnStop")) || !var17)) {
                  throw bj("restOnStop requires elapsed locomotion");
               }

               if (var0.get("canPlayerMove") instanceof Boolean && var0.get("effects") instanceof JSONObject) {
                  int var34 = method9184(var31, var9);
                  method9183((JSONObject)var0.get("effects"), var34, var31, var4, var1);
                  JSONObject var19 = method9188(var0.get("tracks"), "animation.tracks");
                  if (var19.isEmpty()) {
                     throw bj("animation has no tracks");
                  }

                  boolean var20 = var0.containsKey("affectedBones");
                  boolean var21 = var0.containsKey("channelMasks");
                  if (var5 ? var20 != var21 : !var20 && !var21) {
                     if (var5) {
                        if (var20) {
                           String var22 = null;

                           for (Object var24 : method9189(var0.get("affectedBones"), "animation.affectedBones", 8192)) {
                              String var25 = method9190(var24, "affected bone");
                              if (!var1.contains(var25) || !var19.containsKey(var25)) {
                                 throw bj("affected bone has no animation track");
                              }

                              if (var22 != null && var22.compareTo(var25) >= 0) {
                                 throw bj("affected bones must be sorted and unique");
                              }

                              var22 = var25;
                           }
                        } else {
                           int var35 = -1;

                           for (Object var48 : method9189(var0.get("channelMasks"), "animation.channelMasks", 8192)) {
                              JSONArray var52 = method9189(var48, "animation channel mask", 2);
                              if (var52.size() != 2) {
                                 throw bj("invalid animation channel mask");
                              }

                              int var26 = method9193(var52.get(0), "animation channel bone");
                              int var27 = method9193(var52.get(1), "animation channel bits");
                              if (var26 >= var2.size() || var26 <= var35 || var27 < 1 || var27 > 7 || !var19.containsKey((String)var2.get(var26))) {
                                 throw bj("invalid animation channel mask");
                              }

                              var35 = var26;
                           }
                        }
                     }

                     if (var0.containsKey("layer")) {
                        String var36 = method9190(var0.get("layer"), "animation.layer");
                        if (!var36.matches("[a-z0-9_-]{1,64}") || !var5 && !var36.equals("base")) {
                           throw bj("invalid animation layer or missing local transforms");
                        }
                     }

                     if (!var0.containsKey("semanticDriver")
                        || var5
                           && TQ.contains(method9190(var0.get("semanticDriver"), "animation.semanticDriver"))
                           && var0.containsKey("layer")
                           && !"base".equals(var0.getString("layer"))) {
                        if (var0.containsKey("semanticGroup")) {
                           String var37 = method9190(var0.get("semanticGroup"), "animation.semanticGroup");
                           String var44 = var0.getString("layer");
                           if (!var5 || !var37.matches("[a-z0-9_-]{1,64}") || !var0.containsKey("semanticDriver") && var44 != null && !"base".equals(var44)) {
                              throw bj("invalid semantic animation group");
                           }
                        }

                        if (var0.containsKey("semanticPeriod")) {
                           double var38 = method9192(var0.get("semanticPeriod"), "animation.semanticPeriod");
                           if (!"age".equals(var0.getString("semanticDriver")) || !(var38 > 0.0) || var38 > 1000000.0) {
                              throw bj("invalid semantic animation period");
                           }
                        }

                        if ("age".equals(var0.getString("semanticDriver")) && "loop".equals(var10) && !var0.containsKey("semanticPeriod")) {
                           throw bj("looped age animation requires a semantic period");
                        }

                        if (var0.containsKey("priority")) {
                           double var39 = method9192(var0.get("priority"), "animation.priority");
                           if (var39 != Math.rint(var39) || Math.abs(var39) > 1000000.0) {
                              throw bj("invalid animation priority");
                           }
                        }

                        for (String var45 : Set.of("blendIn", "blendOut")) {
                           if (var0.containsKey(var45)) {
                              double var49 = method9192(var0.get(var45), "animation." + var45);
                              if (var49 < 0.0 || var49 > 60.0) {
                                 throw bj("animation blend duration must be between 0 and 60 seconds");
                              }

                              if (!var5 && var49 != 0.0) {
                                 throw bj("animation blends require local transforms");
                              }
                           }
                        }

                        for (Entry var46 : var19.entrySet()) {
                           if (!var1.contains(var46.getKey())) {
                              throw bj("track has unknown bone");
                           }

                           JSONArray var50 = method9189(var46.getValue(), "track frames", 1000000);
                           if (var50.size() != var34) {
                              throw bj("track frame count mismatch");
                           }

                           for (Object var55 : var50) {
                              method9194(var55, var3, "track frame");
                           }
                        }

                        if (var0.containsKey("interpolation")) {
                           if ("manual".equals(var12)) {
                              throw bj("manual interpolation cannot contain sparse plans");
                           }

                           if (var13 == 1) {
                              throw bj("one-tick interpolation cannot contain sparse plans");
                           }

                           JSONObject var42 = method9188(var0.get("interpolation"), "animation.interpolation");
                           if (var42.isEmpty()
                              || var42.size() > var1.size()
                              || !var5
                              || var0.containsKey("semanticDriver")
                              || var0.containsKey("strideDistance")
                              || var0.containsKey("locomotionMode") && !"elapsed".equals(var0.getString("locomotionMode"))
                              || var0.getString("layer") != null
                                 && !"base".equals(var0.getString("layer"))
                                 && !"attack".equals(var11)
                                 && !"death".equals(var11)
                              || "hurt".equals(var6)) {
                              throw bj("display interpolation requires a predictable local base or attack/death animation");
                           }

                           int var47 = method9184(var31, 20);

                           for (Entry var54 : var42.entrySet()) {
                              if (!var1.contains(var54.getKey()) || !var19.containsKey((String)var54.getKey())) {
                                 throw bj("interpolation track requires a known animated bone");
                              }

                              JSONArray var56 = method9189(var54.getValue(), "interpolation track", 1000000);
                              if (var56.size() < 2) {
                                 throw bj("interpolation track requires two endpoints");
                              }

                              int var57 = -1;

                              for (int var28 = 0; var28 < var56.size(); var28++) {
                                 JSONArray var29 = method9189(var56.get(var28), "interpolation endpoint", 2);
                                 if (var29.size() != 2) {
                                    throw bj("interpolation endpoint must contain frame and matrix index");
                                 }

                                 int var30 = method9193(var29.get(0), "interpolation frame");
                                 method9194(var29.get(1), var3, "interpolation matrix");
                                 if (var30 >= var47
                                    || var28 == 0 && var30 != 0
                                    || var28 == var56.size() - 1 && var30 != var47 - 1
                                    || var30 <= var57
                                    || var57 >= 0 && var30 - var57 > var13) {
                                    throw bj("invalid interpolation frame sequence");
                                 }

                                 var57 = var30;
                              }
                           }
                        }
                     } else {
                        throw bj("invalid semantic animation driver");
                     }
                  } else {
                     throw bj(var5 ? "local animation requires exactly one influence mask" : "world animation cannot declare influence masks");
                  }
               } else {
                  throw bj("invalid animation fields");
               }
            } else {
               throw bj("unknown animation mode or type");
            }
         } else {
            throw bj("invalid animation loop range");
         }
      } else {
         throw bj("animation sampleRate must be between 1 and 1000");
      }
   }

   private static int method9181(double var0, int var2) {
      return (int)Math.ceil(var0 * var2 - 1.0E-9);
   }

   private static void method9182(Map var0) {
      HashMap var1 = new HashMap();
      HashMap var2 = new HashMap();

      for (String var4 : var0.keySet()) {
         var1.put(var4, 0);
         var2.put(var4, new ArrayList());
      }

      for (Entry var10 : var0.entrySet()) {
         for (Object var6 : method9189(((JSONObject)var10.getValue()).get("parents"), "bone.parents", 8192)) {
            var1.put((String)var10.getKey(), (Integer)var1.get(var10.getKey()) + 1);
            ((List)var2.get(var6)).add((String)var10.getKey());
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

         for (String var7 : (List)var2.get(var14)) {
            if (var1.merge(var7, -1, Integer::sum) == 0) {
               var9.add(var7);
            }
         }
      }

      if (var12 != var0.size()) {
         throw bj("bone hierarchy cycle");
      }
   }

   private static void method9183(JSONObject var0, int var1, double var2, Map var4, Set var5) {
      method9187(var0, Set.of("sounds", "particles"), Set.of("appearance", "visibility", "tint", "signals"), "effects");

      for (String var7 : Set.of("sounds", "particles", "signals")) {
         if (var0.containsKey(var7)) {
            JSONObject var8 = method9188(var0.get(var7), "effects." + var7);
            method9187(var8, Set.of("keyframes"), Set.of(), "effects." + var7);
            JSONArray var9 = method9189(var8.get("keyframes"), "effects." + var7 + ".keyframes", var1);
            if (var9.size() != var1) {
               throw bj("effects frame count mismatch");
            }

            for (Object var11 : var9) {
               for (Object var13 : method9189(var11, "effects frame", 1024)) {
                  JSONObject var14 = method9188(var13, "effect cue");
                  if (var14.containsKey("time") || var7.equals("signals")) {
                     double var15 = method9192(var14.get("time"), "effect cue time");
                     if (var15 < 0.0 || var15 > var2 + 1.0E-9) {
                        throw bj("effect cue time outside clip");
                     }
                  }

                  if (var7.equals("signals")) {
                     method9187(var14, Set.of("id", "time"), Set.of(), "signal cue");
                     if (!method9190(var14.get("id"), "signal id").matches("[A-Za-z0-9_.:/-]{1,128}")) {
                        throw bj("invalid signal id");
                     }
                  }

                  if (var14.containsKey("follow_locator")) {
                     if (!var7.equals("particles") || !(var14.get("follow_locator") instanceof Boolean)) {
                        throw bj("follow_locator must be a particle cue boolean");
                     }

                     if (Boolean.TRUE.equals(var14.get("follow_locator"))) {
                        if (!ParticleEmitterManager.dP(method9190(var14.get("name"), "particle name"))) {
                           throw bj("follow_locator requires a custom particle effect");
                        }

                        if (!var5.contains(method9190(var14.get("locator"), "particle locator"))) {
                           throw bj("follow_locator references an unknown bone");
                        }
                     }
                  }
               }
            }
         }
      }

      if (var0.containsKey("appearance")) {
         JSONObject var17 = method9188(var0.get("appearance"), "effects.appearance");
         method9187(var17, Set.of("tracks"), Set.of(), "effects.appearance");
         JSONObject var19 = method9188(var17.get("tracks"), "effects.appearance.tracks");

         for (Entry var23 : var19.entrySet()) {
            List var25 = (List)var4.get(var23.getKey());
            if (var25 == null) {
               throw bj("appearance track references an unknown group");
            }

            JSONArray var27 = method9189(var23.getValue(), "appearance track frames", var1);
            if (var27.size() != var1) {
               throw bj("appearance track frame count mismatch");
            }

            for (Object var31 : var27) {
               if (var31 != null && !var25.contains(method9190(var31, "appearance state frame"))) {
                  throw bj("appearance track references an unknown state");
               }
            }
         }
      }

      for (String var20 : Set.of("visibility", "tint")) {
         if (var0.containsKey(var20)) {
            JSONObject var22 = method9188(var0.get(var20), "effects." + var20);
            method9187(var22, Set.of("tracks"), Set.of(), "effects." + var20);
            JSONObject var24 = method9188(var22.get("tracks"), "effects." + var20 + ".tracks");

            for (Entry var28 : var24.entrySet()) {
               if (!var5.contains(var28.getKey())) {
                  throw bj("property track has unknown bone");
               }

               JSONArray var30 = method9189(var28.getValue(), "property track frames", var1);
               if (var30.size() != var1) {
                  throw bj("property track frame count mismatch");
               }

               for (Object var33 : var30) {
                  if (var20.equals("visibility")) {
                     if (var33 != null && !(var33 instanceof Boolean)) {
                        throw bj("visibility must be boolean or null");
                     }
                  } else {
                     double var34 = method9192(var33, "tint");
                     if (var34 != Math.rint(var34) || var34 < -1.0 || var34 > 1.6777215E7) {
                        throw bj("tint must be RGB integer or -1");
                     }
                  }
               }
            }
         }
      }
   }

   private static int method9184(double var0, int var2) {
      if (var0 > 999999.0 / var2) {
         throw bj("animation length too large");
      } else {
         return (int)Math.ceil(var0 * var2) + 1;
      }
   }

   static String method9185(Object var0) {
      if (var0 == null || var0 instanceof Boolean || var0 instanceof String) {
         return JSON.toJSONString(var0);
      } else if (var0 instanceof Number var8) {
         return method9186(var8);
      } else if (var0 instanceof Map var7) {
         TreeMap var9 = new TreeMap();

         for (Entry var12 : var7.entrySet()) {
            if (!(var12.getKey() instanceof String var5)) {
               throw bj("JSON key is not a string");
            }

            var9.put(var5, var12.getValue());
         }

         ArrayList var11 = new ArrayList();

         for (Entry var14 : var9.entrySet()) {
            var11.add(JSON.toJSONString(var14.getKey()) + ":" + method9185(var14.getValue()));
         }

         return "{" + String.join(",", var11) + "}";
      } else if (!(var0 instanceof Iterable var1)) {
         throw bj("unsupported JSON value");
      } else {
         ArrayList var2 = new ArrayList();

         for (Object var4 : var1) {
            var2.add(method9185(var4));
         }

         return "[" + String.join(",", var2) + "]";
      }
   }

   private static String method9186(Number var0) {
      double var1 = var0.doubleValue();
      if (!Double.isFinite(var1)) {
         throw bj("non-finite JSON number");
      } else {
         return var1 == 0.0 ? "0" : BigDecimal.valueOf(var1).stripTrailingZeros().toPlainString();
      }
   }

   private static void method9187(JSONObject var0, Set var1, Set var2, String var3) {
      HashSet var4 = new HashSet(var0.keySet());
      if (!var4.containsAll(var1)) {
         throw bj("unexpected " + var3 + " fields");
      }

      var4.removeAll(var1);
      var4.removeAll(var2);
      if (!var4.isEmpty()) {
         throw bj("unexpected " + var3 + " fields");
      }
   }

   private static JSONObject method9188(Object var0, String var1) {
      if (var0 instanceof JSONObject var2) {
         return var2;
      } else {
         throw bj(var1 + " must be an object");
      }
   }

   private static JSONArray method9189(Object var0, String var1, int var2) {
      if (var0 instanceof JSONArray var3 && var3.size() <= var2) {
         return var3;
      } else {
         throw bj(var1 + " must be a bounded array");
      }
   }

   private static String method9190(Object var0, String var1) {
      return method9191(var0, var1, false);
   }

   private static String method9191(Object var0, String var1, boolean var2) {
      if (var0 instanceof String var3 && (var2 || !var3.isEmpty()) && var3.length() <= 256) {
         return var3;
      } else {
         throw bj(var1 + " must be a bounded string");
      }
   }

   private static double method9192(Object var0, String var1) {
      if (var0 instanceof Number var2 && Double.isFinite(var2.doubleValue())) {
         return var2.doubleValue();
      } else {
         throw bj(var1 + " must be finite");
      }
   }

   private static int method9193(Object var0, String var1) {
      double var2 = method9192(var0, var1);
      if (!(var2 < 0.0) && !(var2 > 2.147483647E9) && var2 == Math.rint(var2)) {
         return (int)var2;
      } else {
         throw bj(var1 + " must be an integer");
      }
   }

   private static void method9194(Object var0, int var1, String var2) {
      if (method9193(var0, var2) >= var1) {
         throw bj(var2 + " out of range");
      }
   }

   private static int aw(int var0) {
      try {
         return Math.multiplyExact(var0, 4);
      } catch (ArithmeticException var2) {
         throw method9195("float data too large", var2);
      }
   }

   private static IllegalArgumentException bj(String var0) {
      return new IllegalArgumentException("Invalid IAD2: " + var0);
   }

   private static IllegalArgumentException method9195(String var0, Throwable var1) {
      return new IllegalArgumentException("Invalid IAD2: " + var0, var1);
   }
}
