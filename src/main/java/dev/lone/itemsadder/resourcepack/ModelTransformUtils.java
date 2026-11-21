package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.internal.SwitchMap77;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemSpec;
import dev.lone.itemsadder.util.JsonHelper;
import dev.lone.itemsadder.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class ModelTransformUtils {
   private ModelTransformUtils() {
   }

   static ModelDefinition applyDefaultFurnitureTransform(ItemSpec var0, ModelDefinition var1) {
      return applyFurnitureTransform(var0, var1, BlockFace.UP);
   }

   static ModelDefinition applyFurnitureTransform(ItemSpec var0, ModelDefinition var1, BlockFace var2) {
      CustomItemDefinition var3 = (CustomItemDefinition)ContentManager.method8084().field92.get(var0.Eo());
      return transformForFace(var3, var1, var2);
   }

   public static Map buildFurnitureModels(CustomItemDefinition var0) {
      if (!Version.isOlderThan(Version.v1_21_3) && var0.anZ == null && furnitureModelId(var0, BlockFace.NORTH) != null) {
         String var1 = var0.DS().jO("resource.model_path");
         if (var1 != null && !var1.isBlank()) {
            if (var1.endsWith(".json")) {
               var1 = var1.substring(0, var1.length() - 5);
            }

            String var2 = var1.contains(":") ? var1 : var0.getNamespace() + ":" + var1;
            String[] var3 = var2.split(":", 2);
            String var4 = "assets/" + var3[0] + "/models/" + var3[1] + ".json";
            File var5 = ContentManager.method8198(var4);
            JSONObject var6 = var5 == null && var3[0].equals("minecraft") && ContentManager.method8084().field91.hasVanillaModel(var2)
               ? ContentManager.method8084().field91.getModelJson(var2)
               : null;
            if (var6 != null || var5 != null && var5.isFile()) {
               try {
                  ModelDefinition var7 = new ModelDefinition(var2, var4, var6 != null ? var6 : JsonHelper.readJsonFile(var5));
                  EnumMap var8 = new EnumMap(BlockFace.class);

                  for (BlockFace var12 : new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST}) {
                     var8.put(var12, transformForFace(var0, var7, var12));
                  }

                  return var8;
               } catch (IOException var13) {
                  Log.warn("Could not read furniture model '" + var2 + "' for '" + var0.getNamespacedID() + "'");
                  return Map.of();
               }
            } else {
               return Map.of();
            }
         } else {
            return Map.of();
         }
      } else {
         return Map.of();
      }
   }

   private static ModelDefinition transformForFace(CustomItemDefinition var0, ModelDefinition var1, BlockFace var2) {
      if (var1 == null || var1.bHq == null) {
         return var1;
      }

      if (var0 == null) {
         return var1;
      }

      ConfigSectionWrapper var3 = var0.DS();
      String var4 = "behaviours.furniture";
      if (var3.jI(var4) && var3.method7078(var4 + ".enabled", true)) {
         String var5 = var3.bc(var4 + ".display_transformation.source_entity", "");
         EntityType var6 = var3.method7109(var4 + ".entity", EntityType.ARMOR_STAND, null);
         if (var6 != null && var6 != EntityType.ITEM_DISPLAY && !var5.isEmpty()) {
            String var7 = var6 == EntityType.ARMOR_STAND
               ? (var3.method7078(var4 + ".small_hitbox", var3.method7078(var4 + ".small", false)) ? "small_armor_stand" : "armor_stand")
               : (var6 != EntityType.ITEM_FRAME && var6 != EntityType.GLOW_ITEM_FRAME ? "" : "item_frame");
            if (!var7.isEmpty() && !var7.equals(var5)) {
               String var8 = var5.equals("armor_stand") || var5.equals("small_armor_stand")
                  ? "head"
                  : (var5.equals("item_display") ? var3.bc(var4 + ".display_transformation.transform", "fixed").toLowerCase(Locale.ROOT) : "fixed");
               String var9 = var7.equals("item_frame") ? "fixed" : "head";
               JSONObject var10 = findDisplayTransform(var1.bHq, var8, new HashSet());
               if (var10 == null) {
                  Log.warn("Could not resolve model display context for furniture '" + var0.getNamespacedID() + "'");
                  return var1;
               }

               Matrix4f var11 = entityAnchorMatrix(var5, var3, var4, var2).mul(toMatrix(var10));
               Matrix4f var12 = entityAnchorMatrix(var7, var3, var4, var2).invert().mul(var11);
               var12.translate(0.5F, 0.5F, 0.5F);
               JSONObject var13 = toJsonTransform(var12);
               if (var13 == null) {
                  Log.warn("Furniture model transform cannot be represented in JSON for '" + var0.getNamespacedID() + "'");
                  return var1;
               }

               JSONObject var14 = JsonHelper.copyObject(var1.bHq);
               JSONObject var15 = var14.getJSONObject("display");
               if (var15 == null) {
                  var15 = new JSONObject();
                  var14.put("display", var15);
               }

               var15.put(var9, var13);
               String[] var16 = var0.getNamespacedID().split(":", 2);
               String var17 = var2 == BlockFace.UP ? "" : "_" + var2.name().toLowerCase(Locale.ROOT);
               String var18 = var16[0] + ":ia_auto/furniture/" + var16[1] + var17;
               String var19 = "assets/" + var16[0] + "/models/ia_auto/furniture/" + var16[1] + var17 + ".json";
               return new ModelDefinition(var18, var19, var14);
            } else {
               return var1;
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static String furnitureModelId(CustomItemDefinition var0, BlockFace var1) {
      if (var0 != null && var1 != null && (var1 != BlockFace.UP || var0.anZ == null)) {
         ConfigSectionWrapper var2 = var0.DS();
         if (var0.anZ == null && !var2.jI("resource.model_path")) {
            return null;
         } else {
            String var3 = "behaviours.furniture";
            String var4 = var2.bc(var3 + ".display_transformation.source_entity", "");
            EntityType var5 = var2.method7109(var3 + ".entity", EntityType.ARMOR_STAND, null);
            String var6 = var5 == EntityType.ARMOR_STAND
               ? (var2.method7078(var3 + ".small_hitbox", var2.method7078(var3 + ".small", false)) ? "small_armor_stand" : "armor_stand")
               : (var5 != EntityType.ITEM_FRAME && var5 != EntityType.GLOW_ITEM_FRAME ? "" : "item_frame");
            if (!var4.isEmpty() && !var6.isEmpty() && !var4.equals(var6)) {
               String[] var7 = var0.getNamespacedID().split(":", 2);
               return var7[0] + ":ia_auto/furniture/" + var7[1] + (var1 == BlockFace.UP ? "" : "_" + var1.name().toLowerCase(Locale.ROOT));
            } else {
               return null;
            }
         }
      } else {
         return null;
      }
   }

   private static JSONObject findDisplayTransform(JSONObject var0, String var1, Set var2) {
      if (var1.equals("none")) {
         return new JSONObject();
      }

      JSONObject var3 = var0.getJSONObject("display");
      if (var3 != null && var3.getJSONObject(var1) != null) {
         return var3.getJSONObject(var1);
      }

      String var4 = var0.getString("parent");
      if (var4 == null) {
         return new JSONObject();
      }

      if (!var4.contains(":")) {
         var4 = "minecraft:" + var4;
      }

      if (!var2.add(var4)) {
         return null;
      }

      String[] var5 = var4.split(":", 2);
      File var6 = ContentManager.method8198("assets/" + var5[0] + "/models/" + var5[1] + ".json");
      JSONObject var7 = var6 == null && ContentManager.method8084().field91.hasVanillaModel(var4)
         ? ContentManager.method8084().field91.getModelJson(var4)
         : null;
      if (var7 != null || var6 != null && var6.isFile()) {
         try {
            return findDisplayTransform(var7 != null ? var7 : JsonHelper.readJsonFile(var6), var1, var2);
         } catch (IOException var9) {
            return null;
         }
      } else {
         return null;
      }
   }

   private static Matrix4f toMatrix(JSONObject var0) {
      Vector3f var1 = vectorOrDefault(var0.getJSONArray("rotation"), 0.0F);
      Vector3f var2 = vectorOrDefault(var0.getJSONArray("translation"), 0.0F).mul(0.0625F);
      Vector3f var3 = vectorOrDefault(var0.getJSONArray("scale"), 1.0F);
      return new Matrix4f()
         .translate(var2)
         .rotateXYZ((float)Math.toRadians(var1.x), (float)Math.toRadians(var1.y), (float)Math.toRadians(var1.z))
         .scale(var3)
         .translate(-0.5F, -0.5F, -0.5F);
   }

   private static Vector3f vectorOrDefault(JSONArray var0, float var1) {
      return var0 != null && var0.size() == 3 ? new Vector3f(var0.getFloatValue(0), var0.getFloatValue(1), var0.getFloatValue(2)) : new Vector3f(var1);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static Matrix4f entityAnchorMatrix(String var0, ConfigSectionWrapper var1, String var2, BlockFace var3) {
      if (var3 != BlockFace.UP) {
         Matrix4f var4 = switch (SwitchMap77.bHw[var3.ordinal()]) {
            case 1 -> new Matrix4f().translate(0.5F, 0.5F, 1.0F);
            case 2 -> new Matrix4f().translate(0.5F, 0.5F, 0.0F).rotateY((float) Math.PI);
            case 3 -> new Matrix4f().translate(0.0F, 0.5F, 0.5F).rotateY((float) (-Math.PI / 2));
            case 4 -> new Matrix4f().translate(1.0F, 0.5F, 0.5F).rotateY((float) (Math.PI / 2));
            case 5 -> new Matrix4f().translate(0.5F, 1.0F, 0.5F);
            default -> throw new IllegalArgumentException("Unsupported furniture face " + var3);
         };
         if (var0.equals("item_frame")) {
            return var3 == BlockFace.DOWN ? var4.rotateX((float) (-Math.PI / 2)).scale(0.5F) : var4.scale(0.5F);
         }

         if (var0.equals("item_display")) {
            String var15 = var2 + ".display_transformation";
            Matrix4f var18 = var3 == BlockFace.DOWN ? var4.rotateY((float) Math.PI) : var4.rotateX((float) (-Math.PI / 2));
            var18.translate(
               var1.method7102(var15 + ".translation.x", 0.0F),
               var1.method7102(var15 + ".translation.y", 0.0F),
               var1.method7102(var15 + ".translation.z", 0.0F)
            );
            if (var3 == BlockFace.DOWN) {
               var18.rotateZ((float) Math.PI);
            } else {
               var18.rotate(readQuaternion(var1, var15 + ".left_rotation"));
            }

            return var18.scale(var1.method7102(var15 + ".scale.x", 1.0F), var1.method7102(var15 + ".scale.y", 1.0F), var1.method7102(var15 + ".scale.z", 1.0F))
               .rotate(readQuaternion(var1, var15 + ".right_rotation"));
         }

         if (var0.equals("armor_stand") || var0.equals("small_armor_stand")) {
            boolean var14 = var0.equals("small_armor_stand");
            float var17 = var1.method7102(var2 + ".hitbox.width_offset", 0.0F) == 0.5F ? -1.0F : 0.0F;
            return new Matrix4f().translate(0.5F + var17, var14 ? 0.890625F : 1.6875F, 0.5F).rotateY((float) Math.PI).scale(var14 ? 0.46875F : 0.625F);
         }
      }
      return switch (var0) {
         case "item_frame" -> new Matrix4f().rotateX((float)Math.toRadians(90.0)).scale(0.5F);
         case "armor_stand", "small_armor_stand" -> {
            boolean var16 = var0.equals("small_armor_stand");
            float var19 = var1.method7102(var2 + ".hitbox.width_offset", 0.0F) == 0.5F ? -1.0F : 0.0F;
            yield new Matrix4f().translate(var19, var16 ? 0.890625F : 1.6875F, 0.0F).rotateY((float) Math.PI).scale(var16 ? 0.46875F : 0.625F);
         }
         case "item_display" -> {
            String var6 = var2 + ".display_transformation";
            float var7 = var1.method7102(var6 + ".translation.x", 0.0F);
            float var8 = var1.method7102(var6 + ".translation.y", 0.0F);
            float var9 = var1.method7102(var6 + ".translation.z", 0.0F);
            float var10 = var1.method7102(var6 + ".scale.x", 1.0F);
            float var11 = var1.method7102(var6 + ".scale.y", 1.0F);
            float var12 = var1.method7102(var6 + ".scale.z", 1.0F);
            yield new Matrix4f()
               .rotateY((float) Math.PI)
               .translate(var7, var8, var9)
               .rotate(readQuaternion(var1, var6 + ".left_rotation"))
               .scale(var10, var11, var12)
               .rotate(readQuaternion(var1, var6 + ".right_rotation"));
         }
         default -> new Matrix4f();
      };
   }

   private static Quaternionf readQuaternion(ConfigSectionWrapper var0, String var1) {
      if (var0.jI(var1 + ".quaternion")) {
         return new Quaternionf(
            var0.jQ(var1 + ".quaternion.x"), var0.jQ(var1 + ".quaternion.y"), var0.jQ(var1 + ".quaternion.z"), var0.jQ(var1 + ".quaternion.w")
         );
      } else {
         return var0.jI(var1 + ".axis_angle")
            ? new Quaternionf(
               new AxisAngle4f(
                  (float)Math.toRadians(var0.jQ(var1 + ".axis_angle.angle")),
                  var0.jQ(var1 + ".axis_angle.axis.x"),
                  var0.jQ(var1 + ".axis_angle.axis.y"),
                  var0.jQ(var1 + ".axis_angle.axis.z")
               )
            )
            : new Quaternionf();
      }
   }

   private static JSONObject toJsonTransform(Matrix4f var0) {
      Vector3f var1 = var0.getTranslation(new Vector3f()).mul(16.0F);
      Vector3f var2 = var0.getScale(new Vector3f());
      if (!(var0.determinant3x3() <= 0.0F)
         && !(Math.abs(var1.x) > 80.0F)
         && !(Math.abs(var1.y) > 80.0F)
         && !(Math.abs(var1.z) > 80.0F)
         && !(var2.x > 4.0F)
         && !(var2.y > 4.0F)
         && !(var2.z > 4.0F)
         && !(var2.x < 1.0E-4F)
         && !(var2.y < 1.0E-4F)
         && !(var2.z < 1.0E-4F)) {
         Quaternionf var3 = new Matrix4f(var0).scale(1.0F / var2.x, 1.0F / var2.y, 1.0F / var2.z).getUnnormalizedRotation(new Quaternionf()).normalize();
         Vector3f var4 = var3.getEulerAnglesXYZ(new Vector3f()).mul(180.0F / (float)Math.PI);
         Matrix4f var5 = new Matrix4f()
            .translate(new Vector3f(var1).mul(0.0625F))
            .rotateXYZ((float)Math.toRadians(var4.x), (float)Math.toRadians(var4.y), (float)Math.toRadians(var4.z))
            .scale(var2);
         return !(Math.abs(var5.m00() - var0.m00()) > 0.001F)
               && !(Math.abs(var5.m01() - var0.m01()) > 0.001F)
               && !(Math.abs(var5.m02() - var0.m02()) > 0.001F)
               && !(Math.abs(var5.m10() - var0.m10()) > 0.001F)
               && !(Math.abs(var5.m11() - var0.m11()) > 0.001F)
               && !(Math.abs(var5.m12() - var0.m12()) > 0.001F)
               && !(Math.abs(var5.m20() - var0.m20()) > 0.001F)
               && !(Math.abs(var5.m21() - var0.m21()) > 0.001F)
               && !(Math.abs(var5.m22() - var0.m22()) > 0.001F)
            ? new JSONObject()
               .fluentPut("rotation", new JSONArray().fluentAdd(var4.x).fluentAdd(var4.y).fluentAdd(var4.z))
               .fluentPut("translation", new JSONArray().fluentAdd(var1.x).fluentAdd(var1.y).fluentAdd(var1.z))
               .fluentPut("scale", new JSONArray().fluentAdd(var2.x).fluentAdd(var2.y).fluentAdd(var2.z))
            : null;
      } else {
         return null;
      }
   }
}
