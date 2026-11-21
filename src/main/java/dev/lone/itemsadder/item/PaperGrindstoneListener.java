package dev.lone.itemsadder.item;

import com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PaperGrindstoneListener implements Listener {
   @EventHandler
   private void onPrepareGrindstone(PrepareGrindstoneEvent var1) {
      GrindstoneListener.method1068(var1, var1::setResult);
   }
}
