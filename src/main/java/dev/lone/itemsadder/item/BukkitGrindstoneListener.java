package dev.lone.itemsadder.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;

public class BukkitGrindstoneListener implements Listener {
   @EventHandler
   private void method2289(PrepareGrindstoneEvent var1) {
      GrindstoneListener.method1068(var1, var1::setResult);
   }
}
