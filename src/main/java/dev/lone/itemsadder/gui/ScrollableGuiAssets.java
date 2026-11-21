package dev.lone.itemsadder.gui;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.core.ContentManager;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.imageio.ImageIO;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public final class ScrollableGuiAssets {
   public static final String bDX = "_iainternal:gui/scroll_track";
   public static final String bDY = "_iainternal:item/gui/scroll_track";
   public static final String bDZ = "_iainternal:gui/empty_slot";
   private static final String bEa = "_iainternal:item/gui/scroll_track/fast_offset_";
   public static final String bEb = "assets/_iainternal/scrollable_gui_v1.json";
   public static final String bEc = "assets/_iainternal/scrollable_gui_legacy_v1.json";
   private static final String bEd = "ia_overlay_1_21_4_plus/assets/_iainternal/";
   static final int bEe = -9;
   static final int bEf = 17;
   private static final int bEg = -13355461;
   private static final int bEh = -3815218;
   private static final Map bEi = new LinkedHashMap();

   private ScrollableGuiAssets() {
   }

   public static NamespacedKey method6614(NamespacedKey var0, NamespacedKey var1) {
      return method6618(null, var0, var1, null);
   }

   public static synchronized NamespacedKey method6615(Plugin var0, NamespacedKey var1, NamespacedKey var2) {
      return method6617(var0, var1, var2, null);
   }

   public static NamespacedKey method6616(NamespacedKey var0, NamespacedKey var1, NamespacedKey var2) {
      return method6618(null, var0, var1, var2);
   }

   public static NamespacedKey method6617(Plugin var0, NamespacedKey var1, NamespacedKey var2, NamespacedKey var3) {
      return method6618(Objects.requireNonNull(var0, "owner"), var1, var2, var3);
   }

   private static synchronized NamespacedKey method6618(Plugin var0, NamespacedKey var1, NamespacedKey var2, NamespacedKey var3) {
      Objects.requireNonNull(var1, "track");
      Objects.requireNonNull(var2, "thumb");
      ScrollableGuiKeySet var4 = new ScrollableGuiKeySet(var1, var2, var3);
      ScrollableGuiEntry var5 = bEi.computeIfAbsent(
         var4, var1x -> new ScrollableGuiEntry(new NamespacedKey("_iainternal", "gui/scroll_track/custom/" + method6626(var4)))
      );
      if (var0 == null) {
         var5.bEq = true;
      } else {
         var5.bEp.add(Objects.requireNonNull(var0, "owner"));
      }

      return var5.bEo;
   }

   public static synchronized void method6619(Plugin var0) {
      Objects.requireNonNull(var0, "owner");
      Iterator var1 = bEi.values().iterator();

      while (var1.hasNext()) {
         ScrollableGuiEntry var2 = (ScrollableGuiEntry)var1.next();
         var2.bEp.remove(var0);
         if (!var2.bEq && var2.bEp.isEmpty()) {
            var1.remove();
         }
      }
   }

   public static synchronized ScrollableGuiRegistry XP() {
      LinkedHashMap var0 = new LinkedHashMap();
      bEi.forEach((var1, var2) -> var0.put(var1, var2.bEo));
      return new ScrollableGuiRegistry(var0);
   }

   public static Set XQ() {
      return XP().XQ();
   }

   public static Map XR() {
      return XP().XR();
   }

   private static void method6620(Map var0, ScrollableGuiKeySet var1, NamespacedKey var2) {
      BufferedImage var3 = method6623(var1.bEl);
      BufferedImage var4 = method6623(var1.bEm);
      var0.putAll(method6622(var2, var3, var4, var1.bEn == null ? null : method6623(var1.bEn)));
   }

   static Map method6621(NamespacedKey var0, BufferedImage var1, BufferedImage var2) {
      return method6622(var0, var1, var2, null);
   }

   static Map method6622(NamespacedKey var0, BufferedImage var1, BufferedImage var2, BufferedImage var3) {
      LinkedHashMap var4 = new LinkedHashMap();
      JSONArray var5 = new JSONArray();
      JSONArray var6 = new JSONArray();
      String var7 = var0.getKey();

      for (int var8 = -9; var8 <= 17; var8++) {
         String var9 = "offset_" + var8;
         String var10 = "_iainternal:" + var7 + "/" + var9;
         var5.add(JSONObject.of("threshold", var8, "model", fR("_iainternal:item/" + var7 + "/" + var9)));
         var4.put("ia_overlay_1_21_4_plus/assets/_iainternal/models/item/" + var7 + "/" + var9 + ".json", JSON.toJSONBytes(hV(var10)));
         var4.put("assets/_iainternal/textures/" + var7 + "/" + var9 + ".png", method6629(method6625(var1, var2, var8)));
         if (var3 != null) {
            String var11 = "fast_" + var9;
            var6.add(JSONObject.of("threshold", var8, "model", fR("_iainternal:item/" + var7 + "/" + var11)));
            var4.put(
               "ia_overlay_1_21_4_plus/assets/_iainternal/models/item/" + var7 + "/" + var11 + ".json",
               JSON.toJSONBytes(hV("_iainternal:" + var7 + "/" + var11))
            );
            var4.put("assets/_iainternal/textures/" + var7 + "/" + var11 + ".png", method6629(method6625(var1, var3, var8)));
         }
      }

      JSONObject var12 = fR("_iainternal:item/" + var7 + "/track");
      var5.add(JSONObject.of("threshold", 18, "model", var12));
      var4.put("ia_overlay_1_21_4_plus/assets/_iainternal/models/item/" + var7 + "/track.json", JSON.toJSONBytes(hV("_iainternal:" + var7 + "/track")));
      var4.put("assets/_iainternal/textures/" + var7 + "/track.png", method6629(method6625(var1, null, 0)));
      JSONObject var13 = JSONObject.of(
         "type", "minecraft:range_dispatch", "property", "minecraft:custom_model_data", "index", 0, "entries", var5, "fallback", var12
      );
      JSONObject var14 = var13;
      if (var3 != null) {
         var6.add(JSONObject.of("threshold", 18, "model", var12));
         JSONObject var15 = JSONObject.of(
            "type", "minecraft:range_dispatch", "property", "minecraft:custom_model_data", "index", 0, "entries", var6, "fallback", var12
         );
         var14 = JSONObject.of(
            "type",
            "minecraft:range_dispatch",
            "property",
            "minecraft:custom_model_data",
            "index",
            1,
            "entries",
            JSONArray.of(JSONObject.of("threshold", 1, "model", var15)),
            "fallback",
            var13
         );
      }

      var4.put("ia_overlay_1_21_4_plus/assets/_iainternal/items/" + var7 + ".json", JSON.toJSONBytes(JSONObject.of("oversized_in_gui", true, "model", var14)));
      return var4;
   }

   private static JSONObject ab(JSONObject var0) {
      JSONArray var1 = new JSONArray();

      for (int var2 = -9; var2 <= 17; var2++) {
         var1.add(JSONObject.of("threshold", var2, "model", fR("_iainternal:item/gui/scroll_track/fast_offset_" + var2)));
      }

      var1.add(JSONObject.of("threshold", 18, "model", fR("_iainternal:item/gui/scroll_track/track")));
      return JSONObject.of(
         "type",
         "minecraft:range_dispatch",
         "property",
         "minecraft:custom_model_data",
         "index",
         1,
         "entries",
         JSONArray.of(
            JSONObject.of(
               "threshold",
               1,
               "model",
               JSONObject.of(
                  "type",
                  "minecraft:range_dispatch",
                  "property",
                  "minecraft:custom_model_data",
                  "index",
                  0,
                  "entries",
                  var1,
                  "fallback",
                  fR("_iainternal:item/gui/scroll_track/track")
               )
            )
         ),
         "fallback",
         var0
      );
   }

   private static BufferedImage method6623(NamespacedKey var0) {
      return method6624(var0, ContentManager.method8199(var0.getNamespace() + ":" + var0.getKey()));
   }

   static BufferedImage method6624(NamespacedKey var0, File var1) {
      String var2 = var0.getNamespace() + ":" + var0.getKey();

      try {
         BufferedImage var3 = var1 == null ? null : ImageIO.read(var1);
         if (var1 == null && var0.getNamespace().equals("_iainternal")) {
            try (InputStream var4 = ScrollableGuiAssets.class
                  .getResourceAsStream("/contents/_iainternal/resourcepack/assets/_iainternal/textures/" + var0.getKey() + ".png")) {
               if (var4 != null) {
                  var3 = ImageIO.read(var4);
               }
            }
         }

         if (var3 == null) {
            throw new IOException("missing or unreadable PNG");
         } else {
            return var3;
         }
      } catch (IOException var9) {
         throw new IllegalStateException("Unable to read scrollable GUI texture " + var2, var9);
      }
   }

   static BufferedImage method6625(BufferedImage var0, BufferedImage var1, int var2) {
      BufferedImage var3 = new BufferedImage(18, 18, 2);
      Graphics2D var4 = var3.createGraphics();
      var4.setComposite(AlphaComposite.Src);
      var4.drawImage(var0, 0, 0, 18, 18, null);
      if (var1 != null) {
         var4.setComposite(AlphaComposite.SrcOver);
         var4.drawImage(var1, 6, var2, 12, var2 + 10, 0, 0, var1.getWidth(), var1.getHeight(), null);
      }

      var4.dispose();
      return var3;
   }

   private static String method6626(ScrollableGuiKeySet var0) {
      try {
         byte[] var1 = MessageDigest.getInstance("SHA-256")
            .digest((var0.bEl + "\n" + var0.bEm + (var0.bEn == null ? "" : "\nfast=" + var0.bEn)).getBytes(StandardCharsets.UTF_8));
         StringBuilder var2 = new StringBuilder(64);

         for (byte var6 : var1) {
            var2.append(String.format("%02x", var6));
         }

         return var2.toString();
      } catch (NoSuchAlgorithmException var7) {
         throw new IllegalStateException(var7);
      }
   }

   private static JSONObject fR(String var0) {
      return JSONObject.of("type", "minecraft:model", "model", var0);
   }

   private static JSONObject hV(String var0) {
      return JSONObject.of(
         "parent",
         "minecraft:item/generated",
         "textures",
         JSONObject.of("layer0", var0),
         "display",
         JSONObject.of("gui", JSONObject.of("scale", new float[]{1.125F, 1.125F, 1.0F}))
      );
   }

   private static BufferedImage dj(int var0) {
      BufferedImage var1 = method6628(var0);
      int[] var2 = new int[]{10, 9, 8, 9, 10, 9, 8};
      int[] var3 = new int[]{2, 3, 4, 5, 5, 6, 7};

      for (int var4 = 0; var4 < var2.length; var4++) {
         if (var0 + var3[var4] >= 0 && var0 + var3[var4] < 18) {
            var1.setRGB(var2[var4], var0 + var3[var4], -11745);
         }
      }

      return var1;
   }

   private static byte[] method6627(Integer var0) {
      return method6629(method6628(var0));
   }

   private static BufferedImage method6628(Integer var0) {
      BufferedImage var1 = new BufferedImage(18, 18, 2);

      for (int var2 = 0; var2 < 18; var2++) {
         for (int var3 = 7; var3 <= 10; var3++) {
            var1.setRGB(var3, var2, -13355461);
         }
      }

      if (var0 != null) {
         for (int var4 = Math.max(0, var0); var4 < Math.min(18, var0 + 10); var4++) {
            for (int var5 = 6; var5 <= 11; var5++) {
               var1.setRGB(var5, var4, -3815218);
            }
         }
      }

      return var1;
   }

   private static BufferedImage hW(String var0) {
      BufferedImage var1 = new BufferedImage(18, 18, 2);
      int var2 = var0.endsWith("enabled") ? -3815218 : -8947330;

      for (int var3 = 0; var3 < 18; var3++) {
         for (int var4 = 7; var4 <= 10; var4++) {
            var1.setRGB(var4, var3, -13355461);
         }
      }

      boolean var6 = var0.startsWith("up_");

      for (int var7 = 0; var7 < 4; var7++) {
         for (int var5 = 8 - var7; var5 <= 9 + var7; var5++) {
            var1.setRGB(var5, var6 ? 6 + var7 : 11 - var7, var2);
         }
      }

      for (int var8 = var6 ? 10 : 5; var8 <= (var6 ? 12 : 7); var8++) {
         for (int var9 = 8; var9 <= 9; var9++) {
            var1.setRGB(var9, var8, var2);
         }
      }

      return var1;
   }

   private static byte[] method6629(BufferedImage var0) {
      try {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         BufferedImage var2 = new BufferedImage(144, 144, 2);

         for (int var3 = 0; var3 < 144; var3++) {
            for (int var4 = 0; var4 < 144; var4++) {
               var2.setRGB(var4, var3, var0.getRGB(var4 / 8, var3 / 8));
            }
         }

         ImageIO.write(var2, "PNG", var1);
         return var1.toByteArray();
      } catch (IOException var5) {
         throw new IllegalStateException("PNG encoding unavailable", var5);
      }
   }
}
