package dev.lone.itemsadder.entity;

import com.google.common.collect.ImmutableList;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.anim.AnimationBinaryCodec;
import dev.lone.itemsadder.anim.AnimationPresetLibrary;
import dev.lone.itemsadder.anim.EntityLifecycleState;
import dev.lone.itemsadder.api.Events.PlayerEmoteEndEvent;
import dev.lone.itemsadder.api.Events.PlayerEmotePlayEvent;
import dev.lone.itemsadder.api.Events.PlayerEmoteEndEvent.Cause;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.PlaceableItemSpec;
import dev.lone.itemsadder.item.PlacedItemInstance;
import dev.lone.itemsadder.metrics.LongAdderStats;
import dev.lone.itemsadder.metrics.LongStatsSnapshot;
import dev.lone.itemsadder.network.NativePacketsBridge;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.particle.ParticleOffsetEmitter;
import dev.lone.itemsadder.physics.EntityBoundsSnapshot;
import dev.lone.itemsadder.resourcepack.ResourcePackGenerator;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.CustomHashMap;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NullableBox;
import dev.lone.itemsadder.util.PlayerActionBar;
import dev.lone.itemsadder.util.RunnableQueue;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.ToggleState;
import dev.lone.itemsadder.util.Utils;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fc;

public class CustomEntityManager {
   private static final ThreadMXBean om = ManagementFactory.getThreadMXBean();
   private static final com.sun.management.ThreadMXBean on;
   private static volatile LongAdderStats field100;
   public static int op = 0;
   public static final int oq = 50;
   private static final long or = 5L;
   private static final long os = 200L;
   private static final long ot = 20L;
   public static final int ou = 40;
   public static final double ov = 0.3F;
   public static final long ow = 10L;
   public static final double ox = 1.75;
   public static final double oy = -0.0784000015258789;
   public static final NamespacedKey oz = new NamespacedKey("itemsadder", "entity_mount_seat");
   public static final NamespacedKey oA = new NamespacedKey("itemsadder", "entity_mount_seat_custom_entity");
   public static final NamespacedKey oB = new NamespacedKey("itemsadder", "custom_entity");
   static final NamespacedKey oC = new NamespacedKey("itemsadder", "age_stage_root");
   public static final NamespacedKey oD = new NamespacedKey("itemsadder", "custom_entity_player_skin");
   public static final NamespacedKey oE = new NamespacedKey("itemsadder", "player_disguise");
   public static final String oF = "_iainternal:player";
   public static final String oG = "_iainternal:player_v2";
   public static final String oH = "_iainternal/models/entity/player/";
   public static final String field101 = "player_animations";
   public static final String oJ = "player_advanced_animations";
   public static final String oK = "player";
   public static final String oL = "internal/player/player.player_animations";
   public static final String oM = "ewogICJ0aW1lc3RhbXAiIDogMTY2NjMwMjkxNzkzOCwKICAicHJvZmlsZUlkIiA6ICJjOWRlZTM4MDUzYjg0YzI5YjZlZjA5YjJlMDM5OTc0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTQVJfRGVjZW1iZXI1IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzMxZjQ3N2ViMWE3YmVlZTYzMWMyY2E2NGQwNmY4ZjY4ZmE5M2EzMzg2ZDA0NDUyYWIyN2Y0M2FjZGYxYjYwY2IiCiAgICB9CiAgfQp9";
   public final Map oN = Collections.synchronizedMap(new HashMap());
   private final List field102 = Collections.synchronizedList(new ArrayList());
   private volatile List oP = List.of();
   private final Map oQ = new HashMap();
   private volatile EntityBoundsSnapshot[] oR = ModelPhysicsLoop.HV;
   private long oS;
   private final Map oT = Collections.synchronizedMap(new HashMap());
   private final Map oU = Collections.synchronizedMap(new CustomHashMap());
   private final Map oV = Collections.synchronizedMap(new HashMap());
   private final Map oW = Collections.synchronizedMap(new CustomHashMap());
   private long oX;
   private volatile boolean oY;
   private final ConcurrentMap oZ = new ConcurrentHashMap();
   private List pa = new ArrayList();
   private final Map pb = new HashMap();
   private HashMap pc = new HashMap();
   private List pd = new ArrayList();
   private final Map pe = Collections.synchronizedMap(new HashMap());
   private final Map pf = Collections.synchronizedMap(new HashMap());
   public final List pg = Collections.synchronizedList(new ArrayList());
   private static CustomEntityManager ph;
   private static volatile boolean field103;
   private CustomEntityEventListener pi;
   final RunnableQueue pj = new RunnableQueue();
   private final CustomEntityWorker pk = new CustomEntityWorker(this);
   private static final String pl = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAMAAACdt4HsAAAAaVBMVEUAAACqcllBNZs6MYmbY0kAr69GOqUrHg0ApKQzJBEElZWQWT8FiIgAf39KSkokGAiBUzk3NzcDeno/KhUKvLw/Pz92SzMAaGiPXj4oKChHIg4AzMy0e2GUYD7///9SPYlVVVV3QjVqQDDj1w1CAAAAAXRSTlMAQObYZgAAAsZJREFUWMPtlt2WojAQhA00kpAo4M+IjjuzM+//kFvVIcvq0RN0LtcihIZjfXQnCFkk1bVDc3U3avGoardFI+ANegYAMzf/pvIPA+h22J8H1LEIH0t4HOAch3G77XwH+eFxAMcAGjrdnpkFx3Gsu+EEdcN8I50t+y0EAk4cD06jOg/gD1vn0ODn1hLHIeX17azaa3ZyPp+NOIaoxWkaoGYBmjHuKQZ+Y0RdsQwWkgfEx09a8/Fp5PPDtOL1kmsVlAc4/lYgY76/jWHUav5tS0h+ENu69TJ0v/xw+vo6IegG8bhIQD0H0Hn4hcY4/34QEPg4ttDMf2UJ9f3xmM7fjQBr3stR6XpVVRalmpuA47Hv/wLEQ3ITYAC/m0EWsAMgn8H9EixLuM6gKMpiuVyWOBQIygbCfUSMrWJs0cNaIcJhBfFkApQTYAk1ITRV1RjDnjGdVfIDcDisEE6A0VgkQNhsgoWFHWMSyIKJkIYAwm4D0DZQMjG2dGFfjTpAPF4AWAYJJURTsAoIjBkSIiKemwonlxmgEcKdaQdrFTCWoAnxn2oEHdwGugRA6RggpozbhhTzlC4iFIA2AaAESLMQmDamj7ECLAEqLeQig1KNxCCCmtCgWU1CIwTwB+avALkCrNflqLUKLjJ0HCm64Q/MPxEuAP0ert+07hnu42QgnwAbA1SEYBkBEgHyL6Dv9zTuewj9+EhjWkKI4wIWenjo1LlksHjpv9QuqrJQhWDx0ktXyn5s+U3i64jxUwsOArwY84MMdpWBfpKB9TKrhLReKIr00V1B6dNO5QHJWQIE6edcCdrPA5QEFBOgiWa2+RkoAFFaDySN74OkCXhvveAp4aby2QxohjQDxgJrXBpEPVYCRIDaSUE0fxAhArwCqEiZ8RClJyHWorWrPQO4t15IftozgNvrBQVIAkgecL1e4AwSkdYHiyv9AfNfNne5fuGHAAAAAElFTkSuQmCC";
   private static BufferedImage pm = null;

   private static long percentileOf(long[] var0, double var1) {
      return var0.length == 0 ? 0L : var0[Math.max(0, (int)Math.ceil(var0.length * var1) - 1)];
   }

   private static long getThreadCpuTime() {
      return om.isCurrentThreadCpuTimeSupported() && om.isThreadCpuTimeEnabled() ? om.getCurrentThreadCpuTime() : -1L;
   }

   private static long getThreadAllocatedBytes() {
      return on != null && on.isThreadAllocatedMemorySupported() && on.isThreadAllocatedMemoryEnabled()
         ? on.getThreadAllocatedBytes(Thread.currentThread().threadId())
         : -1L;
   }

   public static void resetStats() {
      CustomEntityImpl.flushManagerQueue();
      field100 = new LongAdderStats();
   }

   public static LongStatsSnapshot fC() {
      CustomEntityImpl.flushManagerQueue();
      LongAdderStats var0 = field100;
      field100 = null;
      return var0 == null ? new LongStatsSnapshot(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L) : var0.gd();
   }

   public static void recordStat(long var0) {
      LongAdderStats var2 = field100;
      if (var2 != null) {
         var2.pt.add(var0);
      }
   }

   public static boolean isInitialized() {
      return ph != null;
   }

   public static CustomEntityManager getInstance() {
      if (ph == null) {
         ph = new CustomEntityManager();
      }

      return ph;
   }

   CustomEntityManager() {
      if (!field103) {
         this.pi = new CustomEntityEventListener();
         this.pi.register(Main.getInstance());
         this.startSchedulers();
         this.pk.start();
      }
   }

   public void shutdown() {
      this.shutdownAsync();
   }

   public CompletionStage shutdownAsync() {
      field103 = true;
      if (this.pi != null) {
         this.pi.method9232(Set.copyOf(this.pg));
      }

      this.clearAll();
      CompletionStage var1 = this.pi == null ? CompletableFuture.completedFuture(null) : this.pi.vM();
      SchedulerUtils.cancelTasksFor(this.getClass());
      return CompletableFuture.allOf(this.pk.gz().toCompletableFuture(), var1.toCompletableFuture());
   }

   public boolean fG() {
      return this.oN.containsKey("_iainternal:player");
   }

   public int getEntityCount() {
      return this.field102.size();
   }

   void submitToOwner(CustomEntityImpl var1, Runnable var2) {
      Runnable var3 = () -> {
         if (!field103 && !var1.removed) {
            var2.run();
         }
      };
      this.pk.submit(var3);
   }

