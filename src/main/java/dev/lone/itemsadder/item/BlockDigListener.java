package dev.lone.itemsadder.item;

import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.PlayerAction;
import beer.devs.nativepackets.api.PlayerActionPacketView;
import beer.devs.nativepackets.api.SchedulerAccess;
import beer.devs.nativepackets.api.SynchronizedPacketAccess;
import beer.devs.nativepackets.api.SchedulerAccess.ScheduledTask;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.network.ConnectionRecord;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.network.ViaVersionIntegration;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.IntTableConstantsI;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.ServerCompat;
import dev.lone.itemsadder.util.StoreException;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import dev.lone.itemsadder.world.InstrumentRegistry;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.WeakHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.Nullable;

public class BlockDigListener implements IaListener {
   private static BlockDigListener ahh;
   Plugin field30;
   private PacketListenerHandle ahi;
   private volatile boolean cV;
   private volatile long cW;
   private final boolean ahj = !ServerCompat.classExists("io.papermc.paper.threadedregions.RegionizedServer")
      && PlayerInteractionFacade.getHandler().isAvailable();
   private final Map ahk = Collections.synchronizedMap(new WeakHashMap());
   private final Map ahl = Collections.synchronizedMap(new WeakHashMap());

   BlockDigSession method1268(Player var1) {
      return this.ahl.computeIfAbsent(var1, var0 -> new BlockDigSession());
   }

   private static boolean method1269(BlockDigSession var0, long var1) {
      synchronized (var0) {
         if (var0.ahz != var1) {
            return false;
         }

         var0.ahy = null;
         return true;
      }
   }

   private static void method1270(BlockDigSession var0) {
      ScheduledTask var1;
      synchronized (var0) {
         var0.ahz++;
         var1 = var0.ahy;
         var0.ahy = null;
      }

      if (var1 != null) {
         var1.cancel();
      }
   }

   public static BlockDigListener BR() {
      if (ahh == null) {
         ahh = new BlockDigListener(Main.getInstance());
      }

      return ahh;
   }

   private BlockDigListener(Plugin var1) {
      ahh = this;
      this.field30 = var1;
      this.bh(null);
   }

   public void register() {
      this.cV = true;
      long var1 = ++this.cW;
      EventRegistrar.registerIfAbsent(this, this.field30);
      this.method1271(var1);
   }

   @Override
   public void unregister() {
      this.cV = false;
      this.cW++;
      IaListener.super.unregister();
      if (this.ahi != null) {
         NativePacketsHolder.PE().method1889(this.field30, this.ahi);
         this.ahi = null;
      }

      Bukkit.getOnlinePlayers().forEach(this::be);
   }

