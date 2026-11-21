package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.block.CustomBlockListener;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.world.BlockEffectsService;
import dev.lone.itemsadder.world.ChunkBlockHookHolder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Bed;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class PlacementEffect extends ItemEffect {
   public final int ajw;
   public final int ajx;
   public final boolean ajy;
   public final boolean ajz;
   public final boolean ajA;
   public final boolean ajB;

   public PlacementEffect(int var1, int var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      this.ajw = var1;
      this.ajx = var2;
      this.ajy = var3;
      this.ajz = var4;
      this.ajA = var5;
      this.ajB = var6;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      return EffectOutcome.failure();
   }

   @Override
   public EffectOutcome applyOnBlockBreak(BlockBreakEvent var1, CustomItemDefinition var2, ItemStack var3) {
      Player var4 = var1.getPlayer();
      Block var5 = var1.getBlock();
      if (var5.getType().getHardness() == 0.0F) {
         return EffectOutcome.success();
      }

      BlockFace var6 = CustomBlockListener.dn.method714(var4);
      ArrayList var7 = new ArrayList();
      ArrayList var8 = new ArrayList();
      boolean var9 = var3.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH);
      if (this.ajy) {
         if (!this.canPlaceAt(var4, var6, this.ajw, var5, var7)) {
            return EffectOutcome.failure();
         }
      } else {
         this.placeBlocks(var6, this.ajw, var5, var7);
      }

      int var10 = 0;
      HashMap var11 = new HashMap();
      HashMap var12 = new HashMap();

      for (Block var14 : var7) {
         if (!var14.equals(var5) && !BlockUtils.isProtectedMaterial(var14.getType()) && (!this.ajz || !BlockUtils.isOreLike(var14))) {
            CustomBlockDefinition var15 = CustomBlockDefinition.isCustomBlockType(var14) ? CustomBlockDefinition.getDefinitionAt(var14) : null;
            if ((var15 != null ? var15.matchesPlacementItem(var3) : BlockEffectsService.getAdapter().isHarvestableWith(var14, var3))
               && BlockFaceUtils.simulateBreak(var14, var4)) {
               Collection var16 = var14.getDrops();
               if (var14.getBlockData() instanceof Bisected var17) {
                  Block var26 = var14.getRelative(var17.getHalf() == Half.TOP ? BlockFace.DOWN : BlockFace.UP);
                  if (var26.getType() == var14.getType()) {
                     var8.add(var26);
                  }
               } else if (var14.getBlockData() instanceof Bed var18) {
                  Block var28 = var14.getRelative(var18.getFacing().getOppositeFace());
                  if (var28.getType() == var14.getType()) {
                     var8.add(var28);
                  }
               }

               var8.add(var14);
               var10++;
               if ((!this.ajB || var9) && this.ajA) {
                  if (!var16.isEmpty()) {
                     for (ItemStack var24 : var16) {
                        if (CustomBlockDefinition.isCustomBlockMaterial(var24.getType()) && var15 != null) {
                           this.addBlockEntry(var12, var15);
                        } else if (var11.containsKey(var24.getType())) {
                           var11.put(var24.getType(), (Integer)var11.get(var24.getType()) + var24.getAmount());
                        } else {
                           var11.put(var24.getType(), var24.getAmount());
                        }
                     }
                  } else if (var15 != null) {
                     this.addBlockEntry(var12, var15);
                  } else {
                     Material var23 = var14.getType();
                     if (var23 == Material.STONE) {
                        Material var25 = var9 ? Material.STONE : Material.COBBLESTONE;
                        if (var11.containsKey(var25)) {
                           var11.put(var25, (Integer)var11.get(var25) + 1);
                        } else {
                           var11.put(var25, 1);
                        }
                     } else if (var11.containsKey(var23)) {
                        var11.put(var23, (Integer)var11.get(var23) + 1);
                     } else {
                        var11.put(var23, 1);
                     }
                  }
               }
            }
         }
      }

      if (!var8.isEmpty()) {
         BlockEffectsService.clearBlocks(var8, var5);

         for (Block var21 : var8) {
            ContentManager.method8084().method8183(var21).removeCustomBlock(var21);
         }

         if (ChunkBlockHookHolder.isAntiXrayEnabled(var5.getWorld())) {
            BlockEffectsService.refreshNeighbors(var8, var5);
         }
      }

      CustomItemDefinition.method8826(var3, var10, var4);
      var11.forEach((var1x, var2x) -> LocationScanner.dropItem(var5.getLocation(), new ItemStack(var1x, var2x)));
      var12.forEach((var1x, var2x) -> LocationScanner.dropItem(var5.getLocation(), var2x));
      return EffectOutcome.failure();
   }

   private void placeBlocks(BlockFace var1, int var2, Block var3, List var4) {
      ArrayList var5 = new ArrayList();
      int var6 = var2 / 3;
      if (var1 == BlockFace.UP) {
         Location var7 = var3.getLocation().add(-var6, 0.0, var6);
         Location var8 = var3.getLocation().add(var6, -this.ajx, -var6);
         var4.addAll(BlockUtils.getBlocksInRegionCurrentWorld(var7, var8));
      } else if (var1 == BlockFace.DOWN) {
         Location var9 = var3.getLocation().add(-var6, 0.0, var6);
         Location var18 = var3.getLocation().add(var6, this.ajx, -var6);
         var4.addAll(BlockUtils.getBlocksInRegionCurrentWorld(var9, var18));
      } else if (var1 == BlockFace.NORTH) {
         Location var10 = var3.getLocation().add(-var6, var6, 0.0);
         Location var19 = var3.getLocation().add(var6, -var6, this.ajx);
         var4.addAll(BlockUtils.getBlocksInRegionCurrentWorld(var10, var19));
      } else if (var1 == BlockFace.SOUTH) {
         Location var11 = var3.getLocation().add(-var6, var6, 0.0);
         Location var20 = var3.getLocation().add(var6, -var6, -this.ajx);
         var4.addAll(BlockUtils.getBlocksInRegionCurrentWorld(var11, var20));
      } else if (var1 == BlockFace.EAST) {
         Location var12 = var3.getLocation().add(0.0, var6, -var6);
         Location var21 = var3.getLocation().add(-this.ajx, -var6, var6);
         var4.addAll(BlockUtils.getBlocksInRegionCurrentWorld(var12, var21));
      } else if (var1 == BlockFace.WEST) {
         Location var13 = var3.getLocation().add(0.0, var6, -var6);
         Location var22 = var3.getLocation().add(this.ajx, -var6, var6);
         var4.addAll(BlockUtils.getBlocksInRegionCurrentWorld(var13, var22));
      }

      if (var1 == BlockFace.UP || var1 == BlockFace.DOWN) {
         Location var16 = var3.getLocation().add(-var6, 0.0, var6);
         Location var25 = var3.getLocation().add(var6, 0.0, -var6);
         var5.addAll(BlockUtils.getBlocksInRegionCurrentWorld(var16, var25));
      } else if (var1 == BlockFace.NORTH || var1 == BlockFace.SOUTH) {
         Location var15 = var3.getLocation().add(-var6, var6, 0.0);
         Location var24 = var3.getLocation().add(var6, -var6, 0.0);
         var5.addAll(BlockUtils.getBlocksInRegionCurrentWorld(var15, var24));
      } else if (var1 == BlockFace.EAST || var1 == BlockFace.WEST) {
         Location var14 = var3.getLocation().add(0.0, var6, -var6);
         Location var23 = var3.getLocation().add(0.0, -var6, var6);
         var5.addAll(BlockUtils.getBlocksInRegionCurrentWorld(var14, var23));
      }

      for (Block var26 : var5) {
         this.at(var26);
      }
   }

   private boolean canPlaceAt(Player var1, BlockFace var2, int var3, Block var4, List var5) {
      World var6 = var4.getWorld();
      Location var7 = var4.getLocation();
      BlockFace var8 = var2.getOppositeFace();
      if (var8 != BlockFace.UP && var8 != BlockFace.DOWN) {
         if (var7.getY() > var1.getLocation().getY()) {
            var7.subtract(0.0, 1.0, 0.0);
         }

         for (int var10 = 0; var10 < var3; var10++) {
            Block var9 = var6.getBlockAt(var7);
            var5.add(var9);
            this.at(var9);
            var7.add(0.0, 1.0, 0.0);
         }

         return true;
      } else {
         return false;
      }
   }

   private void addBlockEntry(HashMap var1, CustomBlockDefinition var2) {
      ItemStack var3;
      if (var1.containsKey(var2.getNamespacedID())) {
         var3 = (ItemStack)var1.get(var2.getNamespacedID());
         var3.setAmount(var3.getAmount() + 1);
      } else {
         var3 = var2.Ds();
         var3.setAmount(1);
      }

      var1.put(var2.getNamespacedID(), var3);
   }

   private void at(Block var1) {
      var1.getLocation().getBlock().getWorld().playEffect(var1.getLocation(), Effect.STEP_SOUND, var1.getType());
   }
}