   public static boolean isOwnerThread() {
      return ph != null && ph.pk.gx();
   }

   public static void requireOwnerThread() {
      if (!isOwnerThread()) {
         throw new IllegalStateException("Custom entity mutation must run on the render owner");
      }
   }

   public boolean isNametagsEnabled() {
      return this.oY;
   }

   public void setNametagsEnabled(boolean var1) {
      if (this.oY != var1) {
         this.oY = var1;
         List var2;
         synchronized (this.field102) {
            var2 = List.copyOf(this.field102);
         }

         this.pk.submit(() -> {
            for (CustomEntityImpl var3 : var2) {
               if (var1) {
                  var3.createNametagDisplay();
               } else {
                  var3.clearNametagDisplay();
               }
            }
         });
      }
   }

   public void clearDefinitions() {
      this.oN.clear();
   }

   public Map fM() {
      return this.pe;
   }

   public List findDefinitions(String var1, boolean var2) {
      Set var3 = this.oN.keySet();
      ArrayList var4 = new ArrayList();

      for (String var6 : var3) {
         if (var6.contains(var1)) {
            var4.add(var6);
         }
      }

      if (var4.size() > 0) {
         if (!var2) {
            var4.remove("_iainternal:player");
         }

         var4.removeAll(this.pd);
         return var4;
      } else {
         return Collections.singletonList("MobNotFound");
      }
   }

   void registerEntity(CustomEntityImpl var1) {
      var1.setEntityManager(this);
      synchronized (this.field102) {
         this.field102.add(var1);
      }

      LivingEntity var5 = var1.sl.isAttached() ? var1.getBukkitEntity() : null;
      EntityViewerKey var3 = var5 != null && !var1.qQ ? new EntityViewerKey(var5.getUniqueId(), var5) : null;
      if (var5 != null) {
         this.oT.put(var5.getUniqueId(), var1);
         this.oU.put(var5.getEntityId(), var1);
         if (var3 != null) {
            this.oZ.put(var5.getEntityId(), var3);
         }
      }

      var1.rt = var1.sl.getWorld();
      this.oV.computeIfAbsent(var1.rt, var0 -> new ArrayList<>()).add(var1);
      this.pk.submit(() -> {
         var1.advanceLifecycle();
         if (this.oY) {
            var1.createNametagDisplay();
         }

         var1.spawnInteractionEntity();
         var1.scheduleAllBoneUpdates();
         synchronized (this.field102) {
            this.oP = List.copyOf(this.field102);
         }
      });
   }

   public void registerNetId(CustomEntityImpl var1, int var2) {
      if (var1.getLifecycleState() == EntityLifecycleState.ACTIVE && this.field102.contains(var1)) {
         this.oW.put(var2, var1);
         LivingEntity var3 = var1.sl.isAttached() ? var1.getBukkitEntity() : null;
         if (var3 != null && !var1.qQ && (var1.qR || var2 == var1.getInteractionEntityId())) {
            this.oZ.put(var2, new EntityViewerKey(var3.getUniqueId(), var3));
         }
      }
   }

   public void registerSettingsBuilder(CustomMobSettingsBuilder var1) {
      this.registerSettings(var1.build());
   }

   public void registerSettings(CustomMobSettings var1) {
      this.oN.put(var1.vn.getNamespacedID(), var1);
   }

   public void registerBuiltInModels() {
      CachedDisplayEntity.clearCache();
      String var1 = "_iainternal:player".split(":")[0];
      String var2 = "_iainternal:player".split(":")[1];

      CustomEntityModel var3;
      try {
         var3 = new CustomEntityModel(var1, var2);
         var3.method442("player", Main.ep("internal/player/player.player_animations"), true);
      } catch (Exception var10) {
         Log.error("Failed to read player animation data: " + var1 + ":" + var2, var10);
         return;
      }

      CustomMobSettingsBuilder var4 = CustomMobSettings.toBuilder()
         .withModel(var3)
         .withDisplayName("_iainternal:player")
         .withEntityType(EntityType.ZOMBIE)
         .withHasBossbar(false)
         .withSilent(true)
         .withCanSunBurn(false)
         .withCanDropLoot(false)
         .withMountOnInteract(false)
         .withShadow(true)
         .withMaxHealth(20.0)
         .withBossBarStyle(null)
         .withBossBarColor(null)
         .withMovementSpeed(null)
         .withFlyingSpeed(null)
         .withInvulnerable(false);
      getInstance().registerSettingsBuilder(var4);
      if (Settings.get().emoteModernMethod && NativePacketsHolder.aN()) {
         CustomEntityModel var5 = new CustomEntityModel("_iainternal", "player_v2");
         if (var5.method449(new File("internal/player_v2.iaentity"), AnimationPresetLibrary.method9226("_iainternal", "player_v2"))) {
            this.registerSettingsBuilder(
               CustomMobSettings.toBuilder()
                  .withModel(var5)
                  .withDisplayName("_iainternal:player_v2")
                  .withEntityType(EntityType.ZOMBIE)
                  .withHasBossbar(false)
                  .withSilent(true)
                  .withCanSunBurn(false)
                  .withCanDropLoot(false)
                  .withMountOnInteract(false)
                  .withShadow(true)
                  .withMaxHealth(20.0)
                  .withBossBarStyle(null)
                  .withBossBarColor(null)
                  .withMovementSpeed(null)
                  .withFlyingSpeed(null)
                  .withInvulnerable(false)
            );
         }

         if (!Settings.get().emotesUseArmorStands) {
            try (InputStream var6 = Main.class.getResourceAsStream("/internal/player/player_emotes_v2.iaemotes")) {
               if (var6 == null) {
                  throw new IOException("Bundled V2 player emotes are missing");
               }

               CustomEntityModel var7 = new CustomEntityModel("_iainternal", "player_emotes");
               if (var7.method449(new File("internal/player/player_emotes_v2.iaemotes"), AnimationBinaryCodec.method9161(var6.readAllBytes()))) {
                  this.registerSettingsBuilder(
                     CustomMobSettings.toBuilder()
                        .withModel(var7)
                        .withDisplayName("_iainternal:player_emotes")
                        .withEntityType(EntityType.ZOMBIE)
                        .withHasBossbar(false)
                        .withSilent(true)
                        .withCanSunBurn(false)
                        .withCanDropLoot(false)
                        .withMountOnInteract(false)
                        .withShadow(true)
                        .withMaxHealth(20.0)
                        .withBossBarStyle(null)
                        .withBossBarColor(null)
                        .withMovementSpeed(null)
                        .withFlyingSpeed(null)
                        .withInvulnerable(false)
                  );
               }
            } catch (IOException | IllegalArgumentException var12) {
               Log.error("Failed to load bundled V2 player emotes", var12);
            }
         }
      }

      for (File var14 : ContentManager.method8203()) {
         FileUtils.listFiles(var14, new String[]{"player_animations"}, true)
            .forEach(
               var2x -> {
                  try {
                     if (this.isDuplicateAnimationFile(var2x)) {
                        Log.warn("Warning: Skipped loading of a '.player_animations' file.");
                        Log.warn("Found both '.player_advanced_animations' and '.player_animations' file in folder.");
                        Log.warn("Please delete the folder and regenerate it using Blockbench. File: " + Utils.relativePath(var2x));
                        return;
                     }

                     var3.method445(
                        var2x,
                        ResourcePackGenerator.getContentsNamespace(var2x) + "_" + FilenameUtils.removeExtension(var2x.getName()),
                        FileUtils.readFileToString(var2x, StandardCharsets.UTF_8.toString())
                     );
                  } catch (Exception var4x) {
                     Log.error("Failed to load player animations from file: " + var2x.getAbsolutePath());
                     var4x.printStackTrace();
                  }
               }
            );
      }
   }

   public void loadAdvancedPlayerAnimations() {
      for (File var2 : ContentManager.method8203()) {
         FileUtils.listFiles(var2, new String[]{"player_advanced_animations"}, true)
            .forEach(
               var1 -> {
                  try {
                     if (!Settings.get().emotesUseArmorStands) {
                        Log.warn(
                           "Skipped legacy emote model because modern emotes are enabled (use_armorstands=false). Open the original emote project in Blockbench and re-export it in the modern format using the new ItemsAdder extension. File: "
                              + Utils.relativePath(var1)
                        );
                        return;
                     }

                     if (this.isDuplicateAnimationFile(var1)) {
                        Log.warn(
                           "Warning: Skipped loading of advanced player animations. Found duplicate. Please delete it and regenerate it using Blockbench. File: "
                              + Utils.relativePath(var1)
                        );
                        return;
                     }

                     String var2x = var1.getParentFile().getParentFile().getParentFile().getParentFile().getName();
                     String var3 = var1.getParentFile().getName();
                     getInstance().loadPlayerModelEntity(var2x + ":" + var3, Utils.substringFrom(Utils.normalizeSlashes(var1.getAbsolutePath()), "assets/"));
                  } catch (Exception var4) {
                     Log.error("Failed to load player animations from file: " + var1.getAbsolutePath());
                     var4.printStackTrace();
                  }
               }
            );
      }
   }

   public void loadBehaviors() {
      this.oN.forEach((var0, var1) -> {
         var1.wm.load();
         var1.wn.load();
      });
   }

   private boolean isDuplicateAnimationFile(File var1) {
      for (Entry var3 : getInstance().oN.entrySet()) {
         String var4 = (String)var3.getKey();
         CustomMobSettings var5 = (CustomMobSettings)var3.getValue();
         if (!var4.equals("_iainternal:player") && var5.vn.AM && var5.vn.method471(var1)) {
            return true;
         }
      }

      return false;
   }

