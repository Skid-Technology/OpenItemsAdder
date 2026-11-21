package dev.lone.itemsadder.item.effect;

import com.jeff_media.armorequipevent.ArmorEquipEvent;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.ColoredItemDefinition;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemEventType;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.SchedulerUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ArmorEffectListener implements IaListener {
   private final ContentManager ale;
   private ArmorEffectManager alf;

   public ArmorEffectListener(Plugin var1, ContentManager var2) {
      this.ale = var2;
      EventRegistrar.registerIfAbsent(this, var1);
   }

   public void registerArmorEffects(Plugin var1, Iterable var2) {
      if (this.alf != null) {
         this.alf.close();
      }

      ArmorEffectManager var3 = new ArmorEffectManager(var1, this.ale, var2);
      this.alf = var3.isEnabled() ? var3 : null;
   }

   @Override
   public void unregister() {
      if (this.alf != null) {
         this.alf.close();
      }

      IaListener.super.unregister();
   }

   @EventHandler(ignoreCancelled = true)
   void onArmorEquip(ArmorEquipEvent var1) {
      CustomItemDefinition var2 = this.ale.method8149(var1.getOldArmorPiece());
      CustomItemDefinition var3 = this.ale.method8149(var1.getNewArmorPiece());
      if (var2 != null || var3 != null) {
         if (var3 != null) {
            this.applyArmorChange(var1, var1.getNewArmorPiece(), var3, true);
            if (!this.hasArmorEffects(var3)) {
               var1.setCancelled(true);
               return;
            }
         }

         if (var2 != null) {
            this.applyArmorChange(var1, var1.getOldArmorPiece(), var2, false);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   void onArmorEquipDup2(ArmorEquipEvent var1) {
      if (this.alf != null) {
         ArmorEffectManager var2 = this.alf;
         SchedulerUtils.runLaterSync(() -> var2.bq(var1.getPlayer()), 1L);
      }
   }

   @EventHandler
   void onPlayerJoin(PlayerJoinEvent var1) {
      if (this.alf != null) {
         ArmorEffectManager var2 = this.alf;
         SchedulerUtils.runLaterSync(() -> var2.bq(var1.getPlayer()), 1L);
      }
   }

   @EventHandler
   void onPlayerRespawn(PlayerRespawnEvent var1) {
      if (this.alf != null) {
         ArmorEffectManager var2 = this.alf;
         SchedulerUtils.runLaterSync(() -> var2.bq(var1.getPlayer()), 1L);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   void onPlayerDeath(PlayerDeathEvent var1) {
      if (this.alf != null) {
         this.alf.bs(var1.getEntity());
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   void onPotionEffectChange(EntityPotionEffectEvent var1) {
      if (this.alf != null) {
         this.alf.onPotionEffect(var1);
      }
   }

   @EventHandler
   void onPlayerQuit(PlayerQuitEvent var1) {
      if (this.alf != null) {
         this.alf.bs(var1.getPlayer());
      }
   }

   @EventHandler(priority = EventPriority.LOWEST)
   private void onPlayerInteract(PlayerInteractEvent var1) {
      if (var1.getItem() != null && var1.getClickedBlock() != null && var1.getClickedBlock().getType() == Material.WATER_CAULDRON) {
         CustomItemDefinition var2 = ContentManager.method8084().method8149(var1.getItem());
         if (var2 instanceof ColoredItemDefinition) {
            var1.setUseInteractedBlock(Result.DENY);
            var1.setUseItemInHand(Result.DENY);
         }
      }
   }

   private boolean hasArmorEffects(CustomItemDefinition var1) {
      return var1 instanceof ColoredItemDefinition ? ((ColoredItemDefinition)var1).Df() : true;
   }

   void applyArmorChange(ArmorEquipEvent var1, ItemStack var2, CustomItemDefinition var3, boolean var4) {
      if (var4) {
         if (!this.hasArmorEffects(var3)) {
            var1.setCancelled(true);
         } else {
            var3.method8761(var1.getPlayer(), ItemEventType.WEAR, var3x -> var3x.applyOnArmorEquip(var1, var3, var2));
         }
      } else {
         var3.method8761(var1.getPlayer(), ItemEventType.UNWEAR, var3x -> var3x.applyOnArmorEquip(var1, var3, var2));
      }
   }
}
