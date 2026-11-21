package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import javax.imageio.ImageIO;
import org.jetbrains.annotations.Nullable;

public final class PackImageUtils {
   private PackImageUtils() {
   }

   public static Map buildEquipmentFiles(String var0, String var1, JSONObject var2, Map var3, Function var4) {
      LinkedHashMap var5 = new LinkedHashMap();
      PackPlan var6 = parsePlan(var2, var0.equals("26.3"));
      if (var6.layers().values().stream().allMatch(List::isEmpty)) {
         return Map.of();
      }

      for (List var8 : var6.layers().values()) {
         for (PackOverrideEntry var10 : var8) {
            copyTexturePair(var10.texture(), var3, var4, var5, true);
         }
      }

      for (PackTextureRef var15 : var6.overrides()) {
         if (var15.palette() != null) {
            copyStaticTexture(texturePath(withSubfolder(var15.palette(), "palettes/")), var3, var4, var5);
         }

         if (var15.texture() != null) {
            for (String var17 : var6.layers().keySet()) {
               String var11 = withSubfolder(var15.texture(), "trims/entity/" + var17 + "/");
               copyTexturePair(var11, var3, var4, var5, false);
               if (var15.palette() != null) {
                  byte[] var12 = (byte[])var5.get(texturePath(var11) + ".mcmeta");
                  if (var12 == null) {
                     var12 = (byte[])var3.get(texturePath(var11) + ".mcmeta");
                  }

                  if (var12 == null) {
                     throw new IOException("Missing trim palette metadata: " + var11);
                  }

                  String var13 = JSON.parseObject(var12).getJSONObject("palette").getString("base_palette");
                  copyStaticTexture(texturePath(withSubfolder(var13, "palettes/")), var3, var4, var5);
               }
            }
         }
      }

      var5.put(var1, JSON.toJSONBytes(var2));
      return Map.copyOf(var5);
   }

   private static void copyTexturePair(String var0, Map var1, Function var2, Map var3, boolean var4) {
      String var5 = texturePath(var0);
      copyStaticTexture(var5, var1, var2, var3);
      BufferedImage var6 = loadTexture(var3, var5);
      byte[] var7 = (byte[])var2.apply(var5 + ".mcmeta");
      if (var7 == null) {
         var7 = (byte[])var1.get(var5 + ".mcmeta");
      }

      if (!var4 && var7 != null && JSON.parseObject(var7).containsKey("animation")) {
         throw new IOException("Animated trim masks are unsupported: " + var0);
      }

      validateArmorSheet(var6, var7);
      if (var7 != null) {
         var3.put(var5 + ".mcmeta", var7);
      }
   }

   public static void validateArmorSheet(BufferedImage var0, @Nullable byte[] var1) {
      JSONObject var2 = var1 == null ? null : JSON.parseObject(var1);
      if (var2 != null && var2.containsKey("animation") && !(var2.get("animation") instanceof JSONObject)) {
         throw new IOException("Invalid armor animation metadata");
      }

      JSONObject var3 = var2 == null ? null : var2.getJSONObject("animation");
      int var4 = var0.getWidth();
      int var5 = var0.getHeight();
      if (var3 == null) {
         if (var4 != var5 * 2) {
            throw new IOException("Expected a 64:32 armor sheet");
         }
      } else {
         int var6 = var3.containsKey("width") ? requirePositiveInt(var3.get("width")) : (var3.containsKey("height") ? var4 : Math.min(var4, var5));
         int var7 = var3.containsKey("height") ? requirePositiveInt(var3.get("height")) : (var3.containsKey("width") ? var5 : Math.min(var4, var5));
         if (var6 == var7 * 2 && var4 % var6 == 0 && var5 % var7 == 0) {
            int var8 = var4 / var6 * (var5 / var7);
            if (var3.containsKey("frametime")) {
               requirePositiveInt(var3.get("frametime"));
            }

            if (var3.containsKey("interpolate") && !(var3.get("interpolate") instanceof Boolean)) {
               throw new IOException("Invalid armor animation interpolation");
            }

            if (var3.containsKey("frames")) {
               if (!(var3.get("frames") instanceof JSONArray var9) || var9.isEmpty()) {
                  throw new IOException("Empty or invalid armor animation frames");
               }

               for (Object var11 : var9) {
                  Object var12 = var11;
                  if (var11 instanceof JSONObject var13) {
                     var12 = var13.get("index");
                     if (var13.containsKey("time")) {
                        requirePositiveInt(var13.get("time"));
                     }
                  }

                  int var16 = requirePositiveInt(var12 instanceof Number var14 ? var14.doubleValue() + 1.0 : null) - 1;
                  if (var16 >= var8) {
                     throw new IOException("Armor animation frame outside sheet");
                  }
               }
            }
         } else {
            throw new IOException("Expected 64:32 animation frames dividing the armor sheet");
         }
      }
   }

