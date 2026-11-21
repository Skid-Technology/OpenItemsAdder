package dev.lone.itemsadder.item;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins.ProtectionPlugins;
import dev.lone.itemsadder.api.CustomBlockActionEvent;
import dev.lone.itemsadder.api.PlayerEquipEvent;
import dev.lone.itemsadder.api.PlayerLivingActionEvent;
import dev.lone.itemsadder.api.Events.ItemBurnEvent;
import dev.lone.itemsadder.block.AgeableBlockDefinition;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.block.CustomBlockListener;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.event.EntityEquipmentChangeEvent;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.event.PlayerHeldItemEvent;
import dev.lone.itemsadder.internal.SwitchMap95;
import dev.lone.itemsadder.util.BossBarRegistry;
import dev.lone.itemsadder.util.BossBarSpec;
import dev.lone.itemsadder.util.CollectionUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.RayTraceTarget;
import dev.lone.itemsadder.util.RayTraceWalker;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRemoveEvent.Cause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ItemEventListener implements IaListener {
   private static ItemEventListener aiK;
   final ContentManager aiL;
   public final HashMap aiM = new HashMap();
   public final BossBarRegistry aiN = new BossBarRegistry();

   @Nullable
   public static ItemEventListener Cp() {
      return aiK;
   }

   public ItemEventListener(Plugin var1, ContentManager var2) {
      if (aiK != null) {
         aiK.aiN.clearAll();
         aiK.unregister();
      }

      aiK = this;
      this.aiL = var2;
      EventRegistrar.registerIfAbsent(this, var1);
      SchedulerUtils.runRepeating(var1x -> {
         for (Entry var3 : this.aiN.sessionsByPlayer.entrySet()) {
            Iterator var4 = ((HashMap)var3.getValue()).entrySet().iterator();

            while (var4.hasNext()) {
               Entry var5 = (Entry)var4.next();
               ItemUseSession var6 = (ItemUseSession)var5.getValue();
               CustomItemDefinition var7 = var6.aqk;
               long var8 = var6.aqj;
               if (var7 != null) {
                  for (Entry var11 : var7.aog.aiD.entrySet()) {
                     if (System.currentTimeMillis() - var8 > (Long)var11.getValue()) {
                        var4.remove();
                     }
                  }
               }
            }
         }
      }, 12000L, 24000L);
   }

   public void Cq() {
      if (Settings.get().cooldownBars && this.aiL.method8192()) {
         SchedulerUtils.runRepeating(var1 -> {
            if (!this.aiN.sessionsByPlayer.isEmpty()) {
               for (Entry var3 : this.aiN.sessionsByPlayer.entrySet()) {
                  HashMap var4 = (HashMap)var3.getValue();
                  if (!var4.isEmpty()) {
                     Player var5 = Bukkit.getPlayer((UUID)var3.getKey());
                     if (var5 != null && var5.isOnline()) {
                        for (Entry var7 : var4.entrySet()) {
                           ItemUseSession var8 = (ItemUseSession)var7.getValue();
                           if (var8.EK()) {
                              CustomItemDefinition var9 = var8.aqk;
                              if (var9 != null && var9.Dy()) {
                                 boolean var10 = this.method936(var5, var9);
                                 var8.method10011(!var10, var5);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }, 1L, Settings.get().cooldownBarsRefreshTicks);
      }
   }

   public void method933(ItemHandlerBinding var1) {
      List var2 = this.aiM.getOrDefault(var1.aiI, new ArrayList());
      var2.add(var1);
      this.aiM.put(var1.aiI, var2);
   }

   void method934(Event var1, CustomItemDefinition var2, ItemStack var3) {
      if (var2 != null) {
         List var4 = (List)this.aiM.get(var1.getClass());
         if (var4 != null) {
            if (!(var1 instanceof PlayerInteractEvent var5 && !ProtectionPlugins.canUseItem(var5.getPlayer(), var2))) {
               if (!(var1 instanceof PlayerInteractEntityEvent var7 && !ProtectionPlugins.canUseItem(var7.getPlayer(), var2))) {
                  for (ItemHandlerBinding var6 : var4) {
                     var6.aiJ.execute(var1, var2, var3);
                  }
               }
            }
         }
      }
   }

   public boolean method935(LivingEntity var1, CustomItemDefinition var2) {
      if (!var2.Dy()) {
         return true;
      }

      Player var3 = (Player)var1;
      BossBarSpec var4 = var2.DL();
      if (var4 == null) {
         return false;
      }

      AtomicBoolean var5 = new AtomicBoolean(false);
      ItemUseSession var6 = (ItemUseSession)CollectionUtils.computeNested(this.aiN.sessionsByPlayer, var1.getUniqueId(), var2.aol, var5x -> {
         var5.set(true);
         return new ItemUseSession(var2, this.aiL.aH.method878(var3, var4.title));
      });
      boolean var7 = var5.get() || var6.method10008();
      if (var7) {
         var6.EJ();
         if (var4.displayMode == HudDisplayMode.VANILLA) {
            PlayerInteractionFacade.getHandler().consumeItem(var3, var2, (int)(var2.Dx() / 50L));
         }
      }

      var6.EM();
      var6.method10011(true, var3);
      return var7;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public boolean method936(LivingEntity var1, CustomItemDefinition var2) {
      if (!var2.Dy()) {
         return true;
      }

      Player var3 = (Player)var1;
      BossBarSpec var4 = var2.DL();
      if (var4 == null) {
         return false;
      }

      boolean var5 = true;
      ItemUseSession var6 = (ItemUseSession)CollectionUtils.computeNested(
         this.aiN.sessionsByPlayer, var1.getUniqueId(), var2.aol, var4x -> new ItemUseSession(var2, this.aiL.aH.method878(var3, var4.title))
      );
      if (!var6.method10008()) {
         if (var2.DM() == HudDisplayMode.TITLE && var6.aqo >= 80) {
            return true;
         }

         var5 = false;
         long var7 = var6.EF();
         switch (SwitchMap95.aiO[var4.displayMode.ordinal()]) {
            case 1:
            case 2:
            default:
               break;
            case 3:
               if (var6.EH()) {
                  var3.sendTitle(" ", Main.aFi.lj("please-wait-events-cooldown").replace("{interval}", var6.EG()), var6.EI() ? 15 : 0, 25, 0);
               }
               break;
            case 4:
               float var9 = var4.progress - (float)var7 * var4.progress / (float)var2.Dx();
               var6.method10010(var9, var3);
               break;
            case 5:
               float var10 = 1.0F - (float)var7 / (float)var2.Dx();
               BossBar var11 = this.aiN.barsByPlayer.computeIfAbsent(var3, var3x -> {
                  BossBar var4x = Bukkit.createBossBar(Main.aFi.lj("cooldown-bossbar-title"), var4.getColor(), var4.getStyle(), new BarFlag[0]);
                  var4x.setProgress(var10);
                  var4x.addPlayer(var3);
                  return var4x;
               });
               var11.setProgress(var10);
         }

         var6.EL();
      }

      return var5;
   }

   static void method937(PlayerInteractEvent var0) {
      if ((var0.getAction() == Action.RIGHT_CLICK_AIR || var0.getAction() == Action.LEFT_CLICK_AIR)
         && var0.useInteractedBlock() == Result.DENY
         && var0.useItemInHand() != Result.DENY) {
         var0.setUseInteractedBlock(Result.DEFAULT);
      }
   }

   private boolean method938(PlayerInteractEvent var1) {
      return var1.useInteractedBlock() == Result.DENY && var1.useItemInHand() == Result.DENY;
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   void method939(PlayerInteractEvent var1) {
      if (!this.method938(var1)) {
         ItemStack var2 = var1.getItem();
         CustomItemDefinition var3 = this.aiL.method8149(var2);
         if (var3 != null) {
            method937(var1);
            this.method934(var1, var3, var2);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method940(PlayerAnimationEvent var1) {
      if (var1.getAnimationType() == PlayerAnimationType.ARM_SWING) {
         if (CustomBlockListener.dn.method719(var1.getPlayer())) {
            return;
         }

         if (CustomBlockListener.dn.method723(var1.getPlayer())) {
            return;
         }

         if (CustomBlockListener.dn.method726(var1.getPlayer())) {
            return;
         }

         ItemStack var2 = var1.getPlayer().getItemInHand();
         CustomItemDefinition var3 = this.aiL.method8149(var2);
         if (var3 == null) {
            return;
         }

         RayTraceWalker var4 = new RayTraceWalker(var1.getPlayer(), 6.2);
         if (var4.matchesTarget(RayTraceTarget.ENTITY) && var4.hasHitEntity()) {
            if (var4.getHitEntity().getLocation().distance(var1.getPlayer().getLocation()) < 3.3) {
               return;
            }

            PlayerInteractEvent var5 = new PlayerInteractEvent(
               var1.getPlayer(), Action.LEFT_CLICK_AIR, var1.getPlayer().getItemInHand(), null, var1.getPlayer().getFacing()
            );
            this.method934(var5, var3, var2);
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   void method941(PlayerEditBookEvent var1) {
      ItemStack var2 = var1.getPlayer().getInventory().getItem(var1.getSlot());
      CustomItemDefinition var3 = this.aiL.method8149(var2);
      if (var3 != null) {
         this.method934(var1, var3, var2);
      }
   }

   @EventHandler(ignoreCancelled = true)
   void method942(EntityShootBowEvent var1) {
      if (var1.getEntity().getType() == EntityType.PLAYER) {
         ItemStack var2 = var1.getBow();
         SimpleItemDefinition var3 = (SimpleItemDefinition)this.aiL.method8150(SimpleItemDefinition.class, var2);
         if (var3 != null) {
            this.method934(var1, var3, var2);
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   void method943(EntityEquipmentChangeEvent var1) {
      ItemStack var2 = var1.Ly();
      CustomItemDefinition var3 = this.aiL.method8149(var2);
      if (var3 != null) {
         this.method934(var1, var3, var2);
      }
   }

   @EventHandler(ignoreCancelled = true)
   void method944(ProjectileLaunchEvent var1) {
      if (var1.getEntity().getShooter() instanceof Player) {
         ItemStack var2 = ItemStackUtils.method7253(var1.getEntity());
         if (var2 != null) {
            CustomItemDefinition var3 = this.aiL.method8149(var2);
            this.method934(var1, var3, var2);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   void method945(EntityRemoveEvent var1) {
      if (var1.getCause() == Cause.DEATH && var1.getEntity() instanceof Item var2) {
         EntityDamageEvent var7 = var2.getLastDamageCause();
         if (var7 != null && (var7.getCause() == DamageCause.FIRE || var7.getCause() == DamageCause.FIRE_TICK)) {
            Block var4 = var2.getLocation().getBlock();
            String var5 = null;
            if (var4.getType() == Material.FIRE) {
               String var6 = this.aiL.method8183(var4).getCustomBlockId(var4);
               if (this.aiL.method8154(var6) instanceof AgeableBlockDefinition) {
                  var5 = var6;
               }
            }

            Bukkit.getPluginManager().callEvent(new ItemBurnEvent(var2, var5));
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   void method946(PlayerFishEvent var1) {
      ItemStack var2 = this.aiL.method8181(var1.getPlayer(), Material.FISHING_ROD);
      if (var2 != null) {
         CustomItemDefinition var3 = this.aiL.method8149(var2);
         this.method934(var1, var3, var2);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   void method947(PlayerItemConsumeEvent var1) {
      ItemStack var2 = var1.getItem();
      CustomItemDefinition var3 = this.aiL.method8149(var2);
      if (var3 != null) {
         this.method934(var1, var3, var2);
      }
   }

   @EventHandler(ignoreCancelled = true)
   void method948(FoodLevelChangeEvent var1) {
      if (!EventRegistrar.isCustomFoodLevelEvent(var1)) {
         if (var1.getItem() != null) {
            ItemStack var2 = var1.getItem();
            CustomItemDefinition var3 = this.aiL.method8149(var2);
            if (var3 != null) {
               this.method934(var1, var3, var2);
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   void method949(EntityDamageByEntityEvent var1) {
      if (!EventRegistrar.isCustomEntityDamageEvent(var1)) {
         if (var1.getDamager() instanceof Player) {
            Player var2 = (Player)var1.getDamager();
            ItemStack var3 = var2.getInventory().getItemInMainHand();
            CustomItemDefinition var4 = this.aiL.method8149(var3);
            if (var4 != null) {
               this.method934(var1, var4, var3);
            }
         }

         if (var1.getEntity() instanceof Player) {
            Player var9 = (Player)var1.getEntity();

            for (EquipmentSlot var6 : ItemStackUtils.bMq) {
               ItemStack var7 = var9.getInventory().getItem(var6);
               CustomItemDefinition var8 = this.aiL.method8149(var7);
               if (var8 != null) {
                  this.method934(PlayerEquipEvent.method9988(var9, var1.getDamager(), true, var6), var8, var7);
               }
            }

            ItemStack var11 = var9.getInventory().getItemInMainHand();
            CustomItemDefinition var13 = this.aiL.method8149(var11);
            if (var13 != null) {
               this.method934(PlayerEquipEvent.method9988(var9, var1.getDamager(), false, EquipmentSlot.HAND), var13, var11);
            }

            ItemStack var14 = var9.getInventory().getItemInOffHand();
            CustomItemDefinition var15 = this.aiL.method8149(var14);
            if (var15 != null) {
               this.method934(PlayerEquipEvent.method9988(var9, var1.getDamager(), false, EquipmentSlot.OFF_HAND), var15, var14);
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   void method950(PlayerInteractEntityEvent var1) {
      Player var2 = var1.getPlayer();
      ItemStack var3 = var1.getHand() == EquipmentSlot.HAND ? var2.getInventory().getItemInMainHand() : var2.getInventory().getItemInOffHand();
      CustomItemDefinition var4 = this.aiL.method8149(var3);
      if (var4 != null) {
         this.method934(var1, var4, var3);
      }
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   void method951(BlockBreakEvent var1) {
      if (!EventRegistrar.isCustomBlockBreakEvent(var1)) {
         if (var1.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (!CustomBlockDefinition.isCustomBlockType(var1.getBlock()) || !ContentManager.isCustomBlock(var1.getBlock())) {
               ItemStack var2 = var1.getPlayer().getInventory().getItemInMainHand();
               CustomItemDefinition var3 = this.aiL.method8149(var2);
               if (var3 != null) {
                  this.method934(var1, var3, var2);
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   void method952(CustomBlockActionEvent var1) {
      if (var1.getPlayer().getGameMode() != GameMode.CREATIVE) {
         ItemStack var2 = var1.getPlayer().getInventory().getItemInMainHand();
         CustomItemDefinition var3 = this.aiL.method8149(var2);
         if (var3 != null) {
            this.method934(var1, var3, var2);
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   void method953(PlayerDropItemEvent var1) {
      ItemStack var2 = var1.getItemDrop().getItemStack();
      CustomItemDefinition var3 = this.aiL.method8149(var2);
      if (var3 != null) {
         this.method934(var1, var3, var2);
      }
   }

   @EventHandler(ignoreCancelled = true)
   void method954(PlayerHeldItemEvent var1) {
      ItemStack var2 = var1.getItemStack();
      CustomItemDefinition var3 = this.aiL.method8149(var2);
      if (var3 != null) {
         SchedulerUtils.runOnMainThread(() -> this.method934(var1, var3, var2));
      }
   }

   @EventHandler(ignoreCancelled = true)
   void method955(PlayerPickupItemEvent var1) {
      ItemStack var2 = var1.getItem().getItemStack();
      CustomItemDefinition var3 = this.aiL.method8149(var2);
      if (var3 != null) {
         this.method934(var1, var3, var2);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   void method956(BlockPlaceEvent var1) {
      if (var1.canBuild() && !EventRegistrar.isCustomBlockPlaceEvent(var1)) {
         ItemStack var2 = var1.getItemInHand();
         CustomItemDefinition var3 = this.aiL.method8149(var2);
         if (var3 != null) {
            this.method934(var1, var3, var2);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   void method957(BlockMultiPlaceEvent var1) {
      if (!var1.isCancelled() && var1.canBuild()) {
         ItemStack var2 = var1.getItemInHand();
         CustomItemDefinition var3 = this.aiL.method8149(var2);
         if (var3 != null) {
            this.method934(var1, var3, var2);
         }
      }
   }

   @EventHandler
   void method958(InventoryClickEvent var1) {
      ItemStack var2;
      CustomItemDefinition var3;
      if (var1.getCurrentItem() != null && var1.getCurrentItem().getType() != Material.AIR) {
         var3 = this.aiL.method8149(var1.getCurrentItem());
         var2 = var1.getCurrentItem();
      } else {
         var3 = this.aiL.method8149(var1.getCursor());
         var2 = var1.getCursor();
      }

      if (var3 != null) {
         this.method934(var1, var3, var2);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   void method959(PlayerBucketEmptyEvent var1) {
      ItemStack var2 = null;
      if (var1.getPlayer().getInventory().getItemInMainHand().getType() == Material.WATER_BUCKET) {
         var2 = var1.getPlayer().getInventory().getItemInMainHand();
      } else if (var1.getPlayer().getInventory().getItemInOffHand().getType() == Material.WATER_BUCKET) {
         var2 = var1.getPlayer().getInventory().getItemInOffHand();
      }

      CustomItemDefinition var3 = this.aiL.method8149(var2);
      if (var3 != null) {
         this.method934(var1, var3, var2);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   void method960(PlayerBucketFillEvent var1) {
      ItemStack var2 = null;
      if (var1.getPlayer().getInventory().getItemInMainHand().getType() == Material.BUCKET) {
         var2 = var1.getPlayer().getInventory().getItemInMainHand();
      } else if (var1.getPlayer().getInventory().getItemInOffHand().getType() == Material.BUCKET) {
         var2 = var1.getPlayer().getInventory().getItemInOffHand();
      }

      CustomItemDefinition var3 = this.aiL.method8149(var2);
      if (var3 != null) {
         this.method934(var1, var3, var2);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   void method961(PlayerItemBreakEvent var1) {
      ItemStack var2 = var1.getBrokenItem();
      CustomItemDefinition var3 = this.aiL.method8149(var2);
      if (var3 != null) {
         this.method934(var1, var3, var2);
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   void method962(EntityDeathEvent var1) {
      if (var1.getEntity().getKiller() != null
         && var1.getEntity().getLastDamageCause() != null
         && (
            var1.getEntity().getLastDamageCause().getCause() == DamageCause.ENTITY_SWEEP_ATTACK
               || var1.getEntity().getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK
         )) {
         Player var2 = var1.getEntity().getKiller();
         ItemStack var3 = var2.getItemInHand();
         CustomItemDefinition var4 = this.aiL.method8149(var3);
         if (var4 == null) {
            return;
         }

         this.method934(new PlayerLivingActionEvent(var2, var1.getEntity()), var4, var3);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   void method963(PlayerQuitEvent var1) {
      this.aiN.removeBar(var1.getPlayer());
   }
}
