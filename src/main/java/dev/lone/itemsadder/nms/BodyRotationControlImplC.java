package dev.lone.itemsadder.nms;

import dev.lone.itemsadder.entity.BodyRotationController;
import dev.lone.itemsadder.entity.EntityFieldSwapper;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.MarkerAnnotation;
import dev.lone.itemsadder.util.ServerCompat;
import java.lang.reflect.Field;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftMob;
import org.bukkit.entity.LivingEntity;

public class BodyRotationControlImplC extends BodyRotationControl implements BodyRotationController {
   private final float aPj;
   private Mob aPh;
   Field aOQ = ServerCompat.findFieldByType(Mob.class, BodyRotationControl.class);
   Field aOR = ServerCompat.findFieldByType(BodyRotationControl.class, Mob.class);
   public float aOS;
   public float aOT;
   private float aOU;
   private float aOV;
   private int aOW = 0;

   public BodyRotationControlImplC(LivingEntity var1, float var2) {
      super(null);
      if (!(var1 instanceof CraftMob)) {
         throw new IllegalArgumentException("bukkitEntity must be an instance of CraftMob");
      }

      this.aPh = ((CraftMob)var1).getHandle();
      this.aOU = this.aPh.yBodyRot;
      this.aOV = this.aPh.yHeadRot;
      this.aOS = this.aOU;
      this.aOT = this.aOV;
      this.aPj = var2;
      EntityFieldSwapper.swapFields(var1, this.aOR, this.aOQ, this, this.aPh);
   }

   @Override
   public boolean matchesDup2(LivingEntity bukkitEntity) {
      if (!(bukkitEntity instanceof CraftMob)) {
         return false;
      }

      this.aPh = ((CraftMob)bukkitEntity).getHandle();
      EntityFieldSwapper.swapFields(bukkitEntity, this.aOR, this.aOQ, new BodyRotationControl(this.aPh), this.aPh);
      return true;
   }

   @MarkerAnnotation
   public void clientTick() {
      float var1 = this.aPh.getYRot();
      float var2 = this.aPh.yHeadRot;
      boolean var3 = this.iq();
      if (var3) {
         this.aOW = 0;
         this.aOU = BlockFaceUtils.lerpYaw(this.aOU, var1, 0.2F);
         this.aOV = BlockFaceUtils.lerpYaw(this.aOV, var2, 0.5F);
         this.aOS = this.aOU;
      } else {
         float var4 = BlockFaceUtils.normalizeYaw(var2 - this.aOU);
         if (Math.abs(var4) > this.aPj) {
            this.aOW++;
            if (this.aOW > 10) {
               this.aOU = BlockFaceUtils.lerpYaw(this.aOU, var2, 0.1F);
               this.aOS = this.aOU;
            }
         } else {
            this.aOW = 0;
         }

         float var5 = BlockFaceUtils.normalizeYaw(var2 - this.aOS);
         var5 = Mth.clamp(var5, -this.aPj, this.aPj);
         float var6 = this.aOS + var5;
         this.aOV = BlockFaceUtils.lerpYaw(this.aOV, var6, 0.3F);
      }

      this.aOT = this.aOV;
      this.aPh.yHeadRot = this.aOT;
      this.aPh.yHeadRotO = this.aOT;
      this.aPh.yBodyRot = this.aOS;
      this.aPh.yBodyRotO = this.aOS;
   }

   @Override
   public boolean iq() {
      double var1 = this.aPh.getX() - this.aPh.xo;
      double var3 = this.aPh.getZ() - this.aPh.zo;
      return var1 * var1 + var3 * var3 > 2.5000003E-7F;
   }

   @Override
   public float Nn() {
      return this.aOT;
   }

   @Override
   public float No() {
      return this.aOS;
   }
}
