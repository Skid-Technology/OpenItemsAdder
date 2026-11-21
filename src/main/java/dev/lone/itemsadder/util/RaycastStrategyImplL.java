package dev.lone.itemsadder.util;

import java.util.HashSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class RaycastStrategyImplL implements RaycastStrategy {
   public RaycastResult traceNmsBlocks(Entity var1, net.minecraft.core.BlockPos var2, net.minecraft.core.BlockPos var3, HashSet var4) {
      boolean var5 = true;
      Level var6 = var1.level();
      net.minecraft.core.BlockPos.MutableBlockPos var7 = new net.minecraft.core.BlockPos.MutableBlockPos();
      if (var6.hasChunksAt(var2, var3)) {
         for (int var8 = var2.getX(); var8 <= var3.getX(); var8++) {
            for (int var9 = var2.getY(); var9 <= var3.getY(); var9++) {
               for (int var10 = var2.getZ(); var10 <= var3.getZ(); var10++) {
                  var7.set(var8, var9, var10);
                  BlockState var11 = var6.getBlockState(var7);
                  if (!var11.isAir()) {
                     var5 = false;
                     if (var4.contains(var11.getBlock())) {
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
      AABB var4 = var3.getBoundingBox();
      net.minecraft.core.BlockPos var5 = new net.minecraft.core.BlockPos(
         Mth.floor(var4.minX - 0.001), Mth.floor(var4.minY - 0.001), Mth.floor(var4.minZ - 0.001)
      );
      net.minecraft.core.BlockPos var6 = new net.minecraft.core.BlockPos(
         Mth.floor(var4.maxX + 0.001), Mth.floor(var4.maxY + 0.001), Mth.floor(var4.maxZ + 0.001)
      );
      return this.traceNmsBlocks(var3, var5, var6, nmsBlockTypes);
   }

   @Nullable
   @Override
   public Vector traceVector(Location a, Location b, boolean ignoreNonCollidable) {
      CraftWorld var4 = (CraftWorld)a.getWorld();
      ServerLevel var5 = var4.getHandle();
      BlockHitResult var6 = var5.clip(
         new ClipContext(
            new Vec3(a.getX(), a.getY(), a.getZ()),
            new Vec3(b.getX(), b.getY(), b.getZ()),
            ignoreNonCollidable ? Block.COLLIDER : Block.OUTLINE,
            Fluid.ANY,
            (Entity)null
         )
      );
      return var6.getType() == Type.MISS ? null : new Vector(var6.getLocation().x, var6.getLocation().y, var6.getLocation().z);
   }
}
