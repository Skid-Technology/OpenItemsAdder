package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent;
import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.PackEntry;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.BinaryPackCodec;
import dev.lone.itemsadder.util.BinaryPackResult;
import dev.lone.itemsadder.util.BossBarManager;
import dev.lone.itemsadder.util.DoubleCallback;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.ImageOptimizeStatus;
import dev.lone.itemsadder.util.JsonHelper;
import dev.lone.itemsadder.util.ListCollector;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.StreamWriter;
import dev.lone.itemsadder.util.Utils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.runtime.SwitchBootstraps;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

public class ResourcePackWriter {
   public static final boolean DEBUG = false;
   protected final String packName;
   private final PackBuildContext buildContext;
   public Set ignoredExtensions;
   public List ignoredSuffixes;
   public Object2ObjectOpenHashMap fileSources;
   public Multimap stringEntries;
   public Multimap streamEntries;
   private final Map fileByDestination = new HashMap();
   private final ResourcePackBuilder itemBuilder = new ResourcePackBuilder();
   private final Map writers = new LinkedHashMap();
   private final Map sourceFiles = new HashMap();
   private final Set modernModelDestinations = new HashSet();
   private final Set generatedStreams = Collections.newSetFromMap(new IdentityHashMap());
   private final Map entryInfo = new IdentityHashMap();

   private static String minecraftVersion() {
      return Bukkit.getBukkitVersion();
   }

   private ResourcePackGenerator Vx() {
      return this.buildContext == null ? ContentManager.method8084().aL.generator : this.buildContext.Vx();
   }

   private Path Vy() {
      return this.buildContext == null ? Main.getInstance().getDataFolder().toPath() : this.buildContext.Vy();
   }

   public ResourcePackWriter(String var1, Set var2) {
      this(var1, var2, null);
   }

   ResourcePackWriter(String var1, Set var2, PackBuildContext var3) {
      this.buildContext = var3;
      var2.add("iaentitymodel");
      var2.add("iaentityproject");
      var2.add("iaemotesproject");
      var2.add("iaemotes");
      var2.add("bbmodel");
      var2.add("metadata");
      var2.add("uuid");
      var2.add("vscode");
      var2.add("yml");
      var2.add("player_animations");
      var2.add("player_advanced_animations");
      var2.add("jspp");
      var2.add("js");
      var2.add("java");
      var2.add("kts");
      this.ignoredExtensions = var2;
      this.packName = var1;
      this.fileSources = new Object2ObjectOpenHashMap();
      this.stringEntries = ArrayListMultimap.create();
      this.streamEntries = ArrayListMultimap.create();
   }

   public void gu(String var1) {
      if (this.ignoredSuffixes == null) {
         this.ignoredSuffixes = new ArrayList();
      }

      this.ignoredSuffixes.add(Utils.normalizeSlashes(var1));
   }

   public boolean addFile(File var1, String var2, boolean var3) {
      return this.addFileInternal(var1, var2, var3, false);
   }

   public boolean addFileInternal(File var1, String var2, boolean var3, boolean var4) {
      JSONObject var5 = null;
      if (var1.getName().startsWith(".")) {
         return false;
      }

      if (var1.getName().endsWith(".json") && Utils.normalizeSlashes(var1.getAbsolutePath()).contains("minecraft/blockstates/")) {
         try {
            var5 = JsonHelper.readJsonFile(var1);
            List var6 = ListCollector.collectModelNames(var5);
            this.Vx().blockTextureModels.addAll(var6);
            Log.devLog("Found " + var6.size() + " models using block textures in blockstates JSON: " + Utils.shortPath(var1));
         } catch (IOException var13) {
            throw new RuntimeException(var13);
         }
      }

      if (this.fileSources.containsKey(var1)) {
         return false;
      }

      var2 = Utils.normalizeSlashes(var2);
      if (var3 && this.isRegistered(var2)) {
         Log.warn("Duplicate found. Already added as stream. " + Utils.shortPath(var1) + " -> " + Utils.normalizePluginPath(var2));
         return false;
      }

      File var15 = (File)this.fileByDestination.get(var2);
      if (var15 != null) {
         if (!var4) {
            Log.warn("Duplicate found. " + Utils.shortPath(var1) + " -> " + Utils.normalizePluginPath(var2));
            Log.warn("Previous file: " + Utils.shortPath(var15));
            return false;
         }

         this.fileSources.remove(var15);
         this.fileByDestination.remove(var2);
      }

      if (this.isIgnoredPath(var2) || this.ignoredExtensions.contains(FilenameUtils.getExtension(var2))) {
         return false;
      }

      if (PackAssetChecker.method6640(var1, var2)) {
         return false;
      }

      if (Settings.get().fixAtlasItemModels && var2.matches("assets/[^/]+/items/.*\\.json")) {
         try {
            JSONObject var7 = JsonHelper.readJsonFile(var1);
            String var8 = "ia_overlay_modern_atlas/" + var2;
            boolean var9 = this.registerWriter(var8, false, var2x -> {
               JSONObject var3x = this.Vx().getPlannedItemDefinition(var8);
               JSON.writeTo(var2x, var3x, new Feature[]{Feature.LargeObject});
            });
            if (var9) {
               this.sourceFiles.put(var8, var1);
               this.Vx().registerItemDefinition(var2, var7);
            }
         } catch (IOException var12) {
            Log.error("Failed to prepare modern item definition: " + Utils.shortPath(var1), var12);
         }
      }

      if (var2.matches("assets/[^/]+/models/.*\\.json")) {
         String var16 = PackManifest.normalizeModelId(var2);
         String var17 = var2;
         boolean var18 = this.registerWriter(var17, false, var4x -> {
            JSONObject var5x;
            if (Settings.get().fixAtlasItemModels) {
               var5x = this.Vx().getPlannedModel(var17);
            } else {
               var5x = JsonHelper.readJsonFile(var1);
               ResourcePackGenerator.registerModelJson(var16, var5x);
               this.Vx().updateModelTextures(var16, var5x);
            }

            JSON.writeTo(var4x, var5x, new Feature[]{Feature.LargeObject});
         });
         if (var18) {
            this.sourceFiles.put(var17, var1);
         }

         if (Settings.get().fixAtlasItemModels && var18) {
            this.modernModelDestinations.add(var17);

            try {
               ResourcePackGenerator var10 = this.Vx();
               if (!var10.putModelFile(var17, var1)) {
                  var10.putModelJson(var17, JsonHelper.readJsonFile(var1));
               }
            } catch (IOException var11) {
               throw new IllegalStateException("Failed to plan model before compression: '" + var17 + "'", var11);
            }
         }

         return true;
      } else {
         this.fileSources.put(var1, var2);
         this.fileByDestination.put(var2, var1);
         if (var5 != null) {
            this.Vx().registerBlockstate(var2, var5);
         }

         return true;
      }
   }

   public void addString(String var1, String var2) {
      this.addStringEntry(var1, var2, false);
   }

   public void addStringEntry(String var1, String var2, boolean var3) {
      if (var3 || !this.stringEntries.containsEntry(var1, var2)) {
         this.stringEntries.put(var1, var2);
      }
   }

   public void addText(String var1, String var2) {
      this.addStreamEntry(new ByteArrayInputStream(var1.getBytes(StandardCharsets.UTF_8)), var2, false);
   }

