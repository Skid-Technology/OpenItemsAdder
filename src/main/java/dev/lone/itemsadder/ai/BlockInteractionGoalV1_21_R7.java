package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R7;
import java.util.function.Predicate;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.gamerules.GameRules;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftMob;
import org.bukkit.craftbukkit.v1_21_R7.event.CraftEventFactory;
import org.bukkit.entity.Mob;

public class BlockInteractionGoalV1_21_R7 extends PathfinderGoal {
   private static final int bfE = 40;
   private static Predicate beh;
   private final EntityInsentient bfF;
   private final World bfG;
   private int bek;

   BlockInteractionGoalV1_21_R7(MobGoalFactoryImplV1_21_R7 var1, Mob var2, Predicate var3) {
      this.bfH = var2;
      this.bfI = var3;
      this.bfF = ((CraftMob)this.bfH).getHandle();
      this.bfG = this.bfF.ao();
      if (this.bfI == null) {
         beh = var0 -> false;
      } else {
         beh = var1x -> {
            BlockState var2x = var1x.createCraftBlockData().createBlockState();
            return var0.test(var2x);
         };
      }
   }

   public boolean method4955() {
      if (!((WorldServer)this.bfG).G.a.b(this.bfF.dM())) {
         return false;
      }

      if (this.bfF.ep().a(this.bfF.e_() ? 50 : 1000) != 0) {
         return false;
      }

      BlockPosition var1 = this.bfF.dK();
      return beh.test(this.bfG.a_(var1));
   }

   public void method4956() {
      this.bek = this.a(40);
      this.bfG.a(this.bfF, (byte)10);
      this.bfF.N().n();
   }

   public void method4957() {
      this.bek = 0;
   }

   public boolean method4958() {
      return this.bek > 0;
   }

   public void method4959() {
      this.bek = Math.max(0, this.bek - 1);
      if (this.bek == this.a(4)) {
         BlockPosition var1 = this.bfF.dK();
         IBlockData var2 = this.bfG.a_(var1);
         if (beh.test(var2)) {
            if (CraftEventFactory.callEntityChangeBlockEvent(this.bfF, var1, var2.y().g(), !(Boolean)a(this.bfG).U().a(GameRules.MOB_GRIEFING))) {
               this.bfG.b(var1, false);
            }

            this.bfF.R();
         } else {
            BlockPosition var3 = var1.e();
            if (this.bfG.a_(var3).a(Blocks.GRASS_BLOCK)) {
               if (CraftEventFactory.callEntityChangeBlockEvent(this.bfF, var3, Blocks.DIRT.m(), !(Boolean)a(this.bfG).U().a(GameRules.MOB_GRIEFING))) {
                  this.bfG.c(2001, var3, Block.j(Blocks.GRASS_BLOCK.m()));
                  this.bfG.a(var3, Blocks.DIRT.m(), 2);
               }

               this.bfF.R();
            }
         }
      }
   }
}
