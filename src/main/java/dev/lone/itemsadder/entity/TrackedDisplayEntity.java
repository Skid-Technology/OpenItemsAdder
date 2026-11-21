package dev.lone.itemsadder.entity;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Entity;
import org.joml.Matrix4fc;
import org.joml.Quaterniond;

public final class TrackedDisplayEntity implements AutoCloseable {
   private final Entity auxx;
   private volatile DisplayPoseSnapshot auy;
   private volatile boolean cz;

   public TrackedDisplayEntity(Entity var1) {
      this.auxx = Objects.requireNonNull(var1);
   }

   public Entity getEntity() {
      return this.auxx;
   }

   public DisplayPoseSnapshot getSnapshot() {
      return this.cz ? null : this.auy;
   }

   public boolean isActive() {
      return this.cz;
   }

   public void update(UUID var1, double var2, double var4, double var6, Matrix4fc var8, Set var9) {
      if (!this.cz) {
         TransformDecomposition var10 = TransformDecomposition.decompose(var8);
         Quaterniond var11 = new Quaterniond(var10.getRotation()).mul(new Quaterniond(var10.mA())).normalize();
         this.auy = new DisplayPoseSnapshot(var1, var2 + var8.m30(), var4 + var8.m31(), var6 + var8.m32(), var11.x, var11.y, var11.z, var11.w, var9);
      }
   }

   @Override
   public void close() {
      this.cz = true;
      this.auy = null;
   }
}
