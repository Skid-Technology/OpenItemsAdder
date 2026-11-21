package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R6;
import java.util.function.Predicate;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftMob;
import org.bukkit.craftbukkit.v1_21_R6.event.CraftEventFactory;
import org.bukkit.entity.Mob;

class BlockInteractionGoalV1_21_R6 extends PathfinderGoal {
   private static final int bfg = 40;
   private static Predicate beh;
   private final EntityInsentient bfh;
   private final World bfi;
   private int bek;

   BlockInteractionGoalV1_21_R6(MobGoalFactoryImplV1_21_R6 var1, Mob var2, Predicate var3) {
      this.bfj = var2;
      this.bfk = var3;
      this.bfh = ((CraftMob)this.bfj).getHandle();
      this.bfi = this.bfh.an();
      if (this.bfk == null) {
         beh = var0 -> false;
      } else {
         beh = var1x -> {
            BlockState var2x = var1x.createCraftBlockData().createBlockState();
            return var0.test(var2x);
         };
      }
   }

   public boolean method4910() {
      if (!((WorldServer)this.bfi).H.a.b(this.bfh.dH())) {
         return false;
      }

      if (this.bfh.ek().a(this.bfh.g_() ? 50 : 1000) != 0) {
         return false;
      }

      BlockPosition var1 = this.bfh.dF();
      return beh.test(this.bfi.a_(var1));
   }

   public void method4911() {
      this.bek = this.a(40);
      this.bfi.a(this.bfh, (byte)10);
      this.bfh.S().n();
   }

   public void method4912() {
      this.bek = 0;
   }

   public boolean method4913() {
      return this.bek > 0;
   }

   public void method4914() {
      this.bek = Math.max(0, this.bek - 1);
      if (this.bek == this.a(4)) {
         BlockPosition var1 = this.bfh.dF();
         IBlockData var2 = this.bfi.a_(var1);
         if (beh.test(var2)) {
            if (CraftEventFactory.callEntityChangeBlockEvent(this.bfh, var1, var2.y().g(), !a(this.bfi).S().c(GameRules.d))) {
               this.bfi.b(var1, false);
            }

            this.bfh.V();
         } else {
            BlockPosition var3 = var1.e();
            if (this.bfi.a_(var3).a(Blocks.i)) {
               if (CraftEventFactory.callEntityChangeBlockEvent(this.bfh, var3, Blocks.j.m(), !a(this.bfi).S().c(GameRules.d))) {
                  this.bfi.c(2001, var3, Block.j(Blocks.i.m()));
                  this.bfi.a(var3, Blocks.j.m(), 2);
               }

               this.bfh.V();
            }
         }
      }
   }
}
