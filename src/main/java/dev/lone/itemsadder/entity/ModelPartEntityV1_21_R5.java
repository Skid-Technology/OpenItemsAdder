package dev.lone.itemsadder.entity;

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
import org.bukkit.craftbukkit.v1_21_R5.CraftServer;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.jetbrains.annotations.Nullable;

final class ModelPartEntityV1_21_R5 extends EntityLiving implements ModelPartEntity {
   private final EntityLiving aXE;
   private final boolean aXF;
   private final float aXG;
   private final DoubleConsumer aXH;
   private CraftLivingEntity aXC;
   private float xS = 0.01F;
   private float ut = 0.01F;
   private boolean broken;
   private boolean aWJ;

   ModelPartEntityV1_21_R5(LivingEntity var1, @Nullable Float var2, float var3, DoubleConsumer var4) {
      super(((CraftLivingEntity)var1).getHandle().ap(), ((CraftLivingEntity)var1).getHandle().ai());
      this.aXE = ((CraftLivingEntity)var1).getHandle();
      this.aXF = var2 != null;
      this.aXG = var3;
      this.aXH = var4;
      if (var2 != null) {
         this.h(GenericAttributes.t).a(var2.floatValue());
         this.x(var2);
      }

      this.g(true);
      this.aq = true;
      this.f(true);
      this.l(true);
      this.OB().setPersistent(false);
   }

   public CraftLivingEntity OB() {
      if (this.aXC == null) {
         this.aXC = (CraftLivingEntity)ModelPartTypeSpoofer.spoof(new ModelPartCraftEntityV1_21_R5(this, (CraftServer)Bukkit.getServer(), this));
      }

      return this.aXC;
   }

   @Override
   public LivingEntity getLivingEntity() {
      return this.OB();
   }

   @Override
   public boolean isBroken() {
      return ((WorldServer)this.ai()).b(this);
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

   public boolean dX() {
      return false;
   }

   public EnumMainHand fF() {
      return EnumMainHand.b;
   }

   @Override
   public void a_(double x, double y, double z, float width, float height) {
      if (!this.dU()) {
         if (this.xS != width || this.ut != height) {
            this.xS = width;
            this.ut = height;
            this.j_();
         }

         if (this.dC() != x || this.dE() != y || this.dI() != z) {
            this.a_(x, y, z);
         }
      }
   }

   protected EntitySize method3354(EntityPose pose) {
      float var2 = this.eq();
      return EntitySize.b(Math.max(0.01F, this.xS) / var2, Math.max(0.01F, this.ut) / var2);
   }

   public void method3355() {
      if (!this.aXE.dU() && this.aXE.bO()) {
         this.az();
      } else {
         this.at();
      }
   }

   public boolean method3356(WorldServer level, DamageSource source, float amount) {
      if (this.broken || this.aXE.dU() || this.aXE.a(level, source)) {
         return false;
      }

      if (!this.aXF) {
         boolean var16 = this.aWJ;
         this.aWJ = true;

         try {
            return this.aXE.a(level, source, amount * this.aXG);
         } finally {
            this.aWJ = var16;
         }
      } else {
         if (!super.a(level, source, amount)) {
            return false;
         }

         if (!this.broken) {
            this.aXH.accept(this.eL());
         }

         if (this.aXG > 0.0F) {
            boolean var4 = this.aWJ;
            this.aWJ = true;

            try {
               this.aXE.a(level, source, amount * this.aXG);
            } finally {
               this.aWJ = var4;
            }
         }

         if (source.d() instanceof EntityLiving var14) {
            this.aXE.a(var14);
         }

         if (source.d() instanceof EntityHuman var15) {
            this.aXE.a(var15, 100);
         }

         return true;
      }
   }

   public void method3357(DamageSource source) {
      if (!this.broken) {
         this.broken = true;
         this.at();
         this.aXH.accept(0.0);
      }
   }

   public float eL() {
      return this.aXE != null && !this.aXF ? this.aXE.eL() : super.eL();
   }

   public AttributeMapBase fg() {
      return this.aXE != null && !this.aXF ? this.aXE.fg() : super.fg();
   }

   public ItemStack method3358(EnumItemSlot slot) {
      return this.aXE != null && !this.aXF ? this.aXE.a(slot) : super.a(slot);
   }

   public boolean aK() {
      return this.aXE != null && !this.aXF ? this.aXE.aK() : super.aK();
   }

   public Vec3D dA() {
      return this.aXE != null && !this.aXF ? this.aXE.dA() : super.dA();
   }

   public void method3359(Vec3D movement) {
      if (this.aXE != null && !this.aXF) {
         this.aXE.i(movement);
      } else {
         super.i(movement);
      }
   }

   public boolean addEffect(MobEffect effect, @Nullable Entity source, Cause cause) {
      return this.aXE != null && !this.aXF ? this.aXE.addEffect(effect, source, cause) : super.addEffect(effect, source, cause);
   }

   public void method3360(int ticks) {
      if (this.aXE != null && !this.aXF) {
         this.aXE.g(ticks);
      } else {
         super.g(ticks);
      }
   }

   public void method3361(int ticks) {
      if (this.aXE != null && !this.aXF) {
         this.aXE.h(ticks);
      } else {
         super.h(ticks);
      }
   }
}