   public void loadPlayerModelEntity(String var1, String var2) {
      String var3 = var1.split(":")[0];
      String var4 = var1.split(":")[1];

      CustomEntityModelBridge var5;
      try {
         var5 = new CustomEntityModelBridge(var3, var4);
         if (!var5.bc(var2)) {
            return;
         }
      } catch (Exception var7) {
         Log.error("Failed to load custom player model entity: " + var3 + ":" + var4);
         var7.printStackTrace();
         return;
      }

      CustomMobSettingsBuilder var6 = CustomMobSettings.toBuilder()
         .withModel(var5)
         .withDisplayName(var1)
         .withEntityType(EntityType.ZOMBIE)
         .withHasBossbar(false)
         .withSilent(true)
         .withCanSunBurn(false)
         .withCanDropLoot(false)
         .withMountOnInteract(false)
         .withShadow(true)
         .withMaxHealth(20.0)
         .withBossBarStyle(null)
         .withBossBarColor(null)
         .withMovementSpeed(null)
         .withFlyingSpeed(null)
         .withInvulnerable(false)
         .withGravity(true)
         .withAi(true);
      getInstance().registerSettingsBuilder(var6);
      Log.log("Loaded player model " + var5.getNamespacedID());
   }

   public void loadEmoteConfig(YamlConfig var1) {
      ConfigSectionWrapper var2 = var1.kc("emotes");
      if (var2 != null) {
         for (String var5 : var2.bJ(false)) {
            ConfigSectionWrapper var6 = var2.kc(var5);
            String var7 = var6.bc("id", null);
            if (var7 == null) {
               Log.warn("Missing id for emote '" + var5 + "'. File:" + var1.DJ());
            } else {
               NullableBox var8 = NullableBox.empty();
               ModelDefinitionData var9 = this.findEmoteDefinition(var5, var8);
               if (var9 != null) {
                  if (!var6.method7078("enabled", true)) {
                     ((CustomEntityModel)var8.value).method448(var5);
                  } else {
                     this.pb.put(var5, var6.jZ("keywords"));
                     var9.setFlags(
                        var6.method7078("hide_equipment.helmet", false),
                        var6.method7078("hide_equipment.mainhand", false),
                        var6.method7078("hide_equipment.offhand", false)
                     );
                     var9.ac(var6.method7078("can_player_move", true));
                     var9.af(var6.method7078("third_person_view", Settings.get().emotesThirdPersonView));
                     var9.ag(var6.method7078("track_player_rotation.head", true));
                     var9.ah(var6.method7078("track_player_rotation.body", true));
                     var9.method8728(
                        var6.method7078("cancel_conditions.sneak", true),
                        var6.method7078("cancel_conditions.take_damage", true),
                        var6.method7078("cancel_conditions.deal_damage", true),
                        var6.method7078("cancel_conditions.in_air", true)
                     );
                     var9.ad(var6.method7078("on_stop.stop_sounds", true));
                     var9.ae(var6.method7078("on_stop.skip_emote_end", false));
                     var9.setOptionalRange(var6.method7101("speed.movement", null), var6.method7101("speed.flying", null));
                     if (var6.hA("background_music.sound.name")) {
                        var9.setSound(
                           var6.jO("background_music.sound.name"),
                           BukkitRegistryLookup.soundCategoryOr(var6.jO("background_music.sound.category"), SoundCategory.MUSIC),
                           var6.method7102("background_music.sound.volume", 1.0F),
                           var6.method7102("background_music.sound.pitch", 1.0F),
                           var6.method7102("background_music.repeat_interval_seconds", 0.0F)
                        );
                     }
                  }
               } else {
                  Log.warn("Unknown emote '" + var5 + "'. File:" + var1.DJ());
               }
            }
         }
      }
   }

   public CustomEntityImpl attachModelPart(ModelPartDisplay var1, String var2, Matrix4fc var3) {
      if (!Bukkit.isPrimaryThread()) {
         throw new IllegalStateException("Attach models on the server thread");
      }

      CustomEntityImpl var4 = var1.Hm;
      CustomMobSettings var5 = (CustomMobSettings)this.oN.get(var2);
      if (var4.removed || var4.getModelPart(var1.Ho.getId()) != var1) {
         throw new IllegalStateException("Attachment owner or bone is no longer available");
      }

      if (!var4.qK.vn.AN || var4.qK.vn.AO || var5 == null || !var5.vn.AN || var5.vn.AO || var5.vn.AM) {
         throw new IllegalArgumentException("Attachments require registered V2 models");
      }

      if (var3.isFinite() && (var3.properties() & 2) != 0) {
         Location var6 = var4.getLocation().clone();
         var6.setYaw(0.0F);
         var6.setPitch(0.0F);
         CustomEntityImpl var7 = new CustomEntityImpl(var5, false, true);
         var7.qP = true;
         var7.qQ = true;
         var7.qT = true;
         var7.qO = false;
         var7.spawn(var6);
         var7.rq = new EntityPartRenderer(var1, var7, var3);
         if (var4.rs == null) {
            var4.rs = new ArrayList();
         }

         var4.rs.add(var7);
         this.registerEntity(var7);
         return var7;
      } else {
         throw new IllegalArgumentException("Attachment offset must be finite and affine");
      }
   }

   @Nullable
   public CustomEntityImpl spawnCustomEntity(String var1, Location var2, boolean var3, boolean var4, boolean var5) {
      return this.spawnEntityAt(var1, null, var2, var3, var4, var5);
   }

   @Nullable
   public CustomEntityImpl spawnCustomEntitySimple(String var1, Location var2, boolean var3) {
      return this.spawnEntityAt(var1, null, var2, var3, false, false);
   }

   public CustomEntityImpl method8260(
      String var1, Location var2, @Nullable String var3, List var4, boolean var5, boolean var6, boolean var7, @Nullable Consumer var8
   ) {
      return this.method8261(var1, var2, var3, var4, var5, var6, var7, true, false, var8);
   }

   @Nullable
   public CustomEntityImpl method8261(
      String var1,
      Location var2,
      @Nullable String var3,
      List var4,
      boolean var5,
      boolean var6,
      boolean var7,
      boolean var8,
      boolean var9,
      @Nullable Consumer var10
   ) {
      if (var2 == null || var2.getWorld() == null) {
         return null;
      }

      if (var3 == null && var1.equals("_iainternal:player")) {
         return null;
      }

      CustomEntityImpl var11 = new CustomEntityImpl((CustomMobSettings)this.oN.get(var1), true);
      if (var3 != null) {
         var11.setPlayerSkinRaw(var3);
      }

      var11.qO = var5;
      var11.qP = var6;
      var11.qQ = var7;
      var11.qR = var8;
      var11.qS = var9;
      var11.qT = true;
      if (var6) {
         var11.spawn(var2);
         if (var10 != null) {
            var10.accept(null);
         }
      } else {
         var11.spawnEntityWithConsumer(var2, var10);
      }

      for (Player var13 : var4) {
         var11.createViewer(var13, true);
      }

      if (var3 != null && !var6) {
         var11.changePlayerSkin(var3);
      }

      this.registerEntity(var11);
      var11.getModelInstance().qE();
      var11.pushMovementUpdate();
      return var11;
   }

   @Nullable
   public CustomEntityImpl spawnEntityAt(String var1, @Nullable String var2, Location var3, boolean var4, boolean var5, boolean var6) {
      if (var3 != null && var3.getWorld() != null) {
         CustomMobSettings var7 = (CustomMobSettings)this.oN.get(var1);
         if (var7 == null) {
            return null;
         }

         if (var2 == null && var1.equals("_iainternal:player")) {
            return null;
         }

         if (var7.vn.AM && var2 == null) {
            return null;
         }

         CustomEntityImpl var8 = new CustomEntityImpl(var7, true);
         if (var2 != null) {
            var8.setPlayerSkinRaw(var2);
         }

         var8.addViewersInWorld(var3.getWorld());
         var8.qO = var4;
         var8.qQ = var6;
         var8.qP = var5;
         if (var5) {
            var8.spawn(var3);
         } else {
            var8.spawnEntity(var3);
         }

         if (var2 != null && !var5) {
            var8.changePlayerSkin(var2);
         }

         this.registerEntity(var8);
         var8.getModelInstance().qE();
         var8.pushMovementUpdate();
         return var8;
      } else {
         return null;
      }
   }

   @Nullable
   public CustomEntityImpl attachModel(String var1, LivingEntity var2, boolean var3) {
      return this.attachModelWithSkin(var1, null, var2, true, var3, false, true);
   }

   @Nullable
   public CustomEntityImpl attachModelWithSkin(String var1, @Nullable String var2, LivingEntity var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      return this.attachModelInternal(var1, var2, var3, var4, var5, var6, false, var7);
   }

   @Nullable
   public CustomEntityImpl attachModelInternal(
      String var1, @Nullable String var2, LivingEntity var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8
   ) {
      if (this.isCustomEntityLoaded(var3)) {
         this.removeEntityByBukkit(var3);
      }

      CustomEntityImpl var9 = new CustomEntityImpl((CustomMobSettings)this.oN.get(var1), var5);
      var9.qO = var4;
      var9.qQ = var6;
      var9.qR = var7;
      var9.addViewersInWorld(var3.getWorld());
      var9.attachToEntity(var3, var8);
      this.applyAttachedSkin(var2, var3, var9);
      this.registerEntity(var9);
      return var9;
   }

   @Nullable
   public CustomEntityImpl attachModelWithViewers(String var1, @Nullable String var2, LivingEntity var3, List var4, boolean var5, boolean var6) {
      if (this.isCustomEntityLoaded(var3)) {
         this.removeEntityByBukkit(var3);
      }

      CustomEntityImpl var7 = new CustomEntityImpl((CustomMobSettings)this.oN.get(var1), var6);
      var7.qO = var5;
      var7.qT = true;
      var7.attachToEntity(var3, true);
      this.applyAttachedSkin(var2, var3, var7);

      for (Player var9 : var4) {
         var7.createViewer(var9, true);
      }

      this.registerEntity(var7);
      return var7;
   }

