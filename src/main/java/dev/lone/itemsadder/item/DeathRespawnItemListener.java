package dev.lone.itemsadder.item;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class DeathRespawnItemListener implements IaListener {
   HashMap afb = new HashMap();

   public DeathRespawnItemListener(Plugin var1, ContentManager var2) {
      EventRegistrar.registerIfAbsent(this, var1);
   }

   @EventHandler(priority = EventPriority.LOWEST)
   private void method552(PlayerDeathEvent var1) {
      ContentManager var2 = ContentManager.method8084();
      Player var3 = var1.getEntity();
      this.afb.remove(var3.getUniqueId());
      Iterator var4 = var1.getDrops().iterator();

      while (var4.hasNext()) {
         ItemStack var5 = (ItemStack)var4.next();
         CustomItemDefinition var6 = var2.method8149(var5);
         if (var6 != null && var6.aoh.hasAction("keep_on_death")) {
            var4.remove();
            this.afb.computeIfAbsent(var3.getUniqueId(), var0 -> new ArrayList<>()).add(var5);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   private void method553(PlayerRespawnEvent var1) {
      Player var2 = var1.getPlayer();
      List var3 = (List)this.afb.get(var2.getUniqueId());
      if (var3 != null) {
         Iterator var4 = var3.iterator();

         while (var4.hasNext()) {
            ItemStack var5 = (ItemStack)var4.next();
            if (var2.getInventory().contains(var5)) {
               var4.remove();
            }
         }

         var4 = var3.iterator();

         while (var4.hasNext()) {
            ItemStack var7 = (ItemStack)var4.next();
            var4.remove();
            ItemStackUtils.method7243(var2, var7);
         }
      }

      this.afb.remove(var2.getUniqueId());
   }
}
