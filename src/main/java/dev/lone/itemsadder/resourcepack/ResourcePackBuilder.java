package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.PackEntry;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.internal.SwitchMap72;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

final class ResourcePackBuilder {
   private static final int MAX_ITEM_JSON_BYTES = 8388608;
   private static final Pattern ITEM_DEF_PATTERN = Pattern.compile("assets/[a-z0-9_.-]+/items/[a-z0-9/._-]+\\.json");
   private static final Pattern MODEL_ITEM_PATTERN = Pattern.compile("assets/minecraft/models/item/[a-z0-9_.-]+\\.json");
   private static final long DEFAULT_MERGE_BUDGET = Math.max(65536L, Math.min(16777216L, Runtime.getRuntime().maxMemory() / 128L));
   private final long mergeBudget;
   private long usedBudget;
   private final Map indexes = new LinkedHashMap();
   private final Set skippedItems = new HashSet();

   ResourcePackBuilder() {
      this(DEFAULT_MERGE_BUDGET);
   }

   ResourcePackBuilder(long var1) {
      if (var1 < 1024L) {
         throw new IllegalArgumentException("item merge budget must be at least 1 KiB");
      }

      this.mergeBudget = var1;
   }

   void registerItemInput(File var1, File var2, String var3, Consumer var4) {
      if (isItemMergeTarget(var3)) {
         String var5 = stripPackPrefix(var3);
         Path var6 = var1.toPath().toAbsolutePath().normalize();

         try {
            PackIndex var7 = (PackIndex)this.indexes.get(var6);
            if (var7 == null) {
               Path var8 = var6.resolve("pack.mcmeta");
               JSONObject var9 = Files.isRegularFile(var8) ? this.readJsonBounded(var8) : new JSONObject();
               if (var9.get("overlays") instanceof JSONObject var10) {
                  Object var16 = var10.get("entries");
                  if (var16 instanceof JSONArray) {
                     for (Object var14 : (JSONArray)var16) {
                        if (var14 instanceof JSONObject var12) {
                           normalizeOverlayEntry(var12);
                        }
                     }
                  }
               }

               var7 = new PackIndex(var9);
               this.indexes.put(var6, var7);
            }

            if (!var7.buh.containsKey(var3)) {
               var7.buh.put(var3, this.readJsonBounded(var2.toPath()));
               var7.bui.put(var3, var2.toString());
            }
         } catch (IOException | RuntimeException var15) {
            this.skippedItems.add(isLegacyModelItemPath(var5) ? var5.replace("/models/item/", "/items/") : var5);
            var4.accept("[Pack] Item merge skipped input " + var2 + ": " + var15.getMessage());
         }
      }
   }

   private JSONObject readJsonBounded(Path var1) {
      try (InputStream var2 = Files.newInputStream(var1)) {
         int var3 = (int)Math.min(8388608L, this.mergeBudget - this.usedBudget);
         if (var3 <= 0) {
            throw new IOException("item import memory budget exhausted");
         }

         byte[] var4 = var2.readNBytes(var3 + 1);
         if (var4.length > var3) {
            throw new IOException("item JSON exceeds file/import memory budget");
         }

         this.usedBudget += var4.length;
         if (var4.length >= 3 && (var4[0] & 255) == 239 && (var4[1] & 255) == 187 && (var4[2] & 255) == 191) {
            var4 = Arrays.copyOfRange(var4, 3, var4.length);
         }

         JSONObject var5 = JSON.parseObject(var4);
         if (var5 == null) {
            throw new IOException("null JSON object");
         } else {
            return var5;
         }
      }
   }

   static void normalizeOverlayEntry(JSONObject var0) {
      if (var0.get("formats") instanceof JSONArray var1
         && var1.size() == 1
         && var1.get(0) instanceof JSONObject var2
         && var2.containsKey("min_inclusive")
         && var2.containsKey("max_inclusive")) {
         var0.put("formats", JSON.copy(var2, new Feature[0]));
      }

      ResourcePackGenerator.normalizeFormatRange(var0);
   }

   static String resolveAssetPath(String var0, String var1) {
      if (var1.startsWith("assets/")) {
         return var0 + "/" + var1;
      }

      String[] var2 = var1.split("/", 3);
      return var2.length == 3
            && var2[0].matches("[a-z0-9_.-]+")
            && Set.of("items", "models", "textures", "atlases", "blockstates", "font", "sounds", "lang").contains(var2[1])
         ? var0 + "/assets/" + var1
         : var0 + "/" + var1;
   }

