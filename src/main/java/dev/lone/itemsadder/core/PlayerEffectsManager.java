package dev.lone.itemsadder.core;

import beer.devs.nativepackets.api.ActionBarPacketView;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.NativeText;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.SystemChatPacketView;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.command.RuleSource;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.event.PlayerEventEffect;
import dev.lone.itemsadder.lang.TextFormatCache;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.text.NmsChatService;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

public class PlayerEffectsManager implements IaListener {
   private static final long acy = 1700L;
   final Plugin acz;
   final ContentManager acA;
   public final Map acB = new ConcurrentHashMap();
   public final Map acC = Collections.synchronizedMap(new WeakHashMap());
   BukkitTask acD;
   BukkitTask acE;
   BukkitTask acF;
   private PacketListenerHandle acG;
   private PacketListenerHandle acH;

   public PlayerEffectsManager(Plugin var1, ContentManager var2) {
      this.acz = var1;
      this.acA = var2;
   }

   public void init() {
      for (Player var2 : Bukkit.getOnlinePlayers()) {
         if (this.acA.aG.method702(var2)) {
            this.method701(var2);
            ((PlayerEffectState)this.acB.get(var2)).Ap();
         }
      }

      EventRegistrar.registerIfAbsent(this, this.acz);
      this.Ah();
      this.Aj();
   }

   private void method701(Player var1) {
      PlayerEffectState var2 = new PlayerEffectState(var1);
      this.acB.put(var1, var2);

      for (NamespacedElement var4 : this.acA.aF.abm.values()) {
         if (var4.method491(var2) instanceof NamespacedElementWithEffects var6) {
            if (!this.acA.aG.method703(var1, var6.zQ())) {
               this.acA.aG.method704(var1, var6.zQ(), var6.zP());
            } else {
               var6.method499(this.acA.aG.method1429(var1, var6.zQ(), var6.zP()));
            }

            var2.method9644(var6);
         }
      }

      var2.recalculateOffsets();
   }

   void Ah() {
      this.Ai();
      long var1 = this.acA.method8114();
      byte var3 = 5;
      this.acD = SchedulerUtils.scheduleRepeatingSync(var3x -> {
         if (this.acA.method8110() && this.acA.method8115(var1)) {
            for (Entry var5 : this.acB.entrySet()) {
               Player var6 = (Player)var5.getKey();
               PlayerEffectState var7 = (PlayerEffectState)var5.getValue();

               for (NamespacedElementWithEffects var9 : var7.acK) {
                  var9.method495(var6, 5);
               }
            }
         }
      }, 0L, 5L);
      this.acE = SchedulerUtils.scheduleRepeatingSync(var3x -> {
         if (this.acA.method8110() && this.acA.method8115(var1)) {
            for (Entry var5 : this.acB.entrySet()) {
               Player var6 = (Player)var5.getKey();
               PlayerEffectState var7 = (PlayerEffectState)var5.getValue();
               if (var7 != null && this.acA.aG.method702(var6)) {
                  for (NamespacedElementWithEffects var9 : var7.acK) {
                     if (var9.zL() && var9.zE()) {
                        var7.recalculateOffsets();
                        break;
                     }
                  }
               }
            }
         }
      }, 0L, 1L);
      byte var4 = 20;
      this.acF = Bukkit.getServer().getScheduler().runTaskTimer(this.acz, () -> {
         if (this.acA.method8110() && this.acA.method8115(var1)) {
            for (Entry var4x : this.acB.entrySet()) {
               Player var5 = (Player)var4x.getKey();
               PlayerEffectState var6 = (PlayerEffectState)var4x.getValue();
               if (this.acA.aG.method702(var5)) {
                  var6.Aq();
                  if (!PermissionNodes.method1619(var5, RuleSource.TRIGGERS)) {
                     for (NamespacedElementWithEffects var8 : var6.acK) {
                        if (!PermissionNodes.method1617(var5, var8, RuleSource.TRIGGERS)) {
                           var8.method497(var5, 20);
                        }
                     }
                  }
               }

               if (this.acC.containsKey(var5)) {
                  if (System.currentTimeMillis() - (Long)this.acC.get(var5) <= 1700L) {
                     continue;
                  }

                  this.acC.remove(var5);
               }

               this.az(var5);
            }
         }
      }, 0L, 20L);
   }

   public void Ai() {
      if (this.acD != null) {
         this.acD.cancel();
         this.acD = null;
      }

      if (this.acE != null) {
         this.acE.cancel();
         this.acE = null;
      }

      if (this.acF != null) {
         this.acF.cancel();
         this.acF = null;
      }
   }

   private void Aj() {
      this.Ak();
      this.acG = NativePacketsHolder.PE()
         .method1888(
            this,
            PacketSelector.exact(PacketTypes.ACTION_BAR),
            ListenerPriority.NORMAL,
            true,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var1 -> this.method875(var1, ((ActionBarPacketView)var1.view(ActionBarPacketView.class)).message())
         );
      this.acH = NativePacketsHolder.PE()
         .method1888(this, PacketSelector.exact(PacketTypes.SYSTEM_CHAT), ListenerPriority.NORMAL, true, ListenerFailurePolicy.PASS_ORIGINAL, var1 -> {
            SystemChatPacketView var2 = (SystemChatPacketView)var1.view(SystemChatPacketView.class);
            if (var2.overlay()) {
               this.method875(var1, var2.message());
            }
         });
   }

   private void method875(PacketEvent var1, NativeText var2) {
      UUID var3 = NativePacketsHolder.PE().method1883(var1);
      if (var3 != null && !NmsChatService.Nq().method2957(var2.nativeHandle(), (String)TextFormatCache.abT.get(1))) {
         Player var4 = NativePacketsHolder.PE().method1885(var3, var1.connection().id());
         if (var4 != null) {
            this.aB(var4);
         }
      }
   }

