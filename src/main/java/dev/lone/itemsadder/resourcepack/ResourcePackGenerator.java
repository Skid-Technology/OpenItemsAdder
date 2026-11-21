package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import com.google.common.io.BaseEncoding;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.anim.AnimationBinaryCodec;
import dev.lone.itemsadder.anim.AnimationBinaryPayload;
import dev.lone.itemsadder.anim.AnimationDataCompiler;
import dev.lone.itemsadder.anim.AnimationJsonBuilder;
import dev.lone.itemsadder.anim.AnimationSourceEntry;
import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.PackEntry;
import dev.lone.itemsadder.block.BlockRealizationMode;
import dev.lone.itemsadder.block.BlockStateMapper;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.CustomEntityDefinition;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.fontimage.FontImageLoader;
import dev.lone.itemsadder.fontimage.FontImageSettingsRecord;
import dev.lone.itemsadder.gui.GuiEntryRecord;
import dev.lone.itemsadder.gui.ScrollableGuiAssets;
import dev.lone.itemsadder.gui.ScrollableGuiRegistry;
import dev.lone.itemsadder.internal.SwitchMap74;
import dev.lone.itemsadder.item.CustomItemDataEntry;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.item.ItemSpec;
import dev.lone.itemsadder.item.LeatherColorDefinition;
import dev.lone.itemsadder.item.TexturedItemBase;
import dev.lone.itemsadder.loot.LootTableLoader;
import dev.lone.itemsadder.util.BinaryPackCodec;
import dev.lone.itemsadder.util.BossBarManager;
import dev.lone.itemsadder.util.CustomHashSet;
import dev.lone.itemsadder.util.DoubleCallback;
import dev.lone.itemsadder.util.FileHelper;
import dev.lone.itemsadder.util.IntBoolPair;
import dev.lone.itemsadder.util.IntVector3;
import dev.lone.itemsadder.util.JsonHelper;
import dev.lone.itemsadder.util.ListCollector;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NameRegistry;
import dev.lone.itemsadder.util.OneShotRunner;
import dev.lone.itemsadder.util.PackedByteStream;
import dev.lone.itemsadder.util.PersistentCounter;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.ServerCompat;
import dev.lone.itemsadder.util.StringKey;
import dev.lone.itemsadder.util.StringPair;
import dev.lone.itemsadder.util.Utils;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.lang.runtime.SwitchBootstraps;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ResourcePackGenerator {
   public static final String OVERLAY_1_21_4_PLUS_DIR = "ia_overlay_1_21_4_plus";
   private static final int MODERN_FORMAT_THRESHOLD = 65;
   static final int FORMAT_1_21_10 = 69;
   static final int FORMAT_26_2 = 88;
   static final int FORMAT_26_3 = 97;
   static final int bzy = 1;
   private static final int bzz = 9999;
   private static final int bzA = 512;
   private static final String DIAGNOSTICS_FILE_NAME = "resource-pack-diagnostics.txt";
   private static final JSONObject TRIM_PALETTE_SOURCES = JSON.parseObject(
      "{\n  \"sources\": [\n    {\"type\":\"minecraft:directory\",\"prefix\":\"item/\",\"source\":\"item\"},\n    {\n      \"type\":\"minecraft:paletted_permutations\",\n      \"palette_key\":\"minecraft:trims/color_palettes/trim_palette\",\n      \"textures\":[\n        \"minecraft:trims/items/helmet_trim\",\n        \"minecraft:trims/items/chestplate_trim\",\n        \"minecraft:trims/items/leggings_trim\",\n        \"minecraft:trims/items/boots_trim\"\n      ],\n      \"permutations\":{\n        \"amethyst\":\"minecraft:trims/color_palettes/amethyst\",\n        \"copper\":\"minecraft:trims/color_palettes/copper\",\n        \"copper_darker\":\"minecraft:trims/color_palettes/copper_darker\",\n        \"diamond\":\"minecraft:trims/color_palettes/diamond\",\n        \"diamond_darker\":\"minecraft:trims/color_palettes/diamond_darker\",\n        \"emerald\":\"minecraft:trims/color_palettes/emerald\",\n        \"gold\":\"minecraft:trims/color_palettes/gold\",\n        \"gold_darker\":\"minecraft:trims/color_palettes/gold_darker\",\n        \"iron\":\"minecraft:trims/color_palettes/iron\",\n        \"iron_darker\":\"minecraft:trims/color_palettes/iron_darker\",\n        \"lapis\":\"minecraft:trims/color_palettes/lapis\",\n        \"netherite\":\"minecraft:trims/color_palettes/netherite\",\n        \"netherite_darker\":\"minecraft:trims/color_palettes/netherite_darker\",\n        \"quartz\":\"minecraft:trims/color_palettes/quartz\",\n        \"redstone\":\"minecraft:trims/color_palettes/redstone\",\n        \"resin\":\"minecraft:trims/color_palettes/resin\"\n      }\n    }\n  ]\n}\n"
   );
   private static final JSONObject BLOCK_ATLAS_SOURCES = JSON.parseObject(
      "{\n  \"sources\": [\n    {\"type\":\"minecraft:directory\",\"prefix\":\"block/\",\"source\":\"block\"},\n    {\"type\":\"minecraft:directory\",\"prefix\":\"entity/conduit/\",\"source\":\"entity/conduit\"},\n    {\"type\":\"minecraft:single\",\"resource\":\"minecraft:entity/bell/bell_body\"},\n    {\"type\":\"minecraft:single\",\"resource\":\"minecraft:entity/enchanting_table_book\"}\n  ]\n}\n"
   );
   private final HashSet ignoredExtensions;
   private final OneShotRunner generationRunner = new OneShotRunner();
   public volatile boolean generationRunning = false;
   private volatile Throwable lastFailure;
   public volatile boolean generated = false;
   ArmorTextureBuilder armorTextureBuilder;
   public HashMap materialModelJsons;
   public HashMap overlayEntriesByRange;
   final Map bzM = new TreeMap();
   public Map manifestJsons;
   public Map furnitureItemJsons;
   private final Map bzP = new HashMap();
   public HashMap blockstateOverlays;
   Set bzR;
   Set blockTextureModels;
   Set forcedModelIds = Set.of();
   final Map atlasModelRefs = new TreeMap();
   final Map blockstateModelRefs = new TreeMap();
   final Map itemDefinitionCache = new PackFileCache(true);
   final Map blockstateCache = new PackFileCache(true);
   final Map bzY = new HashMap();
   ItemContextIndex itemContextIndex;
   final Map modelJsonCache = new PackFileCache(true);
   final Map modelSourceCache = new PackFileCache(true);
   final Map modernModelCache = new PackFileCache(true);
   private Set reservedAssetIds = Set.of();
   final Map modelFlags = new HashMap();
   private PackIssueLog issueLog = new PackIssueLog();
   private Consumer warnSink = var0 -> {};
   private Consumer infoSink = var0 -> {};
   private final PackBuildMetrics buildMetrics = new PackBuildMetrics();
   private final Map bAj = new IdentityHashMap();
   private Set bAk = Set.of();
   @Nullable
   private PackBuildReport buildReport;
   private Map bAm = Map.of();
   public JSONObject packArraysJson;
   public HashMap bAo;
   public HashMap atlasSources;
   public JSONObject blocksAtlas;
   public JSONObject bAr;
   private Map bAs = Map.of();
   static Map modelParents = new HashMap();
   CustomHashSet packFileEntries = new CustomHashSet();
   private final Set pendingAtlasKeys = new HashSet();
   public List bAw;
   JSONObject bAx = new JSONObject();
   static NameRegistry nameRegistry = new NameRegistry();
   Set skippedAssets = new HashSet();
   ResourcePackWriter writer;
   private ScrollableGuiRegistry scrollGuiRegistry = ScrollableGuiAssets.XP();

   @Internal
   public boolean hasVanillaAssetFailure() {
      return this.lastFailure instanceof VanillaAssetException;
   }

   ResourcePackGenerator() {
      PackConfigProcessor.resetDedupState();
      PackWarningSummary.reset();
      this.ignoredExtensions = new HashSet();
   }

   public ResourcePackGenerator(ResourcePackService var1) {
      PackConfigProcessor.resetDedupState();
      PackWarningSummary.reset();
      this.ignoredExtensions = new HashSet(Main.config.jZ("resource_pack.ignore_files_ext"));
      this.armorTextureBuilder = ArmorTextureBuilder.getInstance(true);
      this.materialModelJsons = new HashMap();
      this.overlayEntriesByRange = new HashMap();
      this.manifestJsons = new PackFileCache(true);
      this.furnitureItemJsons = new PackFileCache(true);
      if (!(var1.delivery instanceof ExternalPackDelivery) && hasServerResourcePackProperty()) {
         Log.error(" ");
         Log.error(" ");
         Log.error(" ");
         Log.error(" ");
         Log.error(
            "[Pack] &cERROR: please remove the &6resource-pack &csetting from &6server.properties &cfile. It's in the root folder of the server. Remove it and restart."
         );
         Log.error(" ");
         Log.error(" ");
         Log.error(" ");
         Log.error(" ");
      }
   }

   public static void extractDefaultContents() {
      CodeSource var0 = Main.class.getProtectionDomain().getCodeSource();
      if (var0 != null) {
         Plugin var1 = Main.getPlugin();
         boolean var2 = false;
         int var3 = 0;

         try {
            File var4 = new File(var0.getLocation().toURI());

            try (JarFile var5 = new JarFile(var4)) {
               Enumeration var6 = var5.entries();

               while (var6.hasMoreElements()) {
                  JarEntry var7 = (JarEntry)var6.nextElement();
                  String var8 = var7.getName();
                  if (var8.startsWith("contents/") && !var7.isDirectory()) {
                     File var9 = new File(var1.getDataFolder(), var8);
                     if (!var9.exists()) {
                        try (InputStream var10 = var5.getInputStream(var7)) {
                           if (!var2) {
                              Log.log(ChatColor.AQUA + "[Pack] Extracting default files...");
                              var2 = true;
                           }

                           FileUtils.copyInputStreamToFile(var10, var9);
                           Log.log(ChatColor.AQUA + "[Pack] - Extracted " + var8);
                           var3++;
                        }
                     }
                  }
               }
            }

            try {
               boolean var19 = false;
               File var20 = new File(var1.getDataFolder() + "/contents/_iainternal/resourcepack/assets/_iainternal/font/gui.json");
               if (var20.exists()) {
                  JSONObject var21 = JsonHelper.readJsonFile(var20);

                  for (Object var23 : var21.getJSONArray("providers")) {
                     JSONObject var24 = (JSONObject)var23;
                     if (var24.getString("file").equals("base/black.png")) {
                        var24.put("file", "char/black.png");
                        var19 = true;
                     }
                  }

                  if (var19) {
                     FileUtils.write(var20, JsonHelper.toJsonString(var21));
                     var3++;
                  }
               }
            } catch (Throwable var15) {
               Log.error("[Pack] Failed to update old 'contents/_iainternal' files. Consider deleting the folder and regenerate your pack.", var15);
            }

            if (var3 > 0) {
               Log.log(ChatColor.AQUA + "[Pack] Extracted " + var3 + " default files.");
            }
         } catch (IOException | URISyntaxException var18) {
            Log.error("[Pack] ERROR EXTRACTING internal contents!", var18);
         }
      } else {
         Log.error("[Pack] ERROR EXTRACTING internal contents! Try to download the plugin JAR again and restart.");
      }
   }

   public static void setFrontGuiLight(JSONObject var0) {
      if (!var0.containsKey("parent") || var0.getString("parent").equals("item/generated")) {
         if (!var0.containsKey("gui_light")) {
            var0.put("gui_light", "front");
         }
      }
   }

   public static void registerModelJson(String var0, JSONObject var1) {
      modelParents.put(var0, var1.getString("parent"));
      if (Settings.get().compressJson) {
         removeGroups(var1);
      }

      if (Settings.get().fixMissingParticle) {
         fixMissingParticle(var1);
      }

      if (Settings.get().fixDarkInInventory) {
         setFrontGuiLight(var1);
      }
   }

   public File getPackFile() {
      return new File(ContentManager.field72);
   }

   public long getPackFileSize() {
      return this.getPackFile().length();
   }

   public boolean packFileExists() {
      return this.getPackFile().exists();
   }

   public String getPackFileSizeText() {
      return FileUtils.byteCountToDisplaySize(this.getPackFile().length());
   }

   public String getPackFileDateText() {
      return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(this.getPackFile().lastModified()));
   }

   public String getPackFilePath() {
      return Utils.worldRelativePath(this.getPackFile()).replace("\\", "/");
   }

   public String getPackFileSha1() {
      return computeSha1Hex(this.getPackFile());
   }

   private static String computeSha1Hex(File var0) {
      try {
         FileInputStream var3 = new FileInputStream(var0);

         byte[] var2;
         try {
            var2 = DigestUtils.sha1(var3);
         } catch (Throwable var7) {
            try {
               var3.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }

            throw var7;
         }

         var3.close();
         String var1;
         if (var2 != null) {
            var1 = toHex(var2);
         } else {
            var1 = BaseEncoding.base16().lowerCase().encode("null".getBytes());
         }

         return var1;
      } catch (Exception var8) {
         throw new RuntimeException(var8);
      }
   }

   private String readResourceWith(String var1, Function var2) {
      if (!var1.startsWith("/")) {
         var1 = "/" + var1;
      }

      InputStream var3 = this.getClass().getResourceAsStream(var1);
      if (var3 != null) {
         String var4 = new BufferedReader(new InputStreamReader(var3, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
         return (String)var2.apply(var4);
      } else {
         throw new FileNotFoundException("Corrupted JAR! Can't find '" + var1 + "' file inside the JAR!");
      }
   }

   private String readResourceText(String var1) {
      if (!var1.startsWith("/")) {
         var1 = "/" + var1;
      }

      InputStream var2 = this.getClass().getResourceAsStream(var1);
      if (var2 != null) {
         return new BufferedReader(new InputStreamReader(var2, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
      } else {
         throw new FileNotFoundException("Corrupted JAR! Can't find '" + var1 + "' file inside the JAR!");
      }
   }

   public void generate(@Nullable Consumer var1, @Nullable DoubleCallback var2) {
      this.startGeneration(var1, var2, null);
   }

   public void startGeneration(@Nullable Consumer var1, @Nullable DoubleCallback var2, @Nullable Runnable var3) {
      this.lastFailure = null;
      ContentManager var4 = ContentManager.method8084();
      PackBuildContext var5 = PackBuildContext.method126(var4, this);
      long var6 = var4.method8114();
      Settings var8 = Settings.get();
      boolean var9 = Settings.bI();
      boolean var10 = var4.method8162();
      Map var11 = Map.copyOf(var4.ab());
      Map var12 = Map.copyOf(var4.ae);
      ArrayList var13 = new ArrayList();
      List var14 = List.copyOf(var4.ac());
      GeneratedAssets var15 = var4.field85.getAssets();
      Function var16 = var4.method8197();
      LinkedHashMap var17 = new LinkedHashMap();
      ArrayList var18 = new ArrayList();
      CustomEntityManager.getInstance().oN.values().forEach(var2x -> {
         if (var2x.vn.nX() != null) {
            var13.add(var2x.vn.nX());
         }

         Path var3x = var2x.vn.method472();
         if (var3x != null) {
            String var4x = var2x.vn.getNamespace();
            if (!var4x.matches("[a-z0-9._-]+") || var4x.equals(".") || var4x.equals("..")) {
               throw new IllegalArgumentException("Invalid embedded texture namespace: " + var4x);
            }

            var18.add(new PackSourceEntry(var4x, var3x, var2x.vn.on()));
         }
      });
      FontImageLoader.Gl().forEach((var1x, var2x) -> var17.put(var1x, List.copyOf(var2x)));
      File var19 = FontImageLoader.Gj();
      YamlConfig var20 = FontImageLoader.Gk();
      this.generated = false;
      this.generationRunning = true;
      if (!this.generationRunner
         .tryStart(
            () -> {
               Log.log(ChatColor.AQUA + "[Pack] Compressing resource_pack ... ");
               boolean var21 = false;
               Throwable var22 = null;
               DoubleCallback var23 = var2 == null ? null : var2xx -> this.generationRunner.runIfNotCancelled(() -> var2.run(var2xx));
               Path var24 = Utils.toPath(ContentManager.field72);
               Path var25 = var24.resolveSibling(var24.getFileName() + "." + UUID.randomUUID() + ".tmp");
               Path var26 = null;

               try {
                  try {
                     BossBarManager.setTitleAndProgress("Preparing pack: version overlays...", 0.0);
                     this.writer = new ResourcePackWriter(var25.toString(), this.ignoredExtensions, var5);
                     if (!this.bAx.containsKey("overlays")) {
                        this.bAx.put("overlays", new JSONObject());
                     }

                     if (!this.bAx.getJSONObject("overlays").containsKey("entries")) {
                        this.bAx.getJSONObject("overlays").put("entries", new JSONArray());
                     }

                     if (var8.emoteLegacyShadersPre1214) {
                        JSONObject var27 = new JSONObject();
                        var27.put("directory", "ia_overlay_legacy_emotes");
                        setFormatsRange(var27, ServerCompat.packVersionOf(Version.v1_20_6), ServerCompat.packVersionOf(Version.v1_21_4));
                        this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var27);
                        JSONObject var28 = new JSONObject();
                        var28.put("directory", "ia_overlay_legacy_emotes_1_21_2_to_4");
                        setFormatsRange(var28, ServerCompat.packVersionOf(Version.v1_21_3), ServerCompat.packVersionOf(Version.v1_21_4));
                        this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var28);
                     }

                     JSONObject var227 = new JSONObject();
                     var227.put("directory", "ia_overlay_1_21_2_plus");
                     setFormatsRange(var227, ServerCompat.packVersionOf(Version.v1_21_3), 9999);
                     this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var227);
                     var227 = new JSONObject();
                     var227.put("directory", "ia_overlay_1_21_4_to_5");
                     setFormatsRange(var227, ServerCompat.packVersionOf(Version.v1_21_4), ServerCompat.packVersionOf(Version.v1_21_5));
                     this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var227);
                     var227 = new JSONObject();
                     var227.put("directory", "ia_overlay_1_21_4_plus");
                     setFormatsRange(var227, ServerCompat.packVersionOf(Version.v1_21_4), 9999);
                     this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var227);
                     var227 = new JSONObject();
                     var227.put("directory", "ia_overlay_1_21_5");
                     setFormatsRange(var227, ServerCompat.packVersionOf(Version.v1_21_5), ServerCompat.packVersionOf(Version.v1_21_5));
                     this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var227);
                     var227 = new JSONObject();
                     var227.put("directory", "ia_overlay_1_21_6_to_11");
                     setFormatsRange(var227, ServerCompat.packVersionOf(Version.v1_21_6), ServerCompat.packVersionOf(Version.v1_21_11));
                     this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var227);
                     var227 = new JSONObject();
                     var227.put("directory", "ia_overlay_1_21_6_plus");
                     setFormatsRange(var227, ServerCompat.packVersionOf(Version.v1_21_6), 9999);
                     this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var227);
                     var227 = new JSONObject();
                     var227.put("directory", "ia_overlay_1_21_9_to_11");
                     setFormatsRange(var227, 69, ServerCompat.packVersionOf(Version.v1_21_11));
                     this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var227);
                     var227 = new JSONObject();
                     var227.put("directory", "ia_overlay_modern_atlas");
                     setFormatsRange(var227, ServerCompat.packVersionOf(Version.v1_21_11), 9999);
                     this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var227);
                     var227 = new JSONObject();
                     var227.put("directory", "ia_overlay_1_20_blockstates");
                     setFormatsRange(var227, ServerCompat.packVersionOf(Version.v1_20_R2), 9999);
                     this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var227);
                     var227 = new JSONObject();
                     var227.put("directory", "ia_overlay_26_1_blockstates");
                     setFormatsRange(var227, ServerCompat.packVersionOf(Version.v26_1_1), 9999);
                     this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var227);
                     var227 = new JSONObject();
                     var227.put("directory", "ia_overlay_26_1_plus");
                     setFormatsRange(var227, ServerCompat.packVersionOf(Version.v26_1_2), 9999);
                     this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var227);
                     var227 = new JSONObject();
                     var227.put("directory", "ia_overlay_26_2_plus");
                     setFormatsRange(var227, 88, 9999);
                     this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var227);
                     var227 = new JSONObject();
                     var227.put("directory", "ia_overlay_26_3_plus");
                     setFormatsAndMinMax(var227, 97, 1, 9999);
                     this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var227);
                     BossBarManager.setTitleKeepProgress("Preparing pack: generating " + this.materialModelJsons.size() + " modern item definitions...");
                     this.materialModelJsons.forEach((var1xx, var2xx) -> {
                        new OverlayPackGenerator().addOverlayEntry(this.overlayEntriesByRange, OverlayVersionRange.v_1_21_4_to_5, var1xx, var2xx);
                        new OverlayPackGenerator().addOverlayEntry(this.overlayEntriesByRange, OverlayVersionRange.v_1_21_6_plus, var1xx, var2xx);
                        new OverlayPackGenerator().addOverlayEntry(this.overlayEntriesByRange, OverlayVersionRange.v_26_1_plus, var1xx, var2xx);
                     });
                     HashMap var240 = (HashMap)this.overlayEntriesByRange.get(OverlayVersionRange.v_1_21_6_plus);
                     HashMap var241 = (HashMap)this.overlayEntriesByRange.get(OverlayVersionRange.v_26_1_plus);
                     var240.forEach((var1xx, var2xx) -> {
                        if (var241.containsKey(var1xx)) {
                           VersionedPackEntry var3xx = (VersionedPackEntry)var241.get(var1xx);
                           if (var2xx.hash == var3xx.hash) {
                              var241.remove(var1xx);
                           }
                        }
                     });
                     BossBarManager.setTitleKeepProgress("Preparing pack: text shaders...");
                     if (var9) {
                        try {
                           String var29 = this.readResourceWith(
                              "internal/shaders/text_effects/legacy/rendertype_text.vsh", ResourcePackGenerator::stripDebugSections
                           );
                           this.writer.addText(var29, "assets/minecraft/shaders/core/rendertype_text.vsh");
                           String var30 = this.readResourceWith(
                              "internal/shaders/text_effects/ia_overlay_1_21_6_to_11/rendertype_text.vsh", ResourcePackGenerator::stripDebugSections
                           );
                           this.writer.addText(var30, "ia_overlay_1_21_6_to_11/assets/minecraft/shaders/core/rendertype_text.vsh");
                           var30 = this.readResourceWith(
                              "internal/shaders/text_effects/ia_overlay_26_1_plus/rendertype_text.vsh", ResourcePackGenerator::stripDebugSections
                           );
                           this.writer.addText(var30, "ia_overlay_26_1_plus/assets/minecraft/shaders/core/rendertype_text.vsh");
                           var30 = this.readResourceWith(
                              "internal/shaders/text_effects/ia_overlay_26_2_plus/text.vsh", ResourcePackGenerator::stripDebugSections
                           );
                           this.writer.addText(var30, "ia_overlay_26_2_plus/assets/minecraft/shaders/core/text.vsh");
                           var30 = this.readResourceWith(
                              "internal/shaders/text_effects/ia_overlay_26_3_plus/text.vsh", ResourcePackGenerator::stripDebugSections
                           );
                           this.writer.addText(var30, "ia_overlay_26_3_plus/assets/minecraft/shaders/core/text.vsh");
                           this.writer
                              .addString("internal/shaders/text_effects/legacy/rendertype_text.json", "assets/minecraft/shaders/core/rendertype_text.json");
                           this.writer
                              .addString(
                                 "internal/shaders/text_effects/ia_overlay_1_21_2_plus/rendertype_text.json",
                                 "ia_overlay_1_21_2_plus/assets/minecraft/shaders/core/rendertype_text.json"
                              );
                        } catch (FileNotFoundException var220) {
                           Log.error("Error replacing placeholders in shader.", var220);
                        }
                     }

                     String var242 = null;
                     String var246 = null;
                     String var31 = null;
                     String var32 = null;
                     BossBarManager.setTitleKeepProgress("Preparing pack: armor shaders and textures...");
                     if (!var8.armorShadersEnabled) {
                        this.addLeatherArmorLayers(this.writer);
                        if (var10) {
                           Log.warn("Found custom armors but you disabled `generate-custom-armors-textures`. Armors might not show.");
                        }
                     } else {
                        if (this.armorTextureBuilder.isGenerated()) {
                           if (var8.armorGenShaderPre1214) {
                              String var33 = this.readResourceWith(
                                 "internal/shaders/armors/legacy/rendertype_armor_cutout_no_cull.fsh",
                                 var1xx -> var1xx.replace("%TEX_RES%", String.valueOf(this.armorTextureBuilder.bHO))
                              );
                              this.writer.addText(var33, "assets/minecraft/shaders/core/rendertype_armor_cutout_no_cull.fsh");
                              this.writer
                                 .addString(
                                    "internal/shaders/armors/legacy/rendertype_armor_cutout_no_cull.json",
                                    "assets/minecraft/shaders/core/rendertype_armor_cutout_no_cull.json"
                                 );
                              this.writer
                                 .addString(
                                    "internal/shaders/armors/legacy/rendertype_armor_cutout_no_cull.vsh",
                                    "assets/minecraft/shaders/core/rendertype_armor_cutout_no_cull.vsh"
                                 );
                              this.writer
                                 .addString(
                                    "internal/shaders/armors/ia_overlay_1_21_2_plus/rendertype_armor_cutout_no_cull.json",
                                    "ia_overlay_1_21_2_plus/assets/minecraft/shaders/core/rendertype_armor_cutout_no_cull.json"
                                 );
                           }

                           if (var8.armorGenShader1215Plus) {
                              var242 = this.readResourceWith("internal/shaders/armors_emotes/ia_overlay_1_21_5/entity.fsh", this::replaceTextureTokens);
                              var246 = this.readResourceWith("internal/shaders/armors_emotes/ia_overlay_1_21_6_plus/entity.fsh", this::replaceTextureTokens);
                              var31 = this.applyTextureTokens(this.readResourceText("internal/shaders/armors_emotes/ia_overlay_26_1_plus/entity.fsh"), true);
                              var32 = this.applyTextureTokens(this.readResourceText("internal/shaders/armors_emotes/ia_overlay_26_3_plus/entity.fsh"), true);
                           }

                           this.writer.addBuffer(this.armorTextureBuilder.bHR, "assets/minecraft/textures/models/armor/leather_layer_1.png");
                           this.writer.addBuffer(this.armorTextureBuilder.bHS, "assets/minecraft/textures/models/armor/leather_layer_2.png");
                           this.writer
                              .addBufferEntry(
                                 this.armorTextureBuilder.bHR, "ia_overlay_1_21_2_plus/assets/minecraft/textures/entity/equipment/humanoid/leather.png", true
                              );
                           this.writer
                              .addBufferEntry(
                                 this.armorTextureBuilder.bHS,
                                 "ia_overlay_1_21_2_plus/assets/minecraft/textures/entity/equipment/humanoid_leggings/leather.png",
                                 true
                              );
                           this.writer
                              .addString(
                                 "internal/textures/models/armor/leather_layer_1_overlay.png",
                                 "assets/minecraft/textures/models/armor/leather_layer_1_overlay.png"
                              );
                           this.writer
                              .addString(
                                 "internal/textures/models/armor/leather_layer_2_overlay.png",
                                 "assets/minecraft/textures/models/armor/leather_layer_2_overlay.png"
                              );
                           this.writer
                              .addString(
                                 "internal/textures/models/armor/leather_layer_1_overlay.png",
                                 "ia_overlay_1_21_2_plus/assets/minecraft/textures/entity/equipment/humanoid/leather_overlay.png"
                              );
                           this.writer
                              .addString(
                                 "internal/textures/models/armor/leather_layer_2_overlay.png",
                                 "ia_overlay_1_21_2_plus/assets/minecraft/textures/entity/equipment/humanoid_leggings/leather_overlay.png"
                              );
                        } else {
                           String var249 = this.armorTextureBuilder.bHR == null
                              ? "assets/minecraft/textures/models/armor/leather_layer_1.png"
                              : "assets/minecraft/textures/models/armor/leather_layer_2.png";
                           Log.error("Failed to generate custom armor atlas: '" + var249 + "' was not generated.");
                        }

                        if (var8.armorGenOptifineCit) {
                           this.armorTextureBuilder.bHZ.forEach((var1xx, var2xx) -> this.writer.addBuffer(var2xx, var1xx));
                        }

                        for (String var34 : this.armorTextureBuilder.bIa) {
                           this.writer.gu(var34);
                        }

                        this.writer.gu("assets/minecraft/textures/models/armor/leather_layer_1.png");
                        this.writer.gu("assets/minecraft/textures/models/armor/leather_layer_2.png");
                        this.writer.gu("assets/minecraft/textures/models/armor/leather_layer_1_overlay.png");
                        this.writer.gu("assets/minecraft/textures/models/armor/leather_layer_2_overlay.png");
                        this.writer.gu("assets/minecraft/textures/entity/equipment/humanoid/leather.png");
                        this.writer.gu("assets/minecraft/textures/entity/equipment/humanoid_leggings/leather.png");
                        this.writer.addString("internal/emissive.properties", "assets/minecraft/optifine/emissive.properties");
                        this.armorTextureBuilder.clear();
                     }

                     this.scrollGuiRegistry.XR().forEach((var1xx, var2xx) -> this.writer.addBytesTracked(var2xx, var1xx));
                     BossBarManager.setTitleKeepProgress("Preparing pack: emote models and shaders...");
                     if (var8.emotesEnabled) {
                        for (String var36 : new String[]{"parm_left_3", "parm_right_4", "pbody_2", "phead_0", "pleg_left_1", "pleg_right_5", "sus_6"}) {
                           writePlayerModel(this.writer, var36, var36);
                        }

                        for (int var252 = 0; var252 < 6; var252++) {
                           writePlayerModel(this.writer, "sus_6", "dummy_" + var252);
                        }

                        if (var8.emoteLegacyShadersPre1214) {
                           this.writer
                              .addString(
                                 "internal/shaders/emotes/legacy/rendertype_entity_translucent.fsh",
                                 "ia_overlay_legacy_emotes/assets/minecraft/shaders/core/rendertype_entity_translucent.fsh"
                              );
                           this.writer
                              .addString(
                                 "internal/shaders/emotes/ia_overlay_1_20_5_plus/rendertype_entity_translucent.vsh",
                                 "ia_overlay_legacy_emotes/assets/minecraft/shaders/core/rendertype_entity_translucent.vsh"
                              );
                           this.writer
                              .addString(
                                 "internal/shaders/emotes/legacy/rendertype_entity_translucent.json",
                                 "ia_overlay_legacy_emotes/assets/minecraft/shaders/core/rendertype_entity_translucent.json"
                              );
                           this.writer
                              .addString(
                                 "internal/shaders/emotes/ia_overlay_1_21_2_plus/rendertype_entity_translucent.json",
                                 "ia_overlay_legacy_emotes_1_21_2_to_4/assets/minecraft/shaders/core/rendertype_entity_translucent.json"
                              );
                        }

                        if (var8.emoteShaders1215To1219) {
                           if (var242 == null) {
                              var242 = this.readResourceText("internal/shaders/armors_emotes/ia_overlay_1_21_5/entity.fsh");
                           }

                           if (var246 == null) {
                              var246 = this.readResourceText("internal/shaders/armors_emotes/ia_overlay_1_21_6_plus/entity.fsh");
                           }

                           if (var31 == null) {
                              var31 = this.applyTextureTokens(this.readResourceText("internal/shaders/armors_emotes/ia_overlay_26_1_plus/entity.fsh"), false);
                           }

                           if (var32 == null) {
                              var32 = this.applyTextureTokens(this.readResourceText("internal/shaders/armors_emotes/ia_overlay_26_3_plus/entity.fsh"), false);
                           }
                        }

                        if (var8.emoteModernMethod) {
                           this.writer
                              .addString(
                                 "internal/player/1_21_4_plus/_iainternal/textures/item/player_model/player_model_pixel.png",
                                 "assets/_iainternal/textures/item/player_model/player_model_pixel.png"
                              );
                           Map var253 = new GuiAssetGenerator(!var8.emotesUseArmorStands).generateAssets();

                           for (Entry var266 : var253.entrySet()) {
                              this.writer.addBytesTracked((byte[])var266.getValue(), "assets/" + Utils.normalizeSlashes((String)var266.getKey()));
                           }
                        }
                     }

                     if (var242 != null) {
                        if (!var8.armorGenShader1215Plus) {
                           var242 = var242.replaceAll("(?s)//%START_CUSTOM_ARMORS_VARS%.*?//%END_CUSTOM_ARMORS_VARS%", "")
                              .replaceAll("(?s)//%START_CUSTOM_ARMORS%.*?//%END_CUSTOM_ARMORS%", "");
                        }

                        String var254 = this.readResourceText("internal/shaders/armors_emotes/ia_overlay_1_21_5/entity.vsh");
                        if (!var8.emotesEnabled || !var8.emoteShaders1215To1219) {
                           var254 = var254.replaceAll("(?s)//%START_EMOTES%.*?//%END_EMOTES%", "");
                        }

                        this.writer.addText(var242, "ia_overlay_1_21_5/assets/minecraft/shaders/core/entity.fsh");
                        this.writer.addText(var254, "ia_overlay_1_21_5/assets/minecraft/shaders/core/entity.vsh");
                     }

                     if (var246 != null) {
                        if (!var8.armorGenShader1215Plus) {
                           var246 = var246.replaceAll("(?s)//%START_CUSTOM_ARMORS_VARS%.*?//%END_CUSTOM_ARMORS_VARS%", "")
                              .replaceAll("(?s)//%START_CUSTOM_ARMORS%.*?//%END_CUSTOM_ARMORS%", "");
                        }

                        String var255 = this.readResourceText("internal/shaders/armors_emotes/ia_overlay_1_21_6_plus/entity.vsh");
                        if (!var8.emotesEnabled || !var8.emoteShaders1215To1219) {
                           var255 = var255.replaceAll("(?s)//%START_EMOTES%.*?//%END_EMOTES%", "");
                        }

                        this.writer.addText(var246, "ia_overlay_1_21_6_plus/assets/minecraft/shaders/core/entity.fsh");
                        this.writer.addText(var255, "ia_overlay_1_21_6_plus/assets/minecraft/shaders/core/entity.vsh");
                        this.writer.addText(stripEmoteSections(var246, false), "ia_overlay_1_21_9_to_11/assets/minecraft/shaders/core/entity.fsh");
                        this.writer.addText(stripEmoteSections(var255, false), "ia_overlay_1_21_9_to_11/assets/minecraft/shaders/core/entity.vsh");
                        String var261 = this.readResourceText("internal/shaders/armors_emotes/ia_overlay_26_1_plus/entity.vsh");
                        if (!var8.armorGenShader1215Plus) {
                           var31 = var31.replaceAll("(?s)//%START_CUSTOM_ARMORS_VARS%.*?//%END_CUSTOM_ARMORS_VARS%", "")
                              .replaceAll("(?s)//%START_CUSTOM_ARMORS%.*?//%END_CUSTOM_ARMORS%", "");
                        }

                        var261 = stripEmoteSections(var261, false);
                        var31 = stripEmoteSections(var31, false);
                        this.writer.addText(var31, "ia_overlay_26_1_plus/assets/minecraft/shaders/core/entity.fsh");
                        this.writer.addText(var261, "ia_overlay_26_1_plus/assets/minecraft/shaders/core/entity.vsh");
                        String var267 = this.readResourceText("internal/shaders/armors_emotes/ia_overlay_26_3_plus/entity.vsh");
                        if (!var8.armorGenShader1215Plus) {
                           var32 = var32.replaceAll("(?s)//%START_CUSTOM_ARMORS_VARS%.*?//%END_CUSTOM_ARMORS_VARS%", "")
                              .replaceAll("(?s)//%START_CUSTOM_ARMORS%.*?//%END_CUSTOM_ARMORS%", "");
                        }

                        var267 = stripEmoteSections(var267, false);
                        var32 = stripEmoteSections(var32, false);
                        this.writer.addText(var32, "ia_overlay_26_3_plus/assets/minecraft/shaders/core/entity.fsh");
                        this.writer.addText(var267, "ia_overlay_26_3_plus/assets/minecraft/shaders/core/entity.vsh");
                     }

                     BossBarManager.setTitleKeepProgress("Preparing pack: generated models, textures and languages...");
                     if (var8.hideSlotHighlight) {
                        this.writer
                           .addString(
                              "internal/textures/item/empty.png",
                              "ia_overlay_1_21_4_plus/assets/minecraft/textures/gui/sprites/container/slot_highlight_back.png"
                           );
                     }

                     this.writer.addString("internal/textures/item/empty.png", "assets/minecraft/textures/item/empty.png");
                     this.writer.addString("internal/textures/char/black.png", "assets/minecraft/textures/char/black.png");
                     this.writer.addString("internal/textures/item/iamissing.png", "assets/minecraft/textures/item/iamissing.png");
                     this.writer
                        .addString("internal/models/item/base/furniture_big_painting.json", "assets/minecraft/models/item/base/furniture_big_painting.json");
                     this.writer
                        .addString("internal/models/item/base/furniture_long_painting.json", "assets/minecraft/models/item/base/furniture_long_painting.json");
                     this.writer.addString("internal/models/item/base/furniture_painting.json", "assets/minecraft/models/item/base/furniture_painting.json");
                     this.writer
                        .addString("internal/models/item/base/furniture_tall_painting.json", "assets/minecraft/models/item/base/furniture_tall_painting.json");
                     this.writer.addString("internal/models/block/base/block_tile.json", "assets/minecraft/models/block/base/block_tile.json");
                     this.writer
                        .addString(
                           "internal/models/block/original/brown_mushroom_block.json", "assets/minecraft/models/block/original/brown_mushroom_block.json"
                        );
                     this.writer.addString("internal/models/block/original/chorus_plant.json", "assets/minecraft/models/block/original/chorus_plant.json");
                     this.writer.addString("internal/models/block/original/mushroom_stem.json", "assets/minecraft/models/block/original/mushroom_stem.json");
                     this.writer.addString("internal/models/block/original/note_block.json", "assets/minecraft/models/block/original/note_block.json");
                     this.writer
                        .addString("internal/models/block/original/red_mushroom_block.json", "assets/minecraft/models/block/original/red_mushroom_block.json");
                     this.writer.addString("internal/models/block/crop_cross.json", "assets/minecraft/models/block/crop_cross.json");
                     this.writer.addString("internal/models/block/spawner.json", "assets/minecraft/models/block/spawner.json");
                     this.writer.addString("internal/models/block/crop_billboard.json", "assets/minecraft/models/block/crop_billboard.json");
                     this.materialModelJsons.forEach((var2xx, var3xx) -> {
                        if (var8.fixMissingParticle) {
                           fixMissingParticle(var3xx);
                        }

                        this.writer.addJsonUnchanged(var3xx, "assets/minecraft/models/item/" + var2xx + ".json");
                     });
                     this.manifestJsons.forEach((var1xx, var2xx) -> this.writer.addModelJson(var2xx, var1xx));
                     this.furnitureItemJsons
                        .keySet()
                        .forEach(var1xx -> this.writer.addSuppliedJson(() -> (JSONObject)this.furnitureItemJsons.get(var1xx), var1xx));
                     this.bzP.forEach((var1xx, var2xx) -> this.writer.addStreamEntry(new ByteArrayInputStream(var2xx), var1xx, false));
                     this.blockstateOverlays.forEach((var1xx, var2xx) -> this.writer.addJsonUnchanged(var2xx, var1xx));
                     this.bAo.forEach((var1xx, var2xx) -> this.writer.addStringBytes(var2xx, "assets/minecraft/lang/" + var1xx + ".json", false));
                     this.writer.addJsonAsText(this.packArraysJson, "assets/minecraft/font/default.json");
                     if (var8.guiScrollable || var8.guiScrollableInventoryTitle) {
                        JsonAssetWithCallbacks.loadWithCallbacks(
                           "internal/inventory_title/manifest.json", var1xx -> var1xx.asObject().forEach((var1xxx, var2xx) -> {
                              String var3xx = (String)var2xx;
                              this.writer.addString("internal/inventory_title/" + var1xxx, var3xx);
                              if (var1xxx.startsWith("1_21_4/")) {
                                 String var4xx = var3xx.substring("ia_overlay_1_21_4_plus/".length());
                                 this.writer.addString("internal/inventory_title/" + var1xxx, var4xx);
                              }
                           }), var0 -> {
                              throw new IllegalStateException("Missing inventory caption font assets", var0.getError());
                           }
                        );
                     }

                     if (var8.fontImagesWithForceUnicode) {
                        this.writer.addJsonAsText(this.packArraysJson, "assets/minecraft/font/uniform.json");
                     }

                     this.writer.addString("internal/block_outline/block_outline.json", "assets/minecraft/items/block_outline.json");
                     this.writer.addString("internal/block_outline/block_outline_model.json", "assets/minecraft/models/item/block_outline.json");
                     this.writer
                        .addString(
                           "internal/block_outline/block_outline_model_translucent.json",
                           "ia_overlay_1_21_4_plus/assets/minecraft/models/item/block_outline.json"
                        );
                     this.writer.addString("internal/block_outline/block_outline.png", "assets/minecraft/textures/item/block_outline.png");
                     BossBarManager.setTitleKeepProgress("Preparing pack: equipment and tooltip assets...");
                     if (Version.isAtLeast(Version.v1_21_3)) {
                        var11.forEach(
                           (var2xx, var3xx) -> {
                              Log.log("###### Generating equipment: " + var2xx);
                              JSONObject var4xx = var3xx.EW();
                              if (var3xx.EV()) {
                                 File var14xx = (File)var16.apply(ContentManager.method8194(var3xx.EX().getNamespace(), var3xx.Fa()));
                                 if (var14xx == null) {
                                    Log.error("Failed to find equipment wings texture for " + var2xx + ". This is probably a bug.");
                                 } else {
                                    this.writer
                                       .addFile(
                                          var14xx,
                                          "ia_overlay_1_21_2_plus/assets/"
                                             + var2xx.getNamespace()
                                             + "/textures/entity/equipment/wings/"
                                             + var2xx.getKey()
                                             + ".png",
                                          false
                                       );
                                    this.writer
                                       .addJsonUnchanged(
                                          var4xx, "ia_overlay_1_21_2_plus/assets/" + var2xx.getNamespace() + "/models/equipment/" + var2xx.getKey() + ".json"
                                       );
                                    this.writer
                                       .addJsonUnchanged(
                                          var4xx, "ia_overlay_1_21_4_plus/assets/" + var2xx.getNamespace() + "/equipment/" + var2xx.getKey() + ".json"
                                       );
                                 }
                              } else {
                                 File var5xx = (File)var16.apply(ContentManager.method8194(var3xx.EX().getNamespace(), var3xx.EY()));
                                 File var6xx = (File)var16.apply(ContentManager.method8194(var3xx.EX().getNamespace(), var3xx.EZ()));
                                 if (var5xx != null && var6xx != null) {
                                    this.writer
                                       .addFile(
                                          var5xx,
                                          "ia_overlay_1_21_2_plus/assets/"
                                             + var2xx.getNamespace()
                                             + "/textures/entity/equipment/humanoid/"
                                             + var2xx.getKey()
                                             + ".png",
                                          false
                                       );
                                    this.writer
                                       .addFile(
                                          var6xx,
                                          "ia_overlay_1_21_2_plus/assets/"
                                             + var2xx.getNamespace()
                                             + "/textures/entity/equipment/humanoid_leggings/"
                                             + var2xx.getKey()
                                             + ".png",
                                          false
                                       );
                                    this.writer
                                       .addJsonUnchanged(
                                          var4xx, "ia_overlay_1_21_2_plus/assets/" + var2xx.getNamespace() + "/models/equipment/" + var2xx.getKey() + ".json"
                                       );
                                    this.writer
                                       .addJsonUnchanged(
                                          var4xx, "ia_overlay_1_21_4_plus/assets/" + var2xx.getNamespace() + "/equipment/" + var2xx.getKey() + ".json"
                                       );

                                    for (PackFileEntry var8xx : this.packFileEntries) {
                                       File var9xx = var8xx.toFile();
                                       if (var9xx.equals(var5xx)) {
                                          String var10xx = var8xx.getTexturePath();
                                          Log.devLog("----Found used model texture: " + var8xx.bIF + " -> " + var10xx);

                                          try {
                                             this.writer.addStreamEntry(FileUtils.openInputStream(var5xx), var10xx, false);
                                          } catch (IOException var12xx) {
                                             var12xx.printStackTrace();
                                          }
                                       } else if (var9xx.equals(var6xx)) {
                                          String var15xx = var8xx.getTexturePath();
                                          Log.devLog("----Found used model texture: " + var8xx.bIF + " -> " + var15xx);

                                          try {
                                             this.writer.addStreamEntry(FileUtils.openInputStream(var6xx), var15xx, false);
                                          } catch (IOException var13xx) {
                                             var13xx.printStackTrace();
                                          }
                                       }
                                    }
                                 } else {
                                    Log.error("Failed to find equipment texture for " + var2xx + ". This may be an ItemsAdder bug.");
                                 }
                              }
                           }
                        );
                        var14.forEach(
                           var2xx -> {
                              File var3xx = (File)var16.apply(ContentManager.method8194(null, var2xx + "_background"));
                              File var4xx = (File)var16.apply(ContentManager.method8194(null, var2xx + "_frame"));
                              if (var3xx != null && var4xx != null) {
                                 Log.log(ChatColor.AQUA + "###### Generating tooltip: " + var2xx);
                                 this.writer
                                    .addFile(
                                       var3xx,
                                       "ia_overlay_1_21_2_plus/assets/"
                                          + var2xx.getNamespace()
                                          + "/textures/gui/sprites/tooltip/"
                                          + var2xx.getKey()
                                          + "_background.png",
                                       false
                                    );
                                 this.writer
                                    .addFile(
                                       var4xx,
                                       "ia_overlay_1_21_2_plus/assets/"
                                          + var2xx.getNamespace()
                                          + "/textures/gui/sprites/tooltip/"
                                          + var2xx.getKey()
                                          + "_frame.png",
                                       false
                                    );
                                 File var5xx = (File)var16.apply(ContentManager.method8194(null, var2xx + "_background") + ".mcmeta");
                                 if (var5xx != null) {
                                    this.writer
                                       .addFile(
                                          var5xx,
                                          "ia_overlay_1_21_2_plus/assets/"
                                             + var2xx.getNamespace()
                                             + "/textures/gui/sprites/tooltip/"
                                             + var2xx.getKey()
                                             + "_background.png.mcmeta",
                                          false
                                       );
                                 }

                                 File var6xx = (File)var16.apply(ContentManager.method8194(null, var2xx + "_frame") + ".mcmeta");
                                 if (var6xx != null) {
                                    this.writer
                                       .addFile(
                                          var6xx,
                                          "ia_overlay_1_21_2_plus/assets/"
                                             + var2xx.getNamespace()
                                             + "/textures/gui/sprites/tooltip/"
                                             + var2xx.getKey()
                                             + "_frame.png.mcmeta",
                                          false
                                       );
                                 }
                              } else {
                                 Log.error("Failed to find tooltip background texture for " + var2xx + ". This may be an ItemsAdder bug.");
                              }
                           }
                        );
                     }

                     BossBarManager.setTitleKeepProgress("Preparing pack: collecting referenced textures...");
                     this.packFileEntries.forEach(var1xx -> {
                        File var2xx = var1xx.toFile();
                        if (var2xx.exists()) {
                           this.writer.addFile(var2xx, var1xx.getTexturePath(), false);
                        }
                     });
                     if (var18.stream().anyMatch(PackSourceEntry::geometry)) {
                        this.addOverlayFormatEntry("ia_overlay_v2_1_21_6_plus", ServerCompat.packVersionOf(Version.v1_21_6), 9999);
                        this.addOverlayFormatEntry("ia_overlay_v2_modern_atlas", ServerCompat.packVersionOf(Version.v1_21_11), 9999);
                     }

                     this.writer.addJsonUnchanged(this.bAx, "pack.mcmeta");
                     this.overlayEntriesByRange
                        .forEach(
                           (var1xx, var2xx) -> var2xx.forEach(
                              (var2xxx, var3xx) -> this.writer
                                 .addJsonUnchanged(var3xx.json, var1xx.getOverlayKey() + "/assets/minecraft/items/" + var2xxx + ".json")
                           )
                        );
                     BossBarManager.setTitleKeepProgress("Preparing pack: scanning remaining files...");
                     this.writer.addResourceFiles();
                     if (!var18.isEmpty()) {
                        Files.createDirectories(var24.getParent());
                        var26 = Files.createTempDirectory(var24.getParent(), "entity-v2-textures-");

                        for (PackSourceEntry var263 : var18) {
                           AnimationBinaryPayload var269 = AnimationBinaryCodec.method9161(Files.readAllBytes(var263.source()));
                           String var271 = var269.vH().getJSONObject("entity").getString("namespace");
                           if (var271 == null || !var271.matches("[a-z0-9._-]+") || var271.equals(".") || var271.equals("..")) {
                              throw new IllegalArgumentException("Invalid embedded texture namespace: " + var271);
                           }

                           Path var37 = var26.resolve(var271);
                           Files.createDirectories(var37);

                           for (Entry var39 : AnimationDataCompiler.method9222(var269, var37).entrySet()) {
                              this.writer
                                 .addFileInternal(
                                    ((Path)var39.getValue()).toFile(), "assets/" + var271 + "/textures/ia_v2/" + (String)var39.getKey(), false, true
                                 );
                           }

                           for (AnimationSourceEntry var276 : AnimationJsonBuilder.method9196(var269)) {
                              this.writer.addJsonDefault(var276.vK(), var276.vJ());
                           }
                        }
                     }

                     for (PngImage var264 : var15.textures().values()) {
                        this.writer.addBytesTracked(var264.png(), GeneratedAssetStore.particleTexturePath(var264));
                     }

                     var15.models().forEach((var1xx, var2xx) -> {
                        this.writer.addJsonRaw(var2xx, var1xx, true);
                        if (var1xx.contains("/models/")) {
                           this.putAtlasJson(var1xx, var2xx);
                        } else if (var1xx.contains("/items/")) {
                           this.atlasModelRefs.put(var1xx, new TreeSet(ListCollector.collectModelNames(var2xx)));
                        }
                     });
                     this.modelSourceCache.clear();
                     BossBarManager.setTitleKeepProgress("Preparing pack: planning modern atlases...");
                     this.cleanupStaleModelCaches();
                     if (this.itemContextIndex != null) {
                        this.itemContextIndex.toSortedMap().forEach((var1xx, var2xx) -> {
                           String var3xx = "ia_overlay_modern_atlas/" + ItemContextBuilder.modelJsonPath(var1xx);
                           this.writer.addJsonRaw(this.getPlannedModel(var3xx), var3xx, true);
                        });
                        var240.forEach(
                           (var1xx, var2xx) -> this.writer
                              .addJsonRaw(this.rewriteModelRefs(var2xx.json), "ia_overlay_modern_atlas/assets/minecraft/items/" + var1xx + ".json", true)
                        );
                        var241.forEach((var1xx, var2xx) -> {
                           JSONObject var3xx = this.rewriteModelRefs(var2xx.json);
                           var2xx.json.clear();
                           var2xx.json.putAll(var3xx);
                           var2xx.hash = var2xx.json.hashCode();
                        });
                     }

                     BossBarManager.setTitleKeepProgress("Preparing pack: writing atlas metadata...");
                     this.removeDuplicateAtlasSources();
                     this.atlasSources.forEach((var1xx, var2xx) -> {
                        JSONObject var3xx = this.dedupeAtlasSources(var2xx);
                        this.writer.addJsonUnchanged(var3xx, "ia_overlay_modern_atlas/assets/minecraft/atlases/" + var1xx);
                     });
                     this.writer.addJsonUnchanged(this.dedupeAtlasSources(this.blocksAtlas), "assets/minecraft/atlases/blocks.json");
                     if (!var13.isEmpty() && var8.customEntitiesHumanoidEquipment && Version.isAtLeast(Version.v1_21_4)) {
                        if (var4.aL.manager.isPerClientDeliveryActive()) {
                           this.buildHumanoidEquipment(var13, var11, var12, var16, List.copyOf(var4.field87));
                        } else {
                           Log.warn("[Pack] Custom humanoid equipment requires per-client pack hosting and a known client protocol; armor output disabled.");
                        }
                     }

                     BossBarManager.setTitleKeepProgress("Preparing pack: validating final contents...");
                     this.checkMissingModelParents();
                     BossBarManager.setTitleKeepProgress("Processing pack sounds...");
                     if (!var17.isEmpty()) {
                        PackBuildWorker var258 = PackBuildWorker.getInstance();
                        boolean var265 = var258 != null && var258.isAvailable();
                        HashSet var270 = new HashSet();
                        int var272 = 0;

                        for (Entry var275 : var17.entrySet()) {
                           String var277 = (String)var275.getKey();
                           List var40 = (List)var275.getValue();
                           Log.devLog("\ud83d\udd0a Generating sounds: " + var277);
                           JSONObject var41 = this.bAs.computeIfAbsent(var277, var0 -> new JSONObject());

                           for (GuiEntryRecord var43 : var40) {
                              JSONObject var44 = new JSONObject();
                              JSONArray var45 = new JSONArray();
                              var44.put("sounds", var45);
                              if (var43.Gq() != null) {
                                 var44.put("replace", var43.Gq());
                              }

                              if (var43.Gp() != null) {
                                 var44.put("subtitle", var43.Gp());
                              }

                              for (FontImageSettingsRecord var47 : var43.Gt()) {
                                 JSONObject var48 = buildSoundJson(var47, var277);
                                 var45.add(var48);
                                 IntBoolPair var49 = new IntBoolPair(var47.GE(), var47.GF());
                                 if (var49.hasValue()) {
                                    File var50 = var47.Gw();
                                    String var51 = "assets/" + var277 + "/sounds/" + var47.path() + ".ogg";
                                    String var52 = audioCacheKey(var50, var49);
                                    File var53 = new File(var19, var52 + ".ogg");
                                    ConfigSectionWrapper var54 = var20.kd(var52);
                                    if (isAudioCacheValid(var54, var50, var53, var49)) {
                                       this.writer.addFileInternal(var53, var51, false, true);
                                       Log.devLog("Processed sound: " + var47.path() + ". Namespace '" + var277 + "' (cached).");
                                    } else if (var265 && !var270.contains(var52)) {
                                       try {
                                          long var55 = var50.lastModified();
                                          long var57 = var50.length();
                                          var258.transcodeAudio(var50, var53, var49);
                                          if (var55 != var50.lastModified() || var57 != var50.length()) {
                                             throw new IOException("Source changed while processing audio");
                                          }

                                          var54.method7074("last_modified", var55);
                                          var54.method7074("source_size", var57);
                                          var54.method7074("path_source", var50.getAbsolutePath());
                                          var54.method7074("path_destination", var53.getAbsolutePath());
                                          var54.method7074("bitrate_quality", var49.key);
                                          var54.method7074("mono", var49.flag);
                                          this.writer.addFileInternal(var53, var51, false, true);
                                          Log.devLog("Processed sound: " + var47.path() + ". Namespace '" + var277 + "'.");
                                       } catch (IOException var223) {
                                          var270.add(var52);
                                          Log.warn(
                                             "[Sounds] Failed to process '"
                                                + var277
                                                + ":"
                                                + var47.path()
                                                + "'; using the original audio. "
                                                + var223.getMessage()
                                          );
                                       } catch (InterruptedException var224) {
                                          Thread.currentThread().interrupt();
                                          Log.warn("[Sounds] Audio processing interrupted.");
                                          return;
                                       }
                                    } else if (!var265) {
                                       var272++;
                                    }
                                 }

                                 var41.put(var43.nk(), var44);
                              }
                           }

                           Log.devLog("Saved sounds: " + var41.toJSONString(new Feature[0]));
                        }

                        if (var272 > 0) {
                           Log.warn(
                              "[Sounds] Skipped "
                                 + var272
                                 + " audio transformation(s); using the originals. FFmpeg: "
                                 + (var258 == null ? "unavailable" : var258.getStatus())
                                 + ". Run /iazip when FFmpeg is ready to apply bitrate/mono settings."
                           );
                           if (var258 != null) {
                              var258.requestNotice();
                           }
                        }
                     }

                     if (this.bAr != null) {
                        PackJsonMerger.addBlockSoundOverrides(this.bAr);
                     }

                     this.bAs.forEach((var1xx, var2xx) -> this.writer.addJsonUnchanged(var2xx, "assets/" + var1xx + "/sounds.json"));
                     if (this.generationRunner.isCancelled()) {
                        return;
                     }

                     var4.field91.rethrowFailure();
                     var21 = this.writer.method6164(var23);
                     var4.field91.rethrowFailure();
                     if (var21 && !this.generationRunner.isCancelled()) {
                        BossBarManager.setTitleKeepProgress("Calculating pack hash...");
                        computeSha1Hex(var25.toFile());
                     }

                     if (var23 != null) {
                        var23.run(1.0);
                        return;
                     }
                  } catch (Throwable var225) {
                     var22 = var225;
                  }
               } finally {
                  try {
                     try {
                        this.clearGenerationState();
                     } finally {
                        if (var26 != null) {
                           FileUtils.deleteDirectory(var26.toFile());
                        }
                     }

                     if (var21 && var22 == null && !this.generationRunner.isCancelled()) {
                        FontImageLoader.method1937(var20);
                        var20.save();
                     }
                  } catch (Throwable var222) {
                     if (var22 == null) {
                        var22 = var222;
                     } else {
                        var22.addSuppressed(var222);
                     }
                  }

                  CacheTriple var64 = PackFileCache.resetStats();
                  Log.devLog(
                     "[Pack] Model JSON cache: "
                        + var64.cacheHits()
                        + " hits, "
                        + var64.bytesRead()
                        + " bytes read, "
                        + var64.bytesWritten()
                        + " bytes written."
                  );
                  if (var22 != null) {
                     Log.error("Failed to generate the resourcepack.", var22);
                  }

                  this.lastFailure = var22;
                  boolean var65 = var21 && var22 == null;

                  try {
                     if (this.generationRunner.isCancelled()) {
                        this.generationRunning = false;
                        Files.deleteIfExists(var25);
                        if (var3 != null) {
                           var3.run();
                        }
                     } else {
                        SchedulerUtils.runOnMainThread(() -> {
                           boolean var9xx = false;

                           try {
                              if (this.generationRunner.isCancelled() || !var4.method8115(var6)) {
                                 if (var3 != null) {
                                    var3.run();
                                 }

                                 return;
                              }

                              if (var65) {
                                 var4.field91.rethrowFailure();
                                 BossBarManager.setTitleKeepProgress("Publishing pack...");
                                 atomicMove(var25, var24);
                                 var9xx = true;
                              }

                              this.writeDiagnosticsFile();
                              LootTableLoader.FR();
                           } catch (Throwable var20xx) {
                              this.lastFailure = var20xx;
                              Log.error("Failed to publish the resourcepack.", var20xx);
                           } finally {
                              this.generationRunning = false;
                              this.generated = var9xx;

                              try {
                                 Files.deleteIfExists(var25);
                              } catch (IOException var19x) {
                                 Log.error("Could not remove temporary resourcepack.", var19x);
                              }
                           }

                           if (var1 != null) {
                              var1.accept(var9xx);
                           }
                        });
                     }
                  } catch (Throwable var219) {
                     this.generationRunning = false;
                     this.generated = false;

                     try {
                        Files.deleteIfExists(var25);
                     } catch (IOException var218) {
                        var219.addSuppressed(var218);
                     }

                     Log.error("Could not finish resource-pack compression.", var219);
                     if (var3 != null) {
                        var3.run();
                     }
                  }
               }
            },
            var0 -> {
               BukkitTask var1x = SchedulerUtils.runSync(var0);
               return var1x::cancel;
            }
         )) {
         this.generationRunning = false;
         throw new IllegalStateException("Resource-pack compression is stopped or already pending");
      }
   }

   boolean cancelGeneration() {
      boolean var1 = this.generationRunner.cancel();
      this.generated = false;
      if (var1) {
         this.generationRunning = false;
      }

      return var1;
   }

   static void atomicMove(Path var0, Path var1) {
      Files.move(var0, var1, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
   }

   private void writeDiagnosticsFile() {
      Path var1 = Utils.toPath(ContentManager.field72).resolveSibling("resource-pack-diagnostics.txt");
      Path var2 = var1.resolveSibling(var1.getFileName() + ".tmp");

      try {
         if (PackWarningSummary.isEmpty()) {
            Files.deleteIfExists(var1);
            return;
         }

         Files.createDirectories(var1.getParent());
         Files.writeString(var2, PackWarningSummary.buildReport(), StandardCharsets.UTF_8);
         atomicMove(var2, var1);
      } catch (IOException var6) {
         try {
            Files.deleteIfExists(var2);
         } catch (IOException var5) {
         }

         Log.warn("[Pack] Failed to write diagnostics report: " + var6.getMessage());
      }
   }

   private void checkMissingModelParents() {
      TreeMap var1 = new TreeMap();
      Set var2 = this.writer.collectAllDestinations();

      for (Entry var4 : modelParents.entrySet()) {
         String var5 = (String)var4.getKey();
         String var6 = (String)var4.getValue();
         if ((this.itemContextIndex == null || !this.itemContextIndex.resolvedNames().containsValue(var5))
            && var6 != null
            && !VanillaAssetManager.isBuiltinPath(var6)) {
            String var7 = ItemContextBuilder.normalizeId(var6);
            String var8 = ItemContextBuilder.modelJsonPath(var7);
            if (!modelParents.containsKey(var7) && !var2.contains(var8) && ContentManager.method8084().field91.getModelJsonFor(Version.get(), var7) == null) {
               String var9 = "Model '" + var5 + "' references missing parent '" + var6 + "'.";
               String var10 = var7.substring(var7.indexOf(58) + 1);
               String var11 = var5.substring(var5.lastIndexOf(47) + 1);
               if (ItemContextBuilder.normalizeId(var5).equals(var7) || !var10.contains("/") && var10.equals(var11)) {
                  var9 = var9 + " Remove this self-parent reference or use the intended namespaced parent path.";
               }

               var1.put(ItemContextBuilder.modelJsonPath(var5), var9);
               PackWarningSummary.addWarning("Missing model parents", var9);
            }
         }
      }

      if (!var1.isEmpty()) {
         StringBuilder var12 = new StringBuilder("[Pack] Found ")
            .append(var1.size())
            .append(" model")
            .append(var1.size() == 1 ? "" : "s")
            .append(" with missing parents. Files:");
         var1.forEach((var1x, var2x) -> var12.append("\n  - ").append(var1x).append(" — ").append(var2x));
         var12.append("\n  Full details: ItemsAdder/output/").append("resource-pack-diagnostics.txt");
         Log.warn(var12.toString());
      }
   }

   static String audioCacheKey(File var0, IntBoolPair var1) {
      return Utils.sha256Hex(var0.getAbsolutePath() + "|" + var1.key + "|" + var1.flag);
   }

   static boolean isAudioCacheValid(ConfigSectionWrapper var0, File var1, File var2, IntBoolPair var3) {
      return var1.isFile()
         && var2.isFile()
         && var2.length() > 0L
         && var0.method7081("last_modified", -1L) == var1.lastModified()
         && var0.method7081("source_size", -1L) == var1.length()
         && Objects.equals(var0.method7077("bitrate_quality", null), var3.key)
         && Objects.equals(var0.method7104("mono", null), var3.flag);
   }

   @NotNull
   private static JSONObject buildSoundJson(FontImageSettingsRecord var0, String var1) {
      JSONObject var2 = new JSONObject();
      var2.put("name", var1 + ":" + var0.path());
      if (var0.Gx() != null) {
         var2.put("volume", var0.Gx());
      }

      if (var0.Gy() != null) {
         var2.put("pitch", var0.Gy());
      }

      if (var0.Gz() != null) {
         var2.put("weight", var0.Gz());
      }

      if (var0.GA() != null) {
         var2.put("stream", var0.GA());
      }

      if (var0.GB() != null) {
         var2.put("attenuation_distance", var0.GB());
      }

      if (var0.GC() != null) {
         var2.put("preload", var0.GC());
      }

      return var2;
   }

   private void addLeatherArmorLayers(ResourcePackWriter var1) {
      if (Settings.get().armorOverwriteLeatherLayers) {
         var1.addString("internal/textures/models/armor/leather_layer_1.png", "assets/minecraft/textures/models/armor/leather_layer_1.png");
         var1.addString("internal/textures/models/armor/leather_layer_2.png", "assets/minecraft/textures/models/armor/leather_layer_2.png");
         var1.addString("internal/textures/models/armor/leather_layer_1_overlay.png", "assets/minecraft/textures/models/armor/leather_layer_1_overlay.png");
         var1.addString("internal/textures/models/armor/leather_layer_2_overlay.png", "assets/minecraft/textures/models/armor/leather_layer_2_overlay.png");
         var1.addStringEntry(
            "internal/textures/models/armor/leather_layer_1.png",
            "ia_overlay_1_21_2_plus/assets/minecraft/textures/entity/equipment/humanoid/leather.png",
            true
         );
         var1.addStringEntry(
            "internal/textures/models/armor/leather_layer_2.png",
            "ia_overlay_1_21_2_plus/assets/minecraft/textures/entity/equipment/humanoid_leggings/leather.png",
            true
         );
         var1.addStringEntry(
            "internal/textures/models/armor/leather_layer_1_overlay.png",
            "ia_overlay_1_21_2_plus/assets/minecraft/textures/entity/equipment/humanoid/leather_overlay.png",
            true
         );
         var1.addStringEntry(
            "internal/textures/models/armor/leather_layer_2_overlay.png",
            "ia_overlay_1_21_2_plus/assets/minecraft/textures/entity/equipment/humanoid_leggings/leather_overlay.png",
            true
         );
      }
   }

   public static String toHex(byte[] var0) {
      return BaseEncoding.base16().lowerCase().encode(var0);
   }

   public void buildItemManifests(List var1) {
      for (TexturedItemBase var3 : var1) {
         if (var3.getNamespace().equals("minecraft")) {
            Log.error(
               "Please don't use 'minecraft' as namespace. Item '" + var3.getNamespacedID() + "'. File: " + (var3.ape != null ? var3.ape.DJ() : "unknown")
            );
         } else if (var3.nB()) {
            HashMap var8 = new PackDataIndex(var3).ZL();

            for (Entry var10 : var8.entrySet()) {
               PackManifest var7 = new PackConfigProcessor((String)var10.getKey()).process((JSONObject)var10.getValue());
               if (var7 != null) {
                  this.manifestJsons.put(var7.bIJ, var7.bIK);
                  var7.bIM.forEach(var1x -> {
                     if (!var1x.isSpecial()) {
                        this.packFileEntries.add(var1x);
                     }
                  });
               }
            }
         } else {
            PackManifest var4 = new PackItemAdapter(var3).buildManifest();
            if (var4 != null) {
               this.manifestJsons.put(var4.bIJ, var4.bIK);
               var4.bIM.forEach(var1x -> {
                  if (!var1x.isSpecial()) {
                     this.packFileEntries.add(var1x);
                  }
               });
               if (var3.ape != null && var3.ape.DP() != null && var3.ape.DP().EQ() != null) {
                  String var5 = var4.bIJ.replaceFirst("\\.json$", "_broken.json");
                  PackManifest var6 = new PackConfigProcessor(var5).process(ModelJsonUtils.copyWithLayer0(var4.bIK, var3.ape.DP().EQ()));
                  this.manifestJsons.put(var6.bIJ, var6.bIK);
                  var6.bIM.stream().filter(var0 -> !var0.isSpecial()).forEach(this.packFileEntries::add);
               }
            }
         }
      }
   }

   public void buildBlockstateOverlays(HashMap var1) {
      BlockStateMapper var2 = new BlockStateMapper(var1.values());
      this.blockstateOverlays = var2.buildBlockstateOverlays();
      this.bzR = this.collectBlockstateModelIds();
   }

   public void buildPackArrays(HashMap var1) {
      PackArrayBuilder var2 = new PackArrayBuilder(var1.values());
      this.packArraysJson = var2.ZN();
   }

   void mergeAtlases() {
      this.reservedAssetIds = Stream.concat(
            CustomEntityManager.getInstance().oN.values().stream().flatMap(var0 -> var0.vn.om().stream()),
            ContentManager.method8084().field85.getAssets().textures().keySet().stream().map(var0 -> "itemsadder:item/iaparticles/" + var0)
         )
         .collect(Collectors.toUnmodifiableSet());
      BossBarManager.setTitleKeepProgress("Generating atlases: merging custom atlases...");
      this.atlasSources = new HashMap();
      JSONObject var1 = new JSONObject();
      JSONArray var2 = new JSONArray();
      var1.put("sources", var2);
      this.atlasSources.put("blocks.json", var1);
      JSONObject var3 = new JSONObject();
      JSONArray var4 = new JSONArray();
      var3.put("sources", var4);
      this.atlasSources.put("items.json", var3);

      for (int var5 = -9; var5 <= 17; var5++) {
         var4.add(JSONObject.of("type", "single", "resource", "_iainternal:gui/scroll_track/offset_" + var5));
      }

      var4.add(JSONObject.of("type", "single", "resource", "_iainternal:gui/scroll_track/track"));
      this.blocksAtlas = new JSONObject();
      JSONArray var22 = new JSONArray();
      this.blocksAtlas.put("sources", var22);

      for (int var6 = -9; var6 <= 17; var6++) {
         var22.add(JSONObject.of("type", "single", "resource", "_iainternal:gui/scroll_track/offset_" + var6));
      }

      var22.add(JSONObject.of("type", "single", "resource", "_iainternal:gui/scroll_track/track"));

      for (NamespacedKey var7 : this.scrollGuiRegistry.XQ()) {
         var4.add(JSONObject.of("type", "single", "resource", var7.toString()));
         var22.add(JSONObject.of("type", "single", "resource", var7.toString()));
      }

      Log.devLog("Merging atlases from resourcepacks...");

      for (File var8 : ContentManager.method8084().field87) {
         File var9 = new File(var8, "minecraft/atlases/");
         if (var9.exists()) {
            for (File var12 : Utils.listFilesOrEmpty(var9, var0 -> var0.getName().endsWith(".json"))) {
               try {
                  JSONObject var13 = JsonHelper.readJsonFile(var12);
                  if (!var13.containsKey("sources") || !(var13.get("sources") instanceof JSONArray)) {
                     throw new RuntimeException("Atlas file has invalid 'sources' attribute.");
                  }

                  if (excludeDedicatedEntityAtlases(var12.getName(), var13.getJSONArray("sources"))) {
                     Log.warn(
                        "[Pack] Atlas directory source could include vanilla sprites owned by dedicated atlases; those vanilla sprites were excluded. File: "
                           + Utils.shortPathOf(var12.getAbsolutePath())
                     );
                  }

                  if (!this.atlasSources.containsKey(var12.getName())) {
                     this.atlasSources.put(var12.getName(), var13);
                  } else {
                     ((JSONObject)this.atlasSources.get(var12.getName())).getJSONArray("sources").addAll(var13.getJSONArray("sources"));
                  }
               } catch (JSONException var20) {
                  Log.error("Malformed JSON file. File: " + Utils.shortPathOf(var12.getAbsolutePath()));
                  Log.error(var20.getMessage());
               } catch (Throwable var21) {
                  Log.error("Error opening a custom atlas file. Make sure it's valid or delete it. File: " + Utils.shortPathOf(var12.getAbsolutePath()), var21);
               }
            }
         }
      }

      BossBarManager.setTitleKeepProgress("Generating atlases: loading fallback rules...");
      this.loadFallbackRules();
      BossBarManager.setTitleKeepProgress("Generating atlases: collecting blockstate models...");
      Log.devLog("Collecting models from blockstates to generate atlas files...");
      this.blockstateOverlays.forEach((var1x, var2x) -> {
         List var3x = ListCollector.collectModelNames(var2x);
         this.blockTextureModels.addAll(var3x);
      });
      Log.devLog("Collecting textures from models to generate atlas files...");
      List var26 = ContentManager.method8204();
      int var27 = 0;
      int var28 = var26.size();
      this.modelJsonCache.clear();
      this.modelSourceCache.clear();

      for (File var30 : var26) {
         var27++;
         if (var27 == 1 || var27 % 100 == 0 || var27 == var28) {
            BossBarManager.setTitleKeepProgress("Generating atlases: validating models " + var27 + "/" + var28 + "...");
         }

         PackManifest var31 = new FilePackConfigProcessor(var30).loadManifest();
         if (var31 != null) {
            var31.getSkipped().removeIf(this::isDefinedAtlasSprite);
            if (var31.bIL && Settings.get().fixAtlasItemModels) {
               this.modelSourceCache.put(Utils.normalizeSlashes(var30.getAbsolutePath()), var31.bIK);
            }

            this.skippedAssets.addAll(var31.getSkipped());
            if (!Settings.get().fixAtlasItemModels) {
               boolean var32 = var31.isBlockModel() || this.bzR.contains(var31.getItemId()) || this.blockTextureModels.contains(var31.getItemId());

               for (String var15 : var31.collectTextureIds()) {
                  if (!var32 && var15.contains("block/")) {
                     var32 = true;
                  }

                  if (!nameRegistry.containsName(var15, var32)) {
                     if (var31.isSkipped(var15)) {
                        var15 = "minecraft:item/iamissing";
                     }

                     String var16 = nameRegistry.obfuscateName(var15, var32);
                     StringPair var17 = new StringPair(var15, var16);
                     JSONObject var18 = new JSONObject();
                     var18.put("type", "single");
                     var18.put("resource", var17.first);
                     var18.put("sprite", var17.second);
                     if (var32) {
                        var2.add(var18);
                     } else {
                        var4.add(var18);
                     }

                     JSONObject var19 = new JSONObject();
                     var19.put("type", "single");
                     var19.put("resource", var17.first);
                     var19.put("sprite", var17.second);
                     var22.add(var19);
                  }
               }
            }
         }
      }

      BossBarManager.setTitleKeepProgress("Generating atlases: checking missing textures...");
      PackConfigProcessor.reportMissingTextures(this::isDefinedAtlasSprite);
   }

   static boolean excludeDedicatedEntityAtlases(String var0, JSONArray var1) {
      if (!var0.equals("items.json")) {
         return false;
      }

      boolean var2 = var1.stream()
         .filter(JSONObject.class::isInstance)
         .map(JSONObject.class::cast)
         .anyMatch(
            var0x -> {
               String var1x = var0x.getString("type");
               String var2x = var0x.getString("source");
               return var1x != null
                  && var1x.endsWith("directory")
                  && var2x != null
                  && (
                     var2x.isEmpty()
                        || var2x.equals("entity")
                        || var2x.equals("entity/banner")
                        || var2x.startsWith("entity/banner/")
                        || var2x.equals("entity/chest")
                        || var2x.startsWith("entity/chest/")
                        || var2x.equals("entity/decorated_pot")
                        || var2x.startsWith("entity/decorated_pot/")
                        || var2x.equals("entity/shield")
                        || var2x.startsWith("entity/shield/")
                        || var2x.equals("entity/shulker")
                        || var2x.startsWith("entity/shulker/")
                  );
            }
         );
      if (!var2) {
         return false;
      }

      JSONObject var3 = new JSONObject();
      var3.put("namespace", "^minecraft$");
      var3.put("path", "^entity/(?:banner|chest|decorated_pot|shield|shulker)(?:/.*)?$");
      JSONObject var4 = new JSONObject();
      var4.put("type", "filter");
      var4.put("pattern", var3);
      if (var1.contains(var4)) {
         return false;
      }

      var1.add(var4);
      return true;
   }

   void loadFallbackRules() {
      this.applyFallbackRules(Log::warn, Log::devWarn);
   }

   void loadFallbackRulesWith(Consumer var1) {
      this.applyFallbackRules(var1, var1);
   }

   private void applyFallbackRules(Consumer var1, Consumer var2) {
      this.blockTextureModels = new HashSet();
      this.forcedModelIds = Set.of();
      if (!Settings.get().fixAtlasItemModels) {
         this.itemContextIndex = null;
         this.modernModelCache.clear();
         this.modelFlags.clear();
         this.blockstateCache.clear();
         nameRegistry.clear();
         this.pendingAtlasKeys.clear();
         var1.accept("[Pack] Modern atlas planner rollback is enabled; the old workaround may restore mixed-atlas or glint limitations.");
      } else {
         if (Settings.get().forceFixAtlasModels != null && !Settings.get().forceFixAtlasModels.isEmpty()) {
            HashSet var3 = new HashSet();
            var2.accept("The 1.21.11 atlas material list is deprecated and is used only for unreachable models.");

            for (String var5 : Settings.get().forceFixAtlasModels) {
               this.materialModelJsons.forEach((var3x, var4) -> {
                  if (var3x.equals(var5.toLowerCase())) {
                     List var5x = ListCollector.collectModelNames(var4);
                     var3.addAll(var5x);
                     this.blockTextureModels.addAll(var5x);
                  }
               });
            }

            this.forcedModelIds = Set.copyOf(var3);
         }
      }
   }

   void putAtlasJson(String var1, JSONObject var2) {
      this.modelJsonCache.putIfAbsent(Utils.normalizeSlashes(var1), (JSONObject)JSON.copy(var2, new Feature[0]));
   }

   boolean putModelFile(String var1, File var2) {
      String var3 = Utils.normalizeSlashes(var2.getAbsolutePath());
      if (this.modelSourceCache.containsKey(var3)) {
         ((PackFileCache)this.modelJsonCache).copyEntry(Utils.normalizeSlashes(var1), (PackFileCache)this.modelSourceCache, var3, true);
      } else {
         try {
            ((PackFileCache)this.modelJsonCache).putBytes(Utils.normalizeSlashes(var1), Files.readAllBytes(var2.toPath()));
         } catch (IOException var5) {
            throw new UncheckedIOException(var5);
         }
      }

      return true;
   }

   void putModelJson(String var1, JSONObject var2) {
      this.modelJsonCache.put(Utils.normalizeSlashes(var1), var2);
   }

   void cleanupStaleModelCaches() {
      if (Settings.get().fixAtlasItemModels) {
         this.modelJsonCache.keySet().retainAll(this.writer.collectStaleModelPaths());
         Map var1 = toSortedSets(this.atlasModelRefs);
         this.materialModelJsons
            .forEach((var1x, var2x) -> var1.put("assets/minecraft/items/" + var1x + ".json", new TreeSet(ListCollector.collectModelNames(var2x))));
         this.furnitureItemJsons.forEach((var1x, var2x) -> var1.put(var1x, new TreeSet(ListCollector.collectModelNames(var2x))));
         Map var2 = toSortedSets(this.blockstateModelRefs);
         this.blockstateOverlays.forEach((var1x, var2x) -> var2.putIfAbsent(var1x, new TreeSet(ListCollector.collectModelNames(var2x))));
         TreeSet var3 = new TreeSet();
         var1.values().forEach(var3::addAll);
         TreeSet var4 = new TreeSet();
         var2.values().forEach(var4::addAll);

         try {
            this.validateAtlasPlanWith(var3, var4, var0 -> {});
         } finally {
            this.modelSourceCache.clear();
         }
      }
   }

   void finalizeModernAtlases(Map var1) {
      Consumer var2 = this.warnSink;
      Consumer var3 = this.infoSink;
      this.warnSink = Log::warn;
      this.infoSink = Log::log;

      try {
         this.planAtlases(
            var1,
            null,
            this::getVanillaModelJson,
            getVersionsFrom1_21_11(),
            var0 -> ContentManager.method8078(var0) || ContentManager.method8084().field91.hasVanillaTexture(var0),
            (var0, var1x) -> ContentManager.method8078(var1x)
               || !var1x.startsWith("minecraft:") && ContentManager.method8084().field91.hasVanillaTexture(var1x)
               || var1x.startsWith("minecraft:")
                  && ContentManager.method8084().field91.getAssetList(var0).contains("textures/" + var1x.substring("minecraft:".length()) + ".png")
         );
      } catch (IOException var8) {
         throw new IllegalStateException("Failed to finalize modern atlas entries", var8);
      } finally {
         this.warnSink = var2;
         this.infoSink = var3;
      }
   }

   void planUnknownVersionAtlases(Map var1, Predicate var2) {
      this.planAtlases(
         var1, var2, (var0, var1x) -> null, List.of(Version.UNKNOWN), var0 -> var0.startsWith("minecraft:"), (var0, var1x) -> var1x.startsWith("minecraft:")
      );
   }

   void planAtlasesWithFilters(Map var1, Predicate var2, Predicate var3) {
      this.planAtlases(var1, var2, (var0, var1x) -> null, List.of(Version.UNKNOWN), var3, (var1x, var2x) -> var3.test(var2x));
   }

   void planAtlasesForVersions(Map var1, BiFunction var2, Collection var3, Predicate var4) {
      this.planAtlases(var1, VanillaAssetManager::isBuiltinPath, var2, var3, var4, (var1x, var2x) -> var4.test(var2x));
   }

   private void planAtlases(Map var1, @Nullable Predicate var2, BiFunction var3, Collection var4, Predicate var5, BiPredicate var6) {
      PackReport var7 = new PackReport();

      try {
         this.buildMetrics.reset();
         boolean var8 = var4.stream().noneMatch(Version.UNKNOWN::equals);
         if (var8) {
            this.rebuildHumanoidAtlasEntries(var1, HumanoidEquipmentPack.buU.stream().filter(var0 -> var0.protocol() >= 774).toList());
         }

         PackBuildReport var9 = PackBuildReport.method6512(var1, this.buildMetrics);
         this.buildReport = var9;
         this.bAm = Map.of();
         this.bAk = this.collectRemappedModelIds(var9);
         Set var10 = toAssetPathSet(var9.bAM);
         Predicate var11 = var2;
         if (var11 == null) {
            Set var12 = var9.bAM
               .stream()
               .filter(ResourcePackGenerator::isModelJsonPath)
               .map(PackManifest::normalizeModelId)
               .map(ItemContextBuilder::normalizeId)
               .collect(Collectors.toUnmodifiableSet());
            var11 = var1x -> VanillaAssetManager.isBuiltinPath(var1x) || var12.contains(ItemContextBuilder.normalizeId(var1x));
         }

         JSONObject var44 = getPackJson(var9, "pack.mcmeta");
         if (!var8) {
            ItemContextIndex var13 = this.itemContextIndex;
            PackFileCache var14 = new PackFileCache((PackFileCache)this.modelJsonCache);
            PackFileCache var15 = new PackFileCache((PackFileCache)this.modernModelCache);
            HashMap var16 = new HashMap(this.modelFlags);
            Set var17 = var9.bAM;
            HashSet var18 = new HashSet(var15.keySet());
            var18.removeAll(var17);
            Set var19 = this.collectManagedAtlasKeys();
            var19.removeAll(var17);
            String var20 = "ia_overlay_modern_atlas/assets/minecraft/atlases/items.json";
            String var21 = "ia_overlay_modern_atlas/assets/minecraft/atlases/blocks.json";
            String var22 = "assets/minecraft/atlases/blocks.json";
            boolean var23 = var17.contains(var20);
            boolean var24 = var17.contains(var21);
            boolean var25 = var17.contains(var22);
            JSONObject var26 = getPackJson(var9, var20);
            JSONObject var27 = getPackJson(var9, var21);
            JSONObject var28 = getPackJson(var9, var22);
            if (var26 != null) {
               this.atlasSources.put("items.json", var26);
            } else {
               this.atlasSources.put("items.json", emptyAtlas());
            }

            if (var27 != null) {
               this.atlasSources.put("blocks.json", var27);
            } else {
               this.atlasSources.put("blocks.json", emptyAtlas());
            }

            if (var28 != null) {
               this.blocksAtlas = var28;
            } else {
               this.blocksAtlas = emptyAtlas();
            }

            this.pendingAtlasKeys.clear();
            restoreAtlasSpriteMappings(var26, false);
            restoreAtlasSpriteMappings(var27, true);
            this.modelJsonCache.clear();
            PackFileCache var29 = new PackFileCache(true);
            TreeMap var30 = new TreeMap();
            HashSet var31 = new HashSet();
            PackFileCache var32 = new PackFileCache(true);

            for (PackEntryWithPath var34 : var9.bAL) {
               String var35 = var34.path();
               PackEntry var36 = var34.VR();
               if (!var36.isRemoved()) {
                  if (!isModelJsonPath(var35)) {
                     if (isUnderAssetFolder(var35, "items")) {
                        JSONObject var50 = parsePackJson(var36, var35);
                        if (var13 != null) {
                           var13.remapModelPaths(var50);
                        }

                        String var53 = stripToAssetsRoot(var35);
                        if (var35.startsWith("ia_overlay_modern_atlas/") || !var29.containsKey(var53)) {
                           var29.put(var53, var50);
                        }
                     } else if (isUnderAssetFolder(var35, "blockstates")) {
                        JSONObject var49 = parsePackJson(var36, var35);
                        String var52 = stripToAssetsRoot(var35);
                        if (var35.startsWith("ia_overlay_modern_atlas/") || !var30.containsKey(var52)) {
                           var30.put(var52, new TreeSet(ListCollector.collectModelNames(var49)));
                        }
                     }
                  } else {
                     JSONObject var37 = (JSONObject)var15.get(var35);
                     JSONObject var38 = parseOrReuseJson(var36, var35, var37);
                     JSONObject var39 = (JSONObject)var14.get(var35);
                     JSONObject var40;
                     if (var37 != null && var37.equals(var38)) {
                        var40 = var39;
                     } else {
                        rewriteModelTextureNames(var38);
                        if (var13 != null) {
                           var13.remapModelPaths(var38);
                        }

                        var40 = var38;
                        if (var37 != null && var39 == null) {
                           var32.put(var35, var38);
                           var40 = null;
                        }
                     }

                     if (var40 != null) {
                        this.modelJsonCache.put(var35, var40);
                     } else if (var35.startsWith("ia_overlay_modern_atlas/")) {
                        var31.add(var35);
                     }
                  }
               }
            }

            TreeMap var45 = new TreeMap();
            var29.forEach((var1x, var2x) -> var45.put(var1x, new TreeSet(ListCollector.collectModelNames(var2x))));
            this.atlasModelRefs.clear();
            this.atlasModelRefs.putAll(var45);
            this.blockstateModelRefs.clear();
            this.blockstateModelRefs.putAll(var30);
            TreeSet var46 = new TreeSet();
            var45.values().forEach(var46::addAll);
            TreeSet var47 = new TreeSet();
            var30.values().forEach(var47::addAll);
            this.itemDefinitionCache.clear();
            this.itemDefinitionCache.putAll(var29);
            this.collectMissingAssets(var10, var5);
            this.removeMissingSourceEntries((JSONObject)this.atlasSources.get("items.json"));
            this.removeMissingSourceEntries((JSONObject)this.atlasSources.get("blocks.json"));
            this.removeMissingSourceEntries(this.blocksAtlas);
            this.validateAtlasPlanInternal(var46, var47, Map.of(), this.warnSink, var7, List.of(Version.UNKNOWN), null);
            var31.stream().filter(var1x -> !this.modernModelCache.containsKey(var1x)).forEach(var1::remove);
            var32.forEach((var2x, var3x) -> {
               boolean var4x = var16.getOrDefault(var2x, false);
               this.putPlannedModel(var2x, PackManifest.normalizeModelId(var2x), var3x, var4x);
               this.applyModelTextures(var2x, var4x);
            });
            var18.forEach(var1x -> {
               this.modernModelCache.remove(var1x);
               this.modelFlags.remove(var1x);
            });
            var19.forEach(this.blockstateCache::remove);
            boolean var48 = this.hasPendingTextureFlag(false);
            boolean var51 = this.hasPendingTextureFlag(true);
            if (!var23 && var48) {
               throw new IllegalStateException("Modern atlas validation failed: listener removed required atlas entry '" + var20 + "'");
            }

            if (!var24 && var51) {
               throw new IllegalStateException("Modern atlas validation failed: listener removed required atlas entry '" + var21 + "'");
            }

            if (var25 || !var48 && !var51) {
               this.resolveAtlasEntriesForVersions(var45, var30, var11, var3, var4, var10, var5, var7);
               this.modernModelCache.forEach((var1x, var2x) -> var1.put(var1x, toPackEntry(var1x, var2x)));
               this.blockstateCache.forEach((var1x, var2x) -> var1.put(var1x, toPackEntry(var1x, var2x)));
               this.removeDuplicateAtlasSources();
               this.atlasSources.forEach((var3x, var4x) -> {
                  String var5x = "ia_overlay_modern_atlas/assets/minecraft/atlases/" + var3x;
                  if (var17.contains(var5x)) {
                     var1.put(var5x, toPackEntry(var5x, this.dedupeAtlasSources(withoutHumanoidSources(var4x))));
                  }
               });
               if (var25) {
                  var1.put(var22, toPackEntry(var22, this.dedupeAtlasSources(this.blocksAtlas)));
               }

               this.applyHumanoidEquipmentAtlases(var1);
               return;
            }

            throw new IllegalStateException("Modern atlas validation failed: listener removed required atlas entry '" + var22 + "'");
         }

         if (var44 == null) {
            throw new IllegalStateException("Modern atlas planning requires final pack entry 'pack.mcmeta', but it is missing or removed");
         }

         this.planPerVersionAtlases(var1, var9, var44, var3, var4, var5, var6, var7);
         this.rebuildHumanoidAtlasEntries(var1, HumanoidEquipmentPack.buU.stream().filter(var0 -> var0.protocol() < 774).toList());
      } finally {
         this.flushPackReport(var7);
         this.buildReport = null;
         this.bAm = Map.of();
      }
   }

   private void planPerVersionAtlases(
      Map var1, PackBuildReport var2, JSONObject var3, BiFunction var4, Collection var5, Predicate var6, BiPredicate var7, PackReport var8
   ) {
      ItemContextIndex var9 = this.itemContextIndex;
      PackFileCache var10 = new PackFileCache((PackFileCache)this.modelJsonCache);
      PackFileCache var11 = new PackFileCache((PackFileCache)this.itemDefinitionCache);
      PackFileCache var12 = new PackFileCache((PackFileCache)this.modernModelCache);
      PackFileCache var13 = new PackFileCache((PackFileCache)this.blockstateCache);
      HashMap var14 = new HashMap(nameRegistry.itemAliases);
      HashMap var15 = new HashMap(nameRegistry.blockAliases);
      Set var16 = this.collectModernItemPaths();
      HashSet var17 = new HashSet();
      PackFileCache var18 = new PackFileCache(true);

      for (PackEntryWithPath var20 : var2.bAL) {
         String var21 = var20.path();
         PackEntry var22 = var20.VR();
         if (!var22.isRemoved()
            && var21.endsWith(".json")
            && (
               isModelJsonPath(var21) || isUnderAssetFolder(var21, "items") || isUnderAssetFolder(var21, "blockstates") || isUnderAssetFolder(var21, "atlases")
            )) {
            JSONObject var23 = isModelJsonPath(var21) ? (JSONObject)var12.get(var21) : null;
            JSONObject var24 = parseOrReuseJson(var22, var21, var23);
            if (isModelJsonPath(var21)) {
               JSONObject var25 = (JSONObject)var10.get(var21);
               if (var23 != null && var23.equals(var24)) {
                  if (var25 == null) {
                     var17.add(var21);
                     continue;
                  }

                  var24 = var25;
               } else {
                  rewriteModelTextureNames(var24);
                  if (var9 != null) {
                     var9.remapModelPaths(var24);
                  }
               }
            } else if (isUnderAssetFolder(var21, "items")) {
               var23 = (JSONObject)var13.get(var21);
               JSONObject var73 = (JSONObject)var11.get(var21);
               if (var23 != null && var23.equals(var24) && var73 != null) {
                  var24 = (JSONObject)JSON.copy(var73, new Feature[0]);
               } else if (var23 != null && var23.equals(var24) && !var16.contains(var21)) {
                  var17.add(var21);
                  continue;
               }

               if (var9 != null) {
                  var9.remapModelPaths(var24);
               }
            }

            var18.put(var21, var24);
         }
      }

      PackBuildReport var66 = var2.method6513(var17);
      String var67 = "assets/minecraft/atlases/blocks.json";
      JSONObject var68 = (JSONObject)var18.get(var67);
      this.blocksAtlas = var68 == null ? emptyAtlas() : (JSONObject)JSON.copy(var68, new Feature[0]);
      int var69 = this.blocksAtlas.getJSONArray("sources").size();
      nameRegistry.clear();
      var14.forEach((var0, var1x) -> nameRegistry.restoreMapping(var0, var1x, false));
      var15.forEach((var0, var1x) -> nameRegistry.restoreMapping(var0, var1x, true));
      TreeMap var71 = new TreeMap();
      var5.forEach(var1x -> var71.computeIfAbsent(ServerCompat.packVersionOf(var1x), var0x -> new ArrayList()).add(var1x));
      PackFileCache var72 = new PackFileCache(true);
      HashMap var74 = new HashMap();
      PackFileCache var26 = new PackFileCache(true);
      PackFileCache var27 = new PackFileCache(true);
      PackFileCache var28 = new PackFileCache(true);
      PackFileCache var29 = new PackFileCache(true);
      TreeMap var30 = new TreeMap();
      TreeMap var31 = new TreeMap();
      HashSet var32 = new HashSet();
      HashMap var33 = new HashMap();
      PackFileCache var34 = new PackFileCache(true);
      Map var35 = indexOverlayEntries(var3);
      TreeSet var36 = new TreeSet();
      String var37 = "assets/minecraft/models/item/";

      for (String var39 : var18.keySet()) {
         String var40 = stripToAssetsRoot(var39);
         if (var40.startsWith(var37) && var40.indexOf(47, var37.length()) < 0) {
            var36.addAll(var18.getMetadata(var39).modelReferences());
         }
      }

      PackIssueLog var75 = new PackIssueLog();
      PackResolverState var76 = new PackResolverState();

      for (List var41 : var71.values()) {
         Version var42 = (Version)var41.getFirst();
         Map var43 = buildOverlayMap(var66, var3, var42);
         this.bAm = var43;
         String var44 = selectModernAtlasOverlay(var3, var42);
         String var45 = resolveAtlasAssetPath(var3, var43, "assets/minecraft/atlases/items.json", var44, var42);
         String var46 = resolveAtlasAssetPath(var3, var43, "assets/minecraft/atlases/blocks.json", var44, var42);
         Map var47 = filterEntriesByFolder(var43, var18, "models", true);
         HashSet var48 = new HashSet();
         if (var42 == Version.v26_3) {
            for (String var50 : var47.keySet()) {
               JSONObject var51 = extractBuiltinEntityModel((JSONObject)var47.get(var50));
               if (var51 != null) {
                  var48.add(var50);
               }
            }
         }

         Map var87 = filterEntriesByFolder(var43, var18, "items", false);
         Map var88 = filterEntriesByFolder(var43, var18, "blockstates", false);
         Map var89 = toSortedModelNames(var87);
         Map var52 = toSortedModelNames(var88);

         for (String var54 : var47.keySet()) {
            var28.copyEntry(var54, (PackFileCache)var47, var54, false);
         }

         var87.forEach((var2x, var3x) -> {
            String var4x = (String)var43.get(var2x);
            if (var4x != null) {
               var29.putIfAbsent(var4x, var3x);
            }
         });
         var30.putAll(var89);
         var31.putAll(var52);
         this.modelJsonCache.clear();
         this.modelJsonCache.putAll(var47);
         this.itemDefinitionCache.clear();
         this.itemDefinitionCache.putAll(var87);
         this.atlasSources = new HashMap();
         this.atlasSources.put("items.json", buildRemappedAtlas(var43, var18, "assets/minecraft/atlases/items.json", var14, Map.of()));
         this.atlasSources.put("blocks.json", buildRemappedAtlas(var43, var18, "assets/minecraft/atlases/blocks.json", Map.of(), var15));
         restoreAtlasSpriteMappings((JSONObject)this.atlasSources.get("items.json"), false);
         restoreAtlasSpriteMappings((JSONObject)this.atlasSources.get("blocks.json"), true);
         HashSet var90 = new HashSet(var43.keySet());
         this.collectMissingAssets(var90, var6);
         this.removeMissingSourceEntries((JSONObject)this.atlasSources.get("items.json"));
         this.removeMissingSourceEntries((JSONObject)this.atlasSources.get("blocks.json"));
         TreeSet var91 = new TreeSet();
         var89.values().forEach(var91::addAll);
         var91.addAll(var36);
         TreeSet var55 = new TreeSet();
         var52.values().forEach(var55::addAll);
         Set var56 = var47.keySet().stream().map(PackManifest::normalizeModelId).map(ItemContextBuilder::normalizeId).collect(Collectors.toSet());
         Map var57 = planModelTextures(var89, var55, var47, var42, var4);
         this.pendingAtlasKeys.clear();
         this.validateAtlasPlanInternal(var91, var55, var57, this.warnSink, var8, var41, var76);
         Set var58 = var47.keySet();

         for (String var60 : this.modernModelCache.keySet()) {
            String var61 = (String)var43.get(stripToAssetsRoot(var60));
            if (var61 == null || var61.equals(var60) || !var58.contains(var61)) {
               PackMetadata var62 = ((PackFileCache)this.modernModelCache).getMetadata(var60);
               JSONObject var63 = var62.outOfBoundsUv() ? clampUvCoordinates((JSONObject)this.modernModelCache.get(var60)) : null;
               boolean var64 = var63 != null || Objects.equals(this.modernModelCache.get(var60), var47.get(var60));
               String var65 = var58.contains(var60)
                  ? (var64 ? var60 : resolveAtlasAssetPath(var3, var43, stripToAssetsRoot(var60), var44, var42))
                  : var44 + "/" + stripToAssetsRoot(var60);
               if (var63 == null) {
                  copyPlannedEntry(var72, var65, (PackFileCache)this.modernModelCache, var60, var42);
               } else {
                  putPlannedEntry(var72, var65, var63, var42);
               }

               var74.put(var65, (Boolean)this.modelFlags.get(var60));
               if (var63 != null && var32.add(var65)) {
                  PackWarningSummary.addWarning(
                     "Minecraft 26.1+ UV compatibility", "Model: " + var60 + "\nClamped UV coordinates outside 0..16 in the shared model asset."
                  );
               }
            }
         }

         for (String var93 : var48) {
            String var94 = resolveAtlasAssetPath(var3, var43, stripToAssetsRoot(var93), var44, var42);
            JSONObject var95 = (JSONObject)var72.get(var94);
            if (var95 == null) {
               putPlannedEntry(var72, var94, extractBuiltinEntityModel((JSONObject)var47.get(var93)), var42);
            } else {
               JSONObject var96 = extractBuiltinEntityModel(var95);
               if (var96 != null) {
                  var72.put(var94, var96);
               }
            }
         }

         var87.forEach((var8x, var9x) -> {
            String var10x = (String)var43.get(var8x);
            if (var10x != null && canRewriteAsset(var3, var10x)) {
               JSONObject var11x = this.rewriteModelRefs(var9x);
               putPlannedEntry(var26, var10x, var11x, var42);
               var34.put(var8x, var11x);
               var33.put(var8x, overlayIndexOf(var10x, var35));
            }
         });
         this.blockstateCache.forEach((var8x, var9x) -> {
            String var10x = stripToAssetsRoot(var8x);
            String var11x = var44 + "/" + var10x;
            JSONObject var12x = (JSONObject)var87.get(var10x);
            int var13x = overlayIndexOf((String)var43.get(var10x), var35);
            int var14x = var33.getOrDefault(var10x, Integer.MIN_VALUE);
            if (var14x > var13x) {
               var13x = var14x;
               var12x = (JSONObject)var34.get(var10x);
            }

            int var15x = overlayIndexOf(var11x, var35);
            if (var15x > var13x && !Objects.equals(var9x, var12x)) {
               putPlannedEntry(var26, var11x, var9x, var42);
               var34.put(var10x, var9x);
               var33.put(var10x, var15x);
            } else {
               if (var12x != null) {
                  var34.put(var10x, var12x);
               }

               var33.put(var10x, var13x);
            }
         });
         this.resolveAtlasEntriesForVersions(
            var89,
            var52,
            var1x -> VanillaAssetManager.isBuiltinPath(var1x) || var56.contains(ItemContextBuilder.normalizeId(var1x)),
            var4,
            var41,
            var90,
            var6,
            var8
         );
         var75.mergeFrom(this.issueLog);
         putPlannedEntry(var27, var45, this.dedupeAtlasSources((JSONObject)this.atlasSources.get("items.json")), var42);
         putPlannedEntry(var27, var46, this.dedupeAtlasSources((JSONObject)this.atlasSources.get("blocks.json")), var42);
      }

      this.atlasModelRefs.clear();
      this.atlasModelRefs.putAll(var30);
      this.blockstateModelRefs.clear();
      this.blockstateModelRefs.putAll(var31);
      this.modelJsonCache.clear();
      this.modelJsonCache.putAll(var28);
      this.itemDefinitionCache.clear();
      this.itemDefinitionCache.putAll(var29);
      this.modernModelCache.clear();
      this.modernModelCache.putAll(var72);
      this.modelFlags.clear();
      this.modelFlags.putAll(var74);
      this.blockstateCache.clear();
      this.blockstateCache.putAll(var26);
      this.issueLog = var75;
      HashSet var78 = new HashSet(var72.keySet());
      var78.addAll(var26.keySet());
      var78.addAll(var27.keySet());
      var17.stream().filter(var1x -> !var78.contains(var1x)).forEach(var1::remove);
      copyEntriesInto(var1, var72);
      copyEntriesInto(var1, var26);
      var27.forEach((var1x, var2x) -> var27.put(var1x, var1x.startsWith("ia_humanoid_equipment_") ? var2x : withoutHumanoidSources(var2x)));
      copyEntriesInto(var1, var27);
      if (var1.containsKey(var67) && !((PackEntry)var1.get(var67)).isRemoved()) {
         List var79 = List.of(Version.v1_21_4, Version.v1_21_5, Version.v1_21_6, Version.v1_21_8, Version.v1_21_10);
         HashSet var80 = new HashSet();

         for (Version var83 : var79) {
            var80.addAll(buildOverlayMap(var66, var3, var83).keySet());
         }

         JSONArray var82 = this.blocksAtlas.getJSONArray("sources");

         for (int var84 = var82.size() - 1; var84 >= 0; var84--) {
            JSONObject var85 = var82.getJSONObject(var84);
            if (var85 != null
               && var85.containsKey("resource")
               && var85.containsKey("sprite")
               && (
                  var84 >= var69
                     || Objects.equals(var85.getString("sprite"), var14.get(var85.getString("resource")))
                     || Objects.equals(var85.getString("sprite"), var15.get(var85.getString("resource")))
               )) {
               String var86 = toNamespacedId(var85.getString("resource"));
               if (!"minecraft:item/iamissing".equals(var86)
                  && !this.isDefinedAtlasSprite(var86)
                  && !hasTextureFile(var80, var86)
                  && var79.stream().noneMatch(var2x -> var7.test(var2x, var86))) {
                  var82.remove(var84);
               }
            }
         }

         var1.put(var67, toPackEntry(var67, this.dedupeAtlasSources(this.blocksAtlas)));
      }
   }

   private Set collectModernItemPaths() {
      HashSet var1 = new HashSet();
      if (this.overlayEntriesByRange != null) {
         Map var2 = (Map)this.overlayEntriesByRange.get(OverlayVersionRange.v_1_21_6_plus);
         if (var2 != null) {
            var2.keySet().forEach(var1x -> var1.add("ia_overlay_modern_atlas/assets/minecraft/items/" + var1x + ".json"));
         }
      }

      return var1;
   }

   private static Map filterEntriesByFolder(Map var0, Map var1, String var2, boolean var3) {
      PackFileCache var4 = new PackFileCache(true);
      var0.forEach((var4x, var5) -> {
         if (isUnderAssetFolder(var4x, var2)) {
            String var6 = var3 ? var5 : var4x;
            if (var1 instanceof PackFileCache var7) {
               var4.copyEntry(var6, var7, var5, true);
            } else {
               JSONObject var8 = (JSONObject)var1.get(var5);
               if (var8 != null) {
                  var4.putJson(var6, var8);
               }
            }
         }
      });
      return var4;
   }

   private static Map toSortedModelNames(Map var0) {
      TreeMap var1 = new TreeMap();
      var0.forEach((var1x, var2) -> var1.put(var1x, new TreeSet(ListCollector.collectModelNames(var2))));
      return var1;
   }

   private static JSONObject buildRemappedAtlas(Map var0, Map var1, String var2, Map var3, Map var4) {
      String var5 = (String)var0.get(var2);
      JSONObject var6 = var5 == null ? null : (JSONObject)var1.get(var5);
      return removeRemappedSources(var6 == null ? emptyAtlas() : var6, var3, var4);
   }

   private static JSONObject removeRemappedSources(JSONObject var0, Map var1, Map var2) {
      JSONObject var3 = (JSONObject)JSON.copy(var0, new Feature[0]);
      if (var3.get("sources") instanceof JSONArray var4) {
         var4.removeIf(var2x -> {
            if (var2x instanceof JSONObject var3x && var3x.containsKey("resource") && var3x.containsKey("sprite")) {
               String var4x = var3x.getString("resource");
               String var5 = var3x.getString("sprite");
               return Objects.equals(var5, var1.get(var4x)) || Objects.equals(var5, var2.get(var4x));
            } else {
               return false;
            }
         });
         return var3;
      } else {
         return var3;
      }
   }

   private static Map indexOverlayEntries(JSONObject var0) {
      HashMap var1 = new HashMap();
      JSONObject var2 = var0.getJSONObject("overlays");
      JSONArray var3 = var2 == null ? null : var2.getJSONArray("entries");
      if (var3 == null) {
         return var1;
      }

      for (int var4 = 0; var4 < var3.size(); var4++) {
         String var5 = var3.getJSONObject(var4).getString("directory");
         if (var5 != null) {
            var1.put(Utils.normalizeSlashes(var5), var4);
         }
      }

      return var1;
   }

   private static int overlayIndexOf(@Nullable String var0, Map var1) {
      if (var0 == null) {
         return Integer.MIN_VALUE;
      }

      var0 = Utils.normalizeSlashes(var0);
      if (var0.startsWith("assets/")) {
         return -1;
      }

      int var2 = var0.indexOf("/assets/");
      return var2 < 0 ? Integer.MIN_VALUE : var1.getOrDefault(var0.substring(0, var2), -1);
   }

   static String selectModernAtlasOverlay(JSONObject var0, Version var1) {
      JSONObject var2 = var0.getJSONObject("overlays");
      if (var2 != null && var2.get("entries") instanceof JSONArray var3) {
         String var13 = null;
         String var5 = null;
         int var6 = Integer.MIN_VALUE;
         int var7 = ServerCompat.packVersionOf(var1);

         for (Object var9 : var3) {
            if (var9 instanceof JSONObject var10) {
               String var11 = var10.getString("directory");
               if ("ia_overlay_modern_atlas".equals(var11)
                  || "ia_overlay_26_1_plus".equals(var11)
                  || "ia_overlay_26_2_plus".equals(var11)
                  || "ia_overlay_26_3_plus".equals(var11)) {
                  int[] var12 = parseFormatRange(var10);
                  if (overlayAppliesToVersion(var10, var1)) {
                     var13 = var11;
                     if (var12[0] >= var6) {
                        var5 = var11;
                        var6 = var12[0];
                     }
                  }
               }
            }
         }

         if (var13 == null) {
            throw new IllegalStateException("No ItemsAdder modern atlas overlay applies to client " + var1.getName() + " (pack format " + var7 + ")");
         } else if (!var13.equals(var5)) {
            throw new IllegalStateException(
               "ItemsAdder modern atlas overlay precedence selects '"
                  + var13
                  + "' after the more specific '"
                  + var5
                  + "' for client "
                  + var1.getName()
                  + " (pack format "
                  + var7
                  + ")"
            );
         } else {
            return var13;
         }
      } else {
         throw new IllegalStateException("Modern atlas planning requires pack.mcmeta overlay entries");
      }
   }

   private static String resolveAtlasAssetPath(JSONObject var0, Map var1, String var2, String var3, Version var4) {
      String var5 = var3 + "/" + var2;
      String var6 = (String)var1.get(var2);
      if (var6 != null && !var6.equals(var5)) {
         JSONArray var7 = var0.getJSONObject("overlays").getJSONArray("entries");
         int var8 = ServerCompat.packVersionOf(var4);
         int var9 = -1;
         int var10 = -1;

         for (int var11 = 0; var11 < var7.size(); var11++) {
            JSONObject var12 = var7.getJSONObject(var11);
            if (overlayAppliesToVersion(var12, var4)) {
               String var13 = StringUtils.strip(Utils.normalizeSlashes(var12.getString("directory")), "/");
               if (var3.equals(var13)) {
                  var9 = var11;
               }

               if (var6.startsWith(var13 + "/assets/")) {
                  var10 = var11;
               }
            }
         }

         if (var10 <= var9) {
            return var5;
         } else if (!canRewriteAsset(var0, var6)) {
            throw new IllegalStateException(
               "Modern atlas planning cannot rewrite effective asset '"
                  + var6
                  + "' for client "
                  + var4.getName()
                  + " (pack format "
                  + var8
                  + ") because its later overlay also spans legacy formats; move the asset to a modern-only overlay or declare it before ItemsAdder overlay '"
                  + var3
                  + "'"
            );
         } else {
            return var6;
         }
      } else {
         return var5;
      }
   }

   private static boolean canRewriteAsset(JSONObject var0, String var1) {
      JSONObject var2 = var0.getJSONObject("overlays");
      if (var2 != null && var2.get("entries") instanceof JSONArray var3) {
         int var10 = ServerCompat.packVersionOf(Version.v1_21_11);
         boolean var5 = false;

         for (Object var7 : var3) {
            if (var7 instanceof JSONObject var8) {
               String var9 = StringUtils.strip(Utils.normalizeSlashes(var8.getString("directory")), "/");
               if (!StringUtils.isBlank(var9) && var1.startsWith(var9 + "/")) {
                  var5 = true;
                  if (parseFormatRange(var8)[0] < var10) {
                     return false;
                  }
               }
            }
         }

         return var5;
      } else {
         return false;
      }
   }

   private static void putPlannedEntry(Map var0, String var1, JSONObject var2, Version var3) {
      var1 = Utils.normalizeSlashes(var1);
      JSONObject var4 = var0.putIfAbsent(var1, var2);
      if (var4 != null && !var4.equals(var2)) {
         throw new IllegalStateException(
            "Modern atlas planning produced different JSON for physical entry '" + var1 + "' across client versions, including " + var3.getName()
         );
      }
   }

   private static void copyPlannedEntry(PackFileCache var0, String var1, PackFileCache var2, String var3, Version var4) {
      var1 = Utils.normalizeSlashes(var1);
      if (!var0.containsKey(var1)) {
         var0.copyEntry(var1, var2, var3, false);
      } else if (!var0.sameContent(var1, var2, var3) && !var0.getJson(var1).equals(var2.getJson(var3))) {
         throw new IllegalStateException(
            "Modern atlas planning produced different JSON for physical entry '" + var1 + "' across client versions, including " + var4.getName()
         );
      }
   }

   static void writePlayerModel(ResourcePackWriter var0, String var1, String var2) {
      String var3 = "internal/player/" + var1 + ".json";
      String var4 = "assets/_iainternal/models/entity/player/" + var2 + ".json";
      var0.addString(var3, var4);

      try (InputStream var5 = ResourcePackGenerator.class.getResourceAsStream("/" + var3)) {
         if (var5 == null) {
            throw new FileNotFoundException(var3);
         }

         JSONObject var6 = extractBuiltinEntityModel(JSON.parseObject(var5.readAllBytes()));
         if (var6 != null) {
            var6.putIfAbsent("gui_light", "side");
            var0.addJsonUnchanged(var6, "ia_overlay_1_21_4_plus/" + var4);
         }
      }
   }

   @Nullable
   static JSONObject extractBuiltinEntityModel(JSONObject var0) {
      String var1 = var0.getString("parent");
      if (!"builtin/entity".equals(var1) && !"minecraft:builtin/entity".equals(var1)) {
         return null;
      }

      JSONObject var2 = (JSONObject)JSON.copy(var0, new Feature[0]);
      var2.remove("parent");
      return var2;
   }

   @Nullable
   static JSONObject clampUvCoordinates(JSONObject var0) {
      if (!(var0.get("elements") instanceof JSONArray var2)) {
         return null;
      } else {
         JSONObject var3 = null;
         JSONArray var4 = null;

         for (int var5 = 0; var5 < var2.size(); var5++) {
            if (var2.get(var5) instanceof JSONObject var7 && var7.get("faces") instanceof JSONObject var8) {
               for (Entry var10 : var8.entrySet()) {
                  if (var10.getValue() instanceof JSONObject var12 && var12.get("uv") instanceof JSONArray var13 && var13.size() == 4) {
                     for (int var19 = 0; var19 < var13.size(); var19++) {
                        if (var13.get(var19) instanceof Number var15) {
                           double var20 = var15.doubleValue();
                           if (Double.isFinite(var20) && (!(var20 >= 0.0) || !(var20 <= 16.0))) {
                              if (var3 == null) {
                                 var3 = (JSONObject)JSON.copy(var0, new Feature[0]);
                                 var4 = var3.getJSONArray("elements");
                              }

                              var4.getJSONObject(var5)
                                 .getJSONObject("faces")
                                 .getJSONObject((String)var10.getKey())
                                 .getJSONArray("uv")
                                 .set(var19, var20 < 0.0 ? 0 : 16);
                           }
                        }
                     }
                  }
               }
            }
         }

         return var3;
      }
   }

   private static void copyEntriesInto(Map var0, Map var1) {
      for (String var3 : var1.keySet()) {
         PackEntry var4 = (PackEntry)var0.get(var3);
         if (var4 == null || !var4.isRemoved()) {
            var0.put(var3, var1 instanceof PackFileCache var5 ? var5.toPackEntry(var3) : toPackEntry(var3, (JSONObject)var1.get(var3)));
         }
      }
   }

   private Set collectManagedAtlasKeys() {
      HashSet var1 = new HashSet(this.blockstateCache.keySet());
      if (this.overlayEntriesByRange != null) {
         Map var2 = (Map)this.overlayEntriesByRange.get(OverlayVersionRange.v_1_21_6_plus);
         if (var2 != null) {
            var2.keySet().forEach(var1x -> var1.add("ia_overlay_modern_atlas/assets/minecraft/items/" + var1x + ".json"));
         }
      }

      return var1;
   }

   private boolean hasPendingTextureFlag(boolean var1) {
      for (String var3 : this.modernModelCache.keySet()) {
         if ((Boolean)this.modelFlags.get(var3) == var1
            && ((PackFileCache)this.modernModelCache)
               .getMetadata(var3)
               .textures()
               .values()
               .stream()
               .map(StringKey::value)
               .filter(Objects::nonNull)
               .anyMatch(var0 -> !PackFileEntry.isSpecialRef(var0))) {
            return true;
         }
      }

      return false;
   }

   private static JSONObject emptyAtlas() {
      return new JSONObject().fluentPut("sources", new JSONArray());
   }

   private static void restoreAtlasSpriteMappings(@Nullable JSONObject var0, boolean var1) {
      if (var0 != null) {
         Object var3 = var0.get("sources");
         if (var3 instanceof JSONArray) {
            for (Object var4 : (JSONArray)var3) {
               if (var4 instanceof JSONObject var5 && var5.containsKey("resource") && var5.containsKey("sprite")) {
                  nameRegistry.restoreMapping(var5.getString("resource"), var5.getString("sprite"), var1);
               }
            }

            return;
         }
      }
   }

   private static void rewriteModelTextureNames(JSONObject var0) {
      JSONObject var1 = var0.getJSONObject("textures");
      if (var1 != null) {
         HashMap var2 = new HashMap();
         nameRegistry.itemAliases.forEach((var1x, var2x) -> var2.put(var2x, var1x));
         nameRegistry.blockAliases.forEach((var1x, var2x) -> var2.put(var2x, var1x));

         for (Entry var4 : var1.entrySet()) {
            String var5 = textureSpriteOf(var4.getValue());
            if (var5 != null) {
               setTextureSprite(var4, var2.getOrDefault(var5, var5));
            }
         }
      }
   }

   private static boolean isUnderAssetFolder(String var0, String var1) {
      int var2 = var0.indexOf("assets/");
      if (var2 >= 0 && var0.endsWith(".json")) {
         String var3 = var0.substring(var2 + "assets/".length());
         int var4 = var3.indexOf(47);
         return var4 > 0 && var3.startsWith(var1 + "/", var4 + 1);
      } else {
         return false;
      }
   }

   static boolean isModelJsonPath(String var0) {
      return isUnderAssetFolder(var0, "models") && (!var0.startsWith("ia_overlay_1_21_2_plus/") || !isUnderAssetFolder(var0, "models/equipment"));
   }

   private static String stripToAssetsRoot(String var0) {
      int var1 = var0.indexOf("assets/");
      return var1 < 0 ? var0 : var0.substring(var1);
   }

   static Map resolveOverlayMapForVersion(Map var0, JSONObject var1, Version var2) {
      return (Map)createOverlayResolver(var0, var1).apply(var2);
   }

   static Function createOverlayResolver(Map var0, JSONObject var1) {
      PackBuildReport var2 = PackBuildReport.method6512(var0, new PackBuildMetrics());
      return var2x -> buildOverlayMap(var2, var1, var2x);
   }

   private static Map buildOverlayMap(PackBuildReport var0, JSONObject var1, Version var2) {
      LinkedHashMap var3 = new LinkedHashMap();
      var0.bAL.forEach(var1x -> {
         if (!var1x.VR().isRemoved() && var1x.path().startsWith("assets/")) {
            var3.put(var1x.path(), var1x.path());
         }
      });
      Object var4 = var1.get("overlays");
      if (var4 == null) {
         return var3;
      }

      if (var4 instanceof JSONObject var5) {
         Object var7 = var5.get("entries");
         if (var7 instanceof JSONArray) {
            for (Object var8 : (JSONArray)var7) {
               if (!(var8 instanceof JSONObject var9)) {
                  throw new IllegalStateException("Invalid overlay entry in final pack entry 'pack.mcmeta': " + var8);
               }

               String var10 = overlayDirectoryName(var9);
               if (overlayAppliesToVersion(var9, var2)) {
                  String var11 = StringUtils.strip(var10, "/") + "/";
                  var0.bAL.forEach(var2x -> {
                     if (!var2x.VR().isRemoved() && var2x.path().startsWith(var11 + "assets/")) {
                        var3.put(var2x.path().substring(var11.length()), var2x.path());
                     }
                  });
               }
            }

            return var3;
         } else {
            throw new IllegalStateException("Invalid overlay entries in final pack entry 'pack.mcmeta': " + var5.get("entries"));
         }
      } else {
         throw new IllegalStateException("Invalid overlays in final pack entry 'pack.mcmeta': " + var4);
      }
   }

   private static boolean overlayAppliesToVersion(JSONObject var0, Version var1) {
      int var2 = ServerCompat.packVersionOf(var1);
      if (!var0.containsKey("min_format")) {
         int[] var6 = parseFormatRange(var0);
         return var2 >= var6[0] && var2 <= var6[1];
      } else {
         parseFormatRange(var0);
         int var3 = var1 == Version.v26_3 ? 1 : 0;
         int[] var4 = parseFormatBounds(var0.get("min_format"), 0);
         int[] var5 = parseFormatBounds(var0.get("max_format"), Integer.MAX_VALUE);
         return compareToRange(var2, var3, var4) >= 0 && compareToRange(var2, var3, var5) <= 0;
      }
   }

   private static int compareToRange(int var0, int var1, int[] var2) {
      int var3 = Integer.compare(var0, var2[0]);
      return var3 == 0 ? Integer.compare(var1, var2[1]) : var3;
   }

   static int[] parseFormatRange(JSONObject var0) {
      try {
         boolean var1 = var0.containsKey("min_format");
         if (var1 != var0.containsKey("max_format")) {
            throw new IllegalArgumentException();
         }

         if (var1) {
            int[] var14 = parseFormatBounds(var0.get("min_format"), 0);
            int[] var15 = parseFormatBounds(var0.get("max_format"), Integer.MAX_VALUE);
            if (var14[0] <= var15[0] && (var14[0] != var15[0] || var14[1] <= var15[1])) {
               return new int[]{var14[0] + (var14[1] == 0 ? 0 : 1), var15[0]};
            } else {
               throw new IllegalArgumentException();
            }
         } else {
            Object var2 = var0.get("formats");
            if (var2 instanceof Integer var5) {
               return new int[]{var5, var5};
            }
            if (var2 instanceof JSONArray var6 && var6.size() == 2 && var6.get(0) instanceof Integer var7 && var6.get(1) instanceof Integer var8) {
               return formatRangeOf(var7, var8);
            }
            if (var2 instanceof JSONObject var9 && var9.get("min_inclusive") instanceof Integer var10 && var9.get("max_inclusive") instanceof Integer var11) {
               return formatRangeOf(var10, var11);
            }
            throw new IllegalArgumentException();
         }
      } catch (RuntimeException var13) {
         throw new IllegalStateException(
            "Invalid overlay format range in final pack entry 'pack.mcmeta' for directory '" + var0.getString("directory") + "'", var13
         );
      }
   }

   private static int[] formatRangeOf(int var0, int var1) {
      if (var0 > var1) {
         throw new IllegalArgumentException();
      } else {
         return new int[]{var0, var1};
      }
   }

   static String overlayDirectoryName(JSONObject var0) {
      Object var1 = var0.get("directory");
      if (var1 instanceof String var2 && var2.matches("[-_a-zA-Z0-9.]+")) {
         return var2;
      } else {
         throw new IllegalStateException("Invalid overlay directory in final pack entry 'pack.mcmeta': '" + var1 + "'");
      }
   }

   static int parseFormatMajor(Object var0) {
      return parseFormatBounds(var0, 0)[0];
   }

   private static int[] parseFormatBounds(Object var0, int var1) {
      if (var2 instanceof Integer var4 && var4 >= 0) {
         return new int[]{var4, var1};
      }
      if (var2 instanceof JSONArray var5 && var5.size() >= 1 && var5.size() <= 256 && var5.stream().allMatch(var0x -> var0x instanceof Integer var1x && var1x >= 0)) {
         return new int[]{var5.getIntValue(0), var5.size() > 1 ? var5.getIntValue(1) : var1};
      }
      throw new IllegalArgumentException("Invalid pack format: " + var0);
   }

   private static Set toAssetPathSet(Set var0) {
      return var0.stream().map(ResourcePackGenerator::stripToAssetsRoot).filter(var0x -> var0x.startsWith("assets/")).collect(Collectors.toUnmodifiableSet());
   }

   private void collectMissingAssets(Set var1, Predicate var2) {
      this.skippedAssets.clear();

      for (String var4 : this.modelJsonCache.keySet()) {
         for (StringKey var6 : ((PackFileCache)this.modelJsonCache).getMetadata(var4).textures().values()) {
            String var7 = var6.value();
            if (var7 != null && !PackFileEntry.isSpecialRef(var7)) {
               String var8 = toNamespacedId(var7);
               if (!hasTextureFile(var1, var8) && !var2.test(var8) && !this.isDefinedAtlasSprite(var8)) {
                  this.skippedAssets.add(var8);
               }
            }
         }
      }
   }

   private static boolean hasTextureFile(Set var0, String var1) {
      String var2 = buildAssetFilePath(var1, PackAssetKind.TEXTURES, "png");
      return var0.contains(var2);
   }

   private void removeMissingSourceEntries(JSONObject var1) {
      if (var1 != null && var1.get("sources") instanceof JSONArray var2) {
         var2.removeIf(
            var1x -> var1x instanceof JSONObject var2x
               && var2x.containsKey("resource")
               && var2x.containsKey("sprite")
               && this.skippedAssets.contains(toNamespacedId(var2x.getString("resource")))
         );
      }
   }

   @Nullable
   private static JSONObject getJsonFromMap(Map var0, String var1) {
      PackEntry var2 = (PackEntry)var0.get(var1);
      return var2 != null && !var2.isRemoved() ? parsePackJson(var2, var1) : null;
   }

   @Nullable
   private static JSONObject getPackJson(PackBuildReport var0, String var1) {
      PackEntry var2 = var0.hB(var1);
      return var2 == null ? null : parsePackJson(var2, var1);
   }

   private static JSONObject parsePackJson(PackEntry var0, String var1) {
      return parseOrReuseJson(var0, var1, null);
   }

   private static JSONObject parseOrReuseJson(PackEntry var0, String var1, @Nullable JSONObject var2) {
      byte[] var3 = var0.getBytes();

      try {
         JSONObject var4;
         if (var2 != null && Arrays.equals(var3, JSON.toJSONBytes(var2, new Feature[]{Feature.LargeObject}))) {
            var4 = var2;
         } else {
            var4 = JSON.parseObject(var3);
         }

         if (Main.getInstance() == null) {
            var0.setBytes(var3);
         } else {
            var0.setStream(PackFileCache.storeModelJson(var3)::openDigesting);
         }

         return var4;
      } catch (JSONException var5) {
         throw new IllegalStateException("Malformed JSON in final pack entry '" + var1 + "'", var5);
      }
   }

   private static PackEntry toPackEntry(String var0, JSONObject var1) {
      byte[] var2 = JSON.toJSONBytes(var1, new Feature[]{Feature.LargeObject});
      if (Main.getInstance() == null) {
         return PackEntry.bytes(var0, var2);
      }

      try {
         return PackEntry.stream(var0, PackFileCache.storeModelJson(var2)::openDigesting);
      } catch (IOException var4) {
         throw new UncheckedIOException(var4);
      }
   }

   private static Map toSortedSets(Map var0) {
      TreeMap var1 = new TreeMap();
      var0.forEach((var1x, var2) -> var1.put(var1x, new TreeSet(var2)));
      return var1;
   }

   void validateAtlasPlan(Collection var1, Collection var2) {
      this.validateAtlasPlanWith(var1, var2, Log::warn);
   }

   void validateAtlasPlanWith(Collection var1, Collection var2, Consumer var3) {
      this.validateAtlasPlanWithOverrides(var1, var2, Map.of(), var3);
   }

   void validateAtlasPlanWithOverrides(Collection var1, Collection var2, Map var3, Consumer var4) {
      this.validateAtlasPlanInternal(var1, var2, var3, var4, null, List.of(Version.UNKNOWN), null);
   }

   private void validateAtlasPlanInternal(
      Collection var1, Collection var2, Map var3, Consumer var4, @Nullable PackReport var5, Collection var6, @Nullable PackResolverState var7
   ) {
      PackFileCache var8 = new PackFileCache(true);
      var3.forEach((var1x, var2x) -> var8.putJson(ItemContextBuilder.normalizeId(var1x), var2x));

      for (String var10 : this.modelJsonCache.keySet()) {
         String var11 = PackManifest.normalizeModelId(var10);
         var8.copyEntry(var11, (PackFileCache)this.modelJsonCache, var10, var10.startsWith("ia_overlay_modern_atlas/"));
      }

      TreeSet var23 = new TreeSet(var1);
      String var24 = "assets/minecraft/models/item/";

      for (String var12 : this.modelJsonCache.keySet()) {
         String var13 = stripToAssetsRoot(var12);
         if (var13.startsWith(var24) && var13.indexOf(47, var24.length()) < 0) {
            var23.add(PackManifest.normalizeModelId(var12));
            var23.addAll(((PackFileCache)this.modelJsonCache).getMetadata(var12).modelReferences());
         }
      }

      this.itemContextIndex = ItemContextBuilder.buildIndex(var8, var23, var2);
      this.modernModelCache.clear();
      this.modelFlags.clear();
      modelParents.clear();

      for (String var28 : var8.keySet()) {
         modelParents.put(var28, var8.getMetadata(var28).parent());
      }

      Set var27 = Set.copyOf(this.forcedModelIds);
      PackFileFilter var29 = createPackFileFilter();
      HashMap var30 = new HashMap();
      TreeMap var14 = new TreeMap();
      ArrayList var15 = new ArrayList();

      for (String var17 : this.modelJsonCache.keySet()) {
         String var18 = PackManifest.normalizeModelId(var17);
         boolean var19;
         if (this.itemContextIndex.sourcePaths().contains(var18)) {
            var19 = true;
         } else if (this.itemContextIndex.itemPaths().contains(var18)) {
            var19 = false;
         } else if (var29.bAG.contains(var18)) {
            var19 = false;
         } else {
            boolean var20 = PackManifest.isBlockModelId(var18);
            boolean var21 = !var20 && var27.contains(var18);
            var19 = var20 || var21;
            boolean var22 = var29.isInModelFolder(var18) || this.isInMetadataFolder(var17, var30);
            if (!var22) {
               var14.put(var18, var19);
               var15.add(this.createAssetEntry(var17, var18, var19, var21));
            }
         }

         if (var7 == null) {
            this.putPlannedModel(var17, var18, (JSONObject)this.modelJsonCache.get(var17), var19);
         } else {
            PackFileCache var41 = (PackFileCache)this.modelJsonCache;
            String var44 = var41.getDigest(var17);
            if (var7.bBy.containsKey(var44)) {
               modelParents.put(var18, var41.getMetadata(var17).parent());
               ((PackFileCache)this.modernModelCache).copyEntry(var17, var7.bBy, var44, true);
               this.modelFlags.put(var17, var19);
            } else {
               this.putPlannedModel(var17, var18, (JSONObject)this.modelJsonCache.get(var17), var19);
               var7.bBy.copyEntry(var44, (PackFileCache)this.modernModelCache, var17, true);
            }
         }
      }

      this.buildMetrics.bBb = this.buildMetrics.bBb + var14.size();
      if (!var14.isEmpty()) {
         if (var5 != null) {
            var5.addUnreferencedEntries(var6, var15);
         } else {
            PackReport var31 = new PackReport();
            var31.addUnreferencedModels(var6, var14);
            this.buildMetrics.bBc++;
            var4.accept(var31.formatForLog());
         }
      }

      this.itemContextIndex
         .forEachResolved((var1x, var2x) -> this.putPlannedModel("ia_overlay_modern_atlas/" + ItemContextBuilder.modelJsonPath(var1x), var1x, var2x, false));
      TreeSet var32 = new TreeSet();

      for (String var36 : this.modernModelCache.keySet()) {
         boolean var39 = (Boolean)this.modelFlags.get(var36);

         for (StringKey var45 : ((PackFileCache)this.modernModelCache).getMetadata(var36).textures().values()) {
            String var47 = var45.value();
            if (var47 != null && !PackFileEntry.isSpecialRef(var47)) {
               var32.add((var39 ? "b:" : "i:") + this.resolveTextureAlias(var47));
            }
         }
      }

      for (String var37 : var32) {
         boolean var40 = var37.startsWith("b:");
         String var43 = var37.substring(2);
         if (!this.ensureAtlasSprite(var43, var40)) {
            String var46 = nameRegistry.getAlias(var43, var40);
            if (var46 == null) {
               var46 = nameRegistry.obfuscateName(var43, var40);
            }

            if (var40) {
               this.addBlockAtlasAlias(var43, var46);
            } else {
               this.addItemAtlasAlias(var43, var46);
            }

            JSONObject var48 = new JSONObject();
            var48.put("type", "single");
            var48.put("resource", var43);
            var48.put("sprite", var46);
            if (!isHumanoidEquipmentRef(var43)) {
               this.blocksAtlas.getJSONArray("sources").add(var48);
            }
         }
      }

      for (String var38 : List.copyOf(this.modernModelCache.keySet())) {
         this.applyModelTexturesWithState(var38, (Boolean)this.modelFlags.get(var38), var7);
      }

      this.blockstateCache.clear();
      this.itemDefinitionCache.forEach((var1x, var2x) -> this.blockstateCache.put("ia_overlay_modern_atlas/" + var1x, this.rewriteModelRefs(var2x)));
   }

   private void buildHumanoidEquipment(List var1, Map var2, Map var3, Function var4, List var5) {
      LinkedHashMap var6 = new LinkedHashMap();

      for (CustomItemDataEntry var8 : var2.values()) {
         if (!var8.EV()) {
            addLeatherEquipmentTextures(
               var6,
               var8.EX().toString(),
               (File)var4.apply(ContentManager.method8194(var8.EX().getNamespace(), var8.EY())),
               (File)var4.apply(ContentManager.method8194(var8.EX().getNamespace(), var8.EZ())),
               null
            );
         }
      }

      for (LeatherColorDefinition var23 : var3.values()) {
         if (var23.apH == null && var23.apI == null && !var23.apK) {
            addLeatherEquipmentTextures(
               var6,
               var23.getNamespace() + ":ia_equipment_legacy/" + var23.getId(),
               var23.apF == null ? null : (File)var4.apply(var23.apF),
               var23.apG == null ? null : (File)var4.apply(var23.apG),
               var23
            );
         } else {
            Log.warn("[Pack] Humanoid equipment cannot reproduce shader-only legacy armor " + var23.getNamespacedID());
         }
      }

      LinkedHashMap var22 = new LinkedHashMap();
      LinkedHashMap var24 = new LinkedHashMap();
      Map var9 = scanEquipmentDefinitions(var5);
      HashSet var10 = new HashSet();
      Path var11 = Main.getInstance().getDataFolder().toPath().resolve("storage/cache/vanilla_assets/mojang");

      for (PackVersionSpec var13 : HumanoidEquipmentPack.buU) {
         BossBarManager.setTitleKeepProgress("Preparing humanoid equipment: " + var13.version());
         RemoteAssetRef var14 = VanillaAssetCache.loadVersionAsset(var11, var13.version());
         LinkedHashMap var15 = new LinkedHashMap(VanillaAssetCache.loadEquipmentAssetsDefault(var11, var14));
         var15.putAll(var6);

         for (Entry var17 : var9.entrySet()) {
            if (!var6.containsKey(var17.getKey())) {
               try {
                  Map var18 = PackImageUtils.buildEquipmentFiles(var13.version(), (String)var17.getKey(), (JSONObject)var17.getValue(), var15, var1x -> {
                     File var2x = (File)var4.apply(var1x);
                     if (var2x == null) {
                        return null;
                     }

                     try {
                        return Files.readAllBytes(var2x.toPath());
                     } catch (IOException var4x) {
                        throw new UncheckedIOException(var4x);
                     }
                  });
                  var15.remove(var17.getKey());
                  var15.putAll(var18);
               } catch (IOException | RuntimeException var20) {
                  var15.remove(var17.getKey());
                  if (var10.add((String)var17.getKey())) {
                     Log.warn("[Pack] Humanoid equipment unsupported " + (String)var17.getKey() + ": " + var20.getMessage());
                  }
               }
            }
         }

         PackBuildPlan var25 = HumanoidEquipmentBuilder.buildPlan(var13.version(), var15, var1);
         var22.put(var13.version(), var25.Tu());
         var25.To().forEach((var1x, var2x) -> {
            Float var3x = var24.putIfAbsent(var1x, var2x);
            if (var3x != null && Math.abs(var3x - var2x) > 1.0E-5F) {
               throw new IllegalStateException("Equipment geometry scale differs between recipes");
            }
         });
         JSONObject var26 = JSONObject.of("directory", var13.directoryName());
         setFormatsRange(var26, var13.major(), var13.major());
         var26.put("min_format", JSONArray.of(var13.major(), var13.minor()));
         var26.put("max_format", JSONArray.of(var13.major(), var13.minor()));
         this.bAx.getJSONObject("overlays").getJSONArray("entries").add(var26);
         var25.Ts().forEach((var2x, var3x) -> this.writer.addBytesTracked(var3x, var13.directoryName() + "/" + var2x));
         boolean var27 = var13.protocol() >= 774;
         JSONObject var19 = (JSONObject)JSON.copy(var27 ? (JSONObject)this.atlasSources.get("items.json") : this.blocksAtlas, new Feature[0]);
         var19.getJSONArray("sources").addAll(var25.Tt());
         this.writer
            .addJsonUnchanged(this.dedupeAtlasSources(var19), var13.directoryName() + "/assets/minecraft/atlases/" + (var27 ? "items" : "blocks") + ".json");
      }

      this.writer.addBytesTracked(HumanoidEquipmentPack.encodeManifest(var22, var24), "ia_humanoid_equipment.json");
   }

   void applyHumanoidEquipmentAtlases(Map var1) {
      this.rebuildHumanoidAtlasEntries(var1, HumanoidEquipmentPack.buU);
   }

   private void rebuildHumanoidAtlasEntries(Map var1, Collection var2) {
      Set var3 = var2.stream()
         .map(var0 -> var0.directoryName() + "/assets/minecraft/atlases/" + (var0.protocol() >= 774 ? "items" : "blocks") + ".json")
         .filter(var1x -> var1.containsKey(var1x) && !((PackEntry)var1.get(var1x)).isRemoved())
         .collect(Collectors.toSet());
      if (!var3.isEmpty()) {
         PackBuildReport var4 = PackBuildReport.method6512(var1, this.buildMetrics).method6513(var3);
         JSONObject var5 = getPackJson(var4, "pack.mcmeta");

         for (PackVersionSpec var7 : var2) {
            String var8 = "assets/minecraft/atlases/" + (var7.protocol() >= 774 ? "items" : "blocks") + ".json";
            String var9 = var7.directoryName() + "/" + var8;
            if (var3.contains(var9)) {
               Version var10 = Arrays.stream(Version.values())
                  .filter(var1x -> var1x.getProtocol() == var7.protocol() && ServerCompat.packVersionOf(var1x) == var7.major())
                  .findFirst()
                  .orElseThrow();
               String var11 = (String)buildOverlayMap(var4, var5, var10).get(var8);
               JSONObject var12 = var11 == null ? emptyAtlas() : getPackJson(var4, var11);
               JSONObject var13 = getJsonFromMap(var1, var9);
               var1.put(var9, toPackEntry(var9, this.dedupeAtlasSources(mergeEquipmentAtlas(var12, var13))));
            }
         }
      }
   }

   static JSONObject mergeEquipmentAtlas(JSONObject var0, JSONObject var1) {
      JSONObject var2 = withoutHumanoidSources(var0);

      for (Object var4 : var1.getJSONArray("sources")) {
         if (var4 instanceof JSONObject var5 && var5.get("resource") instanceof String var6 && isHumanoidEquipmentRef(var6)) {
            var2.getJSONArray("sources").add(var5);
         }
      }

      return var2;
   }

   private static Map scanEquipmentDefinitions(List var0) {
      LinkedHashMap var1 = new LinkedHashMap();

      for (File var3 : var0) {
         File[] var4 = var3.listFiles(File::isDirectory);
         if (var4 != null) {
            for (File var8 : var4) {
               Path var9 = var8.toPath().resolve("equipment");
               if (Files.isDirectory(var9)) {
                  try (Stream var10 = Files.walk(var9)) {
                     for (Path var12 : var10.filter(var0x -> Files.isRegularFile(var0x) && var0x.toString().endsWith(".json")).sorted().toList()) {
                        String var13 = "assets/" + var8.getName() + "/equipment/" + var9.relativize(var12).toString().replace(File.separatorChar, '/');
                        if (!var1.containsKey(var13)) {
                           try {
                              var1.put(var13, JSON.parseObject(Files.readAllBytes(var12)));
                           } catch (RuntimeException var16) {
                              Log.warn("[Pack] Invalid humanoid equipment definition " + var13 + ": " + var16.getMessage());
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var1;
   }

   private static void addLeatherEquipmentTextures(Map var0, String var1, @Nullable File var2, @Nullable File var3, @Nullable LeatherColorDefinition var4) {
      if (var2 != null && var3 != null) {
         LinkedHashMap var5 = new LinkedHashMap();
         JSONObject var6 = new JSONObject();

         for (Entry var8 : Map.of("humanoid", var2, "humanoid_leggings", var3).entrySet()) {
            File var9 = (File)var8.getValue();
            byte[] var10 = Files.readAllBytes(var9.toPath());
            String var11 = PackImageUtils.withSubfolder(var1, "entity/equipment/" + (String)var8.getKey() + "/");
            String var12 = PackImageUtils.texturePath(var11);
            var5.put(var12, var10);
            BufferedImage var13 = PackImageUtils.loadTexture(var5, var12);
            Path var14 = var9.toPath().resolveSibling(var9.getName() + ".mcmeta");
            byte[] var15 = Files.exists(var14) ? Files.readAllBytes(var14) : null;

            try {
               if (var4 != null) {
                  var15 = PackImageUtils.toAnimatedLegacyArmor(var13, var4.apL, var4.apM, var15);
               }

               PackImageUtils.validateArmorSheet(var13, var15);
            } catch (IOException | RuntimeException var17) {
               Log.warn("[Pack] Humanoid equipment invalid armor texture " + var1 + ": " + var17.getMessage());
               return;
            }

            if (var15 != null) {
               var5.put(var12 + ".mcmeta", var15);
            }

            var6.put((String)var8.getKey(), JSONArray.of(JSONObject.of("texture", var1)));
         }

         String[] var18 = var1.split(":", 2);
         var0.put("assets/" + var18[0] + "/equipment/" + var18[1] + ".json", JSON.toJSONBytes(JSONObject.of("layers", var6)));
         var0.putAll(var5);
      } else {
         Log.warn("[Pack] Humanoid equipment missing armor texture: " + var1);
      }
   }

   private static PackFileFilter createPackFileFilter() {
      if (!CustomEntityManager.isInitialized()) {
         return new PackFileFilter(Set.of(), Set.of());
      }

      Map var0 = CustomEntityManager.getInstance().oN;
      synchronized (var0) {
         List var2 = var0.values().stream().flatMap(var0x -> var0x.vn.AL.values().stream()).toList();
         return new PackFileFilter(collectEntityModelIds(var2), collectEntityFolders(var2));
      }
   }

   private boolean isInMetadataFolder(String var1, Map var2) {
      String var3 = this.bAm.getOrDefault(var1, var1);
      PackEntry var4 = this.buildReport == null ? null : this.buildReport.hB(var3);
      File var5 = var4 != null && this.writer != null ? this.writer.fileOf(var4, var3) : null;
      return isInMetadataDir(var5, var2);
   }

   static boolean isInMetadataDir(@Nullable File var0, Map var1) {
      if (var0 == null) {
         return false;
      }

      File var2 = var0.getParentFile();
      return var2 != null && var1.computeIfAbsent(var2, ResourcePackGenerator::hasMetadataMarker);
   }

   public static boolean isMetadataDir(@Nullable File var0) {
      return var0 != null && hasMetadataMarker(var0.getParentFile());
   }

   private static boolean hasMetadataMarker(@Nullable File var0) {
      return var0 != null && (new File(var0, ".metadata").isFile() || new File(var0, ".uuid").isFile());
   }

   static Set collectEntityModelIds(Collection var0) {
      return var0.stream()
         .filter(var0x -> !var0x.An.nO())
         .map(CustomEntityDefinition::nC)
         .map(ItemContextBuilder::normalizeId)
         .collect(Collectors.toUnmodifiableSet());
   }

   static Set collectEntityFolders(Collection var0) {
      return var0.stream()
         .map(CustomEntityDefinition::nC)
         .map(ItemContextBuilder::normalizeId)
         .filter(var0x -> var0x.lastIndexOf(47) > var0x.indexOf(58))
         .map(var0x -> var0x.substring(0, var0x.lastIndexOf(47)))
         .collect(Collectors.toUnmodifiableSet());
   }

   static Set collectEntityIds(Collection var0) {
      return var0.stream().map(CustomEntityDefinition::nC).map(ItemContextBuilder::normalizeId).collect(Collectors.toUnmodifiableSet());
   }

   private PackAssetEntry createAssetEntry(String var1, String var2, boolean var3, boolean var4) {
      String var5 = this.bAm.getOrDefault(var1, var1);
      PackEntry var6 = this.buildReport == null ? null : this.buildReport.hB(var5);
      PackEntrySource var7 = var6 != null && this.writer != null ? this.writer.sourceOf(var6, var5) : null;
      String var8 = var6 != null && this.writer != null ? Objects.requireNonNullElse(this.writer.fileNameOf(var6, var5), var5) : var5;
      PackOrigin var9 = resolveOrigin(var6 != null, var7, this.bAk.contains(var2), this.bzY.values().stream().anyMatch(var1x -> var1x.containsKey(var2)));
      String var10 = var3 ? "blockstate" : "item declaration";
      String var11;
      if (var9 == PackOrigin.ITEMSADDER_BUNDLED) {
         var11 = "no " + var10 + " root; bundled runtime support model";
      } else if (var9 == PackOrigin.ITEMSADDER_GENERATED) {
         var11 = "no " + var10 + " root; generated internal support model";
      } else if (var4) {
         var11 = "configuration fallback; no " + var10 + " root";
      } else {
         var11 = "folder fallback; no " + var10 + " root";
      }

      return new PackAssetEntry(var2, var3, var9, var5, var8, var11, var6 != null, false);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   static PackOrigin resolveOrigin(boolean var0, @Nullable PackEntrySource var1, boolean var2, boolean var3) {
      if (var0) {
         if (var1 == null) {
            return PackOrigin.EVENT_STREAM_OR_WRITER;
         }

         if (var1 != PackEntrySource.STREAM && var1 != PackEntrySource.WRITER) {
            return switch (SwitchMap74.bAC[var1.ordinal()]) {
               case 1 -> PackOrigin.ITEMSADDER_BUNDLED;
               case 2 -> PackOrigin.ITEMSADDER_GENERATED;
               case 3 -> PackOrigin.USER_CONTENT_PACK;
               case 4 -> PackOrigin.EXTERNAL_RESOURCE_PACK;
               case 5 -> PackOrigin.UNKNOWN;
               case 6, 7 -> throw new IllegalStateException("handled above");
               default -> throw new MatchException(null, null);
            };
         } else {
            return var2 ? PackOrigin.ITEMSADDER_GENERATED : PackOrigin.EVENT_STREAM_OR_WRITER;
         }
      } else if (var2) {
         return PackOrigin.ITEMSADDER_GENERATED;
      } else {
         return var3 ? PackOrigin.VANILLA_CLIENT_ASSET : PackOrigin.UNKNOWN;
      }
   }

   static Map planModelTextures(Map var0, Set var1, Map var2, Version var3, BiFunction var4) {
      PackFileCache var5 = new PackFileCache(true);
      HashMap var6 = new HashMap();
      var2.keySet().forEach(var1x -> var6.put(ItemContextBuilder.normalizeId(PackManifest.normalizeModelId(var1x)), var1x));
      BiFunction var7 = (var3x, var4x) -> {
         String var5x = (String)var6.get(ItemContextBuilder.normalizeId(var4x));
         return var5x != null ? (JSONObject)var2.get(var5x) : (JSONObject)var4.apply(var3x, var4x);
      };
      Function var8 = var4x -> {
         String var5x = (String)var6.get(ItemContextBuilder.normalizeId(var4x));
         if (var5x != null && var2 instanceof PackFileCache var7x) {
            return var7x.getMetadata(var5x);
         } else {
            JSONObject var6x = var5x != null ? (JSONObject)var2.get(var5x) : (JSONObject)var4.apply(var3, var4x);
            return var6x == null ? null : PackMetadata.fromModelJson(var6x);
         }
      };
      var0.values()
         .forEach(
            var6x -> {
               Set var7x = var6x.stream().map(ItemContextBuilder::normalizeId).collect(Collectors.toCollection(TreeSet::new));
               boolean var8x = var7x.stream().anyMatch(var0xx -> !var0xx.startsWith("minecraft:"));
               Set var9 = var7x.stream()
                  .filter(var2xx -> var2xx.startsWith("minecraft:block/") || var8x && hasBlockTextureInParents(var2xx, var8, new HashSet()))
                  .collect(Collectors.toCollection(TreeSet::new));
               if (!var9.isEmpty()) {
                  if (var9.size() != var7x.size() || !var7x.stream().noneMatch(var2xx -> var1.contains(var2xx) && var6.containsKey(var2xx))) {
                     var1.addAll(var9);
                     var9.stream().forEach(var4xx -> collectParentModels(var3, var4xx, var7, var6.keySet(), var5, new HashSet()));
                  }
               }
            }
         );
      return var5;
   }

   private static boolean hasBlockTextureInParents(String var0, Function var1, Set var2) {
      String var3 = ItemContextBuilder.normalizeId(var0);
      if (!var2.add(var3)) {
         return false;
      }

      PackMetadata var4 = (PackMetadata)var1.apply(var3);
      if (var4 == null) {
         return false;
      }

      if (var4.textures()
         .values()
         .stream()
         .map(StringKey::value)
         .filter(Objects::nonNull)
         .filter(var0x -> !PackFileEntry.isSpecialRef(var0x))
         .map(var0x -> (String)(var0x.contains(":") ? var0x : "minecraft:" + var0x))
         .anyMatch(var0x -> var0x.contains(":block/"))) {
         return true;
      }

      String var5 = var4.parent();
      return var5 != null && !var5.isBlank() && hasBlockTextureInParents(var5, var1, var2);
   }

   private static void collectParentModels(Version var0, String var1, BiFunction var2, Set var3, Map var4, Set var5) {
      String var6 = ItemContextBuilder.normalizeId(var1);
      if (!var4.containsKey(var6) && !VanillaAssetManager.isBuiltinPath(var6) && var5.add(var6)) {
         JSONObject var7 = (JSONObject)var2.apply(var0, var6);
         if (var7 != null) {
            if (!var3.contains(var6)) {
               var4.put(var6, var7);
            }

            String var8 = var7.getString("parent");
            if (var8 != null && !var8.isBlank()) {
               collectParentModels(var0, var8, var2, var3, var4, var5);
            }

            var5.remove(var6);
         }
      }
   }

   private void putPlannedModel(String var1, String var2, JSONObject var3, boolean var4) {
      JSONObject var5 = copyWithTextures(var3);
      registerModelJson(var2, var5);
      var1 = Utils.normalizeSlashes(var1);
      this.modernModelCache.put(var1, var5);
      this.modelFlags.put(var1, var4);
   }

   static JSONObject copyWithTextures(JSONObject var0) {
      JSONObject var1 = new JSONObject(var0);
      JSONObject var2 = var0.getJSONObject("textures");
      if (var2 == null) {
         return var1;
      }

      JSONObject var3 = new JSONObject(var2);
      var2.forEach((var1x, var2x) -> {
         if (var2x instanceof JSONObject var3x) {
            var3.put(var1x, new JSONObject(var3x));
         }
      });
      var1.put("textures", var3);
      return var1;
   }

   JSONObject getPlannedModel(String var1) {
      var1 = Utils.normalizeSlashes(var1);
      JSONObject var2 = (JSONObject)this.modernModelCache.get(var1);
      if (var2 == null) {
         throw new IllegalStateException("Modern atlas model was not planned before compression: '" + var1 + "'");
      } else {
         return var2;
      }
   }

   JSONObject getPlannedItemDefinition(String var1) {
      var1 = Utils.normalizeSlashes(var1);
      JSONObject var2 = (JSONObject)this.blockstateCache.get(var1);
      if (var2 == null) {
         throw new IllegalStateException("Modern item definition was not planned before compression: '" + var1 + "'");
      } else {
         return (JSONObject)JSON.copy(var2, new Feature[0]);
      }
   }

   private String resolveTextureAlias(String var1) {
      String var2 = toNamespacedId(var1);
      return !this.skippedAssets.contains(var2) && !var2.equals("minecraft:missingno") ? var2 : "minecraft:item/iamissing";
   }

   private static String toNamespacedId(String var0) {
      return var0.contains(":") ? var0 : "minecraft:" + var0;
   }

   @Nullable
   private static String textureSpriteOf(Object var0) {
      if (var0 instanceof JSONObject var1) {
         return var1.getString("sprite");
      } else {
         return var0 == null ? null : String.valueOf(var0);
      }
   }

   private static void setTextureSprite(Entry var0, String var1) {
      if (var0.getValue() instanceof JSONObject var2) {
         var2.put("sprite", var1);
      } else {
         var0.setValue(var1);
      }
   }

   private void rewriteTextures(String var1, JSONObject var2, boolean var3) {
      JSONObject var4 = var2.getJSONObject("textures");
      if (var4 != null) {
         for (Entry var6 : var4.entrySet()) {
            String var7 = textureSpriteOf(var6.getValue());
            if (var7 == null || var7.isBlank()) {
               throw new IllegalStateException("Cannot read texture in model '" + var1 + "': " + var6.getValue());
            }

            if (!PackFileEntry.isSpecialRef(var7)) {
               setTextureSprite(var6, this.resolveAtlasTexture(var1, var7, var3));
            }
         }
      }
   }

   private String resolveAtlasTexture(String var1, String var2, boolean var3) {
      String var4 = this.resolveTextureAlias(var2);
      JSONObject var5 = (JSONObject)this.atlasSources.get(var3 ? "blocks.json" : "items.json");
      if (this.findAtlasSourceFor(var5, var4) != null) {
         return var4;
      } else {
         String var6 = nameRegistry.getAlias(var4, var3);
         if (var6 == null) {
            throw new IllegalStateException(
               "Modern atlas alias was not planned for model '" + var1 + "', texture '" + var4 + "', context '" + (var3 ? "block" : "item") + "'"
            );
         } else {
            return var6;
         }
      }
   }

   private void applyModelTextures(String var1, boolean var2) {
      this.applyModelTexturesWithState(var1, var2, null);
   }

   private void applyModelTexturesWithState(String var1, boolean var2, @Nullable PackResolverState var3) {
      PackMetadata var4 = ((PackFileCache)this.modernModelCache).getMetadata(var1);
      if (!var4.textures().values().stream().allMatch(var0 -> var0.value() != null && !var0.value().isBlank() && PackFileEntry.isSpecialRef(var0.value()))) {
         PackOverlayEntry var5 = null;
         if (var3 != null) {
            TreeMap var6 = new TreeMap();
            boolean var7 = true;

            for (Entry var9 : var4.textures().entrySet()) {
               String var10 = ((StringKey)var9.getValue()).value();
               if (var10 == null || var10.isBlank()) {
                  var7 = false;
                  break;
               }

               if (!PackFileEntry.isSpecialRef(var10)) {
                  var6.put((String)var9.getKey(), this.resolveAtlasTexture(var1, var10, var2));
               }
            }

            if (var7) {
               var5 = new PackOverlayEntry(((PackFileCache)this.modernModelCache).getDigest(var1), var2, Map.copyOf(var6));
               String var13 = (String)var3.bBA.get(var5);
               if (var13 != null) {
                  ((PackFileCache)this.modernModelCache).copyEntry(var1, var3.bBz, var13, true);
                  return;
               }
            }
         }

         JSONObject var11 = (JSONObject)this.modernModelCache.get(var1);
         this.rewriteTextures(var1, var11, var2);
         ((PackFileCache)this.modernModelCache).putBytes(var1, JSON.toJSONBytes(var11, new Feature[]{Feature.LargeObject}));
         if (var5 != null) {
            String var12 = Integer.toString(var3.bBA.size());
            var3.bBz.copyEntry(var12, (PackFileCache)this.modernModelCache, var1, true);
            var3.bBA.put(var5, var12);
         }
      }
   }

   void planAtlasEntries(Map var1, Map var2, Predicate var3) {
      this.validateAtlasEntries(var1, var2, var3, null, null);
   }

   void planAtlasEntriesWith(Map var1, Map var2, Predicate var3, Function var4) {
      this.validateAtlasEntriesWith(var1, var2, var3, var4, null, null);
   }

   void planAtlasEntriesWithContext(Map var1, Map var2, Predicate var3, BiFunction var4, Collection var5, @Nullable Set var6, @Nullable Predicate var7) {
      PackReport var8 = new PackReport();

      try {
         this.resolveAtlasEntriesForVersions(var1, var2, var3, var4, var5, var6, var7, var8);
      } finally {
         this.flushPackReport(var8);
      }
   }

   private void resolveAtlasEntriesForVersions(
      Map var1, Map var2, Predicate var3, BiFunction var4, Collection var5, @Nullable Set var6, @Nullable Predicate var7, PackReport var8
   ) {
      Set var9 = var5.stream().filter(var0 -> var0 != Version.UNKNOWN).map(ServerCompat::packVersionOf).collect(Collectors.toSet());
      if (var9.size() > 1) {
         this.issueLog = new PackIssueLog();
         this.issueLog
            .addIssue(
               PackIssueCategory.VANILLA_VERSION_SELECTION_ERROR,
               PackResolutionKind.CACHED_VANILLA_ASSET,
               "one selected declaration view cannot be validated across different pack formats " + var9 + "; select declarations separately for each format"
            );
         throw buildFatalIssueException(this.issueLog);
      }

      PackIssueLog var10 = new PackIssueLog();

      for (Version var12 : var5) {
         Function var13 = var2x -> (JSONObject)var4.apply(var12, var2x);
         PackIssueLog var14 = this.buildAtlasIssueLog(var1, var2, var3, var13, var6, var7, var12);
         var14.getResults().forEach(var3x -> {
            var8.addResult(var12, var3x);
            var10.addIssue(var3x.kind(), var3x.provenance(), "Vanilla client assets " + var12.getName() + ": " + var3x.message());
         });
      }

      this.issueLog = var10;
      if (var10.hasFatal()) {
         throw buildFatalIssueException(var10);
      }
   }

   private void flushPackReport(PackReport var1) {
      if (!var1.isEmpty()) {
         if (var1.hasUnreferenced()) {
            this.buildMetrics.bBc++;
         }

         PackWarningSummary.addWarning("Modern atlas", ChatColor.stripColor(var1.format(true)));
         Consumer var2 = var1.hasWarnings() ? this.warnSink : this.infoSink;
         var2.accept(var1.buildDiagnosticsSummary("ItemsAdder/output/resource-pack-diagnostics.txt"));
      }
   }

   private Set collectRemappedModelIds(PackBuildReport var1) {
      if (this.manifestJsons != null && !this.manifestJsons.isEmpty()) {
         HashSet var2 = new HashSet();

         for (String var4 : this.manifestJsons.keySet()) {
            String var5 = (String)var1.bAN.get(var4);
            if (var5 == null) {
               var5 = Utils.normalizeSlashes(var4);
            }

            var2.add(ItemContextBuilder.normalizeId(PackManifest.normalizeModelId(var5)));
         }

         return Set.copyOf(var2);
      } else {
         return Set.of();
      }
   }

   PackBuildMetricsSnapshot getBuildMetricsSnapshot() {
      return this.buildMetrics.WJ();
   }

   void resetBuildState() {
      this.clearPlannerCaches();
      this.bAk = Set.of();
      this.blocksAtlas = null;
   }

   private void clearPlannerCaches() {
      this.atlasModelRefs.clear();
      this.blockstateModelRefs.clear();
      this.itemDefinitionCache.clear();
      this.blockstateCache.clear();
      this.bzY.clear();
      this.itemContextIndex = null;
      this.modelJsonCache.clear();
      this.modelSourceCache.clear();
      this.modernModelCache.clear();
      this.modelFlags.clear();
      this.bAj.clear();
      if (this.atlasSources != null) {
         this.atlasSources.clear();
      }

      this.pendingAtlasKeys.clear();
      this.skippedAssets.clear();
      nameRegistry.clear();
   }

   void validateAtlasEntries(Map var1, Map var2, Predicate var3, @Nullable Set var4, @Nullable Predicate var5) {
      this.validateAtlasEntriesWith(var1, var2, var3, var0 -> null, var4, var5);
   }

   private void validateAtlasEntriesWith(Map var1, Map var2, Predicate var3, Function var4, @Nullable Set var5, @Nullable Predicate var6) {
      this.issueLog = this.buildAtlasIssueLog(var1, var2, var3, var4, var5, var6, Version.UNKNOWN);
      if (this.issueLog.hasFatal()) {
         throw buildFatalIssueException(this.issueLog);
      }
   }

   PackIssueLog getIssueLog() {
      PackIssueLog var1 = new PackIssueLog();
      var1.mergeFrom(this.issueLog);
      return var1;
   }

   private PackIssueLog buildAtlasIssueLog(Map var1, Map var2, Predicate var3, Function var4, @Nullable Set var5, @Nullable Predicate var6, Version var7) {
      PackIssueLog var8 = new PackIssueLog();
      PackResolver var9 = new PackResolver(this, var3, var4);
      Set var10 = collectAtlasSpriteIds((JSONObject)this.atlasSources.get("items.json"));
      Set var11 = collectAtlasSpriteIds((JSONObject)this.atlasSources.get("blocks.json"));
      TreeSet var12 = new TreeSet(var10);
      var12.retainAll(var11);
      if (!var12.isEmpty()) {
         var8.addIssue(PackIssueCategory.PLANNER_FATAL, PackResolutionKind.PLANNER_REMAP, "sprite IDs are emitted by both items.json and blocks.json: " + var12);
      }

      var1.forEach((var6x, var7x) -> this.validateItemDeclaration(var6x, var7x, var9, var5, var6, var7, var8));
      var2.forEach((var6x, var7x) -> var7x.forEach(var7xx -> this.validateAtlasReference(var6x, var7xx, false, var9, var5, var6, var7, var8)));
      if (var5 != null && var6 != null) {
         this.validateAtlasSources((JSONObject)this.atlasSources.get("items.json"), false, var5, var6, var8);
         this.validateAtlasSources((JSONObject)this.atlasSources.get("blocks.json"), true, var5, var6, var8);
      }

      return var8;
   }

   private void validateItemDeclaration(String var1, Set var2, PackResolver var3, @Nullable Set var4, @Nullable Predicate var5, Version var6, PackIssueLog var7) {
      TreeSet var8 = new TreeSet();
      TreeSet var9 = new TreeSet();

      for (String var11 : new TreeSet(var2)) {
         String var12 = ItemContextBuilder.normalizeId(var11);
         String var13 = this.itemContextIndex.resolvedNames().getOrDefault(var12, var12);
         PackResolutionKind var14 = var13.equals(var12) ? PackResolutionKind.SELECTED_PACK_ENTRY : PackResolutionKind.PLANNER_REMAP;
         Set var15 = ItemContextBuilder.collectMetadataTextures(var13, var1x -> var3.resolveEntry(var1x).Xb());
         if (!var15.isEmpty()) {
            var7.addValidationIssue(
               !this.itemContextIndex.itemPaths().contains(var12) && !this.itemContextIndex.resolvedNames().containsValue(var13)
                  ? PackIssueCategory.PRE_EXISTING_WARNING
                  : PackIssueCategory.PLANNER_FATAL,
               var14,
               this.createValidationError(var1, var11, var13, List.of(var13), "item model has unresolved face texture slots " + var15)
            );
         }

         this.validateModelTree(var1, var11, var13, true, var14, var3, new HashSet(), new ArrayList(), Set.of(), var8, var9, var4, var5, var6, var7);
      }

      if (!var8.isEmpty() && !var9.isEmpty()) {
         var7.addValidationIssue(
            PackIssueCategory.PLANNER_FATAL,
            PackResolutionKind.PLANNER_REMAP,
            this.createValidationError(
               var1,
               String.join(", ", new TreeSet<>(var2)),
               "<declaration>",
               List.of(),
               "item declaration mixes atlas resources; item textures=" + var8 + ", block textures=" + var9
            )
         );
      }
   }

   private void validateAtlasReference(
      String var1, String var2, boolean var3, PackResolver var4, @Nullable Set var5, @Nullable Predicate var6, Version var7, PackIssueLog var8
   ) {
      String var9 = ItemContextBuilder.normalizeId(var2);
      PackResolutionKind var10 = PackResolutionKind.SELECTED_PACK_ENTRY;
      if (var3) {
         String var11 = this.itemContextIndex.resolvedNames().getOrDefault(var9, var9);
         if (!var11.equals(var9)) {
            var10 = PackResolutionKind.PLANNER_REMAP;
         }

         var9 = var11;
      }

      TreeSet var14 = new TreeSet();
      TreeSet var12 = new TreeSet();
      this.validateModelTree(var1, var2, var9, var3, var10, var4, new HashSet(), new ArrayList(), Set.of(), var14, var12, var5, var6, var7, var8);
      if (var3 && !var12.isEmpty() || !var3 && !var14.isEmpty()) {
         String var13 = var3 ? "item" : "block";
         var8.addValidationIssue(
            PackIssueCategory.PLANNER_FATAL,
            var10,
            this.createValidationError(
               var1, var2, var9, List.of(var9), "expected only " + var13 + " atlas aliases; item textures=" + var14 + ", block textures=" + var12
            )
         );
      }
   }

   private void validateModelTree(
      String var1,
      String var2,
      String var3,
      boolean var4,
      PackResolutionKind var5,
      PackResolver var6,
      Set var7,
      List var8,
      Set var9,
      Set var10,
      Set var11,
      @Nullable Set var12,
      @Nullable Predicate var13,
      Version var14,
      PackIssueLog var15
   ) {
      var3 = ItemContextBuilder.normalizeId(var3);
      if (var7.add(var3)) {
         this.buildMetrics.bAU++;
         ArrayList var16 = new ArrayList(var8);
         var16.add(var3);
         PackResolutionEntry var17 = var6.resolveEntry(var3);
         PackMetadata var18 = var17.Xb();
         PackResolutionKind var19 = var17.UO();
         if (var18 != null && var19 != PackResolutionKind.CACHED_VANILLA_ASSET) {
            this.buildMetrics.bAV++;
         }

         if (var18 == null) {
            if (!var6.matchesFilter(var3)) {
               boolean var29 = this.itemContextIndex.itemPaths().contains(var3)
                  || this.itemContextIndex.sourcePaths().contains(var3)
                  || this.itemContextIndex.resolvedNames().containsValue(var3);
               boolean var31 = var29 && var14 != Version.UNKNOWN && var3.startsWith("minecraft:") && !var6.isPlannedModel(var3);
               if (var5 != PackResolutionKind.PLANNER_REMAP && var5 != PackResolutionKind.PLANNER_ITEM_VARIANT && !var29) {
                  var15.addValidationIssue(
                     PackIssueCategory.PRE_EXISTING_WARNING,
                     PackResolutionKind.ORIGINAL_UNRESOLVED,
                     this.createValidationError(
                        var1,
                        var2,
                        var3,
                        var16,
                        "original model reference is unresolved; no selected pack entry"
                           + (var14 == Version.UNKNOWN ? "" : " or matching Vanilla model for " + var14.getName())
                     )
                  );
               } else {
                  var15.addValidationIssue(
                     var31 ? PackIssueCategory.VANILLA_VERSION_SELECTION_ERROR : PackIssueCategory.PLANNER_FATAL,
                     var31 ? PackResolutionKind.CACHED_VANILLA_ASSET : (var29 ? var6.resolveKind(var3) : var5),
                     this.createValidationError(var1, var2, var3, var16, "planner-generated model target is absent")
                  );
               }
            }
         } else {
            Map var20 = var18.textures();
            if (!var20.isEmpty()) {
               for (Entry var22 : var20.entrySet()) {
                  if (!var9.contains(var22.getKey())) {
                     String var23 = ((StringKey)var22.getValue()).value();
                     if (var23 == null || var23.isBlank()) {
                        JSONObject var36 = var6.resolveJson(var3);
                        JSONObject var39 = var36 == null ? null : var36.getJSONObject("textures");
                        Object var40 = var39 == null ? null : var39.get((String)var22.getKey());
                        var15.addValidationIssue(
                           var19 == PackResolutionKind.CACHED_VANILLA_ASSET ? PackIssueCategory.PRE_EXISTING_WARNING : PackIssueCategory.PLANNER_FATAL,
                           var19,
                           this.createValidationError(var1, var2, var3, var16, "cannot read texture value '" + var40 + "'")
                        );
                     } else if (!PackFileEntry.isSpecialRef(var23)) {
                        String var24 = var3 + "=" + var23;
                        String var25 = toNamespacedId(var23);
                        boolean var26 = this.findAtlasSourceFor((JSONObject)this.atlasSources.get(var4 ? "items.json" : "blocks.json"), var25) != null;
                        if (var26) {
                           if (var4) {
                              var10.add(var24);
                           } else {
                              var11.add(var24);
                           }
                        } else if (nameRegistry.getOriginal(var23, false) != null) {
                           var10.add(var24);
                           if (!var4) {
                              var15.addValidationIssue(
                                 PackIssueCategory.PLANNER_FATAL,
                                 var19,
                                 this.createValidationError(
                                    var1, var2, var3, var16, "conflicting textures: block tree resolved item atlas alias '" + var23 + "'"
                                 )
                              );
                           }
                        } else if (nameRegistry.getOriginal(var23, true) != null) {
                           var11.add(var24);
                           if (var4) {
                              var15.addValidationIssue(
                                 PackIssueCategory.PLANNER_FATAL,
                                 var19,
                                 this.createValidationError(
                                    var1, var2, var3, var16, "conflicting textures: item tree resolved block atlas alias '" + var23 + "'"
                                 )
                              );
                           }
                        } else if (var19 == PackResolutionKind.CACHED_VANILLA_ASSET) {
                           if (var25.equals("minecraft:missingno")) {
                              var10.add(var3 + "=minecraft:item/iamissing");
                              var15.addIssue(
                                 PackIssueCategory.INTENTIONAL_MISSING_FALLBACK,
                                 PackResolutionKind.INTENTIONAL_MISSING_FALLBACK,
                                 "minecraft:missingno is normalized to the item-context fallback minecraft:item/iamissing"
                              );
                           } else if (!hasItemAtlasEntry((JSONObject)this.atlasSources.get("items.json"), var25)
                              && !hasItemAtlasEntry(TRIM_PALETTE_SOURCES, var25)) {
                              if (!hasItemAtlasEntry((JSONObject)this.atlasSources.get("blocks.json"), var25) && !hasItemAtlasEntry(BLOCK_ATLAS_SOURCES, var25)
                                 )
                               {
                                 var15.addValidationIssue(
                                    PackIssueCategory.PRE_EXISTING_WARNING,
                                    var19,
                                    this.createValidationError(
                                       var1, var2, var3, var16, "texture '" + var23 + "' is not selected by the effective item or block atlas"
                                    )
                                 );
                              } else {
                                 var11.add(var24);
                              }
                           } else {
                              var10.add(var24);
                           }
                        } else {
                           var15.addValidationIssue(
                              PackIssueCategory.PLANNER_FATAL,
                              var19,
                              this.createValidationError(var1, var2, var3, var16, "texture was not rewritten to a planned atlas alias: '" + var23 + "'")
                           );
                        }

                        String var27 = var26 ? var25 : getOriginalAtlasName(var23, var4);
                        if ("minecraft:item/iamissing".equals(var27)) {
                           var15.addIssue(
                              PackIssueCategory.INTENTIONAL_MISSING_FALLBACK,
                              PackResolutionKind.INTENTIONAL_MISSING_FALLBACK,
                              "atlas context '" + (var4 ? "item" : "block") + "' uses the intentional missing fallback minecraft:item/iamissing"
                           );
                        }

                        if (var12 != null
                           && var13 != null
                           && var27 != null
                           && !"minecraft:item/iamissing".equals(var27)
                           && !this.isAllowedSprite(var27, var12, var13)) {
                           var15.addValidationIssue(
                              this.hasRemappedSprite(var27, var23) ? PackIssueCategory.PLANNER_FATAL : PackIssueCategory.PRE_EXISTING_WARNING,
                              this.hasRemappedSprite(var27, var23) ? PackResolutionKind.PLANNER_REMAP : PackResolutionKind.SELECTED_PACK_ENTRY,
                              this.createValidationError(
                                 var1,
                                 var2,
                                 var3,
                                 var16,
                                 "texture reference '"
                                    + var23
                                    + "', resolved resource location '"
                                    + var27
                                    + "', atlas context '"
                                    + (var4 ? "item" : "block")
                                    + "' has no selected pack entry '"
                                    + buildAssetFilePath(var27, PackAssetKind.TEXTURES, "png")
                                    + "'"
                              )
                           );
                        }
                     }
                  }
               }
            }

            String var30 = var18.parent();
            if (var30 != null && !var30.isBlank()) {
               String var32 = ItemContextBuilder.normalizeId(var30);
               PackResolutionKind var34 = this.kindForModel(var32);
               HashSet var37 = new HashSet(var9);
               if (!var20.isEmpty()) {
                  var37.addAll(var20.keySet());
               }

               this.validateModelTree(var1, var2, var30, var4, var34, var6, var7, var16, var37, var10, var11, var12, var13, var14, var15);
            }

            for (String var35 : var18.modernModelReferences()) {
               String var38 = ItemContextBuilder.normalizeId(var35);
               this.validateModelTree(var1, var2, var35, var4, this.kindForModel(var38), var6, var7, var16, Set.of(), var10, var11, var12, var13, var14, var15);
            }
         }
      }
   }

   private PackResolutionKind kindForModel(String var1) {
      return this.itemContextIndex.resolvedNames().containsValue(var1) ? PackResolutionKind.PLANNER_REMAP : PackResolutionKind.SELECTED_PACK_ENTRY;
   }

   private static List collectModelNamesWithoutOverrides(JSONObject var0) {
      JSONObject var1 = new JSONObject();
      var1.putAll(var0);
      var1.remove("overrides");
      return ListCollector.collectModelNames(var1);
   }

   static boolean hasItemAtlasEntry(JSONObject var0, String var1) {
      return hasAtlasSourceFor(var0, var1, true);
   }

   static boolean hasAtlasSprite(JSONObject var0, String var1) {
      return findPermutationSource(var0, var1) != null;
   }

   private static boolean hasAtlasSourceFor(JSONObject var0, String var1, boolean var2) {
      if (var0 != null && var0.get("sources") instanceof JSONArray var3) {
         String var15 = toNamespacedId(var1);
         if (findPermutationSource(var0, var15) != null) {
            return true;
         }

         if (!var2) {
            return false;
         }

         int var5 = var15.indexOf(58);
         String var6 = var15.substring(0, var5);
         String var7 = var15.substring(var5 + 1);

         for (Object var9 : var3) {
            if (var9 instanceof JSONObject var10) {
               String var11 = var10.getString("type");
               if (var11 != null) {
                  var11 = var11.substring(var11.indexOf(58) + 1);
                  if ("directory".equals(var11)) {
                     String var17 = toNamespacedId(var10.getString("source"));
                     String var13 = var17.substring(0, var17.indexOf(58));
                     String var14 = StringUtils.strip(StringUtils.defaultString(var10.getString("prefix")), "/");
                     if (var6.equals(var13) && (var14.isEmpty() || var7.equals(var14) || var7.startsWith(var14 + "/"))) {
                        return true;
                     }
                  } else if ("single".equals(var11)) {
                     String var12 = var10.getString("sprite");
                     if (var12 == null) {
                        var12 = var10.getString("resource");
                     }

                     if (var12 != null && var15.equals(toNamespacedId(var12))) {
                        return true;
                     }
                  }
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   @Nullable
   private static JSONObject findPermutationSource(JSONObject var0, String var1) {
      if (var0 != null && var0.get("sources") instanceof JSONArray var2) {
         String var14 = toNamespacedId(var1);

         for (Object var5 : var2) {
            if (var5 instanceof JSONObject var6) {
               String var7 = var6.getString("type");
               if (var7 != null
                  && "paletted_permutations".equals(var7.substring(var7.indexOf(58) + 1))
                  && var6.get("textures") instanceof JSONArray var8
                  && var6.get("permutations") instanceof JSONObject var9) {
                  String var16 = StringUtils.defaultString(var6.getString("separator"), "_");

                  for (Object var12 : var8) {
                     String var13 = toNamespacedId(String.valueOf(var12));
                     if (var9.keySet().stream().anyMatch(var3 -> var14.equals(var13 + var16 + var3))) {
                        return var6;
                     }
                  }
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   @Nullable
   private JSONObject findAtlasSourceFor(JSONObject var1, String var2) {
      return var1 == null ? null : (JSONObject)this.bAj.computeIfAbsent(var1, ResourcePackGenerator::buildPermutationIndex).get(toNamespacedId(var2));
   }

   private static Map buildPermutationIndex(JSONObject var0) {
      HashMap var1 = new HashMap();
      if (!(var0.get("sources") instanceof JSONArray var2)) {
         return var1;
      } else {
         for (Object var4 : var2) {
            if (var4 instanceof JSONObject var5) {
               String var6 = var5.getString("type");
               if (var6 != null
                  && "paletted_permutations".equals(var6.substring(var6.indexOf(58) + 1))
                  && var5.get("textures") instanceof JSONArray var7
                  && var5.get("permutations") instanceof JSONObject var8) {
                  String var15 = StringUtils.defaultString(var5.getString("separator"), "_");

                  for (Object var11 : var7) {
                     String var12 = toNamespacedId(String.valueOf(var11));
                     var8.keySet().forEach(var4x -> var1.putIfAbsent(var12 + var15 + var4x, var5));
                  }
               }
            }
         }

         return var1;
      }
   }

   private boolean ensureAtlasSprite(String var1, boolean var2) {
      JSONObject var3 = (JSONObject)this.atlasSources.get(var2 ? "blocks.json" : "items.json");
      JSONObject var4 = this.findAtlasSourceFor(var3, var1);
      if (var4 == null) {
         var4 = this.findAtlasSourceFor((JSONObject)this.atlasSources.get(var2 ? "items.json" : "blocks.json"), var1);
      }

      if (var4 == null) {
         var4 = this.findAtlasSourceFor(this.blocksAtlas, var1);
      }

      if (var4 == null) {
         var4 = this.findAtlasSourceFor(TRIM_PALETTE_SOURCES, var1);
      }

      if (var4 == null) {
         var4 = this.findAtlasSourceFor(BLOCK_ATLAS_SOURCES, var1);
      }

      if (var4 == null) {
         return false;
      }

      if (this.findAtlasSourceFor(var3, var1) == null) {
         var3.getJSONArray("sources").add(JSON.copy(var4, new Feature[0]));
         this.bAj.remove(var3);
      }

      if (!isHumanoidEquipmentRef(var1) && this.findAtlasSourceFor(this.blocksAtlas, var1) == null) {
         this.blocksAtlas.getJSONArray("sources").add(JSON.copy(var4, new Feature[0]));
         this.bAj.remove(this.blocksAtlas);
      }

      return true;
   }

   boolean isDefinedAtlasSprite(String var1) {
      return this.writer != null && this.writer.stringEntries.containsValue(buildAssetFilePath(toNamespacedId(var1), PackAssetKind.TEXTURES, "png"))
         || this.reservedAssetIds.contains(var1)
         || this.atlasSources != null && this.atlasSources.values().stream().anyMatch(var2 -> this.findAtlasSourceFor(var2, var1) != null)
         || this.findAtlasSourceFor(this.blocksAtlas, var1) != null
         || this.findAtlasSourceFor(TRIM_PALETTE_SOURCES, var1) != null
         || this.findAtlasSourceFor(BLOCK_ATLAS_SOURCES, var1) != null;
   }

   static List getVersionsFrom1_21_11() {
      List var0 = Arrays.stream(Version.values()).filter(var0x -> var0x.getOrdinal() >= Version.v1_21_11.getOrdinal()).toList();
      List var1 = var0.stream().filter(var0x -> !VanillaAssetManager.ip.contains(var0x)).toList();
      if (!var1.isEmpty()) {
         throw new IllegalStateException(
            "Cached Vanilla assets are unavailable for supported modern client versions: " + var1.stream().map(Version::getName).toList()
         );
      } else {
         return var0;
      }
   }

   @Nullable
   private JSONObject getVanillaModelJson(Version var1, String var2) {
      String var3 = ItemContextBuilder.normalizeId(var2);
      if (!VanillaAssetManager.isBuiltinPath(var3) && var3.startsWith("minecraft:")) {
         Map var4 = this.bzY.computeIfAbsent(var1, var0 -> new PackFileCache(true));
         return var4.computeIfAbsent(var3, var1x -> {
            JSONObject var2x = ContentManager.method8084().field91.getModelJsonFor(var1, var1x);
            return var2x == null ? null : var2x;
         });
      } else {
         return null;
      }
   }

   @Nullable
   private static String getOriginalAtlasName(String var0, boolean var1) {
      return nameRegistry.getOriginal(var0, !var1);
   }

   private void validateAtlasSources(JSONObject var1, boolean var2, Set var3, Predicate var4, PackIssueLog var5) {
      if (var1 != null) {
         Object var7 = var1.get("sources");
         if (var7 instanceof JSONArray) {
            for (Object var8 : (JSONArray)var7) {
               if (var8 instanceof JSONObject var9 && var9.containsKey("resource") && var9.containsKey("sprite")) {
                  String var10 = toNamespacedId(var9.getString("resource"));
                  if (this.isAllowedSprite(var10, var3, var4)) {
                     if ("minecraft:item/iamissing".equals(var10)) {
                        var5.addIssue(
                           PackIssueCategory.INTENTIONAL_MISSING_FALLBACK,
                           PackResolutionKind.INTENTIONAL_MISSING_FALLBACK,
                           "atlas context '"
                              + (var2 ? "block" : "item")
                              + "' intentionally maps sprite '"
                              + var9.getString("sprite")
                              + "' to minecraft:item/iamissing"
                        );
                     }
                  } else {
                     boolean var11 = this.hasRemappedSprite(var10, var9.getString("sprite"));
                     var5.addIssue(
                        var11 ? PackIssueCategory.PLANNER_FATAL : PackIssueCategory.PRE_EXISTING_WARNING,
                        var11 ? PackResolutionKind.PLANNER_REMAP : PackResolutionKind.SELECTED_PACK_ENTRY,
                        "atlas texture reference '"
                           + var9.getString("sprite")
                           + "', resolved resource location '"
                           + var10
                           + "', atlas context '"
                           + (var2 ? "block" : "item")
                           + "' has no selected pack entry '"
                           + buildAssetFilePath(var10, PackAssetKind.TEXTURES, "png")
                           + "'"
                     );
                  }
               }
            }

            return;
         }
      }
   }

   private boolean hasRemappedSprite(String var1, String var2) {
      return this.pendingAtlasKeys.contains("rs:" + var1 + "->" + var2);
   }

   private boolean isAllowedSprite(String var1, Set var2, Predicate var3) {
      return "minecraft:item/iamissing".equals(var1) || hasTextureFile(var2, var1) || var3.test(var1) || this.isDefinedAtlasSprite(var1);
   }

   private PackValidationError createValidationError(String var1, String var2, String var3, List var4, String var5) {
      String var6 = this.bAm.getOrDefault(var1, var1);
      PackEntry var7 = this.buildReport == null ? null : this.buildReport.hB(var6);
      String var8 = var7 != null && this.writer != null ? Objects.requireNonNullElse(this.writer.fileNameOf(var7, var6), var1) : var1;
      return new PackValidationError(var8, var2, var3, List.copyOf(var4), var5);
   }

   private static IllegalStateException buildFatalIssueException(PackIssueLog var0) {
      String var1 = var0.getResults()
         .stream()
         .filter(PackResolutionResult::isFatal)
         .map(var0x -> "[" + var0x.kind() + "/" + var0x.provenance() + "] " + var0x.message())
         .collect(Collectors.joining("\n - ", "\n - ", ""));
      return new IllegalStateException("Modern atlas validation failed:" + var1);
   }

   private static Set collectAtlasSpriteIds(JSONObject var0) {
      TreeSet var1 = new TreeSet();
      if (var0 != null) {
         Object var3 = var0.get("sources");
         if (var3 instanceof JSONArray) {
            for (Object var4 : (JSONArray)var3) {
               if (var4 instanceof JSONObject var5 && var5.containsKey("resource")) {
                  var1.add(var5.getString(var5.containsKey("sprite") ? "sprite" : "resource"));
               }
            }

            return var1;
         }
      }

      return var1;
   }

   public void updateModelTextures(String var1, JSONObject var2) {
      if (Settings.get().fixAtlasItemModels && this.itemContextIndex != null) {
         String var14 = ItemContextBuilder.normalizeId(var1);
         boolean var15 = this.itemContextIndex.resolvedNames().containsValue(var14);
         boolean var16 = !var15 && this.itemContextIndex.sourcePaths().contains(var14);
         if (!var16 && !var15 && !this.itemContextIndex.itemPaths().contains(var14)) {
            throw new IllegalStateException("Modern atlas context was not planned for model '" + var14 + "'");
         }

         this.rewriteTextures(var14, var2, var16);
      } else if (var2.get("textures") instanceof JSONObject var4) {
         boolean var5 = PackManifest.isBlockModelId(var1) || this.bzR.contains(var1) || this.blockTextureModels.contains(var1);
         if (!var5 && var2.containsKey("elements")) {
            for (Object var7 : var2.getJSONArray("elements")) {
               if (var7 instanceof JSONObject var8 && var8.containsKey("faces")) {
                  JSONObject var9 = var8.getJSONObject("faces");

                  for (String var11 : var9.keySet()) {
                     JSONObject var12 = var9.getJSONObject(var11);
                     if (var12.containsKey("texture")) {
                        String var13 = var12.getString("texture");
                        if (var13.equals("#missing")) {
                           var12.put("texture", "#particle");
                        }
                     }
                  }
               }
            }
         }

         for (Entry var18 : var4.entrySet()) {
            String var19 = textureSpriteOf(var18.getValue());
            if (var19 != null && !var19.isBlank() && !PackFileEntry.isSpecialRef(var19)) {
               if (!var19.contains(":")) {
                  var19 = "minecraft:" + var19;
               }

               if (this.skippedAssets.contains(var19)) {
                  var19 = "minecraft:item/iamissing";
               }

               String var20 = this.resolveTextureSprite(var19, var5);
               if (var20 != null) {
                  setTextureSprite(var18, var20);
               }
            }
         }
      }
   }

   @Nullable
   private String resolveTextureSprite(String var1, boolean var2) {
      String var3 = this.resolveTextureAlias(var1);
      if (this.ensureAtlasSprite(var3, var2)) {
         return var3;
      }

      String var4 = nameRegistry.getOrGenerate(var3, var2);
      if (var4 == null) {
         return null;
      }

      if (var2) {
         this.addBlockAtlasAlias(var3, var4);
      } else {
         this.addItemAtlasAlias(var3, var4);
      }

      JSONObject var5 = new JSONObject();
      var5.put("type", "single");
      var5.put("resource", var3);
      var5.put("sprite", var4);
      if (!isHumanoidEquipmentRef(var3)) {
         this.blocksAtlas.getJSONArray("sources").add(var5);
      }

      return var4;
   }

   private static boolean isHumanoidEquipmentRef(String var0) {
      return var0.startsWith("_iainternal:humanoid_equipment/") || var0.startsWith("_iainternal:textures/humanoid_equipment/");
   }

   private static JSONObject withoutHumanoidSources(JSONObject var0) {
      JSONObject var1 = new JSONObject(var0);
      JSONArray var2 = new JSONArray();

      for (Object var4 : var0.getJSONArray("sources")) {
         if (!(var4 instanceof JSONObject var5 && var5.get("resource") instanceof String var6 && isHumanoidEquipmentRef(var6))) {
            var2.add(var4);
         }
      }

      var1.put("sources", var2);
      return var1;
   }

   void registerItemDefinition(String var1, JSONObject var2) {
      var1 = Utils.normalizeSlashes(var1);
      TreeSet var3 = new TreeSet(ListCollector.collectModelNames(var2));
      this.atlasModelRefs.put(var1, var3);
      this.itemDefinitionCache.put(var1, (JSONObject)JSON.copy(var2, new Feature[0]));
   }

   void registerBlockstate(String var1, JSONObject var2) {
      TreeSet var3 = new TreeSet(ListCollector.collectModelNames(var2));
      this.blockstateModelRefs.put(Utils.normalizeSlashes(var1), var3);
   }

   JSONObject rewriteModelRefs(JSONObject var1) {
      JSONObject var2 = (JSONObject)JSON.copy(var1, new Feature[0]);
      if (this.itemContextIndex != null) {
         this.itemContextIndex.rewriteModelReferences(var2);
      }

      return var2;
   }

   private void addItemAtlasAlias(String var1, String var2) {
      String var3 = var2 == null ? "r:" + var1 : "rs:" + var1 + "->" + var2;
      if (this.pendingAtlasKeys.add(var3)) {
         JSONObject var4 = (JSONObject)this.atlasSources.get("items.json");
         JSONArray var5 = var4.getJSONArray("sources");
         JSONObject var6 = new JSONObject();
         var6.put("type", "single");
         var6.put("resource", var1);
         if (var2 != null) {
            var6.put("sprite", var2);
         }

         var5.add(var6);
      }
   }

   private void addBlockAtlasAlias(String var1, String var2) {
      String var3 = var2 == null ? "r:" + var1 : "rs:" + var1 + "->" + var2;
      if (this.pendingAtlasKeys.add(var3)) {
         JSONObject var4 = (JSONObject)this.atlasSources.get("blocks.json");
         JSONArray var5 = var4.getJSONArray("sources");
         JSONObject var6 = new JSONObject();
         var6.put("type", "single");
         var6.put("resource", var1);
         if (var2 != null) {
            var6.put("sprite", var2);
         }

         var5.add(var6);
      }
   }

   Set collectBlockstateModelIds() {
      HashSet var1 = new HashSet();
      this.blockstateOverlays.values().forEach(var1x -> var1.addAll(ListCollector.collectModelNames(var1x)));
      return var1;
   }

   JSONObject dedupeAtlasSources(JSONObject var1) {
      JSONObject var2 = new JSONObject();

      for (Entry var4 : var1.entrySet()) {
         String var5 = (String)var4.getKey();
         Object var6 = var4.getValue();
         if (!"sources".equals(var5)) {
            var2.put(var5, var6);
         }
      }

      if (var1.containsKey("sources") && var1.get("sources") instanceof JSONArray var12) {
         HashSet var14 = new HashSet();
         JSONArray var15 = new JSONArray();

         for (Object var7 : var12) {
            if (var7 instanceof JSONObject var8) {
               String var9 = null;
               if (var8.containsKey("resource") && var8.containsKey("sprite")) {
                  String var17 = var8.getString("resource");
                  String var11 = var8.getString("sprite");
                  var9 = "rs:" + var17 + "->" + var11;
               } else if (var8.containsKey("resource")) {
                  String var10 = var8.getString("resource");
                  var9 = "r:" + var10;
               }

               if (var9 == null || var14.add(var9)) {
                  var15.add(var8);
               }
            } else {
               var15.add(var7);
            }
         }

         var2.put("sources", var15);
      }

      return var2;
   }

   void removeDuplicateAtlasSources() {
      JSONObject var1 = (JSONObject)this.atlasSources.get("blocks.json");
      JSONObject var2 = (JSONObject)this.atlasSources.get("items.json");
      HashSet var3 = new HashSet();
      if (var1.containsKey("sources")) {
         Object var5 = var1.get("sources");
         if (var5 instanceof JSONArray) {
            for (Object var6 : (JSONArray)var5) {
               if (var6 instanceof JSONObject var7) {
                  if (var7.containsKey("resource") && var7.containsKey("sprite")) {
                     String var17 = var7.getString("resource");
                     String var9 = var7.getString("sprite");
                     var3.add("rs:" + var17 + "->" + var9);
                  } else if (var7.containsKey("resource")) {
                     String var8 = var7.getString("resource");
                     var3.add("r:" + var8);
                  }
               }
            }
         }
      }

      if (var2.containsKey("sources") && var2.get("sources") instanceof JSONArray var11) {
         Iterator var14 = var11.iterator();

         while (var14.hasNext()) {
            if (var14.next() instanceof JSONObject var16) {
               if (var16.containsKey("resource") && var16.containsKey("sprite")) {
                  String var19 = var16.getString("resource");
                  String var21 = var16.getString("sprite");
                  String var10 = "rs:" + var19 + "->" + var21;
                  if (var3.contains(var10)) {
                     var14.remove();
                  }
               } else if (var16.containsKey("resource")) {
                  String var18 = var16.getString("resource");
                  String var20 = "r:" + var18;
                  if (var3.contains(var20)) {
                     var14.remove();
                  }
               }
            }
         }
      }
   }

   public void registerMaterialNames(HashMap var1) {
      for (Entry var3 : var1.entrySet()) {
         MaterialTextureNames var4 = new MaterialTextureNames((Material)var3.getKey(), (List)var3.getValue());
         this.materialModelJsons.put(var4.ZP(), var4.pd());
         this.bzM.put(var4.ZP(), var4.ZO());
      }
   }

   public void registerScrollGuiItems(HashMap var1) {
      this.scrollGuiRegistry = ScrollableGuiAssets.XP();
      var1.computeIfAbsent(Material.PAPER, var0 -> new ArrayList()).addAll(this.scrollGuiRegistry.XS());
   }

   public void generateArmorTextures() {
      if (Settings.get().armorShadersEnabled) {
         this.armorTextureBuilder.generateArmorAtlas();
      }

      if (Settings.get().armorGenOptifineCit) {
         this.armorTextureBuilder.generateOptifineArmors();
      }
   }

   void clearGenerationState() {
      this.clearPlannerCaches();
      if (this.writer != null) {
         this.writer.clear();
         this.writer = null;
      }

      modelParents.clear();
      if (this.materialModelJsons != null) {
         this.materialModelJsons.clear();
      }

      this.bzM.clear();
      if (this.manifestJsons != null) {
         this.manifestJsons.clear();
      }

      if (this.furnitureItemJsons != null) {
         this.furnitureItemJsons.clear();
      }

      this.bzP.clear();
      if (this.blockstateOverlays != null) {
         this.blockstateOverlays.clear();
      }

      if (this.bAo != null) {
         this.bAo.clear();
      }

      if (this.bAw != null) {
         this.bAw.clear();
      }

      this.packFileEntries.clear();
      this.packArraysJson = null;
      this.bAr = null;
      this.bAs = Map.of();
      this.bAx = new JSONObject();
      if (this.armorTextureBuilder != null) {
         this.armorTextureBuilder.reset();
      }

      this.forcedModelIds = Set.of();
      if (this.blockTextureModels != null) {
         this.blockTextureModels.clear();
      }
   }

   public static boolean hasServerResourcePackProperty() {
      String var0;
      try {
         BufferedReader var1 = new BufferedReader(new FileReader("server.properties"));
         Properties var2 = new Properties();
         var2.load(var1);
         var1.close();
         var0 = var2.getProperty("resource-pack");
      } catch (Exception var3) {
         Log.error("Failed to read server.properties (you can probably ignore this): " + var3.getMessage());
         var3.printStackTrace();
         return false;
      }

      return var0 != null && !var0.isEmpty() && !var0.equals(" ");
   }

   public void writePackMetadata() {
      if (!this.bAx.containsKey("pack")) {
         this.bAx.put("pack", new JSONObject());
      }

      JSONObject var1 = this.bAx.getJSONObject("pack");
      int var2 = 0;
      Object var3 = null;

      for (File var5 : ContentManager.method8084().field88) {
         File var6 = new File(var5, "pack.mcmeta");
         if (var6.exists()) {
            try {
               JSONObject var7 = JsonHelper.readJsonFile(var6);
               if (var7.containsKey("pack")) {
                  JSONObject var8 = var7.getJSONObject("pack");
                  if (var3 == null && var8.containsKey("description")) {
                     Object var9 = var8.get("description");
                     if (var9 != null) {
                        var3 = var9;
                     }
                  }

                  if (var8.containsKey("pack_format")) {
                     int var23 = var8.getIntValue("pack_format");
                     if (var23 > var2) {
                        var2 = var23;
                     }
                  }

                  if (var8.containsKey("supported_formats")) {
                     Object var24 = var8.get("supported_formats");
                     switch (var24) {
                        case Integer var12:
                           if (!var1.containsKey("supported_formats")) {
                              var1.put("supported_formats", var12);
                           } else if (var1.get("supported_formats") instanceof Integer) {
                              int var30 = var1.getIntValue("supported_formats");
                              if (var12 > var30) {
                                 var1.put("supported_formats", var12);
                              }
                           }
                           break;
                        case JSONArray var13:
                           if (var13.size() == 2) {
                              if (!var1.containsKey("supported_formats")) {
                                 var1.put("supported_formats", var13);
                              } else if (var1.get("supported_formats") instanceof JSONArray) {
                                 JSONArray var32 = var1.getJSONArray("supported_formats");
                                 if (var13.getInteger(1) > var32.getInteger(1)) {
                                    var32.set(1, var13.getInteger(1));
                                 }
                              }
                           } else {
                              Log.error("Invalid 'supported_formats' array size in pack.mcmeta file. File: " + Utils.shortPathOf(var6.getAbsolutePath()));
                           }
                           break;
                        case JSONObject var14:
                           if (!var14.containsKey("min_inclusive") || !var14.containsKey("max_inclusive")) {
                              Log.error("Invalid 'supported_formats' object in pack.mcmeta file. File: " + Utils.shortPathOf(var6.getAbsolutePath()));
                           } else if (!var1.containsKey("supported_formats")) {
                              var1.put("supported_formats", var14);
                           } else if (var1.get("supported_formats") instanceof JSONObject) {
                              JSONObject var15 = var1.getJSONObject("supported_formats");
                              if (var14.getIntValue("max_inclusive") > var15.getIntValue("max_inclusive")) {
                                 var15.put("max_inclusive", var14.getIntValue("max_inclusive"));
                              }
                           }
                           break;
                        case null:
                        default:
                           Log.error("Invalid 'supported_formats' type in pack.mcmeta file. File: " + Utils.shortPathOf(var6.getAbsolutePath()));
                     }
                  }
               }

               if (var7.containsKey("overlays")) {
                  JSONObject var21 = var7.getJSONObject("overlays");
                  if (var21.containsKey("entries")) {
                     if (!this.bAx.containsKey("overlays")) {
                        this.bAx.put("overlays", new JSONObject());
                     }

                     JSONObject var25 = this.bAx.getJSONObject("overlays");
                     JSONArray var27 = var21.getJSONArray("entries");
                     if (!var25.containsKey("entries")) {
                        var25.put("entries", new JSONArray());
                     }

                     JSONArray var28 = var25.getJSONArray("entries");

                     label115:
                     for (Object var31 : var27) {
                        JSONObject var33 = (JSONObject)var31;
                        ResourcePackBuilder.normalizeOverlayEntry(var33);
                        normalizeFormatRange(var33);

                        for (Object var16 : var28) {
                           JSONObject var17 = (JSONObject)var16;
                           if (var33.getString("directory").equals(var17.getString("directory"))) {
                              Log.warn(
                                 "Duplicate overlay entry '"
                                    + var33.getString("directory")
                                    + "' in pack.mcmeta file. File: "
                                    + Utils.shortPathOf(var6.getAbsolutePath())
                              );
                              break label115;
                           }
                        }

                        var28.add(var31);
                     }
                  }
               }

               for (String var26 : var7.keySet()) {
                  if (!var26.equals("pack") && !var26.equals("overlays")) {
                     this.bAx.put(var26, var7.get(var26));
                  }
               }
            } catch (JSONException var18) {
               Log.error("Malformed JSON in pack.mcmeta file. File: " + Utils.shortPathOf(var6.getAbsolutePath()));
               Log.error(var18.getMessage());
            } catch (Throwable var19) {
               Log.error("Error opening pack.mcmeta file. File: " + Utils.shortPathOf(var6.getAbsolutePath()), var19);
            }
         }
      }

      int var20 = Math.max(var2, ServerCompat.getPackVersion());
      var1.put("pack_format", var20);
      setSupportedFormats(var1, ServerCompat.packVersionOf(Version.v1_20_6), 9999);
      var1.put("description", Objects.requireNonNullElse(var3, "ItemsAdder generated resourcepack"));
   }

   public void warnDuplicateImages(boolean var1) {
      HashMap var2 = new HashMap();

      for (File var4 : ContentManager.method8084().field87) {
         compressFolderPngs(var4);
         if (var1) {
            ArrayList var5 = new ArrayList(FileUtils.listFiles(var4, new String[]{"png"}, true));
            int var6 = 0;

            for (int var7 = var5.size(); var6 < var7; var6++) {
               File var8 = (File)var5.get(var6);
               if (!var8.getPath().contains("tooltip")
                  && !var8.getPath().endsWith("_e.png")
                  && !var8.getPath().contains("optifine/cit/")
                  && !var8.getPath().contains("gui")) {
                  try {
                     byte[] var9 = FileUtils.readFileToByteArray(var8);
                     String var10 = DigestUtils.md5Hex(var9);
                     var2.computeIfAbsent(var10, var0 -> new ArrayList<>()).add(Utils.shortPathOf(var8.getAbsolutePath()));
                  } catch (IOException var11) {
                     Log.warn("Failed to calculate hash for image: " + Utils.shortPathOf(var8.getAbsolutePath()));
                  }
               }
            }
         }
      }

      if (var1) {
         List var12 = var2.entrySet().stream().filter(var0 -> ((List)var0.getValue()).size() > 1).toList();
         if (!var12.isEmpty()) {
            Log.warn(" ");
            Log.warn(" ");
            Log.warn("Duplicate images found in resourcepacks: " + var12.size());
            Log.warn("Consider reusing the same textures instead of creating multiple copies.");
            Log.warn(" ");
            Log.warn(" ");
         }

         var12.forEach(var0 -> {
            Log.warn((String)((List)var0.getValue()).getFirst());
            ((List)var0.getValue()).forEach(var0x -> Log.warn("  - " + var0x));
         });
      }
   }

   public static void compressFolderPngs(File var0) {
      compressPngsInFolder(var0);
   }

   public static void compressPngsInFolder(File var0) {
      PersistentCounter var1 = new PersistentCounter();
      int var2 = 0;
      long var3 = 0L;
      long var5 = 0L;
      if (var0.exists()) {
         boolean var7 = false;
         ImageWriter var9 = null;
         ImageWriteParam var10 = null;
         ArrayList var11 = new ArrayList(FileUtils.listFiles(var0, new String[]{"png"}, true));
         int var12 = 0;

         for (int var13 = var11.size(); var12 < var13; var12++) {
            File var14 = (File)var11.get(var12);

            try {
               if (!isPngHandled(var14)) {
                  try (InputStream var15 = Files.newInputStream(var14.toPath())) {
                     if (BinaryPackCodec.validatePngHeader(var15.readNBytes(24)) != null) {
                        continue;
                     }
                  }

                  long var35 = var14.length();
                  var3 += var35;

                  BufferedImage var17;
                  try (ImageInputStream var18 = ImageIO.createImageInputStream(Files.newInputStream(var14.toPath()))) {
                     ImageReader var19 = ImageIO.getImageReadersByFormatName("png").next();
                     var19.setInput(var18, true, false);
                     var17 = var19.read(0);
                     var19.dispose();
                  }

                  PackedByteStream var36 = new PackedByteStream();

                  try (MemoryCacheImageOutputStream var37 = new MemoryCacheImageOutputStream(var36)) {
                     if (!var7) {
                        ImageTypeSpecifier var8 = ImageTypeSpecifier.createFromRenderedImage(var17);
                        var9 = ImageIO.getImageWriters(var8, "png").next();
                        var10 = var9.getDefaultWriteParam();
                        var7 = true;
                     }

                     var9.setOutput(var37);
                     var9.write(null, new IIOImage(var17, null, null), var10);
                     long var20 = var36.size();
                     if (var20 < var35) {
                        byte[] var22 = var36.getBuffer();

                        try (FileOutputStream var23 = new FileOutputStream(var14)) {
                           var23.write(var22, 0, var36.size());
                           var2++;
                        }
                     }

                     var5 += var20;
                     markPngHandled(var14);
                  }
               }
            } catch (Throwable var34) {
               Log.error("Error stripping metadata of file '" + var14.getAbsolutePath().replace(var0.getAbsolutePath(), "") + "': " + var34.getMessage());
            }
         }

         if (var9 != null) {
            var9.dispose();
         }
      } else {
         Log.error("Error stripping metadata. Respack folder not found!");
      }

      if (var2 > 0) {
         Log.log(
            ChatColor.GREEN
               + "Compressed "
               + var2
               + " PNGs in "
               + var1.lapFromStart()
               + "ms. "
               + Utils.humanReadableBytes(var3)
               + "->"
               + Utils.humanReadableBytes(var5)
         );
      }
   }

   private static void markPngHandled(File var0) {
      FileHelper.storeModifiedTime(var0, "ia_compression_handled");
   }

   private static boolean isPngHandled(File var0) {
      return FileHelper.isModifiedTimeUnchanged(var0, "ia_compression_handled");
   }

   public static void removeGroups(JSONObject var0) {
      var0.remove("groups");
   }

   public static void fixMissingParticle(JSONObject var0) {
      if (!var0.containsKey("textures")) {
         if (var0.containsKey("parent") || !(var0.get("elements") instanceof JSONArray var1) || !var1.isEmpty()) {
            return;
         }

         var0.put("textures", new JSONObject());
      }

      if (var0.get("textures") instanceof JSONObject var7) {
         Optional var3 = var7.containsKey("layer0") ? Optional.of("layer0") : var7.keySet().stream().findFirst();
         if (!var3.isEmpty() || !var0.containsKey("parent")) {
            String var4 = var3.<String>map(var0x -> "#" + var0x).orElse("minecraft:item/iamissing");
            String var5 = var7.getString("particle");
            if (var5 == null || var5.isEmpty()) {
               var7.put("particle", var4);
            }
         }
      }
   }

   private static void warnDuplicateOverrides(String var0, File var1, int[] var2, int var3, int var4) {
      String var5 = formatCustomModelDataRanges(var2, var3);
      if (var4 < 0) {
         Log.warn("Duplicate CustomModelData values for item '" + var0 + "': " + var5 + ". More values follow. File: " + Utils.shortPath(var1));
      } else {
         if (var4 == var3) {
            Log.warn(
               "Found "
                  + var4
                  + " duplicate CustomModelData override"
                  + (var4 == 1 ? "" : "s")
                  + " for item '"
                  + var0
                  + "': "
                  + var5
                  + ". File: "
                  + Utils.shortPath(var1)
            );
         } else {
            Log.warn(
               "Duplicate CustomModelData values for item '"
                  + var0
                  + "': "
                  + var5
                  + ". Total duplicate overrides: "
                  + var4
                  + ". File: "
                  + Utils.shortPath(var1)
            );
         }
      }
   }

   static String formatCustomModelDataRanges(int[] var0, int var1) {
      if (var1 == 0) {
         return "";
      }

      Arrays.sort(var0, 0, var1);
      StringBuilder var2 = new StringBuilder(Math.min(var1 * 6, 4096));
      int var3 = var0[0];
      int var4 = var3;

      for (int var5 = 1; var5 < var1; var5++) {
         int var6 = var0[var5];
         if (var6 != var4) {
            if (var6 == var4 + 1L) {
               var4 = var6;
            } else {
               appendRange(var2, var3, var4);
               var4 = var6;
               var3 = var6;
            }
         }
      }

      appendRange(var2, var3, var4);
      return var2.toString();
   }

   private static void appendRange(StringBuilder var0, int var1, int var2) {
      if (!var0.isEmpty()) {
         var0.append(", ");
      }

      var0.append(var1);
      if (var1 != var2) {
         var0.append('-').append(var2);
      }
   }

   public void mergeItemOverrides() {
      BossBarManager.setTitleAndProgress("Processing packs: merging item overrides...", 0.0);
      this.bAw = new ArrayList();
      HashMap var1 = new HashMap();
      List var2 = ContentManager.method8084().field87;

      for (File var4 : var2) {
         File[] var5 = var4.listFiles(File::isDirectory);
         if (var5 != null) {
            File var6 = new File(var4, "minecraft/models");
            if (var6.exists()) {
               File var7 = new File(var6, "item");
               if (var7.exists()) {
                  File[] var8 = var7.listFiles(var0 -> var0.isFile() && var0.getName().endsWith(".json") && !var0.getName().startsWith("."));
                  if (var8 != null) {
                     for (File var12 : var8) {
                        try {
                           JSONObject var13 = JsonHelper.readJsonFile(var12);
                           String var14 = var12.getName();
                           String var15 = FilenameUtils.removeExtension(var14);
                           if (!var13.containsKey("overrides")) {
                              var1.put(var15, var13);
                           } else if (!var1.containsKey(var15)) {
                              var1.put(var15, var13);
                           } else {
                              JSONObject var16 = (JSONObject)var1.get(var15);
                              JSONArray var17 = var13.getJSONArray("overrides");
                              if (!var16.containsKey("overrides")) {
                                 var16.put("overrides", var17);
                              } else {
                                 JSONArray var18 = var16.getJSONArray("overrides");
                                 int[] var19 = new int[Math.min(var17.size(), 512)];
                                 int var20 = 0;
                                 int var21 = 0;
                                 JSONArray var22 = (JSONArray)JsonHelper.toDocumentContext(var16)
                                    .read(
                                       "$.overrides[?(@.predicate)].predicate[?(@.custom_model_data)].custom_model_data", new com.jayway.jsonpath.Predicate[0]
                                    );

                                 for (Object var24 : var17) {
                                    Object var25 = ((JSONArray)JsonHelper.toDocumentContext((JSONObject)var24)
                                          .read("$.predicate[?(@.custom_model_data)]", new com.jayway.jsonpath.Predicate[0]))
                                       .getJSONObject(0)
                                       .get("custom_model_data");
                                    if (var25 != null) {
                                       if (var22.contains(var25)) {
                                          if (var20 == var19.length) {
                                             warnDuplicateOverrides(var15, var12, var19, var20, -1);
                                             var20 = 0;
                                          }

                                          var19[var20++] = (Integer)var25;
                                          var21++;
                                       } else {
                                          Material var26 = ItemOperationsService.parseMaterial(var15);
                                          if (var26 != null) {
                                             CustomItemDefinition var27 = ContentManager.method8084().method8164(var26, (Integer)var25);
                                             if (var27 == null) {
                                                var18.add(var24);
                                             } else {
                                                Log.warn(
                                                   "Skipped CustomModelData '"
                                                      + var25
                                                      + "' for item override '"
                                                      + var14
                                                      + "'. Already used by ItemsAdder custom item '"
                                                      + var27.aok
                                                      + "'. Item file: "
                                                      + var27.DJ()
                                                );
                                             }
                                          } else {
                                             Log.warn("Cannot find Bukkit material for model file: " + Utils.shortPath(var12));
                                          }
                                       }
                                    }
                                 }

                                 if (var21 > 0) {
                                    warnDuplicateOverrides(var15, var12, var19, var20, var21);
                                 }
                              }
                           }
                        } catch (IOException var33) {
                           Log.error("Failed to process item file: " + Utils.shortPath(var12), var33);
                        } catch (Throwable var34) {
                           Log.error("Malformed JSON " + Utils.shortPath(var12));
                           Log.error(var34.getMessage());
                        }
                     }
                  }
               }
            }
         }
      }

      BossBarManager.setTitleKeepProgress("Processing packs: merging " + this.materialModelJsons.size() + " generated predicates...");

      for (Entry var38 : this.materialModelJsons.entrySet()) {
         try {
            String var41 = (String)var38.getKey();
            JSONObject var44 = (JSONObject)var38.getValue();
            JSONObject var46 = (JSONObject)var1.get(var41);
            if (var46 != null) {
               if (!var46.containsKey("overrides")) {
                  var46.put("overrides", new JSONArray());
               }

               var46.getJSONArray("overrides").addAll(var44.getJSONArray("overrides"));
            } else {
               var46 = var44;
               var1.put(var41, var46);
            }

            MaterialTextureNames.ah(var46);
            JSONArray var48 = var46.getJSONArray("overrides");

            try {
               if (!var48.getJSONObject(var48.size() - 1).getString("model").equals("item/" + (String)var38.getKey())) {
                  JSONObject var50 = new JSONObject();
                  int var52 = var48.getJSONObject(var48.size() - 1).getJSONObject("predicate").getInteger("custom_model_data");
                  var50.put("predicate", new JSONObject());
                  var50.getJSONObject("predicate").put("custom_model_data", var52 + 1);
                  var50.put("model", "item/" + (String)var38.getKey());
                  var48.add(var50);
               }
            } catch (Exception var28) {
            }
         } catch (Throwable var29) {
            Log.error("Failed to process item file: " + (String)var38.getKey(), var29);
         }
      }

      this.materialModelJsons.clear();
      this.materialModelJsons.putAll(var1);
      var1.clear();
      BossBarManager.setTitleKeepProgress("Processing packs: merging font providers...");
      if (!this.packArraysJson.containsKey("providers")) {
         this.packArraysJson.put("providers", new JSONArray());
      }

      Iterator var36 = var2.iterator();

      while (true) {
         File var42;
         JSONObject var45;
         while (true) {
            if (!var36.hasNext()) {
               BossBarManager.setTitleKeepProgress("Processing packs: collecting font textures...");

               for (Entry var40 : ContentManager.method8084().aE.Yp.entrySet()) {
                  var42 = ((CustomTexture)var40.getValue()).getFontImageFile();
                  if (var42 != null) {
                     this.bAw.add(var42);
                  }
               }

               BossBarManager.setTitleKeepProgress("Processing packs: generating atlases...");
               this.mergeAtlases();
               BossBarManager.setTitleKeepProgress("Processing packs: generating sounds metadata...");
               this.bAs = PackJsonMerger.loadAllSoundFiles();
               this.bAr = this.bAs.computeIfAbsent("minecraft", var0 -> new JSONObject());
               return;
            }

            File var39 = (File)var36.next();
            var42 = new File(var39, "minecraft/font/default.json");
            if (var42.exists()) {
               try {
                  var45 = JsonHelper.readJsonFile(var42);
                  break;
               } catch (JSONException var30) {
                  Log.error("Malformed JSON file. File: " + Utils.shortPathOf(var42.getAbsolutePath()));
                  Log.error(var30.getMessage());
               } catch (Throwable var31) {
                  Log.error("Error opening a custom font file. Make sure it's valid or delete it. File: " + Utils.shortPathOf(var42.getAbsolutePath()), var31);
               }
            }
         }

         if (var45 != null && var45.containsKey("providers")) {
            Map var47 = this.buildCharOwnerMap(this.packArraysJson);
            ArrayList var49 = new ArrayList();
            JSONArray var51 = this.packArraysJson.getJSONArray("providers");

            for (Object var54 : var45.getJSONArray("providers")) {
               try {
                  JSONObject var55 = (JSONObject)var54;
                  if (!var51.contains(var55)) {
                     if (!var55.containsKey("type") || !var55.getString("type").equals("bitmap")) {
                        var51.add(var54);
                     } else if (!var55.containsKey("file")) {
                        Log.warn("Missing 'file' property for provider. File: " + Utils.shortPath(var42));
                        Log.warn(JsonHelper.toFormattedJsonString(var55, true, true).replace("\\\"", "\""));
                     } else if (!var55.containsKey("chars")) {
                        Log.warn("Missing 'chars' property for provider '" + var55.getString("file") + "'. File: " + Utils.shortPath(var42));
                     } else {
                        boolean var57 = true;
                        JSONArray var58 = var55.getJSONArray("chars");

                        for (Object var61 : var58) {
                           String var65 = (String)var47.get(var61);
                           if (var65 != null) {
                              String var68 = CustomTexture.escapeForJs(String.valueOf(var61))
                                 + " in '"
                                 + var55.getString("file")
                                 + "' is already supplied by '"
                                 + var65
                                 + "'";
                              if (JsonHelper.isPrimitiveValue(var61)) {
                                 CustomTexture var69 = ContentManager.method8084().aE.bC((String)var61);
                                 if (var69 != null) {
                                    var68 = var68 + " (ItemsAdder font_image '" + var69.getNamespacedID() + "')";
                                 }
                              }

                              var49.add(var68);
                              PackWarningSummary.addWarning(
                                 "Duplicate font characters",
                                 var68 + "\nFile: " + Utils.shortPath(var42) + "\nThe existing provider wins; this bitmap provider was skipped."
                              );
                              var57 = false;
                           }
                        }

                        if (var57) {
                           int var60 = var51.size();
                           if (var55.containsKey("ascent") && var55.containsKey("height")) {
                              int var62 = var55.getInteger("ascent");
                              int var66 = var55.getInteger("height");
                              if (var62 > var66) {
                                 this.warnFontProvider("Ascent " + var62 + " higher than height " + var66 + " ", var58, var55, var42);
                              }
                           }

                           String var63 = var55.getString("file");
                           if (var63.startsWith("minecraft:") && this.vanillaTextureExists(var63)) {
                              var51.add(var54);
                           } else {
                              if (var63.contains(":")) {
                                 var63 = "assets/" + var63.replace(":", "/textures/");
                              } else {
                                 var63 = "assets/minecraft/textures/" + var63;
                              }

                              File var67 = ContentManager.method8198(toAssetFilePath(var63, "png"));
                              if (var67 != null && var67.exists()) {
                                 var51.add(var54);
                                 this.bAw.add(var67);
                              } else if (var63.startsWith("minecraft:")) {
                                 Log.warn("Failed to add font glyph entry. Vanilla resource '" + var63 + "' does not exist. File: " + Utils.shortPath(var42));
                              } else {
                                 this.warnFontProvider("png not found for ", var58, var55, var42);
                              }
                           }

                           if (var51.size() > var60) {
                              var58.forEach(var2x -> var47.putIfAbsent(var2x, var55.getString("file")));
                           }
                        }
                     }
                  }
               } catch (Throwable var32) {
                  if (var32 instanceof VanillaAssetException var56) {
                     throw var56;
                  }

                  Log.error("Error reading JSON file: " + Utils.shortPath(var42), var32);
               }
            }

            if (!var49.isEmpty()) {
               Log.warn(
                  "Skipped bitmap font providers with "
                     + var49.size()
                     + " duplicate character"
                     + (var49.size() == 1 ? "" : "s")
                     + ". Existing providers win. Conflicts:\n- "
                     + String.join("\n- ", var49)
                     + "\nFile: "
                     + Utils.shortPath(var42)
               );
            }
         }
      }
   }

   public boolean vanillaTextureExists(String var1) {
      if (!ContentManager.method8084().field91.isReady()) {
         Log.warn("Cannot check if the vanilla texture exists by location '" + var1 + "' because the vanilla assets are not downloaded yet. Retry later.");
         return false;
      }

      if (var1.startsWith("minecraft:")) {
         var1 = "textures/" + var1.substring("minecraft:".length());
      } else {
         var1 = FilenameUtils.removeExtension(var1);
         var1 = var1.replace("assets/minecraft/", "");
      }

      if (!var1.endsWith(".png")) {
         var1 = var1 + ".png";
      }

      return ContentManager.method8084().field91.hasAsset(var1);
   }

   private void warnFontProvider(String var1, Object var2, JSONObject var3, File var4) {
      String var5 = "'. File: " + Utils.shortPath(var4);
      String var6 = var3.getString("file");
      if (var2 instanceof JSONArray var7) {
         if (var7.size() == 1) {
            String var8 = var7.getString(0);
            Log.warn(var1 + var8 + " (" + CustomTexture.escapeForJs(var8) + ") '" + var6 + var5);
         } else {
            Log.warn(var1 + var2 + " (" + CustomTexture.escapeForJs(var2.toString()) + ") '" + var6 + var5);
         }
      } else {
         Log.warn(var1 + var2 + " (" + CustomTexture.escapeForJs(var2.toString()) + ") '" + var6 + var5);
      }
   }

   Map buildCharOwnerMap(JSONObject var1) {
      HashMap var2 = new HashMap();
      JSONArray var3 = var1.getJSONArray("providers");
      if (var3 == null) {
         return var2;
      }

      for (Object var5 : var3) {
         if (var5 instanceof JSONObject var6) {
            JSONArray var7 = var6.getJSONArray("chars");
            if (var7 != null) {
               var7.forEach(var2x -> var2.putIfAbsent(var2x, Objects.requireNonNullElse(var6.getString("file"), "provider without a file property")));
            }
         }
      }

      return var2;
   }

   private static String stripDebugSections(String var0) {
      if (!Settings.get().hideScoreboardNumbersOldClients) {
         var0 = var0.replaceAll("//<SCOREBOARD_HIDE_RED_NUMBERS>.*?//</SCOREBOARD_HIDE_RED_NUMBERS>", "");
      }

      if (!Settings.get().textEffectsEnabled) {
         var0 = var0.replaceAll("//<TEXT_EFFECTS>.*?//</TEXT_EFFECTS>", "");
      }

      if (!Settings.get().textEffectsLegacyColors) {
         var0 = var0.replaceAll("//<LEGACY_TEXT_EFFECTS>.*?//</LEGACY_TEXT_EFFECTS>", "");
      }

      if (Settings.get().compressShaders) {
         var0 = new PackNamePrefixer(var0).minify();
      }

      return var0;
   }

   private String replaceTextureTokens(String var1) {
      IntVector3 var2 = this.armorTextureBuilder.getAtlasDimensions();
      if (var2 == null) {
         var2 = new IntVector3(64, 32, 0);
      }

      var1 = var1.replace("%TEX_RES%", String.valueOf(this.armorTextureBuilder.bHO));
      var1 = var1.replace("%TEX_WIDTH%", String.valueOf(var2.x));
      return var1.replace("%TEX_HEIGHT%", String.valueOf(var2.y));
   }

   String applyTextureTokens(String var1, boolean var2) {
      return var2 ? this.replaceTextureTokens(var1) : var1;
   }

   static String stripEmoteSections(String var0, boolean var1) {
      return var1
         ? var0
         : var0.replaceAll("(?s)//%START_EMOTES%.*?//%END_EMOTES%", "")
            .replaceAll("(?m)^(?:layout\\([^\\r\\n]*\\)\\h+)?(?:flat\\h+)?(?:in|out)\\h+(?:vec2|int)\\h+(?:origTexCoord0|isTopLayer|isHead);\\R", "")
            .replaceAll(
               "(?m)^#define (?:VANILLA_SKIN_TEX_SIZE|BONE_TEX_SIZE|BONE_TEX_SIZE_NORM|PER_BONE_VTXS|PER_BONE_VTXS_BOTH_LAYERS|IS_NOT_GUI|MIGHT_BE_TOP_LAYER|b1|handle_top_layer)\\b[^\\r\\n]*\\R",
               ""
            )
            .replaceAll("(?m)^\\h*(?:isTopLayer|isHead) = 0;\\R", "")
            .replaceAll("(?m)^\\h*origTexCoord0 = UV0;\\R", "")
            .replaceAll("(?m)^\\h*if \\(isHead == 1 && gl_FrontFacing == false\\) discard;\\R(?:\\h*\\R)?", "")
            .replaceAll("(?m)^\\h*if \\(isTopLayer == 1 \\|\\| origTexCoord0.y >= 0.25\\)\\R\\h*UV = origTexCoord0;\\R", "");
   }

   public static String toAssetFilePath(String var0, String var1) {
      String var2 = "";
      if (!var0.startsWith("assets/")) {
         var2 = "assets/";
      }

      var2 = var2 + var0.replace(":", "/");
      if (!var2.endsWith(var1)) {
         var2 = var2 + "." + var1;
      }

      return var2;
   }

   public static String buildAssetFilePath(String var0, PackAssetKind var1, String var2) {
      if (!var0.startsWith("assets/") && !var0.contains(":")) {
         var0 = "minecraft:" + var0;
      }

      String var3 = "";
      if (!var0.startsWith("assets/")) {
         var3 = "assets/";
      }

      var3 = var3 + var0.replace(":", "/" + var1.str + "/");
      if (!var3.endsWith(var2)) {
         var3 = var3 + "." + var2;
      }

      return var3;
   }

   public static boolean isVanillaOrPlainId(String var0) {
      return var0.startsWith("minecraft:") || !var0.contains(":");
   }

   public static String getContentsNamespace(File var0) {
      String var1 = Utils.relativePath(var0);
      var1 = Utils.removePrefix(var1, "contents/");
      var1 = Utils.removePrefix(var1, "/contents/");
      return StringUtils.substringBefore(var1, "/");
   }

   private static JSONArray minMaxArray(int var0, int var1) {
      return new JSONArray().fluentAdd(Math.min(var0, var1)).fluentAdd(Math.max(var0, var1));
   }

   private static JSONArray formatArrayFor(int var0, int var1) {
      int var2 = Math.min(var0, var1);
      int var3 = Math.max(var0, var1);
      if (var2 < 65 && var3 >= 65) {
         var3 = 64;
      }

      return minMaxArray(var2, var3);
   }

   private static void putMinMaxFormats(JSONObject var0, int var1, int var2) {
      int var3 = Math.min(var1, var2);
      int var4 = Math.max(var1, var2);
      var0.put("min_format", var3);
      var0.put("max_format", var4);
   }

   static void setSupportedFormats(JSONObject var0, int var1, int var2) {
      JSONArray var3 = formatArrayFor(var1, var2);
      var0.put("supported_formats", var3);
      if (var0.containsKey("pack_format")) {
         var0.put("pack_format", Math.clamp(var0.getIntValue("pack_format"), var3.getIntValue(0), var3.getIntValue(1)));
      }

      putMinMaxFormats(var0, var1, var2);
   }

   static void setFormatsRange(JSONObject var0, int var1, int var2) {
      var0.put("formats", formatArrayFor(var1, var2));
      putMinMaxFormats(var0, var1, var2);
   }

   void addOverlayFormatEntry(String var1, int var2, int var3) {
      JSONArray var4 = this.bAx.getJSONObject("overlays").getJSONArray("entries");

      for (Object var6 : var4) {
         if (var1.equals(((JSONObject)var6).getString("directory"))) {
            return;
         }
      }

      JSONObject var7 = new JSONObject();
      var7.put("directory", var1);
      setFormatsRange(var7, var2, var3);

      for (int var8 = 0; var8 < var4.size(); var8++) {
         if ("ia_overlay_modern_atlas".equals(var4.getJSONObject(var8).getString("directory"))) {
            var4.add(var8, var7);
            return;
         }
      }

      var4.add(var7);
   }

   static void setFormatsAndMinMax(JSONObject var0, int var1, int var2, int var3) {
      var0.put("formats", formatArrayFor(var1, var3));
      var0.put("min_format", new JSONArray().fluentAdd(var1).fluentAdd(var2));
      var0.put("max_format", var3);
   }

   static void normalizeFormatRange(JSONObject var0) {
      Object var1 = var0.get("formats");
      if (!var0.containsKey("min_format") || !var0.containsKey("max_format")) {
         Object var2 = var1;
         switch (var2) {
            case Integer var7:
               Integer var4 = (Integer)var2;
               putMinMaxFormats(var0, var4, var4);
               break;
            case JSONArray var5 when var5.size() == 2:
               putMinMaxFormats(var0, var5.getIntValue(0), var5.getIntValue(1));
               break;
            case JSONObject var6 when var6.containsKey("min_inclusive") && var6.containsKey("max_inclusive"):
               putMinMaxFormats(var0, var6.getIntValue("min_inclusive"), var6.getIntValue("max_inclusive"));
               break;
            case null:
            default:
         }
      }

      if (!var0.containsKey("formats") && var0.containsKey("min_format") && var0.containsKey("max_format")) {
         var0.put("formats", formatArrayFor(parseFormatMajor(var0.get("min_format")), parseFormatMajor(var0.get("max_format"))));
      }
   }

   public void generateEntityBlockModels(CustomItemDefinition var1) {
      if (var1.anZ != null) {
         this.registerItemModels(var1.anZ);
      }

      if (var1 instanceof CustomBlockDefinition var2 && var2.aps == BlockRealizationMode.ENTITY) {
         String var3 = var1.method8734(false, true);
         this.putEntityItemDefinition(entityBlockItemPath_1_21_4(var2.getNamespace(), var2.getId()), buildEntityBlockDispatch(var3, false));
         this.putEntityItemDefinition(entityBlockItemPath_26_1(var2.getNamespace(), var2.getId()), buildEntityBlockDispatch(var3, true));
         Map var4 = var2.getEntityModelTextures();
         if (var4 != null) {
            Map var5 = this.buildEntityBlockTextures(var2.getNamespace(), var2.getId(), var4);
            if (!var5.isEmpty()) {
               this.putEntityItemDefinition(
                  entityBlockGroupItemPath(var2.getNamespace(), var2.getId()), modelOnlyItem(entityBlockGroupModelId(var2.getNamespace(), var2.getId()))
               );
               this.manifestJsons.put(entityBlockGroupModelFile(var2.getNamespace(), var2.getId()), buildEntityBlockModel(var5));
            }
         }

         for (int var6 = 0; var6 < 10; var6++) {
            this.manifestJsons.putIfAbsent(breakingStageModelPath(var6), buildDestroyStageModel(var6));
            this.manifestJsons.putIfAbsent(breakingTranslucentModelPath(var6), translucentDestroyStageModel(var6));
         }
      }
   }

   private void putEntityItemDefinition(String var1, JSONObject var2) {
      String var3 = stripToAssetsRoot(var1);
      File var4 = ContentManager.method8198(var3);
      if (var4 != null) {
         throw new IllegalArgumentException("ENTITY block item definition path collides with user content: " + var3);
      }

      JSONObject var5 = this.furnitureItemJsons.putIfAbsent(var1, var2);
      if (var5 != null && !var5.equals(var2)) {
         throw new IllegalStateException("Conflicting ENTITY block item definition: " + var1);
      }
   }

   static String entityBlockKey(String var0, String var1) {
      return "_iainternal:entity_blocks/" + var0 + "/" + var1;
   }

   static String entityBlockItemPath_1_21_4(String var0, String var1) {
      return "ia_overlay_1_21_4_plus/assets/_iainternal/items/entity_blocks/" + var0 + "/" + var1 + ".json";
   }

   static String entityBlockItemPath_26_1(String var0, String var1) {
      return "ia_overlay_26_1_plus/assets/_iainternal/items/entity_blocks/" + var0 + "/" + var1 + ".json";
   }

   static String entityBlockGroupItemPath(String var0, String var1) {
      return "ia_overlay_1_21_4_plus/assets/_iainternal/items/entity_blocks/" + var0 + "/" + var1 + "/group_3x3x1.json";
   }

   static String entityBlockGroupModelId(String var0, String var1) {
      return "_iainternal:entity_blocks/" + var0 + "/" + var1 + "/group_3x3x1";
   }

   static String entityBlockGroupModelFile(String var0, String var1) {
      return "assets/_iainternal/models/entity_blocks/" + var0 + "/" + var1 + "/group_3x3x1.json";
   }

   static String entityBlockTextureId(String var0, String var1) {
      return "_iainternal:block/entity_blocks/" + var0 + "/" + var1 + "/group_3x3x1";
   }

   static String entityBlockTextureFile(String var0, String var1) {
      return "assets/_iainternal/textures/block/entity_blocks/" + var0 + "/" + var1 + "/group_3x3x1.png";
   }

   static String entityBlockTextureRef(String var0, String var1, String var2) {
      return "all".equals(var2) ? entityBlockTextureId(var0, var1) : entityBlockTextureId(var0, var1) + "/" + var2;
   }

   static String entityBlockTextureFileRef(String var0, String var1, String var2) {
      return "all".equals(var2) ? entityBlockTextureFile(var0, var1) : entityBlockTextureFile(var0, var1).replace(".png", "/" + var2 + ".png");
   }

   private Map buildEntityBlockTextures(String var1, String var2, Map var3) {
      HashMap var4 = new HashMap();

      for (Entry var6 : var3.entrySet()) {
         File var7 = ContentManager.method8199((String)var6.getValue());
         if (var7 == null) {
            return Map.of();
         }

         var4.put((String)var6.getKey(), var7);
      }

      HashMap var15 = new HashMap();

      try {
         for (Entry var17 : var4.entrySet()) {
            String var8 = (String)var17.getKey();
            File var9 = (File)var17.getValue();
            String var10 = entityBlockTextureFileRef(var1, var2, var8);
            BufferedImage var11 = ImageIO.read(var9);
            if (var11 == null) {
               throw new IOException("Invalid PNG: " + var9);
            }

            ByteArrayOutputStream var12 = new ByteArrayOutputStream();
            if (!ImageIO.write(ensureSquareFrame(var11), "PNG", var12)) {
               throw new IOException("PNG writer unavailable");
            }

            this.bzP.put(var10, var12.toByteArray());
            File var13 = new File(var9.getPath() + ".mcmeta");
            if (var13.isFile()) {
               this.bzP.put(var10 + ".mcmeta", Files.readAllBytes(var13.toPath()));
            }

            var15.put(var8, entityBlockTextureRef(var1, var2, var8));
         }

         return var15;
      } catch (IOException var14) {
         throw new IllegalStateException("Failed to generate ENTITY block group textures", var14);
      }
   }

   static BufferedImage ensureSquareFrame(BufferedImage var0) {
      int var1 = var0.getWidth();
      if (var1 >= 1 && var0.getHeight() % var1 == 0) {
         byte var2 = 3;
         int var3 = var0.getHeight() / var1;
         BufferedImage var4 = new BufferedImage(var1 * var2, var1 * var2 * var3, 2);
         Graphics2D var5 = var4.createGraphics();

         try {
            for (int var6 = 0; var6 < var3; var6++) {
               for (int var7 = 0; var7 < var2; var7++) {
                  for (int var8 = 0; var8 < var2; var8++) {
                     var5.drawImage(
                        var0,
                        var8 * var1,
                        (var6 * var2 + var7) * var1,
                        (var8 + 1) * var1,
                        (var6 * var2 + var7 + 1) * var1,
                        0,
                        var6 * var1,
                        var1,
                        (var6 + 1) * var1,
                        null
                     );
                  }
               }
            }
         } finally {
            var5.dispose();
         }

         return var4;
      } else {
         throw new IllegalArgumentException("ENTITY block texture frames must be square: " + var0.getWidth() + "x" + var0.getHeight());
      }
   }

   static JSONObject modelOnlyItem(String var0) {
      return new JSONObject().fluentPut("model", new JSONObject().fluentPut("type", "minecraft:model").fluentPut("model", var0));
   }

   static JSONObject buildEntityBlockDispatch(String var0, boolean var1) {
      JSONArray var2 = new JSONArray();

      for (int var3 = 0; var3 < 10; var3++) {
         var2.add(
            new JSONObject()
               .fluentPut("threshold", var3 + 1)
               .fluentPut("model", var1 ? buildTranslucentBreakingModel(var0, var3).get("model") : buildBreakingModel(var0, var3).get("model"))
         );
      }

      return new JSONObject()
         .fluentPut(
            "model",
            new JSONObject()
               .fluentPut("type", "minecraft:range_dispatch")
               .fluentPut("property", "minecraft:custom_model_data")
               .fluentPut("index", 0)
               .fluentPut("entries", var2)
               .fluentPut("fallback", modelOnlyItem(var0).get("model"))
         );
   }

   static JSONObject buildCubeModel(String var0) {
      return buildEntityBlockModel(Map.of("all", var0));
   }

   static JSONObject buildEntityBlockModel(Map var0) {
      boolean var1 = var0.containsKey("all");
      JSONObject var2 = new JSONObject();
      var0.forEach((var1x, var2x) -> var2.put(var1x, var2x.replace(".png", "")));
      var2.put("particle", var2.getString(var1 ? "all" : "down"));
      JSONObject var3 = new JSONObject();
      var3.put("down", buildUvFace(var1 ? "all" : "down", 16.0, 5.333333333333333));
      var3.put("up", buildUvFace(var1 ? "all" : "up", 16.0, 5.333333333333333));
      var3.put("north", buildUvFace(var1 ? "all" : "north", 16.0, 16.0));
      var3.put("south", buildUvFace(var1 ? "all" : "south", 16.0, 16.0));
      var3.put("west", buildUvFace(var1 ? "all" : "west", 5.333333333333333, 16.0));
      var3.put("east", buildUvFace(var1 ? "all" : "east", 5.333333333333333, 16.0));
      JSONArray var4 = new JSONArray()
         .fluentAdd(
            new JSONObject()
               .fluentPut("from", new JSONArray().fluentAdd(0).fluentAdd(0).fluentAdd(0))
               .fluentPut("to", new JSONArray().fluentAdd(16).fluentAdd(16).fluentAdd(16))
               .fluentPut("faces", var3)
         );
      return new JSONObject().fluentPut("parent", "minecraft:block/block").fluentPut("textures", var2).fluentPut("elements", var4);
   }

   private static JSONObject buildUvFace(String var0, double var1, double var3) {
      return new JSONObject().fluentPut("texture", "#" + var0).fluentPut("uv", new JSONArray().fluentAdd(0).fluentAdd(0).fluentAdd(var1).fluentAdd(var3));
   }

   static JSONObject buildBreakingModel(String var0, int var1) {
      return buildCompositeModel(var0, breakingStageId(var1));
   }

   static JSONObject buildTranslucentBreakingModel(String var0, int var1) {
      return buildCompositeModel(var0, breakingTranslucentStageId(var1));
   }

   private static JSONObject buildCompositeModel(String var0, String var1) {
      return new JSONObject()
         .fluentPut(
            "model",
            new JSONObject()
               .fluentPut("type", "minecraft:composite")
               .fluentPut(
                  "models",
                  new JSONArray()
                     .fluentAdd(new JSONObject().fluentPut("type", "minecraft:model").fluentPut("model", var0))
                     .fluentAdd(new JSONObject().fluentPut("type", "minecraft:model").fluentPut("model", var1))
               )
         );
   }

   static String breakingStageId(int var0) {
      return "_iainternal:entity_blocks/breaking/stage_" + var0;
   }

   static String breakingStageModelPath(int var0) {
      return "assets/_iainternal/models/entity_blocks/breaking/stage_" + var0 + ".json";
   }

   static String breakingTranslucentModelPath(int var0) {
      return "ia_overlay_26_1_plus/assets/_iainternal/models/entity_blocks/breaking_translucent/stage_" + var0 + ".json";
   }

   static String breakingTranslucentStageId(int var0) {
      return "_iainternal:entity_blocks/breaking_translucent/stage_" + var0;
   }

   static JSONObject buildDestroyStageModel(int var0) {
      return buildDestroyStage(var0, false);
   }

   static JSONObject translucentDestroyStageModel(int var0) {
      return buildDestroyStage(var0, true);
   }

   private static JSONObject buildDestroyStage(int var0, boolean var1) {
      JSONObject var2 = new JSONObject();

      for (String var6 : new String[]{"down", "up", "north", "south", "west", "east"}) {
         var2.put(var6, new JSONObject().fluentPut("texture", "#crack").fluentPut("uv", new JSONArray().fluentAdd(0).fluentAdd(0).fluentAdd(16).fluentAdd(16)));
      }

      Object var7 = "minecraft:block/destroy_stage_" + var0;
      if (var1) {
         var7 = new JSONObject().fluentPut("sprite", var7).fluentPut("force_translucent", true);
      }

      return new JSONObject()
         .fluentPut("gui_light", "side")
         .fluentPut(
            "display",
            new JSONObject()
               .fluentPut(
                  "fixed",
                  new JSONObject()
                     .fluentPut("rotation", new JSONArray().fluentAdd(0).fluentAdd(0).fluentAdd(0))
                     .fluentPut("translation", new JSONArray().fluentAdd(0).fluentAdd(0).fluentAdd(0))
                     .fluentPut("scale", new JSONArray().fluentAdd(0.5).fluentAdd(0.5).fluentAdd(0.5))
               )
         )
         .fluentPut("ambientocclusion", false)
         .fluentPut("textures", new JSONObject().fluentPut("crack", var7).fluentPut("particle", "#crack"))
         .fluentPut(
            "elements",
            new JSONArray()
               .fluentAdd(
                  new JSONObject()
                     .fluentPut("from", new JSONArray().fluentAdd(-0.01).fluentAdd(-0.01).fluentAdd(-0.01))
                     .fluentPut("to", new JSONArray().fluentAdd(16.01).fluentAdd(16.01).fluentAdd(16.01))
                     .fluentPut("shade", false)
                     .fluentPut("faces", var2)
               )
         );
   }

   public void registerItemModels(ItemSpec var1) {
      if (var1 == null) {
         throw new RuntimeException(" wtf, you should call this only if the itemGraphics are set!");
      }

      ModelDefinitionBundle var2 = new ModelDefinitionLoader(var1).loadBundle();
      this.packFileEntries.addAll(var2.usedTextures());
      var2.statusModels().forEach((var1x, var2x) -> {
         if (!var2x.isVanillaReference()) {
            this.manifestJsons.put(var2x.bHp, var2x.bHq);
         }
      });
      if (var2.baseModel() != null && !var2.baseModel().isVanillaReference()) {
         this.manifestJsons.put(var2.baseModel().bHp, var2.baseModel().bHq);
      }

      if (var2.baseLayerJson() != null) {
         this.manifestJsons.put(var1.El(), var2.baseLayerJson());
      }

      this.furnitureItemJsons.put(var1.Eh(), var2.itemJson());
      this.furnitureItemJsons.putAll(var2.furnitureFiles());
   }
}
