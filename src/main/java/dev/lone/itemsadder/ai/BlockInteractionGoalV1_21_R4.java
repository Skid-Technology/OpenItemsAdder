package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R4;
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
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftMob;
import org.bukkit.craftbukkit.v1_21_R4.event.CraftEventFactory;
import org.bukkit.entity.Mob;

class BlockInteractionGoalV1_21_R4 extends PathfinderGoal {
   private static final int bgY = 40;
   private static Predicate beh;
   private final EntityInsentient bgZ;
   private final World bha;
   private int bek;

   BlockInteractionGoalV1_21_R4(MobGoalFactoryImplV1_21_R4 var1, Mob var2, Predicate var3) {
      this.bhb = var2;
      this.bhc = var3;
      this.bgZ = ((CraftMob)this.bhb).getHandle();
      this.bha = this.bgZ.dV();
      if (this.bhc == null) {
         beh = var0 -> false;
      } else {
         beh = var1x -> {
            BlockState var2x = var1x.createCraftBlockData().createBlockState();
            return var0.test(var2x);
         };
      }
   }

   public boolean method5032() {
      if (!((WorldServer)this.bha).J.a.b(this.bgZ.dx())) {
         return false;
      }

      if (this.bgZ.dY().a(this.bgZ.e_() ? 50 : 1000) != 0) {
         return false;
      }

      BlockPosition var1 = this.bgZ.dv();
      return beh.test(this.bha.a_(var1));
   }

   public void method5033() {
      this.bek = this.a(40);
      this.bha.a(this.bgZ, (byte)10);
      this.bgZ.N().m();
   }

   public void method5034() {
      this.bek = 0;
   }

   public boolean method5035() {
      return this.bek > 0;
   }

   public int OX() {
      return this.bek;
   }

   public void method5036() {
      this.bek = Math.max(0, this.bek - 1);
      if (this.bek == this.a(4)) {
         BlockPosition var1 = this.bgZ.dv();
         IBlockData var2 = this.bha.a_(var1);
         if (beh.test(var2)) {
            if (CraftEventFactory.callEntityChangeBlockEvent(this.bgZ, var1, var2.y().g(), !a(this.bha).O().c(GameRules.d))) {
               this.bha.b(var1, false);
            }

            this.bgZ.Q();
         } else {
            BlockPosition var3 = var1.e();
            if (this.bha.a_(var3).a(Blocks.i)) {
               if (CraftEventFactory.callEntityChangeBlockEvent(this.bgZ, var3, Blocks.j.m(), !a(this.bha).O().c(GameRules.d))) {
                  this.bha.c(2001, var3, Block.j(Blocks.i.m()));
                  this.bha.a(var3, Blocks.j.m(), 2);
               }

               this.bgZ.Q();
            }
         }
      }
   }
}
