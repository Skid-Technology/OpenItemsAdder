package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R5;
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
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftMob;
import org.bukkit.craftbukkit.v1_21_R5.event.CraftEventFactory;
import org.bukkit.entity.Mob;

class BlockInteractionGoalV1_21_R5 extends PathfinderGoal {
   private static final int bhw = 40;
   private static Predicate beh;
   private final EntityInsentient bhx;
   private final World bhy;
   private int bek;

   BlockInteractionGoalV1_21_R5(MobGoalFactoryImplV1_21_R5 var1, Mob var2, Predicate var3) {
      this.bhz = var2;
      this.bhA = var3;
      this.bhx = ((CraftMob)this.bhz).getHandle();
      this.bhy = this.bhx.ai();
      if (this.bhA == null) {
         beh = var0 -> false;
      } else {
         beh = var1x -> {
            BlockState var2x = var1x.createCraftBlockData().createBlockState();
            return var0.test(var2x);
         };
      }
   }

   public boolean method5055() {
      if (!((WorldServer)this.bhy).J.a.b(this.bhx.dz())) {
         return false;
      }

      if (this.bhx.ec().a(this.bhx.g_() ? 50 : 1000) != 0) {
         return false;
      }

      BlockPosition var1 = this.bhx.dx();
      return beh.test(this.bhy.a_(var1));
   }

   public void method5056() {
      this.bek = this.a(40);
      this.bhy.a(this.bhx, (byte)10);
      this.bhx.S().n();
   }

   public void method5057() {
      this.bek = 0;
   }

   public boolean method5058() {
      return this.bek > 0;
   }

   public void method5059() {
      this.bek = Math.max(0, this.bek - 1);
      if (this.bek == this.a(4)) {
         BlockPosition var1 = this.bhx.dx();
         IBlockData var2 = this.bhy.a_(var1);
         if (beh.test(var2)) {
            if (CraftEventFactory.callEntityChangeBlockEvent(this.bhx, var1, var2.y().g(), !a(this.bhy).P().c(GameRules.d))) {
               this.bhy.b(var1, false);
            }

            this.bhx.V();
         } else {
            BlockPosition var3 = var1.e();
            if (this.bhy.a_(var3).a(Blocks.i)) {
               if (CraftEventFactory.callEntityChangeBlockEvent(this.bhx, var3, Blocks.j.m(), !a(this.bhy).P().c(GameRules.d))) {
                  this.bhy.c(2001, var3, Block.j(Blocks.i.m()));
                  this.bhy.a(var3, Blocks.j.m(), 2);
               }

               this.bhx.V();
            }
         }
      }
   }
}