   public void addJsonOverride(JSONObject var1, String var2, boolean var3) {
      Objects.requireNonNull(var1, "json");
      boolean var4 = Settings.get().fixAtlasItemModels;
      if (!var4) {
         this.Vx().updateModelTextures(PackManifest.normalizeModelId(var2), var1);
      }

      boolean var5;
      if (var4) {
         var5 = this.registerWriter(var2, var3, var2x -> JSON.writeTo(var2x, this.Vx().getPlannedModel(var2), new Feature[]{Feature.LargeObject}));
      } else {
         var5 = this.registerWriter(var2, var3, var1x -> JSON.writeTo(var1x, var1, new Feature[]{Feature.LargeObject}));
      }

      if (var4 && var5) {
         this.modernModelDestinations.add(Utils.normalizeSlashes(var2));
         this.Vx().putModelJson(var2, var1);
      }
   }

   public void addJsonRaw(JSONObject var1, String var2, boolean var3) {
      Objects.requireNonNull(var1, "json");
      this.registerWriter(var2, var3, var1x -> JSON.writeTo(var1x, var1, new Feature[]{Feature.LargeObject}));
   }

   public void addSuppliedJson(Supplier var1, String var2) {
      this.registerWriter(var2, false, var2x -> JSON.writeTo(var2x, Objects.requireNonNull((JSONObject)var1.get(), var2), new Feature[]{Feature.LargeObject}));
   }

   private boolean registerWriter(String var1, boolean var2, StreamWriter var3) {
      Objects.requireNonNull(var1, "destination");
      Objects.requireNonNull(var3, "writer");
      if (!var2 && this.isRegistered(var1)) {
         return false;
      }

      this.sourceFiles.remove(var1);
      this.writers.put(var1, var3);
      return true;
   }

   boolean isWriterOwned(String var1) {
      String var2 = Utils.normalizeSlashes(var1);
      return this.modernModelDestinations.contains(var2)
            && !this.streamEntries.containsKey(var2)
            && !this.stringEntries.values().stream().map(Utils::normalizeSlashes).anyMatch(var2::equals)
            && !this.fileSources.values().stream().map(Utils::normalizeSlashes).anyMatch(var2::equals)
         ? this.writers.containsKey(var2)
         : false;
   }

   Set collectStaleModelPaths() {
      HashSet var1 = new HashSet(this.modernModelDestinations);
      var1.removeIf(var1x -> !this.writers.containsKey(var1x));
      var1.removeAll(this.streamEntries.keySet());
      this.stringEntries.values().forEach(var1x -> var1.remove(Utils.normalizeSlashes(var1x)));
      this.fileSources.values().forEach(var1x -> var1.remove(Utils.normalizeSlashes(var1x)));
      return var1;
   }

   Set collectAllDestinations() {
      HashSet var1 = new HashSet();
      this.streamEntries.keySet().stream().map(Utils::normalizeSlashes).forEach(var1::add);
      this.writers.keySet().stream().map(Utils::normalizeSlashes).forEach(var1::add);
      this.stringEntries.values().stream().map(Utils::normalizeSlashes).forEach(var1::add);
      this.fileSources.values().stream().map(Utils::normalizeSlashes).forEach(var1::add);
      return var1;
   }

   public void addStream(InputStream var1, String var2) {
      this.addStreamEntry(var1, var2, false);
   }

   public void addStreamEntry(InputStream var1, String var2, boolean var3) {
      if (var3 || !this.isRegistered(var2)) {
         this.streamEntries.put(var2, var1);
      }
   }

   void addBytesTracked(byte[] var1, String var2) {
      boolean var3 = this.isRegistered(var2);
      ByteArrayInputStream var4 = new ByteArrayInputStream(var1);
      this.addStreamEntry(var4, var2, false);
      if (!var3 && isGeneratedPath(Utils.normalizeSlashes(var2))) {
         this.generatedStreams.add(var4);
      }
   }

   public void addBytes(byte[] var1, String var2, boolean var3) {
      this.addStreamEntry(new ByteArrayInputStream(var1), var2, var3);
   }

   public void addModelJson(JSONObject var1, String var2) {
      ResourcePackGenerator.registerModelJson(PackManifest.normalizeModelId(var2), var1);
      this.addJsonDefault(var1, var2);
   }

   public void addJsonAsText(JSONObject var1, String var2) {
      this.addStringBytes(JsonHelper.toLargeJsonString(var1), var2, false);
   }

   public void addJsonUnchanged(JSONObject var1, String var2) {
      this.addJsonRaw(var1, var2, false);
   }

   public void addJsonDefault(JSONObject var1, String var2) {
      this.addJsonOverride(var1, var2, false);
   }

   public void addStringBytes(String var1, String var2, boolean var3) {
      this.addBytes(var1.getBytes(StandardCharsets.UTF_8), var2, var3);
   }

   public void addBuffer(ByteArrayOutputStream var1, String var2) {
      this.addBufferEntry(var1, var2, false);
   }

   public void addBufferEntry(ByteArrayOutputStream var1, String var2, boolean var3) {
      if (var3 || !this.isRegistered(var2)) {
         this.addBytes(var1.toByteArray(), var2, var3);
      }
   }

   private boolean isRegistered(String var1) {
      return this.streamEntries.containsKey(var1) || this.writers.containsKey(var1);
   }

   LinkedHashMap collectEntries() {
      LinkedHashMap var1 = new LinkedHashMap();
      this.entryInfo.clear();
      PackEntryGroups var2 = this.buildContext == null ? PackEntryGroups.VN() : this.buildContext.VL();
      ObjectIterator var3 = this.fileSources.entrySet().iterator();

      while (var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         File var5 = (File)var4.getKey();
         String var6 = Utils.normalizeSlashes((String)var4.getValue());
         this.addEntryWithSource(
            var1, PackEntry.stream(var6, () -> new FileInputStream(var5)), isGeneratedPath(var6) ? classifySource(var5, var2) : PackEntrySource.UNKNOWN, var5
         );
      }

      for (Entry var11 : this.stringEntries.entries()) {
         String var14 = (String)var11.getKey();
         String var17 = Utils.normalizeSlashes((String)var11.getValue());
         this.addEntry(var1, PackEntry.stream(var17, () -> {
            InputStream var2x = this.getClass().getResourceAsStream("/" + var14);
            if (var2x == null) {
               throw new FileNotFoundException("Bundled file is null: " + var14);
            } else {
               return var2x;
            }
         }), PackEntrySource.BUNDLED);
      }

      for (Entry var12 : this.streamEntries.entries()) {
         String var15 = Utils.normalizeSlashes((String)var12.getKey());
         this.addEntry(
            var1,
            streamEntry(var15, (InputStream)var12.getValue()),
            this.generatedStreams.contains(var12.getValue()) ? PackEntrySource.GENERATED : PackEntrySource.STREAM
         );
      }

      for (Entry var13 : this.writers.entrySet()) {
         String var16 = Utils.normalizeSlashes((String)var13.getKey());
         StreamWriter var18 = (StreamWriter)var13.getValue();
         File var7 = (File)this.sourceFiles.get(var13.getKey());
         this.addEntryWithSource(var1, PackEntry.writer(var16, var18::writeTo), var7 == null ? PackEntrySource.WRITER : classifySource(var7, var2), var7);
      }

      return var1;
   }

   private static PackEntry streamEntry(String var0, InputStream var1) {
      byte[][] var2 = new byte[1][];
      return PackEntry.stream(var0, () -> {
         if (var2[0] == null) {
            try (InputStream var2x = var1) {
               var2[0] = var2x.readAllBytes();
            }
         }

         return new ByteArrayInputStream(var2[0]);
      });
   }

