package dev.lone.itemsadder.resourcepack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.PackEntry;
import dev.lone.itemsadder.internal.SwitchMap76;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

final class ResourcePackLoader {
   private final Map sourceEntries;
   private final BiFunction sourceResolver;
   private final List atlasSearchDirs;
   private final List diagnostics = new ArrayList();
   private final Map pngStats = new HashMap();
   private final Map mcmetaByPath = new HashMap();
   private final Set skippedMetadata = new HashSet();
   private boolean pixelOptimizable = true;
   private SolidifiedFrameCache frameCache;

   private ResourcePackLoader(Map var1, BiFunction var2, List var3) {
      this.sourceEntries = var1;
      this.sourceResolver = var2;
      this.atlasSearchDirs = var3;
   }

   static PackLoadResult validatePack(Map var0, BiFunction var1, List var2) {
      ResourcePackLoader var3 = new ResourcePackLoader(var0, var1, var2);
      var3.normalizeTextureMetadata();
      Map var4 = Map.of();

      try {
         var4 = var3.collectOverlayPaths();
         List var5 = var3.collectAtlasFolders(var4);
         var3.reportAtlasWarnings(var4, var5);
      } catch (IOException | RuntimeException var10) {
         var3.addFinding(
            PackFindingSeverity.WARNING, "ATLAS_VIEW", "pack.mcmeta", "Could not validate the 26.2 atlas view; no atlas changes made: " + var10.getMessage()
         );
      }

      TreeMap var11 = new TreeMap();

      for (Entry var7 : var3.pngStats.entrySet()) {
         String var8 = ((String)var7.getKey()).startsWith("assets/")
            ? (String)var7.getKey()
            : ((String)var7.getKey()).substring(((String)var7.getKey()).indexOf("/assets/") + 1);
         if (((PackStatsTuple)var7.getValue()).Xw() >= 1048576L || var3.mcmetaByPath.containsKey(var4.get(var8 + ".mcmeta"))) {
            String var9 = (String)var1.apply((PackEntry)var0.get(var7.getKey()), (String)var7.getKey());
            if (var9 != null) {
               var11.put((String)var7.getKey(), var9);
            }
         }
      }

      TreeMap var12 = new TreeMap();
      var4.forEach((var4x, var5x) -> {
         if (var11.containsKey(var5x)) {
            var12.put(var4x, (String)var11.get(var5x));
         }

         if (var4x.endsWith(".png.mcmeta")) {
            String var6 = (String)var1.apply((PackEntry)var0.get(var5x), var5x);
            if (var6 != null) {
               var12.put(var4x, var6);
            }
         }
      });
      return new PackLoadResult(List.copyOf(var3.diagnostics), Map.copyOf(var12));
   }

   static PackLoadResult validate(Map var0, BiFunction var1) {
      return validatePixelView(var0, var1, null);
   }

   static PackLoadResult validatePixelView(Map var0, BiFunction var1, SolidifiedFrameCache var2) {
      ResourcePackLoader var3 = new ResourcePackLoader(var0, var1, List.of());
      var3.frameCache = var2;
      var3.normalizeTextureMetadata();

      try {
         JsonObject var4 = requireObject(parseJsonEntry((PackEntry)var0.get("pack.mcmeta")), "pack.mcmeta");
         if (var4.has("overlays")) {
            throw new IOException("Pixel optimization requires a standalone per-version pack");
         }

         JsonObject var5 = requireObject(var4.get("pack"), "pack");
         if (parseFormatRangeBits(var5.get("min_format"), false) != 377957122048L || parseFormatRangeBits(var5.get("max_format"), true) != 377957122048L) {
            throw new IOException("Pixel proof covers resource format 88.0 only");
         }

         Map var6 = var3.collectOverlayPaths();
         List var7 = var3.collectAtlasFolders(var6);
         if (var3.pixelOptimizable) {
            var3.optimizeItemTextures(var6, var7);
         }
      } catch (IOException | RuntimeException var8) {
         var3.addFinding(PackFindingSeverity.WARNING, "PIXEL_VIEW", "pack.mcmeta", "Pixel optimization skipped: " + var8.getMessage());
      }

      return new PackLoadResult(List.copyOf(var3.diagnostics));
   }

   static PackLoadResult optimizePackTextures(Path var0, Path var1, List var2) {
      return optimizePackTexturesWithCache(var0, var1, var2, new SolidifiedFrameCache(var1.toAbsolutePath().getParent().resolve("texture-cache")));
   }