   private void method1271(long var1) {
      this.ahi = NativePacketsHolder.PE()
         .method1888(
            this.field30,
            PacketSelector.exact(PacketTypes.PLAYER_ACTION),
            ListenerPriority.NORMAL,
            false,
            ListenerFailurePolicy.CANCEL_PACKET,
            var3 -> {
               if (this.cV && this.cW == var1) {
                  PlayerActionPacketView var4 = (PlayerActionPacketView)var3.view(PlayerActionPacketView.class);
                  PlayerAction var5 = var4.action();
                  if (var5 == PlayerAction.START_DESTROY_BLOCK || var5 == PlayerAction.ABORT_DESTROY_BLOCK || var5 == PlayerAction.STOP_DESTROY_BLOCK) {
                     if (!this.ahj || var5 == PlayerAction.STOP_DESTROY_BLOCK) {
                        UUID var6 = NativePacketsHolder.PE().method1883(var3);
                        if (var6 != null) {
                           BlockDigAction var7 = new BlockDigAction(
                              var6, var3.connection().id(), var5, var4.blockX(), var4.blockY(), var4.blockZ(), var4.sequence(), var1
                           );
                           if (this.ahj) {
                              Player var8 = NativePacketsHolder.PE().method1885(var6, var7.bl());
                              if (var8 != null) {
                                 PlayerInteractionFacade.getHandler().runOnMainThread(() -> {
                                    if (this.cV && this.cW == var1 && var8.isOnline() && NativePacketsHolder.PE().method1885(var6, var7.bl()) == var8) {
                                       World var7x = var8.getWorld();
                                       if (var7x.isChunkLoaded(var7.method1() >> 4, var7.method3() >> 4)) {
                                          this.method1272(var8, var7x.getBlockAt(var7.method1(), var7.method2(), var7.method3()), var5);
                                       }
                                    }
                                 });
                              }
                           } else {
                              var3.sync((var2, var3x) -> this.method1276(var2, var3x, var7));
                           }
                        }
                     }
                  }
               }
            }
         );
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean method1272(Player var1, Block var2, PlayerAction var3) {
      if (var1.getGameMode() == GameMode.CREATIVE) {
         return false;
      }

      if (!ContentManager.method8084().method8090()) {
         return true;
      }

      ConnectionRecord var4 = NativePacketsHolder.PE().cB(var1);
      if (var4 == null) {
         return true;
      }

      CustomBlockChunkStoreBase var5 = ContentManager.method8084().method8183(var2);

      boolean var6;
      try {
         var6 = var5.isCustomBlockLoaded(var2.getLocation());
      } catch (StoreException var8) {
         return true;
      }

      if (var6) {
         this.bf(var1);
         switch (IntTableConstantsI.SWITCH_MAP[var3.ordinal()]) {
            case 1:
               this.method1279(var5, var1, var2);
               break;
            case 2:
               this.method1280(var1, var2);
               break;
            case 3:
               this.bc(var1);
         }

         return true;
      } else {
         this.method1278(var1, new BlockDigAction(var1.getUniqueId(), var4.bl(), var3, var2.getX(), var2.getY(), var2.getZ(), -1, this.cW));
         return false;
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void method1273(BlockDamageEvent var1) {
      if (this.cV && this.ahj && this.method1272(var1.getPlayer(), var1.getBlock(), PlayerAction.START_DESTROY_BLOCK)) {
         var1.setCancelled(true);
      }
   }

   @EventHandler
   private void method1274(BlockDamageAbortEvent var1) {
      if (this.cV && this.ahj) {
         this.method1272(var1.getPlayer(), var1.getBlock(), PlayerAction.ABORT_DESTROY_BLOCK);
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void method1275(BlockBreakEvent var1) {
      if (this.cV && this.ahj && !EventRegistrar.isCustomBlockBreakEvent(var1) && var1.getPlayer().getGameMode() != GameMode.CREATIVE) {
         if (!ContentManager.method8084().method8090() || ContentManager.method8084().method8183(var1.getBlock()).isCustomBlock(var1.getBlock())) {
            var1.setCancelled(true);
         }
      }
   }

   private void method1276(PacketEvent var1, SynchronizedPacketAccess var2, BlockDigAction var3) {
      if (this.cV
         && this.cW == var3.ai()
         && var2.player() instanceof Player var4
         && var4.isOnline()
         && NativePacketsHolder.PE().method1885(var3.bk(), var3.bl()) == var4) {
         if (var4.getGameMode() != GameMode.CREATIVE) {
            if (!ContentManager.method8084().method8090()) {
               var1.cancel();
            } else {
               Log.devLog("Action " + var3.BS());
               Location var6 = (Location)var2.location(var3.method1(), var3.method2(), var3.method3());
               if (NativePacketsHolder.PE().IP().ownsRegion(var6)) {
                  this.method1277(var1, var2, var3);
               } else {
                  var1.syncAt(var6, (var2x, var3x) -> this.method1277(var2x, var3x, var3));
               }
            }
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void method1277(PacketEvent var1, SynchronizedPacketAccess var2, BlockDigAction var3) {
      if (this.cV && this.cW == var3.ai() && var2.player() instanceof Player var4 && NativePacketsHolder.PE().method1885(var3.bk(), var3.bl()) == var4) {
         Block var10 = (Block)var2.block(var3.method1(), var3.method2(), var3.method3());
         if (var10 == null) {
            var1.cancel();
         } else {
            CustomBlockChunkStoreBase var6 = ContentManager.method8084().method8183(var10);

            boolean var7;
            try {
               var7 = var6.isCustomBlockLoaded(var10.getLocation());
            } catch (StoreException var9) {
               var1.cancel();
               return;
            }

            if (var7) {
               var1.cancel();
               Object var8 = NativePacketsHolder.PE().Pv().blockChangedAck(var3.BT());
               NativePacketsHolder.PE().method1868(var4, var8);
               if (NativePacketsHolder.PE().IP().ownsEntity(var4)) {
                  this.bf(var4);
                  switch (IntTableConstantsI.SWITCH_MAP[var3.BS().ordinal()]) {
                     case 1:
                        this.method1279(var6, var4, var10);
                        break;
                     case 2:
                        this.method1280(var4, var10);
                        break;
                     case 3:
                        this.bc(var4);
                  }
               }
            } else if (NativePacketsHolder.PE().IP().ownsEntity(var4)) {
               this.method1278(var4, var3);
            } else {
               var1.sync(
                  (var2x, var3x) -> {
                     if (this.cV
                        && this.cW == var3.ai()
                        && var3x.player() instanceof Player var4x
                        && NativePacketsHolder.PE().method1885(var3.bk(), var3.bl()) == var4x) {
                        this.method1278(var4x, var3);
                     }
                  }
               );
            }
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void method1278(Player var1, BlockDigAction var2) {
      BlockDigSession var3 = this.method1268(var1);
      var3.ahB = null;
      switch (IntTableConstantsI.SWITCH_MAP[var2.BS().ordinal()]) {
         case 1:
            if (var3.ahA != null) {
               this.bc(var1);
            }

            method1270(this.method1268(var1));
            this.be(var1);
            break;
         case 2:
         case 3:
            SchedulerAccess var4 = NativePacketsHolder.PE().IP();
            long var5;
            ScheduledTask var7;
            synchronized (var3) {
               var5 = ++var3.ahz;
               var7 = var3.ahy;
               var3.ahy = null;
            }

            if (var7 != null) {
               var7.cancel();
            }

            try {
               ScheduledTask var8 = var4.globalLater(this.field30, () -> {
                  if (this.cV && this.cW == var2.ai()) {
                     Player var6 = NativePacketsHolder.PE().method1885(var2.bk(), var2.bl());
                     if (var6 == null) {
                        method1269(var3, var5);
                     } else {
                        try {
                           var4.entity(this.field30, var6, () -> {
                              if (method1269(var3, var5)) {
                                 if (this.cV && this.cW == var2.ai() && NativePacketsHolder.PE().method1885(var2.bk(), var2.bl()) == var6) {
                                    if (!this.bg(var6)) {
                                       this.method1285(var6, 1.0F);
                                    }
                                 }
                              }
                           }, () -> method1269(var3, var5));
                        } catch (RuntimeException | LinkageError var8x) {
                           method1269(var3, var5);
                        }
                     }
                  } else {
                     method1269(var3, var5);
                  }
               }, 3L);
               synchronized (var3) {
                  if (var3.ahz == var5) {
                     var3.ahy = var8;
                  } else {
                     var8.cancel();
                  }
               }
            } catch (RuntimeException | LinkageError var12) {
               method1269(var3, var5);
            }
      }
   }

   private void method1279(CustomBlockChunkStoreBase var1, Player var2, @Nullable Block var3) {
      BlockDigSession var4 = this.method1268(var2);
      var4.ahB = null;
      if (var3 != null) {
         if (var4.ahA != null) {
            if (var3.equals(var4.ahA.aaU)) {
               return;
            }

            this.bc(var2);
            var4 = this.method1268(var2);
         }

         CustomBlockDefinition var5 = ContentManager.method8084().method8160(var1.getCustomBlockId(var3));
         if (var5 == null) {
            SchedulerUtils.runOnMainThread(() -> var1.removeCustomBlockAndRefresh(var3, true));
            if (Settings.get().logUnknownBlockBreak) {
               Log.warn(var2.getName() + " mined unknown block " + var1.getCustomBlockId(var3) + " " + LocationScanner.formatCoords(var3.getLocation()));
            }
         } else {
            BlockBreakingSession var6 = new BlockBreakingSession(var3, var2, var5, var1);
            var4.ahA = var6;
            var6.startBreaking();
            if (ServerCompat.viaVersionPresent && ViaVersionIntegration.method4869(var2) && var5.anF.getMaterial().getHardness() <= 0.0F) {
               var6.finishBreaking();
            }
         }
      }
   }

   private void bc(Player var1) {
      BlockDigSession var2 = this.method1268(var1);
      var2.ahB = null;
      if (var2.ahA != null) {
         var2.ahA.cancelDigging();
         method1270(var2);
      }
   }

   private void method1280(Player var1, Block var2) {
      this.bc(var1);
      this.method1268(var1).ahB = var2.getLocation();
   }

   void method1281(Player var1, BlockBreakingSession var2) {
      BlockDigSession var3 = (BlockDigSession)this.ahl.get(var1);
      if (var3 != null && var3.ahA == var2) {
         this.method1280(var1, var2.aaU);
      }
   }

   @Nullable
   private Block bd(Player var1) {
      BlockDigSession var2 = this.method1268(var1);
      Location var3 = var2.ahB;
      if (var3 == null) {
         return null;
      }

      if (var1.getGameMode() != GameMode.CREATIVE && var1.getGameMode() != GameMode.SPECTATOR && var1.getWorld().equals(var3.getWorld())) {
         Location var4 = var1.getEyeLocation();
         double var5 = Objects.requireNonNull(var1.getAttribute(Attribute.PLAYER_BLOCK_INTERACTION_RANGE)).getValue();
         RayTraceResult var7 = var1.getWorld()
            .rayTrace(var4, var4.getDirection(), var5, FluidCollisionMode.NEVER, false, 0.0, var1x -> var1x != var1 && var1.canSee(var1x));
         if (var7 != null && var7.getHitBlock() != null && var3.equals(var7.getHitBlock().getLocation())) {
            var2.ahB = null;
            return var7.getHitBlock();
         } else {
            return null;
         }
      } else {
         var2.ahB = null;
         return null;
      }
   }

   public void ax(Player var1) {
      BlockDigSession var2 = (BlockDigSession)this.ahl.remove(var1);
      if (var2 != null) {
         method1270(var2);
         if (var2.ahA != null) {
            var2.ahA.cancelDigTask();
         }

         var2.ahB = null;
      }
   }

   public synchronized boolean method1282(Location var1) {
      return this.ahk.containsKey(var1);
   }

   public synchronized void method1283(Location var1, BlockBreakingSession var2) {
      this.ahk.put(var1, var2);
   }

   public synchronized void method1284(Location var1, BlockBreakingSession var2) {
      this.ahk.remove(var1, var2);
   }

   private void be(Player player) {
      BlockDigSession var2 = this.method1268(player);
      if (!var2.ahv) {
         var2.ahv = true;
         PlayerInteractionFacade.getHandler().be(player);
      }
   }

   private void bf(Player var1) {
      BlockDigSession var2 = this.method1268(var1);
      var2.ahv = false;
      Log.devLog(ChatColor.AQUA + "Sending fake break speed 0 to " + var1.getName());
      PlayerInteractionFacade.getHandler().setReach(var1, 0.0F);
   }

   private void method1285(Player player, float speed) {
      BlockDigSession var3 = this.method1268(player);
      var3.ahv = false;
      Log.devLog(ChatColor.AQUA + "Sending fake break speed " + speed + " to " + player.getName());
      PlayerInteractionFacade.getHandler().setReach(player, speed);
   }

   boolean bg(Player var1) {
      BlockDigSession var2 = this.method1268(var1);
      return var2.ahw != null && System.currentTimeMillis() - var2.ahw < 150L;
   }

   private void bh(@Nullable Player var1) {
      if (Settings.get().customBlocksMushroom || Settings.get().customBlocksNote || Settings.get().customBlocksChorus) {
         InstrumentRegistry.getAdapter().ck(var1);
      }
   }

   @EventHandler
   private void method515(PlayerJoinEvent var1) {
      this.bh(var1.getPlayer());
   }

   @EventHandler
   private void method516(PlayerQuitEvent var1) {
      this.ax(var1.getPlayer());
   }

   @EventHandler
   private void method1286(PlayerChangedWorldEvent var1) {
      if (this.cV && this.ahj) {
         this.bc(var1.getPlayer());
         this.be(var1.getPlayer());
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method1287(BlockDamageEvent var1) {
      BlockDigSession var2 = this.method1268(var1.getPlayer());
      var2.ahx = var1.getPlayer().getGameMode() != GameMode.CREATIVE && var1.getInstaBreak() ? var1.getBlock() : null;
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method1288(PlayerAnimationEvent var1) {
      if (var1.getAnimationType() == PlayerAnimationType.ARM_SWING) {
         BlockDigSession var2 = this.method1268(var1.getPlayer());
         var2.ahw = System.currentTimeMillis();
         if (var2.ahA == null && this.cV && ContentManager.method8084().method8090()) {
            Block var3 = this.bd(var1.getPlayer());
            if (var3 != null && NativePacketsHolder.PE().IP().ownsRegion(var3.getLocation())) {
               CustomBlockChunkStoreBase var4 = ContentManager.method8084().method8183(var3);
               if (var4.isCustomBlock(var3)) {
                  this.method1279(var4, var1.getPlayer(), var3);
               } else {
                  this.be(var1.getPlayer());
               }
            }
         }

         if (var2.ahA != null && var2.ahA.updateBreaking()) {
            var2.ahA = null;
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   private void method546(BlockBreakEvent var1) {
      if (!EventRegistrar.isCustomBlockBreakEvent(var1)) {
         Block var2 = var1.getBlock();
         Material var3 = var2.getType();
         BlockDigSession var4 = this.method1268(var1.getPlayer());
         Block var5 = var4.ahx;
         var4.ahx = null;
         if (var3 == Material.TRIPWIRE) {
            CustomBlockChunkStoreBase var6 = ContentManager.method8084().method8183(var2);
            if (var6.isCustomBlock(var2)) {
               var1.setCancelled(true);
               Player var7 = var1.getPlayer();
               this.bf(var7);
               this.bc(var7);
               this.method1279(var6, var7, var2);
               return;
            }
         } else if (var1.isCancelled()) {
            this.be(var1.getPlayer());
         }

         if (!var1.isCancelled() && !var4.ahv && var2.equals(var5) && var3 != Material.DRAGON_EGG) {
            var1.getPlayer().playEffect(var2.getLocation(), Effect.STEP_SOUND, var3);
         }
      }
   }
}
