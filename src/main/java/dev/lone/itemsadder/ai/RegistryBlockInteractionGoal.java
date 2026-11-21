package dev.lone.itemsadder.ai;

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
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftMob;
import org.bukkit.craftbukkit.v1_20_R4.event.CraftEventFactory;
import org.bukkit.entity.Mob;

class RegistryBlockInteractionGoal extends PathfinderGoal {
   private static final int beg = 40;
   private static Predicate beh;
   private final EntityInsentient bei;
   private final World bej;
   private int bek;

   RegistryBlockInteractionGoal(MobGoalRegistry var1, Mob var2, Predicate var3) {
      this.bel = var2;
      this.bem = var3;
      this.bei = ((CraftMob)this.bel).getHandle();
      this.bej = this.bei.dP();
      if (this.bem == null) {
         beh = var0 -> false;
      } else {
         beh = var1x -> {
            BlockState var2x = var1x.createCraftBlockData().createBlockState();
            return var0.test(var2x);
         };
      }
   }

   public boolean method4857() {
      if (!((WorldServer)this.bej).I.a.c(this.bei.dr())) {
         return false;
      }

      if (this.bei.el().a(this.bei.p_() ? 50 : 1000) != 0) {
         return false;
      }

      BlockPosition var1 = this.bei.dp();
      return beh.test(this.bej.a_(var1));
   }

   public void method4858() {
      this.bek = this.a(40);
      this.bej.a(this.bei, (byte)10);
      this.bei.K().n();
   }

   public void method4859() {
      this.bek = 0;
   }

   public boolean method4860() {
      return this.bek > 0;
   }

   public int OX() {
      return this.bek;
   }

   public void method4861() {
      this.bek = Math.max(0, this.bek - 1);
      if (this.bek == this.a(4)) {
         BlockPosition var1 = this.bei.dp();
         IBlockData var2 = this.bej.a_(var1);
         if (beh.test(var2)) {
            if (CraftEventFactory.callEntityChangeBlockEvent(this.bei, var1, var2.u().g(), !this.bej.ab().b(GameRules.c))) {
               this.bej.b(var1, false);
            }

            this.bei.O();
         } else {
            BlockPosition var3 = var1.d();
            if (this.bej.a_(var3).a(Blocks.i)) {
               if (CraftEventFactory.callEntityChangeBlockEvent(this.bei, var3, Blocks.j.o(), !this.bej.ab().b(GameRules.c))) {
                  this.bej.c(2001, var3, Block.i(Blocks.i.o()));
                  this.bej.a(var3, Blocks.j.o(), 2);
               }

               this.bei.O();
            }
         }
      }
   }
}
