package dev.lone.itemsadder.item;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.text.NmsChatService;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.SoundUtils;
import dev.lone.itemsadder.util.Utils;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BlockBreakListener implements IaListener {
   private final ConcurrentHashMap afe = new ConcurrentHashMap();
   InventoryMoveListener aff;

   public BlockBreakListener(Plugin var1) {
      EventRegistrar.registerIfAbsent(this, var1);
      this.aff = new InventoryMoveListener(this);
      EventRegistrar.registerIfAbsent(this.aff, var1);
      SchedulerUtils.scheduleRepeating(var1x -> {
         for (Block var3 : this.afe.keySet()) {
            if (BlockFaceUtils.isChunkLoadedAt(var3)) {
               var3.getWorld().spawnParticle(Particle.NOTE, BlockFaceUtils.topCenter(var3), 1);
               if (var3.getType() != Material.JUKEBOX) {
                  SchedulerUtils.runOnMainThread(() -> this.method547(var3));
               }
            } else {
               this.afe.remove(var3);
            }
         }
      }, 20L, 20L);
   }

   @Override
   public void unregister() {
      IaListener.super.unregister();
      IaListener.method532(this.aff);
   }

   @EventHandler
   private void method539(PlayerInteractEvent var1) {
      if (!ItemStackUtils.method7250(var1)) {
         Block var2 = var1.getClickedBlock();
         if (var1.getAction() == Action.RIGHT_CLICK_BLOCK && var2.getType() == Material.JUKEBOX) {
            CustomBlockChunkStoreBase var3 = ContentManager.method8084().method8183(var2);
            if (!this.afe.containsKey(var2) && var3.getBlockStringData(var2) == null) {
               Jukebox var4 = (Jukebox)var2.getState();
               if (!var4.isPlaying() && var4.getRecord().getType() == Material.AIR) {
                  if (CustomItemDefinition.isCustomItem(var1.getItem())) {
                     CustomItemDefinition var5 = ContentManager.method8084().method8149(var1.getItem());
                     if (var5 != null) {
                        if (!var5.aoh.hasAction("music_disc")) {
                           return;
                        }

                        ItemCommandAction var6 = (ItemCommandAction)var5.aoh.getAction("music_disc");
                        if (var6.aex == null) {
                           return;
                        }

                        ItemStack var7 = var1.getItem().clone();
                        var7.setAmount(1);
                        String var8 = var6.aew;
                        var8 = Utils.toSectionCodes(var8);
                        var4.setRecord(var7);
                        if (!var4.update()) {
                           Log.warn("Failed to put custom music disc into jukebox: " + BlockFaceUtils.locationCoordsString(var2.getLocation()));
                           return;
                        }

                        var3.setBlockStringData(var2, var6.aex);
                        NmsChatService.Nq()
                           .method2955(var1.getPlayer(), AdventureText.toNative(AdventureText.fromLegacy(Main.aFi.lj("now-playing").replace("{value}", var8))));
                        SoundUtils.playSound(var2.getLocation(), var6.aex, SoundCategory.RECORDS, 1.0F, 1.0F);
                        ItemStackUtils.dl(var1.getPlayer());
                        this.afe.put(var2, var6.aex);
                     }
                  }
               }
            } else {
               this.method548(var2, var3);
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void method546(BlockBreakEvent var1) {
      if (!EventRegistrar.isCustomBlockBreakEvent(var1)) {
         if (var1.getBlock().getType() == Material.JUKEBOX) {
            this.method547(var1.getBlock());
         }
      }
   }

   private void method547(Block var1) {
      CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8183(var1);
      this.method548(var1, var2);
   }

   private void method548(Block var1, CustomBlockChunkStoreBase var2) {
      String var3 = (String)this.afe.remove(var1);
      if (var3 == null) {
         var3 = var2.getBlockStringData(var1);
      }

      if (var3 != null) {
         SoundUtils.playSound(var1.getLocation(), SoundCategory.RECORDS, var3);
         ItemStack var4 = var1.getState() instanceof Jukebox var5 ? var5.getRecord() : null;
         ItemStack var7 = method549(var4, var2.getStoredItem(var1));
         if (var7 != null) {
            Location var8 = BlockFaceUtils.topCenter(var1);
            var8.subtract(0.5, 0.0, 0.5);
            var1.getWorld().dropItemNaturally(var8, var7);
         }

         var2.removeCustomBlock(var1);
      }
   }

   static ItemStack method549(ItemStack var0, ItemStack var1) {
      return var0 != null && var0.getType() != Material.AIR ? null : var1;
   }
}