   private static void putIfAbsentEntry(Map var0, PackEntry var1) {
      var0.putIfAbsent(Utils.normalizeSlashes(var1.getPath()), var1);
   }

   private void addEntry(Map var1, PackEntry var2, PackEntrySource var3) {
      this.addEntryWithSource(var1, var2, var3, null);
   }

   private void addEntryWithSource(Map var1, PackEntry var2, PackEntrySource var3, @Nullable File var4) {
      String var5 = Utils.normalizeSlashes(var2.getPath());
      var1.putIfAbsent(var5, var2);
      if (var1.get(var5) == var2 && (isGeneratedPath(var5) || var4 != null)) {
         this.entryInfo.put(var2, new PackFileEntry(var5, var3, var4));
      }
   }

   List deduplicateEntries(LinkedHashMap var1) {
      HashMap var2 = new HashMap();
      HashMap var3 = new HashMap();
      ArrayList var4 = new ArrayList();
      var1.forEach(
         (var4x, var5x) -> {
            String var6 = Utils.normalizeSlashes(var5x.getPath());
            PackEntryRecord var7 = new PackEntryRecord(var4x, var5x);
            if (var5x.isRemoved()) {
               var3.putIfAbsent(var6, var7);
            } else {
               PackEntryRecord var8 = var2.putIfAbsent(var6, var7);
               if (var8 != null) {
                  var4.add(
                     "Normalized pack path collision: selected "
                        + this.describeCollision(var8, var6)
                        + ", discarded "
                        + this.describeCollision(var7, var6)
                        + ", ZIP path '"
                        + truncatePath(var6)
                        + "'"
                  );
               }
            }
         }
      );
      LinkedHashMap var5 = new LinkedHashMap();
      var1.forEach((var3x, var4x) -> {
         String var5x = Utils.normalizeSlashes(var4x.getPath());
         PackEntryRecord var6 = (PackEntryRecord)var2.getOrDefault(var5x, (PackEntryRecord)var3.get(var5x));
         if (var6 != null && var6.VQ().equals(var3x) && var6.VR() == var4x) {
            var5.putIfAbsent(var5x, var4x);
         }
      });
      var1.clear();
      var1.putAll(var5);
      return List.copyOf(var4);
   }

   private String describeCollision(PackEntryRecord var1, String var2) {
      PackEntry var3 = var1.VR();
      PackEntrySource var4 = this.sourceOf(var3, var2);
      return "{map key='" + truncatePath(var1.VQ()) + "', entry path='" + truncatePath(var3.getPath()) + "'" + (var4 == null ? "" : ", origin=" + var4) + "}";
   }

   private static String truncatePath(String var0) {
      return var0.length() <= 256 ? var0 : var0.substring(0, 253) + "...";
   }

   PackEntrySource sourceOf(PackEntry var1, String var2) {
      PackFileEntry var3 = (PackFileEntry)this.entryInfo.get(var1);
      return var3 != null && var3.path().equals(Utils.normalizeSlashes(var2)) ? var3.VT() : null;
   }

   @Nullable
   String fileNameOf(PackEntry var1, String var2) {
      File var3 = this.fileOf(var1, var2);
      return var3 == null ? null : Utils.shortPath(var3);
   }

   @Nullable
   File fileOf(PackEntry var1, String var2) {
      PackFileEntry var3 = (PackFileEntry)this.entryInfo.get(var1);
      return var3 != null && var3.path().equals(Utils.normalizeSlashes(var2)) ? var3.VU() : null;
   }

   private static boolean isGeneratedPath(String var0) {
      return ResourcePackGenerator.isModelJsonPath(var0);
   }

   private static PackEntrySource classifySource(File var0, PackEntryGroups var1) {
      try {
         Path var2 = realPathOrAbsolute(var0.toPath());
         if (var1.VO().stream().anyMatch(var2::startsWith)) {
            return PackEntrySource.USER_CONTENT;
         } else {
            return var1.VP().stream().anyMatch(var2::startsWith) ? PackEntrySource.EXTERNAL_PACK : PackEntrySource.UNKNOWN;
         }
      } catch (RuntimeException var3) {
         return PackEntrySource.UNKNOWN;
      }
   }

   private static Path realPathOrAbsolute(Path var0) {
      try {
         return var0.toRealPath();
      } catch (IOException var2) {
         return var0.toAbsolutePath().normalize();
      }
   }

   static void obfuscatePngs(Map var0, PackManifestBuilder var1) {
      if (Settings.get().obfuscatePng) {
         HashSet var2 = new HashSet();

         for (PackEntry var4 : var0.values()) {
            String var5 = Utils.normalizeSlashes(var4.getPath());
            if (!var4.isRemoved() && var5.toLowerCase(Locale.ROOT).endsWith(".png") && var2.add(var5)) {
               try (InputStream var6 = var4.openStream()) {
                  byte[] var7 = readCapped(var6);
                  if (var7 == null) {
                     var1.addEntry(var5, ImageOptimizeStatus.UNSUPPORTED_FORMAT, "PNG exceeds size limit");
                  } else {
                     BinaryPackResult var8 = BinaryPackCodec.protect(var7);
                     var1.addEntry(var5, var8.VS(), var8.Ue());
                     if (var8.VS() == ImageOptimizeStatus.APPLIED) {
                        var4.setBytes(var8.Ud());
                     }
                  }
               } catch (Exception var11) {
                  var1.addEntry(var5, ImageOptimizeStatus.READ_ERROR, "read error");
               }
            }
         }
      }
   }

   static int removeInvalidPngs(Map var0) {
      int var1 = 0;

      for (PackEntry var3 : var0.values()) {
         String var4 = Utils.normalizeSlashes(var3.getPath());
         if (!var3.isRemoved() && var4.toLowerCase(Locale.ROOT).endsWith(".png")) {
            String var5;
            try (InputStream var6 = var3.openStream()) {
               var5 = BinaryPackCodec.validatePngHeader(var6.readNBytes(24));
            } catch (IOException var11) {
               var5 = "cannot read PNG header";
            }

            if (var5 != null) {
               var3.remove();
               var1++;
               PackWarningSummary.addWarning(
                  "Invalid PNG files",
                  "File: " + displayPath(var4) + "\nReason: " + var5 + "\nThe file was excluded and referenced models use the ItemsAdder missing texture."
               );
            }
         }
      }

      return var1;
   }

   static List findMipmapCanvasIssues(Map var0, Collection var1) {
      return findMipmapCanvasIssuesWith(var0, var1, List.of());
   }