   void mergeModernItems(Map var1, ResourcePackGenerator var2, List var3, Consumer var4) {
      TreeMap var5 = new TreeMap();
      var2.overlayEntriesByRange.forEach((var1x, var2x) -> {
         byte var3x = switch (SwitchMap72.bug[var1x.ordinal()]) {
            case 1 -> 46;
            case 2 -> 63;
            case 3 -> 84;
            default -> throw new MatchException(null, null);
         };
         TreeMap var4x = new TreeMap();
         var2x.forEach((var1xx, var2xx) -> var4x.put("assets/minecraft/items/" + var1xx + ".json", var2xx.json));
         var5.put(Integer.valueOf(var3x), var4x);
      });
      this.mergeItemOverlays(
         var1,
         var5,
         var2.bzM,
         var3,
         var4,
         (var0, var1x) -> VanillaAssetManager.isBuiltinPath(var1x)
            || ContentManager.method8077(var1x)
            || ContentManager.method8084().field91.getModelJsonFor(var0, var1x) != null,
         (var0, var1x) -> ContentManager.method8078(var1x)
            || var1x.startsWith("minecraft:") && ContentManager.method8084().field91.getAssetList(var0).contains("textures/" + var1x.substring(10) + ".png"),
         ResourcePackBuilder::modelJsonFor
      );
   }

   void mergeItems(Map var1, Map var2, Map var3, List var4, Consumer var5) {
      this.mergeItemsWithChecks(var1, var2, var3, var4, var5, (var0, var1x) -> true, (var0, var1x) -> true);
   }

   void mergeItemsWithChecks(Map var1, Map var2, Map var3, List var4, Consumer var5, BiPredicate var6, BiPredicate var7) {
      this.mergeItemOverlays(var1, var2, var3, var4, var5, var6, var7, (var0, var1x) -> null);
   }

