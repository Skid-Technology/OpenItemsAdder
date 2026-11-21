package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class EntityWorldListener implements Listener {
   private static EntityWorldListener qi;
   List qj = new ArrayList();
   Map qk = new HashMap();

   public static EntityWorldListener getInstance() {
      if (qi == null) {
         qi = new EntityWorldListener();
      }

      return qi;
   }

   private EntityWorldListener() {
      EventRegistrar.registerListener(this, Main.getInstance());
   }

   public void register(EntitySpawnRule var1) {
      this.qj.add(var1);
      Main.getInstance().getServer().getWorlds().forEach(var2 -> this.method8373(var1, var2));
   }

   public void clear() {
      this.qj.clear();
      this.qk.clear();
   }

   private void method8373(EntitySpawnRule var1, World var2) {
      if (var1.isWorldAllowed(var2)) {
         this.qk.computeIfAbsent(var2, var0 -> new ArrayList<>()).add(var1);
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void onCreatureSpawn(CreatureSpawnEvent var1) {
      if (var1.getSpawnReason() != SpawnReason.CUSTOM) {
         if (var1.getSpawnReason() == SpawnReason.NATURAL) {
            Location var2 = var1.getLocation();
            List var3 = (List)this.qk.get(var2.getWorld());
            if (var3 != null) {
               for (EntitySpawnRule var5 : var3) {
                  if (var5.canSpawnAt(var1.getEntityType(), var2) && var5.isEnabled()) {
                     for (int var6 = 0; var6 < var5.getMaxPerChunk(); var6++) {
                        int var7 = 0;

                        for (Entity var11 : var2.getChunk().getEntities()) {
                           if (var11.getType() == var1.getEntityType() || CustomEntityManager.getInstance().isCustomEntityLoaded(var11)) {
                              var7++;
                           }
                        }

                        if (var7 >= Settings.get().customEntitiesMaxPerChunk) {
                           Log.devLog(
                              "Too many custom entities at "
                                 + BlockFaceUtils.locationCoordsString(var2)
                                 + ". Skipping spawning of "
                                 + var5.getSettings().getNamespacedId()
                           );
                           break;
                        }

                        if (var5.getGroup().mode() == SpawnMode.REPLACE) {
                           var1.setCancelled(true);
                        }

                        CustomEntityImpl var12 = CustomEntityManager.getInstance().spawnCustomEntitySimple(var5.getSettings().getNamespacedId(), var2, true);
                        if (var12 != null) {
                           Log.devLog(
                              "Spawned custom entity "
                                 + var5.getSettings().getNamespacedId()
                                 + " in "
                                 + var2.getWorld().getName()
                                 + " at "
                                 + var2.getBlockX()
                                 + " "
                                 + var2.getBlockY()
                                 + " "
                                 + var2.getBlockZ()
                           );
                        } else {
                           Log.devLog(
                              "Failed to spawn unknown custom entity "
                                 + var5.getSettings().getNamespacedId()
                                 + " in "
                                 + var2.getWorld().getName()
                                 + " at "
                                 + var2.getBlockX()
                                 + " "
                                 + var2.getBlockY()
                                 + " "
                                 + var2.getBlockZ()
                           );
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void onWorldLoad(WorldLoadEvent var1) {
      this.qj.forEach(var2 -> this.method8373(var2, var1.getWorld()));
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void onWorldUnload(WorldUnloadEvent var1) {
      List var2 = (List)this.qk.get(var1.getWorld());
      if (var2 != null) {
         this.qk.remove(var1.getWorld());
      }
   }
}