   static List findMipmapCanvasIssuesWith(Map var0, Collection var1, Collection var2) {
      HashSet var3 = new HashSet(var1);
      var3.addAll(var2);
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      var0.values()
         .stream()
         .filter(var0x -> !var0x.isRemoved() && Utils.normalizeSlashes(var0x.getPath()).endsWith("assets/minecraft/atlases/blocks.json"))
         .forEach(var3x -> collectBlockAtlasSources(var3x, var3, var4, var5));
      if (!var4.isEmpty()) {
         for (PackEntry var7 : var0.values()) {
            if (!var7.isRemoved()) {
               String var8 = texturePathToId(Utils.normalizeSlashes(var7.getPath()));
               if (var8 != null && !var5.stream().anyMatch(var1x -> var1x.matches(var8))) {
                  String var9 = var8.substring(var8.indexOf(58) + 1);
                  if (var4.stream().anyMatch(var9::startsWith)) {
                     var3.add(var8);
                  }
               }
            }
         }
      }

      ArrayList var27 = new ArrayList();

      for (String var29 : new TreeSet(var3)) {
         String var30 = var29.contains(":") ? var29 : "minecraft:" + var29;
         int var10 = var30.indexOf(58);
         String var11 = "assets/" + var30.substring(0, var10) + "/textures/" + var30.substring(var10 + 1) + ".png";
         PackEntry var12 = (PackEntry)var0.get(var11);
         if (var12 != null && !var12.isRemoved()) {
            try (InputStream var13 = var12.openStream()) {
               byte[] var14 = var13.readNBytes(24);
               if (BinaryPackCodec.validatePngHeader(var14) == null) {
                  long var15 = BinaryPackCodec.getWidth(var14);
                  long var17 = BinaryPackCodec.getHeight(var14);
                  if (var15 % 16L != 0L || var17 % 16L != 0L) {
                     long var19 = (var15 + 15L) / 16L * 16L;
                     long var21 = (var17 + 15L) / 16L * 16L;
                     int var23 = Math.min(Long.numberOfTrailingZeros(var15), Long.numberOfTrailingZeros(var17));
                     var27.add(
                        var30
                           + " ("
                           + displayPath(var11)
                           + "): "
                           + var15
                           + "x"
                           + var17
                           + " -> recommended canvas "
                           + var19
                           + "x"
                           + var21
                           + "; limits mip level to "
                           + var23
                     );
                  }
               }
            } catch (IOException var26) {
            }
         }
      }

      return var27;
   }

   private static String displayPath(String var0) {
      String var1 = Utils.normalizeSlashes(var0);
      if (Main.getInstance() == null) {
         return var1;
      }

      try {
         File var2 = var1.startsWith("assets/") ? ContentManager.method8198(var1) : ContentManager.method8193(var1);
         return var2 == null ? var1 : Utils.shortPath(var2);
      } catch (RuntimeException var3) {
         return var1;
      }
   }

   private static void collectBlockAtlasSources(PackEntry var0, Set var1, List var2, List var3) {
      try {
         JSONObject var4 = JsonHelper.parseJson(var0.getText());
         Object var6 = var4.get("sources");
         if (!(var6 instanceof JSONArray)) {
            return;
         }

         for (Object var7 : (JSONArray)var6) {
            if (var7 instanceof JSONObject var8) {
               String var9 = var8.getString("type");
               if (var9 != null) {
                  var9 = var9.substring(var9.indexOf(58) + 1);
               }

               if ("single".equals(var9) || "unstitch".equals(var9)) {
                  String var15 = var8.getString("resource");
                  if (var15 != null) {
                     var1.add(var15);
                  }
               } else if ("directory".equals(var9)) {
                  String var11 = var8.getString("source");
                  if (var11 != null) {
                     var2.add(var11 + "/");
                  }
               } else if ("filter".equals(var9) && var8.get("pattern") instanceof JSONObject var10) {
                  var3.add(
                     new PathFilter(
                        Pattern.compile(StringUtils.defaultString(var10.getString("namespace"), ".*")),
                        Pattern.compile(StringUtils.defaultString(var10.getString("path"), ".*"))
                     )
                  );
               }
            }
         }
      } catch (Exception var12) {
      }
   }

   @Nullable
   private static String texturePathToId(String var0) {
      if (var0.startsWith("assets/") && var0.endsWith(".png")) {
         int var1 = var0.indexOf(47, "assets/".length());
         return var1 >= 0 && var0.startsWith("textures/", var1 + 1)
            ? var0.substring("assets/".length(), var1) + ":" + var0.substring(var1 + "/textures/".length(), var0.length() - ".png".length())
            : null;
      } else {
         return null;
      }
   }

   private static void warnBlockAtlasMipmap(Map var0, Collection var1, Collection var2) {
      List var3 = findMipmapCanvasIssuesWith(var0, var1, var2);
      if (!var3.isEmpty()) {
         StringBuilder var4 = new StringBuilder("[Pack] ")
            .append(var3.size())
            .append(" PNG texture")
            .append(var3.size() == 1 ? "" : "s")
            .append(" included in the block atlas limit mipmap quality. ")
            .append("Textures do not need power-of-two dimensions: for mip level 4, both width and height must be divisible by 2^4 = 16. ")
            .append("ItemsAdder does not resize user textures; update or re-export these PNGs:");
         var3.forEach(var1x -> var4.append("\n  - ").append(var1x));
         Log.warn(var4.toString());
      }
   }

   private static byte[] readCapped(InputStream var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream(8192);
      byte[] var2 = new byte[8192];

      int var3;
      while ((var3 = var0.read(var2)) >= 0) {
         if (var3 == 0) {
            int var4 = var0.read();
            if (var4 < 0) {
               break;
            }

            if (var1.size() == 134217728) {
               return null;
            }

            var1.write(var4);
         } else {
            if (var1.size() > 134217728 - var3) {
               return null;
            }

            var1.write(var2, 0, var3);
         }
      }

      return var1.toByteArray();
   }

   void writeZipEntry(PackEntry var1, ZipOutputStream var2, byte[] var3, PackManifestBuilder var4) {
      String var5 = Utils.normalizeSlashes(var1.getPath());
      ZipEntry var6 = new ZipEntry(var5);

      try {
         var2.putNextEntry(var6);

         int var8;
         try (InputStream var7 = var1.openStream()) {
            while ((var8 = var7.read(var3)) >= 0) {
               var2.write(var3, 0, var8);
            }
         }
      } catch (ZipException var18) {
         var4.addWriteError(var5, var18.getMessage());
         Log.warn("File compression skipped. Duplicate found: '" + var5 + "' | " + var18.getMessage());
      } catch (Throwable var19) {
         var4.addWriteError(var5, var19.getMessage());
         Log.warn("File compression skipped '" + var5 + "'");
         Log.error("Uncaught error", var19);
      } finally {
         var2.closeEntry();
         if (Settings.get().obfuscateZipEntries) {
            ZipEntryUtils.normalizeEntry(var6);
         }
      }
   }

   private void writeZipEntryQuiet(PackEntry var1, ZipOutputStream var2, byte[] var3) {
      this.writeZipEntry(var1, var2, var3, new PackManifestBuilder());
   }

