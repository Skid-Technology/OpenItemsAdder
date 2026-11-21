package dev.lone.itemsadder.loot;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.internal.SwitchMap3;
import dev.lone.itemsadder.item.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ToolDropRule extends NbtDropConditions {
   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public boolean method1995(ItemStack var1, Material var2) {
      if (var1.getType() == Material.SHEARS) {
         switch (SwitchMap3.atw[var2.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
               return true;
            default:
               return var2.toString().endsWith("_LEAVES");
         }
      } else {
         return var2 == Material.SPAWNER && Settings.get().allowSpawnerLootSilkTouch ? false : ItemStackUtils.method7218(var1, Enchantment.SILK_TOUCH);
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public boolean method1996(ItemStack var1, Material var2) {
      if (var1.getType() == Material.SHEARS) {
         switch (SwitchMap3.atw[var2.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
               return true;
            default:
               return var2.toString().endsWith("_LEAVES");
         }
      } else {
         return var2 == Material.SPAWNER && Settings.get().allowSpawnerLootSilkTouch ? false : ItemStackUtils.method7218(var1, Enchantment.SILK_TOUCH);
      }
   }
}
