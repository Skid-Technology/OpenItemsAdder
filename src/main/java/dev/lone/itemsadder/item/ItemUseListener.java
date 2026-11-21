package dev.lone.itemsadder.item;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ItemUseListener implements IaListener {
   private final ContentManager afc;

   public ItemUseListener(Plugin var1, ContentManager var2) {
      this.afc = var2;
      EventRegistrar.registerIfAbsent(this, var1);
      var2.aK.method933(new ItemHandlerBinding(PlayerInteractEvent.class, this::handleItemInteract));
   }

   @ItemEventHandler
   private void handleItemInteract(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (!ItemStackUtils.method7250(var1)) {
         if (var2.aoh.hasAction("liquid_analyzer")) {
            var1.setCancelled(true);
            Block var4;
            if (var1.getClickedBlock() != null) {
               var4 = var1.getClickedBlock().getRelative(var1.getBlockFace());
            } else {
               var4 = PlayerInteractionFacade.cL(var1.getPlayer());
            }

            if (ItemMaterialAction.method381(var4.getType())) {
               Levelled var5 = (Levelled)var4.getBlockData();
               CustomBlockChunkStoreBase var7 = this.afc.method8182(var4.getChunk());
               String var6;
               if (!var7.hasBlockEntryAt(var4)) {
                  if (var5.getLevel() > 0 && var5.getLevel() < var5.getMaximumLevel()) {
                     var1.getPlayer().sendMessage(Main.aFi.lj("liquid-analyzer-low-level"));
                     return;
                  }

                  var6 = var4.getType().toString();
               } else {
                  var6 = var7.getCustomBlockIdIfFlagged(var4);
               }

               if (var6 != null) {
                  var1.getPlayer().sendMessage(var6);
                  if (var1.getPlayer().getGameMode() != GameMode.CREATIVE) {
                     ItemStackUtils.au(var1.getItem());
                  }
               }
            } else {
               var1.getPlayer().sendMessage(Main.aFi.lj("liquid-analyzer-no-liquid"));
            }
         }
      }
   }
}
