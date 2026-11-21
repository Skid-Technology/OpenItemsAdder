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
import org.bukkit.craftbukkit.v1_21_R6.CraftServer;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.jetbrains.annotations.Nullable;

final class ModelPartEntityV1_21_R6 extends EntityLiving implements ModelPartEntity {
   private final EntityLiving aWS;
   private final boolean aWT;
   private final float aWU;
   private final DoubleConsumer aWV;
   private CraftLivingEntity aWW;
   private float xS = 0.01F;
   private float ut = 0.01F;
   private boolean broken;
   private boolean aWJ;

   ModelPartEntityV1_21_R6(LivingEntity var1, @Nullable Float var2, float var3, DoubleConsumer var4) {
      super(((CraftLivingEntity)var1).getHandle().ax(), ((CraftLivingEntity)var1).getHandle().an());
      this.aWS = ((CraftLivingEntity)var1).getHandle();
      this.aWT = var2 != null;
      this.aWU = var3;
      this.aWV = var4;
      if (var2 != null) {
         this.h(GenericAttributes.t).a(var2.floatValue());
         this.x(var2);
      }

      this.g(true);
      this.ar = true;
      this.f(true);
      this.l(true);
      this.Ow().setPersistent(false);
   }

   public CraftLivingEntity Ow() {
      if (this.aWW == null) {
         this.aWW = (CraftLivingEntity)ModelPartTypeSpoofer.spoof(new LivingEquipmentV1_21_R6(this, (CraftServer)Bukkit.getServer(), this));
      }

      return this.aWW;
   }

   @Override
   public LivingEntity getLivingEntity() {
      return this.Ow();
   }

   @Override
   public boolean isBroken() {
      return ((WorldServer)this.an()).b(this);
   }

   @Override
   public void remove() {
      this.aB();
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

   public boolean ef() {
      return false;
   }

   public EnumMainHand as() {
      return EnumMainHand.b;
   }

   @Override
   public void a_(double x, double y, double z, float width, float height) {
      if (!this.ec()) {
         if (this.xS != width || this.ut != height) {
            this.xS = width;
            this.ut = height;
            this.l_();
         }

         if (this.dK() != x || this.dM() != y || this.dQ() != z) {
            this.a_(x, y, z);
         }
      }
   }

   protected EntitySize method3367(EntityPose pose) {
      float var2 = this.eA();
      return EntitySize.b(Math.max(0.01F, this.xS) / var2, Math.max(0.01F, this.ut) / var2);
   }

   public void method3355() {
      if (!this.aWS.ec() && this.aWS.bX()) {
         this.aH();
      } else {
         this.aB();
      }
   }

   public boolean method3356(WorldServer level, DamageSource source, float amount) {
      if (this.broken || this.aWS.ec() || this.aWS.a(level, source)) {
         return false;
      }

      if (!this.aWT) {
         boolean var16 = this.aWJ;
         this.aWJ = true;

         try {
            return this.aWS.a(level, source, amount * this.aWU);
         } finally {
            this.aWJ = var16;
         }
      } else {
         if (!super.a(level, source, amount)) {
            return false;
         }

         if (!this.broken) {
            this.aWV.accept(this.eU());
         }

         if (this.aWU > 0.0F) {
            boolean var4 = this.aWJ;
            this.aWJ = true;

            try {
               this.aWS.a(level, source, amount * this.aWU);
            } finally {
               this.aWJ = var4;
            }
         }

         if (source.d() instanceof EntityLiving var14) {
            this.aWS.a(var14);
         }

         if (source.d() instanceof EntityHuman var15) {
            this.aWS.a(var15, 100);
         }

         return true;
      }
   }

   public void method3357(DamageSource source) {
      if (!this.broken) {
         this.broken = true;
         this.aB();
         this.aWV.accept(0.0);
      }
   }

   public float eU() {
      return this.aWS != null && !this.aWT ? this.aWS.eU() : super.eU();
   }

   public AttributeMapBase fp() {
      return this.aWS != null && !this.aWT ? this.aWS.fp() : super.fp();
   }

   public ItemStack method3358(EnumItemSlot slot) {
      return this.aWS != null && !this.aWT ? this.aWS.a(slot) : super.a(slot);
   }

   public boolean aS() {
      return this.aWS != null && !this.aWT ? this.aWS.aS() : super.aS();
   }

   public Vec3D dI() {
      return this.aWS != null && !this.aWT ? this.aWS.dI() : super.dI();
   }

   public void method3368(Vec3D movement) {
      if (this.aWS != null && !this.aWT) {
         this.aWS.k(movement);
      } else {
         super.k(movement);
      }
   }

   public boolean addEffect(MobEffect effect, @Nullable Entity source, Cause cause) {
      return this.aWS != null && !this.aWT ? this.aWS.addEffect(effect, source, cause) : super.addEffect(effect, source, cause);
   }

   public void method3360(int ticks) {
      if (this.aWS != null && !this.aWT) {
         this.aWS.g(ticks);
      } else {
         super.g(ticks);
      }
   }

   public void method3361(int ticks) {
      if (this.aWS != null && !this.aWT) {
         this.aWS.h(ticks);
      } else {
         super.h(ticks);
      }
   }
}
