package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemEventType;
import dev.lone.itemsadder.item.ItemRuleSpec;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.ServerCompat;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public final class GeneralItemEffectListener implements IaListener {
   private final ContentManager alx;
   final PlayerEffectCooldowns aly = new PlayerEffectCooldowns();
   @Nullable
   private final IaListener alz;

   public GeneralItemEffectListener(Plugin var1, ContentManager var2) {
      this.alx = var2;
      EventRegistrar.registerIfAbsent(this, var1);
      this.alz = this.getDelegate();
      SchedulerUtils.runLater(this::tick, 1L);
   }

   @Nullable
   private IaListener getDelegate() {
      if (!ServerCompat.isPaper()) {
         return null;
      }

      try {
         return new ElytraBoostListener(this);
      } catch (LinkageError | RuntimeException var2) {
         Log.warn("Failed to register Paper Elytra boost compatibility. Falling back to Bukkit interaction handling. " + var2);
         return null;
      }
   }

   @Override
   public void unregister() {
      this.aly.clear();
      IaListener.method532(this.alz);
      IaListener.super.unregister();
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method1349(EntityToggleGlideEvent var1) {
      if (var1.getEntity() instanceof Player var2) {
         if (var1.isGliding()) {
            CustomItemDefinition var4 = this.getHeldDefinition(var2);
            if (var4 == null) {
               return;
            }

            this.aly.setCooldown(var2.getUniqueId(), var4.getNamespacedID());
            dispatchGeneral(var1, var2, var4, getHeldItem(var2), ItemEventType.GLIDE_START);
         } else {
            CooldownEntry var5 = this.aly.getPlayerEntry(var2.getUniqueId());
            if (var5 != null) {
               this.applyCooldown(var1, var2, var5);
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onPlayerInteract(PlayerInteractEvent var1) {
      if (this.alz == null
         && (var1.getAction() == Action.RIGHT_CLICK_AIR || var1.getAction() == Action.RIGHT_CLICK_BLOCK)
         && var1.getItem() != null
         && var1.getItem().getType() == Material.FIREWORK_ROCKET) {
         EffectFlags var2 = this.resolveFlags(var1.getPlayer(), var1.getItem(), var1);
         if (var2.CH()) {
            if (!var2.CI()) {
               var1.setCancelled(true);
            } else {
               if (!var2.CJ() && var1.getHand() != null) {
                  this.onEquipmentSlot(var1.getPlayer(), var1.getHand(), var1.getItem().clone());
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onItemDamage(PlayerItemDamageEvent var1) {
      CustomItemDefinition var2 = this.getHeldDefinition(var1.getPlayer());
      CustomItemDefinition var3 = this.alx.method8149(var1.getItem());
      if (var2 != null && var3 != null && var2.getNamespacedID().equals(var3.getNamespacedID())) {
         CooldownEntry var4 = this.aly.getOrCreateCooldown(var1.getPlayer().getUniqueId(), var2.getNamespacedID());
         var1.setDamage(var4 == null ? var2.DP().bC(var1.getDamage()) : var4.computeRemaining(var1.getDamage(), var2.DP().ET()));
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onEntityDamage(EntityDamageEvent var1) {
      if (var1.getCause() == DamageCause.FLY_INTO_WALL && var1.getEntity() instanceof Player var2) {
         CustomItemDefinition var4 = this.getHeldDefinition(var2);
         if (var4 != null) {
            var1.setDamage(var4.DP().method76(var1.getDamage()));
            dispatchGeneral(var1, var2, var4, getHeldItem(var2), ItemEventType.ELYTRA_COLLISION);
         }
      }
   }

   @EventHandler
   private void onPlayerDeath(PlayerDeathEvent var1) {
      this.aly.getPlayerEntry(var1.getEntity().getUniqueId());
   }

   @EventHandler
   private void onPlayerQuit(PlayerQuitEvent var1) {
      this.aly.getPlayerEntry(var1.getPlayer().getUniqueId());
   }

   EffectFlags resolveFlags(Player var1, ItemStack var2, Event var3) {
      CustomItemDefinition var4 = this.getHeldDefinition(var1);
      if (var4 != null && var1.isGliding()) {
         CooldownEntry var5 = this.aly.findCooldown(var1.getUniqueId(), var4.getNamespacedID());
         ItemRuleSpec var6 = var4.DP();
         EffectFlags var7 = flagsFor(var6, var5.alH);
         if (!var7.CI()) {
            return var7;
         }

         var5.alH = var6.ES().lR();
         dispatchGeneral(var3, var1, var4, getHeldItem(var1), ItemEventType.ELYTRA_BOOST);
         return var7;
      } else {
         return EffectFlags.alD;
      }
   }

   static EffectFlags flagsFor(ItemRuleSpec var0, int var1) {
      return var0.ES().enabled() && var1 <= 0 ? new EffectFlags(true, true, var0.ES().CJ()) : new EffectFlags(true, false, var0.ES().CJ());
   }

   private void onEquipmentSlot(Player var1, EquipmentSlot var2, ItemStack var3) {
      SchedulerUtils.runLaterSync(() -> {
         ItemStack var3x = var2 == EquipmentSlot.HAND ? var1.getInventory().getItemInMainHand() : var1.getInventory().getItemInOffHand();
         if (var3x.getType().isAir() && var3.getAmount() == 1) {
            if (var2 == EquipmentSlot.HAND) {
               var1.getInventory().setItemInMainHand(var3);
            } else {
               var1.getInventory().setItemInOffHand(var3);
            }
         } else if (var3x.isSimilar(var3) && var3x.getAmount() == var3.getAmount() - 1) {
            var3x.setAmount(var3x.getAmount() + 1);
         }
      }, 1L);
   }

   private void tick() {
      Iterator var1 = this.aly.alE.entrySet().iterator();

      while (var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         Player var3 = Bukkit.getPlayer((UUID)var2.getKey());
         CooldownEntry var4 = (CooldownEntry)var2.getValue();
         CustomItemDefinition var5 = var3 == null ? null : this.getHeldDefinition(var3);
         if (var3 != null && var3.isOnline() && var3.isGliding() && var5 != null) {
            if (!var5.getNamespacedID().equals(var4.alF)) {
               this.applyCooldown(new EntityToggleGlideEvent(var3, false), var3, var4);
               var4 = this.aly.findCooldown((UUID)var2.getKey(), var5.getNamespacedID());
            }

            var4.alG++;
            if (var4.alH > 0) {
               var4.alH--;
            }

            if (var5.DP().bB(var4.alG)) {
               var1.remove();
               var3.setGliding(false);
               this.applyCooldown(new EntityToggleGlideEvent(var3, false), var3, var4);
            }
         } else {
            var1.remove();
            if (var3 != null) {
               this.applyCooldown(new EntityToggleGlideEvent(var3, false), var3, var4);
            }
         }
      }
   }

   private void applyCooldown(Event var1, Player var2, CooldownEntry var3) {
      CustomItemDefinition var4 = this.alx.method8151(var3.alF, false);
      if (var4 != null) {
         dispatchGeneral(var1, var2, var4, getHeldItem(var2), ItemEventType.GLIDE_STOP);
      }
   }

   private static void dispatchGeneral(Event var0, Player var1, CustomItemDefinition var2, ItemStack var3, ItemEventType var4) {
      var2.method8761(var1, var4, var4x -> var4x.applyEffect(var0, var2, var3, var1));
   }

   @Nullable
   private CustomItemDefinition getHeldDefinition(Player var1) {
      CustomItemDefinition var2 = this.alx.method8149(getHeldItem(var1));
      return var2 != null && var2.DP() != null ? var2 : null;
   }

   private static ItemStack getHeldItem(Player var0) {
      ItemStack var1 = var0.getInventory().getChestplate();
      return var1 == null ? new ItemStack(Material.AIR) : var1;
   }
}
