package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.anim.DisplayTransform;
import dev.lone.itemsadder.particle.BedrockComponentSchema;
import dev.lone.itemsadder.particle.BedrockParticleParser;
import dev.lone.itemsadder.particle.ParticleEffectDefinition;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class GeneratedAssetStore {
   private volatile GeneratedAssets axG = new GeneratedAssets(Map.of(), Map.of(), Map.of());

   public GeneratedAssets getAssets() {
      return this.axG;
   }

   public synchronized void loadBundles(List var1) {
      this.loadBundlesFiltered(var1, var0 -> !var0.equals("ON_SHELF"));
   }

   public synchronized void loadBundlesFiltered(List var1, Predicate var2) {
      LinkedHashMap var3 = new LinkedHashMap();
      LinkedHashMap var4 = new LinkedHashMap();
      LinkedHashMap var5 = new LinkedHashMap();
      PngImageCache var6 = new PngImageCache();
      long var7 = 0L;
      long var9 = 0L;

      for (Path var12 : var1) {
         try (Stream var13 = Files.walk(var12)) {
            for (Path var15 : var13.filter(var0 -> Files.isRegularFile(var0))
               .filter(var0 -> var0.getFileName().toString().endsWith(".iaparticle"))
               .sorted(Comparator.comparing(Path::toString))
               .toList()) {
               long var16 = Files.size(var15);
               if (var16 > 16777228L || (var7 += var16) > 67108864L || var3.size() >= 1024) {
                  throw new IllegalArgumentException("Particle bundle load budget exceeded at " + var15);
               }

               JSONObject var18;
               try {
                  var18 = IaImageCodec.decodeBundle(Files.readAllBytes(var15));
               } catch (IllegalArgumentException var36) {
                  throw new IllegalArgumentException(var15 + ": " + var36.getMessage(), var36);
               }

               String var19 = var18.getString("root");
               if (var3.containsKey(var19)) {
                  throw new IllegalArgumentException("Duplicate particle root " + var19 + " at " + var15);
               }

               LinkedHashMap var20 = new LinkedHashMap();
               JSONObject var21 = var18.getJSONObject("textures");

               for (String var23 : var21.keySet()) {
                  PngImage var24 = var6.decodePng(Base64.getDecoder().decode(var21.getString(var23)));
                  var20.put(var23, var24);
                  if (var4.putIfAbsent(var24.digest(), var24) == null) {
                     var9 += (long)var24.width() * var24.height();
                     if (var9 > 16777216L) {
                        throw new IllegalArgumentException("Particle texture registry exceeds decoded pixel budget");
                     }
                  }
               }

               LinkedHashMap var38 = new LinkedHashMap();
               LinkedHashMap var39 = new LinkedHashMap();
               LinkedHashMap var40 = new LinkedHashMap();
               JSONObject var25 = var18.getJSONObject("effects");

               for (String var27 : var25.keySet()) {
                  JSONObject var28 = var25.getJSONObject(var27);
                  BedrockComponentSchema.method2141(var28.getJSONObject("source"));
                  ParticleEffectDefinition var29 = new ParticleEffectDefinition(var28.getJSONObject("source"), var28.getJSONArray("variants"));
                  var39.put(var27, var29);
                  JSONObject var30 = var28.getJSONObject("display");
                  if (!var29.au("particle_appearance_lighting") && (var30 == null || !var30.containsKey("brightness"))) {
                     var30 = var30 == null ? new JSONObject() : new JSONObject(var30);
                     var30.put("brightness", JSONObject.of("block", 15, "sky", 15));
                  }

                  DisplayTransform var31 = new DisplayTransform(var30, var29.avo);
                  if (!var2.test(var31.awN)) {
                     throw new IllegalArgumentException(var15 + ": unsupported particle display context " + var31.awN + " on this server");
                  }

                  var40.put(var27, var31);
                  ArrayList var32 = new ArrayList();
                  if (var28.containsKey("variants")) {
                     for (Object var34 : var28.getJSONArray("variants")) {
                        var32.add(
                           BedrockParticleParser.method2135(var28.getJSONObject("source"), (PngImage)var20.get(((JSONObject)var34).getString("texture")), var5)
                        );
                     }
                  } else {
                     var32.add(BedrockParticleParser.method2135(var28.getJSONObject("source"), (PngImage)var20.get(var28.getString("texture")), var5));
                  }

                  var38.put(var27, List.copyOf(var32));
                  if (var5.size() > 8192) {
                     throw new IllegalArgumentException("Particle model registry exceeds 8192 models");
                  }
               }

               var3.put(var19, new AssetBundle(var15, var19, Map.copyOf(var20), Map.copyOf(var38), Map.copyOf(var39), Map.copyOf(var40)));
            }
         }
      }

      this.axG = new GeneratedAssets(Map.copyOf(var3), Map.copyOf(var4), Map.copyOf(var5));
   }

   public static String particleTexturePath(PngImage var0) {
      return "assets/itemsadder/textures/item/iaparticles/" + var0.digest() + ".png";
   }
}
