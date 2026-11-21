package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R5Dup2;
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

class BlockInteractionGoalV1_21_R5Dup2 extends PathfinderGoal {
   private static final int bhU = 40;
   private static Predicate beh;
   private final EntityInsentient bhV;
   private final World bhW;
   private int bek;

   BlockInteractionGoalV1_21_R5Dup2(MobGoalFactoryImplV1_21_R5Dup2 var1, Mob var2, Predicate var3) {
      this.bhX = var2;
      this.bhY = var3;
      this.bhV = ((CraftMob)this.bhX).getHandle();
      this.bhW = this.bhV.ai();
      if (this.bhY == null) {
         beh = var0 -> false;
      } else {
         beh = var1x -> {
            BlockState var2x = var1x.createCraftBlockData().createBlockState();
            return var0.test(var2x);
         };
      }
   }

   public boolean method5078() {
      if (!((WorldServer)this.bhW).J.a.b(this.bhV.dz())) {
         return false;
      }

      if (this.bhV.ec().a(this.bhV.g_() ? 50 : 1000) != 0) {
         return false;
      }

      BlockPosition var1 = this.bhV.dx();
      return beh.test(this.bhW.a_(var1));
   }

   public void method5079() {
      this.bek = this.a(40);
      this.bhW.a(this.bhV, (byte)10);
      this.bhV.S().n();
   }

   public void method5080() {
      this.bek = 0;
   }

   public boolean method5081() {
      return this.bek > 0;
   }

   public void method5082() {
      this.bek = Math.max(0, this.bek - 1);
      if (this.bek == this.a(4)) {
         BlockPosition var1 = this.bhV.dx();
         IBlockData var2 = this.bhW.a_(var1);
         if (beh.test(var2)) {
            if (CraftEventFactory.callEntityChangeBlockEvent(this.bhV, var1, var2.y().g(), !a(this.bhW).P().c(GameRules.d))) {
               this.bhW.b(var1, false);
            }

            this.bhV.V();
         } else {
            BlockPosition var3 = var1.e();
            if (this.bhW.a_(var3).a(Blocks.i)) {
               if (CraftEventFactory.callEntityChangeBlockEvent(this.bhV, var3, Blocks.j.m(), !a(this.bhW).P().c(GameRules.d))) {
                  this.bhW.c(2001, var3, Block.j(Blocks.i.m()));
                  this.bhW.a(var3, Blocks.j.m(), 2);
               }

               this.bhV.V();
            }
         }
      }
   }
}
