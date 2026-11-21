package dev.lone.itemsadder.item;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PlaceItemListener implements IaListener {
   private final Plugin afo;
   private final ContentManager afp;

   public PlaceItemListener(Plugin var1, ContentManager var2) {
      this.afo = var1;
      this.afp = var2;
      EventRegistrar.registerIfAbsent(this, var1);
      var2.aK.method933(new ItemHandlerBinding(PlayerInteractEvent.class, this::onInteractPlaceItem));
      var2.aK.method933(new ItemHandlerBinding(BlockPlaceEvent.class, this::onBlockPlaceItem));
   }

   @ItemEventHandler
   private void onInteractPlaceItem(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (!var1.isCancelled()) {
         if (!ItemStackUtils.method7250(var1)) {
            if (var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
               if (var2.aoh.hasAction("sapling")) {
                  ItemTreeAction var4 = (ItemTreeAction)var2.aoh.getAction("sapling");
                  var1.setCancelled(true);
                  var4.plantSapling(var3, var1.getPlayer(), var1.getClickedBlock().getRelative(BlockFace.UP));
               }
            }
         }
      }
   }

   @ItemEventHandler
   private void onBlockPlaceItem(BlockPlaceEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (!var1.isCancelled()) {
         if (var2.aoh.hasAction("sapling")) {
            ItemTreeAction var4 = (ItemTreeAction)var2.aoh.getAction("sapling");
            var1.setCancelled(true);
            var4.plantSapling(var3, var1.getPlayer(), var1.getBlockPlaced().getLocation().getBlock());
         }
      }
   }
}
