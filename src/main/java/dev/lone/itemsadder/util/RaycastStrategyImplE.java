package dev.lone.itemsadder.util;

import java.util.HashSet;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.BlockPosition.MutableBlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.RayTrace;
import net.minecraft.world.level.World;
import net.minecraft.world.level.RayTrace.BlockCollisionOption;
import net.minecraft.world.level.RayTrace.FluidCollisionOption;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.phys.MovingObjectPosition.EnumMovingObjectType;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class RaycastStrategyImplE implements RaycastStrategy {
   public RaycastResult traceNmsBlocks(Entity var1, BlockPosition var2, BlockPosition var3, HashSet var4) {
      boolean var5 = true;
      World var6 = var1.dW();
      MutableBlockPosition var7 = new MutableBlockPosition();
      if (var6.a(var2, var3)) {
         for (int var8 = var2.u(); var8 <= var3.u(); var8++) {
            for (int var9 = var2.v(); var9 <= var3.v(); var9++) {
               for (int var10 = var2.w(); var10 <= var3.w(); var10++) {
                  var7.d(var8, var9, var10);
                  IBlockData var11 = var6.a_(var7);
                  if (!var11.l()) {
                     var5 = false;
                     if (var4.contains(var11.b())) {
                        return RaycastResult.MATCHED_BLOCK;
                     }
                  }
               }
            }
         }
      }

      return var5 ? RaycastResult.NOTHING : RaycastResult.ANOTHER_BLOCK;
   }

   @Override
   public RaycastResult traceEntity(org.bukkit.entity.Entity bukkitEntity, HashSet nmsBlockTypes) {
      Entity var3 = ((CraftEntity)bukkitEntity).getHandle();
      AxisAlignedBB var4 = var3.cR();
      BlockPosition var5 = new BlockPosition(MathHelper.a(var4.a - 0.001), MathHelper.a(var4.b - 0.001), MathHelper.a(var4.c - 0.001));
      BlockPosition var6 = new BlockPosition(MathHelper.a(var4.d + 0.001), MathHelper.a(var4.e + 0.001), MathHelper.a(var4.f + 0.001));
      return this.traceNmsBlocks(var3, var5, var6, nmsBlockTypes);
   }

   @Nullable
   @Override
   public Vector traceVector(Location a, Location b, boolean ignoreNonCollidable) {
      CraftWorld var4 = (CraftWorld)a.getWorld();
      WorldServer var5 = var4.getHandle();
      MovingObjectPositionBlock var6 = var5.a(
         new RayTrace(
            new Vec3D(a.getX(), a.getY(), a.getZ()),
            new Vec3D(b.getX(), b.getY(), b.getZ()),
            ignoreNonCollidable ? BlockCollisionOption.a : BlockCollisionOption.b,
            FluidCollisionOption.c,
            (Entity)null
         )
      );
      return var6.d() == EnumMovingObjectType.a ? null : new Vector(var6.g().d, var6.g().e, var6.g().f);
   }
}
