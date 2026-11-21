package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PackDownloadGate implements IaListener {
   final Map bBY = new HashMap();

   public void register() {
      EventRegistrar.registerIfAbsent(this, Main.getInstance());
   }

   @Override
   public void unregister() {
      EventRegistrar.unregisterListener(this);
   }

   public void lockPlayer(Player var1) {
      PackPushTimeout var2 = new PackPushTimeout(var1);
      this.bBY.put(var1, var2);
      if (Settings.get().protectLockUsingEntity) {
         var2.lockMovement();
      }

      if (Main.config.getBoolean("pack_delivery.show_loading_title")) {
         var1.sendTitle(Main.aFi.lj("resourcepack-apply-title"), Main.aFi.lj("resourcepack-apply-subtitle"), 1, 6000, 20);
      }
   }

   public void releasePlayer(Player var1) {
      PackPushTimeout var2 = (PackPushTimeout)this.bBY.get(var1);
      if (var2 != null) {
         this.bBY.remove(var1);
         if (ContentManager.method8084().aL.getPlayerStatus(var1) != PackPushStatus.NONE && Main.config.getBoolean("pack_delivery.show_loading_title")) {
            var1.sendTitle(" ", "", 0, 0, 0);
         }

         var2.unlockMovement();
      }
   }

   public boolean isLocked(Player var1) {
      return this.bBY.containsKey(var1);
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
   private void onDamage(EntityDamageByEntityEvent var1) {
      if (var1.getDamager() instanceof Player var2) {
         PackPushTimeout var5 = (PackPushTimeout)this.bBY.get(var2);
         if (var5 != null) {
            var1.setCancelled(true);
         }
      }

      if (var1.getEntity() instanceof Player var4) {
         PackPushTimeout var7 = (PackPushTimeout)this.bBY.get(var4);
         if (var7 != null) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler
   private void onCommand(PlayerCommandPreprocessEvent var1) {
      if (Settings.get().protectCancelCommands) {
         if (this.isLocked(var1.getPlayer())) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler
   private void onTeleport(PlayerTeleportEvent var1) {
      if (Settings.get().protectCancelTeleport) {
         if (this.isLocked(var1.getPlayer())) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler
   private void onMove(PlayerMoveEvent var1) {
      if (Settings.get().protectCancelMovementEvents) {
         if (this.isLocked(var1.getPlayer())) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler
   private void onQuit(PlayerQuitEvent var1) {
      this.bBY.remove(var1.getPlayer());
   }

   @EventHandler
   private void onDeath(PlayerDeathEvent var1) {
      this.releasePlayer(var1.getEntity());
   }
}
