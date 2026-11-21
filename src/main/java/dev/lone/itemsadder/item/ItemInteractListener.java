package dev.lone.itemsadder.item;

import dev.lone.itemsadder.api.Events.ComplexFurnitureInteractEvent;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.RaycastUtils;
import dev.lone.itemsadder.util.Utils;
import java.util.WeakHashMap;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class ItemInteractListener implements IaListener {
   private final WeakHashMap aeU = new WeakHashMap();

   public ItemInteractListener(Plugin var1) {
      EventRegistrar.registerIfAbsent(this, var1);
      ContentManager.method8084().aK.method933(new ItemHandlerBinding(PlayerInteractEvent.class, this::method586));
   }

   @ItemEventHandler
   private void method586(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
         if (var1.getClickedBlock() != null) {
            Player var4 = var1.getPlayer();
            Location var5;
            if (!var4.isSneaking()) {
               var5 = var1.getClickedBlock().getRelative(var1.getBlockFace()).getLocation().add(0.5, 0.5, 0.5);
            } else {
               var5 = Utils.clickedLocation(var1);
               if (var5 == null) {
                  return;
               }
            }

            PlaceableItemSpec var6 = PlaceableItemSpec.fromDefinition(var2);
            if (var6 != null) {
               if (BlockFaceUtils.simulatePlace(var1.getClickedBlock(), var4)) {
                  if (this.aeU.containsKey(var4)) {
                     int var7 = Math.max(200, var4.getPing() + 200);
                     if (System.currentTimeMillis() - (Long)this.aeU.get(var4) < var7) {
                        return;
                     }
                  }

                  this.aeU.put(var4, System.currentTimeMillis());
                  Log.devLog("ComplexFurnitureEventsHandler - getClickedPosition: " + var1.getClickedPosition());
                  Log.devLog("ComplexFurnitureEventsHandler - getClickedBlock: " + var1.getClickedBlock());
                  Log.devLog("ComplexFurnitureEventsHandler - HAND: " + var1.getHand());
                  BlockFace var9 = var1.getBlockFace();
                  if (Boolean.FALSE.equals(var6.ado)) {
                     Vector var8 = RaycastUtils.raycastBetween(
                        var4.getEyeLocation(), var4.getEyeLocation().add(var4.getEyeLocation().getDirection().multiply(5)), true
                     );
                     if (var8 == null) {
                        return;
                     }

                     var5 = var8.toLocation(var4.getWorld());
                  }

                  var6.place(var4, var2, var3, var5, var9);
               }
            }
         }
      }
   }

   @EventHandler
   private void method539(PlayerInteractEvent var1) {
      if (!EventRegistrar.isLeftClick(var1) && var1.getHand() != EquipmentSlot.OFF_HAND) {
         Player var2 = var1.getPlayer();
         PlacedItemInstance var3 = PlaceableItemSpec.findByPlayer(var2);
         if (var3 != null) {
            if (EventRegistrar.callEvent(new ComplexFurnitureInteractEvent(var1.getPlayer(), var3.entity().getLivingEntity(), var1.getAction(), var3.entity()))
               )
             {
               var3.aG(var2);
            }
         }
      }
   }

   @EventHandler
   private void method587(PlayerQuitEvent var1) {
      this.aeU.remove(var1.getPlayer());
   }
}
