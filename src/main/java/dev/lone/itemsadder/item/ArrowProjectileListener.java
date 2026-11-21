package dev.lone.itemsadder.item;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.plugin.Plugin;

public class ArrowProjectileListener implements IaListener {
   private static final long aeW = 20000000000L;
   private final ContentManager aeX;
   private final Map aeY = Collections.synchronizedMap(new WeakHashMap());
   private final Map aeZ = new LinkedHashMap();

   public ArrowProjectileListener(Plugin var1, ContentManager var2) {
      this.aeX = var2;
      EventRegistrar.registerIfAbsent(this, var1);
      SchedulerUtils.runLater(() -> method632(this.aeZ, System.nanoTime()), 20L);
      var2.aK.method933(new ItemHandlerBinding(PlayerInteractEvent.class, this::method637));
   }

   @Override
   public void unregister() {
      IaListener.super.unregister();
      this.aeZ.keySet().forEach(Entity::remove);
      this.aeZ.clear();
      this.aeY.clear();
   }

   private void method631(Arrow var1) {
      var1.setPickupStatus(PickupStatus.DISALLOWED);
      var1.setPersistent(false);
      this.aeZ.put(var1, System.nanoTime() + 20000000000L);
   }

   static void method632(Map var0, long var1) {
      Iterator var3 = var0.entrySet().iterator();

      while (var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         if (var1 - (Long)var4.getValue() < 0L) {
            break;
         }

         Arrow var5 = (Arrow)var4.getKey();
         var3.remove();
         var5.remove();
      }
   }

   @EventHandler(priority = EventPriority.LOW)
   private void method516(PlayerQuitEvent var1) {
      this.aeY.remove(var1.getPlayer());
   }

   public static boolean method633(Material var0) {
      return var0 == Material.LANTERN
         || var0 == Material.GLOWSTONE
         || var0 == Material.SEA_LANTERN
         || var0 == Material.FLOWER_POT
         || var0 == Material.REDSTONE_LAMP
         || var0.toString().contains("GLASS")
         || var0.toString().contains("ICE");
   }

   @EventHandler(priority = EventPriority.LOW)
   public void method634(ProjectileHitEvent var1) {
      method635(var1, this.aeZ);
   }

   static void method635(ProjectileHitEvent var0, Map var1) {
      if (var1.remove(var0.getEntity()) != null) {
         try {
            Block var2 = var0.getHitBlock();
            if (var2 == null) {
               return;
            }

            var2.getWorld().playEffect(var2.getLocation(), Effect.STEP_SOUND, var2.getType());
            if (method633(var2.getType())
               && var0.getEntity().getShooter() instanceof Player var3
               && var3.isOnline()
               && BlockFaceUtils.simulateBreak(var2, var3)) {
               var2.setType(Material.AIR, false);
            }
         } finally {
            var0.getEntity().remove();
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   void method636(EntityShootBowEvent var1) {
      if (var1.getEntityType() == EntityType.PLAYER) {
         ItemStack var2 = ((Player)var1.getEntity()).getInventory().getItemInMainHand();
         if (var2.getType() == Material.CROSSBOW) {
            CustomItemDefinition var3 = this.aeX.method8149(var2);
            if (var3 != null && var3.aoh.hasAction("gun")) {
               var1.setCancelled(true);
               Arrow var4 = (Arrow)var1.getEntity().launchProjectile(Arrow.class);
               this.method631(var4);
               NativePacketsHolder.al(var4);
               var4.setVelocity(var1.getProjectile().getVelocity());
            }
         }
      }
   }

   @ItemEventHandler
   public void method637(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (!ItemStackUtils.method7250(var1)) {
         if (var1.getAction() == Action.RIGHT_CLICK_BLOCK || var1.getAction() == Action.RIGHT_CLICK_AIR) {
            Player var4 = var1.getPlayer();
            ItemStack var5 = var1.getPlayer().getInventory().getItemInOffHand();
            if (var2.aoh.hasAction("gun")) {
               ItemGiveAction var6 = (ItemGiveAction)var2.aoh.getAction("gun");
               if (var3.getType() == Material.CROSSBOW && this.aJ(var4)) {
                  var1.setUseItemInHand(Result.DENY);
               } else {
                  ItemStack var7 = var6.aep;
                  if (var1.getPlayer().getInventory().getItemInOffHand().getType() != Material.FIREWORK_ROCKET && var3.getType() == Material.CROSSBOW) {
                     SimpleItemDefinitionB var8 = (SimpleItemDefinitionB)var2;
                     CrossbowMeta var9 = (CrossbowMeta)var3.getItemMeta();
                     if (!var9.hasChargedProjectiles()) {
                        var1.setCancelled(true);
                        if (var7 == null || var7.isSimilar(var5)) {
                           Material var10 = Material.ARROW;
                           if (var8.ant) {
                              var10 = Material.FIREWORK_ROCKET;
                           }

                           var9.setChargedProjectiles(new ArrayList());
                           var9.addChargedProjectile(Utils.withDisplayName(new ItemStack(var10), "itemsadder-arrow-1"));
                           if (var8.ans) {
                              var9.addChargedProjectile(Utils.withDisplayName(new ItemStack(var10), "itemsadder-arrow-2"));
                              var9.addChargedProjectile(Utils.withDisplayName(new ItemStack(var10), "itemsadder-arrow-3"));
                              var9.addEnchant(Enchantment.MULTISHOT, 1, true);
                              var9.setEnchantmentGlintOverride(false);
                           } else if (var9.hasEnchant(Enchantment.MULTISHOT)) {
                              var9.removeEnchant(Enchantment.MULTISHOT);
                              var9.setEnchantmentGlintOverride(false);
                           }

                           var1.getPlayer().getItemInHand().setItemMeta(var9);
                           ItemStackUtils.dm(var1.getPlayer());
                           var2.method8761(var1.getPlayer(), ItemEventType.GUN_RELOAD, var3x -> var3x.applyOnInteract(var1, var2, var3));
                           return;
                        }
                     }
                  }

                  if (var7 != null) {
                     boolean var11 = var7 instanceof CustomItemDefinition;
                     if (var11 && CustomItemDefinition.method8780(var7, var5) || !var11 && var7.getType() == var5.getType()) {
                        if (var11 && CustomItemDefinition.method8780(var7, var5) || !var11 && var7.getType() == var5.getType()) {
                           if (var3.getType() != Material.CROSSBOW) {
                              Arrow var12 = (Arrow)var4.launchProjectile(Arrow.class);
                              this.method631(var12);
                              NativePacketsHolder.al(var12);
                              ItemStackUtils.dm(var1.getPlayer());
                              if (ItemStackUtils.aA(var3)) {
                                 Bukkit.getPluginManager().callEvent(new PlayerItemDamageEvent(var4, var3, 1));
                              }
                           }

                           var2.method8761(var4, ItemEventType.GUN_SHOT, var3x -> var3x.applyOnInteract(var1, var2, var3));
                        }
                     } else {
                        var2.method8761(var4, ItemEventType.GUN_NO_AMMO, var3x -> var3x.applyOnInteract(var1, var2, var3));
                     }
                  }
               }
            }
         }
      }
   }

   private boolean aJ(Player var1) {
      if (this.aeY.containsKey(var1)) {
         long var2 = (Long)this.aeY.get(var1);
         if (System.currentTimeMillis() - var2 < 500L) {
            return true;
         }
      }

      this.aeY.put(var1, System.currentTimeMillis());
      return false;
   }
}
