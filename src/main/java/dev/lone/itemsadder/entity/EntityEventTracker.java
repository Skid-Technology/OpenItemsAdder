package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.util.DoubleVector3Tagged;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public final class EntityEventTracker implements AutoCloseable, Listener {
   public static final int QX = 3;
   public static final int QY = 1600;
   public static final int QZ = 256;
   public static final int Ra = 700;
   private static final int Rb = 8;
   private static final double Rc = 0.01;
   private static final int Rd = 8;
   private static final Map Re = Map.of(
      "head",
      "head",
      "body",
      "body",
      "right_arm",
      "left_arm",
      "left_arm",
      "right_arm",
      "right_leg",
      "left_leg",
      "left_leg",
      "right_leg",
      "head_content",
      "head"
   );
   private static final DoubleVector3Tagged Rf = new DoubleVector3Tagged(0.1, Math.toRadians(10.0), 0.01, 2);
   private static final Map Rg = Map.of(
      "head",
      "head",
      "body",
      "body",
      "right_hind_leg",
      "right_hind_leg",
      "left_hind_leg",
      "left_hind_leg",
      "right_front_leg",
      "right_front_leg",
      "left_front_leg",
      "left_front_leg",
      "head_content",
      "head"
   );
   private static final DoubleVector3Tagged Rh = new DoubleVector3Tagged(0.1, 0.01, 0.01, 2);
   @Nullable
   private static EntityEventTracker Ri;
   private final Map Rj = new LinkedHashMap();
   private final Map Rk = new LinkedHashMap();
   @Nullable
   private BukkitTask mS;

   public static EntityEventTracker getInstance() {
      if (Ri == null) {
         Ri = new EntityEventTracker();
         Bukkit.getPluginManager().registerEvents(Ri, Main.getInstance());
      }

      return Ri;
   }

   public static void uq() {
      if (Ri != null) {
         Ri.close();
      }
   }

   private EntityEventTracker() {
   }

   public ScenarioMessage createMessage(Player var1, String var2, String var3, String var4, boolean var5) {
      if (var1.hasPermission("ia.admin") && var1.hasPermission("ia.admin.iadebug")) {
         if (var1.getGameMode() != GameMode.CREATIVE && var1.getGameMode() != GameMode.SPECTATOR) {
            return ScenarioMessage.of("Use Creative or Spectator mode so comparison AI cannot target the observer.");
         }

         boolean var6 = "pig".equalsIgnoreCase(var3) || "minecraft:pig".equalsIgnoreCase(var3);
         boolean var7 = "zombie".equalsIgnoreCase(var3) || "minecraft:zombie".equalsIgnoreCase(var3);
         if (!var6 && !var7) {
            return ScenarioMessage.of("Unsupported vanilla type '" + var3 + "'. Validated presets: zombie, pig.");
         }

         if (var6 == "navigation".equalsIgnoreCase(var4) && var7 == "combat".equalsIgnoreCase(var4)) {
            CustomMobSettings var8 = CustomEntityManager.getInstance().getSettings(var2);
            if (var8 == null) {
               return ScenarioMessage.of("Unknown V2 model: " + var2);
            }

            EntityType var9 = var6 ? EntityType.PIG : EntityType.ZOMBIE;
            if (var8.vw == var9 && var8.vn.AN && !var8.vn.AO) {
               MobScenario var10 = this.getScenario(var1.getUniqueId(), null);
               if (var10 != null && var10.isActive()) {
                  return ScenarioMessage.of("You already own running session " + var10.Si + ". Stop it first.");
               }

               if (this.Rj.values().stream().filter(MobScenario::isActive).count() >= 3L) {
                  return ScenarioMessage.of("The comparison session limit (3) is reached.");
               }

               if (var7 && var1.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
                  return ScenarioMessage.of("Combat comparison requires a non-peaceful world.");
               }

               RouteSegment var11 = getRouteSegment(var1);
               String var12 = method1842(var11);
               if (var12 != null) {
                  return ScenarioMessage.of(var12);
               }

               UUID var13 = UUID.randomUUID();
               MobScenario var14 = new MobScenario(this, var13, var1, var2, var5, var11, var6);
               this.Rj.put(var13, var14);

               try {
                  var14.vc();
                  this.ur();
                  this.us();
                  return new ScenarioMessage(true, var13, "Started " + var4.toLowerCase(Locale.ROOT) + " comparison " + var13 + ".");
               } catch (RuntimeException var16) {
                  var14.bu("startup failed: " + var16.getMessage());
                  return ScenarioMessage.of("Comparison startup failed: " + var16.getMessage());
               }
            } else {
               return ScenarioMessage.of(var2 + " is not a native V2 " + var9.name().toLowerCase(Locale.ROOT) + " model.");
            }
         } else {
            return ScenarioMessage.of("Unsupported preset/scenario pair. Use zombie combat or pig navigation.");
         }
      } else {
         return ScenarioMessage.of("This command requires ia.admin and ia.admin.iadebug.");
      }
   }

   @Nullable
   public MobScenarioSnapshot snapshotOf(UUID var1, @Nullable String var2) {
      MobScenario var3 = this.getScenario(var1, var2);
      return var3 == null ? null : var3.snapshot();
   }

   public List method1839(UUID var1, @Nullable String var2) {
      MobScenario var3 = this.getScenario(var1, var2);
      return var3 == null ? List.of("No matching comparison session.") : var3.vm();
   }

   public boolean hasScenario(UUID var1, @Nullable String var2) {
      MobScenario var3 = this.getScenario(var1, var2);
      if (var3 == null) {
         return false;
      }

      var3.method9155("stopped by owner");
      return true;
   }

   @Nullable
   private MobScenario/* @Nullable */ getScenario(UUID var1, @Nullable String var2) {
      if (var2 == null) {
         return this.Rj.values().stream().filter(var1x -> var1x.Sj.equals(var1)).max(Comparator.comparingLong(var0 -> var0.Sp)).orElse(null);
      }

      try {
         MobScenario var3 = (MobScenario)this.Rj.get(UUID.fromString(var2));
         return var3 != null && var3.Sj.equals(var1) ? var3 : null;
      } catch (IllegalArgumentException var4) {
         return null;
      }
   }

   private void ur() {
      if (this.mS == null) {
         this.mS = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this::tick, 1L, 1L);
      }
   }

   private void tick() {
      boolean var1 = false;

      for (MobScenario var3 : List.copyOf(this.Rj.values())) {
         if (var3.isActive()) {
            var1 = true;

            try {
               var3.tick();
            } catch (RuntimeException var5) {
               var3.bu("scenario exception: " + var5.getClass().getSimpleName() + ": " + var5.getMessage());
            }
         }
      }

      if (!var1 && this.mS != null) {
         this.mS.cancel();
         this.mS = null;
      }
   }

   private static RouteSegment getRouteSegment(Player var0) {
      Vector var1 = var0.getLocation().getDirection().setY(0);
      if (var1.lengthSquared() < 1.0E-4) {
         var1.setZ(1);
      }

      var1.normalize();
      Vector var2 = new Vector(-var1.getZ(), 0.0, var1.getX());
      Location var3 = var0.getLocation().clone().add(var1.clone().multiply(6));
      var3.setYaw((float)Math.toDegrees(Math.atan2(-var1.getX(), var1.getZ())));
      var3.setPitch(0.0F);
      return new RouteSegment(var3, var1, var2, var3.clone().add(var2.clone().multiply(-4)), var3.clone().add(var2.clone().multiply(4)));
   }

   @Nullable
   private static String method1842(RouteSegment var0) {
      for (Location var2 : List.of(var0.Sg, var0.Sh)) {
         for (int var3 = 0; var3 <= 20; var3++) {
            Location var4 = var2.clone().add(var0.Se.clone().multiply(var3));
            if (!var4.getChunk().isLoaded()) {
               return "The comparison corridor contains an unloaded chunk.";
            }

            if (!var4.clone().subtract(0.0, 1.0, 0.0).getBlock().getType().isSolid()
               || !var4.getBlock().isPassable()
               || !var4.clone().add(0.0, 1.0, 0.0).getBlock().isPassable()) {
               return "The comparison corridor needs flat solid floor and two blocks of clearance.";
            }
         }
      }

      return null;
   }

   private void applyScenario(MobScenario var1, Entity var2) {
      var1.Sq.add(var2.getUniqueId());
      this.Rk.put(var2.getUniqueId(), var1.Si);
   }

   @Nullable
   private MobScenario/* @Nullable */ findScenario(Entity var1) {
      UUID var2 = (UUID)this.Rk.get(var1.getUniqueId());
      return var2 == null ? null : (MobScenario)this.Rj.get(var2);
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
   private void onEntityDamage(EntityDamageEvent var1) {
      MobScenario var2 = this.findScenario(var1.getEntity());
      if (var2 != null) {
         if (!(var1 instanceof EntityDamageByEntityEvent var3 && var2.method9151(var3.getDamager(), var1.getEntity()))) {
            var1.setCancelled(true);
            var2.method9153("blocked_damage", var1.getEntity(), null, var1.getFinalDamage(), true);
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
   private void onDamageByEntity(EntityDamageByEntityEvent var1) {
      MobScenario var2 = this.findScenario(var1.getDamager());
      MobScenario var3 = this.findScenario(var1.getEntity());
      if (var2 != null || var3 != null) {
         MobScenario var4 = var2 != null ? var2 : var3;
         boolean var5 = var2 != null && var2 == var3 && var4.method9151(var1.getDamager(), var1.getEntity());
         if (!var5) {
            var1.setCancelled(true);
            var4.method9153("blocked_interference", var1.getDamager(), var1.getEntity(), var1.getFinalDamage(), true);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
   private void onDamageByEntityDup2(EntityDamageByEntityEvent var1) {
      MobScenario var2 = this.findScenario(var1.getDamager());
      MobScenario var3 = this.findScenario(var1.getEntity());
      if (var2 != null && var2 == var3 && var2.method9151(var1.getDamager(), var1.getEntity())) {
         var2.method9152(var1.getDamager(), var1.getEntity(), var1.getFinalDamage(), var1.isCancelled());
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
   private void onCombust(EntityCombustEvent var1) {
      if (this.findScenario(var1.getEntity()) != null) {
         var1.setCancelled(true);
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
   private void onTransform(EntityTransformEvent var1) {
      MobScenario var2 = this.findScenario(var1.getEntity());
      if (var2 != null) {
         var1.setCancelled(true);
         var2.method9153("blocked_transform", var1.getEntity(), null, 0.0, true);
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
   private void onBreed(EntityBreedEvent var1) {
      if (this.findScenario(var1.getMother()) != null || this.findScenario(var1.getFather()) != null) {
         var1.setCancelled(true);
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
   private void onDropItem(EntityDropItemEvent var1) {
      if (this.findScenario(var1.getEntity()) != null) {
         var1.setCancelled(true);
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   private void onEntityDeath(EntityDeathEvent var1) {
      MobScenario var2 = this.findScenario(var1.getEntity());
      if (var2 != null) {
         var1.getDrops().clear();
         var1.setDroppedExp(0);
         var2.bu("session actor died: " + var1.getEntity().getUniqueId());
      }
   }

   @EventHandler
   private void onPlayerQuit(PlayerQuitEvent var1) {
      this.method1856(var1.getPlayer().getUniqueId(), "owner disconnected");
   }

   @EventHandler
   private void onWorldChange(PlayerChangedWorldEvent var1) {
      this.method1856(var1.getPlayer().getUniqueId(), "owner changed world");
   }

   @EventHandler
   private void onChunkUnload(ChunkUnloadEvent var1) {
      for (MobScenario var3 : List.copyOf(this.Rj.values())) {
         if (var3.isActive() && var3.isChunkLoaded(var1.getChunk())) {
            var3.method9155("actor chunk unloaded");
         }
      }
   }

   private void method1856(UUID var1, String var2) {
      this.Rj.values().stream().filter(var1x -> var1x.Sj.equals(var1) && var1x.isActive()).forEach(var1x -> var1x.method9155(var2));
   }

   private void us() {
      while (this.Rj.size() > 8) {
         MobScenario var1 = this.Rj.values().stream().filter(var0 -> !var0.isActive()).min(Comparator.comparingLong(var0 -> var0.Sp)).orElse(null);
         if (var1 == null) {
            return;
         }

         this.Rj.remove(var1.Si);
      }
   }

   @Override
   public void close() {
      for (MobScenario var2 : List.copyOf(this.Rj.values())) {
         var2.method9155("plugin disabled");
      }

      this.Rj.clear();
      this.Rk.clear();
      if (this.mS != null) {
         this.mS.cancel();
      }

      this.mS = null;
      HandlerList.unregisterAll(this);
      Ri = null;
   }

   private static boolean isTracked(@Nullable Entity var0) {
      return var0 != null && var0.isValid() && !var0.isDead();
   }

   private static String method1858(@Nullable Entity var0) {
      return var0 == null ? "none" : var0.getUniqueId().toString();
   }

   private static void method1859(@Nullable EntityEquipment var0) {
      if (var0 != null) {
         var0.clear();
         var0.setHelmetDropChance(0.0F);
         var0.setChestplateDropChance(0.0F);
         var0.setLeggingsDropChance(0.0F);
         var0.setBootsDropChance(0.0F);
         var0.setItemInMainHandDropChance(0.0F);
         var0.setItemInOffHandDropChance(0.0F);
      }
   }
}
