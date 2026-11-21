package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R3;
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
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftMob;
import org.bukkit.craftbukkit.v1_21_R3.event.CraftEventFactory;
import org.bukkit.entity.Mob;

class BlockInteractionGoalV1_21_R3 extends PathfinderGoal {
   private static final int bgA = 40;
   private static Predicate beh;
   private final EntityInsentient bgB;
   private final World bgC;
   private int bek;

   BlockInteractionGoalV1_21_R3(MobGoalFactoryImplV1_21_R3 var1, Mob var2, Predicate var3) {
      this.bgD = var2;
      this.bgE = var3;
      this.bgB = ((CraftMob)this.bgD).getHandle();
      this.bgC = this.bgB.dV();
      if (this.bgE == null) {
         beh = var0 -> false;
      } else {
         beh = var1x -> {
            BlockState var2x = var1x.createCraftBlockData().createBlockState();
            return var0.test(var2x);
         };
      }
   }

   public boolean method5001() {
      if (!((WorldServer)this.bgC).J.a.b(this.bgB.dx())) {
         return false;
      }

      if (this.bgB.dY().a(this.bgB.e_() ? 50 : 1000) != 0) {
         return false;
      }

      BlockPosition var1 = this.bgB.dv();
      return beh.test(this.bgC.a_(var1));
   }

   public void method5002() {
      this.bek = this.a(40);
      this.bgC.a(this.bgB, (byte)10);
      this.bgB.P().m();
   }

   public void method5003() {
      this.bek = 0;
   }

   public boolean method5004() {
      return this.bek > 0;
   }

   public int OX() {
      return this.bek;
   }

   public void method5005() {
      this.bek = Math.max(0, this.bek - 1);
      if (this.bek == this.a(4)) {
         BlockPosition var1 = this.bgB.dv();
         IBlockData var2 = this.bgC.a_(var1);
         if (beh.test(var2)) {
            if (CraftEventFactory.callEntityChangeBlockEvent(this.bgB, var1, var2.y().g(), !a(this.bgC).O().b(GameRules.c))) {
               this.bgC.b(var1, false);
            }

            this.bgB.S();
         } else {
            BlockPosition var3 = var1.e();
            if (this.bgC.a_(var3).a(Blocks.i)) {
               if (CraftEventFactory.callEntityChangeBlockEvent(this.bgB, var3, Blocks.j.m(), !a(this.bgC).O().b(GameRules.c))) {
                  this.bgC.c(2001, var3, Block.j(Blocks.i.m()));
                  this.bgC.a(var3, Blocks.j.m(), 2);
               }

               this.bgB.S();
            }
         }
      }
   }
}
