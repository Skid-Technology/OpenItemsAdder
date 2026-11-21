package dev.lone.itemsadder.item;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ItemInteractFallbackListener implements IaListener {
   public ItemInteractFallbackListener(Plugin var1) {
      EventRegistrar.registerIfAbsent(this, var1);
      ContentManager.method8084().aK.method933(new ItemHandlerBinding(PlayerInteractEvent.class, this::method613));
   }

   @ItemEventHandler
   private void method613(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
         ItemSpawnAction var4 = (ItemSpawnAction)var2.aoh.getAction("entity_summoner");
         if (var4 != null) {
            Log.devLog("getClickedPosition: " + var1.getClickedPosition());
            Log.devLog("getClickedBlock: " + var1.getClickedBlock());
            var4.method336(var1.getPlayer(), var3, Utils.clickedLocation(var1));
         }
      }
   }
}
