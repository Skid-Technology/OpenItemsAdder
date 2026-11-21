package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.IntTableConstantsM;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

public class VisualEffect extends ItemEffect {
   public final Effect ajD;

   public VisualEffect(Effect var1) {
      this.ajD = var1;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      int var5 = (int)var4.getLocation().getYaw();
      byte var6 = 0;
      switch (IntTableConstantsM.SWITCH_MAP[BlockFaceUtils.yawToBlockFace(var5, false).ordinal()]) {
         case 1:
         case 2:
         case 3:
            var6 = 2;
            break;
         case 4:
         case 5:
         case 6:
            var6 = 3;
            break;
         case 7:
            var6 = 4;
            break;
         case 8:
            var6 = 5;
      }

      var4.getWorld().playEffect(var4.getEyeLocation(), this.ajD, var6);
      return EffectOutcome.failure();
   }

   @Override
   public EffectOutcome applyOnProjectileHit(ProjectileHitEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.getHitEntity() != null) {
         var1.getHitEntity().getWorld().playEffect(var1.getHitEntity().getLocation(), this.ajD, 0);
      } else {
         var1.getHitBlock().getWorld().playEffect(var1.getHitBlock().getLocation(), this.ajD, 0);
      }

      return EffectOutcome.failure();
   }
}