   private static int requirePositiveInt(Object var0) {
      if (var0 instanceof Number var1 && !(var1.doubleValue() < 1.0) && !(var1.doubleValue() > 2.147483647E9) && var1.doubleValue() == var1.intValue()) {
         return var1.intValue();
      } else {
         throw new IOException("Armor animation requires positive integer dimensions and times");
      }
   }

   public static byte[] toAnimatedLegacyArmor(BufferedImage var0, int var1, boolean var2, @Nullable byte[] var3) {
      int var4 = var0.getWidth();
      int var5 = var4 / 2;
      if (var4 % 2 == 0 && var5 >= 1 && var0.getHeight() % var5 == 0) {
         int var6 = var0.getHeight() / var5;
         if (var6 > 255 || var1 < 0 || var1 > 255) {
            throw new IOException("Legacy armor animation exceeds its 8-bit metadata");
         }

         if (var6 == 1) {
            validateArmorSheet(var0, var3);
            return var3;
         }

         JSONObject var7 = var3 == null ? new JSONObject() : JSON.parseObject(var3);
         JSONObject var8 = JSONObject.of("width", var4, "height", var5, "interpolate", var2);
         if (var1 == 0) {
            var8.put("frames", JSONArray.of(0));
         } else if (480 % var1 == 0) {
            var8.put("frametime", 480 / var1);
         } else {
            int var9 = var6;

            while (var9 * 480L % var1 != 0L) {
               var9 += var6;
            }

            JSONArray var10 = new JSONArray(var9);

            for (int var11 = 0; var11 < var9; var11++) {
               var10.add(JSONObject.of("index", var11 % var6, "time", (int)((var11 + 1) * 480L / var1 - var11 * 480L / var1)));
            }

            var8.put("frames", var10);
         }

         var7.put("animation", var8);
         return JSON.toJSONBytes(var7);
      } else {
         throw new IOException("Expected vertically stacked 64:32 legacy armor frames");
      }
   }

   private static void copyStaticTexture(String var0, Map var1, Function var2, Map var3) {
      if (!var3.containsKey(var0)) {
         byte[] var4 = (byte[])var2.apply(var0);
         if (var4 == null) {
            var4 = (byte[])var1.get(var0);
         }

         if (var4 == null) {
            throw new IOException("Missing static equipment resource: " + var0);
         }

         var3.put(var0, var4);
      }
   }

   public static PackDefinition parseDefinition(String var0, Map var1) {
      boolean var2 = var0.equals("26.3");
      LinkedHashMap var3 = new LinkedHashMap();
      LinkedHashMap var4 = new LinkedHashMap();
      LinkedHashMap var5 = new LinkedHashMap();
      LinkedHashMap var6 = new LinkedHashMap();

      for (Entry var8 : var1.entrySet()) {
         String var9 = (String)var8.getKey();
         if (var9.endsWith(".json")) {
            String[] var10 = var9.split("/", 4);
            if (var10.length == 4) {
               String var11 = normalizeId(var10[1] + ":" + var10[3].substring(0, var10[3].length() - 5));
               if (var10[0].equals("assets") && var10[2].equals("equipment")) {
                  var3.put(var11, parsePlan(JSON.parseObject((byte[])var8.getValue()), var2));
               } else if (var10[0].equals("data") && var10[2].equals("trim_pattern")) {
                  var4.put(var11, normalizeId(JSON.parseObject((byte[])var8.getValue()).getString("asset_id")));
               } else if (var10[0].equals("data") && var10[2].equals("trim_material")) {
                  JSONObject var12 = JSON.parseObject((byte[])var8.getValue());
                  String var13 = var2 ? normalizeId(var12.getString("palette_id")) : trimColorPalettePath(var12.getString("asset_name"));
                  LinkedHashMap var14 = new LinkedHashMap();
                  JSONObject var15 = var12.getJSONObject("override_armor_assets");
                  if (var15 != null) {
                     for (String var17 : var15.keySet()) {
                        var14.put(normalizeId(var17), trimColorPalettePath(var15.getString(var17)));
                     }
                  }

                  var5.put(var11, new PackOverrideGroup(var13, Map.copyOf(var14)));
               }
            }
         }
      }

      for (PackOverrideGroup var20 : var5.values()) {
         collectTrimPalette(var20.palette(), var2, var1, var6);

         for (String var24 : var20.overrides().values()) {
            collectTrimPalette(var24, var2, var1, var6);
         }
      }

      for (PackPlan var21 : var3.values()) {
         for (PackTextureRef var25 : var21.overrides()) {
            if (var25.palette() != null) {
               collectTrimPalette(var25.palette(), var2, var1, var6);
            }
         }
      }

      return new PackDefinition(var0, Map.copyOf(var3), Map.copyOf(var4), Map.copyOf(var5), Map.copyOf(var6));
   }

