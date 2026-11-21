package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.MobGoalSettings;
import dev.lone.itemsadder.entity.MobInventoryHelper;
import dev.lone.itemsadder.entity.MobPersistentKeys;
import dev.lone.itemsadder.util.IntTableConstants;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Mob;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;

final class MobGoalBlockInteract extends AbstractMobGoal {
   private final BlockSearchScanner abh;
   private Block aaU;
   private int abi;

   MobGoalBlockInteract(Mob var1, MobGoalSettings var2, NavigatorAdapter var3) {
      super(var1, var2, var3);
      this.abh = new BlockSearchScanner((int)var2.getDouble("range"), var2.getInt("vertical_range"), var2.getInt("scan_budget"));
   }

   private boolean method4587(Block var1) {
      return !this.aan.getSet("blocks").contains(var1.getType())
         ? false
         : !this.aan.zq().equals("harvest_crop") || var1.getBlockData() instanceof Ageable var2 && var2.getAge() == var2.getMaximumAge();
   }

   @Override
   protected boolean yX() {
      if (MobPersistentKeys.method9611(this.aam)
         && MobInventoryHelper.method9591(MobInventoryHelper.method9582(this.aam), this.aan.getStringList("consume_items"))) {
         this.aaU = this.abh.method9629(this.aam, this::method4587);
         this.abi = -1;
         return this.aaU != null;
      } else {
         return false;
      }
   }

   @Override
   protected boolean yZ() {
      return MobPersistentKeys.method9611(this.aam)
         && this.aaU != null
         && this.aaU.getWorld() == this.aam.getWorld()
         && this.aam.getWorld().isChunkLoaded(this.aaU.getX() >> 4, this.aaU.getZ() >> 4)
         && this.method4587(this.aaU);
   }

   @Override
   protected void za() {
   }

   @Override
   protected void zb() {
      Location var1 = this.aaU.getLocation().add(0.5, 1.0, 0.5);
      if (!this.method4570(var1)) {
         this.abi = -1;
         this.method4571(var1);
      } else {
         this.aao.stop();
         this.aao.method4592(this.aaU.getLocation().add(0.5, 0.5, 0.5));
         if (this.abi < 0) {
            this.abi = this.ms;
            this.bL("prepare");
         }

         if (this.ms - this.abi >= this.aan.getInt("work_ticks")) {
            this.aap = true;
            BlockData var2 = this.aaU.getBlockData();
            Object var3 = var2.clone();
            ArrayList var4 = new ArrayList(this.aan.getStringList("produce_items"));
            if (this.aan.zq().equals("harvest_crop")) {
               var4.addAll(this.aaU.getDrops(new ItemStack(Material.AIR), this.aam));
               var3 = Material.AIR.createBlockData();
               if (this.aan.getBoolean("replant") && var2 instanceof Ageable var5) {
                  Material var6 = method4588(this.aaU.getType());
                  if (var6 != null) {
                     for (ItemStack var8 : var4) {
                        if (var8.getType() == var6 && var8.getAmount() > 0) {
                           var8.setAmount(var8.getAmount() - 1);
                           Ageable var9 = (Ageable)var5.clone();
                           var9.setAge(0);
                           var3 = var9;
                           break;
                        }
                     }
                  }

                  var4.removeIf(var0 -> var0.getAmount() == 0);
               }
            } else if (!this.aan.getString("replace_with").equals("unchanged")) {
               var3 = Material.valueOf(this.aan.getString("replace_with")).createBlockData();
            }

            EntityChangeBlockEvent var10 = new EntityChangeBlockEvent(this.aam, this.aaU, (BlockData)var3);
            Bukkit.getPluginManager().callEvent(var10);
            if (!var10.isCancelled() && MobPersistentKeys.method9611(this.aam) && this.aaU.getBlockData().equals(var2)) {
               if (MobInventoryHelper.method9593(
                  this.aam, this.aan.getStringList("consume_items"), var4, this.aan.getInt("capacity"), this.aan.getString("output").equals("world")
               )) {
                  this.aaU.setBlockData((BlockData)var3, false);
                  this.bL("execute");
               }
            }
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static Material method4588(Material var0) {
      return switch (IntTableConstants.SWITCH_MAP[var0.ordinal()]) {
         case 1 -> Material.WHEAT_SEEDS;
         case 2 -> Material.CARROT;
         case 3 -> Material.POTATO;
         case 4 -> Material.BEETROOT_SEEDS;
         case 5 -> Material.NETHER_WART;
         case 6 -> Material.SWEET_BERRIES;
         default -> null;
      };
   }
}
