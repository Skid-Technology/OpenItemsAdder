package dev.lone.itemsadder.core;

import beer.devs.fastnbt.nms.Version;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.enchants.AeWrapper;
import dev.lone.itemsadder.Core.OtherPlugins.enchants.AeWrapper.PendingEnchantException;
import dev.lone.itemsadder.Core.OtherPlugins.mmoitems.MmoItemsHook;
import dev.lone.itemsadder.api.Events.ItemsAdderFirstLoadEvent;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import dev.lone.itemsadder.api.ItemsAdder.Advanced.InjectedItemModifier;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.block.CustomBlockListener;
import dev.lone.itemsadder.block.CustomBlockStateSettings;
import dev.lone.itemsadder.block.CustomBlockVariant;
import dev.lone.itemsadder.block.ExplosiveBlockListener;
import dev.lone.itemsadder.command.TargetScope;
import dev.lone.itemsadder.command.ZipCommand;
import dev.lone.itemsadder.config.ConfigConversionBackup;
import dev.lone.itemsadder.config.ConfigFileScanner;
import dev.lone.itemsadder.config.ConfigMerger;
import dev.lone.itemsadder.config.ConfigSectionLoader;
import dev.lone.itemsadder.config.ContentIndex;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.config.YamlDuplicateKeyChecker;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.DisplayEntityPool;
import dev.lone.itemsadder.entity.EntityAttachmentListener;
import dev.lone.itemsadder.entity.EntityConfigLoader;
import dev.lone.itemsadder.entity.EntityTypeFilter;
import dev.lone.itemsadder.entity.MobGoalLoader;
import dev.lone.itemsadder.entity.MountTrackingListener;
import dev.lone.itemsadder.entity.ProjectileListener;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.fontimage.ChatPacketListener;
import dev.lone.itemsadder.fontimage.FontImageChatRenderer;
import dev.lone.itemsadder.fontimage.FontImageLoader;
import dev.lone.itemsadder.gui.GuiEventGuard;
import dev.lone.itemsadder.gui.GuiIconRegistry;
import dev.lone.itemsadder.gui.ScrollableGuiAssets;
import dev.lone.itemsadder.item.AnvilEnchantListener;
import dev.lone.itemsadder.item.AnvilRenameListener;
import dev.lone.itemsadder.item.ArrowProjectileListener;
import dev.lone.itemsadder.item.BlockBreakListener;
import dev.lone.itemsadder.item.BlockDigListener;
import dev.lone.itemsadder.item.BlockFluidListener;
import dev.lone.itemsadder.item.BlockInteractionListener;
import dev.lone.itemsadder.item.CustomItemDataEntry;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.DeathRespawnItemListener;
import dev.lone.itemsadder.item.FurnaceItemListener;
import dev.lone.itemsadder.item.FurnitureItemEntity;
import dev.lone.itemsadder.item.GrindstoneListener;
import dev.lone.itemsadder.item.InventoryItemListener;
import dev.lone.itemsadder.item.ItemActionBase;
import dev.lone.itemsadder.item.ItemDefinitionLoader;
import dev.lone.itemsadder.item.ItemEventDispatcher;
import dev.lone.itemsadder.item.ItemEventListener;
import dev.lone.itemsadder.item.ItemInteractFallbackListener;
import dev.lone.itemsadder.item.ItemInteractListener;
import dev.lone.itemsadder.item.ItemLoadSession;
import dev.lone.itemsadder.item.ItemLoadTask;
import dev.lone.itemsadder.item.ItemLoader;
import dev.lone.itemsadder.item.ItemMetaTracker;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.item.ItemThrowManager;
import dev.lone.itemsadder.item.ItemUseHandler;
import dev.lone.itemsadder.item.ItemUseListener;
import dev.lone.itemsadder.item.MerchantInteractListener;
import dev.lone.itemsadder.item.PlaceItemListener;
import dev.lone.itemsadder.item.PlacedBlockInteractListener;
import dev.lone.itemsadder.item.PlacedBlockItemDefinition;
import dev.lone.itemsadder.item.PlantItemDefinition;
import dev.lone.itemsadder.item.PrepareEnchantListener;
import dev.lone.itemsadder.item.effect.ArmorEffectListener;
import dev.lone.itemsadder.item.effect.BookEditListener;
import dev.lone.itemsadder.item.effect.BowEffectListener;
import dev.lone.itemsadder.item.effect.ConsumeEffectListener;
import dev.lone.itemsadder.item.effect.FishingEffectListener;
import dev.lone.itemsadder.item.effect.GeneralItemEffectListener;
import dev.lone.itemsadder.item.effect.ItemEventEffectListener;
import dev.lone.itemsadder.item.effect.ProjectileEffectListener;
import dev.lone.itemsadder.item.effect.ToolStateListener;
import dev.lone.itemsadder.item.effect.TridentTrackingManager;
import dev.lone.itemsadder.lang.LocaleLoader;
import dev.lone.itemsadder.lang.MessageFormatter;
import dev.lone.itemsadder.lang.MinecraftLocales;
import dev.lone.itemsadder.loot.LootManager;
import dev.lone.itemsadder.loot.LootTableLoader;
import dev.lone.itemsadder.metrics.MetricsManager;
import dev.lone.itemsadder.network.ContainerPacketListener;
import dev.lone.itemsadder.network.EntityMetadataListener;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.network.PlayerActionPacketListener;
import dev.lone.itemsadder.network.ScoreboardPacketListener;
import dev.lone.itemsadder.network.TradeSelectionListener;
import dev.lone.itemsadder.particle.ParticleDisplayManager;
import dev.lone.itemsadder.recipe.AnvilRecipeListener;
import dev.lone.itemsadder.recipe.CraftingManager;
import dev.lone.itemsadder.recipe.CustomItemOutput;
import dev.lone.itemsadder.recipe.FurnaceRecipeListener;
import dev.lone.itemsadder.recipe.MaterialOutput;
import dev.lone.itemsadder.recipe.RecipeManagerHolder;
import dev.lone.itemsadder.recipe.RecipeOutput;
import dev.lone.itemsadder.recipe.RecipeRegistry;
import dev.lone.itemsadder.recipe.SmithingRecipeListener;
import dev.lone.itemsadder.resourcepack.AssetPathResolver;
import dev.lone.itemsadder.resourcepack.GeneratedAssetStore;
import dev.lone.itemsadder.resourcepack.PackBuildWorker;
import dev.lone.itemsadder.resourcepack.PackConfigProcessor;
import dev.lone.itemsadder.resourcepack.PackWarningSummary;
import dev.lone.itemsadder.resourcepack.ResourcePackGenerationTask;
import dev.lone.itemsadder.resourcepack.ResourcePackGenerator;
import dev.lone.itemsadder.resourcepack.ResourcePackLoadState;
import dev.lone.itemsadder.resourcepack.ResourcePackService;
import dev.lone.itemsadder.resourcepack.VanillaAssetException;
import dev.lone.itemsadder.resourcepack.VanillaAssetManager;
import dev.lone.itemsadder.script.ScriptManager;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.BossBarManager;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.CounterRegistry;
import dev.lone.itemsadder.util.DoubleCallback;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.FileListHelper;
import dev.lone.itemsadder.util.LocalizedMessages;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.MapBuilderHelper;
import dev.lone.itemsadder.util.PersistentCounter;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.ServerCompat;
import dev.lone.itemsadder.util.Utils;
import dev.lone.itemsadder.world.BlockStateManager;
import dev.lone.itemsadder.world.BlockVisibilityTracker;
import dev.lone.itemsadder.world.ChunkDataStore;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import dev.lone.itemsadder.worldgen.TreeDecorator;
import dev.lone.itemsadder.worldgen.WorldGenerationManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;
import lombok.Generated;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class ContentManager {
   private final AtomicReference field52 = new AtomicReference();
   private final ThreadLocal field53 = new ThreadLocal();
   private final AtomicReference field54 = new AtomicReference();
   private final CompletableFuture field55 = new CompletableFuture();
   private volatile boolean field56 = true;
   private volatile boolean field57;
   private volatile boolean field58;
   private volatile long field59;
   private boolean field60;
   private boolean field61;
   private boolean field62;
   private Runnable field63;
   private volatile MessageFormatter field64;
   private volatile ContentIndex field65;
   @Deprecated
   public static Gson field66;
   static ContentManager field67;
   final Plugin field68;
   public static final String field69 = Main.getInstance().getDataFolder().getAbsolutePath();
   public static final String field70 = field69 + "/storage/";
   public static final String field71 = "generated.zip";
   public static final String field72 = field69 + "/output/generated.zip";
   public static final String field73 = field69 + "/contents/";
   public static final String field74 = field73 + "vanilla/resourcepack/";
   public static final String field75 = field69 + "/data/resource_pack/";
   public static final String field76 = field75 + "assets/";
   public static final String field77 = field69 + "/data/items_packs/";
   public static final String field78 = "__manually_handled";
   public static final char field79 = '~';
   static Set field80 = new HashSet();
   static Set field81 = new HashSet();
   public volatile boolean field82;
   public volatile boolean field83;
   public List field84 = new ArrayList();
   public final GeneratedAssetStore field85 = new GeneratedAssetStore();
   public final ParticleDisplayManager field86 = new ParticleDisplayManager();
   public List field87 = new ArrayList();
   public List field88 = new ArrayList();
   public boolean field89;
   public ScriptManager field90;
   public VanillaAssetManager field91;
   public LinkedHashMap field92 = new LinkedHashMap();
   public Map field93 = new HashMap();
   public LinkedHashMap field94 = new LinkedHashMap();
   public LinkedHashMap field95 = new LinkedHashMap();
   public LinkedHashMap field96 = new LinkedHashMap();
   @Deprecated
   public LinkedHashMap field97 = new LinkedHashMap();
   public LinkedHashMap field98 = new LinkedHashMap();
   public LinkedHashMap ab = new LinkedHashMap();
   Map ac = new HashMap();
   public HashMap ad = new HashMap();
   public HashMap ae = new HashMap();
   public HashMap af = new HashMap();
   public HashMap ag = new HashMap();
   public HashMap ah = new HashMap();
   public HashMap ai = new HashMap();
   public HashMap aj = new HashMap();
   public HashMap ak = new HashMap();
   public final LinkedHashSet al = new LinkedHashSet();
   public LinkedHashMap am = new LinkedHashMap();
   public LinkedHashMap an = new LinkedHashMap();
   public final DisplayEntityPool ao = new DisplayEntityPool();
   public final BlockStateManager ap = new BlockStateManager();
   public final BlockVisibilityTracker aq = new BlockVisibilityTracker(this.ap, this.ao);
   public final Map ar = new HashMap();
   public final HashSet as = new HashSet();
   public List at = new ArrayList();
   public HashMap au = new HashMap();
   public HashMap av = new HashMap();
   Map aw = new HashMap();
   List ax = new ArrayList();
   public HashMap ay = new HashMap();
   public HashMap az = new HashMap();
   public HashMap aA = new HashMap();
   public static final HashMap aB = new HashMap();
   public static final List aC = new ArrayList();
   private final ConfigFileScanner aD;
   public MessageFormatter aE;
   public MobGoalLoader aF;
   public AbstractPlayerValueProvider aG;
   public PlayerEffectsManager aH;
   public ChunkDataStore aI;
   public LootManager aJ;
   public ItemEventListener aK;
   public ResourcePackService aL;
   private ResourcePackGenerationTask aM;
   public BookEditListener aN;
   public BowEffectListener aO;
   public FishingEffectListener aP;
   public ConsumeEffectListener aQ;
   public ItemEventEffectListener aR;
   public ArmorEffectListener aS;
   public GeneralItemEffectListener aT;
   public ProjectileEffectListener aU;
   public EntityAttachmentListener aV;
   public ItemInteractListener aW;
   public ItemUseHandler aX;
   public PlacedBlockInteractListener aY;
   public ArrowProjectileListener aZ;
   public InventoryItemListener ba;
   public ItemInteractFallbackListener field99;
   public DeathRespawnItemListener bc;
   public BlockBreakListener bd;
   public BlockInteractionListener be;
   public MerchantInteractListener bf;
   public FurnaceItemListener bg;
   public ItemThrowManager bh;
   public BlockFluidListener bi;
   public ItemUseListener bj;
   public PlaceItemListener bk;
   public ExplosiveBlockListener bl;
   public CustomBlockListener bm;
   public PrepareEnchantListener bn;
   public AnvilEnchantListener bo;
   public RecipeRegistry bp;
   @Nullable
   public CraftingManager bq;
   @Nullable
   public FurnaceRecipeListener br;
   @Nullable
   public SmithingRecipeListener bs;
   @Nullable
   public AnvilRecipeListener bt;
   public AnvilRenameListener bu;
   public GrindstoneListener bv;
   public ProjectileListener bw;
   @Nullable
   private BlockInteractionListener bx;
   public final HashMap by = new HashMap();
   public final LocalizedMessages bz = new LocalizedMessages();
   public final MinecraftLocales bA = new MinecraftLocales();
   public GuiIconRegistry bB;
   public FontImageChatRenderer bC;
   public ChatPacketListener bD;
   public boolean bE;

   private ContentManager(Plugin var1, VanillaAssetManager var2) {
      field67 = this;
      this.field68 = var1;
      this.field91 = var2;
      this.aD = new ConfigFileScanner();
      this.method8076();

      try {
         field80 = Utils.listResourceEntries("internal/models", var0 -> var0.endsWith(".json")).stream().map(var0 -> {
            String var1x = FilenameUtils.removeExtension(var0);
            var1x = var1x.replace("internal/models/", "");
            return "minecraft:" + var1x;
         }).collect(Collectors.toSet());
         field81 = Utils.listResourceEntries("internal/textures", var0 -> var0.endsWith(".png")).stream().map(var0 -> {
            String var1x = FilenameUtils.removeExtension(var0);
            var1x = var1x.replace("internal/textures/", "");
            return "minecraft:" + var1x;
         }).collect(Collectors.toSet());
      } catch (IOException var4) {
         Log.error("Error while loading builtin assets from plugin resources.", var4);
      }

      new ToolStateListener();
      SchedulerUtils.runRepeating(new ContentLoadCallback(this), 54000L, 54000L);
      new MetricsManager().register();
      this.field90 = new ScriptManager();
   }

   private void method8076() {
      if (!Settings.get().compressJson) {
         field66 = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
      } else {
         field66 = new GsonBuilder().disableHtmlEscaping().create();
      }
   }

   public static boolean method8077(String var0) {
      return field80.contains(var0);
   }

   public static boolean method8078(String var0) {
      return field81.contains(var0.contains(":") ? var0 : "minecraft:" + var0);
   }

   public void method8079() {
      this.method8080(false);
   }

   public void method8080(boolean var1) {
      this.field62 = var1;

      try {
         CompletableFuture var2 = this.method8102(false, false);
         if (var1) {
            if (!var2.isDone()) {
               throw new IllegalStateException("Initial content load unexpectedly requires a later server tick");
            }

            var2.join();
         }

         var2.thenRun(() -> SchedulerUtils.runLaterSync(() -> {
            if (!this.field57 && this.method8110()) {
               this.method8081();
               if (this.method8110()) {
                  this.method8082();
               }
            }
         }, 1L));
      } finally {
         this.field62 = false;
      }
   }

   public void method8081() {
      if (!this.field57 && this.field63 != null) {
         Runnable var1 = this.field63;
         this.field63 = null;

         try {
            var1.run();
         } catch (Throwable var3) {
            this.method8107(var3);
            this.field55.completeExceptionally(var3);
         }
      }
   }

   private void method8082() {
      try {
         if (!this.aL.generator.packFileExists()) {
            boolean var1 = this.method8133(false, () -> Log.log("Resourcepack not found. Generating..."), var1x -> {
               this.field56 = false;
               if (var1x) {
                  Log.log("Resourcepack generated.");
                  this.aL.delivery.hostPackAndNotify(() -> ZipCommand.method1509(null, false, TargetScope.ALL), Bukkit.getConsoleSender());
               } else {
                  BossBarManager.clear();
               }
            }, BossBarManager::setProgress, null);
            if (!var1) {
               this.field56 = false;
            }
         } else {
            this.field56 = false;
            this.aL.delivery.hostPackAndNotify(BossBarManager::clear, Bukkit.getConsoleSender());
         }
      } catch (RuntimeException | Error var2) {
         this.field56 = false;
         throw var2;
      }
   }

   public boolean method8083() {
      return this.field56;
   }

   public static ContentManager method8084() {
      return field67;
   }

   public static ContentManager method8085(Plugin var0, VanillaAssetManager var1) {
      if (field67 != null) {
         var0.getLogger().severe("Core already initialized. Use the Core.inst() method instead of Core.inst(plugin).");
         return field67;
      } else {
         field67 = new ContentManager(var0, var1);
         return field67;
      }
   }

   public static boolean method8086(Block var0) {
      CustomBlockChunkStoreBase var1 = method8084().method8183(var0);
      if (!var1.isCustomBlock(var0)) {
         return false;
      }

      PlacedBlockItemDefinition var2 = (PlacedBlockItemDefinition)method8084().method8154(var1.getCustomBlockId(var0));
      return var2 == null ? false : var2.aoh.hasAction("block_trade_machine");
   }

   public static File method8087() {
      return new File(Main.getInstance().getDataFolder(), "output");
   }

   public static boolean method8088(@Nullable String var0, YamlConfig var1) {
      return ConfigFileScanner.method1905(var0, var1);
   }

   public boolean method8089() {
      return this.field82;
   }

   public boolean method8090() {
      return this.field83;
   }

   public static Material method8091() {
      return Material.BARRIER;
   }

   public Collection method8092() {
      return this.aD.method1901();
   }

   public Map method8093() {
      return this.aD.method1902();
   }

   public void method8094(boolean var1) {
      this.method8095(true, var1);
   }

   public void method8095(boolean var1, boolean var2) {
      this.method8096(var2);
      RuntimeException var3 = null;

      try {
         if (this.aI != null) {
            this.aI.saveAllRegions(false);
         }
      } catch (RuntimeException var5) {
         var3 = var5;
      }

      try {
         if (this.aG != null) {
            this.aG.method707(false);
         }
      } catch (RuntimeException var6) {
         if (var3 == null) {
            var3 = var6;
         } else {
            var3.addSuppressed(var6);
         }
      }

      if (var3 != null) {
         throw var3;
      }
   }

   private void method8096(boolean var1) {
      this.field86.method2155();
      PlayerActionPacketListener.br();
      if (var1) {
         MountTrackingListener.bi();
      } else {
         MountTrackingListener.aM();
      }

      SchedulerUtils.cancelTasksFor(this.getClass());
      this.aq.stop();
      WorldGenerationManager.Co();
      ContainerPacketListener.Ml();
      GuiEventGuard.LF();
      if (this.aL != null) {
         this.aL.stop();
      }

      if (this.aH != null) {
         this.aH.Ai();
      }

      IaListener.method532(this.aK);
      IaListener.method532(this.aS);
      IaListener.method532(this.aT);
      IaListener.method532(this.aR);
      IaListener.method532(this.aQ);
      IaListener.method532(this.aO);
      IaListener.method532(this.aN);
      IaListener.method532(this.aP);
      IaListener.method532(this.aU);
      IaListener.method532(this.aX);
      IaListener.method532(this.be);
      IaListener.method532(this.aZ);
      IaListener.method532(this.ba);
      IaListener.method532(this.field99);
      IaListener.method532(this.bc);
      IaListener.method532(this.bd);
      IaListener.method532(this.bh);
      IaListener.method532(this.aV);
      IaListener.method532(this.aW);
      IaListener.method532(this.aY);
      IaListener.method532(this.bf);
      IaListener.method532(this.bg);
      IaListener.method532(this.bi);
      IaListener.method532(this.bj);
      IaListener.method532(this.bk);
      IaListener.method532(this.bl);
      if (this.bm != null) {
         this.bm.unregister();
      }

      IaListener.method532(this.bn);
      IaListener.method532(this.bo);
      if (!var1) {
         IaListener.method532(this.bq);
         IaListener.method532(this.br);
         IaListener.method532(this.bs);
         IaListener.method532(this.bt);
      }

      if (this.aG instanceof IaListener) {
         IaListener.method532((IaListener)this.aG);
      }

      IaListener.method532(this.aH);
      if (this.aJ != null) {
         IaListener.method532(this.aJ.atu);
      }

      IaListener.method532(this.bD);
      if (!var1) {
         TridentTrackingManager.unregister();
      }

      if (this.bx != null) {
         if (var1) {
            this.bx.stop();
         } else {
            this.bx.shutdown();
         }
      }
   }

   public void method8097(boolean var1) {
      ItemEventDispatcher.aiA = false;
      NamespaceRegistrar.Gi();
      EntityMetadataListener.Ng().Co();
      this.field92.clear();
      this.field95.clear();
      this.field94.clear();
      this.field96.clear();
      this.field97.clear();
      this.ac.clear();
      this.field98.clear();
      this.ab.clear();
      this.au.clear();
      this.av.clear();
      this.ad.clear();
      this.ae.clear();
      this.af.clear();
      this.ag.clear();
      this.ah.clear();
      this.ai.clear();
      this.aj.clear();
      this.ak.clear();
      this.ar.clear();
      this.aw.clear();
      this.ax.clear();
      this.al.clear();
      this.am.clear();
      this.aq.stop();
      this.ao.clear();
      this.ap.clear();
      this.as.clear();
      this.at.clear();
      this.ay.clear();
      this.az.clear();
      this.aA.clear();
      if (!var1 && this.bp != null) {
         this.bp.method2221(var1);
      }

      if (CustomEntityManager.isInitialized()) {
         CustomEntityManager.getInstance().clearDefinitions();
      }

      CustomBlockVariant.Co();
      BlockDigListener.BR().unregister();
   }

   public boolean method8098() {
      return aB != null && !aB.isEmpty() || aC != null && !aC.isEmpty();
   }

   public void method8099() {
      aC.clear();
      aB.clear();
   }

   public void method8100(Plugin var1) {
      aB.forEach((var1x, var2) -> var2.removeIf(var1xx -> var1.getName().equals(var1xx.owner)));
      aC.removeIf(var1x -> var1.getName().equals(var1x.owner));
   }

   public void method8101(@Nullable Runnable var1, boolean var2, boolean var3) {
      this.method8102(var2, var3).whenComplete((var1x, var2x) -> {
         if (var2x == null && var1 != null) {
            try {
               var1.run();
            } catch (Throwable var4) {
               Log.error("Content completion callback failed.", var4);
            }
         }
      });
   }

   public CompletableFuture method8102(boolean var1, boolean var2) {
      ResourcePackLoadState var3 = this.method8137();
      return var3 == null
         ? CompletableFuture.failedFuture(new IllegalStateException("A content/resource-pack operation is already running"))
         : this.method8103(var1, var2, var3, null).whenComplete((var1x, var2x) -> var3.complete());
   }

   public CompletableFuture method8103(boolean var1, boolean var2, ResourcePackLoadState var3, @Nullable Runnable var4) {
      CompletableFuture var5 = new CompletableFuture();
      if (!this.field57 && var3 != null && var3.isActive() && this.field54.compareAndSet(null, var5)) {
         var3.getFuture().thenRun(() -> {
            if (this.field54.compareAndSet(var5, null)) {
               var5.cancel(false);
            }
         });

         try {
            SchedulerUtils.runOnMainThread(() -> {
               if (!this.field57 && var3.isActive() && this.field54.get() == var5) {
                  this.method8108();
                  this.field58 = false;

                  try {
                     if (var4 != null) {
                        var4.run();
                     }

                     this.method8118(var1, var2, var5, var3);
                  } catch (Throwable var7x) {
                     this.method8104(var5, var7x, var3, var1, var2);
                  }
               } else {
                  this.field54.compareAndSet(var5, null);
                  var5.cancel(false);
               }
            });
         } catch (Throwable var7) {
            this.field54.compareAndSet(var5, null);
            var5.completeExceptionally(var7);
         }

         return var5;
      } else {
         var5.completeExceptionally(new IllegalStateException("Content load unavailable or already running"));
         return var5;
      }
   }

   private void method8104(CompletableFuture var1, Throwable var2, ResourcePackLoadState var3, boolean var4, boolean var5) {
      if (this.field54.get() == var1) {
         this.field82 = false;
         this.field83 = false;
         this.field60 = false;
         this.field58 = true;
         CustomBlockStateSettings.getInstance().resetContentLoad();
         this.field64 = null;
         this.field65 = null;

         try {
            this.method8096(false);
         } catch (Throwable var7) {
            var2.addSuppressed(var7);
         }

         BossBarManager.clear();
         if (var2 instanceof PendingEnchantException var6 && var3.pendingKeys.add(var6.enchant)) {
            Log.log("Waiting for AdvancedEnchantments definition '" + var6.enchant + "' before publishing content.");
            AeWrapper.awaitRegistration(
               var6, this.field68, () -> var3.isActive() && this.field54.get() == var1, var5x -> this.method8105(var1, var5x, var3, var4, var5)
            );
            return;
         }

         if (var2 instanceof VanillaAssetException var8 && var3.handleAssetFailure(var8)) {
            Log.warn("Content loading suspended while vanilla assets are repaired.");
            this.field91.repairAll(var8, var5x -> this.method8105(var1, var5x, var3, var4, var5));
            return;
         }

         this.field54.compareAndSet(var1, null);
         Log.error("Content loading failed. Content is unavailable; fix the cause and run /iareload.", var2);
      }

      var1.completeExceptionally(var2);
   }

   private void method8105(CompletableFuture var1, Throwable var2, ResourcePackLoadState var3, boolean var4, boolean var5) {
      if (var3.isActive() && this.field54.get() == var1) {
         if (var2 != null) {
            this.method8104(var1, var2, var3, var4, var5);
         } else {
            try {
               this.method8108();
               this.field58 = false;
               this.method8118(var4, var5, var1, var3);
            } catch (Throwable var7) {
               this.method8104(var1, var7, var3, var4, var5);
            }
         }
      }
   }

   private void method8106(CompletableFuture var1, ResourcePackLoadState var2) {
      if (!this.field57 && var2.isActive() && this.field54.compareAndSet(var1, null)) {
         if (this.field63 == null) {
            this.field55.complete(null);
         }

         var1.complete(null);
      } else {
         var1.cancel(false);
      }
   }

   public void method8107(Throwable var1) {
      this.field82 = false;
      this.field83 = false;
      this.field60 = false;
      this.field64 = null;
      this.field65 = null;
      this.field58 = true;
      CustomBlockStateSettings.getInstance().resetContentLoad();
      Log.error("Content preparation failed. Fix the cause and run /iareload.", var1);
   }

   public void method8108() {
      if (!Bukkit.isPrimaryThread()) {
         throw new IllegalStateException("Content teardown must run on the server thread");
      }

      this.field63 = null;
      if (this.method8110()) {
         this.field93.clear();

         for (CustomItemDefinition var2 : this.field92.values()) {
            FurnitureItemEntity var3 = (FurnitureItemEntity)var2.aoh.getAction("furniture");
            if (var3 != null) {
               this.field93.put(var2.getNamespacedID(), new EntityTypeFilter(var3.acW, var3.Au()));
            }
         }
      }

      this.field59++;
      this.field82 = false;
      this.field83 = false;
      this.field60 = false;
      this.field64 = null;
      this.field65 = null;
   }

   public MessageFormatter method8109() {
      return Bukkit.isPrimaryThread() ? this.aE : this.field64;
   }

   public boolean method8110() {
      return !this.field57 && this.field82 && this.field83;
   }

   public boolean method8111() {
      return this.method8110() || !this.field57 && Bukkit.isPrimaryThread() && (this.field60 || this.field61);
   }

   public Map method8112() {
      if (!this.method8111()) {
         return Map.of();
      } else if (!Bukkit.isPrimaryThread()) {
         ContentIndex var1 = this.field65;
         return var1 == null ? Map.of() : var1.av();
      } else {
         return (this.field57 || this.field58) && this.field54.get() == null ? Map.of() : this.field92;
      }
   }

   public boolean method8113() {
      return this.field58;
   }

   public long method8114() {
      return this.field59;
   }

   public boolean method8115(long var1) {
      return !this.field57 && this.field59 == var1;
   }

   public CompletableFuture method8116() {
      return this.field55.copy();
   }

   public void method8117() {
      this.field57 = true;
      this.field64 = null;
      this.field65 = null;
      this.field59++;
      this.field82 = false;
      this.field83 = false;
      this.field60 = false;
      CompletableFuture var1 = (CompletableFuture)this.field54.getAndSet(null);
      if (var1 != null) {
         var1.cancel(false);
      }

      this.field55.cancel(false);
      ResourcePackLoadState var2 = (ResourcePackLoadState)this.field52.get();
      if (var2 != null) {
         var2.complete();
      }

      CustomBlockStateSettings.getInstance().resetContentLoad();
   }

   private void method8118(boolean var1, boolean var2, CompletableFuture var3, ResourcePackLoadState var4) {
      if (!Bukkit.isPrimaryThread()) {
         throw new IllegalStateException("Content must be initialized on the server thread");
      }

      if (!this.field91.isReady()) {
         throw new IllegalStateException("Vanilla assets are not loaded");
      }

      this.field91.clearFailure();
      this.method8076();
      boolean var5 = var1 && var2 && this.bp != null;
      BossBarManager.setTitleKeepProgress("Cleaning up...");
      this.method8097(var2);
      PlayerActionPacketListener.init();
      MountTrackingListener.bj();
      if (Settings.get().ffmpegEnabled) {
         try {
            PackBuildWorker.configure(new File(this.field68.getDataFolder(), "storage" + File.separator + "cache" + File.separator + "ffmpeg"));
         } catch (Throwable var7) {
            Log.warn("Error while initializing FFMPEG library: " + var7.getMessage());
         }
      } else {
         PackBuildWorker.disable();
      }

      new ItemMetaTracker();
      BossBarManager.setTitleKeepProgress("Refreshing contents...");
      this.method8205(false);
      CustomBlockStateSettings.getInstance().init();
      BossBarManager.setTitleKeepProgress("Initializing...");
      if (this.aI == null) {
         this.aI = new ChunkDataStore(this.field68);
      }

      this.aL = new ResourcePackService(this.field68);
      this.aM = new ResourcePackGenerationTask(this.field68, this.aL);
      this.method8119();
      this.method8120(var5);
      this.bw = new ProjectileListener(this.field68);
      this.method8121();
      ScoreboardPacketListener.method2041(Settings.get().hideScoreboardNumbers);
      TradeSelectionListener.method3372(
         Settings.get().fixTradeGuiItemsAdderMachines || Settings.get().fixTradeGuiVanillaVillagers || Settings.get().fixTradeGuiOtherPlugins
      );
      this.method8124(var5, var2, var3, var4);
   }

   private void method8119() {
      this.aK = new ItemEventListener(this.field68, this);
      this.aS = new ArmorEffectListener(this.field68, this);
      this.aT = new GeneralItemEffectListener(this.field68, this);
      this.aR = new ItemEventEffectListener(this.field68, this);
      this.aQ = new ConsumeEffectListener(this.field68, this);
      this.aO = new BowEffectListener(this.field68, this);
      this.aN = new BookEditListener(this);
      this.aP = new FishingEffectListener(this.field68, this);
      this.aU = new ProjectileEffectListener(this.field68, this);
      this.aX = new ItemUseHandler();
      this.be = new BlockInteractionListener(this.field68, this);
      this.aZ = new ArrowProjectileListener(this.field68, this);
      this.ba = new InventoryItemListener(this.field68, this);
      this.field99 = new ItemInteractFallbackListener(this.field68);
      this.bc = new DeathRespawnItemListener(this.field68, this);
      this.bd = new BlockBreakListener(this.field68);
      this.bh = new ItemThrowManager(this.field68, this);
      this.aV = new EntityAttachmentListener(this.field68);
      this.aW = new ItemInteractListener(this.field68);
      this.aY = new PlacedBlockInteractListener(this.field68);
      this.bf = new MerchantInteractListener(this.field68, this);
      this.bg = new FurnaceItemListener(this.field68, this);
      this.bk = new PlaceItemListener(this.field68, this);
      if (ServerCompat.isPaper()) {
         this.bl = new ExplosiveBlockListener();
      }

      this.bm = CustomBlockListener.BU();
      this.bn = new PrepareEnchantListener(this.field68, this);
      this.bo = new AnvilEnchantListener(this.field68, this);
   }

   private void method8120(boolean var1) {
      if (!var1) {
         IaListener.method532(this.bq);
         IaListener.method532(this.br);
         IaListener.method532(this.bs);
         IaListener.method532(this.bt);
         if (this.bp != null) {
            this.bp.method2221(true);
         }

         this.bp = new RecipeRegistry();
         this.bq = null;
         this.br = null;
         this.bs = null;
         this.bt = null;
         if (Settings.get().recipesCrafting) {
            this.bq = new CraftingManager(this.field68, this);
         }

         if (Settings.get().recipesCooking) {
            this.br = new FurnaceRecipeListener(this.field68, this);
         }

         if (Settings.get().recipesSmithing) {
            this.bs = new SmithingRecipeListener(this.field68, this);
         }

         if (Settings.get().recipesAnvil) {
            this.bt = new AnvilRecipeListener(this.field68);
         }

         RecipeManagerHolder.Qc();
         if (this.bu == null) {
            this.bu = new AnvilRenameListener(this.field68);
         }

         if (this.bv == null) {
            this.bv = new GrindstoneListener(this.field68);
         }
      }
   }

   private void method8121() {
      if (Settings.get().playerStatsUsePlayerDat) {
         this.aG = new PlayerStatsProvider(this.field68, this);
      } else {
         this.aG = new PlayerValueProviderImpl(this.field68, this);
      }

      this.aE = new MessageFormatter(this.field68, this);
      this.aF = new MobGoalLoader(this.field68, this);
      this.aH = new PlayerEffectsManager(this.field68, this);
      this.aJ = new LootManager(this.field68, this);
   }

   private void method8122(boolean var1) {
      if (Version.isAtLeast(Version.v1_21_4)) {
         if (!this.au.isEmpty()) {
            if (!var1) {
               this.bx = new dev.lone.itemsadder.block.BlockInteractionListener();
               this.bx.onEnable();
            } else if (this.bx != null) {
               this.bx.reloadCropTracking();
            } else {
               this.bx = new dev.lone.itemsadder.block.BlockInteractionListener();
               this.bx.onEnable();
            }
         } else if (this.bx != null) {
            this.bx.shutdown();
            this.bx = null;
         }
      }
   }

   @Deprecated
   public static void method8123(Runnable var0) {
      SchedulerUtils.runOnMainThread(var0);
   }

   private void method8124(boolean var1, boolean var2, CompletableFuture var3, ResourcePackLoadState var4) {
      long var5 = this.field59;
      BossBarManager.setTitleKeepProgress("Preparing content load...");
      this.field82 = false;
      PackConfigProcessor.resetDedupState();
      PackWarningSummary.reset();
      YamlDuplicateKeyChecker.reset();
      CustomBlockStateSettings.getInstance().beginContentLoad();
      PersistentCounter var7 = new PersistentCounter();
      CounterRegistry var8 = new CounterRegistry();
      BossBarManager.setTitleKeepProgress("Extracting internal files...");
      var8.startNewCounter("extract-internal-files");
      if (Settings.get().extractInternalUtilityAssets) {
         ResourcePackGenerator.extractDefaultContents();
      }

      BossBarManager.setTitleKeepProgress("Refreshing content file cache...");
      if (Settings.get().logConfigLoading) {
         Log.log(ChatColor.AQUA + "[Content] Loading content...");
      }

      var8.startNewCounter("contents-cache");
      this.method8205(true);
      BossBarManager.setTitleKeepProgress("Finding files...");
      var8.startNewCounter("find-files");
      if (Settings.get().logConfigLoading) {
         Log.log(ChatColor.AQUA + "[Content] Loading YML files...");
      }

      this.aD.method1903(field73, field77, this.field89, var2);
      this.aL.manager.updateSoundPackFingerprint(this.method8092());
      var8.startNewCounter("compiling-scripts");
      BossBarManager.setTitleKeepProgress("Compiling Scripts...");
      NamespaceRegistrar.bf(var2);
      var8.startNewCounter("font-images");
      BossBarManager.setTitleKeepProgress("Font images...");
      this.aE.wS();
      if (Main.config.getBoolean("huds.enabled")) {
         BossBarManager.setTitleKeepProgress("HUDs...");
         var8.startNewCounter("huds");
         this.aF.loadGoals();
      } else {
         this.aF.setEnabled(false);
      }

      var8.startNewCounter("langs");
      BossBarManager.setTitleKeepProgress("Languages...");
      this.bA.clear();
      BossBarManager.setTitleKeepProgress("Loading dictionaries...");

      for (YamlConfig var10 : this.method8092()) {
         LocaleLoader.method6955(var10, this.bz);
         LocaleLoader.method6956(var10, this.bA);
      }

      ItemDefinitionLoader var12 = new ItemDefinitionLoader(this);
      BossBarManager.setTitleKeepProgress("Converting legacy configurations...");
      var8.startNewCounter("convert-legacy-configs");
      ConfigConversionBackup.Ge();
      BossBarManager.setTitleKeepProgress("Legacy Armors...");
      var8.startNewCounter("armors-legacy");
      MapBuilderHelper.buildLookup(this.method8092(), this.ad, this.ae);
      BossBarManager.setTitleKeepProgress("Sounds...");
      var8.startNewCounter("sounds");
      FontImageLoader.cD();
      BossBarManager.setTitleKeepProgress("Equipments...");
      var8.startNewCounter("equipments");
      ConfigMerger.method9229(this.method8092(), this.aw, this.field87, this.field84);
      BossBarManager.setTitleKeepProgress("Items...");
      var8.startNewCounter("items");
      ItemLoadSession var13 = var12.FY();
      ItemLoadTask var11 = new ItemLoadTask(var5, var1, var2, var7, var8, var12, var13, var3, var4);
      this.method8127(() -> this.method8125(var11));
   }

   private void method8125(ItemLoadTask var1) {
      try {
         this.method8130(var1.ai());
         if (!var1.aq().isActive() || this.field54.get() != var1.ap()) {
            throw new CancellationException("Content load was cancelled");
         }

         this.field61 = true;
         long var2 = System.nanoTime() + 100000000L;

         while (var1.ao().hasRemainingFiles()) {
            var1.ao().loadNextFile();
            if (!this.field62 && System.nanoTime() >= var2) {
               break;
            }
         }

         if (!var1.ao().hasRemainingFiles()) {
            var1.ao().finish();
            this.aS.registerArmorEffects(this.field68, this.method8092());
            this.bE = this.field92.values().stream().anyMatch(CustomItemDefinition::DI);
            this.method8127(() -> this.method8126(var1));
            return;
         }

         this.method8127(() -> this.method8125(var1));
      } catch (Throwable var7) {
         this.field61 = false;
         this.method8104(var1.ap(), var7, var1.aq(), var1.aj(), var1.ak());
         return;
      } finally {
         this.field61 = false;
      }
   }

   private void method8126(ItemLoadTask var1) {
      try {
         this.method8130(var1.ai());
         if (!var1.aq().isActive() || this.field54.get() != var1.ap()) {
            throw new CancellationException("Content load was cancelled");
         }

         this.field61 = true;
         this.method8128(var1);
         this.method8106(var1.ap(), var1.aq());
      } catch (Throwable var6) {
         this.field61 = false;
         this.method8104(var1.ap(), var6, var1.aq(), var1.aj(), var1.ak());
      } finally {
         this.field61 = false;
      }
   }

   private void method8127(Runnable var1) {
      if (this.field62) {
         var1.run();
      } else {
         SchedulerUtils.runLaterSync(var1, 1L);
      }
   }

   private void method8128(ItemLoadTask var1) {
      long var2 = var1.ai();
      boolean var4 = var1.aj();
      boolean var5 = var1.ak();
      PersistentCounter var6 = var1.al();
      CounterRegistry var7 = var1.am();
      ItemDefinitionLoader var8 = var1.an();
      BossBarManager.setTitleKeepProgress("Crops...");
      var7.startNewCounter("crops");
      LootTableLoader.cD();
      BossBarManager.setTitleKeepProgress("Registering cooldown indicators...");
      this.aK.Cq();
      BossBarManager.setTitleKeepProgress("Particle assets...");
      var7.startNewCounter("particles");

      try {
         this.field85
            .loadBundlesFiltered(
               this.field84.stream().map(File::toPath).toList(), var0 -> NativePacketsHolder.aN() && NativePacketsHolder.PD().hasNmsField(var0)
            );
      } catch (IOException | IllegalArgumentException var12) {
         Log.error("Failed to load particle bundles; retaining previous particle assets", var12);
      }

      if (Settings.get().customEntitiesEnabled) {
         BossBarManager.setTitleKeepProgress("Entities...");
         var7.startNewCounter("entities");
         if (Settings.get().customEntitiesDelayPackets) {
            Log.warn(
               "Custom entities 'delay_packets' is enabled. This helps client/server performance but might cause stuttering animations. More info: https://a.devs.beer/ia-delaypackets"
            );
         }

         EntityConfigLoader var9 = new EntityConfigLoader();
         var9.load(this);
         BossBarManager.setTitleKeepProgress("Entities Populators...");
         var7.startNewCounter("entities_populators");
         ConfigSectionLoader.cD();
      }

      if (!this.ay.isEmpty()) {
         BossBarManager.setTitleKeepProgress("Liquids...");
         IaListener.method532(this.bi);
         IaListener.method532(this.bj);
         if (Settings.get().liquidsEnabled) {
            this.bi = new BlockFluidListener(this.field68, this);
            this.bj = new ItemUseListener(this.field68, this);
         }
      }

      this.field60 = true;
      BossBarManager.clear();
      ItemLoader var13;
      if (!var4) {
         BossBarManager.setTitleKeepProgress("Preloading recipes...");
         var7.startNewCounter("recipes");
         var13 = new ItemLoader();
         var13.loadRecipesExcluding(this, this.method8093(), this.bt, var8.FZ());
      } else {
         var13 = null;
      }

      BossBarManager.setTitleKeepProgress("Reloading worlds configuration...");
      var7.startNewCounter("various");
      WorldGenerationManager.GH();
      BossBarManager.setTitleKeepProgress("Finalizing loaded items...");
      var8.Ga();
      WorldGenerationManager.GI();
      if (Settings.get().emotesEnabled) {
         BossBarManager.setTitleKeepProgress("Registering emotes...");
         var7.startNewCounter("emotes");
         CustomEntityManager.getInstance().loadAdvancedPlayerAnimations();
         CustomEntityManager.getInstance().registerBuiltInModels();
         BossBarManager.setTitleKeepProgress("Caching emote models...");
         CustomEntityManager.getInstance().rebuildEmoteIndex();
         BossBarManager.setTitleKeepProgress("Loading emote overrides...");

         for (YamlConfig var11 : this.method8092()) {
            CustomEntityManager.getInstance().loadEmoteConfig(var11);
         }

         BossBarManager.setTitleKeepProgress("Loading emote events and goals...");
         CustomEntityManager.getInstance().loadBehaviors();
      }

      BossBarManager.setTitleKeepProgress("Saving content IDs and statistics...");
      var7.startNewCounter("stats");
      Log.log(ChatColor.GREEN + "[Content] Loaded " + ChatColor.AQUA + this.field92.size() + ChatColor.GREEN + " items");
      CustomBlockStateSettings.getInstance().logUsedIds();
      var7.startNewCounter("cache");
      CustomBlockStateSettings.getInstance().reloadPersistentIds();
      ScrollableGuiAssets.XP().XS();
      if (Main.config.getBoolean("huds.enabled") && this.aF.abm.size() > 0) {
         BossBarManager.setTitleKeepProgress("Initializing HUDs...");
         var7.startNewCounter("huds");
         this.aH.init();
      }

      BossBarManager.setTitleKeepProgress("Loots...");
      var7.startNewCounter("loots");
      this.aJ.method1980(this.method8092());
      BossBarManager.setTitleKeepProgress("Initializing content categories...");
      this.bB = new GuiIconRegistry(this.field68, this);
      BossBarManager.setTitleKeepProgress("Initializing font images book...");
      var7.startNewCounter("chat-packet-handler");
      var7.startNewCounter("font-images-book");
      this.bC = new FontImageChatRenderer();
      BossBarManager.setTitleKeepProgress("Initializing chat packet handlers...");
      var7.startNewCounter("chat-packets-replace-handlers");
      this.bD = new ChatPacketListener(this.field68, this);
      var7.saveCurrent();
      BossBarManager.setTitleKeepProgress("Registering recipes...");
      if (!var4) {
         var7.startNewCounter("recipes-2");
         var13.registerLoadedRecipes(this.bq, this.br, this.bs);
         var7.saveCurrent();
      }

      BossBarManager.setTitleKeepProgress("Loading recipe categories...");
      this.bB.aJX.LR();
      if (!Bukkit.getWorlds().isEmpty()) {
         BossBarManager.setTitleKeepProgress("Rebuilding entity block cache...");
         this.ao.populate((World)Bukkit.getWorlds().get(0), this.al);
         this.ap.rebuildFromStore(this.aI);
         this.aq.start();
      }

      TridentTrackingManager.setEnabled(this.method8163());
      this.method8122(var5);
      BossBarManager.setTitleKeepProgress("Registering gameplay watchers...");
      this.aR.registerAll();
      if (var8.asg) {
         BossBarManager.setTitleKeepProgress("Refreshing biome registry...");
         method8190();
      }

      this.method8130(var2);
      this.field91.rethrowFailure();
      WorldGenerationManager.jS();
      CustomBlockStateSettings.getInstance().commitContentIdsWith(this.aE.wT());
      HashMap var14 = new HashMap();
      this.ai.forEach((var1x, var2x) -> var14.put(var1x, Collections.unmodifiableMap(new LinkedHashMap(var2x))));
      this.field65 = new ContentIndex(
         Collections.unmodifiableMap(new LinkedHashMap(this.field92)),
         Map.copyOf(this.field95),
         Map.copyOf(this.field96),
         Map.copyOf(this.field97),
         Map.copyOf(var14)
      );
      this.field64 = this.aE;
      this.field82 = true;
      this.field83 = true;
      this.aM.finishAndAutoApply(var5);
      if (this.field62) {
         Set var15 = Collections.newSetFromMap(new IdentityHashMap());
         var15.addAll(aC);
         aB.values().forEach(var15::addAll);
         this.field63 = () -> {
            if (!this.field57 && this.field59 == var2 && this.method8110()) {
               boolean var7x = aC.stream().anyMatch(var1xx -> !var15.contains(var1xx))
                  || aB.values().stream().flatMap(Collection::stream).anyMatch(var1xx -> !var15.contains(var1xx));
               if (var7x) {
                  this.field92.values().forEach(var2x -> this.method8132(null, var2x, var15));
                  this.method8120(false);
                  ItemLoader var8x = new ItemLoader();
                  var8x.loadRecipesExcluding(this, this.method8093(), this.bt, var8.FZ());
                  var8x.registerLoadedRecipes(this.bq, this.br, this.bs);
                  this.bB.aJX.LR();
               }

               this.method8129(var5, var4, var2);
               this.field55.complete(null);
            }
         };
      } else {
         this.method8129(var5, var4, var2);
         this.method8130(var2);
      }

      if (Settings.get().isLoggingEnabled("logging.performance.loading_ms")) {
         if (var5) {
            Log.logLevel(String.format("&a[Content] Loaded successfully in &b%dms", var6.lapFromStart()), Level.INFO);
            var7.logBenchmarks();
         } else {
            Log.log("&a[Content] Contents loaded successfully!");
         }
      }

      BossBarManager.clear();
   }

   private void method8129(boolean var1, boolean var2, long var3) {
      BossBarManager.setTitleKeepProgress("Calling content load events...");
      EventRegistrar.callEvent(new ItemsAdderFirstLoadEvent());
      if (!this.field57 && var3 == this.field59 && this.method8110()) {
         EventRegistrar.callEvent(new ItemsAdderLoadDataEvent(var1, var2));
         if (this.field57 || var3 != this.field59 || !this.method8110()) {
            throw new CancellationException("Content changed during load event");
         }
      } else {
         throw new CancellationException("Content changed during load event");
      }
   }

   private void method8130(long var1) {
      if (this.field57 || var1 != this.field59 || this.field54.get() == null) {
         throw new CancellationException("Content load was cancelled");
      }
   }

   public void method8131(@Nullable Plugin var1, CustomItemDefinition var2) {
      this.method8132(var1, var2, Collections.emptySet());
   }

   private void method8132(@Nullable Plugin var1, CustomItemDefinition var2, Set var3) {
      InjectedItemModifier var4 = null;

      try {
         List var5 = (List)aB.get(var2.getNamespacedID());
         if (var5 != null) {
            for (InjectedItemModifier var7 : var5) {
               if (!var3.contains(var7) && (var1 == null || var7.owner.equals(var1.getName()))) {
                  var4 = var7;
                  var7.call(var2.getNamespacedID(), var2);
                  if (this.field57) {
                     throw new CancellationException("Plugin disabled during item modifier");
                  }
               }
            }
         }

         for (InjectedItemModifier var11 : aC) {
            if (!var3.contains(var11) && (var1 == null || var11.owner.equals(var1.getName()))) {
               var4 = var11;
               var11.call(var2.getNamespacedID(), var2);
               if (this.field57) {
                  throw new CancellationException("Plugin disabled during item modifier");
               }
            }
         }
      } catch (CancellationException var8) {
         throw var8;
      } catch (Exception var9) {
         if (var4 != null) {
            if (var4.item != null) {
               Log.error("Error calling injected item modifier of plugin " + var4.owner + " for item " + var4.item);
            } else {
               Log.error("Error calling injected items modifier of plugin " + var4.owner);
            }
         } else {
            Log.error("Error calling injected item modifiers");
         }

         var9.printStackTrace();
      }
   }

   public boolean method8133(boolean var1, @Nullable Runnable var2, @Nullable Consumer var3, @Nullable DoubleCallback var4, @Nullable Player var5) {
      return this.method8136(var1, var2, var3, var4, var5, null);
   }

   public boolean method8134(boolean var1, @Nullable Runnable var2, @Nullable Consumer var3, @Nullable DoubleCallback var4, @Nullable Player var5) {
      return this.method8136(
         var1, var2, var3, var4, var5, Objects.requireNonNull((ResourcePackLoadState)this.field53.get(), "No reservation owned by this caller")
      );
   }

   public boolean method8135(
      boolean var1, @Nullable Runnable var2, @Nullable Consumer var3, @Nullable DoubleCallback var4, @Nullable Player var5, ResourcePackLoadState var6
   ) {
      return this.method8136(var1, var2, var3, var4, var5, Objects.requireNonNull(var6));
   }

   private boolean method8136(
      boolean var1,
      @Nullable Runnable var2,
      @Nullable Consumer var3,
      @Nullable DoubleCallback var4,
      @Nullable Player var5,
      @Nullable ResourcePackLoadState/* @Nullable */ var6
   ) {
      if (!Bukkit.isPrimaryThread()) {
         throw new IllegalStateException("Resource-pack preparation must run on the server thread");
      } else if (!this.method8110()) {
         throw new IllegalStateException("Content is not ready for resource-pack generation");
      } else {
         return this.aM.startGeneration(this.ag, this.field92, this.bx != null, this.aE.Yp, this.bA, var1, var2, var3, var4, var5, var6);
      }
   }

   public ResourcePackLoadState method8137() {
      if (!this.field57 && (this.aL == null || !this.aL.manager.hasPendingPacks())) {
         ResourcePackLoadState var1 = new ResourcePackLoadState(this);
         return this.field52.compareAndSet(null, var1) ? var1 : null;
      } else {
         return null;
      }
   }

   public ResourcePackLoadState method8138() {
      return (ResourcePackLoadState)this.field52.get();
   }

   @Deprecated
   public boolean method8139() {
      ResourcePackLoadState var1 = this.method8137();
      if (var1 == null) {
         return false;
      }

      this.field53.set(var1);
      return true;
   }

   public boolean method8140() {
      return this.field52.get() != null || this.aL != null && this.aL.manager.hasPendingPacks();
   }

   @Deprecated
   public void method8141() {
      ResourcePackLoadState var1 = (ResourcePackLoadState)this.field53.get();
      this.field53.remove();
      if (var1 != null) {
         var1.complete();
      }
   }

   public dev.lone.itemsadder.block.BlockInteractionListener method8142() {
      if (this.bx == null) {
         throw new RuntimeException("Crops are not available for this server version!");
      } else {
         return this.bx;
      }
   }

   @Nullable
   public BlockInteractionListener method8143() {
      return this.bx;
   }

   @Nullable
   private Map method8144(String var1) {
      if (!Bukkit.isPrimaryThread()) {
         ContentIndex var2 = this.field65;
         return var2 == null ? null : (Map)var2.bU.get(var1);
      } else {
         return !this.field58 && !this.field57 ? (Map)this.ai.get(var1) : null;
      }
   }

   @Nullable
   public Collection method8145(String var1) {
      Map var2 = this.method8144(var1);
      return var2 == null ? null : var2.values();
   }

   @Nullable
   public Set method8146(String var1) {
      Map var2 = this.method8144(var1);
      return var2 == null ? null : var2.keySet();
   }

   @Nullable
   public ItemActionBase method8147(String var1, String var2) {
      Map var3 = this.method8144(var1);
      if (var3 == null) {
         return null;
      }

      CustomItemDefinition var4 = (CustomItemDefinition)var3.get(var2);
      if (var4 != null) {
         return var4.aoh.getAction(var1);
      }

      if (!var2.contains(":")) {
         for (CustomItemDefinition var6 : var3.values()) {
            if (var6.aoj.equals(var2)) {
               return var6.aoh.getAction(var1);
            }
         }
      }

      return null;
   }

   @Nullable
   public CustomItemDefinition method8148(@Nullable ItemStack var1, boolean var2) {
      if (!CustomItemDefinition.isCustomItem(var1)) {
         if (var2) {
            Log.error("Warning, ITEM " + var1.getType() + " is not a custom item.");
         }

         return null;
      } else {
         String var3 = CustomItemDefinition.method8750(var1);
         if (!this.method8111()) {
            throw new RuntimeException("Items not finished loading, can't get item " + var3);
         }

         CustomItemDefinition var4 = (CustomItemDefinition)this.method8112().get(var3);
         if (var4 == null) {
            if (!var2) {
               return null;
            }

            Log.error("ERROR, ITEM " + var3 + " not found in configs! Maybe an old item?");
         }

         return var4;
      }
   }

   @Nullable
   public CustomItemDefinition method8149(ItemStack var1) {
      return this.method8148(var1, false);
   }

   @Nullable
   public CustomItemDefinition method8150(Class var1, ItemStack var2) {
      CustomItemDefinition var3 = this.method8148(var2, false);
      if (var3 == null) {
         return null;
      } else {
         return var1.isInstance(var3) ? var3 : null;
      }
   }

   @Nullable
   public CustomItemDefinition method8151(String var1, boolean var2) {
      if (!this.method8111()) {
         return null;
      }

      if (var1 == null) {
         if (var2) {
            Log.error("ERROR, ITEM is null. Maybe an old item?");
         }

         return null;
      } else {
         ContentIndex var3 = Bukkit.isPrimaryThread() ? null : this.field65;
         Map var4 = Bukkit.isPrimaryThread() ? this.field92 : (var3 == null ? Map.of() : var3.av());
         Map var5 = Bukkit.isPrimaryThread() ? this.field95 : (var3 == null ? Map.of() : var3.aw());
         CustomItemDefinition var6 = null;
         if (var1.contains(":")) {
            var6 = (CustomItemDefinition)var4.get(var1);
         } else {
            var6 = (CustomItemDefinition)var5.get(var1);
            if (var6 != null && Settings.get().isLoggingEnabled("logging.items.warnings")) {
               Log.log(ChatColor.YELLOW + "Guessed item namespace for item '" + var1 + "' -> " + var6.getNamespacedID());
            }
         }

         if (var6 == null) {
            if (!var2) {
               return null;
            }

            Log.error("ERROR, ITEM " + var1 + " not found in configs! Maybe an old item?");
         }

         return var6;
      }
   }

   @Nullable
   public CustomItemDefinition method8152(String var1, boolean var2) {
      if (!this.method8111()) {
         return null;
      }

      ContentIndex var3 = Bukkit.isPrimaryThread() ? null : this.field65;
      Map var4 = Bukkit.isPrimaryThread() ? this.field96 : (var3 == null ? Map.of() : var3.ax());
      Map var5 = Bukkit.isPrimaryThread() ? this.field97 : (var3 == null ? Map.of() : var3.ay());
      CustomItemDefinition var6 = null;
      if (var1.contains(":")) {
         var6 = (CustomItemDefinition)var4.get(var1);
      } else {
         var6 = (CustomItemDefinition)var5.get(var1);
         if (Settings.get().isLoggingEnabled("logging.items.warnings")) {
            Log.log(ChatColor.YELLOW + "Guessed item namespace for item '" + var1 + "' -> " + var6.getNamespacedID());
         }
      }

      if (var6 == null) {
         if (!var2) {
            return null;
         }

         Log.error("ERROR, VARIANT " + var1 + " not found in configs! Maybe an old item?");
      }

      return var6;
   }

   @Nullable
   public Object method8153(Class var1, String var2) {
      CustomItemDefinition var3 = this.method8154(var2);
      return var1.isInstance(var3) ? var3 : null;
   }

   @Nullable
   public CustomItemDefinition method8154(String var1) {
      return this.method8151(var1, false);
   }

   public boolean method8155(String var1) {
      return this.method8154(var1) != null;
   }

   public boolean isCustomItem(ItemStack itemStack) {
      return this.method8149(itemStack) != null;
   }

   public boolean method8156(ItemStack var1) {
      CustomItemDefinition var2 = this.method8149(var1);
      return var2 == null ? false : var2 instanceof CustomBlockDefinition;
   }

   public boolean method8157(CustomBlockDefinition var1) {
      return this.at.contains(var1);
   }

   public boolean method8158(String var1) {
      return var1.contains(":") ? this.field92.containsKey(var1) : this.field95.containsKey(var1);
   }

   @Nullable
   public CustomBlockDefinition method8159(ItemStack var1) {
      CustomItemDefinition var2 = this.method8149(var1);
      return !(var2 instanceof CustomBlockDefinition) ? null : (CustomBlockDefinition)var2;
   }

   @Nullable
   public CustomBlockDefinition method8160(String var1) {
      CustomItemDefinition var2 = this.method8154(var1);
      return !(var2 instanceof CustomBlockDefinition) ? null : (CustomBlockDefinition)var2;
   }

   public boolean method8161(Block var1, Consumer var2) {
      CustomBlockChunkStoreBase var3 = method8084().method8183(var1);
      if (!var3.isCustomBlock(var1)) {
         return false;
      }

      CustomBlockDefinition var4 = method8084().method8160(var3.getCustomBlockId(var1));
      if (var4 == null) {
         return false;
      }

      var2.accept(var4);
      return true;
   }

   public boolean hasBehaviour(ItemStack itemStack, String behaviour) {
      CustomItemDefinition var3 = this.method8149(itemStack);
      return var3 == null ? false : var3.aoh.hasAction(behaviour);
   }

   public boolean method8162() {
      if (!method8084().field98.isEmpty()) {
         for (Entry var2 : method8084().field98.entrySet()) {
            if (!((HashMap)var2.getValue()).isEmpty()) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean method8163() {
      return method8084().ag.containsKey(Material.TRIDENT);
   }

   @Nullable
   public CustomItemDefinition method8164(Material var1, int var2) {
      List var3 = (List)this.ag.get(var1);
      if (var3 == null) {
         return null;
      }

      for (CustomItemDefinition var5 : var3) {
         if (var5.nz() == var2) {
            return var5;
         }
      }

      return null;
   }

   @Deprecated
   public CustomBlockDefinition method8165(Material var1, HashMap var2) {
      for (CustomBlockDefinition var4 : this.al) {
         if (var1.equals(var4.anF.getMaterial()) && var2.equals(var4.anF.FA())) {
            return var4;
         }
      }

      return null;
   }

   @Nullable
   public static CustomBlockDefinition method8166(BlockData var0) {
      for (CustomBlockDefinition var2 : method8084().al) {
         if (var0.matches(var2.anF.getBlockData())) {
            return var2;
         }
      }

      return null;
   }

   public boolean method8167(ItemStack var1) {
      return this.method8149(var1) instanceof PlantItemDefinition;
   }

   public List method8168(String var1) {
      LinkedList var2 = new LinkedList();
      if (var1 != null && !var1.isEmpty() && !var1.equals(" ")) {
         var1 = method8169(var1);

         for (Entry var9 : this.field92.entrySet()) {
            CustomItemDefinition var10 = (CustomItemDefinition)var9.getValue();
            if (!(var10 instanceof CustomBlockDefinition var11 && var11.anF != null && var11.anF.Fx())) {
               String var12 = method8169((String)var9.getKey());
               if (var12.contains(var1)) {
                  var2.add((String)var9.getKey());
               }
            }
         }

         return var2;
      } else {
         for (Entry var4 : this.field92.entrySet()) {
            CustomItemDefinition var5 = (CustomItemDefinition)var4.getValue();
            if (!(var5 instanceof CustomBlockDefinition var6 && var6.anF != null && var6.anF.Fx())) {
               var2.add((String)var4.getKey());
            }
         }

         return var2;
      }
   }

   private static String method8169(String var0) {
      return var0.toLowerCase().replace(":", "").replace("_", "").replace("-", "");
   }

   public List method8170(String var1) {
      if (var1 != null && !var1.isEmpty() && !var1.equals(" ")) {
         ArrayList var2 = new ArrayList();

         for (String var4 : TreeDecorator.aDN.keySet()) {
            if (var4.contains(var1)) {
               var2.add(var4);
            }
         }

         return var2;
      } else {
         return new ArrayList(TreeDecorator.aDN.keySet());
      }
   }

   public List method8171(Material var1, String var2) {
      if (!this.ah.containsKey(var1)) {
         return new ArrayList();
      }

      if (var2 != null && !var2.isEmpty() && !var2.equals(" ")) {
         ArrayList var3 = new ArrayList();

         for (String var5 : ((HashMap)this.ah.get(var1)).keySet()) {
            if (var5.contains(var2)) {
               var3.add(var5);
            }
         }

         return var3;
      } else {
         return new ArrayList(((HashMap)this.ah.get(var1)).keySet());
      }
   }

   public List method8172(String var1) {
      if (var1 != null && !var1.isEmpty() && !var1.equals(" ")) {
         LinkedList var2 = new LinkedList();

         for (String var4 : this.aF.abn) {
            if (var4.contains(var1)) {
               var2.add(var4);
            }
         }

         return var2;
      } else {
         return this.aF.abn;
      }
   }

   @Nullable
   public ItemStack method8173(String var1) {
      if (var1 != null && this.method8155(var1)) {
         CustomItemDefinition var2 = this.method8154(var1);
         if (var2 == null) {
            return null;
         }

         ItemStack var3 = var2.clone();
         MmoItemsHook.injectMmoItem(var3);
         return var3;
      } else {
         return null;
      }
   }

   @Deprecated
   @Nullable
   public Object method8174(String var1, @Nullable String var2) {
      if (var1 != null) {
         Material var3 = BukkitRegistryLookup.material(var1);
         if (var3 != null) {
            return var3;
         }

         if (this.method8155(var1)) {
            return this.method8154(var1).Dr();
         }
      }

      if (var2 != null) {
         Log.warn("Can't find item '" + var1 + "' to load some settings. File '" + var2 + "'");
         return method8091();
      } else {
         return null;
      }
   }

   @Nullable
   public RecipeOutput method8175(String var1, @Nullable String var2) {
      if (var1 != null) {
         Material var3 = BukkitRegistryLookup.material(var1);
         if (var3 != null) {
            return new MaterialOutput(var3);
         }

         if (this.method8155(var1)) {
            return new CustomItemOutput(this.method8154(var1));
         }
      }

      if (var2 != null) {
         Log.warn("Can't find item '" + var1 + "' to load some settings. File '" + var2 + "'");
         return new MaterialOutput(method8091());
      } else {
         return null;
      }
   }

   public RecipeOutput method8176(String var1) {
      return this.method8175(var1, null);
   }

   @Nullable
   public ItemStack method8177(String var1, String var2) {
      if (var1 != null) {
         Material var3 = BukkitRegistryLookup.material(var1);
         if (var3 != null) {
            return new ItemStack(var3);
         }

         CustomItemDefinition var4 = this.method8154(var1);
         return var4 == null ? null : var4.Ds();
      } else if (var2 != null) {
         Log.warn("Can't find item '" + var1 + "' to load some settings. File '" + var2 + "'");
         return new ItemStack(method8091());
      } else {
         return null;
      }
   }

   @Nullable
   public ItemStack method8178(String var1) {
      if (var1 != null) {
         Material var2 = BukkitRegistryLookup.material(var1);
         if (var2 != null) {
            return new ItemStack(var2);
         }

         CustomItemDefinition var3 = this.method8154(var1);
         return var3 == null ? null : var3;
      } else {
         return null;
      }
   }

   @Nullable
   public ItemStack method8179(String var1) {
      return this.method8177(var1, null);
   }

   public static boolean method8180(ItemStack var0, ItemStack var1) {
      if (ItemStackUtils.aE(var0) && ItemStackUtils.aE(var1)) {
         return true;
      }

      if (var0 != null && var1 != null) {
         if (var0.getType() != var1.getType()) {
            return false;
         } else if (var0.hasItemMeta() != var1.hasItemMeta()) {
            return false;
         } else {
            return CustomItemDefinition.method8748(var0) && CustomItemDefinition.method8748(var1)
               ? CustomItemDefinition.method8780(var0, var1)
               : var0.isSimilar(var1);
         }
      } else {
         return false;
      }
   }

   public ItemStack method8181(Player var1, Material var2) {
      ItemStack var3 = var1.getInventory().getItemInMainHand();
      ItemStack var4 = var1.getInventory().getItemInOffHand();
      if (var3.getType() == var2) {
         if (CustomItemDefinition.isCustomItem(var3)) {
            return var3;
         }

         if (var4.getType() == var2 && CustomItemDefinition.isCustomItem(var4)) {
            return var4;
         }
      }

      return null;
   }

   public CustomBlockChunkStoreBase method8182(Chunk var1) {
      return this.aI.getRegionForChunk(var1);
   }

   public CustomBlockChunkStoreBase method8183(Block var1) {
      return this.aI.lookupRegionForBlock(var1);
   }

   public CustomBlockChunkStoreBase method8184(Location var1) {
      return this.aI.getRegionForLocation(var1);
   }

   public CustomBlockChunkStoreBase method8185(World var1, int var2, int var3) {
      return this.aI.getOrLoadRegion(var1, var2, var3);
   }

   public static boolean isCustomBlock(Block block) {
      return field67.method8183(block).isCustomBlock(block);
   }

   @Nullable
   public static String method8186(Block var0) {
      return field67.method8183(var0).getCustomBlockId(var0);
   }

   @Nullable
   public static CustomBlockDefinition method8187(Block var0) {
      String var1 = method8186(var0);
      return var1 == null ? null : method8084().method8160(var1);
   }

   public static boolean method8188(Block var0) {
      CustomBlockChunkStoreBase var1 = field67.method8183(var0);
      return var1.getCropSeedId(var0) != null;
   }

   public static boolean method8189(Location var0) {
      return BlockDigListener.BR().method1282(var0);
   }

   public static void method8190() {
      if (Settings.get().registryChangeRejoin) {
         if (!Bukkit.getServer().isAcceptingTransfers()) {
            Log.warn("Temporarily enabling 'accepts-transfers' to automatically re-join players after content refresh.");
         }

         Bukkit.getOnlinePlayers()
            .forEach(var0 -> NativePacketsHolder.PD().sendAnimation(var0, Settings.get().registryRejoinAddress, Settings.get().registryRejoinPort));
      } else if (Settings.get().registryChangeKick) {
         Bukkit.getOnlinePlayers().forEach(var0 -> {
            if (ServerCompat.isPaper()) {
               var0.kick(Main.aFi.method7070("server-content-changed-join-again"));
            } else {
               var0.kickPlayer(AdventureText.toPlainText(Main.aFi.method7070("server-content-changed-join-again")));
            }
         });
      }
   }

   public static String method8191(List var0, int var1, String var2) {
      StringBuilder var3 = new StringBuilder();
      int var4 = 0;

      for (int var5 = var0.size(); var4 < var5; var4++) {
         NamespacedIdentity var6 = (NamespacedIdentity)var0.get(var4);
         if (var4 > var1) {
            var3.setLength(var3.length() - 2);
            var3.append(var2);
            return var3.toString();
         }

         var3.append(var6.getNamespacedID()).append(", ");
      }

      return var3.toString();
   }

   public boolean method8192() {
      for (Entry var2 : this.field92.entrySet()) {
         CustomItemDefinition var3 = (CustomItemDefinition)var2.getValue();
         if (var3.Dy() && var3.DM().isVisible()) {
            return true;
         }
      }

      return false;
   }

   @Nullable
   public static File method8193(String var0) {
      return FileListHelper.findFirstExisting(field67.field88, var0);
   }

   public static String method8194(@Nullable String var0, String var1) {
      return AssetPathResolver.textureAssetPath(var0, var1);
   }

   public static String method8195(@Nullable String var0, String var1) {
      return AssetPathResolver.modelAssetPath(var0, var1);
   }

   public static String method8196(int var0, @Nullable String var1, String var2) {
      return AssetPathResolver.humanoidEquipmentTexturePath(var0, var1, var2);
   }

   @Nullable
   public Function method8197() {
      List var1 = List.copyOf(this.field87);
      List var2 = List.copyOf(this.field84);
      return var2x -> FileListHelper.findAssetFile(var1, var2, var2x);
   }

   public static File method8198(String var0) {
      return FileListHelper.findAssetFile(field67.field87, field67.field84, var0);
   }

   @Nullable
   public static File method8199(String var0) {
      String var1 = method8194(null, var0);
      if (var1 == null) {
         return null;
      }

      var1 = Utils.normalizeSlashes(var1);
      return method8198(var1);
   }

   @Nullable
   public static File method8200(String var0) {
      String var1 = method8195(null, var0);
      if (var1 == null) {
         return null;
      }

      var1 = Utils.normalizeSlashes(var1);
      return method8198(var1);
   }

   public static List method8201(String var0) {
      return FileListHelper.findAllAssetFiles(field67.field87, field67.field84, var0);
   }

   public static List method8202(File var0) {
      return FileListHelper.expandContentFolder(var0);
   }

   public static List method8203() {
      return FileListHelper.expandAllFolders(method8084().field87);
   }

   public static List method8204() {
      return FileListHelper.collectModelJsonFiles(method8084().field87);
   }

   private void method8205(boolean var1) {
      this.field84 = FileListHelper.listContentFolders(field73, field77);
      this.field87 = FileListHelper.collectAssetRoots(field73, field77, field76);
      this.field88 = FileListHelper.collectResourcePackDirs(field73, field77, field75);
      this.field89 = FileListHelper.eitherExists(field77, field76);
      if (var1 && this.field89) {
         Log.warn("Detected legacy ItemsAdder pack (pre 3.3.0). Loading anyway. Info: https://a.devs.beer/ia-new-guidelines-330");
      }
   }

   public int method8206(int var1) {
      for (Entry var3 : this.field98.entrySet()) {
         for (Entry var5 : ((HashMap)var3.getValue()).entrySet()) {
            if (((CustomItemDefinition)var5.getValue()).nz() == var1) {
               return (Integer)var5.getKey();
            }
         }
      }

      throw new IllegalArgumentException("Armor color not found for custom model data " + var1);
   }

   public CustomItemDataEntry method8207(NamespacedKey var1) {
      return (CustomItemDataEntry)this.aw.get(var1);
   }

   public void method8208(CustomItemDefinition var1) {
      this.field94.remove(var1.aol);
      this.field92.remove(var1.getNamespacedID());
      this.field95.remove(var1.getId());
      ((HashMap)this.ah.get(var1.getType())).remove(var1.getNamespacedID());
      if (((HashMap)this.ah.get(var1.getType())).isEmpty()) {
         this.ah.remove(var1.getType());
      }

      this.field96.remove(var1.getNamespacedID());
      this.field97.remove(var1.getId());
   }

   @Generated
   public Map method8209() {
      return this.ac;
   }

   @Generated
   public void method8210(Map var1) {
      this.ac = var1;
   }

   @Generated
   public Map ab() {
      return this.aw;
   }

   @Generated
   public List ac() {
      return this.ax;
   }

   @Generated
   public ChunkDataStore ad() {
      return this.aI;
   }
}
