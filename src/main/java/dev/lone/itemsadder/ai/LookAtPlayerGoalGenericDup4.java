package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplGenericDup4;
import dev.lone.itemsadder.nms.MobGoalService;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.level.gamerules.GameRules;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public class LookAtPlayerGoalGenericDup4 extends TargetGoal {
   private int bes;

   LookAtPlayerGoalGenericDup4(MobGoalFactoryImplGenericDup4 var1, Mob var2, boolean var3, float var4) {
      super(var2, var3);
      this.bkt = var4;
   }

   public boolean canUse() {
      int var1 = this.mob.getLastHurtByMobTimestamp();
      LivingEntity var2 = this.mob.getLastHurtByMob();
      if (var1 != this.bes && var2 != null) {
         if (var2.getType() == EntityTypes.PLAYER && (Boolean)getServerLevel(this.mob).getGameRules().get(GameRules.UNIVERSAL_ANGER)) {
            return false;
         }

         this.bes = var1;
         return this.mob.getRandom().nextFloat() < this.bkt ? this.Pa() : true;
      } else {
         return false;
      }
   }

   public boolean canContinueToUse() {
      return false;
   }

   public void start() {
      MobGoalService.method4852(MobGoalFactoryImplGenericDup4.bjZ, this.mob, this.mob.getLastHurtByMob(), TargetReason.TARGET_ATTACKED_ENTITY);
      this.targetMob = this.mob.getTarget();
      this.unseenMemoryTicks = 300;
      super.start();
   }

   private boolean Pa() {
      double var1 = this.mob.getX() + (this.mob.getRandom().nextDouble() - 0.5) * 32.0;
      double var3 = this.mob.getY() + (this.mob.getRandom().nextInt(16) - 8);
      double var5 = this.mob.getZ() + (this.mob.getRandom().nextDouble() - 0.5) * 32.0;
      return this.mob.randomTeleport(var1, var3, var5, true, BlockTags.DANGEROUS_FOR_TELEPORTATION);
   }
}
