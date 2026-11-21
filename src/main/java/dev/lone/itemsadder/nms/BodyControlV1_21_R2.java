package dev.lone.itemsadder.nms;

import dev.lone.itemsadder.entity.BodyRotationController;
import dev.lone.itemsadder.entity.EntityFieldSwapper;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.MarkerAnnotation;
import dev.lone.itemsadder.util.ServerCompat;
import java.lang.reflect.Field;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.control.EntityAIBodyControl;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftMob;
import org.bukkit.entity.LivingEntity;

public class BodyControlV1_21_R2 extends EntityAIBodyControl implements BodyRotationController {
   private final float aPa;
   private EntityInsentient aOP;
   Field aOQ = ServerCompat.findFieldByType(EntityInsentient.class, EntityAIBodyControl.class);
   Field aOR = ServerCompat.findFieldByType(EntityAIBodyControl.class, EntityInsentient.class);
   public float aOS;
   public float aOT;
   private float aOU;
   private float aOV;
   private int aOW = 0;

   public BodyControlV1_21_R2(LivingEntity var1, float var2) {
      super(null);
      if (!(var1 instanceof CraftMob)) {
         throw new IllegalArgumentException("bukkitEntity must be an instance of CraftMob");
      }

      this.aOP = ((CraftMob)var1).getHandle();
      this.aOU = this.aOP.aX;
      this.aOV = this.aOP.aZ;
      this.aOS = this.aOU;
      this.aOT = this.aOV;
      this.aPa = var2;
      EntityFieldSwapper.swapFields(var1, this.aOR, this.aOQ, this, this.aOP);
   }

   @Override
   public boolean matchesDup2(LivingEntity bukkitEntity) {
      if (!(bukkitEntity instanceof CraftMob)) {
         return false;
      }

      this.aOP = ((CraftMob)bukkitEntity).getHandle();
      EntityFieldSwapper.swapFields(bukkitEntity, this.aOR, this.aOQ, new EntityAIBodyControl(this.aOP), this.aOP);
      return true;
   }

   @MarkerAnnotation
   public void method2752() {
      float var1 = this.aOP.dM();
      float var2 = this.aOP.aZ;
      boolean var3 = this.iq();
      if (var3) {
         this.aOW = 0;
         this.aOU = BlockFaceUtils.lerpYaw(this.aOU, var1, 0.2F);
         this.aOV = BlockFaceUtils.lerpYaw(this.aOV, var2, 0.5F);
         this.aOS = this.aOU;
      } else {
         float var4 = BlockFaceUtils.normalizeYaw(var2 - this.aOU);
         if (Math.abs(var4) > this.aPa) {
            this.aOW++;
            if (this.aOW > 10) {
               this.aOU = BlockFaceUtils.lerpYaw(this.aOU, var2, 0.1F);
               this.aOS = this.aOU;
            }
         } else {
            this.aOW = 0;
         }

         float var5 = BlockFaceUtils.normalizeYaw(var2 - this.aOS);
         var5 = MathHelper.a(var5, -this.aPa, this.aPa);
         float var6 = this.aOS + var5;
         this.aOV = BlockFaceUtils.lerpYaw(this.aOV, var6, 0.3F);
      }

      this.aOT = this.aOV;
      this.aOP.aZ = this.aOT;
      this.aOP.ba = this.aOT;
      this.aOP.aX = this.aOS;
      this.aOP.aY = this.aOS;
   }

   @Override
   public boolean iq() {
      double var1 = this.aOP.dB() - this.aOP.K;
      double var3 = this.aOP.dH() - this.aOP.M;
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
