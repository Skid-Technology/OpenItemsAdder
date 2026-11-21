package dev.lone.itemsadder.loot;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.CustomBlockActionEvent;
import dev.lone.itemsadder.api.Events.CustomEntityDeathEvent;
import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Utils;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.persistence.PersistentDataType;

public class LootDropListener implements IaListener {
   public void GZ() {
      EventRegistrar.registerIfAbsent(this, Main.getPlugin());
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method824(EntityDeathEvent var1) {
      LivingEntity var2 = var1.getEntity();
      if (var2.getKiller() != null && var2.getKiller().getType() == EntityType.PLAYER) {
         if (!CustomEntityManager.isInitialized() || !CustomEntityManager.getInstance().isCustomEntityLoaded(var2)) {
            List var3 = ContentManager.method8084().aJ.method1988(var2);
            if (var3 != null && !var3.isEmpty()) {
               var3.forEach(var1x -> var1x.method2003(var2.getKiller(), var2.getLocation()));
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method825(CustomEntityDeathEvent var1) {
      if (var1.getKiller() != null && var1.getKiller().getType() == EntityType.PLAYER) {
         List var2 = ContentManager.method8084().aJ.method1990(var1.internal, var1.getKiller());
         if (var2 != null) {
            var2.forEach(var1x -> var1x.method2003(var1.getKiller(), var1.getEntity().getLocation()));
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void method546(BlockBreakEvent var1) {
      if (!EventRegistrar.isCustomBlockBreakEvent(var1)) {
         if (var1.getPlayer().getGameMode() != GameMode.CREATIVE) {
            Block var2 = var1.getBlock();
            if (BlockUtils.isFullyGrown(var2)) {
               List var3 = ContentManager.method8084().aJ.method1984(var2, var2.getType(), var1.getPlayer());
               if (var3 != null && !var3.isEmpty()) {
                  for (MaterialToolDropRule var5 : var3) {
                     if (!var5.method1995(var1.getPlayer().getItemInHand(), var2.getType())) {
                        boolean var6 = true;
                        if (var5.Hw()) {
                           for (DropTrigger var8 : var5.Hv()) {
                              if (!var8.aJ(var2)) {
                                 var6 = false;
                                 break;
                              }
                           }
                        }

                        if (var6) {
                           var5.method2003(var1.getPlayer(), var2.getLocation());
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method740(CustomBlockActionEvent var1) {
      if (var1.getPlayer().getGameMode() != GameMode.CREATIVE) {
         if (var1.Bw()) {
            if (!var1.Bx()) {
               Block var2 = var1.getBlock();
               List var3 = ContentManager.method8084().aJ.method1986(var2, var1.Bu(), var1.getPlayer());
               if (var3 != null && !var3.isEmpty()) {
                  for (BlockToolDropRule var5 : var3) {
                     if (!var5.method1996(var1.getPlayer().getInventory().getItemInMainHand(), var1.Bu().anF.getMaterial())) {
                        var5.method2003(var1.getPlayer(), var2.getLocation());
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method826(PlayerFishEvent var1) {
      if (var1.getCaught() != null) {
         if (var1.getState() == State.CAUGHT_ENTITY || var1.getState() == State.CAUGHT_FISH) {
            if (var1.getCaught().getTicksLived() > 3) {
               return;
            }

            if (!(var1.getCaught() instanceof Item)) {
               return;
            }

            if (!((Item)var1.getCaught()).getItemStack().getType().isEdible()) {
               return;
            }

            List var2 = ContentManager.method8084().aJ.method1987(var1.getCaught().getLocation().getBlock(), var1.getPlayer());
            if (var2 != null && var2.size() > 0) {
               int var3 = Utils.randomInt(0, var2.size() - 1);
               ItemDropApplier var4 = (ItemDropApplier)var2.get(var3);
               var4.method2017((Item)var1.getCaught(), var1.getPlayer());
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void method827(SpawnerSpawnEvent var1) {
      var1.getEntity().getPersistentDataContainer().set(LootManager.atl, PersistentDataType.BOOLEAN, true);
   }
}
