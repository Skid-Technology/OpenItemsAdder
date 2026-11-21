package dev.lone.itemsadder.nms;

import dev.lone.itemsadder.entity.ModelPartEntity;
import dev.lone.itemsadder.entity.ModelPartTypeSpoofer;
import java.util.function.DoubleConsumer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EntitySize;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.EnumMainHand;
import net.minecraft.world.entity.ai.attributes.AttributeMapBase;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R7.CraftServer;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.jetbrains.annotations.Nullable;

final class ModelPartEntityV1_21_R7 extends EntityLiving implements ModelPartEntity {
   private final EntityLiving aWY;
   private final boolean aWZ;
   private final float aXa;
   private final DoubleConsumer aXb;
   private CraftLivingEntity aXc;
   private float xS = 0.01F;
   private float ut = 0.01F;
   private boolean broken;
   private boolean aWJ;

   ModelPartEntityV1_21_R7(LivingEntity var1, @Nullable Float var2, float var3, DoubleConsumer var4) {
      super(((CraftLivingEntity)var1).getHandle().ay(), ((CraftLivingEntity)var1).getHandle().ao());
      this.aWY = ((CraftLivingEntity)var1).getHandle();
      this.aWZ = var2 != null;
      this.aXa = var3;
      this.aXb = var4;
      if (var2 != null) {
         this.h(GenericAttributes.u).a(var2.floatValue());
         this.x(var2);
      }

      this.g(true);
      this.ar = true;
      this.f(true);
      this.l(true);
      this.Ox().setPersistent(false);
   }

   public CraftLivingEntity Ox() {
      if (this.aXc == null) {
         this.aXc = (CraftLivingEntity)ModelPartTypeSpoofer.spoof(new LivingEquipmentV1_21_R7(this, (CraftServer)Bukkit.getServer(), this));
      }

      return this.aXc;
   }

   @Override
   public LivingEntity getLivingEntity() {
      return this.Ox();
   }

   @Override
   public boolean isBroken() {
      return ((WorldServer)this.ao()).b(this);
   }

   @Override
   public void remove() {
      this.aC();
   }

   @Override
   public boolean isForwardingDamage() {
      return this.aWJ;
   }

   public boolean method3353(EntityPlayer player) {
      return false;
   }

   public void refreshEntityData(EntityPlayer player) {
   }

   public boolean ek() {
      return false;
   }

   public EnumMainHand at() {
      return EnumMainHand.b;
   }

   @Override
   public void a_(double x, double y, double z, float width, float height) {
      if (!this.eh()) {
         if (this.xS != width || this.ut != height) {
            this.xS = width;
            this.ut = height;
            this.j_();
         }

         if (this.dP() != x || this.dR() != y || this.dV() != z) {
            this.a_(x, y, z);
         }
      }
   }

   protected EntitySize method3367(EntityPose pose) {
      float var2 = this.eF();
      return EntitySize.b(Math.max(0.01F, this.xS) / var2, Math.max(0.01F, this.ut) / var2);
   }

   public void method3355() {
      if (!this.aWY.eh() && this.aWY.cb()) {
         this.aJ();
      } else {
         this.aC();
      }
   }

   public boolean method3356(WorldServer level, DamageSource source, float amount) {
      if (this.broken || this.aWY.eh() || this.aWY.a(level, source)) {
         return false;
      }

      if (!this.aWZ) {
         boolean var16 = this.aWJ;
         this.aWJ = true;

         try {
            return this.aWY.a(level, source, amount * this.aXa);
         } finally {
            this.aWJ = var16;
         }
      } else {
         if (!super.a(level, source, amount)) {
            return false;
         }

         if (!this.broken) {
            this.aXb.accept(this.eZ());
         }

         if (this.aXa > 0.0F) {
            boolean var4 = this.aWJ;
            this.aWJ = true;

            try {
               this.aWY.a(level, source, amount * this.aXa);
            } finally {
               this.aWJ = var4;
            }
         }

         if (source.getEntity() instanceof EntityLiving var14) {
            this.aWY.a(var14);
         }

         if (source.getEntity() instanceof EntityHuman var15) {
            this.aWY.a(var15, 100);
         }

         return true;
      }
   }

   public void method3357(DamageSource source) {
      if (!this.broken) {
         this.broken = true;
         this.aC();
         this.aXb.accept(0.0);
      }
   }

   public float eZ() {
      return this.aWY != null && !this.aWZ ? this.aWY.eZ() : super.eZ();
   }

   public AttributeMapBase fw() {
      return this.aWY != null && !this.aWZ ? this.aWY.fw() : super.fw();
   }

   public ItemStack method3358(EnumItemSlot slot) {
      return this.aWY != null && !this.aWZ ? this.aWY.a(slot) : super.a(slot);
   }

   public boolean aV() {
      return this.aWY != null && !this.aWZ ? this.aWY.aV() : super.aV();
   }

   public Vec3D dN() {
      return this.aWY != null && !this.aWZ ? this.aWY.dN() : super.dN();
   }

   public void method3368(Vec3D movement) {
      if (this.aWY != null && !this.aWZ) {
         this.aWY.k(movement);
      } else {
         super.k(movement);
      }
   }

   public boolean addEffect(MobEffect effect, @Nullable Entity source, Cause cause) {
      return this.aWY != null && !this.aWZ ? this.aWY.addEffect(effect, source, cause) : super.addEffect(effect, source, cause);
   }

   public void method3360(int ticks) {
      if (this.aWY != null && !this.aWZ) {
         this.aWY.g(ticks);
      } else {
         super.g(ticks);
      }
   }

   public void method3361(int ticks) {
      if (this.aWY != null && !this.aWZ) {
         this.aWY.h(ticks);
      } else {
         super.h(ticks);
      }
   }
}
