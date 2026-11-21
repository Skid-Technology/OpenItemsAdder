package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R1;
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
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftMob;
import org.bukkit.craftbukkit.v1_21_R1.event.CraftEventFactory;
import org.bukkit.entity.Mob;

class BlockInteractionGoalV1_21_R1 extends PathfinderGoal {
   private static final int beI = 40;
   private static Predicate beh;
   private final EntityInsentient beJ;
   private final World beK;
   private int bek;

   BlockInteractionGoalV1_21_R1(MobGoalFactoryImplV1_21_R1 var1, Mob var2, Predicate var3) {
      this.beL = var2;
      this.beM = var3;
      this.beJ = ((CraftMob)this.beL).getHandle();
      this.beK = this.beJ.dO();
      if (this.beM == null) {
         beh = var0 -> false;
      } else {
         beh = var1x -> {
            BlockState var2x = var1x.createCraftBlockData().createBlockState();
            return var0.test(var2x);
         };
      }
   }

   public boolean method4887() {
      if (!((WorldServer)this.beK).I.a.b(this.beJ.dq())) {
         return false;
      }

      if (this.beJ.dR().a(this.beJ.o_() ? 50 : 1000) != 0) {
         return false;
      }

      BlockPosition var1 = this.beJ.do_();
      return beh.test(this.beK.a_(var1));
   }

   public void method4888() {
      this.bek = this.a(40);
      this.beK.a(this.beJ, (byte)10);
      this.beJ.N().n();
   }

   public void method4889() {
      this.bek = 0;
   }

   public boolean method4890() {
      return this.bek > 0;
   }

   public int OX() {
      return this.bek;
   }

   public void method4891() {
      this.bek = Math.max(0, this.bek - 1);
      if (this.bek == this.a(4)) {
         BlockPosition var1 = this.beJ.do_();
         IBlockData var2 = this.beK.a_(var1);
         if (beh.test(var2)) {
            if (CraftEventFactory.callEntityChangeBlockEvent(this.beJ, var1, var2.u().g(), !this.beK.ab().b(GameRules.c))) {
               this.beK.b(var1, false);
            }

            this.beJ.Q();
         } else {
            BlockPosition var3 = var1.e();
            if (this.beK.a_(var3).a(Blocks.i)) {
               if (CraftEventFactory.callEntityChangeBlockEvent(this.beJ, var3, Blocks.j.o(), !this.beK.ab().b(GameRules.c))) {
                  this.beK.c(2001, var3, Block.i(Blocks.i.o()));
                  this.beK.a(var3, Blocks.j.o(), 2);
               }

               this.beJ.Q();
            }
         }
      }
   }
}