   private void applyAttachedSkin(@Nullable String var1, LivingEntity var2, CustomEntityImpl var3) {
      if (var1 == null) {
         String var4 = (String)var2.getPersistentDataContainer().get(oD, PersistentDataType.STRING);
         if (var4 != null) {
            var3.changePlayerSkin(var4);
         }
      } else {
         var3.changePlayerSkin(var1);
      }
   }

   @Nullable
   public CustomEntityImpl wrapEntity(LivingEntity var1) {
      return this.restoreEntity(var1, true);
   }

   @Nullable
   public CustomEntityImpl restoreEntity(LivingEntity var1, boolean var2) {
      if (!isTaggedCustomEntity(var1)) {
         return null;
      }

      if (var2) {
         this.removeEntityByBukkit(var1);
      }

      String var3 = (String)var1.getPersistentDataContainer().get(oB, PersistentDataType.STRING);
      CustomMobSettings var4 = (CustomMobSettings)this.oN.get(var3);
      if (var4 == null) {
         return null;
      }

      String var5 = (String)var1.getPersistentDataContainer().get(oD, PersistentDataType.STRING);
      CustomEntityImpl var6 = new CustomEntityImpl(var4, true);
      var6.addViewersInWorld(var1.getWorld());
      var4.applyToEntity(var1, null, false);
      var6.attachToEntity(var1, true);
      this.registerEntity(var6);
      if (var5 != null) {
         var6.changePlayerSkin(var5);
      }

      for (EntityViewer var8 : var6.getViewers()) {
         var6.updateViewer(var8);
      }

      var6.dispatchToManager(var6.getModelInstance()::qM);
      return var6;
   }

   private void updateWorldIndex(CustomEntityImpl var1) {
      World var2 = var1.getWorld();
      if (var1.rt != var2 && !var1.removed) {
         ArrayList var3 = (ArrayList)this.oV.get(var1.rt);
         if (var3 != null) {
            var3.remove(var1);
         }

         this.oV.computeIfAbsent(var2, var0 -> new ArrayList<>()).add(var1);
         var1.rt = var2;
      }
   }

   public void refreshEntity(CustomEntityImpl var1) {
      if (var1 != null) {
         this.updateWorldIndex(var1);
         var1.refreshState();
         if (!var1.qT) {
            var1.addViewersInWorld(var1.getWorld());
         }

         var1.dispatchToManager(() -> {
            var1.applyStateSnapshot();
            List var1x = var1.getViewers();
            int var2 = 0;

            for (int var3 = var1x.size(); var2 < var3; var2++) {
               EntityViewer var4 = (EntityViewer)var1x.get(var2);
               var1.notifyEquipmentViewer(var4);
               var4.uW = true;
               ProjectileTypeHelper.getAdapter().method3261(var4.li, var1.getEntityIdsFor(var4.getRotationPolicy()));
            }

            var2 = 0;

            for (int var6 = var1x.size(); var2 < var6; var2++) {
               EntityViewer var7 = (EntityViewer)var1x.get(var2);
               if (var1.sm != null && var1.getModel().AN && !var1.getModel().AO) {
                  var1.updateViewer(var7);
               } else {
                  var1.rQ.forEach((var1xx, var2x) -> {
                     var7.uW = false;
                     var2x.spawnForViewer(var7);
                     if (var2x.Ho.An == BonePurpose.MOUNT_PIVOT && var2x.getRider() != null) {
                        var2x.getRider().sendPassengersUpdate();
                     }
                  });
               }
            }

            var1.sendRemovePackets();
            var1.dispatchToManager(var1.rC::qM);
         });
      }
   }

   public static void tagEntityModel(Entity var0, String var1) {
      var0.getPersistentDataContainer().set(oB, PersistentDataType.STRING, var1);
   }

   public static void tagEntitySkin(Entity var0, String var1) {
      var0.getPersistentDataContainer().set(oD, PersistentDataType.STRING, var1);
   }

   public void startSchedulers() {
      SchedulerUtils.runLater(
         () -> {
            boolean var1 = false;

            for (CustomEntityImpl var3 : this.oP) {
               if (!var3.removed) {
                  if (var3.rq != null) {
                     EntityPartRenderer var4 = var3.rq;
                     if (var4.xu || var4.xp.Hm.removed) {
                        this.removeWithEffects(var3, false, false);
                        continue;
                     }

                     var4.mh();
                     this.updateWorldIndex(var3);
                  }

                  this.evolveMob(var3);
                  if (!var3.qW) {
                     var3.checkSensitivity();
                     if (var3.rq == null
                        && var3.qK.vn.AN
                        && !var3.qK.vn.AO
                        && var3.getBukkitEntity() instanceof Zombie var6
                        && !(var6 instanceof ZombieVillager)
                        && var6.isConverting()) {
                        var6.setConversionTime(-1);
                     }

                     if (var3.usesHumanoidEquipment()) {
                        var3.syncEquipmentSlots();
                     }

                     var3.captureState();
                     var1 |= var3.isRagdolling();
                  }
               }
            }

            if (var1) {
               this.updatePlayerBounds();
            } else if (!this.oQ.isEmpty()) {
               this.oQ.clear();
               this.oR = ModelPhysicsLoop.HV;
            }
         },
         1L
      );
      SchedulerUtils.runRepeating(var1 -> {
         for (int var2 = 0; var2 < this.field102.size(); var2++) {
            CustomEntityImpl var3 = (CustomEntityImpl)this.field102.get(var2);
            if (!var3.qW) {
               if (var3.isRagdolling()) {
                  var3.clearBossBar();
                  var3.clearNametagLine();
               } else {
                  int var4 = var3.getLocation().getWorld().getViewDistance() * 16;
                  if (var3.rn.getWorld() != var3.getLocation().getWorld() || var3.rn.distance(var3.getLocation()) >= var4 - 1) {
                     this.refreshEntity(var3);
                  }

                  var3.rn = var3.getLocation();
                  List var5 = var3.getViewers();

                  for (int var6 = 0; var6 < var5.size(); var6++) {
                     EntityViewer var7 = (EntityViewer)var5.get(var6);
                     var7.lf();
                  }

                  var3.updateBossBar();
                  var3.updateNametag();
               }
            }
         }
      }, 0L, 5L);
      SchedulerUtils.runRepeating(var1 -> {
         for (int var2 = 0; var2 < this.field102.size(); var2++) {
            CustomEntityImpl var3 = (CustomEntityImpl)this.field102.get(var2);
            if (!var3.qW && !var3.isRagdolling() && !var3.usesHumanoidEquipment()) {
               var3.syncEquipmentSlots();
            }
         }
      }, 0L, 200L);
      SchedulerUtils.runRepeating(var1 -> {
         for (int var2 = 0; var2 < this.field102.size(); var2++) {
            CustomEntityImpl var3 = (CustomEntityImpl)this.field102.get(var2);
            if (!var3.qW && !var3.isRagdolling()) {
               var3.updateFireVisuals();

               for (ModelRiderController var5 : var3.rP) {
                  if (var5.isMounted()) {
                     var5.sendPassengersUpdateDup2();
                  }
               }
            }
         }
      }, 0L, 20L);
      SchedulerUtils.runRepeating(var1 -> {
         for (int var2 = 0; var2 < this.field102.size(); var2++) {
            CustomEntityImpl var3 = (CustomEntityImpl)this.field102.get(var2);
            if (!var3.qW && !var3.isRagdolling()) {
               for (ModelRiderController var5 : var3.rP) {
                  if (var5.isMounted()) {
                     var5.tick();
                  }
               }

               var3.sendRemovePackets();
               var3.applySensitivity();
               var3.updateViewDistance();
            }
         }
      }, 0L, 20L);
      SchedulerUtils.runLater(
         () -> {
            ArrayList var1 = new ArrayList();

            for (int var2 = 0; var2 < this.field102.size(); var2++) {
               CustomEntityImpl var3 = (CustomEntityImpl)this.field102.get(var2);
               if (!var3.qW
                  && var3.sl.isAttached()
                  && !var3.isDying()
                  && !var3.isRagdolling()
                  && !var3.rC.qZ()
                  && (!var3.getBukkitEntity().isValid() || var3.getBukkitEntity().isDead())) {
                  var1.add(var3);
               }
            }

            for (CustomEntityImpl var6 : var1) {
               PlacedItemInstance var4 = PlaceableItemSpec.findByEntity(var6.getBukkitEntity());
               if (var4 != null) {
                  var4.remove();
               }

               this.removeEntityByBukkit(var6.getBukkitEntity());
            }
         },
         10L
      );
      SchedulerUtils.runLater(() -> {
         for (int var1 = 0; var1 < this.field102.size(); var1++) {
            CustomEntityImpl var2 = (CustomEntityImpl)this.field102.get(var1);
            if (!var2.qW && var2.hasRiders()) {
               boolean var3 = false;
               boolean var4 = var2.qK.vL && var2.qK.getFloatValue() > 0.0F;

               for (ModelRiderController var6 : var2.rP) {
                  if (var6.isMounted()) {
                     var3 = true;
                     LivingEntity var7 = var6.MZ;
                     if (var4 && var7 != null) {
                        var7.setRemainingAir(var7.getMaximumAir());
                     }

                     var6.tick();
                  }
               }

               if (var2.qK.vL) {
                  LivingEntity var8 = var2.getBukkitEntity();
                  if (var8 != null) {
                     if (var4) {
                        var8.setRemainingAir(var8.getMaximumAir());
                     }

                     boolean var9 = var3 && var2.qK.getFloatValue() > 0.0F;
                     if (var9 && var8.isInWater()) {
                        var2.disableGravity(var8);
                     } else {
                        var2.restoreGravity(var8);
                     }

                     if (!var9) {
                        var2.sK = EntityMathUtils.method8647(var8, var2.qK.vM, var2.sK);
                     }
                  }
               }
            }
         }
      }, 1L);
   }