   private void Ak() {
      if (this.acG != null) {
         NativePacketsHolder.PE().method1889(this, this.acG);
      }

      if (this.acH != null) {
         NativePacketsHolder.PE().method1889(this, this.acH);
      }

      this.acG = null;
      this.acH = null;
   }

   public void method876(Player var1, String var2, float var3) {
      if (this.acA.method8110()) {
         PlayerEffectState var4 = (PlayerEffectState)this.acB.get(var1);
         if (var4 != null && this.acA.aG.method702(var1)) {
            NamespacedElementWithEffects var5 = (NamespacedElementWithEffects)var4.acN.get(var2);
            if (var5 != null && var5.abF.equals(var2)) {
               var5.method499(var3);
            }

            this.az(var1);
         }
      }
   }

   public void az(Player var1) {
      if (this.acA.method8110()) {
         if (var1.getGameMode() != GameMode.SPECTATOR) {
            if (!this.acA.aL.downloadGate.isLocked(var1)) {
               if (!this.aA(var1)) {
                  PlayerEffectState var2 = (PlayerEffectState)this.acB.get(var1);
                  if (var2 != null) {
                     var2.An();
                  }
               }
            }
         }
      }
   }

   private boolean aA(Player var1) {
      Long var2 = (Long)this.acC.get(var1);
      return var2 == null ? false : System.currentTimeMillis() - var2 < 1700L;
   }

   private void aB(Player var1) {
      this.acC.put(var1, System.currentTimeMillis());
   }

   @Override
   public void unregister() {
      this.Ai();
      IaListener.super.unregister();
      this.Ak();
      this.acB.clear();
      this.acC.clear();
   }

   public boolean method877(String var1, Player var2) {
      PlayerEffectState var3 = (PlayerEffectState)this.acB.get(var2);
      if (var3 == null) {
         return false;
      }

      NamespacedElementWithEffects var4 = var3.bX(var1);
      return var4 != null && PermissionNodes.method1617(var2, var4, RuleSource.COMMANDS);
   }

   @Nullable
   public NamespacedElementWithEffects method878(Player var1, String var2) {
      PlayerEffectState var3 = (PlayerEffectState)this.acB.get(var1);
      return var3 == null ? null : (NamespacedElementWithEffects)var3.acM.get(var2);
   }

   @EventHandler(priority = EventPriority.LOW)
   private void method515(PlayerJoinEvent var1) {
      if (this.acA.method8110()) {
         Player var2 = var1.getPlayer();
         if (this.acA.aG.method702(var2)) {
            this.method701(var2);
            if (this.acB.containsKey(var2)) {
               ((PlayerEffectState)this.acB.get(var2)).Ap();
            }
         }
      }
   }

   @EventHandler
   private void method516(PlayerQuitEvent var1) {
      Player var2 = var1.getPlayer();
      this.acB.remove(var2);
      this.acC.remove(var2);
   }

   @EventHandler(priority = EventPriority.MONITOR)
   private void method879(PlayerGameModeChangeEvent var1) {
      if (this.acA.method8110()) {
         long var2 = this.acA.method8114();
         if (var1.getNewGameMode() == GameMode.SPECTATOR) {
            if (this.acB.containsKey(var1.getPlayer())) {
               ((PlayerEffectState)this.acB.get(var1.getPlayer())).Ao();
            }
         } else {
            SchedulerUtils.runLaterSync(() -> {
               if (this.acA.method8115(var2)) {
                  this.az(var1.getPlayer());
               }
            }, 1L);
         }

         SchedulerUtils.runLaterSync(() -> {
            if (this.acA.method8110() && this.acA.method8115(var2) && this.acB.containsKey(var1.getPlayer())) {
               ((PlayerEffectState)this.acB.get(var1.getPlayer())).recalculateOffsets();
            }
         }, 1L);
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void method880(FoodLevelChangeEvent var1) {
      if (this.acA.method8110() && !EventRegistrar.isCustomFoodLevelEvent(var1)) {
         Player var2 = (Player)var1.getEntity();
         PlayerEffectState var3 = (PlayerEffectState)this.acB.get(var2);
         if (var3 != null) {
            for (NamespacedElementWithEffects var5 : var3.acK) {
               for (PlayerEventEffect var7 : var5.zO()) {
                  var7.method322(var5, var2, var1);
               }
            }

            if (this.acB.containsKey(var2)) {
               ((PlayerEffectState)this.acB.get(var2)).recalculateOffsets();
            }
         }
      }
   }

   @EventHandler
   private void method881(PlayerRespawnEvent var1) {
      if (this.acA.method8110()) {
         Player var2 = var1.getPlayer();
         PlayerEffectState var3 = (PlayerEffectState)this.acB.get(var2);
         if (var3 != null) {
            for (NamespacedElementWithEffects var5 : var3.acK) {
               for (PlayerEventEffect var7 : var5.zO()) {
                  var7.method382(var5, var2, var1);
               }
            }

            if (this.acB.containsKey(var2)) {
               var3.recalculateOffsets();
               var3.Ap();
            }
         }
      }
   }

   @EventHandler
   private void method882(PlayerChangedWorldEvent var1) {
      if (this.acA.method8110()) {
         Player var2 = var1.getPlayer();
         if (this.acB.containsKey(var2)) {
            ((PlayerEffectState)this.acB.get(var2)).Ap();
         }
      }
   }

   @EventHandler
   private void method883(PlayerTeleportEvent var1) {
      if (this.acA.method8110()) {
         Player var2 = var1.getPlayer();
         if (this.acB.containsKey(var2)) {
            ((PlayerEffectState)this.acB.get(var2)).Ap();
         }
      }
   }
}