   void mergeItemOverlays(Map var1, Map var2, Map var3, List var4, Consumer var5, BiPredicate var6, BiPredicate var7, BiFunction var8) {
      if (!this.indexes.isEmpty()) {
         PackEntry var9 = (PackEntry)var1.get("pack.mcmeta");
         if (var9 != null && !var9.isRemoved()) {
            JSONObject var10 = JSON.parseObject(var9.getBytes());
            Function var11 = ResourcePackGenerator.createOverlayResolver(var1, (JSONObject)JSON.copy(var10, new Feature[0]));
            JSONObject var12 = var10.getJSONObject("overlays");
            if (var12 == null) {
               var10.put("overlays", var12 = new JSONObject());
            }

            JSONArray var13 = var12.getJSONArray("entries");
            if (var13 == null) {
               var12.put("entries", var13 = new JSONArray());
            }

            HashSet var14 = new HashSet();

            for (Object var16 : var13) {
               var14.add(((JSONObject)var16).getString("directory"));
            }

            List var55 = var4.stream().map(var0 -> var0.toPath().toAbsolutePath().normalize()).toList();
            ArrayList var56 = new ArrayList(this.indexes.entrySet());
            var56.sort(Comparator.comparingInt(var1x -> {
               int var2x = var55.indexOf(var1x.getKey());
               return var2x < 0 ? Integer.MAX_VALUE : var2x;
            }));
            PackCache var17 = new PackCache(this.mergeBudget / 4L);
            PackCache var18 = new PackCache(this.mergeBudget / 4L);
            IdentityHashMap var19 = new IdentityHashMap();
            IdentityHashMap var20 = new IdentityHashMap();
            TreeMap var21 = new TreeMap(var2);
            HashMap var22 = new HashMap();
            HashMap var23 = new HashMap();
            HashSet var24 = new HashSet();
            HashSet var25 = new HashSet();
            long var26 = 0L;
            boolean var28 = false;

            for (PackFormat var32 : PackFormat.values()) {
               for (PackVersion var34 : var32.getFormats()) {
                  if (var34.major() >= 46) {
                     Version var35 = versionFor(var34);
                     Map var36 = (Map)var11.apply(var35);
                     Map var37 = collectAtlasSpriteAliases(var1, var36);
                     String var38 = "ia_item_merge_" + var34.major() + "_" + var34.minor();
                     if (!var14.contains(var38) && !var1.keySet().stream().anyMatch(var1x -> var1x.startsWith(var38 + "/"))) {
                        TreeMap var39 = new TreeMap();
                        var21.headMap(var34.major(), true).values().forEach(var39::putAll);
                        TreeMap var40 = new TreeMap();
                        HashMap var41 = new HashMap();
                        HashSet var42 = new HashSet();
                        HashSet var43 = new HashSet();

                        for (Entry var45 : var56) {
                           PackIndex var46 = (PackIndex)var45.getValue();

                           Map var47;
                           try {
                              var47 = resolveOverlayEntries(var46, var34);
                           } catch (RuntimeException var54) {
                              var46.buh.keySet().forEach(var1x -> {
                                 String var2x = stripPackPrefix(var1x);
                                 var42.add(isLegacyModelItemPath(var2x) ? var2x.replace("/models/item/", "/items/") : var2x);
                              });
                              var5.accept("[Pack] Item merge skipped " + var45.getKey() + " for " + var34 + ": " + var54.getMessage());
                              continue;
                           }

                           TreeMap var48 = new TreeMap();
                           HashMap var49 = new HashMap();
                           var47.forEach((var3x, var4x) -> {
                              if (isItemDefinitionPath(var3x)) {
                                 var48.put(var3x, (JSONObject)var46.buh.get(var4x));
                                 var49.put(var3x, (String)var46.bui.get(var4x));
                              }
                           });
                           var47.forEach(
                              (var7x, var8x) -> {
                                 if (isLegacyModelItemPath(var7x)) {
                                    String var9x = var7x.replace("/models/item/", "/items/");
                                    if (!var48.containsKey(var9x)) {
                                       JSONObject var10x = (JSONObject)var46.buh.get(var8x);
                                       if (!var19.containsKey(var10x)) {
                                          var19.put(var10x, buildRangeDispatch(var10x, var7x));
                                       }

                                       JSONObject var11x = (JSONObject)var19.get(var10x);
                                       if (var11x != null) {
                                          var48.put(var9x, var11x);
                                          var49.put(var9x, (String)var46.bui.get(var8x));
                                          var43.add(var9x);
                                       } else if (((JSONObject)var46.buh.get(var8x)).containsKey("overrides")) {
                                          var5.accept(
                                             skipMessage(
                                                var9x, var34, (String)var46.bui.get(var8x), "legacy predicates require an explicit modern items definition"
                                             )
                                          );
                                       }
                                    }
                                 }
                              }
                           );
                           var48.forEach(
                              (var15, var16x) -> {
                                 JSONObject var17x = sanitizeItemDefinition(
                                    var16x, var15, var34, (String)var49.get(var15), var1, var36, var37, var35, var6, var7, var8, var5
                                 );
                                 if (var17x == null) {
                                    var42.add(var15);
                                 } else {
                                    JSONObject var18x = (JSONObject)var40.get(var15);
                                    if (var18x == null) {
                                       var40.put(var15, var17x);
                                       var41.put(var15, (String)var49.get(var15));
                                    } else if (!var42.contains(var15)) {
                                       OverlayPackFile var19x = var17.getOrCompute(
                                          new PackCacheKey(var17x, var18x, Set.of()), () -> OverlayPackBuilder.mergeDefinitions(var17x, var18x)
                                       );
                                       if (!var19x.isValid()) {
                                          var42.add(var15);
                                          var5.accept(skipMessage(var15, var34, (String)var41.get(var15) + " vs " + (String)var49.get(var15), var19x.error()));
                                       } else {
                                          var40.put(var15, var19x.json());
                                          var43.add(var15);
                                       }
                                    }
                                 }
                              }
                           );
                        }

                        TreeMap var57 = new TreeMap();

                        for (Entry var60 : var40.entrySet()) {
                           String var61 = (String)var60.getKey();
                           if (!var42.contains(var61) && !this.skippedItems.contains(var61) && !var24.contains(var61)) {
                              JSONObject var62 = (JSONObject)var60.getValue();
                              String var63 = var61.substring(var61.lastIndexOf(47) + 1, var61.length() - 5);
                              JSONObject var50 = (JSONObject)var39.get(var61);
                              byte[] var51;
                              if (var50 != null) {
                                 Set var52 = var3.getOrDefault(var63, Set.of());
                                 if (var52.isEmpty()) {
                                    continue;
                                 }

                                 OverlayPackFile var53 = var18.getOrCompute(
                                    new PackCacheKey(var62, var50, var52), () -> OverlayPackBuilder.buildOwnedOverlay(var62, var50, var52)
                                 );
                                 if (!var53.isValid()) {
                                    var5.accept(skipMessage(var61, var34, (String)var41.get(var61) + " vs ItemsAdder", var53.error()));
                                    continue;
                                 }

                                 var51 = var53.bytes();
                              } else {
                                 if (!var43.contains(var61)) {
                                    continue;
                                 }

                                 var51 = (byte[])var20.computeIfAbsent(var62, JSON::toJSONBytes);
                              }

                              if (var26 + var51.length > this.mergeBudget) {
                                 var24.add(var61);
                                 var22.getOrDefault(var61, List.of()).forEach(var1::remove);
                                 var26 -= var23.getOrDefault(var61, 0L);
                                 var23.remove(var61);
                                 var22.remove(var61);
                                 var18.clear();
                                 var17.clear();
                                 var20.clear();
                                 var5.accept(
                                    skipMessage(
                                       var61,
                                       var34,
                                       (String)var41.get(var61),
                                       "generation memory budget exceeded; merge disabled for this item in all versions"
                                    )
                                 );
                              } else {
                                 var26 += var51.length;
                                 var23.merge(var61, (long)var51.length, Long::sum);
                                 var57.put(var61, var51);
                              }
                           }
                        }

                        if (!var57.isEmpty()) {
                           var14.add(var38);
                           JSONObject var59 = new JSONObject().fluentPut("directory", var38);
                           var59.put("formats", var34.major());
                           var59.put("min_format", List.of(var34.major(), var34.minor()));
                           var59.put("max_format", List.of(var34.major(), var34.minor()));
                           var13.add(var59);
                           var25.add(var38);
                           var57.forEach((var3x, var4x) -> {
                              String var5x = var38 + "/" + var3x;
                              var1.put(var5x, PackEntry.bytes(var5x, var4x));
                              var22.computeIfAbsent(var3x, var0x -> new ArrayList<>()).add(var5x);
                           });
                           var28 = true;
                        }
                     } else {
                        var5.accept("[Pack] Item merge skipped reserved overlay collision: " + var38);
                     }
                  }
               }
            }

            if (var28) {
               var13.removeIf(var2x -> {
                  String var3x = ((JSONObject)var2x).getString("directory");
                  return var25.contains(var3x) && var1.keySet().stream().noneMatch(var1xx -> var1xx.startsWith(var3x + "/"));
               });
               var1.put("pack.mcmeta", PackEntry.bytes("pack.mcmeta", JSON.toJSONBytes(var10)));
            }

            this.indexes.clear();
            this.skippedItems.clear();
            this.usedBudget = 0L;
         }
      }
   }