   private void evolveMob(CustomEntityImpl var1) {
      LivingEntity var2 = var1.getBukkitEntity();
      if (var2 instanceof Mob var3 && var3.isValid() && !var3.isDead()) {
         long var4 = op;
         if (var4 >= var1.qV) {
            String var6 = (String)var2.getPersistentDataContainer().get(oC, PersistentDataType.STRING);
            if (var6 == null) {
               var6 = var1.qK.vn.getNamespacedID();
            }

            CustomMobSettings var7 = (CustomMobSettings)this.oN.get(var6);
            if (var7 != null) {
               List var8 = var7.vX;
               if (!var8.isEmpty()) {
                  Integer var9 = var7.vY ? ProjectileTypeHelper.getAdapter().method3252(var3) ? -1 : 0 : ProjectileTypeHelper.getAdapter().method3251(var3);
                  String var10 = null;
                  if (var9 != null) {
                     for (CustomEntityRef var12 : var8) {
                        if (var9 >= var12.index()) {
                           var10 = var12.namespacedId();
                        }
                     }

                     if (var10 != null && !var10.equals(var1.qK.vn.getNamespacedID())) {
                        CustomMobSettings var26 = (CustomMobSettings)this.oN.get(var10);
                        if (var26 != null && var26.vw != null && var26.vw.getEntityClass() != null && Mob.class.isAssignableFrom(var26.vw.getEntityClass())) {
                           HashMap var27 = new HashMap();
                           HashMap var13 = new HashMap();
                           HashMap var14 = new HashMap();

                           for (Attribute var16 : Registry.ATTRIBUTE) {
                              AttributeInstance var17 = var2.getAttribute(var16);
                              if (var17 != null) {
                                 String var18 = var16.getKey().getKey().replaceFirst("^generic\\.", "");

                                 Double var19 = switch (var18) {
                                    case "max_health" -> var1.qK.vA;
                                    case "movement_speed" -> var1.qK.vD;
                                    case "flying_speed" -> var1.qK.vC == null ? null : var1.qK.vC.doubleValue();
                                    default -> (Double)var1.qK.vT.get(var18);
                                 };
                                 Double var33 = var19 == null ? ProjectileTypeHelper.getAdapter().method3273(var2, var16) : var19;
                                 if (Double.compare(var17.getBaseValue(), var33 == null ? var17.getDefaultValue() : var33) != 0) {
                                    var27.put(var16, var17.getBaseValue());
                                 }

                                 if (!var17.getModifiers().isEmpty()) {
                                    var14.put(var16, List.copyOf(var17.getModifiers()));
                                 }

                                 if (var27.containsKey(var16) || var14.containsKey(var16)) {
                                    var13.put(var16, var17.getBaseValue());
                                 }
                              }
                           }

                           double var28 = var2.getHealth();
                           var1.qV = var4 + 20L;
                           Mob var29 = ProjectileTypeHelper.getAdapter().method3253(var3, var26.vw);
                           if (var29 != null) {
                              if (var29 instanceof Ageable var30) {
                                 if (var7.vY) {
                                    if (var9 < 0) {
                                       var30.setBaby();
                                    } else {
                                       var30.setAdult();
                                    }
                                 } else {
                                    var30.setAge(var9);
                                 }
                              }

                              var29.getPersistentDataContainer().set(oC, PersistentDataType.STRING, var6);
                              var26.applyToEntity(var29, null, false);

                              for (Attribute var32 : Registry.ATTRIBUTE) {
                                 AttributeInstance var34 = var29.getAttribute(var32);
                                 if (var34 == null && var13.containsKey(var32)) {
                                    ProjectileTypeHelper.getAdapter().method3272(var29, var32.getKey(), (Double)var13.get(var32));
                                    var34 = var29.getAttribute(var32);
                                 }

                                 if (var34 != null) {
                                    Double var35 = (Double)var27.get(var32);
                                    if (var35 != null) {
                                       var34.setBaseValue(var35);
                                    }

                                    for (AttributeModifier var23 : var14.getOrDefault(var32, List.of())) {
                                       if (!var34.getModifiers().contains(var23)) {
                                          try {
                                             var34.addModifier(var23);
                                          } catch (IllegalArgumentException var25) {
                                          }
                                       }
                                    }
                                 }
                              }

                              var29.setHealth(Math.min(var28, var29.getMaxHealth()));
                              tagEntityModel(var29, var10);
                              var1.transferToMob(var29, var26, this, var2.getUniqueId());
                              this.oT.remove(var2.getUniqueId(), var1);
                              this.oU.remove(var2.getEntityId(), var1);
                              this.oT.put(var29.getUniqueId(), var1);
                              this.oU.put(var29.getEntityId(), var1);
                              this.clearViewerKey(var2.getEntityId(), var2.getUniqueId());
                              if (!var1.qQ) {
                                 this.oZ.put(var29.getEntityId(), new EntityViewerKey(var29.getUniqueId(), var29));
                              }

                              this.updateWorldIndex(var1);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void updatePlayerBounds() {
      long var1 = ++this.oS;
      Location var3 = new Location(null, 0.0, 0.0, 0.0);
      ArrayList var4 = null;

      for (Player var6 : Bukkit.getOnlinePlayers()) {
         var6.getLocation(var3);
         World var7 = var3.getWorld();
         if (var7 != null) {
            UUID var8 = var7.getUID();
            double var9 = var3.getX();
            double var11 = var3.getY();
            double var13 = var3.getZ();
            EntityPositionSnapshot var15 = (EntityPositionSnapshot)this.oQ.get(var6.getUniqueId());
            if (var15 == null) {
               this.oQ.put(var6.getUniqueId(), new EntityPositionSnapshot(var8, var9, var11, var13, var1));
            } else {
               double var16 = var9 - var15.pW;
               double var18 = var13 - var15.pY;
               double var20 = var11 - var15.pX;
               boolean var22 = var8.equals(var15.jD);
               var15.jD = var8;
               var15.pW = var9;
               var15.pX = var11;
               var15.pY = var13;
               var15.pZ = var1;
               double var23 = var16 * var16 + var18 * var18;
               if (var22
                  && !(Math.abs(var20) > 0.6)
                  && !(var23 < 2.25E-4)
                  && !(var23 > 0.6400000000000001)
                  && var6.isOnGround()
                  && var6.getVehicle() == null
                  && var6.getGameMode() != GameMode.SPECTATOR) {
                  if (var4 == null) {
                     var4 = new ArrayList(1);
                  }

                  var4.add(new EntityBoundsSnapshot(var8, var9, var11, var13, var6.getHeight(), var16, var18));
               }
            }
         }
      }

      if (var1 % 40L == 0L) {
         this.oQ.values().removeIf(var2 -> var2.pZ != var1);
      }

      this.oR = var4 == null ? ModelPhysicsLoop.HV : var4.toArray(ModelPhysicsLoop.HV);
   }

   private void tickEntities(long var1, long var3) {
      LongAdderStats var5 = field100;
      long var6 = System.nanoTime();
      if (var5 != null) {
         var5.method8367(var6);
      }

      long var8 = var5 == null ? -1L : getThreadCpuTime();
      long var10 = var5 == null ? -1L : getThreadAllocatedBytes();
      List var12 = this.oP;
      EntityBoundsSnapshot[] var13 = this.oR;

      try {
         this.pj.runAll();

         for (CustomEntityImpl var15 : var12) {
            if (!var15.removed && !var15.qW) {
               var15.refreshModelParts();
            }

            if (var15.rq != null && !var15.removed && !var15.rq.mi()) {
               for (EntityViewer var34 : var15.getViewers()) {
                  var15.hideViewer(var34);
               }
            } else if (var15.isRagdolling()) {
               if (!var15.removed) {
                  var15.applyStateSnapshot();
                  var15.tickRagdoll(var3, var13);
                  var15.updateModelParts();

                  for (EntityViewer var33 : var15.getViewers()) {
                     var33.method8558(var1);
                  }
               }
            } else if (!var15.removed && !var15.qW) {
               if (!var15.getModel().of() && var15.getViewers().isEmpty() && !var15.isModelReady()) {
                  var15.tickEquipment();
               } else {
                  var15.applyStateSnapshot();
                  if (!var15.getModel().of() && var15.qS && !var15.isModelReady() && var1 % 10000L != 0L) {
                     var15.tickEquipment();
                  } else {
                     if (!var15.ha() && !var15.jD() && !var15.if_() && !var15.ru) {
                        ModelDefinitionData var16 = var15.resolveCurrentAnimation();
                        if (var15.sZ) {
                           if (var15.isStateSM()) {
                              var16 = var15.getAttackAnimation();
                           }

                           if (var15.jn() && (!var15.jC() || var15.getSwimAnimation() == null)) {
                              var16 = var15.iq() && var15.getSneakWalkAnimation() != null
                                 ? var15.getSneakWalkAnimation()
                                 : (!var15.iq() && var15.getSneakAnimation() != null ? var15.getSneakAnimation() : var16);
                           }
                        }

                        if (var16 != null) {
                           var16 = var15.getModelInstance().method8906(var16);
                           if (var15.getCurrentAnimation() == var15.rL && !var15.getModel().of()) {
                              var15.queueAnimation(var16);
                           } else if (var15.isCurrentAnimationActive() && !var15.jo()) {
                              var15.playAnimationIfChanged(var16);
                           } else {
                              var15.queueAnimation(var16);
                           }
                        } else {
                           var15.getModelInstance().method8906(null);
                           if (var15.getModel().nZ()) {
                              var15.getModelInstance().qO();
                           }
                        }
                     }

                     var15.getModelInstance().method8908(var3);
                     var15.updateModelParts();
                     if (var15.isDormant()) {
                        var15.refreshAllPositions();
                     } else {
                        var15.refreshPosition();
                     }

                     if (var15.rq != null) {
                        for (EntityViewer var17 : var15.getViewers()) {
                           if (var17.kW()) {
                              var15.showViewer(var17);
                           } else {
                              var15.hideViewer(var17);
                           }
                        }
                     }

                     var15.captureAnimationTrack();
                     if (!var15.isDormant()) {
                        var15.pushMovementUpdate();
                     }

                     var15.tickMobAttribute();

                     for (EntityViewer var32 : var15.getViewers()) {
                        var32.method8558(var1);
                     }
                  }
               }
            }
         }

         if (var1 % 4L == 0L) {
            refreshViewerDistances(var12);
         }

         for (CustomEntityImpl var25 : var12) {
            if (!var25.removed && !var25.getViewers().isEmpty()) {
               var25.updateNametagPosition();
               var25.refreshNametagDisplay();
            }
         }
      } catch (Throwable var22) {
         var22.printStackTrace();
      }

      op = (int)var1;
      if (var5 != null) {
         long var24 = System.nanoTime() - var6;
         long var31 = var8 < 0L ? -1L : getThreadCpuTime() - var8;
         long var18 = var10 < 0L ? -1L : getThreadAllocatedBytes() - var10;
         var5.method8368(var24, var31, var18);
         long var20 = var3 - 1L;
         if (var20 > 0L) {
            var5.pr.increment();
            var5.ps.add(var20);
         }
      }
   }

   static void refreshViewerDistances(List var0) {
      for (CustomEntityImpl var2 : var0) {
         if (!var2.removed && !var2.getViewers().isEmpty()) {
            for (EntityViewer var4 : var2.getViewers()) {
               double var5 = var2.tc * var2.tc;
               if (!(var4.uR > var5) && (!var2.getModel().AN || var4.kW())) {
                  var2.showViewer(var4);
               } else {
                  var2.hideViewer(var4);
               }
            }
         }
      }
   }

   public void addViewerToWorld(Player var1) {
      ArrayList var2 = (ArrayList)this.oV.get(var1.getWorld());
      if (var2 != null) {
         for (int var3 = 0; var3 < var2.size(); var3++) {
            CustomEntityImpl var4 = (CustomEntityImpl)var2.get(var3);
            if (!var4.qT) {
               var4.addViewer(var1, true);
            }
         }
      }
   }

   public void teleportViewers(Player var1, Location var2) {
      ArrayList var3 = (ArrayList)this.oV.get(var2.getWorld());
      if (var3 != null) {
         for (CustomEntityImpl var5 : var3) {
            if (var5.getModel().AN) {
               for (EntityViewer var7 : var5.getViewers()) {
                  if (var7.li == var1) {
                     var7.teleport(var2);
                  }
               }
            }
         }
      }
   }

   public void removeViewerFrom(Player var1, CustomEntityImpl var2) {
      var2.scheduleViewerRemoval(var1);
   }

   public void removeViewerFromAll(Player var1) {
      for (int var2 = 0; var2 < this.field102.size(); var2++) {
         CustomEntityImpl var3 = (CustomEntityImpl)this.field102.get(var2);
         var3.scheduleViewerRemoval(var1);
      }
   }

   public void clearAll() {
      for (CustomEntityImpl var2 : List.copyOf(this.field102)) {
         this.method8300(var2);
      }

      this.field102.clear();
      this.oP = List.of();
      this.oT.clear();
      this.oU.clear();
      this.oV.clear();
      this.oW.clear();
      this.oZ.clear();
      this.pe.clear();
      this.pf.clear();
      this.pg.clear();
      this.oN.clear();
   }

   public void removeWorldEntities(World var1) {
      ArrayList var2 = (ArrayList)this.oV.get(var1);
      if (var2 != null) {
         for (CustomEntityImpl var4 : List.copyOf(var2)) {
            this.removeEntityInstance(var4);
         }
      }
   }

   public void activateWorldViewers(Player var1) {
      ArrayList var2 = (ArrayList)this.oV.get(var1.getWorld());
      if (var2 != null) {
         for (int var3 = 0; var3 < var2.size(); var3++) {
            CustomEntityImpl var4 = (CustomEntityImpl)var2.get(var3);
            var4.activateViewer(var1);
            var4.sendRemovePacketTo(var1);
         }
      }
   }

   public void deactivateWorldViewers(World var1, Player var2) {
      ArrayList var3 = (ArrayList)this.oV.get(var1);
      if (var3 != null) {
         for (int var4 = 0; var4 < var3.size(); var4++) {
            CustomEntityImpl var5 = (CustomEntityImpl)var3.get(var4);
            if (!var5.qT) {
               var5.scheduleViewerRemoval(var2);
            } else {
               var5.deactivateViewer(var2);
            }
         }
      }
   }

   public boolean isCustomEntityLoaded(Entity var1) {
      return this.oT.containsKey(var1.getUniqueId());
   }

   public boolean isCustomEntityUuid(UUID var1) {
      return this.oT.containsKey(var1);
   }

   @Nullable
   public CustomEntityImpl findNearestEntity(Location var1) {
      ArrayList var2 = (ArrayList)this.oV.get(var1.getWorld());
      if (var2 == null) {
         return null;
      }

      double var3 = 200.0;
      CustomEntityImpl var5 = null;

      for (int var6 = 0; var6 < var2.size(); var6++) {
         CustomEntityImpl var7 = (CustomEntityImpl)var2.get(var6);
         double var8 = var7.getLocation().distance(var1);
         if (!(var8 > var3)) {
            var3 = var8;
            var5 = var7;
         }
      }

      return var5;
   }

   public static boolean isTaggedCustomEntity(Entity var0) {
      return var0.getPersistentDataContainer().has(oB, PersistentDataType.STRING);
   }

   public CustomMobSettings getSettings(String var1) {
      return (CustomMobSettings)this.oN.get(var1);
   }

   @Nullable
   public CustomEntityImpl getByUuid(UUID var1) {
      return (CustomEntityImpl)this.oT.get(var1);
   }

   @Nullable
   public CustomEntityImpl getByEntity(Entity var1) {
      return var1 != null ? (CustomEntityImpl)this.oT.get(var1.getUniqueId()) : null;
   }

   public CustomEntityImpl getTargetEntity(Player var1) {
      RayTraceResult var3 = var1.getWorld()
         .rayTraceEntities(
            var1.getEyeLocation(),
            var1.getEyeLocation().getDirection(),
            5.0,
            0.1,
            var2x -> var2x.getEntityId() != var1.getEntityId() && this.oT.containsKey(var2x.getUniqueId())
         );
      if (var3 != null && var3.getHitEntity() != null) {
         Entity var2 = var3.getHitEntity();
         return (CustomEntityImpl)this.oT.get(var2.getUniqueId());
      } else {
         return null;
      }
   }

   @Nullable
   public CustomEntityImpl getById(int var1) {
      return (CustomEntityImpl)this.oU.get(var1);
   }

   public boolean hasEntityId(int var1) {
      return this.oU.containsKey(var1);
   }

   @Nullable
   public CustomEntityImpl getLifecycleById(int var1) {
      return (CustomEntityImpl)this.oW.get(var1);
   }

   @Nullable
   public EntityViewerKey getViewerKey(int var1) {
      return (EntityViewerKey)this.oZ.get(var1);
   }

   public ImmutableList getAllEntities() {
      return ImmutableList.copyOf(this.field102);
   }

   public void removeEntity(CustomEntityImpl var1) {
      LivingEntity var2 = var1.getBukkitEntity();
      this.unregisterIds(var1, var2 == null ? null : var2.getUniqueId());
   }

   public void unregisterIds(CustomEntityImpl var1, @Nullable UUID var2) {
      for (int var6 : var1.getLifecycleIds()) {
         this.oW.remove(var6);
         this.clearViewerKey(var6, var2);
      }
   }

   public void removeEntityByBukkit(Entity var1) {
      if (var1 != null) {
         CustomEntityImpl var2 = this.getByUuid(var1.getUniqueId());
         if (var2 != null) {
            this.method8300(var2);
         } else {
            UUID var3 = var1.getUniqueId();
            int var4 = var1.getEntityId();
            this.oT.remove(var3);
            this.oU.remove(var4);
            this.clearViewerKey(var4, var3);
         }
      }
   }

   private void clearViewerKey(int var1, @Nullable UUID var2) {
      if (var2 != null) {
         this.oZ.computeIfPresent(var1, (var1x, var2x) -> (EntityViewerKey)(var2x.uuid().equals(var2) ? null : var2x));
      }
   }

   public void removeEntityInstance(CustomEntityImpl var1) {
      if (var1 != null) {
         this.method8300(var1);
      }
   }

   private void method8300(CustomEntityImpl var1) {
      if (!var1.removed) {
         if (var1.rs != null) {
            for (CustomEntityImpl var3 : List.copyOf(var1.rs)) {
               this.method8300(var3);
            }

            var1.rs = null;
         }

         if (var1.rq != null) {
            CustomEntityImpl var9 = var1.rq.xp.Hm;
            if (var9.rs != null) {
               var9.rs.remove(var1);
            }
         }

         LivingEntity var10 = var1.getBukkitEntity();
         UUID var11 = var10 == null ? null : var10.getUniqueId();
         int var4 = var10 == null ? -1 : var10.getEntityId();
         World var5 = var1.rt;
         var1.resetSensitivity();
         var1.disposeBodyRotation();
         var1.closeModelController();
         var1.clearBossBar();
         var1.clearNametagLine();
         var1.markRemoved();
         synchronized (this.field102) {
            this.field102.remove(var1);
            this.oP = List.copyOf(this.field102);
         }

         ArrayList var12 = (ArrayList)this.oV.get(var5);
         if (var12 != null) {
            var12.remove(var1);
         }

         new HashMap<LivingEntity, ModelRiderController>(this.pe).forEach((var2, var3x) -> {
            if (var3x.getCustomEntity() == var1) {
               var3x.doUnmount(false, true);
               this.pe.remove(var2, var3x);
            }
         });
         if (var11 != null) {
            this.oT.remove(var11);
            this.oU.remove(var4);
            this.clearViewerKey(var4, var11);
         }

         NativePacketsBridge var7 = NativePacketsHolder.aN() ? NativePacketsHolder.PE() : null;
         this.pk.submit(() -> var1.cleanupLifecycle(this, var11, var7));
      }
   }

   public void removeWithEffects(CustomEntityImpl var1, boolean var2, boolean var3) {
      Player var4 = null;
      if (var1.getBukkitEntity() instanceof Player) {
         PlayerActionBar var5 = (PlayerActionBar)this.pf.get(var1.getBukkitEntity().getUniqueId());
         if (var5 != null) {
            var5.stop();
            this.pf.remove(var1.getBukkitEntity().getUniqueId());
            var4 = var5.player;
         }
      }

      var1.removeWithParticles(var2, var3);
      this.removeEntityInstance(var1);
      if (var4 != null && var4.isOnline()) {
         ProjectileTypeHelper.getAdapter().cr(var4);
      }
   }

   public void method8302(PlayerActionBar var1, boolean var2, boolean var3) {
      this.removeWithEffects(var1.renderer, var2, var3);
   }

   public void killAndRemove(CustomEntityImpl var1) {
      this.removeEntityInstance(var1);
      var1.killEntity();
   }

   public boolean mountRider(LivingEntity var1, ModelRiderController var2) {
      if (this.isRidden(var1) || var2.isMounted() || var2.getCustomEntity().removed) {
         return false;
      }

      if (!var2.canMount(var1)) {
         return false;
      }

      this.pe.put(var1, var2);
      return true;
   }

   public boolean mountOnEntity(LivingEntity var1, CustomEntityImpl var2) {
      if (!this.isRidden(var1) && !var2.removed) {
         ModelRiderController var3 = var2.getFreeRider();
         if (var3 == null) {
            return false;
         }

         if (!var3.canMount(var1)) {
            return false;
         }

         this.pe.put(var1, var3);
         return true;
      } else {
         return false;
      }
   }

   public void removePassenger(LivingEntity passenger) {
      ModelRiderController var2 = (ModelRiderController)this.pe.get(passenger);
      if (var2 != null) {
         if (var2.tryUnmount()) {
            this.pe.remove(passenger, var2);
         }
      }
   }

   public void unmountRider(ModelRiderController var1) {
      if (var1 != null && var1.MZ != null) {
         LivingEntity var2 = var1.MZ;
         if (var1.tryUnmount()) {
            this.pe.remove(var2, var1);
         }
      }
   }

   public void unmountAllRiders(CustomEntityImpl var1) {
      for (ModelRiderController var3 : var1.rP) {
         this.unmountRider(var3);
      }
   }

   public boolean isRidden(LivingEntity var1) {
      return this.pe.containsKey(var1);
   }

   public boolean isRiderOf(LivingEntity var1, CustomEntityImpl var2) {
      ModelRiderController var3 = (ModelRiderController)this.pe.get(var1);
      return var3 == null ? false : var3.MY.Hm == var2;
   }

   @Nullable
   public ModelRiderController getRiderOf(LivingEntity var1) {
      return (ModelRiderController)this.pe.get(var1);
   }

   @Nullable
   public CustomEntityImpl spawnPlayerActor(@Nullable LivingEntity var1, String var2, Location var3, String var4, @Nullable String var5) {
      CustomEntityImpl var6 = new CustomEntityImpl((CustomMobSettings)this.oN.get(var4), false);
      var6.method8502(true);
      if (var5 != null && var6.getModelDefinition(var5) == null) {
         return null;
      }

      var6.addViewersInWorld(var3.getWorld());
      var6.setPlayerSkinRaw(var6.qK.vn.AN && var1 instanceof Player var7 ? AbstractFakeEntity.getSkinUrl(var7) : var2);
      var6.setIdleAnimation(null);
      var6.setWalkAnimation(null);
      var6.setFlyAnimation(null);
      var6.setDeathAnimation(null);
      if (var1 == null) {
         var6.spawnEntity(var3);
         var6.sendRemovePackets();
         this.registerEntity(var6);
      } else {
         var6.attachToEntity(var1, false);
         this.registerEntity(var6);
      }

      return var6;
   }

   @Nullable
   public CustomEntityImpl method8312(Player var1) {
      PlayerActionBar var2 = this.method8314(var1);
      return var2 == null ? null : var2.renderer;
   }

   public boolean method8313(UUID var1) {
      return this.pf.containsKey(var1);
   }

   @Nullable
   public PlayerActionBar method8314(Player var1) {
      return (PlayerActionBar)this.pf.get(var1.getUniqueId());
   }

   public EmoteResult method8315(Player var1, @NotNull String var2) {
      if (!EventRegistrar.callEvent(new PlayerEmotePlayEvent(var1, var2))) {
         return EmoteResult.CANCELLED;
      }

      NullableBox var3 = NullableBox.empty();
      ModelDefinitionData var4 = this.findEmoteDefinition(var2, var3);
      if (var4 == null || !var3.isPresent()) {
         return EmoteResult.UNKNOWN_EMOTE;
      }

      if (!var1.isOnGround() && var4.zJ) {
         return EmoteResult.CANCELLED;
      }

      PlayerActionBar var5 = this.method8314(var1);
      if (var5 != null) {
         this.method8327(var1);
      }

      CustomEntityImpl var6 = this.spawnPlayerActor(var1, var1.getName(), var1.getLocation(), ((CustomEntityModel)var3.value).getNamespacedID(), var2);
      if (var6 == null) {
         return EmoteResult.UNKNOWN_EMOTE;
      }

      PlayerActionBar var7 = new PlayerActionBar(var1, var6, true);
      var1.getPersistentDataContainer().remove(oE);
      this.pf.put(var1.getUniqueId(), var7);
      if (var6.rQ.values().stream().anyMatch(var0 -> var0.Hn instanceof CachedDisplayEntity)) {
         String var8 = var6.getPlayerSkin();
         CompletableFuture var9 = CachedDisplayEntity.fetchTextureAsync(var8);
         SchedulerUtils.submitAfterSleep(() -> var9.whenComplete((var6xx, var7x) -> {
            if (var7x != null) {
               Log.error("Error preparing emote skin.", var7x);
               SchedulerUtils.runOnMainThread(() -> {
                  if (this.method8314(var1) == var7) {
                     this.method8302(var7, false, false);
                  }
               });
            } else {
               var6.dispatchToManager(() -> {
                  if (!var7.stopped.get()) {
                     boolean var7xx = true;

                     for (ModelPartDisplay var9x : var6.rQ.values()) {
                        if (var9x.Hn instanceof CachedDisplayEntity var10) {
                           var7xx &= var10.method3122(var8, var6xx);
                        }
                     }

                     var6.refreshPassengerPackets();
                     boolean var12 = var7xx;
                     SchedulerUtils.runOnMainThread(() -> {
                        if (this.method8314(var1) == var7) {
                           if (var1.isOnline() && var8.equals(var6.getPlayerSkin()) && var12) {
                              var6.playAnimationInternal(var2, 0, false, () -> SchedulerUtils.runOnMainThread(() -> this.method8330(var7)));
                              var7.start();
                           } else {
                              this.method8302(var7, false, false);
                           }
                        }
                     });
                  }
               });
            }
         }), 5);
      } else {
         SchedulerUtils.submitAfterSleep(() -> {
            try {
               if (var7.stopped.get()) {
                  return;
               }

               var7.start();
            } catch (Exception var2x) {
               Log.error("Error starting emote.", var2x);
            }
         }, 5);
         var6.playAnimationInternal(var2, 0, false, () -> SchedulerUtils.runOnMainThread(() -> this.method8330(var7)));
      }

      return EmoteResult.PLAY;
   }

   public boolean stopEmote(Player var1) {
      var1.getPersistentDataContainer().remove(oE);
      PlayerActionBar var2 = this.method8314(var1);
      if (var2 != null) {
         this.method8327(var1);
         getInstance().removeWithEffects(var2.renderer, false, false);
         this.pf.remove(var1.getUniqueId());
         return true;
      } else {
         return false;
      }
   }

   public boolean startEmoteActor(Player var1, String var2) {
      if (!this.oN.containsKey(var2)) {
         return false;
      }

      PlayerActionBar var3 = this.method8314(var1);
      if (var3 != null) {
         this.method8327(var1);
         getInstance().removeWithEffects(var3.renderer, false, false);
      }

      ModelPartController.an(var1);
      CustomEntityImpl var4 = getInstance().attachModelWithSkin(var2, null, var1, false, true, false, false);
      if (var4 == null) {
         return false;
      }

      var4.method8503(true);
      PlayerActionBar var5 = new PlayerActionBar(var1, var4, false);
      this.pf.put(var1.getUniqueId(), var5);
      var1.getPersistentDataContainer().set(oE, PersistentDataType.STRING, var2);
      var5.start();
      return true;
   }

   public void restoreEmote(Player var1) {
      if (var1.isOnline() && Settings.get().customEntitiesEnabled) {
         String var2 = (String)var1.getPersistentDataContainer().get(oE, PersistentDataType.STRING);
         if (var2 != null && this.oN.containsKey(var2)) {
            this.startEmoteActor(var1, var2);
         }
      }
   }

   @Nullable
   private ModelDefinitionData findEmoteDefinition(String var1, NullableBox var2) {
      CustomMobSettings var3 = (CustomMobSettings)this.pc.get(var1);
      if (var3 == null) {
         return null;
      }

      var2.set(var3.vn);
      return (ModelDefinitionData)var3.vn.AK.get(var1);
   }

   public static ToggleState method8320(CustomEntityImpl var0, String var1) {
      CustomMobSettings var2 = (CustomMobSettings)getInstance().getEmoteOwners().get(var1);
      if (var2 == null) {
         return new ToggleState(false, false, null);
      } else {
         String var3 = var2.vn.getNamespacedID();
         if (var3.equals(var0.getNamespacedId())) {
            var0.playAnimationInternal(var1, 0, false, () -> returnToIdle(var0));
            return new ToggleState(true, false, null);
         } else {
            CustomEntityImpl var4 = spawnEmoteActor(var3, var0.getPlayerSkin(), var0.getBukkitEntity());
            var4.playAnimationInternal(var1, 0, false, () -> returnToIdle(var4));
            return new ToggleState(true, true, var3);
         }
      }
   }

   private static void returnToIdle(CustomEntityImpl var0) {
      if (!isDefaultEntity(var0)) {
         SchedulerUtils.runLaterSync(() -> {
            if (!isDefaultEntity(var0)) {
               String var1 = var0.getPlayerSkin();
               LivingEntity var2 = var0.getBukkitEntity();
               CustomEntityImpl var3 = spawnEmoteActor(Settings.get().emotesUseArmorStands ? "_iainternal:player" : "_iainternal:player_v2", var1, var2);
               var3.playAnimation("idle");
            }
         }, 1L);
      } else {
         var0.playAnimation("idle");
      }
   }

   private static CustomEntityImpl spawnEmoteActor(String var0, @Nullable String var1, LivingEntity var2) {
      CustomEntityImpl var3 = getInstance().attachModel(var0, var2, true);
      if (var1 != null) {
         var3.changePlayerSkin(var1);
      }

      var3.initBodyRotation();
      var3.applySensitivity();
      return var3;
   }

   public CustomMobSettings getDefaultPlayerSettings() {
      return (CustomMobSettings)this.oN.get(Settings.get().emotesUseArmorStands ? "_iainternal:player" : "_iainternal:player_v2");
   }

   public List getEmoteNames() {
      return new ArrayList(this.pa);
   }

   public boolean matchesEmoteSearch(String var1, String var2) {
      return containsSearchTerm(var1, var2, this.pb.getOrDefault(var1, List.of()));
   }

   static boolean containsSearchTerm(String var0, String var1, List var2) {
      return var0.toLowerCase(Locale.ROOT).replace("-", "").contains(var1)
         ? true
         : var2.stream().anyMatch(var1x -> var1x.toLowerCase(Locale.ROOT).replace("-", "").contains(var1));
   }

   public List listSettingsNames(boolean var1) {
      ArrayList var2 = new ArrayList();

      for (Entry var4 : getInstance().oN.entrySet()) {
         if (var1 || !((CustomMobSettings)var4.getValue()).vn.AM) {
            var2.add((String)var4.getKey());
         }
      }

      return var2;
   }

   public void rebuildEmoteIndex() {
      ArrayList var1 = new ArrayList();
      this.pc.clear();
      this.pb.clear();
      this.pd.clear();
      CustomMobSettings var2 = getInstance().getDefaultPlayerSettings();
      if (var2 != null) {
         var1.addAll(var2.vn.getAnimationsNames());
         var1.remove("idle");
         var1.remove("death_ragdoll_pose");
      }

      for (Entry var4 : this.oN.entrySet()) {
         CustomMobSettings var5 = (CustomMobSettings)var4.getValue();
         if (var5.vn.AM && var5.vn.AN != Settings.get().emotesUseArmorStands) {
            var5.vn.AK.forEach((var1x, var2x) -> {
               if (!var1x.equals("idle") && !var1x.equals("death_ragdoll_pose")) {
                  var1.add(var1x);
               }
            });
            this.pd.add(var5.vn.getNamespacedID());
            var5.vn.AK.forEach((var2x, var3) -> {
               if (!var2x.equals("idle") && !var2x.equals("death_ragdoll_pose")) {
                  this.pc.put(var2x, var5);
               }
            });
         }
      }

      this.pa = var1;
   }

   public List getPlayerModelIds() {
      return this.pd;
   }

   public HashMap getEmoteOwners() {
      return this.pc;
   }

   public static boolean isDefaultEntity(CustomEntityImpl var0) {
      return var0.getNamespacedId().equals("_iainternal:player") || var0.getNamespacedId().equals("_iainternal:player_v2");
   }

   public boolean method8327(Player var1) {
      PlayerActionBar var2 = this.method8314(var1);
      if (var2 == null) {
         return false;
      }

      EventRegistrar.callEvent(new PlayerEmoteEndEvent(var1, var2.renderer.rC.bm(null), Cause.STOP));
      ModelDefinitionData var3 = var2.renderer.rC.EU;
      if (var3 != null && var3.zK) {
         for (List var5 : var3.zw) {
            for (ParticleOffsetEmitter var7 : var5) {
               var7.method8889(var1.getLocation());
            }
         }
      }

      this.method8302(var2, false, false);
      return true;
   }

   public void method8328(Player var1) {
      PlayerActionBar var2 = this.method8314(var1);
      if (var2 != null) {
         this.method8329(var2);
      }
   }

   public void method8329(PlayerActionBar var1) {
      Player var2 = var1.player;
      ModelDefinitionData var3 = var1.renderer.rC.EU;
      EventRegistrar.callEvent(new PlayerEmoteEndEvent(var2, var1.renderer.rC.bm(null), Cause.STOP));
      if (var3 != null) {
         if (var3.zK) {
            for (List var5 : var3.zw) {
               for (ParticleOffsetEmitter var7 : var5) {
                  var7.method8889(var2.getLocation());
               }
            }
         }

         if (var3.zL) {
            this.method8302(var1, false, false);
         }
      }

      if (var3 == null || var3.zG) {
         var1.renderer.getModelInstance().method8911(false, false);
      }
   }

   private void method8330(PlayerActionBar var1) {
      if (!var1.renderer.removed) {
         EventRegistrar.callEvent(new PlayerEmoteEndEvent(var1.player, var1.renderer.rC.bm(null), Cause.FINISHED));
         this.removeWithEffects(var1.renderer, true, false);
      }
   }

   public static BufferedImage getSteveSkinImage() {
      if (pm != null) {
         return pm;
      }

      try {
         String var0 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAMAAACdt4HsAAAAaVBMVEUAAACqcllBNZs6MYmbY0kAr69GOqUrHg0ApKQzJBEElZWQWT8FiIgAf39KSkokGAiBUzk3NzcDeno/KhUKvLw/Pz92SzMAaGiPXj4oKChHIg4AzMy0e2GUYD7///9SPYlVVVV3QjVqQDDj1w1CAAAAAXRSTlMAQObYZgAAAsZJREFUWMPtlt2WojAQhA00kpAo4M+IjjuzM+//kFvVIcvq0RN0LtcihIZjfXQnCFkk1bVDc3U3avGoardFI+ANegYAMzf/pvIPA+h22J8H1LEIH0t4HOAch3G77XwH+eFxAMcAGjrdnpkFx3Gsu+EEdcN8I50t+y0EAk4cD06jOg/gD1vn0ODn1hLHIeX17azaa3ZyPp+NOIaoxWkaoGYBmjHuKQZ+Y0RdsQwWkgfEx09a8/Fp5PPDtOL1kmsVlAc4/lYgY76/jWHUav5tS0h+ENu69TJ0v/xw+vo6IegG8bhIQD0H0Hn4hcY4/34QEPg4ttDMf2UJ9f3xmM7fjQBr3stR6XpVVRalmpuA47Hv/wLEQ3ITYAC/m0EWsAMgn8H9EixLuM6gKMpiuVyWOBQIygbCfUSMrWJs0cNaIcJhBfFkApQTYAk1ITRV1RjDnjGdVfIDcDisEE6A0VgkQNhsgoWFHWMSyIKJkIYAwm4D0DZQMjG2dGFfjTpAPF4AWAYJJURTsAoIjBkSIiKemwonlxmgEcKdaQdrFTCWoAnxn2oEHdwGugRA6RggpozbhhTzlC4iFIA2AaAESLMQmDamj7ECLAEqLeQig1KNxCCCmtCgWU1CIwTwB+avALkCrNflqLUKLjJ0HCm64Q/MPxEuAP0ert+07hnu42QgnwAbA1SEYBkBEgHyL6Dv9zTuewj9+EhjWkKI4wIWenjo1LlksHjpv9QuqrJQhWDx0ktXyn5s+U3i64jxUwsOArwY84MMdpWBfpKB9TKrhLReKIr00V1B6dNO5QHJWQIE6edcCdrPA5QEFBOgiWa2+RkoAFFaDySN74OkCXhvveAp4aby2QxohjQDxgJrXBpEPVYCRIDaSUE0fxAhArwCqEiZ8RClJyHWorWrPQO4t15IftozgNvrBQVIAkgecL1e4AwSkdYHiyv9AfNfNne5fuGHAAAAAElFTkSuQmCC"
            .split(",")[1];
         byte[] var1 = Base64.getDecoder().decode(var0);
         pm = ImageIO.read(new ByteArrayInputStream(var1));
         return pm;
      } catch (IOException var2) {
         Log.error("Failed to load Steve skin image.", var2);
         return null;
      }
   }

   static {
      on = om instanceof com.sun.management.ThreadMXBean var0 ? var0 : null;
   }
}
