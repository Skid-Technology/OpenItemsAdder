package dev.lone.itemsadder.item;

import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.world.InstrumentRegistry;
import dev.lone.itemsadder.worldgen.TreeDecorator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemTreeAction extends ItemActionBase {
   public static final String aey = "sapling";
   private final TreeDecorator aez;

   public ItemTreeAction(CustomItemDefinition var1, TreeDecorator var2) {
      super(var1);
      this.aez = var2;
   }

   public TreeDecorator getTreeDecorator() {
      return this.aez;
   }

   public void plantSapling(ItemStack var1, Player var2, Block var3) {
      if (this.aez != null) {
         if (BlockFaceUtils.simulatePlace(var3, var2)) {
            if (InstrumentRegistry.aP(var3)) {
               var3.setType(Material.AIR, false);
            }

            if (this.aez.method2321(var3, false)) {
               ItemStackUtils.au(var1);
            }
         }
      }
   }
}
