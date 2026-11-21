package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.nms.MobGoalFactoryImplV1_21_R2;
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
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftMob;
import org.bukkit.craftbukkit.v1_21_R2.event.CraftEventFactory;
import org.bukkit.entity.Mob;

class BlockInteractionGoalV1_21_R2 extends PathfinderGoal {
   private static final int bgc = 40;
   private static Predicate beh;
   private final EntityInsentient bgd;
   private final World bge;
   private int bek;

   BlockInteractionGoalV1_21_R2(MobGoalFactoryImplV1_21_R2 var1, Mob var2, Predicate var3) {
      this.bgf = var2;
      this.bgg = var3;
      this.bgd = ((CraftMob)this.bgf).getHandle();
      this.bge = this.bgd.dW();
      if (this.bgg == null) {
         beh = var0 -> false;
      } else {
         beh = var1x -> {
            BlockState var2x = var1x.createCraftBlockData().createBlockState();
            return var0.test(var2x);
         };
      }
   }

   public boolean method4978() {
      if (!((WorldServer)this.bge).J.a.b(this.bgd.dy())) {
         return false;
      }

      if (this.bgd.dZ().a(this.bgd.e_() ? 50 : 1000) != 0) {
         return false;
      }

      BlockPosition var1 = this.bgd.dw();
      return beh.test(this.bge.a_(var1));
   }

   public void method4979() {
      this.bek = this.a(40);
      this.bge.a(this.bgd, (byte)10);
      this.bgd.L().o();
   }

   public void method4980() {
      this.bek = 0;
   }

   public boolean method4981() {
      return this.bek > 0;
   }

   public int OX() {
      return this.bek;
   }

   public void method4982() {
      this.bek = Math.max(0, this.bek - 1);
      if (this.bek == this.a(4)) {
         BlockPosition var1 = this.bgd.dw();
         IBlockData var2 = this.bge.a_(var1);
         if (beh.test(var2)) {
            if (CraftEventFactory.callEntityChangeBlockEvent(this.bgd, var1, var2.y().g(), !a(this.bge).N().b(GameRules.c))) {
               this.bge.b(var1, false);
            }

            this.bgd.P();
         } else {
            BlockPosition var3 = var1.e();
            if (this.bge.a_(var3).a(Blocks.i)) {
               if (CraftEventFactory.callEntityChangeBlockEvent(this.bgd, var3, Blocks.j.m(), !a(this.bge).N().b(GameRules.c))) {
                  this.bge.c(2001, var3, Block.j(Blocks.i.m()));
                  this.bge.a(var3, Blocks.j.m(), 2);
               }

               this.bgd.P();
            }
         }
      }
   }
}