   private static String skipMessage(String var0, PackVersion var1, String var2, String var3) {
      return "[Pack] Item merge skipped "
         + var0
         + " (format "
         + var1.major()
         + "."
         + var1.minor()
         + ", "
         + var2
         + "): "
         + var3
         + "; existing precedence retained.";
   }

   private static Version versionFor(PackVersion var0) {
      return switch (var0.major()) {
         case 46 -> Version.v1_21_4;
         case 55 -> Version.v1_21_5;
         case 63 -> Version.v1_21_6;
         case 64 -> Version.v1_21_8;
         case 69 -> Version.v1_21_10;
         case 75 -> Version.v1_21_11;
         case 84 -> Version.v26_1_2;
         case 88 -> Version.v26_2;
         case 97 -> Version.v26_3;
         default -> throw new IllegalArgumentException("Unsupported item merge format " + var0);
      };
   }

   private static JSONObject modelJsonFor(Version var0, String var1) {
      JSONObject var2 = ContentManager.method8084().field91.getModelJsonFor(var0, var1);
      return var2 == null ? null : var2;
   }

   private static JSONObject sanitizeItemDefinition(
      JSONObject var0,
      String var1,
      PackVersion var2,
      String var3,
      Map var4,
      Map var5,
      Map var6,
      Version var7,
      BiPredicate var8,
      BiPredicate var9,
      BiFunction var10,
      Consumer var11
   ) {
      JSONObject var12 = (JSONObject)JSON.copy(var0, new Feature[0]);
      JSONObject var13 = var12.getJSONObject("model");
      if (var13 == null) {
         return var12;
      }

      JSONObject var14 = sanitizeModelTree(var13, null, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, 0);
      if (var14 == null) {
         return null;
      }

      var12.put("model", var14);
      return var12;
   }

