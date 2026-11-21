package dev.lone.itemsadder.block;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.ItemDisplayUtils;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;
import org.jctools.maps.NonBlockingHashMapLong;
import org.jetbrains.annotations.Nullable;

public final class StepSoundTracker implements Listener {
   public static int kL = 1400;
   private static int kM;
   private static final double kN = 0.25;
   public static int kO;
   public static boolean kP = true;
   public static int kQ = 7;
   private static final int kR = 1200;
   private static boolean kS;
   private static final int kT = 500;
   private static final double kU = 30.0;
   private static final int kV = 10;
   private static final double kW = 40.0;
   private static final int kX = 10;
   private static boolean kY;
   private static double kZ;
   private static double la;
   private static int lb;
   private final Map lc = new ConcurrentHashMap();
   private final BlockInteractionListener ld;
   private ScheduledExecutorService le;
   private BukkitTask lf;
   private int lg;
   private static Long lh = 0L;

   public StepSoundTracker(BlockInteractionListener var1) {
      this.ld = var1;
      EventRegistrar.registerListener(this, Main.getInstance());
   }

   public void applySettings() {
      Settings var1 = Settings.get();
      kL = var1.cropMaxShownPerPlayer;
      kP = var1.cropBillboard;
      kQ = var1.cropBillboardDistance;
      kS = var1.cropFovRenderLimit;
      kM = var1.cropTickInterval;
      kO = var1.cropRenderRadiusBlocks;
      kY = var1.cropCheckerboardPattern;
      kZ = var1.cropCheckerboardDistance;
      la = kZ * kZ;
      lb = Math.min(500, (int)(kL / 6.0F));
      if (this.le != null && !this.le.isShutdown()) {
         this.le.shutdownNow();
      }

      this.le = Executors.newSingleThreadScheduledExecutor(var0 -> {
         Thread var1x = new Thread(var0, "ItemsAdder-Crops-Stream-Tick");
         var1x.setDaemon(true);
         return var1x;
      });
      if (this.lf != null) {
         this.lf.cancel();
      }

      this.lf = SchedulerUtils.scheduleRepeatingSync(var1x -> this.tick(), 1L, 1L);
      Bukkit.getOnlinePlayers().forEach(this::registerPlayer);
   }

   public void stop() {
      if (this.lf != null) {
         this.lf.cancel();
         this.lf = null;
      }

      ScheduledExecutorService var1 = this.le;
      this.le = null;
      if (var1 != null) {
         var1.shutdown();

         try {
            if (!var1.awaitTermination(5L, TimeUnit.SECONDS)) {
               var1.shutdownNow();
               var1.awaitTermination(1L, TimeUnit.SECONDS);
            }
         } catch (InterruptedException var5) {
            Thread.currentThread().interrupt();
         }
      }

      for (Entry var3 : this.lc.entrySet()) {
         PlayerStepState var4 = (PlayerStepState)var3.getValue();
         var4.stopContinuousHarvest();
         var4.clearDisplays();
      }

      this.lc.clear();
   }

