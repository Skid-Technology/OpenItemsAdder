package dev.lone.itemsadder.nms;

import dev.lone.itemsadder.entity.ModelPartEntity;
import dev.lone.itemsadder.entity.ModelPartTypeSpoofer;
import java.util.EnumMap;
import java.util.List;
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
import org.bukkit.craftbukkit.v1_21_R2.CraftServer;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.jetbrains.annotations.Nullable;

final class ModelPartEntityV1_21_R2 extends EntityLiving implements ModelPartEntity {
   private final EntityLiving aXe;
   private final boolean aXf;
   private final float aXg;
   private final DoubleConsumer aXh;
   private CraftLivingEntity aXi;
   private final EnumMap aXj = new EnumMap(EnumItemSlot.class);
   private float xS = 0.01F;
   private float ut = 0.01F;
   private boolean broken;
   private boolean aWJ;

   ModelPartEntityV1_21_R2(LivingEntity var1, @Nullable Float var2, float var3, DoubleConsumer var4) {
      super(((CraftLivingEntity)var1).getHandle().aq(), ((CraftLivingEntity)var1).getHandle().dW());
      this.aXe = ((CraftLivingEntity)var1).getHandle();
      this.aXf = var2 != null;
      this.aXg = var3;
      this.aXh = var4;
      if (var2 != null) {
         this.g(GenericAttributes.s).a(var2.floatValue());
         this.x(var2);
      }

      this.f(true);
      this.ad = true;
      this.e(true);
      this.k(true);
      this.Oy().setPersistent(false);
   }

   public CraftLivingEntity Oy() {
      if (this.aXi == null) {
         this.aXi = (CraftLivingEntity)ModelPartTypeSpoofer.spoof(new LivingEquipmentV1_21_R2(this, (CraftServer)Bukkit.getServer(), this));
      }

      return this.aXi;
   }

   @Override
   public LivingEntity getLivingEntity() {
      return this.Oy();
   }

   @Override
   public boolean isBroken() {
      return ((WorldServer)this.dW()).b(this);
   }

   @Override
   public void remove() {
      this.at();
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

   public boolean dU() {
      return false;
   }

   public EnumMainHand fy() {
      return EnumMainHand.b;
   }

   @Override
   public void a_(double x, double y, double z, float width, float height) {
      if (!this.dR()) {
         if (this.xS != width || this.ut != height) {
            this.xS = width;
            this.ut = height;
            this.m_();
         }

         if (this.dB() != x || this.dD() != y || this.dH() != z) {
            this.a_(x, y, z);
         }
      }
   }

   protected EntitySize method3354(EntityPose pose) {
      float var2 = this.ek();
      return EntitySize.b(Math.max(0.01F, this.xS) / var2, Math.max(0.01F, this.ut) / var2);
   }

   public void method3369() {
      if (!this.aXe.dR() && this.aXe.bL()) {
         this.az();
      } else {
         this.at();
      }
   }

   public boolean method3356(WorldServer level, DamageSource source, float amount) {
      if (this.broken || this.aXe.dR() || this.aXe.a(level, source)) {
         return false;
      }

      if (!this.aXf) {
         boolean var16 = this.aWJ;
         this.aWJ = true;

         try {
            return this.aXe.a(level, source, amount * this.aXg);
         } finally {
            this.aWJ = var16;
         }
      } else {
         if (!super.a(level, source, amount)) {
            return false;
         }

         if (!this.broken) {
            this.aXh.accept(this.eE());
         }

         if (this.aXg > 0.0F) {
            boolean var4 = this.aWJ;
            this.aWJ = true;

            try {
               this.aXe.a(level, source, amount * this.aXg);
            } finally {
               this.aWJ = var4;
            }
         }

         if (source.d() instanceof EntityLiving var14) {
            this.aXe.a(var14);
         }

         if (source.d() instanceof EntityHuman var15) {
            this.aXe.c(var15);
         }

         return true;
      }
   }

   public void method3357(DamageSource source) {
      if (!this.broken) {
         this.broken = true;
         this.at();
         this.aXh.accept(0.0);
      }
   }

   public float eE() {
      return this.aXe != null && !this.aXf ? this.aXe.eE() : super.eE();
   }

   public AttributeMapBase eY() {
      return this.aXe != null && !this.aXf ? this.aXe.eY() : super.eY();
   }

   public ItemStack method3358(EnumItemSlot slot) {
      return this.aXe != null && !this.aXf ? this.aXe.a(slot) : this.aXj.getOrDefault(slot, ItemStack.j);
   }

   public Iterable fb() {
      return this.aXe != null && !this.aXf
         ? this.aXe.fb()
         : List.of(this.method3358(EnumItemSlot.f), this.method3358(EnumItemSlot.e), this.method3358(EnumItemSlot.d), this.method3358(EnumItemSlot.c));
   }

   public void method3364(EnumItemSlot slot, ItemStack stack) {
      if (this.aXe != null && !this.aXf) {
         this.aXe.a(slot, stack);
      } else {
         this.aXj.put(slot, stack);
      }
   }

   public boolean aJ() {
      return this.aXe != null && !this.aXf ? this.aXe.aJ() : super.aJ();
   }

   public Vec3D dz() {
      return this.aXe != null && !this.aXf ? this.aXe.dz() : super.dz();
   }

   public void method3365(Vec3D movement) {
      if (this.aXe != null && !this.aXf) {
         this.aXe.h(movement);
      } else {
         super.h(movement);
      }
   }

   public boolean addEffect(MobEffect effect, @Nullable Entity source, Cause cause) {
      return this.aXe != null && !this.aXf ? this.aXe.addEffect(effect, source, cause) : super.addEffect(effect, source, cause);
   }

   public void method3360(int ticks) {
      if (this.aXe != null && !this.aXf) {
         this.aXe.g(ticks);
      } else {
         super.g(ticks);
      }
   }

   public void method3361(int ticks) {
      if (this.aXe != null && !this.aXf) {
         this.aXe.h(ticks);
      } else {
         super.h(ticks);
      }
   }
}
