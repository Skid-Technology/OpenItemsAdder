package dev.lone.itemsadder;

import beer.devs.fastnbt.nms.Version;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jeff_media.armorequipevent.ArmorEquipEvent;
import dev.lone.itemsadder.Core.OtherPlugins.AdvancedSlimePaperWrapper;
import dev.lone.itemsadder.Core.OtherPlugins.Glow;
import dev.lone.itemsadder.Core.OtherPlugins.WorldEditIntegration;
import dev.lone.itemsadder.Core.OtherPlugins.Citizens.CitizensHook;
import dev.lone.itemsadder.Core.OtherPlugins.CustomCrafting.CustomCraftingWrapper;
import dev.lone.itemsadder.Core.OtherPlugins.Denizen.DenizenHook;
import dev.lone.itemsadder.Core.OtherPlugins.Mimic.MimicRegistryManager;
import dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.MythicMobsHook;
import dev.lone.itemsadder.Core.OtherPlugins.PAPI.PAPI_FontImages;
import dev.lone.itemsadder.Core.OtherPlugins.PAPI.PAPI_PlayerStats;
import dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins.ProtectionPlugins;
import dev.lone.itemsadder.Core.OtherPlugins.vulcan.VulcanHook;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import dev.lone.itemsadder.api.Skript.SkriptManager;
import dev.lone.itemsadder.block.CustomBlockListener;
import dev.lone.itemsadder.block.CustomBlockVariant;
import dev.lone.itemsadder.command.CleanCacheCommand;
import dev.lone.itemsadder.command.ColorCommand;
import dev.lone.itemsadder.command.CropsCommand;
import dev.lone.itemsadder.command.CustomModelDataCommand;
import dev.lone.itemsadder.command.DisguiseCommand;
import dev.lone.itemsadder.command.DropCommand;
import dev.lone.itemsadder.command.DurabilityCommand;
import dev.lone.itemsadder.command.EmoteCommand;
import dev.lone.itemsadder.command.EntityCommand;
import dev.lone.itemsadder.command.GetCommand;
import dev.lone.itemsadder.command.GiveCommand;
import dev.lone.itemsadder.command.HitboxCommand;
import dev.lone.itemsadder.command.HudCommand;
import dev.lone.itemsadder.command.IaBlockCommand;
import dev.lone.itemsadder.command.IaCommand;
import dev.lone.itemsadder.command.IaTabCompleter;
import dev.lone.itemsadder.command.ImageCommand;
import dev.lone.itemsadder.command.InfoCommand;
import dev.lone.itemsadder.command.ItemCommandTabCompleter;
import dev.lone.itemsadder.command.LiquidCommand;
import dev.lone.itemsadder.command.ParticleCommand;
import dev.lone.itemsadder.command.PlaySoundCommand;
import dev.lone.itemsadder.command.PlayerStatCommand;
import dev.lone.itemsadder.command.RecipeCommand;
import dev.lone.itemsadder.command.RecipeLookupCommand;
import dev.lone.itemsadder.command.ReloadCommand;
import dev.lone.itemsadder.command.RemoveCommand;
import dev.lone.itemsadder.command.RenameCommand;
import dev.lone.itemsadder.command.RepairCommand;
import dev.lone.itemsadder.command.Sha1Command;
import dev.lone.itemsadder.command.SpawnTreeCommand;
import dev.lone.itemsadder.command.TagCommand;
import dev.lone.itemsadder.command.TextureCommand;
import dev.lone.itemsadder.command.TotemAnimationCommand;
import dev.lone.itemsadder.command.UndisguiseCommand;
import dev.lone.itemsadder.command.ZipCommand;
import dev.lone.itemsadder.config.LocalizedYamlConfig;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.config.YamlFileSnapshot;
import dev.lone.itemsadder.config.YamlFileWriter;
import dev.lone.itemsadder.config.YamlNodeMerger;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.SettingsListener;
import dev.lone.itemsadder.core.StartupChecks;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.DisguiseResolver;
import dev.lone.itemsadder.entity.EntityEventTracker;
import dev.lone.itemsadder.entity.RotationPolicy;
import dev.lone.itemsadder.event.AbstractPlayerTrigger;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.gui.GuiEventGuard;
import dev.lone.itemsadder.item.BeaconSlotListener;
import dev.lone.itemsadder.item.CommandPreprocessListener;
import dev.lone.itemsadder.item.MerchantClickListener;
import dev.lone.itemsadder.item.StonecutterSlotListener;
import dev.lone.itemsadder.item.effect.ArrowDisplayEffect;
import dev.lone.itemsadder.metrics.MetricsManager;
import dev.lone.itemsadder.network.EntityMetadataListener;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.network.SignInputListener;
import dev.lone.itemsadder.network.TradeSelectionListener;
import dev.lone.itemsadder.network.ViaVersionIntegration;
import dev.lone.itemsadder.recipe.RecipeManagerHolder;
import dev.lone.itemsadder.resourcepack.PackBuildWorker;
import dev.lone.itemsadder.resourcepack.PackSignaturePayload;
import dev.lone.itemsadder.resourcepack.PackSyncTaskFactory;
import dev.lone.itemsadder.resourcepack.VanillaAssetManager;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.BossBarManager;
import dev.lone.itemsadder.util.CancellableEvent;
import dev.lone.itemsadder.util.ColorParserListener;
import dev.lone.itemsadder.util.ConstantsHolder;
import dev.lone.itemsadder.util.EnvironmentDetector;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.LocationSink;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NativePacketsLoader;
import dev.lone.itemsadder.util.PluginLibraryManager;
import dev.lone.itemsadder.util.PluginResourceIndex;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.ServerCompat;
import dev.lone.itemsadder.util.Utils;
import dev.lone.itemsadder.world.TreeFeatureHookHolder;
import dev.lone.itemsadder.worldgen.WorldGenerationManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Main extends JavaPlugin implements IaListener {
   public static String field28;
   public static final String SPIGOT_RESOURCE_ID = "1489280531320537218";
   public static String aEF;
   public static String downloadUsername;
   public static String aEH;
   private static boolean aEI;
   public static final String BASE_PACKAGE = "dev.lone.itemsadder";
   private static volatile Main INSTANCE;
   private static volatile Plugin pluginInstance;
   public static YamlConfiguration pluginYaml;
   private ContentManager contentManager;
   private boolean aEL;
   private VanillaAssetManager aEM;
   @Nullable
   public static LocationSink aEN;
   public boolean aEO;
   public static boolean aEP;
   public static boolean aEQ;
   public static boolean aER;
   public static boolean aES;
   public static boolean aET;
   public static boolean aEU;
   public static boolean aEV;
   public static boolean aEW;
   public static boolean aEX;
   public static boolean aEY;
   public static boolean aEZ;
   public static boolean aFa;
   public static boolean aFb;
   public static boolean aFc;
   public static boolean viaVersionPresent;
   public static boolean aFe;
   public static boolean aFf;
   public static boolean aFg;
   public static boolean aFh;
   public static volatile YamlConfig config;
   public static volatile LocalizedYamlConfig aFi;
   public SignInputListener aFj;
   private PAPI_FontImages papiFontImages;
   private PAPI_PlayerStats papiPlayerStats;
   private WorldEditIntegration worldEditIntegration;
   private final Set aFn = new LinkedHashSet();
   public boolean aFo = false;
   List aFp = new ArrayList();
   private final CompletableFuture aFq = new CompletableFuture();

   public static Main getInstance() {
      return INSTANCE;
   }

   public static Plugin getPlugin() {
      return INSTANCE;
   }

   public static PluginCommand getCommand(String var0) {
      return getInstance().getCommand(var0);
   }

   public static boolean isUsernamePlaceholderUnreplaced() {
      return downloadUsername.equals(new String(new byte[]{37, 37, 95, 95, 85, 83, 69, 82, 78, 65, 77, 69, 95, 95, 37, 37}));
   }

   public void onLoad() {
      INSTANCE = this;
      pluginInstance = this;
      if (NativePacketsLoader.isCompatible(this)) {
         this.aEL = true;
         pluginYaml = loadPluginYaml(getInstance().getFile());

         try {
            PluginLibraryManager var1 = new PluginLibraryManager(this);
            var1.loadFromConfig();
            Set var2;
            if (!ServerCompat.isPaper()) {
               var2 = Set.of(
                  "net.kyori:adventure-api:4.23.0",
                  "net.kyori:adventure-text-minimessage:4.23.0",
                  "net.kyori:adventure-text-serializer-gson:4.23.0",
                  "net.kyori:adventure-text-serializer-gson-legacy-impl:4.23.0",
                  "net.kyori:adventure-platform-bukkit:4.4.1"
               );
            } else if (Version.isOlderThan(Version.v26_1_2)) {
               var2 = Set.of(
                  "net.kyori:adventure-text-serializer-gson:4.23.0",
                  "net.kyori:adventure-text-serializer-gson-legacy-impl:4.23.0",
                  "net.kyori:adventure-platform-bukkit:4.4.1"
               );
            } else {
               var2 = Set.of(
                  "net.kyori:adventure-text-serializer-gson:5.1.1",
                  "net.kyori:adventure-text-serializer-gson-legacy-impl:4.26.1",
                  "net.kyori:adventure-platform-bukkit:4.4.1"
               );
            }

            var1.loadLibraries(var2);

            try {
               var1.loadJoltNative();
            } catch (Exception | LinkageError var4) {
               this.getLogger().log(Level.WARNING, "Jolt JNI unavailable; death ragdolls are disabled.", var4);
            }
         } catch (Exception | LinkageError var5) {
            this.getLogger().log(Level.SEVERE, "Failed to load libraries.", var5);
            stop();
            return;
         }

         ProtectionPlugins.register();
      }
   }

   public void onEnable() {
      if (NativePacketsLoader.ensureAvailable(this)) {
         if (!this.aEL) {
            this.getLogger().severe("NativePackets became available after ItemsAdder onLoad; restart required.");
            this.getServer().getPluginManager().disablePlugin(this);
         } else if (EnvironmentDetector.checkServerSupport()) {
            NativePacketsHolder.init();
            ServerCompat.init();
            TreeFeatureHookHolder.getHook().injectDefaultBlockstateFeatures();
            aEI = true;
            if (StartupChecks.method7398(this, aEI)) {
               this.Le();
               Log.printBanner();
               Runnable var1 = ServerCompat.isPaper()
                  ? PackSyncTaskFactory.method6928(
                     this,
                     this.getFile().toPath(),
                     new PackSignaturePayload(
                        aEI ? "discord-beta" : (isUsernamePlaceholderUnreplaced() ? "spigotmc" : (aEH.contains("POLYMART") ? "builtbybit" : "polymart")),
                        !aEI && field28.matches("[1-9][0-9]{0,19}") ? field28 : null,
                        aEI ? "public-beta" : "marketplace-placeholders",
                        this.getServer().getIp(),
                        this.getDescription().getVersion(),
                        this.getServer().getBukkitVersion()
                     )
                  )
                  : () -> {};
               Bukkit.getScheduler().runTaskAsynchronously(this, var1);

               Bukkit.getPluginManager().registerEvents(new ColorParserListener(pluginInstance), pluginInstance);
               AdventureText.initAudiences(this);
               if (config.getBoolean("update_checker")) {
                  new PluginResourceIndex(this, 1851, this.getDescription().getVersion()).checkForUpdates(var1x -> {
                     Log.log(ChatColor.GREEN + " ");
                     Log.log("" + ChatColor.GOLD + ChatColor.YELLOW + "\ud83d\ude80 New version available!");
                     Log.log("" + ChatColor.GOLD + ChatColor.YELLOW + "- Released on: " + var1x.Lk());
                     Log.log("" + ChatColor.GOLD + ChatColor.YELLOW + "- Version: " + var1x.version());
                     if (var1x.Ln() > 0) {
                        Log.log("" + ChatColor.GOLD + ChatColor.YELLOW + "- You are " + var1x.Ln() + " versions behind.");
                     }

                     Log.log(ChatColor.GREEN + " ");
                     this.aFo = true;
                  }, var0 -> {});
               }

               if (Lc()) {
                  Log.error(" ");
                  Log.error(ChatColor.RED + "YOU RELOADED THE PLUGIN/SERVER, this may cause bugs.");
                  Log.error(ChatColor.DARK_GRAY + "Next time use &b/iareload" + ChatColor.DARK_GRAY + " to reload configurations.");
                  Log.error(ChatColor.DARK_GRAY + "&bRestart the server" + ChatColor.DARK_GRAY + " if you want to update the plugin jar.\n\n");
               }

               System.setProperty("ItemsAdderLoaded", "yeaboi");
               this.aEO = isPluginEnabled("ItemsAdderBlocksInjector");
               aFh = isPluginEnabled("MythicMobs");
               if (aFh
                  && (
                     !ServerCompat.classExists("io.lumine.mythic.api.skills.ISkillMechanic")
                        || !ServerCompat.classExists("io.lumine.mythic.bukkit.adapters.item.ItemComponentBukkitItemStack")
                        || !ServerCompat.isAbstractClass("io.lumine.mythic.bukkit.adapters.BukkitItemStack")
                  )) {
                  aFh = false;
                  Log.error("Update MythicMobs, you are running an old version.");
               }

               aEQ = isPluginEnabled("Mimic");
               aEP = isPluginEnabled("RPGInventory");
               aER = isPluginEnabled("PlaceholderAPI");
               aES = isPluginEnabled("Vault");
               aET = isPluginEnabled("PermissionsEx");
               aEU = isPluginEnabled("MMOItems");
               aEV = isPluginEnabled("HyperStones");
               aEW = isPluginEnabled("CustomCrafting");
               if (aEW) {
                  CustomCraftingWrapper.init(Log::error);
               }

               aEX = isPluginEnabled("CraftEnhance");
               if (Version.isOlderThan(Version.v26_1_2)) {
                  Glow.init(this, BlockFaceUtils::playersInView, Log::error);
               }

               aEZ = isPluginEnabled("Orebfuscator");
               aFa = isPluginEnabled("ExecutableItems");
               aFb = isPluginEnabled("AdvancedEnchantments");
               aFc = el("Citizens");
               viaVersionPresent = isPluginEnabled("ViaVersion");
               if (viaVersionPresent) {
                  Log.warn("");
                  Log.warn("ViaVersion detected.");
                  Log.warn("Joining via different game versions can cause issues and some are not fixable due to game limitations.");
                  Log.warn("Make sure that you are aware of these limitations before reporting issues.");
                  Log.warn("Info: https://a.devs.beer/ia-via-load");
                  Log.warn("");
                  ViaVersionIntegration.register();
               }

               aFe = isPluginEnabled("ModelEngine");
               aFf = isPluginEnabled("Denizen");
               aFg = isPluginEnabled("EpicBackpacks");
               aEY = isPluginEnabled("Slimefun");
               aEN = LocationSink.create();
               if (isPluginEnabled("Skript")) {
                  new SkriptManager().init(this);
               }

               if (aFh) {
                  MythicMobsHook.inst().register(this);
               }

               if (aFf) {
                  DenizenHook.hook();
               }

               this.method517(Bukkit.getPluginManager().getPlugin("ItemsAdder-WorldEdit"));
               this.method517(Bukkit.getPluginManager().getPlugin("IA-Edit"));
               if (el("WorldEdit") || el("FastAsyncWorldEdit")) {
                  this.worldEditIntegration = WorldEditIntegration.register(this, el("FastAsyncWorldEdit"));
               }

               if (isPluginEnabled("Vulcan")) {
                  VulcanHook.register(pluginInstance, var0 -> ContentManager.method8084().aL.downloadGate.isLocked(var0), Log::error);
               }

               AdvancedSlimePaperWrapper.init();
               if (isPluginEnabled("Citizens")) {
                  aFc = true;

                  try {
                     if (!CitizensHook.isRegistered()) {
                        CitizensHook.register();
                     }
                  } catch (Throwable var12) {
                     Log.error("Failed to hook into Citizens. Please update make sure you're running a compatible version.", var12);
                  }
               }

               if (isPluginEnabled("DirectionalBlock")) {
                  Log.warn("DirectionalBlocks plugin detected. The plugin is unofficial.");
                  Log.warn("You should instead use the directional blocks feature of ItemsAdder.");
                  Log.warn("Read more: http://a.devs.beer/ia-directional-blocks");
               }

               try {
                  GuiEventGuard.register(this);
               } catch (IllegalStateException var11) {
               }

               CustomBlockListener.method713(this);
               Bukkit.getPluginManager().registerEvents(this, this);

               try {
                  this.aEM = new VanillaAssetManager(
                     new File(getInstance().getDataFolder(), "storage" + File.separator + "cache" + File.separator + "vanilla_assets").toPath(),
                     this::method508
                  );
                  this.aEM.prepare(true);
               } catch (Exception var10) {
                  Log.error("Failed to download or load vanilla assets. Please check your internet connection or the plugin configuration.", var10);
                  this.aFq.completeExceptionally(var10);
                  stop();
                  return;
               }

               new SettingsListener().register();
               CancellableEvent var2 = new CancellableEvent();
               Bukkit.getServer().getPluginManager().callEvent(var2);
               if (var2.isCancelled()) {
                  this.aFq.thenRun(() -> {
                     if (this.isEnabled()) {
                        Bukkit.getScheduler().runTaskLater(this, () -> {
                           Random var0 = new Random();
                           Material[] var1x = Material.values();
                           ContentManager.method8084().field92.forEach((var2xx, var3x) -> {
                              var3x.setType(var1x[var0.nextInt(var1x.length - 1)]);
                              var3x.DC();
                           });
                           ContentManager.method8084().bp.method2221(false);
                           ContentManager.method8084().bB.aJX.LO().clear();
                           ContentManager.method8084().method8094(false);
                           SettingsListener.method7609();
                           ArrayList var2x = new ArrayList();
                           Bukkit.recipeIterator().forEachRemaining(var1xx -> {
                              if (var1xx instanceof Keyed var2xx) {
                                 if (!var2xx.getKey().getNamespace().equals("minecraft")) {
                                    return;
                                 }

                                 var2x.add(var2xx);
                              }
                           });

                           for (Keyed var4x : var2x) {
                              Bukkit.removeRecipe(var4x.getKey());
                           }
                        }, Utils.randomInt(15, 32) * 20L);
                     }
                  });
               } else {
                  if (config.getBoolean("recipes.remove_vanilla_recipes.enabled")) {
                     boolean var3 = Settings.get().isLoggingEnabled("logging.recipes.removed_vanilla");
                     boolean var4 = false;

                     for (String var6 : config.getStringList("recipes.remove_vanilla_recipes.list")) {
                        if (config.getBoolean("recipes.remove_vanilla_recipes.list." + var6)) {
                           for (Recipe var8 : Bukkit.getRecipesFor(new ItemStack(Material.valueOf(var6)))) {
                              if (var8 instanceof Keyed var9 && var9.getKey().getNamespace().equals("minecraft")) {
                                 RecipeManagerHolder.Qb().method651(var9.getKey());
                                 var4 = true;
                                 if (var3) {
                                    Log.warn("Removed recipe " + var9.getKey());
                                 }
                              }
                           }
                        }
                     }

                     if (var4) {
                        RecipeManagerHolder.Qb().PY();
                     }
                  }

                  AbstractPlayerTrigger.register();
               }
            }
         }
      }
   }

   public static void stop() {
      Bukkit.getPluginManager().disablePlugin(getInstance());
      Bukkit.getServer().shutdown();
   }

   private void method508(VanillaAssetManager var1) {
      if (this.isEnabled()) {
         if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(this, () -> this.method508(var1));
         } else {
            this.contentManager = ContentManager.method8085(this, var1);
            this.contentManager.method8116().whenComplete((var1x, var2x) -> {
               if (var2x == null) {
                  this.aFq.complete(null);
               } else {
                  this.aFq.completeExceptionally(var2x);
               }
            });
            this.Lb();
            List var2 = pluginYaml.getStringList("cyclicdepend");
            if (!var2.isEmpty()) {
               for (String var4 : var2) {
                  if (!var4.equals("AdvancedEnchantments") || Settings.get().aeCustomEnchantsInItemConfigs) {
                     Plugin var5 = Bukkit.getPluginManager().getPlugin(var4);
                     if (var5 != null && !var5.isEnabled()) {
                        this.aFp.add(var4);
                        break;
                     }
                  }
               }
            }

            if (!this.aFp.isEmpty()) {
               Log.log(
                  ChatColor.YELLOW
                     + "[!] Waiting for Cyclic dependencies to load before loading ItemsAdder contents: "
                     + ChatColor.AQUA
                     + String.join(", ", this.aFp)
               );
            } else {
               this.bq(true);
            }
         }
      }
   }

   private void bq(boolean var1) {
      if (!var1 || aFb && Settings.get().aeCustomEnchantsInItemConfigs) {
         SchedulerUtils.runLaterSync(() -> {
            if (this.isEnabled()) {
               this.contentManager.method8079();
            }
         }, 1L);
      } else {
         this.contentManager.method8080(true);
      }
   }

   private void Lb() {
      this.Ld();
      new CommandPreprocessListener().register(this);
      new MerchantClickListener().register(this);
      EventRegistrar.registerIfAbsent(new StonecutterSlotListener(), this);
      EventRegistrar.registerIfAbsent(new BeaconSlotListener(), this);
      this.aFj = new SignInputListener(this);
      ArmorEquipEvent.registerListener(this);
   }

   public static boolean Lc() {
      return System.getProperties().containsKey("ItemsAdderLoaded");
   }

   private void Ld() {
      new TextureCommand();
      new ZipCommand();
      new IaCommand();
      new GetCommand();
      new RemoveCommand();
      new GiveCommand();
      new DropCommand();
      new RepairCommand();
      new RenameCommand();
      new DurabilityCommand();
      new IaBlockCommand();
      new RecipeCommand();
      new LiquidCommand();
      new PlayerStatCommand();
      new ImageCommand();
      new TagCommand();
      new CustomModelDataCommand();
      new HudCommand();
      new ReloadCommand();
      new RecipeLookupCommand();
      new InfoCommand();
      new SpawnTreeCommand();
      new TotemAnimationCommand();
      new PlaySoundCommand();
      new ParticleCommand();
      new CleanCacheCommand();
      new Sha1Command();
      new HitboxCommand();
      new DisguiseResolver();
      new EmoteCommand();
      new ColorCommand();
      new EntityCommand();
      new ItemCommandTabCompleter();
      new DisguiseCommand();
      new UndisguiseCommand();
      new CropsCommand();
      new IaTabCompleter();
   }

   public void Le() {
      try (InputStream var1 = this.getResource("config.yml")) {
         if (var1 == null) {
            throw new IOException("Missing bundled config.yml");
         }

         YamlFileSnapshot var2 = YamlNodeMerger.method7526(
            this.getDataFolder().toPath().resolve("config.yml"), new String(var1.readAllBytes(), StandardCharsets.UTF_8)
         );
         YamlConfiguration var3 = new YamlConfiguration();
         var3.options().parseComments(true);
         var3.loadFromString(var2.IC());
         YamlConfig var4 = YamlConfig.method7035(this, "config.yml", var3);
         LocalizedYamlConfig var5 = new LocalizedYamlConfig(this, var4.getString("config_files.lang"), true);
         if (!var5.isLoaded()) {
            throw new IllegalArgumentException("Cannot load the selected language file");
         }

         Settings var6 = Settings.load(var4);
         var2.abY();

         try {
            YamlFileWriter.method7539(this.getDataFolder().toPath().resolve("storage/cache/plugin-version.properties"), this.getVersion());
         } catch (IOException | IllegalArgumentException var9) {
            Log.warn("Could not update plugin version cache: " + var9.getMessage());
         }

         config = var4;
         aFi = var5;
         Settings.set(var6);
         var2.adb().forEach(Log::warn);
         if (var2.ada() != null) {
            Log.log("Updated config.yml. Backup: " + var2.ada().getFileName());
         }
      } catch (Exception var11) {
         throw new IllegalStateException("Cannot load config.yml: " + var11.getMessage(), var11);
      }

      ConstantsHolder.init();
      if (ServerCompat.isDevMode()) {
         Set.of("cz", "de", "en", "es", "fr", "it", "jp", "ko", "nl", "pl", "pt", "ru", "th", "tr", "zh_cn").forEach(var1x -> {
            LocalizedYamlConfig var2x = new LocalizedYamlConfig(this, var1x, true);
            if (var2x.abI()) {
               Log.devLog("Updating " + var1x + " file...");
            }
         });
         Log.devLog("Iterated lang files to update missing properties.");
      }

      if (!config.getBoolean("update_checker")) {
         this.aFo = false;
      }

      Log.setPrefixComponent(ServerCompat.isCoreMissing() ? aFi.method7070("prefix") : AdventureText.fromLegacy("<rw [DEV]> ").append(aFi.method7070("prefix")));
      Log.setPrefix(
         (ServerCompat.isCoreMissing() ? "" : net.md_5.bungee.api.ChatColor.YELLOW + "!!DEV> " + net.md_5.bungee.api.ChatColor.RESET)
            + AdventureText.toPlainText(aFi.method7070("prefix"))
      );
   }

   @EventHandler
   private void method509(PluginEnableEvent var1) {
      if (var1.getPlugin().getName().equals("ItemsAdder-WorldEdit") || var1.getPlugin().getName().equals("IA-Edit")) {
         this.method517(var1.getPlugin());
      }

      switch (var1.getPlugin().getName()) {
         case "CustomCrafting":
            aEW = true;
            CustomCraftingWrapper.init(Log::error);
            break;
         case "CraftEnhance":
            aEX = true;
            break;
         case "Slimefun":
            aEY = true;
            break;
         case "HyperStones":
            aEV = true;
      }

      if (!this.aFp.isEmpty() && this.aFp.contains(var1.getPlugin().getName())) {
         this.aFp.remove(var1.getPlugin().getName());
         Log.log(ChatColor.AQUA + "[OK] Cyclic dependency has loaded: " + ChatColor.AQUA + var1.getPlugin().getName());
         if (this.aFp.isEmpty()) {
            Log.log(ChatColor.GREEN + "[OK] All Cyclic dependencies loaded, now loading ItemsAdder contents...");
            this.bq(false);
         }
      }
   }

   @EventHandler
   private void method510(PluginDisableEvent var1) {
      if (!var1.getPlugin().getName().equals(this.getName())) {
         if (this.contentManager != null) {
            this.contentManager.method8100(var1.getPlugin());
         }
      }
   }

   public void onDisable() {
      if (this.aEM != null) {
         this.aEM.close();
      }

      this.aFq.cancel(false);
      EntityEventTracker.uq();
      if (this.contentManager != null) {
         this.contentManager.method8117();
      }

      if (!this.aEL) {
         INSTANCE = null;
      } else {
         TreeFeatureHookHolder.restoreDefaultBlockstateFeatures();
         BossBarManager.disable();
         BossBarManager.clear();
         if (aFc) {
            CitizensHook.unregister();
         }

         if (aFh) {
            MythicMobsHook.inst().unregister();
         }

         if (aFf) {
            DenizenHook.unhook();
         }

         if (this.worldEditIntegration != null) {
            this.worldEditIntegration.unregister();
         }

         CompletionStage var1 = CompletableFuture.completedFuture(null);
         if (CustomEntityManager.isInitialized()) {
            var1 = CustomEntityManager.getInstance().shutdownAsync();
         }

         if (this.aFj != null) {
            this.aFj.close();
            this.aFj = null;
         }

         TradeSelectionListener.unregister();
         if (this.contentManager != null) {
            try {
               this.contentManager.method8095(false, false);
            } catch (RuntimeException var4) {
               Log.error("Could not save all content data while disabling ItemsAdder.", var4);
            }

            this.contentManager.method8097(false);
            this.contentManager.method8099();
            EntityMetadataListener.aM();
            WorldGenerationManager.GJ();
         }

         CustomBlockVariant.Co();
         RecipeManagerHolder.unregister();
         AdventureText.closeAudiences();
         AbstractPlayerTrigger.aM();
         ArrowDisplayEffect.es();
         if (NativePacketsHolder.aN()) {
            var1 = NativePacketsHolder.method5248(var1);
         }

         try {
            method511(var1);
         } catch (RuntimeException var3) {
            Log.error("Could not complete terminal entity cleanup while disabling ItemsAdder.", var3);
         }

         MetricsManager.br();
         PackBuildWorker.dispose();
         INSTANCE = null;
      }
   }

   static void method511(CompletionStage var0) {
      var0.toCompletableFuture().join();
   }

   @EventHandler
   private void method512(ItemsAdderLoadDataEvent var1) {
      if (aER) {
         try {
            if (this.papiFontImages != null) {
               this.papiFontImages.unregister();
            }

            if (this.papiPlayerStats != null) {
               this.papiPlayerStats.unregister();
            }
         } catch (Throwable var3) {
         }

         this.papiFontImages = new PAPI_FontImages();
         this.papiFontImages.register();
         this.papiPlayerStats = new PAPI_PlayerStats();
         this.papiPlayerStats.register();
      }

      if (aEQ) {
         MimicRegistryManager.register(this);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   private void method513(ServerLoadEvent var1) {
      if (!this.aFp.isEmpty() && this.contentManager != null) {
         this.contentManager
            .method8107(
               new IllegalStateException(
                  "Dependencies failed to enable: " + String.join(", ", this.aFp) + ". Fix their startup errors before loading ItemsAdder content."
               )
            );
      }

      if (this.contentManager != null) {
         this.contentManager.method8081();
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   private void method514(AsyncPlayerPreLoginEvent var1) {
      if (var1.getLoginResult() == Result.ALLOWED
         && (
            !this.aFq.isDone()
               || this.aFq.isCompletedExceptionally()
               || this.contentManager == null
               || !this.contentManager.aL.generator.packFileExists() && this.contentManager.method8083()
         )) {
         var1.disallow(Result.KICK_OTHER, "ItemsAdder is still loading server content. Please try again shortly.");
      }
   }

   @EventHandler
   private void method515(PlayerJoinEvent var1) {
      NativePacketsHolder.PE().method1881(var1.getPlayer(), RotationPolicy.fromId(ViaVersionIntegration.method4866(var1.getPlayer().getUniqueId())));
      if (var1.getPlayer().isOp()) {
         this.aFn.forEach(var1x -> Log.sendPrefixed(var1.getPlayer(), en(var1x)));
      }

      if (this.aFo && var1.getPlayer().isOp()) {
         SchedulerUtils.runLaterSync(
            () -> Log.sendPrefixed(var1.getPlayer(), ChatColor.GREEN + "UPDATE available: " + ChatColor.AQUA + "https://www.spigotmc.org/resources/73355/"),
            80L
         );
      }
   }

   @EventHandler
   private void method516(PlayerQuitEvent var1) {
      if (NativePacketsHolder.aN()) {
         NativePacketsHolder.ax(var1.getPlayer());
      }
   }

   public File getFile() {
      return super.getFile();
   }

   public String getVersion() {
      return this.getDescription().getVersion();
   }

   private static boolean el(String var0) {
      return Bukkit.getPluginManager().isPluginEnabled(var0);
   }

   public static boolean isPluginEnabled(String var0) {
      return Bukkit.getPluginManager().getPlugin(var0) != null;
   }

   private void method517(Plugin var1) {
      if (var1 != null && var1.isEnabled()) {
         Bukkit.getPluginManager().disablePlugin(var1);
         if (!var1.isEnabled() && this.aFn.add(var1.getName())) {
            if (el("WorldEdit") || el("FastAsyncWorldEdit")) {
               WorldEditIntegration.removeLegacyParsers(var1);
            }

            String var2 = en(var1.getName());
            Log.warn(var2);
            Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(var1x -> Log.sendPrefixed(var1x, var2));
         }
      }
   }

   private static String en(String var0) {
      return var0 + " was disabled: ItemsAdder now includes the WorldEdit/FAWE integration. Remove the legacy plugin jar.";
   }

   public static boolean eo(String var0) {
      Plugin var1 = Bukkit.getPluginManager().getPlugin(var0);
      if (var1 == null) {
         return false;
      }

      Bukkit.getPluginManager().disablePlugin(var1);
      return true;
   }

   @NotNull
   public static YamlConfiguration loadPluginYaml(File var0) {
      try {
         String var1 = method519(var0, "plugin.yml");
         if (var1 == null) {
            throw new RuntimeException("plugin.yml not found in the jar file.");
         }

         YamlConfiguration var2 = new YamlConfiguration();
         var2.loadFromString(var1);
         return var2;
      } catch (InvalidConfigurationException var3) {
         throw new RuntimeException("plugin.yml not found in the jar file.");
      }
   }

   public static String method519(File var0, String var1) {
      try {
         JarFile var2 = new JarFile(var0);

         try {
            JarEntry var4 = var2.getJarEntry(var1);

            try (InputStream var5 = var2.getInputStream(var4)) {
               return new BufferedReader(new InputStreamReader(var5, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            }
         } finally {
            var2.close();
         }
      } catch (Exception var16) {
         var16.printStackTrace();
         return null;
      }
   }

   public static String ep(String var0) {
      if (!var0.startsWith("/")) {
         var0 = "/" + var0;
      }

      InputStream var1 = Main.class.getResourceAsStream(var0);
      InputStreamReader var2 = new InputStreamReader(var1);
      BufferedReader var3 = new BufferedReader(var2);
      StringBuffer var4 = new StringBuffer();

      String var5;
      while ((var5 = var3.readLine()) != null) {
         var4.append(var5);
      }

      var3.close();
      var2.close();
      var1.close();
      return var4.toString();
   }

   public static String method520(File var0) {
      FileInputStream var1 = new FileInputStream(var0);
      InputStreamReader var2 = new InputStreamReader(var1);
      BufferedReader var3 = new BufferedReader(var2);
      StringBuffer var4 = new StringBuffer();

      String var5;
      while ((var5 = var3.readLine()) != null) {
         var4.append(var5);
      }

      var3.close();
      var2.close();
      var1.close();
      return var4.toString();
   }

   @Nullable
   public static JsonObject method521(String var0, int var1) {
      URL var2 = new URL(var0);
      HttpURLConnection var3 = (HttpURLConnection)var2.openConnection();
      var3.setRequestMethod("GET");
      var3.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
      var3.setRequestProperty("Content-length", "0");
      var3.setUseCaches(false);
      var3.setAllowUserInteraction(false);
      var3.setConnectTimeout(var1);
      var3.setReadTimeout(var1);
      var3.connect();
      int var4 = var3.getResponseCode();
      switch (var4) {
         case 200:
         case 201:
            BufferedReader var5 = new BufferedReader(new InputStreamReader(var3.getInputStream()));
            StringBuilder var6 = new StringBuilder();

            String var7;
            while ((var7 = var5.readLine()) != null) {
               var6.append(var7).append("\n");
            }

            var5.close();
            var3.disconnect();
            return (JsonObject)new Gson().fromJson(var6.toString(), JsonObject.class);
         case 404:
            return null;
         default:
            var3.disconnect();
            throw new IOException("Error connecting: " + var4);
      }
   }

   static {
      if (Runtime.version().feature() < 21) {
         throw new UnsupportedClassVersionError("ItemsAdder requires Java 21 or newer.");
      }

      field28 = "1489280531320537218";
      aEF = "%%__NONCE__%%";
      downloadUsername = "%%__USERNAME__%%";
      aEH = "%%__POLYMART__%%";
   }
}