   public void tick() {
      int var1 = ++this.lg;
      int var2 = 0;

      for (Player var4 : Bukkit.getOnlinePlayers()) {
         PlayerStepState var5 = this.getState(var4);
         if (var5 != null) {
            var5.tickContinuousHarvest();
         }

         if (!BlockInteractionListener.isLightweightCrops(var4) && var5 != null) {
            this.ld.tickPlayerState(var4, var5);
            if (!var5.lQ.get() && !var5.lM.get()) {
               boolean var6 = var5.field48.get();
               boolean var7 = var5.lN.get() || var6;
               int var8 = var2++;
               if (var7 || var8 % kM == var1 % kM) {
                  Location var9 = var4.getLocation();
                  double var10 = var9.getY() - var4.getWorld().getHighestBlockYAt(var9);
                  boolean var12 = var5.lP.getAndSet(false);
                  if (var12 || var6 || !CustomBlockListener.dn.method719(var4) && var10 < 9.0) {
                     this.updatePlayerHitbox(var4, var12);
                  }

                  if (var5.lM.compareAndSet(false, true)) {
                     BlockDisplayRegistry var13 = this.ld.mZ.getOrCreateDisplayRegistry(var4);
                     if (var13 == null) {
                        var5.lM.set(false);
                     } else {
                        StepSoundRequest var14 = new StepSoundRequest(var4, var5, var13, var9, var4.getEyeLocation(), var10, var1, var8, var5.lN.get(), var5.lr);
                        if (!this.submitAsync(() -> {
                           boolean var3 = false;

                           try {
                              var3 = this.prepareStepUpdate(var14);
                           } finally {
                              if (!var3) {
                                 var5.lM.set(false);
                              }
                           }
                        })) {
                           var5.lM.set(false);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean submitAsync(Runnable var1) {
      ScheduledExecutorService var2 = this.le;
      if (var2 != null && !var2.isShutdown()) {
         try {
            var2.execute(() -> {
               try {
                  var1.run();
               } catch (Throwable var2x) {
                  var2x.printStackTrace();
               }
            });
            return true;
         } catch (RejectedExecutionException var4) {
            return false;
         }
      } else {
         return false;
      }
   }

   void registerPlayer(Player var1) {
      this.lc.putIfAbsent(var1.getUniqueId(), new PlayerStepState(var1));
   }

   @Nullable
   PlayerStepState getState(Player var1) {
      return (PlayerStepState)this.lc.get(var1.getUniqueId());
   }

   @Nullable
   PlayerStepState getStateForPlayer(UUID var1, Player var2) {
      PlayerStepState var3 = (PlayerStepState)this.lc.get(var1);
      return var3 != null && var3.lK == var2 ? var3 : null;
   }

   public void markNeedsUpdate(Player var1) {
      PlayerStepState var2 = this.getState(var1);
      if (var2 != null) {
         var2.lN.set(true);
      }
   }

   public void releaseChunkDisplays(Player var1, int var2, int var3, World var4) {
      this.submitAsync(() -> {
         BlockDisplayRegistry var5 = this.ld.mZ.getDisplayRegistry(var1);
         if (var5 != null) {
            var5.releaseChunkCoords(var2, var3, var4);
         }
      });
   }

   boolean releaseChunkDisplaysIf(Player var1, int var2, int var3, World var4, BooleanSupplier var5) {
      PlayerStepState var6 = this.getState(var1);
      long var7 = var6 == null ? 0L : var6.lr;
      return this.submitAsync(() -> {
         if (var5.getAsBoolean() && this.getState(var1) == var6 && (var6 == null || var6.lr == var7)) {
            BlockDisplayRegistry var9 = this.ld.mZ.getDisplayRegistry(var1);
            if (var9 != null) {
               var9.releaseChunkCoords(var2, var3, var4);
            }
         }
      });
   }

   public void resetPlayer(Player var1) {
      PlayerStepState var2 = this.getState(var1);
      if (var2 != null) {
         this.resetPlayerState(var1, var2);
      } else {
         this.submitAsync(() -> this.ld.mZ.releasePlayer(var1));
      }
   }

   private void resetPlayerState(Player var1, PlayerStepState var2) {
      long var3 = ++var2.lr;
      var2.lQ.set(true);
      if (!this.submitAsync(() -> {
         try {
            this.ld.mZ.releasePlayer(var1);
         } finally {
            if (var2.lr == var3) {
               var2.lQ.set(false);
               var2.lN.set(true);
            }
         }
      })) {
         this.ld.mZ.releasePlayer(var1);
         if (var2.lr == var3) {
            var2.lQ.set(false);
         }
      }
   }

   private boolean prepareStepUpdate(StepSoundRequest var1) {
      Player var2 = var1.player();
      PlayerStepState var3 = var1.state();
      int var4 = var1.playerIndex();
      boolean var5 = var3.lN.getAndSet(false) || var1.forceUpdate();
      if (var1.currentTick() % 1200 == 0) {
         var1.pool().purgeExpiredEntries();
      }

      double var6 = var1.distanceFromFloor();
      if (!var5 && var6 > 30.0 && var4 % 10 != var1.currentTick() % 10) {
         return false;
      }

      boolean var8 = var6 >= 9.0;
      Location var9 = var1.location();
      var3.field47.setWorld(var9.getWorld());
      var3.field47.setX(var9.getX());
      var3.field47.setY(var9.getY());
      var3.field47.setZ(var9.getZ());
      var3.field47.setYaw(var9.getYaw());
      var3.field47.setPitch(var9.getPitch());
      var3.lJ.setWorld(var9.getWorld());
      if (!var5 && !var8) {
         float var10 = var3.getLookSpeed();
         if (var10 > 15.0F) {
            if (var3.lY++ >= 5) {
               var3.lZ = 20;
               return false;
            }
         } else {
            var3.lY = 0;
         }

         if (var3.lZ > 0) {
            var3.lZ--;
            return false;
         }
      }

      if (var3.lL != 0L) {
         long var18 = var1.currentTick() - var3.lL;
         if (var18 > 0L && var3.lH.getWorld().equals(var3.field47.getWorld())) {
            double var12 = var3.lH.distanceSquared(var3.field47);
            double var14 = Math.sqrt(var12) / var18;
            if (var12 > 10000.0) {
               var3.lH.setX(var3.field47.getX());
               var3.lH.setY(var3.field47.getY());
               var3.lH.setZ(var3.field47.getZ());
            } else {
               double var16 = var14 * 20.0;
               if (!var5 && var16 > 40.0 && var4 % 10 != var1.currentTick() % 10) {
                  return false;
               }
            }
         }
      }

      int var19 = var3.field47.getBlockX() >> 4;
      int var11 = var3.field47.getBlockZ() >> 4;
      boolean var20 = var3.lH.getWorld() != var3.field47.getWorld() || var3.lL == 0L;
      boolean var13 = var20 || var3.lS != var19 || var3.lT != var11;
      float var21 = BlockFaceUtils.normalizeYaw(var3.field47.getYaw() - var3.lH.getYaw());
      float var15 = var3.field47.getPitch() - var3.lH.getPitch();
      boolean var22 = Math.abs(var21) >= 5.0F || Math.abs(var15) >= 5.0F;
      boolean var17 = var3.lH.getWorld() == var3.field47.getWorld() && var3.lH.distanceSquared(var3.field47) < 0.25;
      if (!var5 && kS) {
         if (!var13 && !var22) {
            return false;
         }
      } else if (!var5 && !var13 && var17) {
         return false;
      }

      var3.lS = var19;
      var3.lT = var11;
      var3.lH.setWorld(var3.field47.getWorld());
      var3.lH.setX(var3.field47.getX());
      var3.lH.setY(var3.field47.getY());
      var3.lH.setZ(var3.field47.getZ());
      var3.lH.setYaw(var3.field47.getYaw());
      var3.lH.setPitch(var3.field47.getPitch());
      var3.lL = var1.currentTick();
      SchedulerUtils.runOnMainThread(() -> this.scheduleMainThreadScan(var1));
      return true;
   }

   private void scheduleMainThreadScan(StepSoundRequest var1) {
      PlayerStepState var2 = var1.state();
      Player var3 = var1.player();
      if (this.lc.get(var3.getUniqueId()) == var2 && var3.isOnline() && var3.getWorld() == var1.location().getWorld() && var2.lr == var1.lifecycleGeneration()) {
         ObjectArrayList var4 = new ObjectArrayList();
         this.ld.mZ.collectChunkStates(var1.location(), kO, var4);
         if (!this.submitAsync(() -> this.applyDisplayUpdates(var1, var4))) {
            var2.lM.set(false);
         }
      } else {
         var2.lM.set(false);
      }
   }

   private void applyDisplayUpdates(StepSoundRequest var1, ObjectArrayList var2) {
      PlayerStepState var3 = var1.state();

      try {
         if (this.lc.get(var1.player().getUniqueId()) == var3 && var3.lr == var1.lifecycleGeneration()) {
            Player var4 = var1.player();
            Location var5 = var1.location();
            boolean var6 = var1.distanceFromFloor() >= 9.0;
            BlockDisplayRegistry var7 = var1.pool();
            ObjectOpenHashSet var8 = new ObjectOpenHashSet();
            this.ld.mZ.filterNearbyBlocks(var3.field47.getBlockX(), var3.field47.getBlockZ(), kO, var2, var3.lR);
            LongOpenHashSet var9 = new LongOpenHashSet(var3.lR.size());
            ObjectListIterator var10 = var3.lR.iterator();

            while (var10.hasNext()) {
               ChunkLocalBlockDup2 var11 = (ChunkLocalBlockDup2)var10.next();
               var9.add(ChunkBlockTracker.packBlockPosition(var11.relX(), var11.y(), var11.relZ()));
            }

            int var29 = 0;
            ObjectOpenHashSet var30 = new ObjectOpenHashSet();
            NonBlockingHashMapLong var12 = var7.getPositionIndex();
            if (var12 != null && !var12.isEmpty()) {
               for (Entry var14 : var12.entrySet()) {
                  if (var14 != null && !var9.contains((Long)var14.getKey())) {
                     UUID var15 = ((BlockDisplayEntry)var14.getValue()).mr.getUniqueId();
                     var30.add(var15);
                     var29++;
                  }
               }
            }

            ObjectIterator var31 = var30.iterator();

            while (var31.hasNext()) {
               UUID var33 = (UUID)var31.next();
               var7.releaseByUuid(var33);
            }

            if (var29 > 0) {
            }

            boolean var32 = var3.lR.size() > lb;
            if (!var3.lW && var32) {
               var3.lX = 10;
            }

            boolean var34;
            if (var32) {
               if (var3.lX > 0) {
                  var3.lX--;
                  var34 = false;
               } else {
                  var34 = true;
               }
            } else {
               var34 = false;
               var3.lX = 0;
            }

            var3.lW = var32;
            ObjectArrayList var35 = var3.lR;

            for (int var16 = 0; var16 < var35.size(); var16++) {
               ChunkLocalBlockDup2 var17 = (ChunkLocalBlockDup2)var35.get(var16);
               var3.lJ.set(var17.relX(), var17.y(), var17.relZ());
               Location var18 = var3.lJ;
               double var19 = BlockFaceUtils.horizontalDistanceUnchecked(var18, var3.field47);
               if (!(var19 > kO * kO) && (var6 || !kS || var7.getEntryCount() <= 500 || isWithinForwardCone(var1.eyeLocation(), var18, 0.1))) {
                  if (var34 && kY && var19 > la) {
                     int var21 = var18.getBlockX();
                     int var22 = var18.getBlockZ();
                     boolean var23 = (var21 + var22) % 2 == 0;
                     if (!var23) {
                        continue;
                     }
                  }

                  if (var8.size() < kL) {
                     boolean var36 = kP && var34 && (BlockFaceUtils.horizontalDistanceUnchecked(var18, var5) >= kQ || this.isBehindView(var18, var5));
                     BlockDisplayEntry var37 = var7.getEntryAt(var18);
                     if (var37 != null) {
                        boolean var38 = var37.isNamespaceChanged(var17);
                        boolean var24 = var38 || var37.isStateIdChanged(var17);
                        if (var37.resolveUpdate(var24, var36, var34)) {
                           BlockSoundProfile var25 = resolveSoundProfile(var17, var35, var16, var18);
                           if (var25 == null) {
                              continue;
                           }

                           var7.updateDisplayState(var37, var25, var17.age(), var36, var3.field47, var24, var38);
                        }

                        if (var37.isActiveFlag() && var37.advanceCooldown()) {
                           var7.faceDisplayToward(var37, var3.field47, true, false);
                        }
                     } else {
                        BlockSoundProfile var39 = resolveSoundProfile(var17, var35, var16, var18);
                        if (var39 == null) {
                           continue;
                        }

                        var37 = var7.acquireDisplayEntry(false, var39, var39.jP, var17.age(), var18, var36, var3.field47);
                        if (var37 == null) {
                        }
                     }

                     if (var37 != null) {
                        var8.add(var37.mr.getUniqueId());
                     }
                  }
               }
            }

            var7.releaseMissing(var8);
            return;
         }
      } finally {
         var3.lM.set(false);
      }
   }

   public static void forceStepUpdate(Player var0) {
      markStepUpdate(var0, false);
   }

   public static void markStepUpdate(Player var0, boolean var1) {
      StepSoundTracker var2 = ContentManager.method8084().method8142().nb;
      PlayerStepState var3 = var2.getState(var0);
      if (var3 != null) {
         var3.field48.set(true);
         if (var1) {
            var3.lP.set(true);
         }
      }
   }

   private void updatePlayerHitbox(Player var1, boolean var2) {
      Settings var3 = Settings.get();
      PlayerStepState var4 = this.getState(var1);
      if (var4 != null) {
         var4.field48.set(false);
         if (var1.getGameMode() != GameMode.SPECTATOR && var1.getGameMode() != GameMode.ADVENTURE && !BlockInteractionListener.isLightweightCrops(var1)) {
            var4.restoreHitbox();
            BlockDisplayRegistry var5 = this.ld.mZ.getDisplayRegistry(var1);
            if (var5 != null) {
               BlockDisplayEntry var6 = var5.raycastEntry(var1);
               if (var6 != null && var6.mr instanceof ItemDisplay var7) {
                  BoundingBox var13 = var6.getBounds().toBoundingBox();
                  Location var9 = var7.getLocation().getBlock().getLocation();
                  Block var10 = var5.getLastHitBlock();
                  boolean var11 = var3.cropPreciseHitbox && var10 != null && !this.ld.mZ.isCustomCrop(var10.getRelative(BlockFace.UP));
                  boolean var12 = var4.lw.size() == 12
                     && var4.lx != null
                     && var4.lx.getWorld() == var9.getWorld()
                     && (!var3.cropPreciseHitbox || var4.hasValidInteraction());
                  if (!var12) {
                     var4.clearDisplays();
                     ItemDisplayUtils.method6589(var1, var13, var4.lw);
                     var4.updateInteractionHitbox(var13, var11);
                  } else {
                     if (!var2 && !var4.lG && var9.equals(var4.lx) && var13.equals(var4.ly) && var4.lF == var11) {
                        return;
                     }

                     ItemDisplayUtils.method6590(var1, var13, var4.lw);
                     var4.updateInteractionHitbox(var13, var11);
                  }

                  var4.lG = false;
                  var4.lx = var9;
                  var4.ly = var13;
               } else {
                  var4.hideDisplays();
               }
            }
         } else {
            var4.clearDisplays();
         }
      }
   }

   private boolean isBehindView(Location var1, Location var2) {
      double var3 = Math.toRadians(var2.getYaw());
      double var5 = -Math.sin(var3);
      double var7 = Math.cos(var3);
      double var9 = var1.getX() - var2.getX();
      double var11 = var1.getZ() - var2.getZ();
      double var13 = var9 * var5 + var11 * var7;
      double var15 = var9 * var9 + var11 * var11;
      double var17 = var15 - var13 * var13;
      return var17 >= 9.0;
   }

   private static BlockSoundProfile resolveSoundProfile(ChunkLocalBlockDup2 var0, ObjectArrayList var1, int var2, Location var3) {
      BlockSoundProfile var4 = (BlockSoundProfile)ContentManager.method8084().au.get(var0.namespacedId());
      if (var4 != null) {
         return var4;
      }

      if (System.currentTimeMillis() - lh < 10000L) {
         return null;
      }

      lh = System.currentTimeMillis();
      Log.warn(
         "Unknown crop '"
            + var0.namespacedId()
            + "' found at "
            + BlockFaceUtils.locationCoordsString(var3)
            + ". This warning will be shown only once every 10 seconds to avoid spam."
      );
      int var5 = 1;

      String var6;
      for (var6 = var0.namespacedId(); var2 + 1 < var1.size() && ((ChunkLocalBlockDup2)var1.get(var2 + 1)).namespacedId().equals(var6); var5++) {
         var2++;
      }

      if (var5 > 1) {
         Log.devLog("Also skipped " + (var5 - 1) + " additional crops with the same id '" + var6 + "'");
      }

      return null;
   }

   static boolean isWithinForwardCone(Location var0, Location var1, double var2) {
      double var4 = Math.toRadians(var0.getYaw());
      double var6 = -Math.sin(var4);
      double var8 = Math.cos(var4);
      double var10 = var0.getX() - var6 * 2.0;
      double var12 = var0.getZ() - var8 * 2.0;
      double var14 = var1.getX() - var10;
      double var16 = var1.getZ() - var12;
      double var18 = Math.sqrt(var14 * var14 + var16 * var16);
      if (var18 < 1.0E-6) {
         return true;
      }

      double var20 = var6 * (var14 / var18) + var8 * (var16 / var18);
      return var20 >= var2;
   }

   @EventHandler
   private void onSneakToggle(PlayerToggleSneakEvent var1) {
      forceStepUpdate(var1.getPlayer());
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onPlayerMove(PlayerMoveEvent var1) {
      Location var2 = var1.getFrom();
      Location var3 = var1.getTo();
      if (var2.getX() != var3.getX() || var2.getY() != var3.getY() || var2.getZ() != var3.getZ()) {
         PlayerStepState var4 = this.getState(var1.getPlayer());
         if (var4 != null) {
            var4.extendHitboxShort();
         }
      }
   }

   @EventHandler
   private void onPlayerJoin(PlayerJoinEvent var1) {
      SchedulerUtils.runLaterSync(() -> {
         if (var1.getPlayer().isOnline()) {
            this.registerPlayer(var1.getPlayer());
         }
      }, 40L);
   }

   @EventHandler
   private void onPlayerQuit(PlayerQuitEvent var1) {
      PlayerStepState var2 = (PlayerStepState)this.lc.remove(var1.getPlayer().getUniqueId());
      if (var2 != null) {
         if (var2.lz != null) {
            var2.lz.remove();
            var2.lz = null;
         }

         var2.lr++;
         if (!this.submitAsync(() -> {
            var2.reset(false);
            this.ld.mZ.releasePlayerInternal(var1.getPlayer(), false);
         })) {
            this.ld.mZ.releasePlayerInternal(var1.getPlayer(), false);
         }
      }
   }

   @EventHandler
   private void onWorldChange(PlayerChangedWorldEvent var1) {
      PlayerStepState var2 = this.getState(var1.getPlayer());
      if (var2 != null) {
         var2.stopContinuousHarvest();
         var2.clearDisplays();
         this.resetPlayerState(var1.getPlayer(), var2);
      }
   }
}