   private static JSONObject sanitizeModelTree(
      JSONObject var0,
      JSONObject var1,
      String var2,
      PackVersion var3,
      String var4,
      Map var5,
      Map var6,
      Map var7,
      Version var8,
      BiPredicate var9,
      BiPredicate var10,
      BiFunction var11,
      Consumer var12,
      int var13
   ) {
      if (var13 > 128) {
         return var0;
      }

      String var14 = var0.getString("type");
      if (var14 == null) {
         return var0;
      }

      if (var14.startsWith("minecraft:")) {
         var14 = var14.substring(10);
      }

      if (!var14.equals("model") && !var14.equals("special")) {
         if (var0.get("fallback") instanceof JSONObject var23) {
            JSONObject var26 = sanitizeModelTree(var23, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13 + 1);
            if (var26 == null) {
               return null;
            }

            var0.put("fallback", var26);
            var1 = var26;
         }

         for (String var27 : List.of("on_true", "on_false")) {
            if (var0.get(var27) instanceof JSONObject var17) {
               JSONObject var30 = sanitizeModelTree(var17, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13 + 1);
               if (var30 == null) {
                  return null;
               }

               var0.put(var27, var30);
            }
         }

         for (String var28 : List.of("entries", "cases", "models")) {
            if (var0.get(var28) instanceof JSONArray var29) {
               JSONObject var32 = var1;
               if (var32 == null && var2.startsWith("assets/minecraft/items/") && var2.endsWith(".json")) {
                  String var19 = var2.substring("assets/minecraft/items/".length(), var2.length() - 5);
                  JSONObject var20 = new JSONObject().fluentPut("type", "model").fluentPut("model", "minecraft:item/" + var19);
                  if (validateModelChain(var20, var5, var6, var7, var8, var9, var10, var11) == null) {
                     var32 = var20;
                  }
               }

               for (int var33 = 0; var33 < var29.size(); var33++) {
                  JSONObject var34 = var29.getJSONObject(var33);
                  if (var34 != null) {
                     if (var34.get("model") instanceof JSONObject var21) {
                        JSONObject var35 = sanitizeModelTree(var21, var32, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13 + 1);
                        if (var35 == null) {
                           return null;
                        }

                        var34.put("model", var35);
                     } else if (var28.equals("models")) {
                        JSONObject var36 = sanitizeModelTree(var34, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13 + 1);
                        if (var36 == null) {
                           return null;
                        }

                        var29.set(var33, var36);
                     }
                  }
               }

               if (var28.equals("entries")
                  && var14.equals("range_dispatch")
                  && var29.stream().allMatch(var0x -> var0x instanceof JSONObject var1x && var1x.get("threshold") instanceof Number)) {
                  var29.sort((var0x, var1x) -> Float.compare(((JSONObject)var0x).getFloatValue("threshold"), ((JSONObject)var1x).getFloatValue("threshold")));
               }
            }
         }

         return var0;
      } else {
         String var15 = validateModelChain(var0, var5, var6, var7, var8, var9, var10, var11);
         if (var15 != null) {
            if (var1 == null) {
               var12.accept(skipMessage(var2, var3, var4, var15 + " and no valid fallback"));
            } else {
               var12.accept(
                  "[Pack] Item merge ignored invalid variant "
                     + var2
                     + " (format "
                     + var3.major()
                     + "."
                     + var3.minor()
                     + ", "
                     + var4
                     + "): "
                     + var15
                     + "; safe fallback used, other variants retained."
               );
            }

            return var1 == null ? null : (JSONObject)JSON.copy(var1, new Feature[0]);
         } else {
            return var0;
         }
      }
   }

   private static Map collectAtlasSpriteAliases(Map var0, Map var1) {
      HashMap var2 = new HashMap();
      var1.forEach(
         (var2x, var3) -> {
            if (var2x.contains("/atlases/") && var2x.endsWith(".json")) {
               try {
                  JSONObject var4 = JSON.parseObject(((PackEntry)var0.get(var3)).getBytes());
                  if (var4 != null && var4.get("sources") instanceof JSONArray var5) {
                     Iterator var10 = var5.iterator();

                     while (var10.hasNext()) {
                        if (var10.next() instanceof JSONObject var8
                           && "single".equals(var8.getString("type"))
                           && var8.getString("sprite") != null
                           && var8.getString("resource") != null) {
                           var2.put(ItemContextBuilder.normalizeId(var8.getString("sprite")), ItemContextBuilder.normalizeId(var8.getString("resource")));
                        }
                     }
                  }
               } catch (IOException | RuntimeException var9) {
               }
            }
         }
      );
      return var2;
   }

