package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader.Feature;
import dev.lone.itemsadder.entity.TransformJsonCodec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.jetbrains.annotations.Nullable;

public class HumanoidEquipmentPack {
   private final String buP;
   private final Map buQ;
   private final Map buR;
   public static final String buS = "ia_humanoid_equipment.json";
   public static final String buT = "ia_humanoid_equipment_";
   public static final List buU = List.of(
      new PackVersionSpec("1.21.4", 769, 46, 0),
      new PackVersionSpec("1.21.5", 770, 55, 0),
      new PackVersionSpec("1.21.6", 771, 63, 0),
      new PackVersionSpec("1.21.8", 772, 64, 0),
      new PackVersionSpec("1.21.10", 773, 69, 0),
      new PackVersionSpec("1.21.11", 774, 75, 0),
      new PackVersionSpec("26.1.2", 775, 84, 0),
      new PackVersionSpec("26.2", 776, 88, 0),
      new PackVersionSpec("26.3", 777, 97, 1)
   );

   public HumanoidEquipmentPack(String var1, Map var2, Map var3) {
      this.buP = var1;
      this.buQ = var2;
      this.buR = var3;
   }

   @Nullable
   public static PackVersionSpec findVersionSpec(int var0) {
      return buU.stream().filter(var1 -> var1.protocol == var0).findFirst().orElse(null);
   }

   public static byte[] encodeManifest(Map var0, Map var1) {
      JSONObject var2 = new JSONObject();
      var0.forEach(
         (var1x, var2x) -> {
            JSONObject var3 = new JSONObject();
            var2x.equipment().forEach((var1xx, var2xx) -> {
               JSONObject var3x = new JSONObject();
               var2xx.layers().forEach((var1xxx, var2xxx) -> {
                  JSONArray var3xx = new JSONArray();

                  for (PackOverrideEntry var5 : var2xxx) {
                     var3xx.add(JSONObject.of("texture", var5.texture(), "dyeable", var5.dyeable(), "undyed", var5.undyed()));
                  }

                  var3x.put(var1xxx, var3xx);
               });
               JSONArray var4x = new JSONArray();

               for (PackTextureRef var6 : var2xx.overrides()) {
                  var4x.add(JSONObject.of("material", var6.material(), "pattern", var6.pattern(), "texture", var6.texture(), "palette", var6.palette()));
               }

               var3.put(var1xx, JSONObject.of("layers", var3x, "overrides", var4x));
            });
            JSONObject var4 = new JSONObject();
            var2x.materials().forEach((var1xx, var2xx) -> var4.put(var1xx, JSONObject.of("palette", var2xx.palette(), "overrides", var2xx.overrides())));
            var2.put(
               var1x,
               JSONObject.of("version", var2x.version(), "equipment", var3, "patterns", var2x.patterns(), "materials", var4, "palettes", var2x.palettes())
            );
         }
      );
      return JSON.toJSONBytes(JSONObject.of("version", 1, "descriptorSha256", TransformJsonCodec.Cx, "catalogs", var2, "geometryScales", var1));
   }

   @Nullable
   public static HumanoidEquipmentPack loadFromZip(Path var0, String var1) {
      try (ZipFile var2 = new ZipFile(var0.toFile())) {
         ZipEntry var3 = var2.getEntry("ia_humanoid_equipment.json");
         if (var3 == null) {
            return null;
         }

         if (!ResourcePackCompressor.digestFile(var0, "SHA-1").equals(var1)) {
            throw new IOException("Equipment pack does not match the published hash");
         }

         if (var3.getSize() < 0L || var3.getSize() > 8388608L) {
            throw new IOException("Invalid equipment manifest size");
         }

         JSONObject var4;
         try (InputStream var5 = var2.getInputStream(var3)) {
            var4 = JSON.parseObject(var5.readAllBytes());
         }

         if (var4.getIntValue("version") != 1 || !TransformJsonCodec.Cx.equals(var4.getString("descriptorSha256"))) {
            throw new IOException("Equipment pack uses a different geometry recipe; regenerate it");
         }

         HashMap var14 = new HashMap();

         for (PackVersionSpec var7 : buU) {
            JSONObject var8 = var4.getJSONObject("catalogs").getJSONObject(var7.version);
            if (var8 != null) {
               var14.put(var7.version, parseCatalog(var8));
            }
         }

         HashMap var15 = new HashMap();
         var4.getJSONObject("geometryScales").forEach((var1x, var2x) -> {
            float var3x = ((Number)var2x).floatValue();
            if (var1x.matches("_iainternal:humanoid_equipment/[0-9a-f]{64}") && Float.isFinite(var3x) && !(var3x < 1.0F) && !(var3x > 16.0F)) {
               var15.put(var1x, var3x);
            } else {
               throw new IllegalArgumentException("Invalid equipment geometry scale");
            }
         });
         return new HumanoidEquipmentPack(var1, Map.copyOf(var14), Map.copyOf(var15));
      }
   }

   private static PackDefinition parseCatalog(JSONObject var0) {
      HashMap var1 = new HashMap();
      JSONObject var2 = var0.getJSONObject("equipment");

      for (String var4 : var2.keySet()) {
         JSONObject var5 = var2.getJSONObject(var4);
         JSONObject var6 = var5.getJSONObject("layers");
         HashMap var7 = new HashMap();

         for (String var9 : var6.keySet()) {
            ArrayList var10 = new ArrayList();

            for (Object var12 : var6.getJSONArray(var9)) {
               JSONObject var13 = (JSONObject)var12;
               var10.add(new PackOverrideEntry(var13.getString("texture"), var13.getBooleanValue("dyeable"), var13.getInteger("undyed")));
            }

            var7.put(var9, List.copyOf(var10));
         }

         ArrayList var22 = new ArrayList();

         for (Object var25 : var5.getJSONArray("overrides")) {
            JSONObject var26 = (JSONObject)var25;
            var22.add(new PackTextureRef(var26.getString("material"), var26.getString("pattern"), var26.getString("texture"), var26.getString("palette")));
         }

         var1.put(var4, new PackPlan(Map.copyOf(var7), List.copyOf(var22)));
      }

      HashMap var14 = new HashMap();
      JSONObject var15 = var0.getJSONObject("materials");

      for (String var18 : var15.keySet()) {
         JSONObject var20 = var15.getJSONObject(var18);
         var14.put(var18, new PackOverrideGroup(var20.getString("palette"), parseStringMap(var20.getJSONObject("overrides"))));
      }

      HashMap var17 = new HashMap();
      JSONObject var19 = var0.getJSONObject("palettes");

      for (String var23 : var19.keySet()) {
         var17.put(var23, List.copyOf(var19.getJSONArray(var23).toJavaList(Integer.class, new Feature[0])));
      }

      return new PackDefinition(
         var0.getString("version"), Map.copyOf(var1), parseStringMap(var0.getJSONObject("patterns")), Map.copyOf(var14), Map.copyOf(var17)
      );
   }

   private static Map parseStringMap(JSONObject var0) {
      HashMap var1 = new HashMap();
      var0.forEach((var1x, var2) -> var1.put(var1x, (String)var2));
      return Map.copyOf(var1);
   }

   public String hash() {
      return this.buP;
   }

   public Map catalogs() {
      return this.buQ;
   }

   public Map geometryScales() {
      return this.buR;
   }
}