   public boolean method6164(@Nullable DoubleCallback var1) {
      try {
         Path var2 = Utils.toPath(this.packName);
         Files.deleteIfExists(var2);
         Files.createDirectories(var2.getParent());
      } catch (Throwable var41) {
         Log.error("Failed to delete file or create folders for 'generated.zip'", var41);
         return false;
      }

      BossBarManager.setTitleKeepProgress("Collecting pack files...");
      LinkedHashMap var45 = this.collectEntries();

      try {
         this.itemBuilder
            .mergeModernItems(var45, this.Vx(), this.buildContext == null ? ContentManager.method8084().field88 : this.buildContext.VH(), Log::warn);
      } catch (IOException | RuntimeException var40) {
         Log.error("Failed to merge item definitions; pack generation aborted.", var40);
         return false;
      }

      if (Thread.currentThread().isInterrupted()) {
         return false;
      }

      BossBarManager.setTitleKeepProgress("Calling pack finalization event...");
      EventRegistrar.callEvent(new ItemsAdderPackCompressedEvent(var45));
      if (Thread.currentThread().isInterrupted()) {
         return false;
      }

      BossBarManager.setTitleKeepProgress("Validating final pack entries...");
      LinkedHashMap var3 = new LinkedHashMap(var45);
      this.deduplicateEntries(var3).forEach(Log::warn);
      int var4 = removeInvalidPngs(var3);
      if (var4 > 0) {
         Log.warn(
            "[Pack] Excluded "
               + var4
               + " invalid PNG file"
               + (var4 == 1 ? "" : "s")
               + ". Referenced models use the ItemsAdder missing texture. Details: ItemsAdder/output/resource-pack-diagnostics.txt"
         );
      }

      if (Settings.get().fixAtlasItemModels) {
         BossBarManager.setTitleKeepProgress("Finalizing modern atlas entries...");
         long var5 = System.nanoTime();
         TimestampPair var7 = PackFileCache.getStats();
         ResourcePackGenerator var8 = this.Vx();

         try {
            var8.finalizeModernAtlases(var3);
         } finally {
            Log.devLog(
               "[Pack] Modern atlas entries finalized in "
                  + (System.nanoTime() - var5) / 1000000L
                  + "ms; "
                  + (PackFileCache.getStats().calls() - var7.calls())
                  + " model JSON parses, "
                  + (PackFileCache.getStats().bytes() - var7.bytes())
                  + " decoded bytes"
            );
            var8.resetBuildState();
         }
      } else {
         try {
            this.Vx().applyHumanoidEquipmentAtlases(var3);
         } catch (IOException | RuntimeException var38) {
            Log.error("Failed to finalize humanoid equipment atlases; pack generation aborted.", var38);
            return false;
         }
      }

      if (this.buildContext == null ? Main.config.getBoolean("resource_pack.font_images.optimize_using_sheets") : this.buildContext.VK()) {
         try {
            int var46 = AtlasPacker.packFontAtlases(var3);
            if (var46 > 0) {
               Log.devLog("[Pack] Removed " + var46 + " bitmap font providers using lossless image sheets.");
            }
         } catch (IOException | RuntimeException var37) {
            Log.devWarn("[Pack] Could not optimize font images: " + var37.getMessage());
         }

         try {
            BossBarManager.setTitleKeepProgress("Preparing vanilla bitmap font sheets...");
            BitmapTriple var47 = VanillaBitmapLoader.loadBitmapFonts(
               var3, this.Vy().resolve("storage/cache/vanilla-font"), this.buildContext == null ? minecraftVersion() : this.buildContext.minecraftVersion()
            );
            if (var47.removedProviders() > 0) {
               Log.log(
                  "[Pack] Vanilla bitmap fonts: removed "
                     + var47.removedProviders()
                     + " providers, built "
                     + var47.sheetsBuilt()
                     + " sheets, "
                     + var47.cacheHits()
                     + " cache hits."
               );
            }
         } catch (IOException | RuntimeException var36) {
            Log.error("[Pack] Generation aborted: could not prepare verified Mojang vanilla glyph assets. " + var36.getMessage(), var36);
            return false;
         }
      }

      BossBarManager.setTitleKeepProgress("Validating pack textures...");
      PackLoadResult var48 = ResourcePackLoader.validatePack(
         var3,
         this::fileNameOf,
         this.buildContext == null
            ? ContentManager.method8084().field87.stream().map(var0 -> var0.toPath().resolve("minecraft/atlases")).toList()
            : this.buildContext.VG()
      );

      for (String var50 : var48.buildSummaryLines()) {
         if (var50.contains("[WARNING/")) {
            Log.warn(var50);
         } else {
            Log.log(var50);
         }
      }

      try {
         var48.writeReport(this.Vy().resolve("output/pack-texture-validation.txt"));
      } catch (IOException var35) {
         Log.warn("[Pack textures] Could not write detailed validation report: " + var35.getMessage());
      }

      PackAssetChecker.method6641(var3);
      if (Settings.get().validateShaders) {
         BossBarManager.setTitleKeepProgress("Validating pack shaders...");
         ShaderValidator.validateAndLogWith(var3, this::fileNameOf);
      }

      if (Thread.currentThread().isInterrupted()) {
         return false;
      }

      PackManifestBuilder var49 = new PackManifestBuilder();
      BossBarManager.setTitleKeepProgress("Processing pack images...");
      obfuscatePngs(var3, var49);
      if (Thread.currentThread().isInterrupted()) {
         return false;
      }

      try {
         int var51 = pruneOrphanOverlays(var3);
         if (var51 > 0) {
            Log.devLog("[Pack] Removed " + var51 + " empty overlay declarations.");
         }

         int var53 = mergeOverlay26_2(var3);
         if (var53 > 0) {
            Log.devLog("[Pack] Flattened " + var53 + " resources into the 26.2+ overlay.");
         }

         int var9 = PackStatistics.pruneOverlaidFiles(var3);
         if (var9 > 0) {
            Log.devLog("[Pack] Removed " + var9 + " identical overlay resources with equivalent fallbacks.");
         }
      } catch (IOException | RuntimeException var34) {
         Log.devWarn("[Pack] Failed to remove empty overlay declarations: " + var34.getMessage());
      }

      double var52 = 0.0;
      double var54 = var3.values().stream().filter(var0 -> !var0.isRemoved()).count();
      if (Settings.get().obfuscateZipArchive) {
         var54 *= 2.0;
      }

      byte[] var11 = new byte[8192];
      BossBarManager.setTitleKeepProgress("Writing pack archive...");

      try (
         FileOutputStream var12 = new FileOutputStream(this.packName);
         ZipOutputStream var13 = new ZipOutputStream(var12);
      ) {
         var13.setLevel(9);
         var13.setMethod(8);

         for (PackEntry var15 : var3.values()) {
            if (Thread.currentThread().isInterrupted()) {
               return false;
            }

            if (!var15.isRemoved()) {
               var52++;
               if (var1 != null) {
                  var1.run(Math.min(var54 == 0.0 ? 1.0 : var52 / var54, 1.0));
               }

               this.writeZipEntry(var15, var13, var11, var49);
            }
         }

         var13.finish();
         var13.flush();
         var13.close();
         var12.flush();
         var12.close();
         if (!Thread.currentThread().isInterrupted()) {
            try {
               if (Settings.get().obfuscateZipArchive) {
                  BossBarManager.setTitleKeepProgress("Protecting pack archive...");
                  ZipEntryUtils.scrubZipMetadata(new File(this.packName));
               }
            } catch (Throwable var33) {
               Log.error("Error protecting resourcepack", var33);
            }

            if (var1 != null) {
               var1.run(1.0);
            }

            var49.logSummary();
            return true;
         } else {
            return false;
         }
      } catch (Throwable var44) {
         Log.error("Error compressing the zip file", var44);
         var49.logSummary();
         if (var1 != null) {
            var1.run(1.0);
         }

         return false;
      }
   }