   private static boolean textureExists(String var0, Map var1, Version var2, BiPredicate var3) {
      int var4 = var0.indexOf(58);
      String var5 = "assets/" + var0.substring(0, var4) + "/textures/" + var0.substring(var4 + 1) + ".png";
      return var1.containsKey(var5) || var3.test(var2, var0);
   }

   private static String validateModelChain(JSONObject var0, Map var1, Map var2, Map var3, Version var4, BiPredicate var5, BiPredicate var6, BiFunction var7) {
      String var8 = var0.getString("type");
      if (var8 == null) {
         return null;
      }

      String var9 = !var8.endsWith(":special") && !var8.equals("special") ? var0.getString("model") : var0.getString("base");
      if (var9 == null) {
         return null;
      }

      LinkedHashMap var10 = new LinkedHashMap();
      LinkedHashSet var11 = new LinkedHashSet();
      HashSet var12 = new HashSet();
      boolean var13 = false;
      boolean var14 = false;
      String var15 = null;

      for (int var16 = 0; var16 < 128; var16++) {
         var9 = ItemContextBuilder.normalizeId(var9);
         if (!var12.add(var9)) {
            return "model parent cycle at " + var9;
         }

         if (var9.endsWith("item/generated") || var9.endsWith("item/handheld")) {
            var14 = true;
         }

         String var17 = ItemContextBuilder.modelJsonPath(var9);
         String var18 = (String)var2.get(var17);
         if (var15 == null) {
            var15 = var18 == null ? var9 : var18;
         }

         JSONObject var19;
         try {
            if (var18 == null) {
               if (!var5.test(var4, var9)) {
                  return "missing model " + var9 + " (" + var17 + ")";
               }

               var19 = (JSONObject)var7.apply(var4, var9);
            } else {
               var19 = JSON.parseObject(((PackEntry)var1.get(var18)).getBytes());
            }
         } catch (IOException | RuntimeException var29) {
            return "malformed model JSON " + (var18 == null ? var9 : var18) + ": " + var29.getMessage();
         }

         if (var19 == null) {
            if (var18 != null) {
               return "malformed model JSON " + var18;
            }
            break;
         }

         JSONObject var20 = var19.getJSONObject("textures");
         if (var20 != null) {
            var20.forEach((var1x, var2x) -> {
               String var3x = var2x instanceof String var5x ? var5x : (var2x instanceof JSONObject var4x ? var4x.getString("sprite") : null);
               if (var3x != null) {
                  var10.putIfAbsent(var1x, var3x);
               }
            });
         }

         if (!var13 && var19.containsKey("elements")) {
            var13 = true;
            if (var19.get("elements") instanceof JSONArray var21) {
               for (Object var26 : var21) {
                  if (var26 instanceof JSONObject var22 && var22.get("faces") instanceof JSONObject var23) {
                     for (Object var28 : var23.values()) {
                        if (var28 instanceof JSONObject var24 && var24.getString("texture") != null) {
                           var11.add(var24.getString("texture"));
                        }
                     }
                  }
               }
            }
         }

         var9 = var19.getString("parent");
         if (var9 == null) {
            break;
         }

         if (var16 == 127) {
            return "model parent depth exceeds 128";
         }
      }

      if (var14 && !var13) {
         var10.keySet().stream().filter(var0x -> var0x.matches("layer[0-9]+")).map(var0x -> "#" + var0x).forEach(var11::add);
      }

      for (String var32 : var11) {
         String var33 = var32;
         HashSet var35 = new HashSet();

         while (var33.startsWith("#")) {
            String var36 = var33.substring(1);
            if (!var35.add(var36)) {
               return "texture alias cycle at #" + var36;
            }

            var33 = (String)var10.get(var36);
            if (var33 == null) {
               return "missing texture slot #" + var36;
            }
         }

         var33 = ItemContextBuilder.normalizeId(var33);
         int var37 = var33.indexOf(58);
         String var38 = "assets/" + var33.substring(0, var37) + "/textures/" + var33.substring(var37 + 1) + ".png";
         String var39 = (String)var3.get(var33);
         if (!textureExists(var33, var2, var4, var6) && (var39 == null || !textureExists(var39, var2, var4, var6))) {
            return "missing texture " + var33 + " (" + var38 + ") in " + var15;
         }
      }

      return null;
   }

