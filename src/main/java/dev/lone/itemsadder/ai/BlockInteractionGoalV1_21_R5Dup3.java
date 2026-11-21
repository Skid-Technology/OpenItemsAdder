package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R5Dup3;
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

class BlockInteractionGoalV1_21_R5Dup3 extends PathfinderGoal {
   private static final int bis = 40;
   private static Predicate beh;
   private final EntityInsentient bit;
   private final World biu;
   private int bek;

   BlockInteractionGoalV1_21_R5Dup3(MobGoalFactoryImplV1_21_R5Dup3 var1, Mob var2, Predicate var3) {
      this.biv = var2;
      this.biw = var3;
      this.bit = ((CraftMob)this.biv).getHandle();
      this.biu = this.bit.ai();
      if (this.biw == null) {
         beh = var0 -> false;
      } else {
         beh = var1x -> {
            BlockState var2x = var1x.createCraftBlockData().createBlockState();
            return var0.test(var2x);
         };
      }
   }

   public boolean method5101() {
      if (!((WorldServer)this.biu).J.a.b(this.bit.dz())) {
         return false;
      }

      if (this.bit.ec().a(this.bit.g_() ? 50 : 1000) != 0) {
         return false;
      }

      BlockPosition var1 = this.bit.dx();
      return beh.test(this.biu.a_(var1));
   }

   public void method5102() {
      this.bek = this.a(40);
      this.biu.a(this.bit, (byte)10);
      this.bit.S().n();
   }

   public void method5103() {
      this.bek = 0;
   }

   public boolean method5104() {
      return this.bek > 0;
   }

   public void method5105() {
      this.bek = Math.max(0, this.bek - 1);
      if (this.bek == this.a(4)) {
         BlockPosition var1 = this.bit.dx();
         IBlockData var2 = this.biu.a_(var1);
         if (beh.test(var2)) {
            if (CraftEventFactory.callEntityChangeBlockEvent(this.bit, var1, var2.y().g(), !a(this.biu).P().c(GameRules.d))) {
               this.biu.b(var1, false);
            }

            this.bit.V();
         } else {
            BlockPosition var3 = var1.e();
            if (this.biu.a_(var3).a(Blocks.i)) {
               if (CraftEventFactory.callEntityChangeBlockEvent(this.bit, var3, Blocks.j.m(), !a(this.biu).P().c(GameRules.d))) {
                  this.biu.c(2001, var3, Block.j(Blocks.i.m()));
                  this.biu.a(var3, Blocks.j.m(), 2);
               }

               this.bit.V();
            }
         }
      }
   }
}
