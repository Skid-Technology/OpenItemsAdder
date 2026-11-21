package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.config.YamlConfig;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import org.bukkit.configuration.ConfigurationSection;

public final class ShaderFingerprint {
   public static final ShaderFingerprint bFR = new ShaderFingerprint(Map.of());
   private final Map bFS;
   private final String bFT;

   private ShaderFingerprint(Map var1) {
      this.bFS = var1;
      this.bFT = computeHash(var1);
   }

   public static ShaderFingerprint fromSoundPackFiles(Collection var0) {
      Objects.requireNonNull(var0, "files");
      ArrayList var1 = new ArrayList();

      for (YamlConfig var3 : var0) {
         Objects.requireNonNull(var3, "files contains null");
         Object var4 = var3.abM().get("pack_settings.sound_packs");
         if (var4 != null) {
            var1.add(new ShaderPathGroup(var3.GQ(), asStringMap(var4, var3.GQ(), "pack_settings.sound_packs")));
         }
      }

      return build(var1);
   }

   static ShaderFingerprint fromMap(Map var0) {
      Objects.requireNonNull(var0, "files");
      ArrayList var1 = new ArrayList();
      var0.forEach(
         (var1x, var2) -> var1.add(new ShaderPathGroup(Objects.requireNonNull(var1x, "source path"), Objects.requireNonNull(var2, "sound packs in " + var1x)))
      );
      return build(var1);
   }

   public boolean isEmpty() {
      return this.bFS.isEmpty();
   }

   public Map getSoundPacks() {
      return this.bFS;
   }

   public String getHash() {
      return this.bFT;
   }

   private static ShaderFingerprint build(List var0) {
      if (var0.isEmpty()) {
         return bFR;
      }

      var0.sort(Comparator.comparing(ShaderPathGroup::path));
      TreeMap var1 = new TreeMap();
      HashMap var2 = new HashMap();
      HashMap var3 = new HashMap();

      for (ShaderPathGroup var5 : var0) {
         for (Entry var7 : new TreeMap(var5.bFV).entrySet()) {
            String var8 = (String)var7.getKey();
            require(var8 != null && var8.matches("[a-z0-9_-]+") && var8.length() <= 64, var5.bFU, "invalid sound-pack ID '" + var8 + "'");
            String var9 = var2.putIfAbsent(var8, var5.bFU);
            require(var9 == null, var5.bFU, "duplicate sound-pack ID '" + var8 + "' (also in " + var9 + ")");
            Map var10 = asStringMap(var7.getValue(), var5.bFU, "sound pack '" + var8 + "'");
            Object var11 = var10.get("namespaces");
            require(var11 instanceof List, var5.bFU, "sound pack '" + var8 + "' must define a namespaces list");
            TreeSet var12 = new TreeSet();

            for (Object var14 : (List)var11) {
               require(var14 instanceof String, var5.bFU, "sound pack '" + var8 + "' has a non-string namespace");
               String var15 = (String)var14;
               require(var15.matches("[a-z0-9_.-]+"), var5.bFU, "invalid namespace '" + var15 + "' in sound pack '" + var8 + "'");
               require(var12.add(var15), var5.bFU, "namespace '" + var15 + "' is listed more than once in sound pack '" + var8 + "'");
               String var16 = var3.putIfAbsent(var15, var8);
               require(
                  var16 == null,
                  var5.bFU,
                  "namespace '" + var15 + "' belongs to both '" + var16 + "' (" + (String)var2.get(var16) + ") and '" + var8 + "' (" + var5.bFU + ")"
               );
            }

            var1.put(var8, List.copyOf(var12));
         }
      }

      return var1.isEmpty() ? bFR : new ShaderFingerprint(Collections.unmodifiableMap(new LinkedHashMap(var1)));
   }

   private static Map asStringMap(Object var0, String var1, String var2) {
      if (var0 instanceof ConfigurationSection var7) {
         return var7.getValues(false);
      } else if (!(var0 instanceof Map var3)) {
         throw invalid(var1, var2 + " must be a map");
      } else {
         LinkedHashMap var4 = new LinkedHashMap();

         for (Entry var6 : var3.entrySet()) {
            require(var6.getKey() instanceof String, var1, var2 + " has a non-string key");
            var4.put((String)var6.getKey(), var6.getValue());
         }

         return var4;
      }
   }

   private static String computeHash(Map var0) {
      try {
         MessageDigest var1 = MessageDigest.getInstance("SHA-256");
         var0.forEach((var1x, var2x) -> {
            var1.update(var1x.getBytes(StandardCharsets.UTF_8));
            var1.update((byte)0);
            var2x.forEach(var1xx -> {
               var1.update(var1xx.getBytes(StandardCharsets.UTF_8));
               var1.update((byte)0);
            });
            var1.update((byte)10);
         });
         return HexFormat.of().formatHex(var1.digest());
      } catch (NoSuchAlgorithmException var2) {
         throw new IllegalStateException(var2);
      }
   }

   private static void require(boolean var0, String var1, String var2) {
      if (!var0) {
         throw invalid(var1, var2);
      }
   }

   private static IllegalArgumentException invalid(String var0, String var1) {
      return new IllegalArgumentException(var0 + ": " + var1);
   }
}