   private static Map resolveOverlayEntries(PackIndex var0, PackVersion var1) {
      TreeMap var2 = new TreeMap();
      var0.buh.keySet().stream().filter(var0x -> var0x.startsWith("assets/")).forEach(var1x -> var2.put(var1x, var1x));
      if (var0.buj.get("overlays") instanceof JSONObject var3) {
         Object var13 = var3.get("entries");
         if (var13 instanceof JSONArray) {
            for (Object var6 : (JSONArray)var13) {
               JSONObject var7 = (JSONObject)var6;
               String var8 = ResourcePackGenerator.overlayDirectoryName(var7);
               int[] var9 = ResourcePackGenerator.parseFormatRange(var7);
               if (var1.major() >= var9[0] - 1 && var1.major() <= var9[1]) {
                  PackVersion var10 = parseOverlayEndpoint(var7.get("min_format"), 0);
                  PackVersion var11 = parseOverlayEndpoint(var7.get("max_format"), Integer.MAX_VALUE);
                  if (var1.compareVersion(var10) >= 0 && var1.compareVersion(var11) <= 0) {
                     String var12 = var8 + "/";
                     var0.buh
                        .keySet()
                        .stream()
                        .filter(var1x -> var1x.startsWith(var12 + "assets/"))
                        .forEach(var2x -> var2.put(var2x.substring(var12.length()), var2x));
                  }
               }
            }
         }
      }

      return var2;
   }

   private static PackVersion parseOverlayEndpoint(Object var0, int var1) {
      if (var0 instanceof Number var3) {
         return new PackVersion(var3.intValue(), var1);
      } else if (var0 instanceof JSONArray var2 && var2.size() == 2) {
         return new PackVersion(var2.getIntValue(0), var2.getIntValue(1));
      } else {
         throw new IllegalArgumentException("invalid overlay endpoint");
      }
   }

   static JSONObject buildRangeDispatch(JSONObject var0, String var1) {
      if (var0.get("overrides") instanceof JSONArray var2 && !var2.isEmpty()) {
         JSONArray var12 = new JSONArray();
         float var4 = Float.NEGATIVE_INFINITY;

         for (Object var6 : var2) {
            if (var6 instanceof JSONObject var7
               && var7.get("predicate") instanceof JSONObject var8
               && var8.size() == 1
               && var8.get("custom_model_data") instanceof Number var9
               && var7.get("model") instanceof String var10) {
               float var17 = var9.floatValue();
               if (Float.isFinite(var17) && !(var17 <= var4)) {
                  var4 = var17;
                  var12.add(
                     new JSONObject()
                        .fluentPut("threshold", var17)
                        .fluentPut("model", new JSONObject().fluentPut("type", "minecraft:model").fluentPut("model", var10))
                  );
                  continue;
               }

               return null;
            }

            return null;
         }

         String[] var13 = var1.split("/", 4);
         String var14 = var13[1] + ":" + var13[3].substring(0, var13[3].length() - 5);
         return new JSONObject()
            .fluentPut(
               "model",
               new JSONObject()
                  .fluentPut("type", "minecraft:range_dispatch")
                  .fluentPut("property", "minecraft:custom_model_data")
                  .fluentPut("entries", var12)
                  .fluentPut("fallback", new JSONObject().fluentPut("type", "minecraft:model").fluentPut("model", var14))
            );
      } else {
         return null;
      }
   }

   private static String stripPackPrefix(String var0) {
      return var0.startsWith("assets/") ? var0 : var0.substring(var0.indexOf(47) + 1);
   }

   static boolean isItemMergeTarget(String var0) {
      if (!var0.endsWith(".json")) {
         return false;
      }

      String var1 = stripPackPrefix(var0);
      return isItemDefinitionPath(var1) || isLegacyModelItemPath(var1);
   }

   private static boolean isItemDefinitionPath(String var0) {
      return ITEM_DEF_PATTERN.matcher(var0).matches();
   }

   private static boolean isLegacyModelItemPath(String var0) {
      return MODEL_ITEM_PATTERN.matcher(var0).matches();
   }
}