   static PackLoadResult optimizePackTexturesWithCache(Path var0, Path var1, List var2, SolidifiedFrameCache var3) {
      if (!var2.isEmpty() && !var2.stream().anyMatch(var0x -> var0x.major() != 88 || var0x.minor() != 0)) {
         HashMap var4 = new HashMap();
         Path var5 = var0.resolveSibling("pack-texture-sources.json");
         if (Files.isRegularFile(var5)) {
            try (BufferedReader var6 = Files.newBufferedReader(var5)) {
               JsonParser.parseReader(var6)
                  .getAsJsonObject()
                  .entrySet()
                  .forEach(var1x -> var4.put((String)var1x.getKey(), ((JsonElement)var1x.getValue()).getAsString()));
            }
         }

         Path var37 = Files.createTempFile(var1.toAbsolutePath().getParent(), ".texture_pack_", ".tmp");

         try {
            ZipFile var8 = new ZipFile(var1.toFile());

            ArrayList var7;
            try {
               TreeMap var9 = new TreeMap();
               var8.stream().forEach(var2x -> var9.put(var2x.getName(), PackEntry.stream(var2x.getName(), () -> var8.getInputStream(var2x))));
               PackLoadResult var10 = validatePixelView(var9, (var2x, var3x) -> var4.getOrDefault(var3x, var0 + "!/" + var3x), var3);
               var7 = new ArrayList(var10.diagnostics);

               try (ZipOutputStream var11 = new ZipOutputStream(Files.newOutputStream(var37))) {
                  var11.setLevel(1);

                  for (Entry var13 : var9.entrySet()) {
                     if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedIOException("Texture optimization interrupted");
                     }

                     ZipEntry var14 = new ZipEntry((String)var13.getKey());
                     var14.setTimeLocal(LocalDateTime.of(1980, 1, 1, 0, 0));
                     var11.putNextEntry(var14);

                     try (InputStream var15 = ((PackEntry)var13.getValue()).openStream()) {
                        var15.transferTo(var11);
                     }

                     var11.closeEntry();
                  }
               }
            } catch (Throwable var35) {
               try {
                  var8.close();
               } catch (Throwable var28) {
                  var35.addSuppressed(var28);
               }

               throw var35;
            }

            var8.close();
            long var38 = Files.size(var1);
            long var39 = Files.size(var37);
            if (var7.stream().anyMatch(var0x -> var0x.code.equals("ITEM_PIXELS_26_2"))) {
               var7.add(
                  new PackFinding(
                     PackFindingSeverity.INFO,
                     "PER_VERSION_SIZE",
                     var1.toString(),
                     var0.toString(),
                     "26.2 pack bytes "
                        + var38
                        + " -> "
                        + var39
                        + "; pixel preprocessing trades download/cache size for less client reload work. Universal unchanged."
                  )
               );
            }

            Files.move(var37, var1, StandardCopyOption.REPLACE_EXISTING);
            return new PackLoadResult(List.copyOf(var7));
         } finally {
            Files.deleteIfExists(var37);
         }
      } else {
         return new PackLoadResult(List.of());
      }
   }

   private void addFinding(PackFindingSeverity var1, String var2, String var3, String var4) {
      PackEntry var5 = (PackEntry)this.sourceEntries.get(var3);
      String var6 = var5 == null ? null : (String)this.sourceResolver.apply(var5, var3);
      if (var6 == null && var3.contains("/assets/")) {
         String var7 = var3.substring(var3.indexOf("/assets/") + 1);
         if (this.sourceEntries.containsKey(var7)) {
            var6 = (String)this.sourceResolver.apply((PackEntry)this.sourceEntries.get(var7), var7);
         }
      }

      this.diagnostics
         .add(
            new PackFinding(var1, var2, var3, var6 == null ? "generated/event resource (source unavailable)" : var6.replace('\n', ' ').replace('\r', ' '), var4)
         );
   }

   private void normalizeTextureMetadata() {
      for (String var2 : new TreeSet(this.sourceEntries.keySet())) {
         if (var2.endsWith(".png.mcmeta") && !((PackEntry)this.sourceEntries.get(var2)).isRemoved()) {
            try {
               JsonObject var3 = requireObject(parseJsonEntry((PackEntry)this.sourceEntries.get(var2)), "metadata");
               ArrayList var4 = new ArrayList();

               for (String var6 : List.of("animation", "texture")) {
                  if (var3.has(var6)) {
                     JsonObject var7 = requireObject(var3.get(var6), var6);

                     for (String var9 : var6.equals("animation") ? List.of("interpolate") : List.of("blur", "clamp")) {
                        JsonElement var10 = var7.get(var9);
                        if (var10 != null
                           && var10.isJsonPrimitive()
                           && var10.getAsJsonPrimitive().isString()
                           && Set.of("true", "false").contains(var10.getAsString())) {
                           var7.addProperty(var9, Boolean.parseBoolean(var10.getAsString()));
                           var4.add(var6 + "." + var9);
                        }
                     }
                  }
               }

               String var12 = var2.substring(0, var2.length() - ".mcmeta".length());
               if (this.sourceEntries.containsKey(var12) && !((PackEntry)this.sourceEntries.get(var12)).isRemoved()) {
                  PackStatsTuple var13 = this.readPngStats(var12);
                  validateTextureMetadata(var3, var13);
                  this.mcmetaByPath.put(var2, var3);
                  if (!var4.isEmpty()) {
                     ((PackEntry)this.sourceEntries.get(var2)).setText(var3.toString());
                     this.addFinding(
                        PackFindingSeverity.SAFE_FIX,
                        "METADATA_BOOLEAN",
                        var2,
                        "Converted unambiguous string boolean(s): "
                           + String.join(", ", var4)
                           + ". Original source unchanged; invalid sprites may now render correctly."
                     );
                  }

                  if (var3.has("animation") && var13.Xw() >= 1048576L) {
                     PackCountStats var14 = parseAnimation(var3.getAsJsonObject("animation"), var13);
                     this.addFinding(
                        PackFindingSeverity.INFO,
                        "LARGE_ANIMATION",
                        var2,
                        var13.bCC
                           + "x"
                           + var13.bCD
                           + " PNG, "
                           + var14.bCp
                           + "x"
                           + var14.bCq
                           + " frames, "
                           + var14.bCt.size()
                           + " timeline entries; "
                           + var13.Xw()
                           + " pixels processed per sprite. Raw frame dedup/padding changes require post-processing equivalence; no resize."
                     );
                  }
               } else {
                  this.addFinding(
                     PackFindingSeverity.INFO,
                     "INHERITED_TEXTURE_METADATA",
                     var2,
                     "Metadata inherits its PNG from another layer; no automatic repair without matching image geometry."
                  );
                  this.skippedMetadata.add(var2);
               }
            } catch (IOException | RuntimeException var11) {
               this.skippedMetadata.add(var2);
               this.addFinding(
                  PackFindingSeverity.WARNING,
                  "INVALID_METADATA",
                  var2,
                  "Not changed: " + var11.getMessage() + ". Use JSON booleans, positive integer sizes/times and valid frame indices."
               );
            }
         }
      }
   }

   static void validateTextureMetadata(JsonObject var0, PackStatsTuple var1) {
      if (var0.has("animation")) {
         parseAnimation(requireObject(var0.get("animation"), "animation"), var1);
      }

      if (var0.has("texture")) {
         JsonObject var2 = requireObject(var0.get("texture"), "texture");
         readBoolean(var2, "blur", false);
         readBoolean(var2, "clamp", false);
         if (var2.has("mipmap_strategy")
            && (
               !var2.get("mipmap_strategy").isJsonPrimitive()
                  || !var2.getAsJsonPrimitive("mipmap_strategy").isString()
                  || !Set.of("auto", "mean", "cutout", "strict_cutout", "dark_cutout").contains(var2.get("mipmap_strategy").getAsString())
            )) {
            throw new IllegalArgumentException("Unknown texture.mipmap_strategy");
         }

         if (var2.has("alpha_cutoff_bias")
            && (
               !var2.get("alpha_cutoff_bias").isJsonPrimitive()
                  || !var2.getAsJsonPrimitive("alpha_cutoff_bias").isNumber()
                  || !Float.isFinite(var2.get("alpha_cutoff_bias").getAsFloat())
            )) {
            throw new IllegalArgumentException("texture.alpha_cutoff_bias must be finite numeric data");
         }
      }
   }

   static PackCountStats parseAnimation(JsonObject var0, PackStatsTuple var1) {
      int var2 = var0.has("width") ? readIntAtLeast(var0.get("width"), 1) : (var0.has("height") ? var1.bCC : Math.min(var1.bCC, var1.bCD));
      int var3 = var0.has("height") ? readIntAtLeast(var0.get("height"), 1) : (var0.has("width") ? var1.bCD : Math.min(var1.bCC, var1.bCD));
      if (var1.bCC % var2 == 0 && var1.bCD % var3 == 0) {
         long var4 = (long)(var1.bCC / var2) * (var1.bCD / var3);
         if (var4 > 65536L) {
            throw new IllegalArgumentException("More than 65536 frames; validation budget exceeded");
         }

         int var6 = var0.has("frametime") ? readIntAtLeast(var0.get("frametime"), 1) : 1;
         boolean var7 = readBoolean(var0, "interpolate", false);
         ArrayList var8 = new ArrayList();
         ArrayList var9 = new ArrayList();
         if (var0.has("frames")) {
            if (!var0.get("frames").isJsonArray() || var0.getAsJsonArray("frames").size() > 65536) {
               throw new IllegalArgumentException("animation.frames must be an array of at most 65536 entries");
            }

            for (JsonElement var11 : var0.getAsJsonArray("frames")) {
               int var13 = var6;
               int var12;
               if (var11.isJsonObject()) {
                  JsonObject var14 = var11.getAsJsonObject();
                  var12 = readIntAtLeast(var14.get("index"), 0);
                  if (var14.has("time")) {
                     var13 = readIntAtLeast(var14.get("time"), 1);
                  }
               } else {
                  var12 = readIntAtLeast(var11, 0);
               }

               if (var12 >= var4) {
                  throw new IllegalArgumentException("Animation frame " + var12 + " outside PNG frame count " + var4);
               }

               var8.add(var12);
               var9.add(var13);
            }
         } else {
            for (int var15 = 0; var15 < var4; var15++) {
               var8.add(var15);
               var9.add(var6);
            }
         }

         return new PackCountStats(var2, var3, var6, var7, List.copyOf(var8), List.copyOf(var9));
      } else {
         throw new IllegalArgumentException("PNG dimensions are not multiples of animation frame dimensions");
      }
   }

   static boolean readBoolean(JsonObject var0, String var1, boolean var2) {
      if (!var0.has(var1)) {
         return var2;
      } else {
         JsonElement var3 = var0.get(var1);
         if (var3.isJsonPrimitive() && var3.getAsJsonPrimitive().isBoolean()) {
            return var3.getAsBoolean();
         } else {
            throw new IllegalArgumentException(var1 + " must be a JSON boolean");
         }
      }
   }

   static int readIntAtLeast(JsonElement var0, int var1) {
      if (var0 != null && var0.isJsonPrimitive() && var0.getAsJsonPrimitive().isNumber()) {
         int var2;
         try {
            var2 = var0.getAsBigDecimal().intValueExact();
         } catch (ArithmeticException var4) {
            throw new IllegalArgumentException("Expected exact integer, found " + var0);
         }

         if (var2 < var1) {
            throw new IllegalArgumentException("Expected integer >= " + var1);
         } else {
            return var2;
         }
      } else {
         throw new IllegalArgumentException("Expected integer >= " + var1);
      }
   }

   private PackStatsTuple readPngStats(String var1) {
      PackStatsTuple var2 = (PackStatsTuple)this.pngStats.get(var1);
      if (var2 != null) {
         return var2;
      }

      PackEntry var3 = (PackEntry)this.sourceEntries.get(var1);
      if (var3 != null && !var3.isRemoved()) {
         try (InputStream var4 = var3.openStream()) {
            byte[] var5 = var4.readNBytes(33);
            if (var5.length != 33 || ByteBuffer.wrap(var5).getLong() != -8552249625308161526L || ByteBuffer.wrap(var5, 12, 4).getInt() != 1229472850) {
               throw new IOException("Invalid PNG header " + var1);
            }

            PackStatsTuple var6 = new PackStatsTuple(
               ByteBuffer.wrap(var5, 16, 4).getInt(), ByteBuffer.wrap(var5, 20, 4).getInt(), var5[24] & 255, var5[25] & 255
            );
            if (var6.bCC <= 0 || var6.bCD <= 0) {
               throw new IOException("Invalid PNG dimensions");
            }

            this.pngStats.put(var1, var6);
            return var6;
         }
      } else {
         throw new IOException("Missing PNG " + var1);
      }
   }

   static JsonObject requireObject(JsonElement var0, String var1) {
      if (var0 != null && var0.isJsonObject()) {
         return var0.getAsJsonObject();
      } else {
         throw new IllegalArgumentException(var1 + " must be an object");
      }
   }

   static JsonElement parseJsonEntry(PackEntry var0) {
      JsonElement var5;
      try (InputStream var1 = var0.openStream()) {
         byte[] var2 = var1.readNBytes(1048577);
         if (var2.length > 1048576) {
            throw new IOException("JSON exceeds 1 MiB validation budget");
         }

         JsonReader var3 = new JsonReader(new StringReader(StandardCharsets.UTF_8.newDecoder().decode(ByteBuffer.wrap(var2)).toString()));

         try {
            var3.setLenient(false);
            JsonElement var4 = readStrictJson(var3, 0);
            if (var3.peek() != JsonToken.END_DOCUMENT) {
               throw new IOException("Trailing JSON data");
            }

            var5 = var4;
         } catch (Throwable var8) {
            try {
               var3.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }

            throw var8;
         }

         var3.close();
      }

      return var5;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static JsonElement readStrictJson(JsonReader var0, int var1) {
      if (var1 > 32) {
         throw new IOException("JSON nesting exceeds validation budget");
      }

      switch (SwitchMap76.bCo[var0.peek().ordinal()]) {
         case 1:
            JsonObject var4 = new JsonObject();
            var0.beginObject();

            while (var0.hasNext()) {
               String var3 = var0.nextName();
               if (var4.has(var3)) {
                  throw new IOException("Ambiguous duplicate JSON key: " + var3);
               }

               var4.add(var3, readStrictJson(var0, var1 + 1));
            }

            var0.endObject();
            return var4;
         case 2:
            JsonArray var2 = new JsonArray();
            var0.beginArray();

            while (var0.hasNext()) {
               var2.add(readStrictJson(var0, var1 + 1));
            }

            var0.endArray();
            return var2;
         case 3:
            return new JsonPrimitive(var0.nextString());
         case 4:
            return new JsonPrimitive(new BigDecimal(var0.nextString()));
         case 5:
            return new JsonPrimitive(var0.nextBoolean());
         case 6:
            var0.nextNull();
            return JsonNull.INSTANCE;
         default:
            throw new IOException("Invalid JSON value");
      }
   }

   private Map collectOverlayPaths() {
      ArrayList var1 = new ArrayList<>(List.of(""));
      PackEntry var2 = (PackEntry)this.sourceEntries.get("pack.mcmeta");
      if (var2 != null && !var2.isRemoved()) {
         JsonObject var3 = requireObject(parseJsonEntry(var2), "pack.mcmeta");
         if (var3.has("overlays")) {
            for (JsonElement var5 : var3.getAsJsonObject("overlays").getAsJsonArray("entries")) {
               JsonObject var6 = var5.getAsJsonObject();
               String var7 = var6.get("directory").getAsString();
               if (!var7.matches("[a-zA-Z0-9_.-]+") || var7.equals(".") || var7.equals("..")) {
                  throw new IOException("Unsupported overlay directory");
               }

               long var8 = 377957122048L;
               long var10;
               long var12;
               if (!var6.has("min_format") && !var6.has("max_format")) {
                  JsonElement var14 = var6.get("formats");
                  if (var14 == null) {
                     throw new IOException("Overlay has no format range");
                  }

                  if (var14.isJsonObject()) {
                     var10 = parseFormatRangeBits(var14.getAsJsonObject().get("min_inclusive"), false);
                     var12 = parseFormatRangeBits(var14.getAsJsonObject().get("max_inclusive"), true);
                  } else if (var14.isJsonArray()) {
                     if (var14.getAsJsonArray().size() != 2) {
                        throw new IOException("Invalid legacy format range");
                     }

                     var10 = parseFormatRangeBits(var14.getAsJsonArray().get(0), false);
                     var12 = parseFormatRangeBits(var14.getAsJsonArray().get(1), true);
                  } else {
                     var10 = parseFormatRangeBits(var14, false);
                     var12 = parseFormatRangeBits(var14, true);
                  }
               } else {
                  var10 = var6.has("min_format") ? parseFormatRangeBits(var6.get("min_format"), false) : 0L;
                  var12 = var6.has("max_format") ? parseFormatRangeBits(var6.get("max_format"), true) : Long.MAX_VALUE;
               }

               if (var10 > var12) {
                  throw new IOException("Reversed overlay range");
               }

               if (var10 <= var8 && var8 <= var12) {
                  var1.add(var7 + "/");
               }
            }
         }
      }

      TreeMap var15 = new TreeMap();

      for (String var17 : var1) {
         for (String var19 : this.sourceEntries.keySet()) {
            if (!((PackEntry)this.sourceEntries.get(var19)).isRemoved() && var19.startsWith(var17 + "assets/")) {
               var15.put(var19.substring(var17.length()), var19);
            }
         }
      }

      return var15;
   }

   private static long parseFormatRangeBits(JsonElement var0, boolean var1) {
      if (var0.isJsonArray()) {
         JsonArray var2 = var0.getAsJsonArray();
         if (var2.size() >= 1 && var2.size() <= 2) {
            return (long)readIntAtLeast(var2.get(0), 0) << 32 | (var2.size() == 2 ? readIntAtLeast(var2.get(1), 0) : (var1 ? Integer.MAX_VALUE : 0));
         } else {
            throw new IllegalArgumentException("Invalid format tuple");
         }
      } else {
         return (long)readIntAtLeast(var0, 0) << 32 | (var1 ? Integer.MAX_VALUE : 0);
      }
   }

   private List collectAtlasFolders(Map var1) {
      ArrayList var2 = new ArrayList();

      for (String var4 : var1.keySet()) {
         if (var4.contains("/atlases/") && var4.endsWith(".json")) {
            TreeMap var5 = new TreeMap();
            if (var4.equals("assets/minecraft/atlases/blocks.json")) {
               addDirectorySprites(var1, var5, "block", "block/");
               addDirectorySprites(var1, var5, "entity/conduit", "entity/conduit/");
            }

            if (var4.equals("assets/minecraft/atlases/items.json")) {
               addDirectorySprites(var1, var5, "item", "item/");
            }

            JsonObject var6 = requireObject(parseJsonEntry((PackEntry)this.sourceEntries.get(var1.get(var4))), "atlas");

            for (JsonElement var8 : var6.getAsJsonArray("sources")) {
               JsonObject var9 = var8.getAsJsonObject();
               String var10 = var9.get("type").getAsString().replaceFirst("^minecraft:", "");
               switch (var10) {
                  case "single":
                     String var16 = ensureNamespace(var9.get("resource").getAsString());
                     String var17 = var9.has("sprite") ? ensureNamespace(var9.get("sprite").getAsString()) : var16;
                     String var18 = "assets/" + var16.replace(":", "/textures/") + ".png";
                     if (var1.containsKey(var18)) {
                        var5.put(var17, new PackRef(var17, var18, null));
                     }
                     break;
                  case "directory":
                     addDirectorySprites(var1, var5, var9.get("source").getAsString(), var9.get("prefix").getAsString());
                     break;
                  case "filter":
                     JsonObject var13 = var9.getAsJsonObject("pattern");
                     Pattern var14 = Pattern.compile(var13.has("namespace") ? var13.get("namespace").getAsString() : "");
                     Pattern var15 = Pattern.compile(var13.has("path") ? var13.get("path").getAsString() : "");
                     var5.entrySet()
                        .removeIf(
                           var2x -> var14.matcher(((String)var2x.getKey()).split(":", 2)[0]).find()
                              && var15.matcher(((String)var2x.getKey()).split(":", 2)[1]).find()
                        );
                     break;
                  default:
                     this.pixelOptimizable = false;
                     this.addFinding(
                        PackFindingSeverity.WARNING,
                        "ATLAS_PARTIAL",
                        (String)var1.get(var4),
                        "26.2 reference: " + var10 + " source not expanded; pixel transforms disabled because all consumers cannot be proved."
                     );
               }
            }

            var2.add(new PackFolderMap((String)var1.get(var4), var5));
         }
      }

      return var2;
   }

   private static String ensureNamespace(String var0) {
      return var0.contains(":") ? var0 : "minecraft:" + var0;
   }

   private static void addDirectorySprites(Map var0, Map var1, String var2, String var3) {
      for (String var5 : var0.keySet()) {
         String[] var6 = var5.split("/", 3);
         String var7 = "textures/" + var2 + "/";
         if (var6.length == 3 && var6[2].startsWith(var7) && var5.endsWith(".png")) {
            String var8 = var6[1] + ":" + var3 + var6[2].substring(var7.length(), var6[2].length() - 4);
            var1.put(var8, new PackRef(var8, var5, var2));
         }
      }
   }

   private void reportAtlasWarnings(Map var1, List var2) {
      for (PackFolderMap var4 : var2) {
         ArrayList var5 = new ArrayList();
         int var6 = 4;
         TreeMap var7 = new TreeMap();

         for (PackRef var9 : var4.bCw.values()) {
            String var10 = (String)var1.get(var9.texture);
            String var11 = (String)var1.get(var9.texture + ".mcmeta");
            if (!this.skippedMetadata.contains(var11)) {
               PackStatsTuple var12;
               try {
                  var12 = this.readPngStats(var10);
               } catch (IOException | RuntimeException var18) {
                  this.addFinding(
                     PackFindingSeverity.WARNING,
                     "INVALID_ATLAS_TEXTURE",
                     var10,
                     "Cannot inspect sprite " + var9.id + ": " + var18.getMessage() + ". Re-export a valid PNG."
                  );
                  continue;
               }

               JsonObject var13 = (JsonObject)this.mcmetaByPath.get(var11);
               int var14 = var12.bCC;
               int var15 = var12.bCD;
               if (var13 != null && var13.has("animation")) {
                  PackCountStats var16 = parseAnimation(var13.getAsJsonObject("animation"), var12);
                  var14 = var16.bCp;
                  var15 = var16.bCq;
               }

               if (var9.directory != null) {
                  var7.computeIfAbsent(var9.directory, var0 -> new ArrayList<>()).add(var9);
               }

               int var26 = Math.min(Integer.lowestOneBit(var14), Integer.lowestOneBit(var15));
               if (var26 < 16 && var4.bCv.endsWith("/blocks.json")) {
                  var6 = Math.min(var6, Integer.numberOfTrailingZeros(var26));
                  String var17 = (String)this.sourceResolver.apply((PackEntry)this.sourceEntries.get(var10), var10);
                  var5.add(var9.id + " " + var14 + "x" + var15 + ", factor=" + var26 + ", " + (var17 == null ? var10 : var17));
               }
            }
         }

         if (!var5.isEmpty()) {
            this.addFinding(
               PackFindingSeverity.WARNING,
               "MIPMAP_LIMIT",
               var4.bCv,
               "26.2 reference block atlas: requested 4 -> at most "
                  + var6
                  + "; "
                  + var5.size()
                  + " known sprites limit mipmap. Vanilla/other packs can only lower this bound. No resize. Export frame dimensions divisible by 16 or review atlas membership. Sprites: "
                  + String.join("; ", var5)
            );
         }

         for (Entry var20 : var7.entrySet()) {
            List var21 = this.findAtlasFilesFor((String)var20.getKey());
            if (!((String)var20.getKey()).equals("item") || !var4.bCv.endsWith("/items.json") || !var21.isEmpty()) {
               List var22 = ((List)var20.getValue())
                  .stream()
                  .filter(var1x -> var1x.texture.matches(".*/(?:gui|font|fonts|icons?)/.*") || ((String)var20.getKey()).equals("custom"))
                  .toList();
               if (!var22.isEmpty()) {
                  long var23 = 0L;

                  for (PackRef var25 : (List)var20.getValue()) {
                     var23 += this.readPngStats((String)var1.get(var25.texture)).Xw();
                  }

                  this.addFinding(
                     PackFindingSeverity.WARNING,
                     "BROAD_ATLAS_DIRECTORY",
                     var4.bCv,
                     "directory='"
                        + (String)var20.getKey()
                        + "' imports "
                        + ((List)var20.getValue()).size()
                        + " known sprites across namespaces, "
                        + var23
                        + " PNG pixels, "
                        + var23 * 4L
                        + " decoded RGBA bytes per reload before aliases; potential decode/solidify work, not a measured CPU estimate. Source atlas files: "
                        + var21
                        + ". Review GUI/font membership and replace broad directory with explicit single resource/sprite rules only after checking consumers. No automatic filtering: IDs/overrides must remain valid. Examples: "
                        + var22.stream().limit(5L).map(var1x -> var1x.id + " [" + (String)var1.get(var1x.texture) + "]").toList()
                  );
               }
            }
         }
      }
   }

   private List findAtlasFilesFor(String var1) {
      ArrayList var2 = new ArrayList();

      for (Path var4 : this.atlasSearchDirs) {
         if (Files.isDirectory(var4)) {
            try (Stream var5 = Files.list(var4)) {
               for (Path var7 : var5.filter(var0 -> var0.toString().endsWith(".json")).sorted().toList()) {
                  try {
                     JsonObject var8 = requireObject(parseJsonEntry(PackEntry.stream(var7.toString(), () -> Files.newInputStream(var7))), "atlas");

                     for (JsonElement var10 : var8.getAsJsonArray("sources")) {
                        if (var10.isJsonObject() && var10.getAsJsonObject().has("source") && var1.equals(var10.getAsJsonObject().get("source").getAsString())) {
                           var2.add(var7.toString());
                           break;
                        }
                     }
                  } catch (IOException | RuntimeException var12) {
                  }
               }
            }
         }
      }

      return var2;
   }

   private void optimizeItemTextures(Map var1, List var2) {
      String var3 = "assets/minecraft/atlases/items.json";
      if (Objects.equals(var1.get(var3), var3)) {
         TreeMap var4 = new TreeMap();
         HashSet var5 = new HashSet();

         for (PackFolderMap var7 : var2) {
            for (PackRef var9 : var7.bCw.values()) {
               var4.computeIfAbsent(var9.texture, var0 -> new ArrayList<>()).add(var7);
               if (!var7.bCv.equals(var3) || var9.id.split(":", 2)[1].startsWith("item/")) {
                  var5.add(var9.texture);
               }

               if (var9.texture.startsWith("assets/minecraft/")
                  || var9.texture.matches(".*/textures/(?:entity|gui|font|fonts|particle|painting|map|misc|environment|effect|mob_effect|trims)/.*")) {
                  var5.add(var9.texture);
               }
            }
         }

         HashMap var20 = new HashMap();

         for (String var24 : var4.keySet()) {
            String[] var27 = var24.split("/", 3);
            var20.put(var27[1] + ":" + var27[2].substring("textures/".length(), var27[2].length() - 4), var24);
         }

         for (String var25 : var1.keySet()) {
            if (var25.matches("assets/[^/]+/(?:models|items|font|atlases|equipment|particles|post_effect|shaders|waypoint_style)/.*\\.json")) {
               try {
                  collectTextureConsumers(parseJsonEntry((PackEntry)this.sourceEntries.get(var1.get(var25))), var25, "", null, var20, var1, var5);
               } catch (IOException | RuntimeException var17) {
                  this.addFinding(
                     PackFindingSeverity.WARNING,
                     "TEXTURE_CONSUMERS_UNKNOWN",
                     (String)var1.get(var25),
                     "Cannot prove texture consumers: " + var17.getMessage() + ". Pixel optimization skipped; repair this JSON first."
                  );
                  return;
               }
            }
         }

         for (String var26 : var4.keySet()) {
            if (Thread.currentThread().isInterrupted()) {
               return;
            }

            String var28 = (String)var1.get(var26);
            String var10 = (String)var1.get(var26 + ".mcmeta");

            PackStatsTuple var11;
            try {
               var11 = this.readPngStats(var28);
            } catch (IOException | RuntimeException var18) {
               continue;
            }

            if (var5.contains(var26)) {
               if (var11.Xw() >= 1048576L) {
                  this.addFinding(
                     PackFindingSeverity.WARNING,
                     "TEXTURE_CONTEXT",
                     var28,
                     var11.Xw()
                        + " pixels: no preprocessing; a non-ITEMS, item/ bypass or direct image consumer exists. Keep raster/mipmap behavior; review oversized art with its author."
                  );
               }
            } else if (this.sourceEntries.containsKey(var26)
               && var28.equals(var26)
               && (var10 == null || var10.equals(var26 + ".mcmeta"))
               && !this.skippedMetadata.contains(var10)) {
               JsonObject var12 = var10 == null ? new JsonObject() : (JsonObject)this.mcmetaByPath.get(var10);
               if (var12 != null) {
                  try {
                     byte[] var13 = ((PackEntry)this.sourceEntries.get(var28)).getBytes();
                     CachedAtlasEntry var14 = this.frameCache != null && (var12.has("animation") || var11.Xw() >= 1048576L)
                        ? this.frameCache.getOrSolidify(var13, var10 == null ? null : ((PackEntry)this.sourceEntries.get(var10)).getBytes(), var12, var11)
                        : AtlasImageEncoder.optimizePng(var13, var12, var11);
                     if (var14 == null) {
                        if (var11.Xw() >= 1048576L) {
                           this.addFinding(
                              PackFindingSeverity.INFO,
                              "TEXTURE_UNCHANGED",
                              var28,
                              "No safe material transform for this PNG/metadata format, or preprocessing already applied. No resize/strategy override."
                           );
                        }
                     } else {
                        this.addFinding(
                           PackFindingSeverity.SAFE_FIX,
                           "ITEM_PIXELS_26_2",
                           var28,
                           "26.2 ITEMS mip0 only: precomputed="
                              + var14.solidified()
                              + ", physical frames "
                              + var14.originalFrames()
                              + " -> "
                              + var14.uniqueFrames()
                              + ", PNG pixels "
                              + var14.originalPixels()
                              + " -> "
                              + var14.outputPixels()
                              + "; exact processed RGBA/timeline preserved, strategy=mean in per-version 26.2 only. Universal/source unchanged; "
                              + ((List)var4.get(var26)).size()
                              + " known sprite consumer(s). Separate external metadata overrides require revalidation."
                        );
                        PackEntry var15 = PackEntry.bytes(var26, var14.png());
                        PackEntry var16 = PackEntry.bytes(var26 + ".mcmeta", var14.metadata().toString().getBytes(StandardCharsets.UTF_8));
                        this.sourceEntries.put(var26, var15);
                        this.sourceEntries.put(var26 + ".mcmeta", var16);
                     }
                  } catch (IOException | RuntimeException var19) {
                     this.addFinding(
                        PackFindingSeverity.WARNING,
                        "TEXTURE_OPTIMIZATION_SKIPPED",
                        var28,
                        "Output unchanged: " + var19.getMessage() + ". Re-export supported 8-bit RGB/RGBA or indexed PNG; do not resize blindly."
                     );
                  }
               }
            }
         }
      }
   }

   private static void collectTextureConsumers(JsonElement var0, String var1, String var2, JsonObject var3, Map var4, Map var5, Set var6) {
      if (var0.isJsonObject()) {
         JsonObject var7 = var0.getAsJsonObject();

         for (Entry var9 : var7.entrySet()) {
            collectTextureConsumers(
               (JsonElement)var9.getValue(),
               var1,
               var2.equals("textures") && var1.contains("/models/") ? "textures" : (String)var9.getKey(),
               var7,
               var4,
               var5,
               var6
            );
         }
      } else if (var0.isJsonArray()) {
         for (JsonElement var12 : var0.getAsJsonArray()) {
            collectTextureConsumers(var12, var1, var2, var3, var4, var5, var6);
         }
      } else if (var0.isJsonPrimitive() && var0.getAsJsonPrimitive().isString()) {
         String var11 = var0.getAsString();
         String var13 = ensureNamespace(var11.endsWith(".png") ? var11.substring(0, var11.length() - 4) : var11);
         String var14 = (String)var4.get(var13);
         if (var14 == null) {
            return;
         }

         if (var1.contains("/models/") && var2.equals("textures") && !var11.endsWith(".png")) {
            return;
         }

         if (var1.contains("/atlases/")
            && var2.equals("resource")
            && var3 != null
            && var3.has("type")
            && Set.of("single", "minecraft:single").contains(var3.get("type").getAsString())) {
            return;
         }

         if ((
               var1.contains("/models/") && var2.equals("parent")
                  || var1.contains("/items/")
                     && var2.equals("model")
                     && var3 != null
                     && var3.has("type")
                     && Set.of("model", "minecraft:model").contains(var3.get("type").getAsString())
            )
            && var5.containsKey("assets/" + ensureNamespace(var11).replace(":", "/models/") + ".json")) {
            return;
         }

         var6.add(var14);
      }
   }
}