   private static PackPlan parsePlan(JSONObject var0, boolean var1) {
      LinkedHashMap var2 = new LinkedHashMap();
      JSONObject var3 = var0.getJSONObject("layers");
      if (var3 == null) {
         throw new IOException("Equipment lacks layers");
      }

      for (String var5 : List.of("humanoid", "humanoid_leggings")) {
         JSONArray var6 = var3.getJSONArray(var5);
         if (var6 != null) {
            if (var6.size() > 16) {
               throw new IOException("Equipment has too many layers");
            }

            ArrayList var7 = new ArrayList();

            for (Object var9 : var6) {
               JSONObject var10 = (JSONObject)var9;
               if (var10.getBooleanValue("use_player_texture")) {
                  throw new IOException("Player-texture equipment is unsupported on custom humanoids");
               }

               JSONObject var11 = var10.getJSONObject("dyeable");
               Integer var12 = var11 != null && var11.containsKey("color_when_undyed") ? var11.getIntValue("color_when_undyed") & 16777215 : null;
               var7.add(new PackOverrideEntry(withSubfolder(normalizeId(var10.getString("texture")), "entity/equipment/" + var5 + "/"), var11 != null, var12));
            }

            var2.put(var5, List.copyOf(var7));
         }
      }

      ArrayList var13 = new ArrayList();
      JSONArray var14 = var1 ? var0.getJSONArray("trim_overrides") : null;
      if (var14 != null) {
         for (Object var16 : var14) {
            JSONObject var17 = (JSONObject)var16;
            JSONObject var18 = var17.getJSONObject("when");
            if (var18 == null || var18.isEmpty() || !List.of("material", "pattern").containsAll(var18.keySet())) {
               throw new IOException("Invalid equipment trim predicate");
            }

            if (!var17.containsKey("texture") && !var17.containsKey("palette")) {
               throw new IOException("Empty equipment trim override");
            }

            var13.add(
               new PackTextureRef(optionalId(var18, "material"), optionalId(var18, "pattern"), optionalId(var17, "texture"), optionalId(var17, "palette"))
            );
         }
      }

      return new PackPlan(Map.copyOf(var2), List.copyOf(var13));
   }

   private static void collectTrimPalette(String var0, boolean var1, Map var2, Map var3) {
      if (!var3.containsKey(var0)) {
         BufferedImage var4 = loadTexture(var2, texturePath(var1 ? withSubfolder(var0, "palettes/") : var0));
         if (var4.getWidth() * var4.getHeight() != 8) {
            throw new IOException("Trim palette must have eight entries: " + var0);
         }

         ArrayList var5 = new ArrayList();

         for (int var6 = 0; var6 < var4.getHeight(); var6++) {
            for (int var7 = 0; var7 < var4.getWidth(); var7++) {
               int var8 = var4.getRGB(var7, var6);
               if (var8 >>> 24 != 255) {
                  throw new IOException("Translucent trim palette unsupported: " + var0);
               }

               var5.add(var8 & 16777215);
            }
         }

         var3.put(var0, List.copyOf(var5));
      }
   }

