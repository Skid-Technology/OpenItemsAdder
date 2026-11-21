package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.PackEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

final class PackStatistics {
   static int pruneOverlaidFiles(Map var0) {
      PackEntry var1 = (PackEntry)var0.get("pack.mcmeta");
      if (var1 != null && !var1.isRemoved()) {
         ArrayList var2 = new ArrayList();

         try {
            JSONObject var3 = JSON.parseObject(var1.getText()).getJSONObject("overlays");
            if (var3 == null) {
               return 0;
            }

            HashSet var4 = new HashSet();

            for (Object var6 : var3.getJSONArray("entries")) {
               JSONObject var7 = (JSONObject)var6;
               String var8 = var7.getString("directory");
               if (var8 != null && var8.matches("[-_a-zA-Z0-9.]+") && var4.add(var8)) {
                  SizeRange var9 = parseLegacyRange(var7.get("formats"));
                  SizeRange var10 = !var7.containsKey("min_format") && !var7.containsKey("max_format")
                     ? var9
                     : new SizeRange(parseFormatBound(var7.get("min_format"), false), parseFormatBound(var7.get("max_format"), true));
                  if (var10 != null && var10.min <= var10.max && (var9 == null || var9.min <= var9.max)) {
                     var2.add(new PackRangeEntry(var8, var10, var9));
                     continue;
                  }

                  return 0;
               }

               return 0;
            }
         } catch (RuntimeException var11) {
            return 0;
         }

         int var12 = 0;

         for (int var13 = 0; var13 < var2.size(); var13++) {
            PackRangeEntry var14 = (PackRangeEntry)var2.get(var13);
            if (var14.directory.startsWith("ia_overlay_")) {
               String var15 = var14.directory + "/";

               for (String var17 : new ArrayList(var0.keySet())) {
                  if (var17.startsWith(var15 + "assets/") && !((PackEntry)var0.get(var17)).isRemoved()) {
                     String var18 = var17.substring(var15.length());
                     byte[] var19 = ((PackEntry)var0.get(var17)).getBytes();
                     if (isCoveredByOverlay(var0, var2, var13, var18, var19, false) && isCoveredByOverlay(var0, var2, var13, var18, var19, true)) {
                        var0.remove(var17);
                        var12++;
                     }
                  }
               }
            }
         }

         return var12;
      } else {
         return 0;
      }
   }

   private static boolean isCoveredByOverlay(Map var0, List var1, int var2, String var3, byte[] var4, boolean var5) {
      PackRangeEntry var6 = (PackRangeEntry)var1.get(var2);
      SizeRange var7 = var5 ? var6.legacy : var6.modern;
      if (var7 == null) {
         return true;
      }

      TreeSet var8 = new TreeSet();
      var8.add(var7.min);

      for (int var9 = 0; var9 < var2; var9++) {
         SizeRange var10 = var5 ? ((PackRangeEntry)var1.get(var9)).legacy : ((PackRangeEntry)var1.get(var9)).modern;
         if (var10 != null) {
            if (var7.contains(var10.min)) {
               var8.add(var10.min);
            }

            long var11 = nextFormatBound(var10.max);
            if (var7.contains(var11)) {
               var8.add(var11);
            }
         }
      }

      for (long var18 : var8) {
         PackEntry var12 = null;

         for (int var13 = var2 - 1; var13 >= 0; var13--) {
            PackRangeEntry var14 = (PackRangeEntry)var1.get(var13);
            SizeRange var15 = var5 ? var14.legacy : var14.modern;
            if (var15 != null && var15.contains(var18)) {
               PackEntry var16 = (PackEntry)var0.get(var14.directory + "/" + var3);
               if (var16 != null && !var16.isRemoved()) {
                  var12 = var16;
                  break;
               }
            }
         }

         if (var12 == null) {
            var12 = (PackEntry)var0.get(var3);
         }

         if (var12 == null || var12.isRemoved() || !Arrays.equals(var4, var12.getBytes())) {
            return false;
         }
      }

      return true;
   }

   private static long nextFormatBound(long var0) {
      return (var0 & 4294967295L) == 2147483647L ? (var0 >>> 32) + 1L << 32 : var0 + 1L;
   }

   private static long parseFormatBound(Object var0, boolean var1) {
      if (var0 instanceof JSONArray var2) {
         if (var2.size() != 1 && var2.size() != 2) {
            throw new IllegalArgumentException("format length");
         } else {
            return parseFormatNumber(var2.get(0)) << 32 | (var2.size() == 2 ? parseFormatNumber(var2.get(1)) : (var1 ? 2147483647L : 0L));
         }
      } else {
         return parseFormatNumber(var0) << 32 | (var1 ? Integer.MAX_VALUE : 0);
      }
   }

   private static long parseFormatNumber(Object var0) {
      if (!(var0 instanceof Integer) && !(var0 instanceof Long)) {
         throw new IllegalArgumentException("format number");
      } else {
         long var1 = ((Number)var0).longValue();
         if (var1 >= 0L && var1 <= 2147483647L) {
            return var1;
         } else {
            throw new IllegalArgumentException("format bound");
         }
      }
   }

   private static SizeRange parseLegacyRange(Object var0) {
      if (var0 == null) {
         return null;
      }

      if (var0 instanceof JSONArray var4) {
         if (var4.size() != 2) {
            throw new IllegalArgumentException("legacy range");
         } else {
            return new SizeRange(parseFormatNumber(var4.get(0)) << 32, parseFormatNumber(var4.get(1)) << 32);
         }
      } else if (var0 instanceof JSONObject var3) {
         return new SizeRange(parseFormatNumber(var3.get("min_inclusive")) << 32, parseFormatNumber(var3.get("max_inclusive")) << 32);
      } else {
         long var1 = parseFormatNumber(var0) << 32;
         return new SizeRange(var1, var1);
      }
   }
}