   static int mergeOverlay26_2(Map var0) {
      PackEntry var1 = (PackEntry)var0.get("pack.mcmeta");
      if (var1 != null && !var1.isRemoved()) {
         JSONObject var2 = JSON.parseObject(var1.getText());
         JSONObject var3 = var2.getJSONObject("overlays");
         JSONArray var4 = var3 == null ? null : var3.getJSONArray("entries");
         if (var4 == null) {
            return 0;
         }

         if (var4.stream()
            .anyMatch(
               var0x -> var0x instanceof JSONObject var1x && (var1x.get("min_format") instanceof JSONArray || var1x.get("max_format") instanceof JSONArray)
            )) {
            return 0;
         }

         byte var5 = 88;
         JSONObject var6 = var4.stream()
            .filter(JSONObject.class::isInstance)
            .map(JSONObject.class::cast)
            .filter(var0x -> "ia_overlay_26_2_plus".equals(var0x.getString("directory")))
            .findFirst()
            .orElse(null);
         if (var6 == null) {
            return 0;
         }

         int[] var7 = ResourcePackGenerator.parseFormatRange(var6);
         if (var5 >= var7[0] && var5 <= var7[1]) {
            if (var4.stream()
               .filter(JSONObject.class::isInstance)
               .map(JSONObject.class::cast)
               .filter(var1x -> var1x != var6)
               .map(ResourcePackGenerator::parseFormatRange)
               .anyMatch(var1x -> var1x[1] >= var7[0] && (var1x[0] > var7[0] || var1x[1] != var7[1]))) {
               return 0;
            }

            List var8 = var4.stream().filter(JSONObject.class::isInstance).map(JSONObject.class::cast).filter(var1x -> {
               int[] var2x = ResourcePackGenerator.parseFormatRange(var1x);
               return var5 >= var2x[0] && var5 <= var2x[1];
            }).toList();
            if (var8.size() >= 2 && !var8.stream().anyMatch(var0x -> !var0x.containsKey("max_format"))) {
               List var9 = var0.entrySet().stream().filter(var0x -> !((PackEntry)var0x.getValue()).isRemoved()).toList();
               LinkedHashMap var10 = new LinkedHashMap();
               HashSet var11 = new HashSet();

               for (JSONObject var13 : var8) {
                  String var14 = overlayDirectory(var13);
                  String var15 = var14 + "/";
                  var9.stream()
                     .filter(var1x -> ((String)var1x.getKey()).startsWith(var15))
                     .forEach(var2x -> var10.put(((String)var2x.getKey()).substring(var15.length()), (PackEntry)var2x.getValue()));
                  if (var13 != var6) {
                     if (ResourcePackGenerator.parseFormatRange(var13)[0] >= var5) {
                        var11.add(var14);
                     } else {
                        setOverlayMaxFormat(var13, var5 - 1);
                     }
                  }
               }

               String var16 = overlayDirectory(var6);
               var4.removeIf(var1x -> var1x instanceof JSONObject var2x && var11.contains(overlayDirectory(var2x)));
               var0.entrySet().removeIf(var1x -> var11.stream().anyMatch(var1xx -> ((String)var1x.getKey()).startsWith(var1xx + "/")));
               var10.forEach((var2x, var3x) -> {
                  String var4x = var16 + "/" + var2x;
                  if (!var4x.equals(Utils.normalizeSlashes(var3x.getPath()))) {
                     var0.put(var4x, PackEntry.stream(var4x, var3x::openStream));
                  }
               });
               var1.setBytes(JSON.toJSONBytes(var2, new Feature[]{Feature.LargeObject}));
               return var10.size();
            } else {
               return 0;
            }
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   private static void setOverlayMaxFormat(JSONObject var0, int var1) {
      int var2 = ResourcePackGenerator.parseFormatMajor(var0.get("max_format"));
      var0.put("max_format", var1);
      Object var3 = var0.get("formats");
      if (var3 instanceof JSONArray var5) {
         if (var5.size() == 2 && var5.getIntValue(1) == var2) {
            var5.set(1, var1);
         }
      } else if (var3 instanceof JSONObject var6 && var6.getIntValue("max_inclusive") == var2) {
         var6.put("max_inclusive", var1);
      }
   }

   private static String overlayDirectory(JSONObject var0) {
      return ResourcePackGenerator.overlayDirectoryName(var0);
   }

   static int pruneOrphanOverlays(Map var0) {
      PackEntry var1 = (PackEntry)var0.get("pack.mcmeta");
      if (var1 != null && !var1.isRemoved()) {
         JSONObject var2 = JSON.parseObject(var1.getText());
         JSONObject var3 = var2.getJSONObject("overlays");
         JSONArray var4 = var3 == null ? null : var3.getJSONArray("entries");
         if (var4 == null) {
            return 0;
         }

         List var5 = var0.entrySet().stream().filter(var0x -> !((PackEntry)var0x.getValue()).isRemoved()).map(Entry::getKey).toList();
         int var6 = var4.size();
         var4.removeIf(var1x -> {
            if (var1x instanceof JSONObject var2x) {
               String var3x = overlayDirectory(var2x);
               return StringUtils.isBlank(var3x) ? false : var5.stream().noneMatch(var1xx -> var1xx.startsWith(var3x + "/"));
            } else {
               return false;
            }
         });
         int var7 = var6 - var4.size();
         if (var7 > 0) {
            var1.setBytes(JSON.toJSONBytes(var2, new Feature[]{Feature.LargeObject}));
         }

         return var7;
      } else {
         return 0;
      }
   }

   void clear() {
      for (InputStream var2 : this.streamEntries.values()) {
         try {
            var2.close();
         } catch (IOException var4) {
         }
      }

      this.fileSources.clear();
      this.fileByDestination.clear();
      this.stringEntries.clear();
      this.streamEntries.clear();
      this.writers.clear();
      this.sourceFiles.clear();
      this.modernModelDestinations.clear();
      this.generatedStreams.clear();
      this.entryInfo.clear();
   }

   private boolean isIgnoredPath(String var1) {
      if (this.ignoredSuffixes == null) {
         return false;
      }

      for (String var3 : this.ignoredSuffixes) {
         if (var1.endsWith(var3)) {
            return true;
         }
      }

      return false;
   }

   private void writeStreamEntry(InputStream var1, String var2, ZipOutputStream var3, byte[] var4) {
      var2 = Utils.normalizeSlashes(var2);
      ZipEntry var5 = new ZipEntry(var2);

      try {
         var3.putNextEntry(var5);

         int var6;
         try {
            while ((var6 = var1.read(var4)) >= 0) {
               var3.write(var4, 0, var6);
            }
         } finally {
            var3.closeEntry();
            if (Settings.get().obfuscateZipEntries) {
               ZipEntryUtils.normalizeEntry(var5);
            }
         }
      } catch (ZipException var16) {
         Log.warn("Stream compression skipped. Duplicate found: (stream) -> '" + var2 + "' | " + var16.getMessage());
      } finally {
         var3.closeEntry();
      }
   }

   private void writeFileEntry(String var1, String var2, ZipOutputStream var3, byte[] var4) {
      var2 = Utils.normalizeSlashes(var2);
      ZipEntry var5 = new ZipEntry(var2);

      try {
         var3.putNextEntry(var5);

         try {
            int var7;
            try (FileInputStream var6 = new FileInputStream(var1)) {
               while ((var7 = var6.read(var4)) >= 0) {
                  var3.write(var4, 0, var7);
               }
            }
         } finally {
            var3.closeEntry();
            if (Settings.get().obfuscateZipEntries) {
               ZipEntryUtils.normalizeEntry(var5);
            }
         }
      } catch (ZipException var26) {
         Log.warn("File compression skipped. Duplicate found: '" + Utils.shortPathOf(var1) + "' -> '" + var2 + "' | " + var26.getMessage());
      } catch (Throwable var27) {
         Log.warn("File compression skipped. Duplicate found '" + Utils.shortPathOf(var1) + "' -> '" + var2 + "'");
         Log.error("Uncaught error", var27);
      } finally {
         var3.closeEntry();
      }
   }

   private void writeBundledEntry(String var1, String var2, ZipOutputStream var3, byte[] var4) {
      var2 = Utils.normalizeSlashes(var2);
      ZipEntry var5 = new ZipEntry(var2);

      try {
         var3.putNextEntry(var5);
      } catch (ZipException var16) {
         Log.warn("Bundled file compression skipped. Duplicate found '" + Utils.shortPathOf(var1) + "' -> '" + var2 + "' | " + var16.getMessage());
         var3.closeEntry();
         return;
      } catch (Throwable var17) {
         Log.error("Bundled file compression skipped. Duplicate found '" + Utils.shortPathOf(var1) + "' -> '" + var2 + "'");
         Log.error("Uncaught error", var17);
         var3.closeEntry();
         return;
      }

      try (InputStream var6 = this.getClass().getResourceAsStream("/" + var1)) {
         if (var6 == null) {
            Log.warn("Bundled file compression failed '" + Utils.shortPathOf(var1) + "' -> '" + var2 + "'");
            Log.warn("Bundled file is null.");
            var3.closeEntry();
            return;
         }

         int var7;
         while ((var7 = var6.read(var4)) >= 0) {
            var3.write(var4, 0, var7);
         }
      } finally {
         var3.closeEntry();
         if (Settings.get().obfuscateZipEntries) {
            ZipEntryUtils.normalizeEntry(var5);
         }
      }
   }

   void addResourceFiles() {
      ArrayList var1 = new ArrayList();
      Log.devLog("Adding remaining files in namespaces folders...");

      try {
         for (File var3 : this.buildContext == null ? ContentManager.method8084().field88 : this.buildContext.VH()) {
            if (!var3.getName().startsWith(".")) {
               Log.devLog("------- RESOURCE PACK FOLDER: " + var3 + " -------");
               File var4 = new File(var3, "pack.mcmeta");
               if (var4.exists()) {
                  JSONObject var5 = JsonHelper.readJsonFile(var4);
                  File var6 = var4.getParentFile();
                  if (var5.containsKey("overlays") && var5.getJSONObject("overlays").containsKey("entries")) {
                     var5.getJSONObject("overlays").getJSONArray("entries").forEach(var3x -> {
                        try {
                           JSONObject var4x = (JSONObject)var3x;
                           String var5x = var4x.getString("directory");
                           if (StringUtils.isBlank(var5x)) {
                              Log.warn("Overlay 'directory' attribute is empty in: " + Utils.shortPath(var6));
                              return;
                           }

                           File var6x = new File(var6, var5x);
                           if (var6x.exists()) {
                              for (File var9x : FileUtils.listFiles(var6x, null, true)) {
                                 if (!this.ignoredExtensions.contains(FilenameUtils.getExtension(var9x.getName()))) {
                                    String var10x = Utils.normalizeSlashes(var9x.getAbsolutePath().replace(var6.getAbsolutePath(), ""));
                                    String var11x = var6x.toPath().relativize(var9x.toPath()).toString().replace('\\', '/');
                                    var10x = ResourcePackBuilder.resolveAssetPath(var5x, var11x);
                                    this.registerItemFile(var6, var9x, var10x);
                                    this.addFile(var9x, var10x, true);
                                    if (Settings.get().logCompressionOverlays) {
                                       Log.log(ChatColor.AQUA + "[Pack] Added overlay file: " + var10x);
                                    }

                                    var1.add(var9x);
                                 }
                              }
                           } else {
                              Log.warn("Overlay directory '" + var5x + "' not found in: " + Utils.shortPath(var6));
                           }
                        } catch (Exception var12x) {
                           Log.error("Error adding overlay.", var12x);
                        }
                     });
                  }
               }
            }
         }
      } catch (Exception var12) {
         Log.error("Error adding overlays.", var12);
      }

      Log.devLog("Adding all the files in the namespaces folders...");
      List var13 = this.buildContext == null ? ContentManager.method8203() : this.buildContext.VI();

      for (File var16 : var13) {
         File var17 = new File(var16, "assets");
         boolean var18 = var17.isDirectory() && var13.contains(var17);

         for (File var9 : FileUtils.listFiles(var16, null, true)) {
            if (!this.ignoredExtensions.contains(FilenameUtils.getExtension(var9.getName()))
               && !isInHiddenFolder(var16, var9)
               && !var1.contains(var9)
               && (!var18 || !var9.toPath().startsWith(var17.toPath()))) {
               String var10 = "assets/"
                  + Utils.normalizeSlashes(var9.getAbsolutePath())
                     .replace(Utils.normalizeSlashes(var16.getParentFile().getAbsolutePath()), "")
                     .replaceFirst("^/?(resourcepack|resource_pack)/?", "");
               var10 = Utils.normalizeSlashes(var10);
               if (var10.startsWith("assets/assets/")) {
                  var10 = var10.substring("assets/".length());
               }

               if (ResourcePackBuilder.isItemMergeTarget(var10)) {
                  this.registerItemFile(this.findPackRoot(var16, var9), var9, var10);
               }

               if (!var10.startsWith("assets/minecraft/atlases/")) {
                  if (this.isRegistered(var10)) {
                     if (!var10.startsWith("assets/minecraft/atlases/")
                        && !var10.equals("assets/minecraft/font/default.json")
                        && !var10.startsWith("assets/minecraft/lang/")
                        && !var10.startsWith("assets/minecraft/blockstates/")
                        && var10.startsWith("assets/minecraft/models/item/")) {
                     }
                  } else if (validatePackPath(var10)) {
                     boolean var11 = this.addFile(var9, var10, false);
                     if (var11 && Settings.get().logCompressionFiles) {
                        Log.log("[Pack] Added file: " + Utils.shortPath(var9));
                     }
                  }
               }
            }
         }
      }

      File var15 = this.buildContext == null ? ContentManager.method8193("pack.png") : this.buildContext.VJ();
      if (var15 != null) {
         this.addFile(var15, var15.getName(), false);
      }
   }

   private void registerItemFile(File var1, File var2, String var3) {
      if (ResourcePackBuilder.isItemMergeTarget(var3)
         && !var2.getName().startsWith(".")
         && !this.isIgnoredPath(var3)
         && !this.ignoredExtensions.contains(FilenameUtils.getExtension(var2.getName()))
         && validatePackPath(var3)) {
         this.itemBuilder.registerItemInput(var1, var2, var3, Log::warn);
      }
   }

   private File findPackRoot(File var1, File var2) {
      Path var3 = var2.toPath().toAbsolutePath().normalize();
      return (this.buildContext == null ? ContentManager.method8084().field88 : this.buildContext.VH())
         .stream()
         .filter(var1x -> var3.startsWith(var1x.toPath().toAbsolutePath().normalize()))
         .max(Comparator.comparingInt(var0 -> var0.toPath().toAbsolutePath().normalize().getNameCount()))
         .orElseGet(() -> var1.getParentFile().getName().equals("assets") ? var1.getParentFile().getParentFile() : var1.getParentFile());
   }

   public static boolean isInHiddenFolder(File var0, File var1) {
      try {
         String var2 = var0.getCanonicalPath().replace('\\', '/');
         String var3 = var1.getCanonicalPath().replace('\\', '/');
         if (!var3.startsWith(var2)) {
            return false;
         }

         if (var0.getName().startsWith(".")) {
            return true;
         }

         String var4 = var3.substring(var2.length());
         if (var4.startsWith("/")) {
            var4 = var4.substring(1);
         }

         if (var4.isEmpty()) {
            return false;
         }

         String[] var5 = var4.split("/");

         for (int var6 = var5.length - 1; var6 >= 0; var6--) {
            String var7 = var5[var6];
            if (var7.startsWith(".")) {
               return true;
            }
         }

         return false;
      } catch (IOException var8) {
         return var1.getName().startsWith(".");
      }
   }

   public static void printAllowedCharacters() {
      Log.error("Allowed characters: _ . - abcdefghijklmnopqrstuvwxyz 1234567890");
   }

   private static boolean validatePackPath(String var0) {
      if (isValidPackPath(var0)) {
         return true;
      }

      Log.error("Invalid namespace or file path in pack: " + var0);
      return false;
   }

   static boolean isValidPackPath(String var0) {
      if (!var0.contains("minecraft/texts/") && var0.endsWith(".txt")) {
         return true;
      }

      if (!var0.startsWith("assets/")) {
         return false;
      }

      String var1 = var0.substring("assets/".length());
      int var2 = var1.indexOf(47);
      return var2 > 0 && var2 < var1.length() - 1 && isValidNamespace(var1.substring(0, var2)) && hasValidPathChars(var1);
   }

   public static boolean checkPathValid(String var0) {
      try {
         if (!var0.contains("minecraft/texts/") && var0.endsWith(".txt")) {
            return true;
         }

         String var1 = Utils.normalizeSlashes(var0);
         var1 = Utils.normalizePluginPath(var1);
         if (var1.startsWith("assets/")) {
            var1 = var1.substring(7);
         }

         String[] var2 = var1.split("/");
         String var3;
         if (var2.length < 2) {
            var3 = var1;
         } else {
            String var4 = var2[1];
            var3 = var1.substring(var1.indexOf(var4));
            if (!isValidNamespace(var4)) {
               Log.error("Invalid character in namespace '" + var4 + "' of file: " + Utils.normalizePluginPath(var0));
               return false;
            }
         }

         if (!hasValidPathChars(var3)) {
            Log.error("Invalid character in folders/name of file: " + Utils.normalizePluginPath(var0));
            return false;
         }
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

      return true;
   }

   public static boolean hasValidPathChars(String var0) {
      for (int var1 = 0; var1 < var0.length(); var1++) {
         if (!isValidPathChar(var0, var1)) {
            return false;
         }
      }

      return true;
   }

   public static boolean isValidIdentifier(String var0) {
      for (int var1 = 0; var1 < var0.length(); var1++) {
         char var2 = var0.charAt(var1);
         if (var2 != '#' && var2 != '_' && (var2 < 'a' || var2 > 'z') && (var2 < '0' || var2 > '9')) {
            return false;
         }
      }

      return true;
   }

   private static boolean isValidPathChar(String var0, int var1) {
      char var2 = var0.charAt(var1);
      return var2 == '/' || var2 == ':' || var2 == '_' || var2 == '-' || var2 >= 'a' && var2 <= 'z' || var2 >= '0' && var2 <= '9' || var2 == '.';
   }

   public static boolean isValidNamespace(String var0) {
      for (int var1 = 0; var1 < var0.length(); var1++) {
         char var2 = var0.charAt(var1);
         if (var2 != '_' && var2 != '-' && (var2 < 'a' || var2 > 'z') && (var2 < '0' || var2 > '9') && var2 != '.') {
            return false;
         }
      }

      return true;
   }

   @Nullable
   public static List findInvalidPathChars(String var0) {
      ArrayList var1 = null;

      for (int var2 = 0; var2 < var0.length(); var2++) {
         char var3 = var0.charAt(var2);
         if (var3 != '/' && var3 != ':' && var3 != '_' && var3 != '-' && (var3 < 'a' || var3 > 'z') && (var3 < '0' || var3 > '9') && var3 != '.') {
            if (var1 == null) {
               var1 = new ArrayList();
            }

            var1.add(var3);
         }
      }

      return var1;
   }

   @Nullable
   public static List findInvalidIdChars(String var0) {
      ArrayList var1 = null;

      for (int var2 = 0; var2 < var0.length(); var2++) {
         char var3 = var0.charAt(var2);
         if (var3 != '/' && var3 != ':' && var3 != '_' && var3 != '-' && (var3 < 'a' || var3 > 'z') && (var3 < '0' || var3 > '9')) {
            if (var1 == null) {
               var1 = new ArrayList();
            }

            var1.add(var3);
         }
      }

      return var1;
   }

   public void applyModelJsonProcessor(Consumer var1) {
      ArrayList var2 = new ArrayList();
      this.fileSources.forEach((var3, var4x) -> {
         if (var4x.matches("assets/[^/]+/models/.*\\.json")) {
            try {
               JSONObject var5x = JsonHelper.readJsonFile(var3);
               var1.accept(var5x);
               this.addJsonUnchanged(var5x, var4x);
               var2.add(var3);
            } catch (IOException var6x) {
               Log.warn("Failed to read JSON file: " + Utils.relativePath(var3));
            }
         }
      });

      for (File var4 : var2) {
         String var5 = (String)this.fileSources.remove(var4);
         if (var5 != null) {
            this.fileByDestination.remove(var5);
         }
      }

      HashMap var6 = new HashMap();
      this.streamEntries.forEach((var2x, var3) -> {
         if (var2x.matches("assets/[^/]+/models/.*\\.json")) {
            try {
               String var4x = Utils.readStreamAsString(var3);
               JSONObject var5x = JsonHelper.parseJson(var4x);
               var1.accept(var5x);
               var6.put(var2x, new ByteArrayInputStream(JsonHelper.toJsonString(var5x).getBytes(StandardCharsets.UTF_8)));
            } catch (Throwable var6x) {
               Log.warn("Failed to read JSON file from stream: " + var2x);
            }
         }
      });

      for (String var10 : var6.keySet()) {
         this.streamEntries.removeAll(var10);
      }

      for (Entry var11 : var6.entrySet()) {
         this.streamEntries.put((String)var11.getKey(), (InputStream)var11.getValue());
      }

      HashMap var9 = new HashMap();
      this.stringEntries.forEach((var3, var4x) -> {
         if (var4x.matches("assets/[^/]+/models/.*\\.json")) {
            try {
               try (InputStream var5x = this.getClass().getResourceAsStream("/" + var3)) {
                  if (var5x != null) {
                     String var6x = Utils.readStreamAsString(var5x);
                     JSONObject var7 = JsonHelper.parseJson(var6x);
                     var1.accept(var7);
                     this.addJsonUnchanged(var7, var4x);
                     var9.put(var3, var4x);
                     this.streamEntries.put(var4x, new ByteArrayInputStream(JsonHelper.toJsonString(var7).getBytes(StandardCharsets.UTF_8)));
                     return;
                  }

                  Log.warn("Bundled file compression failed '" + Utils.shortPathOf(var3) + "' -> '" + var4x + "'");
                  Log.warn("Bundled file is null.");
               }

               return;
            } catch (Throwable var10x) {
               Log.warn("Failed to read JSON file from bundled file: " + Utils.shortPathOf(var3));
            }
         }
      });
      var9.forEach((var1x, var2x) -> this.stringEntries.removeAll(var1x));
   }
}