   public static byte[] encodeTrimMasks(BufferedImage var0, BufferedImage var1) {
      var0 = toArgb(var0);
      var1 = toArgb(var1);
      if (var1.getWidth() * var1.getHeight() != 8) {
         throw new IOException("Trim base palette must have eight entries");
      }

      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.getHeight(); var3++) {
         for (int var4 = 0; var4 < var1.getWidth(); var4++) {
            if (var2.put(var1.getRGB(var4, var3) & 16777215, var3 * var1.getWidth() + var4) != null) {
               throw new IOException("Duplicate trim palette key");
            }
         }
      }

      BufferedImage var10 = new BufferedImage(var0.getWidth(), var0.getHeight() * 8, 2);
      Arrays.fill(((DataBufferInt)var10.getRaster().getDataBuffer()).getData(), 16777215);

      for (int var11 = 0; var11 < var0.getHeight(); var11++) {
         for (int var5 = 0; var5 < var0.getWidth(); var5++) {
            int var6 = var0.getRGB(var5, var11);
            if (var6 >>> 24 != 0) {
               Integer var7 = (Integer)var2.get(var6 & 16777215);
               if (var7 == null) {
                  throw new IOException("Unknown nontransparent trim color: " + Integer.toHexString(var6));
               }

               var10.setRGB(var5, var11 + var0.getHeight() * var7, var6 & 0xFF000000 | 16777215);
            }
         }
      }

      ByteArrayOutputStream var12 = new ByteArrayOutputStream();
      if (!ImageIO.write(var10, "png", var12)) {
         throw new IOException("PNG writer unavailable");
      } else {
         return var12.toByteArray();
      }
   }

   public static BufferedImage loadTexture(Map var0, String var1) {
      byte[] var2 = (byte[])var0.get(var1);
      if (var2 == null) {
         throw new IOException("Missing equipment texture: " + var1);
      } else {
         BufferedImage var3 = ImageIO.read(new ByteArrayInputStream(var2));
         if (var3 != null && var3.getWidth() >= 1 && var3.getHeight() >= 1 && var3.getWidth() <= 4096 && var3.getHeight() <= 4096) {
            return toArgb(var3);
         } else {
            throw new IOException("Unsupported equipment texture: " + var1);
         }
      }
   }

   private static BufferedImage toArgb(BufferedImage var0) {
      if (var0.getColorModel() instanceof IndexColorModel) {
         return var0;
      }

      int var1 = var0.getColorModel().getNumColorComponents();
      if (var1 != 1 && var1 != 3) {
         throw new IOException("Unsupported equipment PNG color space");
      }

      for (int var5 : var0.getColorModel().getComponentSize()) {
         if (var5 != 8) {
            throw new IOException("Equipment PNG requires 8-bit samples");
         }
      }

      BufferedImage var10 = new BufferedImage(var0.getWidth(), var0.getHeight(), 2);
      int[] var11 = new int[var0.getRaster().getNumBands()];

      for (int var12 = 0; var12 < var0.getHeight(); var12++) {
         for (int var13 = 0; var13 < var0.getWidth(); var13++) {
            var0.getRaster().getPixel(var13, var12, var11);
            int var6 = var0.getColorModel().hasAlpha() ? var11[var1] : 255;
            int var7 = var11[0];
            int var8 = var1 == 1 ? var7 : var11[1];
            int var9 = var1 == 1 ? var7 : var11[2];
            var10.setRGB(var13, var12, var6 << 24 | var7 << 16 | var8 << 8 | var9);
         }
      }

      return var10;
   }

   public static String texturePath(String var0) {
      String[] var1 = normalizeId(var0).split(":", 2);
      return "assets/" + var1[0] + "/textures/" + var1[1] + ".png";
   }

   public static String withSubfolder(String var0, String var1) {
      String[] var2 = normalizeId(var0).split(":", 2);
      return var2[0] + ":" + var1 + var2[1];
   }

   private static String trimColorPalettePath(String var0) {
      return withSubfolder(normalizeId(var0), "trims/color_palettes/");
   }

   private static String optionalId(JSONObject var0, String var1) {
      return var0.containsKey(var1) ? normalizeId(var0.getString(var1)) : null;
   }

   private static String normalizeId(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("Missing equipment resource identifier");
      }

      if (!var0.contains(":")) {
         var0 = "minecraft:" + var0;
      }

      if (var0.matches("[a-z0-9_.-]+:[a-z0-9_./-]+") && !var0.contains("..") && !var0.contains("//")) {
         return var0;
      } else {
         throw new IllegalArgumentException("Invalid equipment resource identifier " + var0);
      }
   }
}
